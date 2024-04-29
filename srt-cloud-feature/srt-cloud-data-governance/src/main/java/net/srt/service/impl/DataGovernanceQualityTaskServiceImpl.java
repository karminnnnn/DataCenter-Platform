package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.srt.api.module.data.governance.constant.MetadataCollectRunStatus;
import net.srt.api.module.data.integrate.constant.CommonRunStatus;
import net.srt.convert.DataGovernanceQualityTaskConvert;
import net.srt.dao.DataGovernanceQualityTaskColumnDao;
import net.srt.dao.DataGovernanceQualityTaskDao;
import net.srt.dao.DataGovernanceQualityTaskTableDao;
import net.srt.entity.DataGovernanceQualityTaskColumnEntity;
import net.srt.entity.DataGovernanceQualityTaskEntity;
import net.srt.entity.DataGovernanceQualityTaskTableEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.DateUtils;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.query.DataGovernanceQualityTaskQuery;
import net.srt.service.DataGovernanceQualityTaskService;
import net.srt.vo.DataGovernanceQualityTaskVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.util.StringUtil;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * 数据治理-质量任务
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-06-23
 */
@Service
@AllArgsConstructor
public class DataGovernanceQualityTaskServiceImpl extends BaseServiceImpl<DataGovernanceQualityTaskDao, DataGovernanceQualityTaskEntity> implements DataGovernanceQualityTaskService {

	private final DataGovernanceQualityTaskTableDao taskTableDao;
	private final DataGovernanceQualityTaskColumnDao taskColumnDao;

	@Override
	public PageResult<DataGovernanceQualityTaskVO> page(DataGovernanceQualityTaskQuery query) {
		IPage<DataGovernanceQualityTaskEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));
		List<DataGovernanceQualityTaskVO> taskVos = DataGovernanceQualityTaskConvert.INSTANCE.convertList(page.getRecords());
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		for (DataGovernanceQualityTaskVO taskVo : taskVos) {
			Double checkCount = taskVo.getCheckCount().doubleValue();
			Double passCount = taskVo.getPassCount().doubleValue();
			if (checkCount == 0) {
				taskVo.setPassRate("-");
			} else {
				taskVo.setPassRate(decimalFormat.format((passCount / checkCount) * 100) + "%");
			}
		}
		return new PageResult<>(taskVos, page.getTotal());
	}

	private LambdaQueryWrapper<DataGovernanceQualityTaskEntity> getWrapper(DataGovernanceQualityTaskQuery query) {
		LambdaQueryWrapper<DataGovernanceQualityTaskEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.like(StringUtil.isNotBlank(query.getName()), DataGovernanceQualityTaskEntity::getName, query.getName())
				.eq(query.getStatus() != null, DataGovernanceQualityTaskEntity::getStatus, query.getStatus())
				.ge(query.getStartTime() != null, DataGovernanceQualityTaskEntity::getStartTime, query.getStartTime())
				.le(query.getEndTime() != null, DataGovernanceQualityTaskEntity::getEndTime, query.getEndTime())
				.orderByDesc(DataGovernanceQualityTaskEntity::getId);
		dataScopeWithOrgId(wrapper);
		return wrapper;
	}

	@Override
	public void save(DataGovernanceQualityTaskVO vo) {
		DataGovernanceQualityTaskEntity entity = DataGovernanceQualityTaskConvert.INSTANCE.convert(vo);

		baseMapper.insert(entity);
	}

	@Override
	public void update(DataGovernanceQualityTaskVO vo) {
		DataGovernanceQualityTaskEntity entity = DataGovernanceQualityTaskConvert.INSTANCE.convert(vo);

		updateById(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(List<Long> idList) {
		for (Long id : idList) {
			//删除表和字段记录
			removeById(id);
			LambdaUpdateWrapper<DataGovernanceQualityTaskTableEntity> taskTableWrapper = Wrappers.lambdaUpdate();
			taskTableWrapper.eq(DataGovernanceQualityTaskTableEntity::getQualityTaskId, id);
			taskTableDao.delete(taskTableWrapper);
			LambdaUpdateWrapper<DataGovernanceQualityTaskColumnEntity> taskColumnWrapper = Wrappers.lambdaUpdate();
			taskColumnWrapper.eq(DataGovernanceQualityTaskColumnEntity::getQualityTaskId, id);
			taskColumnDao.delete(taskColumnWrapper);
		}
	}

	@Override
	public void dealNotFinished() {
		LambdaQueryWrapper<DataGovernanceQualityTaskEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.in(DataGovernanceQualityTaskEntity::getStatus, CommonRunStatus.WAITING.getCode(), MetadataCollectRunStatus.RUNNING.getCode());
		List<DataGovernanceQualityTaskEntity> qualityTaskEntities = baseMapper.selectList(wrapper);
		for (DataGovernanceQualityTaskEntity qualityTaskEntity : qualityTaskEntities) {
			qualityTaskEntity.setEndTime(new Date());
			qualityTaskEntity.setStatus(CommonRunStatus.FAILED.getCode());
			String errorLog = DateUtils.formatDateTime(new Date()) + " The collect task has unexpected stop,you can try run again";
			qualityTaskEntity.setErrorLog(errorLog);
			baseMapper.updateById(qualityTaskEntity);
		}
	}

}
