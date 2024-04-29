package net.srt.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import net.srt.convert.DataServiceApiLogConvert;
import net.srt.entity.DataServiceApiLogEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.query.DataServiceApiLogQuery;
import net.srt.vo.DataServiceApiLogVO;
import net.srt.dao.DataServiceApiLogDao;
import net.srt.service.DataServiceApiLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.util.StringUtil;

import java.util.List;

/**
 * 数据服务-api请求日志
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-02-22
 */
@Service
@AllArgsConstructor
public class DataServiceApiLogServiceImpl extends BaseServiceImpl<DataServiceApiLogDao, DataServiceApiLogEntity> implements DataServiceApiLogService {

	@Override
	public PageResult<DataServiceApiLogVO> page(DataServiceApiLogQuery query) {
		IPage<DataServiceApiLogEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

		return new PageResult<>(DataServiceApiLogConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
	}

	private LambdaQueryWrapper<DataServiceApiLogEntity> getWrapper(DataServiceApiLogQuery query) {
		LambdaQueryWrapper<DataServiceApiLogEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.like(StringUtil.isNotBlank(query.getIp()), DataServiceApiLogEntity::getIp, query.getIp())
				.like(StringUtil.isNotBlank(query.getApiName()), DataServiceApiLogEntity::getApiName, query.getApiName())
				.orderByDesc(DataServiceApiLogEntity::getCreateTime).orderByDesc(DataServiceApiLogEntity::getId);
		dataScopeWithOrgId(wrapper);
		return wrapper;
	}

	@Override
	public void save(DataServiceApiLogVO vo) {
		DataServiceApiLogEntity entity = DataServiceApiLogConvert.INSTANCE.convert(vo);
		baseMapper.insert(entity);
	}

	@Override
	public void update(DataServiceApiLogVO vo) {
		DataServiceApiLogEntity entity = DataServiceApiLogConvert.INSTANCE.convert(vo);
		updateById(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(List<Long> idList) {
		removeByIds(idList);
	}

}
