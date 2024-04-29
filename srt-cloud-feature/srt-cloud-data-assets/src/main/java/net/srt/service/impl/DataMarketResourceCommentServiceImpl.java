package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.srt.convert.DataMarketResourceCommentConvert;
import net.srt.dao.DataMarketResourceCommentDao;
import net.srt.entity.DataMarketResourceCommentEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.query.DataMarketResourceCommentQuery;
import net.srt.service.DataMarketResourceCommentService;
import net.srt.vo.DataMarketResourceCommentVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.util.StringUtil;

import java.util.List;

/**
 * 数据集市-资源评价表
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-08-27
 */
@Service
@AllArgsConstructor
public class DataMarketResourceCommentServiceImpl extends BaseServiceImpl<DataMarketResourceCommentDao, DataMarketResourceCommentEntity> implements DataMarketResourceCommentService {

	@Override
	public PageResult<DataMarketResourceCommentVO> page(DataMarketResourceCommentQuery query) {
		IPage<DataMarketResourceCommentEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

		return new PageResult<>(DataMarketResourceCommentConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
	}

	private LambdaQueryWrapper<DataMarketResourceCommentEntity> getWrapper(DataMarketResourceCommentQuery query) {
		LambdaQueryWrapper<DataMarketResourceCommentEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(DataMarketResourceCommentEntity::getResourceId, query.getResourceId()).eq(query.getLevel() != null, DataMarketResourceCommentEntity::getLevel, query.getLevel())
				.like(StringUtil.isNotBlank(query.getComment()), DataMarketResourceCommentEntity::getComment, query.getComment()).orderByDesc(DataMarketResourceCommentEntity::getId);
		return wrapper;
	}

	@Override
	public void save(DataMarketResourceCommentVO vo) {
		DataMarketResourceCommentEntity entity = DataMarketResourceCommentConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		baseMapper.insert(entity);
	}

	@Override
	public void update(DataMarketResourceCommentVO vo) {
		DataMarketResourceCommentEntity entity = DataMarketResourceCommentConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		updateById(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(List<Long> idList) {
		removeByIds(idList);
	}

}
