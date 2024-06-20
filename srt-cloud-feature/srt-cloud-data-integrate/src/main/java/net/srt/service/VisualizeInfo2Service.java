package net.srt.service;

import net.srt.entity.VisualizeInfo2Entity;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.vo.VisualizeInfo2ListVO;

public interface VisualizeInfo2Service extends BaseService<VisualizeInfo2Entity>{
    VisualizeInfo2ListVO getInfoByNetId(int NetId);
}
