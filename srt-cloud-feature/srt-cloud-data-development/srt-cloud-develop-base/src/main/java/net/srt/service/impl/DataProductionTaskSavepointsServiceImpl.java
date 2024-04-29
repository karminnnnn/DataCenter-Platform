package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.srt.convert.DataProductionTaskSavepointsConvert;
import net.srt.dao.DataProductionTaskSavepointsDao;
import net.srt.entity.DataProductionTaskSavepointsEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.query.DataProductionTaskSavepointsQuery;
import net.srt.service.DataProductionTaskSavepointsService;
import net.srt.vo.DataProductionTaskSavepointsVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 数据生产-作业保存点
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-01-08
 */
@Service
@AllArgsConstructor
public class DataProductionTaskSavepointsServiceImpl extends BaseServiceImpl<DataProductionTaskSavepointsDao, DataProductionTaskSavepointsEntity> implements DataProductionTaskSavepointsService {

	@Override
	public PageResult<DataProductionTaskSavepointsVO> page(DataProductionTaskSavepointsQuery query) {
		IPage<DataProductionTaskSavepointsEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));
		return new PageResult<>(DataProductionTaskSavepointsConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
	}

	private LambdaQueryWrapper<DataProductionTaskSavepointsEntity> getWrapper(DataProductionTaskSavepointsQuery query) {
		LambdaQueryWrapper<DataProductionTaskSavepointsEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(DataProductionTaskSavepointsEntity::getTaskId, query.getTaskId())
				.eq(query.getHistoryId() != null, DataProductionTaskSavepointsEntity::getHistoryId, query.getHistoryId())
				.orderByDesc(DataProductionTaskSavepointsEntity::getCreateTime)
				.orderByDesc(DataProductionTaskSavepointsEntity::getId);
		return wrapper;
	}

	@Override
	public void save(DataProductionTaskSavepointsVO vo) {
		DataProductionTaskSavepointsEntity entity = DataProductionTaskSavepointsConvert.INSTANCE.convert(vo);

		baseMapper.insert(entity);
	}

	@Override
	public void update(DataProductionTaskSavepointsVO vo) {
		DataProductionTaskSavepointsEntity entity = DataProductionTaskSavepointsConvert.INSTANCE.convert(vo);

		updateById(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(List<Long> idList) {
		removeByIds(idList);
	}

	@Override
	public DataProductionTaskSavepointsEntity getLatestSavepointByTaskId(Long id) {
		LambdaQueryWrapper<DataProductionTaskSavepointsEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DataProductionTaskSavepointsEntity::getTaskId, id)
				.orderByDesc(DataProductionTaskSavepointsEntity::getCreateTime)
				.orderByDesc(DataProductionTaskSavepointsEntity::getId).last(" limit 1");
		return baseMapper.selectOne(wrapper);
	}

	@Override
	public DataProductionTaskSavepointsEntity getEarliestSavepointByTaskId(Long id) {
		LambdaQueryWrapper<DataProductionTaskSavepointsEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DataProductionTaskSavepointsEntity::getTaskId, id)
				.orderByAsc(DataProductionTaskSavepointsEntity::getCreateTime)
				.orderByAsc(DataProductionTaskSavepointsEntity::getId).last(" limit 1");
		return baseMapper.selectOne(wrapper);
	}

}
