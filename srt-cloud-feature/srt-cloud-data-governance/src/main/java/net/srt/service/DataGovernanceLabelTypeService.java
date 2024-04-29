package net.srt.service;

import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.vo.DataGovernanceLabelTypeVO;
import net.srt.query.DataGovernanceLabelTypeQuery;
import net.srt.entity.DataGovernanceLabelTypeEntity;

import java.util.List;

/**
 * 数据治理-标签类型
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-12-24
 */
public interface DataGovernanceLabelTypeService extends BaseService<DataGovernanceLabelTypeEntity> {

    PageResult<DataGovernanceLabelTypeVO> page(DataGovernanceLabelTypeQuery query);

    void save(DataGovernanceLabelTypeVO vo);

    void update(DataGovernanceLabelTypeVO vo);

    void delete(List<Long> idList);
}
