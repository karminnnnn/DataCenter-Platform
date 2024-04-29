package net.srt.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.srt.entity.DataServiceApiConfigEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class MappingHandlerMapping {

	public static final String API_PREFIX = "/api/";
	private static Map<String, DataServiceApiConfigEntity> mappings = new ConcurrentHashMap<>();
	private final RequestMappingHandlerMapping requestMappingHandlerMapping;
	private final MappingRequestHandler handler;
	private Method method;

	{
		try {
			method = MappingRequestHandler.class.getDeclaredMethod(
					"invoke", HttpServletRequest.class, String.class, Map.class, Map.class, Map.class);
		} catch (NoSuchMethodException ignored) {
		}
	}


	public static DataServiceApiConfigEntity getMappingApiInfo(HttpServletRequest request) {
		NativeWebRequest webRequest = new ServletWebRequest(request);
		//获取请求路径
		String requestMapping = (String) webRequest.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
		return getMappingApiInfo(buildMappingKey(request.getMethod(), requestMapping));
	}

	public static DataServiceApiConfigEntity getMappingApiInfo(String key) {
		return mappings.get(key);
	}

	public static String buildMappingKey(String requestMethod, String requestMapping) {
		return requestMethod.toUpperCase() + ":" + requestMapping;
	}

	/**
	 * 注册请求映射
	 *
	 * @param api
	 */
	public void registerMapping(DataServiceApiConfigEntity api) {
		String mappingKey = getMappingKey(api);
		if (mappings.containsKey(mappingKey)) {
			// 取消注册
			mappings.remove(mappingKey);
			requestMappingHandlerMapping.unregisterMapping(getRequestMapping(api));
		}
		log.info("注册接口:{}", api.getName());
		RequestMappingInfo requestMapping = getRequestMapping(api);
		mappings.put(mappingKey, api);
		requestMappingHandlerMapping.registerMapping(requestMapping, handler, method);
	}

	/**
	 * 取消注册请求映射
	 *
	 * @param api
	 */
	public void unregisterMapping(DataServiceApiConfigEntity api) {
		log.info("取消注册接口:{}", api.getName());
		String mappingKey = getMappingKey(api);
		if (mappings.containsKey(mappingKey)) {
			// 取消注册
			mappings.remove(mappingKey);
			requestMappingHandlerMapping.unregisterMapping(getRequestMapping(api));
		}
	}

	private String getMappingKey(DataServiceApiConfigEntity api) {
		return buildMappingKey(api.getType(), API_PREFIX + api.getPath());
	}

	private RequestMappingInfo getRequestMapping(DataServiceApiConfigEntity api) {
		return RequestMappingInfo.paths(API_PREFIX + api.getPath()).methods(RequestMethod.valueOf(api.getType())).build();
	}
}
