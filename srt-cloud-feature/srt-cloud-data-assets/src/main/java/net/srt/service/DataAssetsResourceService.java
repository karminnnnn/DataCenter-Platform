package net.srt.service;

import net.srt.entity.DataAssetsResourceEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.query.DataAssetsResourceQuery;
import net.srt.vo.DataAssetsResourceVO;

/**
 * 数据资产-资产列表
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-07-06
 */
public interface DataAssetsResourceService extends BaseService<DataAssetsResourceEntity> {

    PageResult<DataAssetsResourceVO> page(DataAssetsResourceQuery query);

	PageResult<DataAssetsResourceVO> pageMarket(DataAssetsResourceQuery query);

    void save(DataAssetsResourceVO vo);

    void update(DataAssetsResourceVO vo);

    void delete(Long id);

	void online(Long id);

	void offline(Long id);

}
