package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.srt.convert.DataProductionScheduleRecordConvert;
import net.srt.dao.DataProductionScheduleRecordDao;
import net.srt.entity.DataProductionScheduleRecordEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.query.DataProductionScheduleRecordQuery;
import net.srt.service.DataProductionScheduleRecordService;
import net.srt.vo.DataProductionScheduleRecordVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.util.StringUtil;

import java.util.List;

/**
 * 数据生产-作业调度记录
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-01-16
 */
@Service
@AllArgsConstructor
public class DataProductionScheduleRecordServiceImpl extends BaseServiceImpl<DataProductionScheduleRecordDao, DataProductionScheduleRecordEntity> implements DataProductionScheduleRecordService {

	@Override
	public PageResult<DataProductionScheduleRecordVO> page(DataProductionScheduleRecordQuery query) {
		IPage<DataProductionScheduleRecordEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

		return new PageResult<>(DataProductionScheduleRecordConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
	}

	private LambdaQueryWrapper<DataProductionScheduleRecordEntity> getWrapper(DataProductionScheduleRecordQuery query) {
		LambdaQueryWrapper<DataProductionScheduleRecordEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.like(StringUtil.isNotBlank(query.getName()), DataProductionScheduleRecordEntity::getName, query.getName())
				.eq(query.getRunStatus() != null, DataProductionScheduleRecordEntity::getRunStatus, query.getRunStatus())
				.eq(query.getExecuteType() != null, DataProductionScheduleRecordEntity::getExecuteType, query.getExecuteType())
				.gt(query.getStartTime() != null, DataProductionScheduleRecordEntity::getStartTime, query.getStartTime())
				.lt(query.getEndTime() != null, DataProductionScheduleRecordEntity::getEndTime, query.getEndTime());
		dataScopeWithOrgId(wrapper);
		wrapper.orderByDesc(DataProductionScheduleRecordEntity::getId);
		return wrapper;
	}

	@Override
	public void save(DataProductionScheduleRecordVO vo) {
		DataProductionScheduleRecordEntity entity = DataProductionScheduleRecordConvert.INSTANCE.convert(vo);

		baseMapper.insert(entity);
	}

	@Override
	public void update(DataProductionScheduleRecordVO vo) {
		DataProductionScheduleRecordEntity entity = DataProductionScheduleRecordConvert.INSTANCE.convert(vo);

		updateById(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(List<Long> idList) {
		removeByIds(idList);
	}

}
