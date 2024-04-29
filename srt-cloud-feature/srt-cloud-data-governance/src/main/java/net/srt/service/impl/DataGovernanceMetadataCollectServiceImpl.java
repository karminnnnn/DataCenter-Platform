package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.srt.api.module.data.governance.constant.MetadataCollectType;
import net.srt.api.module.quartz.QuartzDataGovernanceMetadataCollectApi;
import net.srt.convert.DataGovernanceMetadataCollectConvert;
import net.srt.dao.DataGovernanceMetadataCollectDao;
import net.srt.dao.DataGovernanceMetadataCollectRecordDao;
import net.srt.entity.DataGovernanceMetadataCollectEntity;
import net.srt.entity.DataGovernanceMetadataCollectRecordEntity;
import net.srt.framework.common.exception.ServerException;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.query.DataGovernanceMetadataCollectQuery;
import net.srt.service.DataGovernanceMetadataCollectRecordService;
import net.srt.service.DataGovernanceMetadataCollectService;
import net.srt.vo.DataGovernanceMetadataCollectVO;
import org.quartz.CronExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.util.StringUtil;

import java.util.Date;
import java.util.List;

/**
 * 数据治理-元数据采集
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-04-01
 */
@Service
@AllArgsConstructor
public class DataGovernanceMetadataCollectServiceImpl extends BaseServiceImpl<DataGovernanceMetadataCollectDao, DataGovernanceMetadataCollectEntity> implements DataGovernanceMetadataCollectService {

	private final QuartzDataGovernanceMetadataCollectApi metadataCollectApi;
	private final DataGovernanceMetadataCollectRecordDao collectRecordDao;

	@Override
	public PageResult<DataGovernanceMetadataCollectVO> page(DataGovernanceMetadataCollectQuery query) {
		IPage<DataGovernanceMetadataCollectEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

		return new PageResult<>(DataGovernanceMetadataCollectConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
	}

	private LambdaQueryWrapper<DataGovernanceMetadataCollectEntity> getWrapper(DataGovernanceMetadataCollectQuery query) {
		LambdaQueryWrapper<DataGovernanceMetadataCollectEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.like(StringUtil.isNotBlank(query.getName()), DataGovernanceMetadataCollectEntity::getName, query.getName())
				.eq(query.getStrategy() != null, DataGovernanceMetadataCollectEntity::getStrategy, query.getStrategy())
				.eq(query.getTaskType() != null, DataGovernanceMetadataCollectEntity::getTaskType, query.getTaskType())
				.orderByDesc(DataGovernanceMetadataCollectEntity::getId);
		dataScopeWithOrgId(wrapper);
		return wrapper;
	}

	@Override
	public void save(DataGovernanceMetadataCollectVO vo) {
		checkParam(vo);
		DataGovernanceMetadataCollectEntity entity = DataGovernanceMetadataCollectConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		baseMapper.insert(entity);
	}

	@Override
	public void update(DataGovernanceMetadataCollectVO vo) {
		checkParam(vo);
		DataGovernanceMetadataCollectEntity entity = DataGovernanceMetadataCollectConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		updateById(entity);
	}

	private void checkParam(DataGovernanceMetadataCollectVO vo) {
		//判断cron表达式
		if (MetadataCollectType.CRON.getValue().equals(vo.getTaskType())) {
			if (!CronExpression.isValidExpression(vo.getCron())) {
				throw new ServerException("cron表达式有误，请检查！");
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(List<Long> idList) {
		removeByIds(idList);
		//删除采集记录
		for (Long id : idList) {
			LambdaQueryWrapper<DataGovernanceMetadataCollectRecordEntity> wrapper = Wrappers.lambdaQuery();
			wrapper.eq(DataGovernanceMetadataCollectRecordEntity::getMetadataCollectId, id);
			collectRecordDao.delete(wrapper);
		}
	}

	@Override
	public void release(Long id) {
		metadataCollectApi.release(id);
		DataGovernanceMetadataCollectEntity entity = baseMapper.selectById(id);
		entity.setReleaseTime(new Date());
		entity.setStatus(1);
		baseMapper.updateById(entity);
	}

	@Override
	public void cancel(Long id) {
		metadataCollectApi.cancel(id);
		DataGovernanceMetadataCollectEntity entity = baseMapper.selectById(id);
		entity.setReleaseTime(null);
		entity.setStatus(0);
		baseMapper.updateById(entity);
	}

	@Override
	public void handRun(Long id) {
		metadataCollectApi.handRun(id);
	}

}
