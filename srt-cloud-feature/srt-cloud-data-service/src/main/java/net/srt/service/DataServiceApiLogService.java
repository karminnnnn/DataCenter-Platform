package net.srt.service;

import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.vo.DataServiceApiLogVO;
import net.srt.query.DataServiceApiLogQuery;
import net.srt.entity.DataServiceApiLogEntity;

import java.util.List;

/**
 * 数据服务-api请求日志
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-02-22
 */
public interface DataServiceApiLogService extends BaseService<DataServiceApiLogEntity> {

    PageResult<DataServiceApiLogVO> page(DataServiceApiLogQuery query);

    void save(DataServiceApiLogVO vo);

    void update(DataServiceApiLogVO vo);

    void delete(List<Long> idList);
}
