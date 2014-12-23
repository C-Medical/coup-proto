package jp.co.mcoup.base.front;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jp.co.mcoup.base.AppConstants;
import jp.co.mcoup.base.front.provide.PageResult;
import jp.co.mcoup.base.front.provide.annotation.Controller;
import jp.co.mcoup.base.front.provide.annotation.GET;
import jp.co.mcoup.base.front.provide.annotation.POST;
import jp.co.mcoup.base.util.ClassScanner;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author izumi_j
 *
 */
public final class WebApiRegistry {
	private static final Logger logger = LoggerFactory.getLogger(WebApiRegistry.class);

	private WebApiRegistry() {
	}

	private static final Map<String, WebApi> apiMap = new ConcurrentHashMap<>();

	/**
	 * WEB-APIを読み込みます。
	 */
	synchronized public static void initialize() {
		ClassScanner.scan(AppConstants.FRONT_PACKAGE_PATH, new WebApiLoader());
		logger.info("{}件のWEB-APIの読込完了。", apiMap.size());
	}

	public static WebApi getWebApi(String url) {
		return apiMap.get(url);
	}

	private static class WebApiLoader implements ClassScanner.ClassVisitor {

		@Override
		public boolean accept(String className) {
			return StringUtils.endsWith(className, "Controller");
		}

		@Override
		public void visit(Class<?> clazz) {
			if (!clazz.isAnnotationPresent(Controller.class)) {
				logger.warn("クラス名が'Controller'で終わっているが、@Controllerが付与されていないクラスがあります >> {}", clazz.getName());
				return;
			}

			// publicで@GETか@POSTが付与されているメソッドが対象
			for (Method method : clazz.getMethods()) {
				if (!Modifier.isPublic(method.getModifiers())) {
					continue;
				}
				if (!method.isAnnotationPresent(GET.class) && !method.isAnnotationPresent(POST.class)) {
					continue;
				}

				String url = null;
				if (PageResult.class.equals(method.getReturnType())) {
					url = toUrl("page", clazz, method);
				} else {
					url = toUrl("ajax", clazz, method);
				}

				if (apiMap.containsKey(url)) {
					logger.warn("URLが重複しています！ >> {}", url);
				}

				apiMap.put(url, WebApi.of(clazz, method));
				logger.debug("WEB-API登録 : {}#{} >> {}", clazz.getName(), method.getName(), url);
			}
		}

		private String toUrl(String prefix, Class<?> clazz, Method method) {
			// jp.co.mcoup.front.hoge.FugaController >> /hoge/fuga
			String pkgCls = StringUtils.replace(clazz.getName(), ".", "/");
			pkgCls = StringUtils.removeStart(pkgCls, AppConstants.FRONT_PACKAGE_PATH);
			pkgCls = StringUtils.removeEnd(pkgCls, "Controller");
			pkgCls = StringUtils.lowerCase(pkgCls);

			// /prefix/hoge/fuga/doSomething
			return StringUtils.join("/", prefix, pkgCls, "/", method.getName());
		}
	}
}
