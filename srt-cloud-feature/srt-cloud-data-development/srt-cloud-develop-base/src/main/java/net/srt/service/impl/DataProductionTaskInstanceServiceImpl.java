package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.srt.convert.DataProductionTaskInstanceConvert;
import net.srt.dao.DataProductionTaskDao;
import net.srt.dao.DataProductionTaskInstanceDao;
import net.srt.entity.DataProductionTaskEntity;
import net.srt.entity.DataProductionTaskInstanceEntity;
import net.srt.flink.gateway.GatewayType;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.query.DataProductionTaskInstanceQuery;
import net.srt.service.DataProductionTaskInstanceService;
import net.srt.vo.DataProductionTaskInstanceVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 数据生产任务实例
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-12-20
 */
@Service
@AllArgsConstructor
public class DataProductionTaskInstanceServiceImpl extends BaseServiceImpl<DataProductionTaskInstanceDao, DataProductionTaskInstanceEntity> implements DataProductionTaskInstanceService {

	private final DataProductionTaskDao taskDao;

    @Override
    public PageResult<DataProductionTaskInstanceVO> page(DataProductionTaskInstanceQuery query) {
        IPage<DataProductionTaskInstanceEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(DataProductionTaskInstanceConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }

    private LambdaQueryWrapper<DataProductionTaskInstanceEntity> getWrapper(DataProductionTaskInstanceQuery query){
        LambdaQueryWrapper<DataProductionTaskInstanceEntity> wrapper = Wrappers.lambdaQuery();

        return wrapper;
    }

    @Override
    public void save(DataProductionTaskInstanceVO vo) {
        DataProductionTaskInstanceEntity entity = DataProductionTaskInstanceConvert.INSTANCE.convert(vo);
		baseMapper.insert(entity);
    }

    @Override
    public void update(DataProductionTaskInstanceVO vo) {
        DataProductionTaskInstanceEntity entity = DataProductionTaskInstanceConvert.INSTANCE.convert(vo);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

	@Override
	public DataProductionTaskInstanceEntity getByIdWithoutTenant(Integer id) {
		DataProductionTaskInstanceEntity instanceEntity = baseMapper.selectById(id);
		DataProductionTaskEntity taskEntity = taskDao.selectById(instanceEntity.getTaskId());
		instanceEntity.setTaskType(GatewayType.getByCode(taskEntity.getType().toString()).getLongValue());
		return instanceEntity;
	}

	@Override
	public DataProductionTaskInstanceEntity getJobInstanceByTaskId(Integer id) {
		return baseMapper.getJobInstanceByTaskId(id);
	}

	@Override
	public List<DataProductionTaskInstanceEntity> listJobInstanceActive() {
		return baseMapper.listJobInstanceActive();
	}

}
