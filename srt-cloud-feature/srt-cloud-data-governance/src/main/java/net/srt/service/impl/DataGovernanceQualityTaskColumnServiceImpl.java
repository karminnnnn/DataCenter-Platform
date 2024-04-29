package net.srt.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceQualityTaskColumnConvert;
import net.srt.entity.DataGovernanceQualityConfigEntity;
import net.srt.entity.DataGovernanceQualityTaskColumnEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.query.DataGovernanceQualityTaskColumnQuery;
import net.srt.vo.DataGovernanceQualityTaskColumnVO;
import net.srt.dao.DataGovernanceQualityTaskColumnDao;
import net.srt.service.DataGovernanceQualityTaskColumnService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 数据治理-字段检测记录
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-06-23
 */
@Service
@AllArgsConstructor
public class DataGovernanceQualityTaskColumnServiceImpl extends BaseServiceImpl<DataGovernanceQualityTaskColumnDao, DataGovernanceQualityTaskColumnEntity> implements DataGovernanceQualityTaskColumnService {

	@Override
	public PageResult<DataGovernanceQualityTaskColumnVO> page(DataGovernanceQualityTaskColumnQuery query) {
		IPage<DataGovernanceQualityTaskColumnEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

		return new PageResult<>(DataGovernanceQualityTaskColumnConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
	}

	private LambdaQueryWrapper<DataGovernanceQualityTaskColumnEntity> getWrapper(DataGovernanceQualityTaskColumnQuery query) {
		LambdaQueryWrapper<DataGovernanceQualityTaskColumnEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(query.getQualityTaskTableId() != null, DataGovernanceQualityTaskColumnEntity::getQualityTaskTableId, query.getQualityTaskTableId())
				.eq(query.getCheckResult() != null, DataGovernanceQualityTaskColumnEntity::getCheckResult, query.getCheckResult())
				.eq(query.getNotPassReason() != null, DataGovernanceQualityTaskColumnEntity::getNotPassReason, query.getNotPassReason())
				.orderByDesc(DataGovernanceQualityTaskColumnEntity::getId);
		return wrapper;
	}

	@Override
	public void save(DataGovernanceQualityTaskColumnVO vo) {
		DataGovernanceQualityTaskColumnEntity entity = DataGovernanceQualityTaskColumnConvert.INSTANCE.convert(vo);

		baseMapper.insert(entity);
	}

	@Override
	public void update(DataGovernanceQualityTaskColumnVO vo) {
		DataGovernanceQualityTaskColumnEntity entity = DataGovernanceQualityTaskColumnConvert.INSTANCE.convert(vo);

		updateById(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(List<Long> idList) {
		removeByIds(idList);
	}

}
