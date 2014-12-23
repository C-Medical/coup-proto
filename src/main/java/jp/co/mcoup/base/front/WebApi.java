package jp.co.mcoup.base.front;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 *
 * @author izumi_j
 *
 */
public final class WebApi {
	private final Class<?> clazz;
	private final Method method;

	private WebApi(Class<?> clazz, Method method) {
		this.clazz = clazz;
		this.method = method;
	}

	public static WebApi of(Class<?> clazz, Method method) {
		return new WebApi(clazz, method);
	}

	public Object execute(Map<String, String[]> requestParam) {
		try {
			final Object controller = clazz.newInstance();
			final Object[] params = resolveParameter(requestParam);
			return method.invoke(controller, params);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new IllegalStateException(e);
		}
	}

	private Object[] resolveParameter(Map<String, String[]> requestParam) {
		return null;
	}

}
