package net.srt.service;

import net.srt.dto.SqlDto;
import net.srt.entity.DataServiceApiConfigEntity;
import net.srt.framework.mybatis.service.BaseService;
import srt.cloud.framework.dbswitch.core.model.JdbcSelectResult;

/**
 * 数据服务-api配置
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-01-28
 */
public interface DataServiceApiExecuteService extends BaseService<DataServiceApiConfigEntity> {

	JdbcSelectResult sqlExecute(SqlDto sqlDto);

	Long verifyToken(String apiToken);
}
