package net.srt.service.impl;

import lombok.AllArgsConstructor;
import net.srt.convert.DataFieldConvert;
import net.srt.dao.DataFieldDao;
import net.srt.entity.DataFieldEntity;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.service.DataFieldService;
import net.srt.vo.DataFieldVO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DataFieldServiceImpl extends BaseServiceImpl<DataFieldDao, DataFieldEntity> implements DataFieldService {
    private final DataFieldDao dataFieldDao;

    @Override
    @Transactional(readOnly = true)
    public DataFieldVO getFieldInfo(Long fieldId) {
        DataFieldEntity entity = dataFieldDao.selectById(fieldId);
        return DataFieldConvert.INSTANCE.convert(entity);
    }
}
