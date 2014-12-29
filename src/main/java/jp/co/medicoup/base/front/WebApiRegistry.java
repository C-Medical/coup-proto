package jp.co.medicoup.base.front;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jp.co.medicoup.base.AppConstants;
import jp.co.medicoup.base.front.provide.PageResult;
import jp.co.medicoup.base.front.provide.annotation.GET;
import jp.co.medicoup.base.front.provide.annotation.POST;
import jp.co.medicoup.registry.ControllerRegistry;

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
        for (Class<?> clazz : ControllerRegistry.values()) {
            // ~Controllerな名前のクラスをWEB-APIとして扱う
            if (!StringUtils.endsWith(clazz.getName(), "Controller")) {
                continue;
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
        logger.info("{}件のWEB-APIの読込完了。", apiMap.size());
    }

    private static String toUrl(String prefix, Class<?> clazz, Method method) {
        // jp.co.mcoup.front.hoge.FugaController >> /hoge/fuga
        String pkgCls = StringUtils.replace(clazz.getName(), ".", "/");
        pkgCls = StringUtils.removeStart(pkgCls, AppConstants.FRONT_PACKAGE_PATH);
        pkgCls = StringUtils.removeEnd(pkgCls, "Controller");
        pkgCls = StringUtils.lowerCase(pkgCls);

        // /prefix/hoge/fuga/doSomething
        return StringUtils.join("/", prefix, pkgCls, "/", method.getName());
    }

    public static WebApi getWebApi(String url) {
        return apiMap.get(url);
    }

}
