package net.srt.service;

import net.srt.entity.DataFieldEntity;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.vo.DataFieldVO;

public interface DataFieldService extends BaseService<DataFieldEntity> {
    DataFieldVO getFieldInfo(Long fieldId);

}
