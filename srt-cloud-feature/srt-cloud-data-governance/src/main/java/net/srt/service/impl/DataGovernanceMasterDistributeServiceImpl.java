package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.srt.api.module.quartz.QuartzDataGovernanceMasterApi;
import net.srt.convert.DataGovernanceMasterDistributeConvert;
import net.srt.dao.DataGovernanceMasterDistributeDao;
import net.srt.entity.DataGovernanceMasterDistributeEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.framework.security.user.SecurityUser;
import net.srt.query.DataGovernanceMasterDistributeQuery;
import net.srt.service.DataGovernanceMasterDistributeService;
import net.srt.vo.DataGovernanceMasterDistributeVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.util.StringUtil;

import java.util.Date;
import java.util.List;

/**
 * 数据治理-主数据派发
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-10-08
 */
@Service
@AllArgsConstructor
public class DataGovernanceMasterDistributeServiceImpl extends BaseServiceImpl<DataGovernanceMasterDistributeDao, DataGovernanceMasterDistributeEntity> implements DataGovernanceMasterDistributeService {

	private final QuartzDataGovernanceMasterApi masterApi;

	@Override
	public PageResult<DataGovernanceMasterDistributeVO> page(DataGovernanceMasterDistributeQuery query) {
		IPage<DataGovernanceMasterDistributeEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

		return new PageResult<>(DataGovernanceMasterDistributeConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
	}

	private LambdaQueryWrapper<DataGovernanceMasterDistributeEntity> getWrapper(DataGovernanceMasterDistributeQuery query) {
		LambdaQueryWrapper<DataGovernanceMasterDistributeEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.like(StringUtil.isNotBlank(query.getName()), DataGovernanceMasterDistributeEntity::getName, query.getName())
				.eq(query.getDistributeType() != null, DataGovernanceMasterDistributeEntity::getDistributeType, query.getDistributeType())
				.eq(query.getStatus() != null, DataGovernanceMasterDistributeEntity::getStatus, query.getStatus())
				.orderByDesc(DataGovernanceMasterDistributeEntity::getId);
		dataScopeWithOrgId(wrapper);
		return wrapper;
	}

	@Override
	public void save(DataGovernanceMasterDistributeVO vo) {
		DataGovernanceMasterDistributeEntity entity = DataGovernanceMasterDistributeConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		baseMapper.insert(entity);
	}

	@Override
	public void update(DataGovernanceMasterDistributeVO vo) {
		DataGovernanceMasterDistributeEntity entity = DataGovernanceMasterDistributeConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		updateById(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(List<Long> idList) {
		removeByIds(idList);
		for (Long id : idList) {
			masterApi.cancel(id);
		}
	}

	@Override
	public void release(Long id) {
		DataGovernanceMasterDistributeEntity distributeEntity = baseMapper.selectById(id);
		distributeEntity.setStatus(1);
		distributeEntity.setReleaseTime(new Date());
		distributeEntity.setReleaseUserId(SecurityUser.getUserId());
		//发布
		masterApi.release(id);
		baseMapper.updateById(distributeEntity);
	}

	@Override
	public void cancel(Long id) {
		DataGovernanceMasterDistributeEntity distributeEntity = baseMapper.selectById(id);
		distributeEntity.setStatus(0);
		distributeEntity.setReleaseTime(null);
		distributeEntity.setReleaseUserId(null);
		masterApi.cancel(id);
		baseMapper.updateById(distributeEntity);
	}

	@Override
	public void handRun(Long id) {
		masterApi.handRun(id);
	}

}
