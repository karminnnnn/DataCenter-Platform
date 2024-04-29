package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.srt.convert.DataProductionJarConvert;
import net.srt.dao.DataProductionJarDao;
import net.srt.dao.DataProductionTaskDao;
import net.srt.entity.DataProductionJarEntity;
import net.srt.entity.DataProductionTaskEntity;
import net.srt.framework.common.exception.ServerException;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.query.DataProductionJarQuery;
import net.srt.service.DataProductionJarService;
import net.srt.vo.DataProductionJarVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.util.StringUtil;

import java.util.List;

/**
 * 数据生产-jar管理
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-11-13
 */
@Service
@AllArgsConstructor
public class DataProductionJarServiceImpl extends BaseServiceImpl<DataProductionJarDao, DataProductionJarEntity> implements DataProductionJarService {

	private final DataProductionTaskDao dataProductionTaskDao;

	@Override
	public PageResult<DataProductionJarVO> page(DataProductionJarQuery query) {
		IPage<DataProductionJarEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

		return new PageResult<>(DataProductionJarConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
	}

	private LambdaQueryWrapper<DataProductionJarEntity> getWrapper(DataProductionJarQuery query) {
		LambdaQueryWrapper<DataProductionJarEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.like(StringUtil.isNotBlank(query.getName()), DataProductionJarEntity::getName, query.getName())
				.eq(query.getSubmitType() != null, DataProductionJarEntity::getSubmitType, query.getSubmitType())
				.orderByDesc(DataProductionJarEntity::getId);
		dataScopeWithOrgId(wrapper);

		return wrapper;
	}

	@Override
	public void save(DataProductionJarVO vo) {
		DataProductionJarEntity entity = DataProductionJarConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		baseMapper.insert(entity);
	}

	@Override
	public void update(DataProductionJarVO vo) {
		DataProductionJarEntity entity = DataProductionJarConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		updateById(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(List<Long> idList) {
		removeByIds(idList);
		for (Long id : idList) {
			LambdaQueryWrapper<DataProductionTaskEntity> wrapper = Wrappers.lambdaQuery();
			wrapper.eq(DataProductionTaskEntity::getJarId, id).last("limit 1");
			DataProductionTaskEntity taskEntity = dataProductionTaskDao.selectOne(wrapper);
			if (taskEntity != null) {
				throw new ServerException(String.format("要删除的 Jar 包存在数据生产任务【%s】与之关联，不可删除！", taskEntity.getName()));
			}
		}
	}

	@Override
	public List<DataProductionJarVO> listAllByRunType(Integer jarRunType) {
		LambdaQueryWrapper<DataProductionJarEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(DataProductionJarEntity::getSubmitType, jarRunType);
		dataScopeWithOrgId(wrapper);
		return DataProductionJarConvert.INSTANCE.convertList(list(wrapper));
	}

}
