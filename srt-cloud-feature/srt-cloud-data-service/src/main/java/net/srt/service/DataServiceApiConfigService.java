package net.srt.service;

import net.srt.dto.SqlDto;
import net.srt.entity.DataServiceApiGroupEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.vo.DataServiceApiAuthVO;
import net.srt.vo.DataServiceApiConfigVO;
import net.srt.query.DataServiceApiConfigQuery;
import net.srt.entity.DataServiceApiConfigEntity;
import srt.cloud.framework.dbswitch.core.model.JdbcSelectResult;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 数据服务-api配置
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-01-28
 */
public interface DataServiceApiConfigService extends BaseService<DataServiceApiConfigEntity> {

    PageResult<DataServiceApiConfigVO> page(DataServiceApiConfigQuery query);

	PageResult<DataServiceApiConfigVO> pageResource(DataServiceApiConfigQuery query);

    void save(DataServiceApiConfigVO vo);

    void update(DataServiceApiConfigVO vo);

    void delete(List<Long> idList);

	String getIpPort();

	JdbcSelectResult sqlExecute(SqlDto sqlDto);

	void online(Long id);

	void offline(Long id);

	PageResult<DataServiceApiConfigVO> pageAuth(DataServiceApiConfigQuery query);

	List<DataServiceApiConfigEntity> listActive();

	List<DataServiceApiConfigEntity> listActiveByGroupId(Long id);

	DataServiceApiAuthVO getAuthInfo(Long authId);

	void exportDocs(List<Long> ids, HttpServletResponse response);

	String ipPort();

	void resetRequested(Long authId);

}
