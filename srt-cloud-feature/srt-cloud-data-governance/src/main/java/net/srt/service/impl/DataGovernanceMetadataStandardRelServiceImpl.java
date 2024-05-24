package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceMetadataStandardRelConvert;
import net.srt.dao.DataGovernanceMetadataStandardRelDao;
import net.srt.entity.DataGovernanceMetadataStandardRelEntity;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.service.DataGovernanceMetadataStandardRelService;
import net.srt.vo.DataGovernanceMetadataStandardRelVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 数据治理-元数据标准关联表
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-05-23
 */
@Service
@AllArgsConstructor
public class DataGovernanceMetadataStandardRelServiceImpl extends BaseServiceImpl<DataGovernanceMetadataStandardRelDao, DataGovernanceMetadataStandardRelEntity> implements DataGovernanceMetadataStandardRelService {


	@Override
	public void save(DataGovernanceMetadataStandardRelVO vo) {
		DataGovernanceMetadataStandardRelEntity entity = DataGovernanceMetadataStandardRelConvert.INSTANCE.convert(vo);
		LambdaQueryWrapper<DataGovernanceMetadataStandardRelEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(DataGovernanceMetadataStandardRelEntity::getMetadataId, vo.getMetadataId()).eq(DataGovernanceMetadataStandardRelEntity::getStandardId, vo.getStandardId()).last("limit 1");
		DataGovernanceMetadataStandardRelEntity relEntity = baseMapper.selectOne(wrapper);
		if (relEntity != null) {
			entity.setId(relEntity.getId());
			baseMapper.updateById(entity);
		} else {
			baseMapper.insert(entity);
		}
	}

	@Override
	public void update(DataGovernanceMetadataStandardRelVO vo) {
		DataGovernanceMetadataStandardRelEntity entity = DataGovernanceMetadataStandardRelConvert.INSTANCE.convert(vo);

		updateById(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long metadataId, Long standardId) {
		LambdaQueryWrapper<DataGovernanceMetadataStandardRelEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(DataGovernanceMetadataStandardRelEntity::getMetadataId, metadataId).eq(DataGovernanceMetadataStandardRelEntity::getStandardId, standardId);
		baseMapper.delete(wrapper);
	}

	@Override
	public DataGovernanceMetadataStandardRelVO getMetadataRel(Long metadataId) {
		LambdaQueryWrapper<DataGovernanceMetadataStandardRelEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(DataGovernanceMetadataStandardRelEntity::getMetadataId, metadataId).last("limit 1");
		DataGovernanceMetadataStandardRelEntity relEntity = baseMapper.selectOne(wrapper);
		if (relEntity != null) {
			/*DataGovernanceStandardEntity standardEntity = standardDao.selectById(relEntity.getStandardId());
			DataGovernanceMetadataStandardRelVO convert = DataGovernanceMetadataStandardRelConvert.INSTANCE.convert(relEntity);
			convert.setStandardCategoryId(standardEntity.getCategoryId());
			return convert;*/
		}
		return null;
	}

}
