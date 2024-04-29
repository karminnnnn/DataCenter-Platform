package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.srt.api.module.data.assets.constant.ResourceOpenType;
import net.srt.api.module.system.constant.SuperAdminEnum;
import net.srt.convert.DataAssetsResourceConvert;
import net.srt.dao.DataAssetsCatalogDao;
import net.srt.dao.DataAssetsResourceDao;
import net.srt.dao.DataAssetsResourceMountDao;
import net.srt.dao.DataAssetsResourceOpenDao;
import net.srt.entity.DataAssetsCatalogEntity;
import net.srt.entity.DataAssetsResourceEntity;
import net.srt.entity.DataAssetsResourceMountEntity;
import net.srt.entity.DataAssetsResourceOpenEntity;
import net.srt.entity.DataMarketResourceApplyEntity;
import net.srt.framework.common.constant.Constant;
import net.srt.framework.common.exception.ServerException;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.framework.security.user.SecurityUser;
import net.srt.query.DataAssetsResourceQuery;
import net.srt.service.DataAssetsResourceService;
import net.srt.service.DataMarketResourceApplyService;
import net.srt.vo.DataAssetsResourceVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.util.StringUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据资产-资产列表
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-07-06
 */
@Service
@AllArgsConstructor
public class DataAssetsResourceServiceImpl extends BaseServiceImpl<DataAssetsResourceDao, DataAssetsResourceEntity> implements DataAssetsResourceService {
	private final DataAssetsCatalogDao catalogDao;
	private final DataAssetsResourceMountDao resourceMountDao;
	private final DataAssetsResourceOpenDao resourceOpenDao;
	private final DataMarketResourceApplyService applyService;

	@Override
	public PageResult<DataAssetsResourceVO> page(DataAssetsResourceQuery query) {
		IPage<DataAssetsResourceEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

		return new PageResult<>(DataAssetsResourceConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
	}

	@Override
	public PageResult<DataAssetsResourceVO> pageMarket(DataAssetsResourceQuery query) {
		if (SuperAdminEnum.YES.getValue().equals(SecurityUser.getUser().getSuperAdmin())) {
			query.setStatus(1);
			return page(query);
		}
		//根据开放类型插查询
		// 查询参数
		Map<String, Object> params = getParams(query);
		IPage<DataAssetsResourceEntity> page = getPage(query);
		params.put(Constant.PAGE, page);
		List<DataAssetsResourceEntity> list = baseMapper.getMarketList(params);
		return new PageResult<>(DataAssetsResourceConvert.INSTANCE.convertList(list), page.getTotal());
	}

	private Map<String, Object> getParams(DataAssetsResourceQuery query) {
		Map<String, Object> params = new HashMap<>();
		params.put("catalogId", query.getCatalogId());
		params.put("name", query.getName());
		params.put("code", query.getCode());
		params.put("status", query.getStatus());
		params.put("mountStatus", query.getMountStatus());
		params.put("dutyUser", query.getDutyUser());
		params.put("userId", SecurityUser.getUserId());
		return params;
	}

	private LambdaQueryWrapper<DataAssetsResourceEntity> getWrapper(DataAssetsResourceQuery query) {
		LambdaQueryWrapper<DataAssetsResourceEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(query.getCatalogId() != null, DataAssetsResourceEntity::getCatalogId, query.getCatalogId())
				.like(StringUtil.isNotBlank(query.getName()), DataAssetsResourceEntity::getName, query.getName())
				.like(StringUtil.isNotBlank(query.getCode()), DataAssetsResourceEntity::getCode, query.getCode())
				.eq(query.getStatus() != null, DataAssetsResourceEntity::getStatus, query.getStatus())
				.eq(query.getMountStatus() != null, DataAssetsResourceEntity::getMountStatus, query.getMountStatus())
				.orderByDesc(DataAssetsResourceEntity::getId);
		return wrapper;
	}

	@Override
	public void save(DataAssetsResourceVO vo) {
		DataAssetsCatalogEntity catalogEntity = catalogDao.selectById(vo.getCatalogId());
		DataAssetsResourceEntity entity = DataAssetsResourceConvert.INSTANCE.convert(vo);
		entity.setOrgId(catalogEntity.getOrgId());
		entity.setProjectId(getProjectId());
		baseMapper.insert(entity);
		//保存开放关联表
		addOpen(entity);
	}

	@Override
	public void update(DataAssetsResourceVO vo) {
		DataAssetsCatalogEntity catalogEntity = catalogDao.selectById(vo.getCatalogId());
		DataAssetsResourceEntity entity = DataAssetsResourceConvert.INSTANCE.convert(vo);
		entity.setOrgId(catalogEntity.getOrgId());
		entity.setProjectId(getProjectId());
		updateById(entity);
		//先删除关联表
		LambdaQueryWrapper<DataAssetsResourceOpenEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(DataAssetsResourceOpenEntity::getResourceId, entity.getId());
		resourceOpenDao.delete(wrapper);
		addOpen(entity);
	}

	private void addOpen(DataAssetsResourceEntity entity) {
		if (ResourceOpenType.ROLE.getValue().equals(entity.getOpenType())) {
			for (Integer openRole : entity.getOpenRoles()) {
				DataAssetsResourceOpenEntity resourceOpenEntity = new DataAssetsResourceOpenEntity();
				resourceOpenEntity.setResourceId(entity.getId());
				resourceOpenEntity.setOpenType(entity.getOpenType());
				resourceOpenEntity.setOpenId(openRole.longValue());
				resourceOpenEntity.setProjectId(entity.getProjectId());
				resourceOpenDao.insert(resourceOpenEntity);
			}
		} else if (ResourceOpenType.USER.getValue().equals(entity.getOpenType())) {
			for (Integer openUser : entity.getOpenUsers()) {
				DataAssetsResourceOpenEntity resourceOpenEntity = new DataAssetsResourceOpenEntity();
				resourceOpenEntity.setResourceId(entity.getId());
				resourceOpenEntity.setOpenType(entity.getOpenType());
				resourceOpenEntity.setOpenId(openUser.longValue());
				resourceOpenEntity.setProjectId(entity.getProjectId());
				resourceOpenDao.insert(resourceOpenEntity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) {
		//判断是否有申请与之关联
		LambdaQueryWrapper<DataMarketResourceApplyEntity> applyWrapper = Wrappers.lambdaQuery();
		applyWrapper.eq(DataMarketResourceApplyEntity::getResourceId, id).last("LIMIT 1");
		DataMarketResourceApplyEntity applyEntity = applyService.getOne(applyWrapper);
		if (applyEntity != null) {
			throw new ServerException(String.format("该资源下存在资源申请【%s】，不可删除", applyEntity.getTitle()));
		}
		removeById(id);
		//删除挂载的资源
		LambdaQueryWrapper<DataAssetsResourceMountEntity> deleteWrapper = Wrappers.lambdaQuery();
		deleteWrapper.eq(DataAssetsResourceMountEntity::getResourceId, id);
		resourceMountDao.delete(deleteWrapper);
	}

	@Override
	public void online(Long id) {
		DataAssetsResourceEntity resourceEntity = baseMapper.selectById(id);
		resourceEntity.setStatus(1);
		resourceEntity.setReleaseTime(new Date());
		resourceEntity.setReleaseUserId(SecurityUser.getUserId().intValue());
		baseMapper.updateById(resourceEntity);
	}

	@Override
	public void offline(Long id) {
		DataAssetsResourceEntity resourceEntity = baseMapper.selectById(id);
		resourceEntity.setStatus(0);
		resourceEntity.setReleaseTime(null);
		resourceEntity.setReleaseUserId(null);
		baseMapper.updateById(resourceEntity);
	}

}
