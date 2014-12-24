package jp.co.medicoup.base.front;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.medicoup.base.front.provide.annotation.GET;
import jp.co.medicoup.base.front.provide.annotation.Json;
import jp.co.medicoup.base.front.provide.annotation.POST;
import jp.co.medicoup.base.front.provide.annotation.Param;
import jp.co.medicoup.base.json.ObjectMapperHolder;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * WEB-APIクラス。
 *
 * @author izumi_j
 *
 */
public final class WebApi {
	private static final Logger logger = LoggerFactory.getLogger(WebApi.class);

	private final Class<?> clazz;
	private final Method method;

	private WebApi(Class<?> clazz, Method method) {
		this.clazz = clazz;
		this.method = method;
	}

	/**
	 * @param clazz
	 * @param method
	 * @return api
	 */
	public static WebApi of(Class<?> clazz, Method method) {
		return new WebApi(clazz, method);
	}

	/**
	 * Httpメソッドがサポートされているかどうか。
	 *
	 * @param method
	 * @return サポートされている場合にtrue
	 */
	public boolean isSupportedMethod(String method) {
		if (StringUtils.equals(method, GET.class.getSimpleName())) {
			return this.method.isAnnotationPresent(GET.class);
		}
		if (StringUtils.equals(method, POST.class.getSimpleName())) {
			return this.method.isAnnotationPresent(POST.class);
		}
		logger.warn("サポートしていないHttpメソッドです！ >> {}", method);
		return false;
	}

	/**
	 * WEB-APIを実行する。
	 *
	 * @param requestParam
	 * @return result
	 */
	public Object execute(Map<String, String[]> requestParam) {
		try {
			final Object controller = clazz.newInstance();
			final Object[] params = resolveParameters(requestParam);
			return method.invoke(controller, params);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * HttpRequestのパラメータをControllerが利用できるように解決する。
	 *
	 * @param requestParam
	 * @return parameter
	 */
	private Object[] resolveParameters(Map<String, String[]> requestParam) {
		final List<Object> result = new ArrayList<>();

		final Map<String, Object> paramToUse = convertRequestParameter(requestParam);

		final Class<?>[] parameterClasses = method.getParameterTypes();
		final Type[] parameterTypes = method.getGenericParameterTypes();
		final Annotation[][] parametersAnnotations = method.getParameterAnnotations();

		for (int i = 0; i < parameterClasses.length; i++) {
			final Class<?> paramClass = parameterClasses[i];
			final Type paramType = parameterTypes[i];

			Param paramAnnotation = null;
			Json jsonAnnotation = null;

			for (Annotation a : parametersAnnotations[i]) {
				if (a.annotationType().equals(Param.class)) {
					paramAnnotation = (Param)a;
					continue;
				}
				if (a.annotationType().equals(Json.class)) {
					jsonAnnotation = (Json)a;
				}
			}

			final Object value = resolveParameter(paramToUse, paramAnnotation, paramClass, paramType,
					jsonAnnotation != null);
			result.add(value);
		}

		return result.size() > 0 ? result.toArray(new Object[result.size()]) : null;
	}

	/**
	 * Controllerのメソッドの引数単位でのパラメータ解決。
	 *
	 * @param requestParam
	 * @param paramAnnotation
	 * @param paramClass
	 * @param paramType
	 * @return parameter
	 */
	@SuppressWarnings("unchecked")
	private Object resolveParameter(Map<String, Object> requestParam, Param paramAnnotation, Class<?> paramClass,
			Type paramType, boolean isJson) {
		final ObjectMapper mapper = ObjectMapperHolder.get();

		String source = null;
		JavaType javaType = null;

		try {
			if (StringUtils.isEmpty(paramAnnotation.value())) {
				// パラメータ名が未指定の場合は、requestParamを丸ごと変換する
				source = mapper.writeValueAsString(requestParam);
			} else {
				// パラメータ名が指定されている場合は、その値を変換する
				final Object val = requestParam.get(paramAnnotation.value());
				if (val == null) {
					return null;
				}

				if (isJson) {
					source = (String)val;
				} else {
					source = mapper.writeValueAsString(val);
				}
			}

			if (Collection.class.isAssignableFrom(paramClass)) {
				final ParameterizedType parameterizedType = (ParameterizedType)paramType;
				final Type actualType = parameterizedType.getActualTypeArguments()[0];
				javaType = mapper.getTypeFactory().constructCollectionType((Class<? extends Collection<?>>)paramClass,
						(Class<?>)actualType);
			}

			if (javaType == null) {
				return mapper.readValue(source, paramClass);
			} else {
				return mapper.readValue(source, javaType);
			}

		} catch (IOException e) {
			logger.error("パラメータの解決に失敗しました！ 変換先 = {}", paramClass.getName(), e);
			return null;
		}
	}

	/**
	 * JSON変換の都合上、要素数が１つしかないパラメータに関して、String配列→単一のStringに変更する。
	 *
	 * @param requestParam
	 * @return converted
	 */
	private Map<String, Object> convertRequestParameter(Map<String, String[]> requestParam) {
		final Map<String, Object> result = new HashMap<>();
		for (Map.Entry<String, String[]> entry : requestParam.entrySet()) {
			if (entry.getValue().length == 0) {
				result.put(entry.getKey(), StringUtils.EMPTY);
			} else if (entry.getValue().length == 1) {
				result.put(entry.getKey(), entry.getValue()[0]);
			} else {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}
}
