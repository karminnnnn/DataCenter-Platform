package net.srt.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import net.srt.api.module.data.integrate.DataSourceApi;
import net.srt.api.module.data.integrate.dto.DataDatabaseDto;
import net.srt.constant.SqlDbType;
import net.srt.dao.DataServiceApiConfigDao;
import net.srt.dto.AppToken;
import net.srt.dto.SqlDto;
import net.srt.entity.DataServiceApiConfigEntity;
import net.srt.flink.common.utils.JSONUtil;
import net.srt.flink.process.context.ProcessContextHolder;
import net.srt.framework.common.cache.RedisCache;
import net.srt.framework.common.cache.RedisKeys;
import net.srt.framework.common.cache.bean.DataProjectCacheBean;
import net.srt.framework.common.exception.ServerException;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.service.DataServiceApiExecuteService;
import org.springframework.stereotype.Service;
import srt.cloud.framework.dbswitch.common.type.ProductTypeEnum;
import srt.cloud.framework.dbswitch.core.model.JdbcSelectResult;
import srt.cloud.framework.dbswitch.core.service.IMetaDataByJdbcService;
import srt.cloud.framework.dbswitch.core.service.impl.MetaDataByJdbcServiceImpl;

import java.util.Map;

/**
 * 数据服务-api配置
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-01-28
 */
@Service
@AllArgsConstructor
public class DataServiceApiExecuteServiceImpl extends BaseServiceImpl<DataServiceApiConfigDao, DataServiceApiConfigEntity> implements DataServiceApiExecuteService {

	private final DataSourceApi DataSourceApi;
	private final RedisCache redisCache;

	public JdbcSelectResult sqlExecute(SqlDto sqlDto) {
		Map<String, Object> sqlParam = JSONUtil.parseObject(sqlDto.getJsonParams(), new TypeReference<Map<String, Object>>() {
		});
		boolean ifMiddleDb = SqlDbType.MIDDLE_DB.getValue().equals(sqlDto.getSqlDbType());
		DataDatabaseDto database;
		if (ifMiddleDb) {
			DataProjectCacheBean project = sqlDto.getProjectId() == null ? getProject() : getProject(sqlDto.getProjectId());
			database = new DataDatabaseDto();
			database.setDatabaseName(project.getDbName());
			database.setJdbcUrl(project.getDbUrl());
			database.setUserName(project.getDbUsername());
			database.setPassword(project.getDbPassword());
			database.setDatabaseType(project.getDbType());
		} else {
			database = DataSourceApi.getById(sqlDto.getDatabaseId()).getData();
		}
		try {
			// zrx
			ProductTypeEnum productTypeEnum = ProductTypeEnum.getByIndex(database.getDatabaseType());
			IMetaDataByJdbcService metaDataService = new MetaDataByJdbcServiceImpl(productTypeEnum);
			String jdbcUrl = database.getJdbcUrl();
			String userName = database.getUserName();
			String password = database.getPassword();
			return metaDataService.queryDataByApiSql(jdbcUrl, userName, password, sqlDto.getStatement(), sqlDto.getOpenTrans(), sqlDto.getSqlSeparator(), sqlParam, sqlDto.getSqlMaxRow());
		} finally {
			ProcessContextHolder.clear();
		}
	}

	@Override
	public Long verifyToken(String apiToken) {
		AppToken appToken = JSONUtil.parseObject((String) redisCache.get(RedisKeys.getAppTokenKey(apiToken)), AppToken.class);
		if (appToken == null) {
			throw new ServerException("token 不合法！");
		}
		Long expireTime = appToken.getExpireAt();
		// 单次失效
		if (expireTime == 0) {
			redisCache.delete(apiToken);
			return appToken.getAppId();
		}
		// 永久有效
		else if (expireTime == -1) {
			return appToken.getAppId();
		}
		// 设置了有效的失效时间
		else {
			if (expireTime > System.currentTimeMillis()) {
				return appToken.getAppId();
			} else {
				// token已经过期就清除
				redisCache.delete(apiToken);
				throw new ServerException("token 已过期!");
			}
		}
	}

}
