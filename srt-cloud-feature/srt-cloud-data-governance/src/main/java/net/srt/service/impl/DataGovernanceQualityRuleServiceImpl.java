package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceQualityRuleConvert;
import net.srt.dao.DataGovernanceQualityRuleDao;
import net.srt.entity.DataGovernanceQualityRuleEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.query.DataGovernanceQualityRuleQuery;
import net.srt.service.DataGovernanceQualityRuleService;
import net.srt.vo.DataGovernanceQualityRuleVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.util.StringUtil;

import java.util.List;

/**
 * 数据治理-质量规则
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-05-28
 */
@Service
@AllArgsConstructor
public class DataGovernanceQualityRuleServiceImpl extends BaseServiceImpl<DataGovernanceQualityRuleDao, DataGovernanceQualityRuleEntity> implements DataGovernanceQualityRuleService {

	@Override
	public PageResult<DataGovernanceQualityRuleVO> page(DataGovernanceQualityRuleQuery query) {
		IPage<DataGovernanceQualityRuleEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

		return new PageResult<>(DataGovernanceQualityRuleConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
	}

	private LambdaQueryWrapper<DataGovernanceQualityRuleEntity> getWrapper(DataGovernanceQualityRuleQuery query) {
		LambdaQueryWrapper<DataGovernanceQualityRuleEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.like(StringUtil.isNotBlank(query.getName()), DataGovernanceQualityRuleEntity::getName, query.getName())
				.like(StringUtil.isNotBlank(query.getEngName()), DataGovernanceQualityRuleEntity::getEngName, query.getEngName());
		return wrapper;
	}

	@Override
	public void save(DataGovernanceQualityRuleVO vo) {
		DataGovernanceQualityRuleEntity entity = DataGovernanceQualityRuleConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		baseMapper.insert(entity);
	}

	@Override
	public void update(DataGovernanceQualityRuleVO vo) {
		DataGovernanceQualityRuleEntity entity = DataGovernanceQualityRuleConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		updateById(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(List<Long> idList) {
		removeByIds(idList);
	}

}
