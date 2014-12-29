package jp.co.medicoup.registry;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import jp.co.medicoup.process.sample.dto.SampleDto;

/**
 *
 * @author izumi_j
 *
 */
public final class EntityRegistry {
    private static final Set<Class<?>> REGISTRY = Collections.newSetFromMap(new ConcurrentHashMap<Class<?>, Boolean>());
    static {
        REGISTRY.add(SampleDto.class);
    }

    public static Set<Class<?>> values() {
        return Collections.unmodifiableSet(REGISTRY);
    }

    private EntityRegistry() {
    }
}
