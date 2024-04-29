package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceMetamodelPropertyConvert;
import net.srt.dao.DataGovernanceMetadataPropertyDao;
import net.srt.dao.DataGovernanceMetamodelPropertyDao;
import net.srt.entity.DataGovernanceMetadataPropertyEntity;
import net.srt.entity.DataGovernanceMetamodelPropertyEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.query.DataGovernanceMetamodelPropertyQuery;
import net.srt.service.DataGovernanceMetamodelPropertyService;
import net.srt.vo.DataGovernanceMetamodelPropertyVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.util.StringUtil;

import java.util.List;

/**
 * 数据治理-元模型属性
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-03-28
 */
@Service
@AllArgsConstructor
public class DataGovernanceMetamodelPropertyServiceImpl extends BaseServiceImpl<DataGovernanceMetamodelPropertyDao, DataGovernanceMetamodelPropertyEntity> implements DataGovernanceMetamodelPropertyService {

	private final DataGovernanceMetadataPropertyDao metadataPropertyDao;

	@Override
	public PageResult<DataGovernanceMetamodelPropertyVO> page(DataGovernanceMetamodelPropertyQuery query) {
		IPage<DataGovernanceMetamodelPropertyEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

		return new PageResult<>(DataGovernanceMetamodelPropertyConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
	}

	private LambdaQueryWrapper<DataGovernanceMetamodelPropertyEntity> getWrapper(DataGovernanceMetamodelPropertyQuery query) {
		LambdaQueryWrapper<DataGovernanceMetamodelPropertyEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(DataGovernanceMetamodelPropertyEntity::getMetamodelId, query.getMetamodelId())
				.like(StringUtil.isNotBlank(query.getName()), DataGovernanceMetamodelPropertyEntity::getName, query.getName())
				.like(StringUtil.isNotBlank(query.getCode()), DataGovernanceMetamodelPropertyEntity::getCode, query.getCode())
				.orderByAsc(DataGovernanceMetamodelPropertyEntity::getOrderNo);
		return wrapper;
	}

	@Override
	public void save(DataGovernanceMetamodelPropertyVO vo) {
		DataGovernanceMetamodelPropertyEntity entity = DataGovernanceMetamodelPropertyConvert.INSTANCE.convert(vo);
		entity.setBuiltin(0);
		entity.setProjectId(getProjectId());
		baseMapper.insert(entity);
	}

	@Override
	public void update(DataGovernanceMetamodelPropertyVO vo) {
		DataGovernanceMetamodelPropertyEntity entity = DataGovernanceMetamodelPropertyConvert.INSTANCE.convert(vo);
		entity.setBuiltin(0);
		entity.setProjectId(getProjectId());
		updateById(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(List<Long> idList) {
		removeByIds(idList);
		//同步删除元数据属性
		for (Long id : idList) {
			LambdaQueryWrapper<DataGovernanceMetadataPropertyEntity> wrapper = new LambdaQueryWrapper<>();
			wrapper.eq(DataGovernanceMetadataPropertyEntity::getMetamodelPropertyId, id);
			metadataPropertyDao.delete(wrapper);
		}

	}

	@Override
	public List<DataGovernanceMetamodelPropertyVO> properties(Long metaModelId) {
		LambdaQueryWrapper<DataGovernanceMetamodelPropertyEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DataGovernanceMetamodelPropertyEntity::getMetamodelId, metaModelId).orderByAsc(DataGovernanceMetamodelPropertyEntity::getOrderNo);
		return DataGovernanceMetamodelPropertyConvert.INSTANCE.convertList(baseMapper.selectList(wrapper));
	}

}
