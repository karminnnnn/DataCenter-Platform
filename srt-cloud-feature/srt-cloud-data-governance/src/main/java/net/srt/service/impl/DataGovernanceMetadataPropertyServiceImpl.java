package net.srt.service.impl;

import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceMetadataPropertyConvert;
import net.srt.dao.DataGovernanceMetadataPropertyDao;
import net.srt.entity.DataGovernanceMetadataPropertyEntity;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.service.DataGovernanceMetadataPropertyService;
import net.srt.vo.DataGovernanceMetadataPropertyVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 数据治理-元数据属性值
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-03-29
 */
@Service
@AllArgsConstructor
public class DataGovernanceMetadataPropertyServiceImpl extends BaseServiceImpl<DataGovernanceMetadataPropertyDao, DataGovernanceMetadataPropertyEntity> implements DataGovernanceMetadataPropertyService {


    @Override
    public void save(DataGovernanceMetadataPropertyVO vo) {
        DataGovernanceMetadataPropertyEntity entity = DataGovernanceMetadataPropertyConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);
    }

    @Override
    public void update(DataGovernanceMetadataPropertyVO vo) {
        DataGovernanceMetadataPropertyEntity entity = DataGovernanceMetadataPropertyConvert.INSTANCE.convert(vo);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

}
