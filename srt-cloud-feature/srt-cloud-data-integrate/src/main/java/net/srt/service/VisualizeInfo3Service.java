package net.srt.service;

import net.srt.entity.VisualizeInfo3Entity;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.vo.VisualizeInfo3ListVO;
import net.srt.vo.VisualizeInfo3VO;

import java.util.List;

public interface VisualizeInfo3Service extends BaseService<VisualizeInfo3Entity> {
    VisualizeInfo3ListVO getVisualizeInfo3List();
}
