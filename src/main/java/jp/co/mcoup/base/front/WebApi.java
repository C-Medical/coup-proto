package jp.co.mcoup.base.front;

import java.lang.reflect.Method;

/**
 *
 * @author izumi_j
 *
 */
public final class WebApi {
	private final String url;
	private final Method method;

	private WebApi(String url, Method method) {
		this.url = url;
		this.method = method;
	}

	public static WebApi of(String url, Method method) {
		return new WebApi(url, method);
	}

}
