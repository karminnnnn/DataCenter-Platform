package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceStandardCodeConvert;
import net.srt.dao.DataGovernanceStandardCodeDao;
import net.srt.dao.DataGovernanceStandardDao;
import net.srt.entity.DataGovernanceStandardCodeEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.query.DataGovernanceStandardCodeQuery;
import net.srt.service.DataGovernanceStandardCodeService;
import net.srt.vo.DataGovernanceStandardCodeVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.util.StringUtil;

import java.util.List;

/**
 * 数据治理-标准码表数据
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-05-19
 */
@Service
@AllArgsConstructor
public class DataGovernanceStandardCodeServiceImpl extends BaseServiceImpl<DataGovernanceStandardCodeDao, DataGovernanceStandardCodeEntity> implements DataGovernanceStandardCodeService {

	private final DataGovernanceStandardDao standardDao;
	@Override
	public PageResult<DataGovernanceStandardCodeVO> page(DataGovernanceStandardCodeQuery query) {
		IPage<DataGovernanceStandardCodeEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

		return new PageResult<>(DataGovernanceStandardCodeConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
	}

	private LambdaQueryWrapper<DataGovernanceStandardCodeEntity> getWrapper(DataGovernanceStandardCodeQuery query) {
		LambdaQueryWrapper<DataGovernanceStandardCodeEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(query.getStandardId() != null, DataGovernanceStandardCodeEntity::getStandardId, query.getStandardId())
				.eq(StringUtil.isNotBlank(query.getDataId()), DataGovernanceStandardCodeEntity::getDataId, query.getDataId())
				.eq(StringUtil.isNotBlank(query.getDataName()), DataGovernanceStandardCodeEntity::getDataName, query.getDataName());
		return wrapper;
	}

	@Override
	public void save(DataGovernanceStandardCodeVO vo) {
		DataGovernanceStandardCodeEntity entity = DataGovernanceStandardCodeConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		baseMapper.insert(entity);
		standardDao.updateCodeNumByStandardId(vo.getStandardId());
	}

	@Override
	public void update(DataGovernanceStandardCodeVO vo) {
		DataGovernanceStandardCodeEntity entity = DataGovernanceStandardCodeConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		updateById(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(List<Long> idList) {
		Long id = idList.get(0);
		DataGovernanceStandardCodeEntity standardCodeEntity = baseMapper.selectById(id);
		removeByIds(idList);
		standardDao.updateCodeNumByStandardId(standardCodeEntity.getStandardId());
	}

}
