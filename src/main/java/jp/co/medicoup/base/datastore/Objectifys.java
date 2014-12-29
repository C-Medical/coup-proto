package jp.co.medicoup.base.datastore;

import jp.co.medicoup.registry.EntityRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.impl.translate.opt.joda.JodaTimeTranslators;

/**
 *
 * @author izumi_j
 *
 */
public final class Objectifys {
    private static final Logger logger = LoggerFactory.getLogger(Objectifys.class);

    private Objectifys() {
    }

    static {
        JodaTimeTranslators.add(factory());

        for (Class<?> clazz : EntityRegistry.values()) {
            factory().register(clazz);
        }

        logger.info("Objectifyの初期化完了。");
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}
