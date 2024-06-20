package net.srt.service;

import net.srt.entity.VisualizeInfoEntity;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.vo.VisualizeInfoListVO;

public interface VisualizeInfoService extends BaseService<VisualizeInfoEntity> {
    VisualizeInfoListVO getVisualizeInfoList();
}
