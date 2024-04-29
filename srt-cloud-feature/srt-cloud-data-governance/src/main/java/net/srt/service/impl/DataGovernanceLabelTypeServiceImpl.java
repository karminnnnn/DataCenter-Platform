package net.srt.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceLabelTypeConvert;
import net.srt.dao.DataGovernanceLabelTypeDao;
import net.srt.entity.DataGovernanceLabelTypeEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.query.DataGovernanceLabelTypeQuery;
import net.srt.vo.DataGovernanceLabelTypeVO;
import net.srt.service.DataGovernanceLabelTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.util.StringUtil;

import java.util.List;

/**
 * 数据治理-标签类型
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-12-24
 */
@Service
@AllArgsConstructor
public class DataGovernanceLabelTypeServiceImpl extends BaseServiceImpl<DataGovernanceLabelTypeDao, DataGovernanceLabelTypeEntity> implements DataGovernanceLabelTypeService {

	@Override
	public PageResult<DataGovernanceLabelTypeVO> page(DataGovernanceLabelTypeQuery query) {
		IPage<DataGovernanceLabelTypeEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

		return new PageResult<>(DataGovernanceLabelTypeConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
	}

	private LambdaQueryWrapper<DataGovernanceLabelTypeEntity> getWrapper(DataGovernanceLabelTypeQuery query) {
		LambdaQueryWrapper<DataGovernanceLabelTypeEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.like(StringUtil.isNotBlank(query.getName()), DataGovernanceLabelTypeEntity::getName, query.getName());
		return wrapper;
	}

	@Override
	public void save(DataGovernanceLabelTypeVO vo) {
		DataGovernanceLabelTypeEntity entity = DataGovernanceLabelTypeConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		baseMapper.insert(entity);
	}

	@Override
	public void update(DataGovernanceLabelTypeVO vo) {
		DataGovernanceLabelTypeEntity entity = DataGovernanceLabelTypeConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		updateById(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(List<Long> idList) {
		//TODO，判断有无关联
		removeByIds(idList);
	}

}
