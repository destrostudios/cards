package com.destrostudios.cards.shared.rules;

public class Tuple<K, V> {

    public K key;
    public V value;

    public Tuple() {
    }

    public Tuple(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
