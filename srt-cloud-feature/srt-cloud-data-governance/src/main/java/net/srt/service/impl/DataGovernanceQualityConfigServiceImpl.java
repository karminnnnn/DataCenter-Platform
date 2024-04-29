package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.srt.api.module.data.governance.DataQualityApi;
import net.srt.api.module.data.governance.constant.BuiltInQualityRule;
import net.srt.api.module.data.governance.dto.quality.QualityConfigParam;
import net.srt.api.module.quartz.QuartzDataGovernanceQualityApi;
import net.srt.convert.DataGovernanceQualityConfigConvert;
import net.srt.dao.DataGovernanceMetadataDao;
import net.srt.dao.DataGovernanceQualityConfigCategoryDao;
import net.srt.dao.DataGovernanceQualityConfigDao;
import net.srt.entity.DataGovernanceMetadataEntity;
import net.srt.entity.DataGovernanceQualityConfigCategoryEntity;
import net.srt.entity.DataGovernanceQualityConfigEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.query.DataGovernanceQualityConfigQuery;
import net.srt.service.DataGovernanceQualityConfigCategoryService;
import net.srt.service.DataGovernanceQualityConfigService;
import net.srt.vo.DataGovernanceQualityConfigVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.util.StringUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据治理-质量规则配置
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-05-29
 */
@Service
@AllArgsConstructor
public class DataGovernanceQualityConfigServiceImpl extends BaseServiceImpl<DataGovernanceQualityConfigDao, DataGovernanceQualityConfigEntity> implements DataGovernanceQualityConfigService {

	private final QuartzDataGovernanceQualityApi quartzDataGovernanceQualityApi;
	private final DataGovernanceQualityConfigCategoryDao categoryDao;
	private final DataGovernanceMetadataDao metadataDao;

	@Override
	public PageResult<DataGovernanceQualityConfigVO> page(DataGovernanceQualityConfigQuery query) {
		IPage<DataGovernanceQualityConfigEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

		return new PageResult<>(DataGovernanceQualityConfigConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
	}

	@Override
	public DataGovernanceQualityConfigVO get(Long id) {
		DataGovernanceQualityConfigVO configVO = DataGovernanceQualityConfigConvert.INSTANCE.convert(baseMapper.selectById(id));
		List<Integer> metadataIds = configVO.getMetadataIds();
		LambdaQueryWrapper<DataGovernanceMetadataEntity> metadataWrapper = Wrappers.lambdaQuery();
		metadataWrapper.in(DataGovernanceMetadataEntity::getId, metadataIds);
		List<DataGovernanceMetadataEntity> metadataEntities = metadataDao.selectList(metadataWrapper);
		if (CollectionUtils.isEmpty(metadataEntities)) {
			configVO.setMetadataStrs("检测字段已被删除，请检查元数据信息");
		} else {
			configVO.setMetadataStrs(metadataEntities.stream().map(DataGovernanceMetadataEntity::getPath).collect(Collectors.joining("；")));
		}
		//如果是关联一致性校验
		if (BuiltInQualityRule.ASSOCIATION_CONSISTENCY.getId().equals(configVO.getRuleId())) {
			QualityConfigParam param = configVO.getParam();
			Integer columnMetaId = param.getColumnMetaId();
			DataGovernanceMetadataEntity relEntity = metadataDao.selectById(columnMetaId);
			if (relEntity != null) {
				configVO.setRelMetadataStr(relEntity.getPath());
			} else {
				configVO.setMetadataStrs("关联字段已被删除，请检查元数据信息");
			}
		}
		return configVO;
	}

	private LambdaQueryWrapper<DataGovernanceQualityConfigEntity> getWrapper(DataGovernanceQualityConfigQuery query) {
		LambdaQueryWrapper<DataGovernanceQualityConfigEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(query.getCategoryId() != null, DataGovernanceQualityConfigEntity::getCategoryId, query.getCategoryId())
				.like(StringUtil.isNotBlank(query.getName()), DataGovernanceQualityConfigEntity::getName, query.getName())
				.eq(query.getStatus() != null, DataGovernanceQualityConfigEntity::getStatus, query.getStatus())
				.eq(query.getTaskType() != null, DataGovernanceQualityConfigEntity::getTaskType, query.getTaskType())
				.orderByDesc(DataGovernanceQualityConfigEntity::getId);
		return wrapper;
	}

	@Override
	public void save(DataGovernanceQualityConfigVO vo) {
		//获取目录
		DataGovernanceQualityConfigCategoryEntity categoryEntity = categoryDao.selectById(vo.getCategoryId());
		DataGovernanceQualityConfigEntity entity = DataGovernanceQualityConfigConvert.INSTANCE.convert(vo);
		entity.setOrgId(categoryEntity.getOrgId());
		entity.setProjectId(getProjectId());
		if (!BuiltInQualityRule.UNIQUENESS.getId().equals(entity.getRuleId()) && !BuiltInQualityRule.LENGTH_CHECK.getId().equals(entity.getRuleId()) && !BuiltInQualityRule.ASSOCIATION_CONSISTENCY.getId().equals(entity.getRuleId()) && !BuiltInQualityRule.TIMELINESS.getId().equals(entity.getRuleId())) {
			entity.setParam(null);
		}
		baseMapper.insert(entity);
	}

	@Override
	public void update(DataGovernanceQualityConfigVO vo) {
		if (!BuiltInQualityRule.UNIQUENESS.getId().equals(vo.getRuleId()) && !BuiltInQualityRule.LENGTH_CHECK.getId().equals(vo.getRuleId()) && !BuiltInQualityRule.ASSOCIATION_CONSISTENCY.getId().equals(vo.getRuleId()) && !BuiltInQualityRule.TIMELINESS.getId().equals(vo.getRuleId())) {
			vo.setParam(null);
		}
		//获取目录
		DataGovernanceQualityConfigCategoryEntity categoryEntity = categoryDao.selectById(vo.getCategoryId());
		DataGovernanceQualityConfigEntity entity = DataGovernanceQualityConfigConvert.INSTANCE.convert(vo);
		entity.setOrgId(categoryEntity.getOrgId());
		entity.setProjectId(getProjectId());
		updateById(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(List<Long> idList) {
		for (Long id : idList) {
			quartzDataGovernanceQualityApi.cancel(id);
		}
		removeByIds(idList);
	}

	@Override
	public void online(Long id) {
		DataGovernanceQualityConfigEntity configEntity = baseMapper.selectById(id);
		configEntity.setStatus(1);
		quartzDataGovernanceQualityApi.release(id);
		baseMapper.updateById(configEntity);
	}

	@Override
	public void offline(Long id) {
		DataGovernanceQualityConfigEntity configEntity = baseMapper.selectById(id);
		configEntity.setStatus(0);
		quartzDataGovernanceQualityApi.cancel(id);
		baseMapper.updateById(configEntity);
	}

	@Override
	public void handRun(Long id) {
		quartzDataGovernanceQualityApi.handRun(id);
	}

}
