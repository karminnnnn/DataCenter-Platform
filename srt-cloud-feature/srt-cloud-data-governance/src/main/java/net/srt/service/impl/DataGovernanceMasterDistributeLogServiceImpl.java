package net.srt.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceMasterDistributeLogConvert;
import net.srt.entity.DataGovernanceMasterDistributeLogEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.query.DataGovernanceMasterDistributeLogQuery;
import net.srt.vo.DataGovernanceMasterDistributeLogVO;
import net.srt.dao.DataGovernanceMasterDistributeLogDao;
import net.srt.service.DataGovernanceMasterDistributeLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 数据治理-主数据派发日志
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-10-08
 */
@Service
@AllArgsConstructor
public class DataGovernanceMasterDistributeLogServiceImpl extends BaseServiceImpl<DataGovernanceMasterDistributeLogDao, DataGovernanceMasterDistributeLogEntity> implements DataGovernanceMasterDistributeLogService {

	@Override
	public PageResult<DataGovernanceMasterDistributeLogVO> page(DataGovernanceMasterDistributeLogQuery query) {
		IPage<DataGovernanceMasterDistributeLogEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

		return new PageResult<>(DataGovernanceMasterDistributeLogConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
	}

	private LambdaQueryWrapper<DataGovernanceMasterDistributeLogEntity> getWrapper(DataGovernanceMasterDistributeLogQuery query) {
		LambdaQueryWrapper<DataGovernanceMasterDistributeLogEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(query.getDistributeId() != null, DataGovernanceMasterDistributeLogEntity::getDistributeId, query.getDistributeId())
				.eq(query.getRunStatus() != null, DataGovernanceMasterDistributeLogEntity::getRunStatus, query.getRunStatus())
				.orderByDesc(DataGovernanceMasterDistributeLogEntity::getId);
		return wrapper;
	}

	@Override
	public void save(DataGovernanceMasterDistributeLogVO vo) {
		DataGovernanceMasterDistributeLogEntity entity = DataGovernanceMasterDistributeLogConvert.INSTANCE.convert(vo);

		baseMapper.insert(entity);
	}

	@Override
	public void update(DataGovernanceMasterDistributeLogVO vo) {
		DataGovernanceMasterDistributeLogEntity entity = DataGovernanceMasterDistributeLogConvert.INSTANCE.convert(vo);

		updateById(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(List<Long> idList) {
		removeByIds(idList);
	}

}
