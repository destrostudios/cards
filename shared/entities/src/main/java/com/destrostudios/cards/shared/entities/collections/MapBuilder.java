package com.destrostudios.cards.shared.entities.collections;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 *
 * @author Philipp
 */
public class MapBuilder<K, V> {

    private final Supplier<Map<K, V>> supply;
    private final Map<K, V> items = new LinkedHashMap<>();

    public MapBuilder() {
        this(LinkedHashMap::new);
    }

    public MapBuilder(Supplier<Map<K, V>> supply) {
        this.supply = supply;
    }

    public MapBuilder<K, V> with(K key, V value) {
        items.put(key, value);
        return this;
    }

    public Map<K, V> build() {
        Map<K, V> map = supply.get();
        map.putAll(items);
        return map;
    }
}
