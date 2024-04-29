package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.srt.api.ServerNames;
import net.srt.convert.DataServiceApiAuthConvert;
import net.srt.convert.DataServiceApiConfigConvert;
import net.srt.dao.DataServiceApiAuthDao;
import net.srt.dao.DataServiceApiConfigDao;
import net.srt.dao.DataServiceApiGroupDao;
import net.srt.dto.SqlDto;
import net.srt.entity.DataServiceApiAuthEntity;
import net.srt.entity.DataServiceApiConfigEntity;
import net.srt.entity.DataServiceApiGroupEntity;
import net.srt.framework.common.constant.Constant;
import net.srt.framework.common.exception.ServerException;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.framework.security.user.SecurityUser;
import net.srt.handler.MappingHandlerMapping;
import net.srt.query.DataServiceApiConfigQuery;
import net.srt.service.DataServiceApiConfigService;
import net.srt.vo.DataServiceApiAuthVO;
import net.srt.vo.DataServiceApiConfigVO;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.util.StringUtil;
import srt.cloud.framework.dbswitch.core.model.JdbcSelectResult;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据服务-api配置
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-01-28
 */
@Service
@AllArgsConstructor
public class DataServiceApiConfigServiceImpl extends BaseServiceImpl<DataServiceApiConfigDao, DataServiceApiConfigEntity> implements DataServiceApiConfigService {

	private final DiscoveryClient discoveryClient;
	private final DataServiceApiExecuteServiceImpl apiExecuteService;
	private final MappingHandlerMapping mappingHandlerMapping;
	private final DataServiceApiAuthDao apiAuthDao;
	private final DataServiceApiGroupDao dataServiceApiGroupDao;

	@Override
	public PageResult<DataServiceApiConfigVO> page(DataServiceApiConfigQuery query) {
		IPage<DataServiceApiConfigEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

		return new PageResult<>(DataServiceApiConfigConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
	}

	@Override
	public PageResult<DataServiceApiConfigVO> pageResource(DataServiceApiConfigQuery query) {
		// 查询参数
		Map<String, Object> params = getParams(query);
		// 分页查询
		query.setOrder("dsac.id");
		IPage<DataServiceApiConfigEntity> page = getPage(query);
		params.put(Constant.PAGE, page);
		// 数据列表
		List<DataServiceApiConfigEntity> list = baseMapper.getResourceList(params);
		List<DataServiceApiConfigVO> dataServiceApiConfigVOS = DataServiceApiConfigConvert.INSTANCE.convertList(list);
		for (DataServiceApiConfigVO dataServiceApiConfigVO : dataServiceApiConfigVOS) {
			DataServiceApiGroupEntity groupEntity = dataServiceApiGroupDao.selectById(dataServiceApiConfigVO.getGroupId());
			dataServiceApiConfigVO.setGroup(groupEntity != null ? groupEntity.getPath() : null);
		}
		return new PageResult<>(dataServiceApiConfigVOS, page.getTotal());
	}

	private LambdaQueryWrapper<DataServiceApiConfigEntity> getWrapper(DataServiceApiConfigQuery query) {
		LambdaQueryWrapper<DataServiceApiConfigEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(query.getGroupId() != null, DataServiceApiConfigEntity::getGroupId, query.getGroupId())
				.like(StringUtil.isNotBlank(query.getName()), DataServiceApiConfigEntity::getName, query.getName())
				.like(StringUtil.isNotBlank(query.getPath()), DataServiceApiConfigEntity::getPath, query.getPath())
				.eq(StringUtil.isNotBlank(query.getContentType()), DataServiceApiConfigEntity::getContentType, query.getContentType())
				.eq(query.getStatus() != null, DataServiceApiConfigEntity::getStatus, query.getStatus())
				.eq(query.getSqlDbType() != null, DataServiceApiConfigEntity::getSqlDbType, query.getSqlDbType())
				.eq(query.getDatabaseId() != null, DataServiceApiConfigEntity::getDatabaseId, query.getDatabaseId())
				.eq(query.getPrevilege() != null, DataServiceApiConfigEntity::getPrevilege, query.getPrevilege())
				.eq(query.getOpenTrans() != null, DataServiceApiConfigEntity::getOpenTrans, query.getOpenTrans())
				.orderByDesc(DataServiceApiConfigEntity::getCreateTime).orderByDesc(DataServiceApiConfigEntity::getId);
		dataScopeWithOrgId(wrapper);
		return wrapper;
	}

	@Override
	public void save(DataServiceApiConfigVO vo) {
		//获取目录
		DataServiceApiGroupEntity groupEntity = dataServiceApiGroupDao.selectById(vo.getGroupId());
		DataServiceApiConfigEntity entity = DataServiceApiConfigConvert.INSTANCE.convert(vo);
		entity.setOrgId(groupEntity.getOrgId());
		entity.setProjectId(getProjectId());
		//判断路径是否重复
		DataServiceApiConfigEntity one = getOneByPath(vo);
		if (one != null) {
			throw new ServerException(String.format("已存在路径为【%s】的 API 【%s】，路径不可重复！", one.getPath(), one.getName()));
		}
		baseMapper.insert(entity);
	}

	@Override
	public void update(DataServiceApiConfigVO vo) {
		DataServiceApiGroupEntity groupEntity = dataServiceApiGroupDao.selectById(vo.getGroupId());
		DataServiceApiConfigEntity entity = DataServiceApiConfigConvert.INSTANCE.convert(vo);
		entity.setOrgId(groupEntity.getOrgId());
		entity.setProjectId(getProjectId());
		DataServiceApiConfigEntity dbEntity = getById(vo.getId());
		DataServiceApiConfigEntity one = getOneByPath(vo);
		if (one != null && !dbEntity.getPath().equals(one.getPath())) {
			throw new ServerException(String.format("已存在路径为【%s】的 API 【%s】，路径不可重复！", one.getPath(), one.getName()));
		}
		if (entity.getStatus() == 0) {
			entity.setReleaseTime(null);
			entity.setReleaseUserId(null);
		}
		updateById(entity);
		//如果服务已上线，先下线，再上线
		if (entity.getStatus() == 1) {
			mappingHandlerMapping.unregisterMapping(dbEntity);
			mappingHandlerMapping.registerMapping(entity);
		}
	}

	private DataServiceApiConfigEntity getOneByPath(DataServiceApiConfigVO vo) {
		LambdaQueryWrapper<DataServiceApiConfigEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DataServiceApiConfigEntity::getPath, vo.getPath()).last(" limit 1");
		return baseMapper.selectOne(wrapper);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(List<Long> idList) {
		removeByIds(idList);
		//同步删除授权信息
		for (Long apiId : idList) {
			LambdaQueryWrapper<DataServiceApiAuthEntity> wrapper = new LambdaQueryWrapper<>();
			wrapper.eq(DataServiceApiAuthEntity::getApiId, apiId);
			apiAuthDao.delete(wrapper);
		}
	}

	@Override
	public String getIpPort() {
		List<ServiceInstance> instances = discoveryClient.getInstances(ServerNames.GATEWAY_SERVER_NAME);
		return instances.get(0).getHost() + ":" + instances.get(0).getPort() + "/data-service/api/";
	}

	@Override
	public JdbcSelectResult sqlExecute(SqlDto sqlDto) {
		return apiExecuteService.sqlExecute(sqlDto);
	}

	@Override
	public void online(Long id) {
		//注册接口
		DataServiceApiConfigEntity apiConfigEntity = getById(id);
		mappingHandlerMapping.registerMapping(apiConfigEntity);
		//更新状态
		apiConfigEntity.setStatus(1);
		apiConfigEntity.setReleaseTime(new Date());
		apiConfigEntity.setReleaseUserId(SecurityUser.getUserId());
		baseMapper.updateById(apiConfigEntity);
	}

	@Override
	public void offline(Long id) {
		DataServiceApiConfigEntity apiConfigEntity = getById(id);
		mappingHandlerMapping.unregisterMapping(apiConfigEntity);
		//更新状态
		apiConfigEntity.setStatus(0);
		apiConfigEntity.setReleaseTime(null);
		apiConfigEntity.setReleaseUserId(null);
		baseMapper.updateById(apiConfigEntity);
	}

	@Override
	public PageResult<DataServiceApiConfigVO> pageAuth(DataServiceApiConfigQuery query) {
		// 查询参数
		Map<String, Object> params = getParams(query);
		// 分页查询
		query.setOrder("dsac.id");
		IPage<DataServiceApiConfigEntity> page = getPage(query);
		params.put(Constant.PAGE, page);
		// 数据列表
		List<DataServiceApiConfigEntity> list = baseMapper.getAuthList(params);
		return new PageResult<>(DataServiceApiConfigConvert.INSTANCE.convertList(list), page.getTotal());
	}

	@Override
	public List<DataServiceApiConfigEntity> listActive() {
		LambdaQueryWrapper<DataServiceApiConfigEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DataServiceApiConfigEntity::getStatus, 1);
		return baseMapper.selectList(wrapper);
	}

	@Override
	public List<DataServiceApiConfigEntity> listActiveByGroupId(Long id) {
		LambdaQueryWrapper<DataServiceApiConfigEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DataServiceApiConfigEntity::getStatus, 1).eq(DataServiceApiConfigEntity::getGroupId, id).orderByDesc(DataServiceApiConfigEntity::getId);
		dataScopeWithOrgId(wrapper);
		return baseMapper.selectList(wrapper);
	}

	@Override
	public DataServiceApiAuthVO getAuthInfo(Long authId) {
		return DataServiceApiAuthConvert.INSTANCE.convert(apiAuthDao.selectById(authId));
	}

	@Override
	public void exportDocs(List<Long> ids, HttpServletResponse response) {
		List<ServiceInstance> instances = discoveryClient.getInstances(ServerNames.GATEWAY_SERVER_NAME);
		ServiceInstance instance = instances.get(0);
		StringBuilder docs = new StringBuilder("## 接口文档\n---\n");
		List<DataServiceApiConfigEntity> apiConfigEntities = baseMapper.selectBatchIds(ids);
		for (DataServiceApiConfigEntity api : apiConfigEntities) {
			docs.append("### ").append(api.getName())
					.append("\n- IP地址：").append(instance.getHost())
					.append("\n- 端口：").append(instance.getPort())
					.append("\n- 接口地址：/data-service/api/").append(api.getPath())
					.append("\n- 请求方式：").append(api.getType())
					.append("\n- Content-Type：").append(api.getContentType())
					.append("\n- 是否需要鉴权：").append(api.getPrevilege() == 1 ? "是" : "否");
			if (api.getPrevilege() == 1) {
				docs.append("\n- 获取鉴权token：").append("/data-service/api/token/generate?appKey=您的appKey&appSecret=您的appToken");
			}
			docs.append("\n- 接口备注：").append(api.getNote())
					.append("\n- 请求参数示例：").append("\n```json\n").append(StringUtil.isNotBlank(api.getJsonParam()) ? api.getJsonParam() : "{}").append("\n```\n")
					.append("\n- 响应结果示例：").append("\n```json\n").append(StringUtil.isNotBlank(api.getResponseResult()) ? api.getResponseResult() : "{\"code\":0,\"msg\":\"success\",\"data\":[]}").append("\n```\n")
					.append("\n---\n");
		}
		response.setContentType("application/x-msdownload;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=API DOCS.md");
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			os.write(docs.toString().getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null)
					os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String ipPort() {
		List<ServiceInstance> instances = discoveryClient.getInstances(ServerNames.GATEWAY_SERVER_NAME);
		return instances.get(0).getHost() + ":" + instances.get(0).getPort();
	}

	@Override
	public void resetRequested(Long authId) {
		apiAuthDao.resetRequested(authId);
	}

	private Map<String, Object> getParams(DataServiceApiConfigQuery query) {
		Map<String, Object> params = new HashMap<>();
		params.put("ifMarket", query.getIfMarket());
		params.put("queryApply", query.getQueryApply());
		if (query.getQueryApply() != null && query.getQueryApply() == 1) {
			params.put("userId", SecurityUser.getUserId());
		}
		params.put("resourceId", query.getResourceId());
		params.put("groupId", query.getGroupId());
		params.put("appId", query.getAppId());
		params.put("name", query.getName());
		params.put("path", query.getPath());
		params.put("contentType", query.getContentType());
		params.put("status", query.getStatus());
		params.put("sqlDbType", query.getSqlDbType());
		params.put("databaseId", query.getDatabaseId());
		params.put("previlege", query.getPrevilege());
		params.put("openTrans", query.getOpenTrans());
		// 数据权限
		params.put(Constant.DATA_SCOPE, getDataScope("dsac", null, null, "project_id", false, true));

		return params;
	}

}
