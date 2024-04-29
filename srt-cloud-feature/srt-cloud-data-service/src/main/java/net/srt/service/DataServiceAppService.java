package net.srt.service;

import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.vo.DataServiceApiAuthVO;
import net.srt.vo.DataServiceAppVO;
import net.srt.query.DataServiceAppQuery;
import net.srt.entity.DataServiceAppEntity;

import java.util.List;

/**
 * 数据服务-app应用
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-02-16
 */
public interface DataServiceAppService extends BaseService<DataServiceAppEntity> {

    PageResult<DataServiceAppVO> page(DataServiceAppQuery query);

    void save(DataServiceAppVO vo);

    void update(DataServiceAppVO vo);

    void delete(List<Long> idList);

	void addAuth(DataServiceApiAuthVO authVO);

	void cancelAuth(Long authId);

	String tokenGenerate(String appKey, String appSecret);

	void upAuth(DataServiceApiAuthVO authVO);
}
