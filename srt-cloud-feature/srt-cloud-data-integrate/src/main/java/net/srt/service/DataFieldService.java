package net.srt.service;

import net.srt.entity.DataFieldEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.query.DataFieldQuery;
import net.srt.query.DataTableQuery;
import net.srt.vo.DataFieldVO;
import net.srt.vo.DataTableVO;

import java.util.List;

public interface DataFieldService extends BaseService<DataFieldEntity> {
    PageResult<DataFieldVO> page(DataFieldQuery query);

    void save(DataFieldVO vo);

    void update(DataFieldVO vo);

    void delete(List<Long> idList);

}
