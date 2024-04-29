package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.srt.api.module.data.service.DataServiceApi;
import net.srt.api.module.data.service.dto.DataServiceApiAuthDto;
import net.srt.convert.DataMarketResourceApplyConvert;
import net.srt.dao.DataMarketResourceApplyDao;
import net.srt.dto.AuthInfo;
import net.srt.dto.CheckDto;
import net.srt.entity.DataMarketResourceApplyEntity;
import net.srt.framework.common.constant.Constant;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.framework.security.user.SecurityUser;
import net.srt.query.DataMarketResourceApplyQuery;
import net.srt.service.DataMarketResourceApplyService;
import net.srt.vo.DataMarketResourceApplyVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据集市-资源申请表
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-07-26
 */
@Service
@AllArgsConstructor
public class DataMarketResourceApplyServiceImpl extends BaseServiceImpl<DataMarketResourceApplyDao, DataMarketResourceApplyEntity> implements DataMarketResourceApplyService {

	private final DataServiceApi dataServiceApi;

	@Override
	public PageResult<DataMarketResourceApplyVO> page(DataMarketResourceApplyQuery query) {
		// 查询参数
		long millis = System.currentTimeMillis();
		Map<String, Object> params = getParams(query);
		IPage<DataMarketResourceApplyEntity> page = getPage(query);
		params.put(Constant.PAGE, page);
		List<DataMarketResourceApplyEntity> list = baseMapper.getList(params);
		List<DataMarketResourceApplyVO> applyVOS = DataMarketResourceApplyConvert.INSTANCE.convertList(list);
		for (DataMarketResourceApplyVO applyVO : applyVOS) {
			applyVO.setEffective(applyVO.getApplyStartTime().getTime() <= millis && applyVO.getApplyEndTime().getTime() >= millis);
		}
		return new PageResult<>(applyVOS, page.getTotal());
	}

	private Map<String, Object> getParams(DataMarketResourceApplyQuery query) {
		Map<String, Object> params = new HashMap<>();
		params.put("resourceId", query.getResourceId());
		params.put("resourceName", query.getResourceName());
		params.put("resourceMountName", query.getResourceMountName());
		params.put("mountType", query.getMountType());
		params.put("applyStartTime", query.getApplyStartTime());
		params.put("applyEndTime", query.getApplyEndTime());
		params.put("status", query.getStatus());
		params.put("ifAuth", query.getIfAuth());
		if (query.getIfSelf() == 1) {
			params.put("creator", SecurityUser.getUserId());
		}
		params.put("projectId", getProjectId());
		return params;
	}


	@Override
	public void save(DataMarketResourceApplyVO vo) {
		DataMarketResourceApplyEntity entity = DataMarketResourceApplyConvert.INSTANCE.convert(vo);
		entity.setApplyUserId(SecurityUser.getUserId().intValue());
		entity.setProjectId(getProjectId());
		baseMapper.insert(entity);
	}

	@Override
	public void update(DataMarketResourceApplyVO vo) {
		DataMarketResourceApplyEntity entity = DataMarketResourceApplyConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		updateById(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(List<Long> idList) {
		for (Long id : idList) {
			DataMarketResourceApplyEntity applyEntity = baseMapper.selectById(id);
			removeById(id);
			if (applyEntity.getMountType() == 2 && applyEntity.getStatus() == 1 && applyEntity.getIfAuth() == 1) {
				//重新授权
				authApi(applyEntity);
			}
		}
	}

	@Override
	public void check(CheckDto checkDto) {
		DataMarketResourceApplyEntity resourceApplyEntity = baseMapper.selectById(checkDto.getId());
		resourceApplyEntity.setCheckTime(new Date());
		resourceApplyEntity.setCheckUserId(SecurityUser.getUserId().intValue());
		resourceApplyEntity.setStatus(checkDto.getStatus());
		resourceApplyEntity.setIfAuth(checkDto.getStatus());
		resourceApplyEntity.setCheckNote(checkDto.getCheckNote());
		baseMapper.updateById(resourceApplyEntity);
		if (checkDto.getStatus() == 1) {
			//如果是API，给应用授权
			if (resourceApplyEntity.getMountType() == 2) {
				authApi(resourceApplyEntity);
			}
		}
	}

	@Override
	public void auth(Integer id, Integer auth) {
		DataMarketResourceApplyEntity applyEntity = baseMapper.selectById(id);
		applyEntity.setIfAuth(auth);
		baseMapper.updateById(applyEntity);
		//如果是api
		if (applyEntity.getMountType() == 2) {
			authApi(applyEntity);
		}
	}

	/**
	 * 给api添加授权
	 *
	 * @param applyEntity
	 */
	private void authApi(DataMarketResourceApplyEntity applyEntity) {
		AuthInfo authInfo = getAuthInfo(applyEntity.getAppId(), applyEntity.getMountId().longValue());
		DataServiceApiAuthDto authDto = new DataServiceApiAuthDto();
		authDto.setApiId(applyEntity.getMountId().longValue());
		authDto.setAppId(applyEntity.getAppId());
		authDto.setProjectId(applyEntity.getProjectId());
		authDto.setCreator(SecurityUser.getUserId());
		authDto.setUpdater(SecurityUser.getUserId());
		authDto.setRequestTimes(authInfo.getRequestTImes());
		authDto.setStartTime(authInfo.getStartTime());
		authDto.setEndTime(authInfo.getEndTime());
		authDto.setHasActiveApply(authInfo.getHasActiveApply());
		dataServiceApi.auth(authDto);
	}

	@Override
	public AuthInfo getAuthInfo(Long appId, Long apiId) {
		Date currentDate = new Date();
		//查询该appId和apiId的在有效期内的申请
		LambdaQueryWrapper<DataMarketResourceApplyEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(DataMarketResourceApplyEntity::getAppId, appId).eq(DataMarketResourceApplyEntity::getMountId, apiId)
				.eq(DataMarketResourceApplyEntity::getMountType, 2).eq(DataMarketResourceApplyEntity::getStatus, 1).eq(DataMarketResourceApplyEntity::getIfAuth, 1)
				.le(DataMarketResourceApplyEntity::getApplyStartTime, currentDate).gt(DataMarketResourceApplyEntity::getApplyEndTime, currentDate);
		List<DataMarketResourceApplyEntity> applies = baseMapper.selectList(wrapper);
		Integer requestTImes = 0;
		Date startTime = null;
		Date endTIme = null;
		for (DataMarketResourceApplyEntity apply : applies) {
			if (startTime == null) {
				startTime = apply.getApplyStartTime();
			} else {
				if (startTime.getTime() < apply.getApplyStartTime().getTime()) {
					startTime = apply.getApplyStartTime();
				}
			}
			if (endTIme == null) {
				endTIme = apply.getApplyEndTime();
			} else {
				if (endTIme.getTime() >= apply.getApplyEndTime().getTime()) {
					endTIme = apply.getApplyEndTime();
				}
			}
		}
		for (DataMarketResourceApplyEntity apply : applies) {
			if (apply.getApplyUseType() == 0) {
				requestTImes = -1;
				break;
			} else {
				requestTImes += apply.getApplyUseTimes();
			}
		}
		AuthInfo authInfo = new AuthInfo();
		authInfo.setRequestTImes(requestTImes);
		authInfo.setStartTime(startTime);
		authInfo.setEndTime(endTIme);
		authInfo.setHasActiveApply(!applies.isEmpty());
		return authInfo;
	}

}
