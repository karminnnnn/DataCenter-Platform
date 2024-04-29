package net.srt.service;

import net.srt.dto.AuthInfo;
import net.srt.dto.CheckDto;
import net.srt.entity.DataMarketResourceApplyEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.query.DataMarketResourceApplyQuery;
import net.srt.vo.DataMarketResourceApplyVO;

import java.util.List;

/**
 * 数据集市-资源申请表
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-07-26
 */
public interface DataMarketResourceApplyService extends BaseService<DataMarketResourceApplyEntity> {

    PageResult<DataMarketResourceApplyVO> page(DataMarketResourceApplyQuery query);

    void save(DataMarketResourceApplyVO vo);

    void update(DataMarketResourceApplyVO vo);

    void delete(List<Long> idList);

	void check(CheckDto checkDto);

	void auth(Integer id, Integer auth);

	AuthInfo getAuthInfo(Long appId, Long apiId);
}
