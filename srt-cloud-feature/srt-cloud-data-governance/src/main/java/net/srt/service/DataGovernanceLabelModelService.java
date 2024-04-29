package net.srt.service;

import net.srt.dto.CheckSqlRequest;
import net.srt.dto.CheckSqlResult;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.query.DataGovernanceLabelModelQuery;
import net.srt.vo.DataGovernanceLabelModelVO;
import net.srt.entity.DataGovernanceLabelModelEntity;

import java.util.List;

/**
 * 数据治理-标签实体
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-12-24
 */
public interface DataGovernanceLabelModelService extends BaseService<DataGovernanceLabelModelEntity> {

    PageResult<DataGovernanceLabelModelVO> page(DataGovernanceLabelModelQuery query);

    void save(DataGovernanceLabelModelVO vo);

    void update(DataGovernanceLabelModelVO vo);

    void delete(List<Long> idList);

	CheckSqlResult checkSql(CheckSqlRequest checkSqlRequest);
}
