package net.srt.service;

import net.srt.entity.DataMarketResourceCommentEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.query.DataMarketResourceCommentQuery;
import net.srt.vo.DataMarketResourceCommentVO;

import java.util.List;

/**
 * 数据集市-资源评价表
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-08-27
 */
public interface DataMarketResourceCommentService extends BaseService<DataMarketResourceCommentEntity> {

    PageResult<DataMarketResourceCommentVO> page(DataMarketResourceCommentQuery query);

    void save(DataMarketResourceCommentVO vo);

    void update(DataMarketResourceCommentVO vo);

    void delete(List<Long> idList);
}
