package net.srt.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import net.srt.api.module.data.governance.constant.MetadataCollectRunStatus;
import net.srt.convert.DataGovernanceMetadataCollectRecordConvert;
import net.srt.entity.DataGovernanceMetadataCollectRecordEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.DateUtils;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.query.DataGovernanceMetadataCollectRecordQuery;
import net.srt.vo.DataGovernanceMetadataCollectRecordVO;
import net.srt.dao.DataGovernanceMetadataCollectRecordDao;
import net.srt.service.DataGovernanceMetadataCollectRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 数据治理-元数据采集任务记录
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-04-04
 */
@Service
@AllArgsConstructor
public class DataGovernanceMetadataCollectRecordServiceImpl extends BaseServiceImpl<DataGovernanceMetadataCollectRecordDao, DataGovernanceMetadataCollectRecordEntity> implements DataGovernanceMetadataCollectRecordService {

	@Override
	public PageResult<DataGovernanceMetadataCollectRecordVO> page(DataGovernanceMetadataCollectRecordQuery query) {
		IPage<DataGovernanceMetadataCollectRecordEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

		return new PageResult<>(DataGovernanceMetadataCollectRecordConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
	}

	private LambdaQueryWrapper<DataGovernanceMetadataCollectRecordEntity> getWrapper(DataGovernanceMetadataCollectRecordQuery query) {
		LambdaQueryWrapper<DataGovernanceMetadataCollectRecordEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(query.getMetadataCollectId() != null, DataGovernanceMetadataCollectRecordEntity::getMetadataCollectId, query.getMetadataCollectId())
				.eq(query.getStatus() != null, DataGovernanceMetadataCollectRecordEntity::getStatus, query.getStatus())
				.gt(query.getStartTime() != null, DataGovernanceMetadataCollectRecordEntity::getStartTime, query.getStartTime())
				.lt(query.getEndTime() != null, DataGovernanceMetadataCollectRecordEntity::getEndTime, query.getEndTime())
				.orderByDesc(DataGovernanceMetadataCollectRecordEntity::getId);
		return wrapper;
	}

	@Override
	public void save(DataGovernanceMetadataCollectRecordVO vo) {
		DataGovernanceMetadataCollectRecordEntity entity = DataGovernanceMetadataCollectRecordConvert.INSTANCE.convert(vo);

		baseMapper.insert(entity);
	}

	@Override
	public void update(DataGovernanceMetadataCollectRecordVO vo) {
		DataGovernanceMetadataCollectRecordEntity entity = DataGovernanceMetadataCollectRecordConvert.INSTANCE.convert(vo);

		updateById(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(List<Long> idList) {
		removeByIds(idList);
	}

	@Override
	public void dealNotFinished() {
		LambdaQueryWrapper<DataGovernanceMetadataCollectRecordEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.in(DataGovernanceMetadataCollectRecordEntity::getStatus, MetadataCollectRunStatus.RUNNING.getCode());
		List<DataGovernanceMetadataCollectRecordEntity> collectRecordEntities = baseMapper.selectList(wrapper);
		for (DataGovernanceMetadataCollectRecordEntity collectRecordEntity : collectRecordEntities) {
			collectRecordEntity.setEndTime(new Date());
			collectRecordEntity.setStatus(MetadataCollectRunStatus.FAILED.getCode());
			String errorLog = DateUtils.formatDateTime(new Date()) + " The collect task has unexpected stop,you can try run again";
			collectRecordEntity.setErrorLog(errorLog);
			collectRecordEntity.setRealTimeLog(collectRecordEntity.getRealTimeLog() == null ? errorLog : collectRecordEntity.getRealTimeLog() + errorLog);
			baseMapper.updateById(collectRecordEntity);
		}
	}

}
