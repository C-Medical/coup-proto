package jp.co.medicoup.registry;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import jp.co.medicoup.front.sample.SampleController;

public class ControllerRegistry {
    private static final Set<Class<?>> REGISTRY = Collections.newSetFromMap(new ConcurrentHashMap<Class<?>, Boolean>());
    static {
        REGISTRY.add(SampleController.class);
    }

    public static Set<Class<?>> values() {
        return Collections.unmodifiableSet(REGISTRY);
    }

    private ControllerRegistry() {
    }
}
