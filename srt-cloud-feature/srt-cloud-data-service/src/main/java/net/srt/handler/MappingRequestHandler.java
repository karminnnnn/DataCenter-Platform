package net.srt.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.srt.dao.DataServiceApiAuthDao;
import net.srt.dao.DataServiceApiConfigDao;
import net.srt.dao.DataServiceAppDao;
import net.srt.dto.SqlDto;
import net.srt.entity.DataServiceApiAuthEntity;
import net.srt.entity.DataServiceApiConfigEntity;
import net.srt.entity.DataServiceApiLogEntity;
import net.srt.entity.DataServiceAppEntity;
import net.srt.flink.common.utils.JSONUtil;
import net.srt.flink.common.utils.LogUtil;
import net.srt.framework.common.exception.ErrorCode;
import net.srt.framework.common.exception.ServerException;
import net.srt.framework.common.utils.IpUtils;
import net.srt.framework.common.utils.Result;
import net.srt.service.DataServiceApiExecuteService;
import net.srt.service.DataServiceApiLogService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import srt.cloud.framework.dbswitch.common.util.StringUtil;
import srt.cloud.framework.dbswitch.core.model.JdbcSelectResult;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class MappingRequestHandler {

	private final DataServiceApiConfigDao apiConfigDao;
	private final DataServiceApiExecuteService apiExecuteService;
	private final DataServiceApiAuthDao apiAuthDao;
	private final DataServiceAppDao appDao;
	private final DataServiceApiLogService apiLogService;

	@SneakyThrows
	@ResponseBody
	public Object invoke(HttpServletRequest request,
						 @RequestHeader(required = false) String apiToken,
						 @PathVariable(required = false) Map<String, Object> pathVariables,
						 @RequestParam(required = false) Map<String, Object> requestParams,
						 @RequestBody(required = false) Map<String, Object> requestBodys) {
		DataServiceApiConfigEntity apiConfigEntity = null;
		DataServiceApiAuthEntity authEntity = null;
		//日志
		long now = System.currentTimeMillis();
		DataServiceApiLogEntity apiLogEntity = new DataServiceApiLogEntity();
		apiLogEntity.setUrl(request.getRequestURI());
		apiLogEntity.setIp(IpUtils.getIpAddr(request));
		apiLogEntity.setStatus(HttpStatus.OK.value());

		try {
			Map<String, Object> params = new HashMap<>();
			if (!CollectionUtils.isEmpty(pathVariables)) {
				log.info("pathVariables:{}", pathVariables.toString());
				params.putAll(pathVariables);
			}
			if (!CollectionUtils.isEmpty(requestParams)) {
				log.info("requestParams:{}", requestParams.toString());
				params.putAll(requestParams);
			}
			if (!CollectionUtils.isEmpty(requestBodys)) {
				log.info("requestBodys:{}", requestBodys.toString());
				params.putAll(requestBodys);
			}
			DataServiceApiConfigEntity mappingApiInfo = MappingHandlerMapping.getMappingApiInfo(request);
			//获取最新的api信息
			apiConfigEntity = apiConfigDao.selectById(mappingApiInfo.getId());
			Assert.notNull(apiConfigEntity, "api已被删除，调用失败");
			//日志
			apiLogEntity.setProjectId(apiConfigEntity.getProjectId());
			apiLogEntity.setApiId(apiConfigEntity.getId());
			apiLogEntity.setApiName(apiConfigEntity.getName());

			if (!request.getMethod().equalsIgnoreCase(apiConfigEntity.getType())) {
				throw new ServerException(String.format("不支持的请求类型，请使用 【%s】方式请求", apiConfigEntity.getType()));
			}
			//如果是私有接口，鉴权
			if (apiConfigEntity.getPrevilege() == 1) {
				if (StringUtil.isBlank(apiToken)) {
					throw new ServerException("No Token!");
				}
				//检验token
				Long appId = apiExecuteService.verifyToken(apiToken);
				DataServiceAppEntity appEntity = appDao.selectById(appId);
				Assert.notNull(appEntity, "应用已被删除，调用失败");

				//日志
				apiLogEntity.setOrgId(apiConfigEntity.getOrgId());
				apiLogEntity.setAppId(appEntity.getId());
				apiLogEntity.setAppName(appEntity.getName());

				LambdaQueryWrapper<DataServiceApiAuthEntity> wrapper = new LambdaQueryWrapper<>();
				wrapper.eq(DataServiceApiAuthEntity::getApiId, apiConfigEntity.getId()).eq(DataServiceApiAuthEntity::getAppId, appId).last(" limit 1");
				authEntity = apiAuthDao.selectOne(wrapper);
				if (authEntity == null) {
					throw new ServerException("API 未授权！");
				}
				//判断是否超过次数
				if (authEntity.getRequestTimes() != -1 && authEntity.getRequestedTimes() >= authEntity.getRequestTimes()) {
					throw new ServerException("API 已超出调用次数限制！");
				}
				//判断是否已过期
				if (authEntity.getStartTime() != null && authEntity.getStartTime().getTime() >= System.currentTimeMillis()) {
					throw new ServerException("API 不在有效期内，调用失败！");
				}
				if (authEntity.getEndTime() != null && authEntity.getEndTime().getTime() <= System.currentTimeMillis()) {
					throw new ServerException("API 不在有效期内，调用失败！");
				}
			}

			JdbcSelectResult jdbcSelectResult = apiExecuteService.sqlExecute(SqlDto.builder().databaseId(apiConfigEntity.getDatabaseId())
					.jsonParams(JSONUtil.toJsonString(params))
					.openTrans(apiConfigEntity.getOpenTrans())
					.projectId(apiConfigEntity.getProjectId())
					.sqlDbType(apiConfigEntity.getSqlDbType())
					.sqlSeparator(apiConfigEntity.getSqlSeparator())
					.sqlMaxRow(apiConfigEntity.getSqlMaxRow())
					.statement(apiConfigEntity.getSqlText())
					.build());
			List<JdbcSelectResult> results = jdbcSelectResult.getResults();
			//有任何一条结果错误，则报错
			for (JdbcSelectResult result : results) {
				if (!result.getSuccess()) {
					throw new ServerException(result.getErrorMsg());
				}
			}
			JdbcSelectResult lastResult = results.isEmpty() ? null : results.get(results.size() - 1);
			apiConfigDao.increaseRequestedSuccessTimes(apiConfigEntity.getId());
			if (authEntity != null) {
				apiAuthDao.increaseRequestedSuccessTimes(authEntity.getId());
			}
			return Result.ok(lastResult != null ? ApiResult.builder()/*.sql(lastResult.getSql())*/.ifQuery(lastResult.getIfQuery()).success(lastResult.getSuccess()).errorMsg(lastResult.getErrorMsg())
					.affectedRows(lastResult.getCount()).time(lastResult.getTime()).columns(lastResult.getColumns()).rowData(lastResult.getRowData()).build() : null);
		} catch (Exception e) {
			apiLogEntity.setStatus(ErrorCode.INTERNAL_SERVER_ERROR.getCode());
			apiLogEntity.setError(LogUtil.getError(e));
			if (apiConfigEntity != null) {
				//api失败调用次数++
				apiConfigDao.increaseRequestedFailedTimes(apiConfigEntity.getId());
			}
			if (authEntity != null) {
				//授权api失败调用次数++
				apiAuthDao.increaseRequestedFailedTimes(authEntity.getId());
			}
			throw new ServerException(e.getMessage());
		} finally {
			if (apiConfigEntity != null) {
				//api调用次数++
				apiConfigDao.increaseRequestedTimes(apiConfigEntity.getId());
			}
			if (authEntity != null) {
				//授权api调用次数++
				apiAuthDao.increaseRequestedTimes(authEntity.getId());
			}
			apiLogEntity.setDuration(System.currentTimeMillis() - now);
			//添加日志
			apiLogService.save(apiLogEntity);
		}
	}

}
