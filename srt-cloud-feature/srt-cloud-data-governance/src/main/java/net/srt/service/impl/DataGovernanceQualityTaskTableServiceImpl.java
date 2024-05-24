package net.srt.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import net.srt.api.module.data.governance.dto.quality.QulaityColumn;
import net.srt.convert.DataGovernanceQualityTaskTableConvert;
import net.srt.dao.DataGovernanceQualityTaskColumnDao;
import net.srt.entity.DataGovernanceQualityTaskColumnEntity;
import net.srt.entity.DataGovernanceQualityTaskTableEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.query.DataGovernanceQualityTaskTableQuery;
import net.srt.vo.DataGovernanceQualityTaskTableVO;
import net.srt.dao.DataGovernanceQualityTaskTableDao;
import net.srt.service.DataGovernanceQualityTaskTableService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.util.SingletonObject;
import srt.cloud.framework.dbswitch.common.util.StringUtil;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据治理-表检测记录
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-06-23
 */
@Service
@AllArgsConstructor
public class DataGovernanceQualityTaskTableServiceImpl extends BaseServiceImpl<DataGovernanceQualityTaskTableDao, DataGovernanceQualityTaskTableEntity> implements DataGovernanceQualityTaskTableService {

	private final DataGovernanceQualityTaskColumnDao taskColumnDao;

	@SneakyThrows
	@Override
	public PageResult<DataGovernanceQualityTaskTableVO> page(DataGovernanceQualityTaskTableQuery query) {
		IPage<DataGovernanceQualityTaskTableEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));
		List<DataGovernanceQualityTaskTableVO> taskTableVos = DataGovernanceQualityTaskTableConvert.INSTANCE.convertList(page.getRecords());
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		for (DataGovernanceQualityTaskTableVO taskTableVo : taskTableVos) {
			taskTableVo.setColumnInfo(SingletonObject.OBJECT_MAPPER.readValue(SingletonObject.OBJECT_MAPPER.writeValueAsString(taskTableVo.getColumnInfo()), new TypeReference<List<QulaityColumn>>() {
			}));
			taskTableVo.setCheckColumns(taskTableVo.getColumnInfo().stream().map(QulaityColumn::getColumnName).collect(Collectors.joining(",")));
			Double checkCount = taskTableVo.getCheckCount().doubleValue();
			Double passCount = taskTableVo.getPassCount().doubleValue();
			if (checkCount == 0) {
				taskTableVo.setPassRate("-");
			} else {
				taskTableVo.setPassRate(decimalFormat.format((passCount / checkCount) * 100) + "%");
			}
		}
		return new PageResult<>(taskTableVos, page.getTotal());
	}

	private LambdaQueryWrapper<DataGovernanceQualityTaskTableEntity> getWrapper(DataGovernanceQualityTaskTableQuery query) {
		LambdaQueryWrapper<DataGovernanceQualityTaskTableEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(query.getQualityTaskId() != null, DataGovernanceQualityTaskTableEntity::getQualityTaskId, query.getQualityTaskId())
				.like(StringUtil.isNotBlank(query.getTableName()), DataGovernanceQualityTaskTableEntity::getTableName, query.getTableName())
				.eq(query.getStatus() != null, DataGovernanceQualityTaskTableEntity::getStatus, query.getStatus())
				.ge(query.getStartTime() != null, DataGovernanceQualityTaskTableEntity::getStartTime, query.getStartTime())
				.le(query.getEndTime() != null, DataGovernanceQualityTaskTableEntity::getEndTime, query.getEndTime())
				.orderByDesc(DataGovernanceQualityTaskTableEntity::getId);
		return wrapper;
	}

	@Override
	public void save(DataGovernanceQualityTaskTableVO vo) {
		DataGovernanceQualityTaskTableEntity entity = DataGovernanceQualityTaskTableConvert.INSTANCE.convert(vo);

		baseMapper.insert(entity);
	}

	@Override
	public void update(DataGovernanceQualityTaskTableVO vo) {
		DataGovernanceQualityTaskTableEntity entity = DataGovernanceQualityTaskTableConvert.INSTANCE.convert(vo);

		updateById(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(List<Long> idList) {
		for (Long id : idList) {
			//删除表和字段记录
			removeById(id);
			LambdaQueryWrapper<DataGovernanceQualityTaskColumnEntity> taskColumnWrapper = Wrappers.lambdaQuery();
			taskColumnWrapper.eq(DataGovernanceQualityTaskColumnEntity::getQualityTaskTableId, id);
			taskColumnDao.delete(taskColumnWrapper);
		}
	}

}
