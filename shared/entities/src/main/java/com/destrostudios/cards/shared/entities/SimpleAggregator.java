package com.destrostudios.cards.shared.entities;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntPredicate;

public class SimpleAggregator implements Aggregator {

    public SimpleAggregator(IntMap<?> intMap) {
        this.intMap = intMap;
    }
    private IntMap<?> intMap;

    @Override
    public List<Integer> list() {
        LinkedList<Integer> list = new LinkedList<>();
        intMap.foreachKey(list::add);
        list.sort(Comparator.naturalOrder());
        return list;
    }

    @Override
    public List<Integer> list(IntPredicate predicate) {
        LinkedList<Integer> list = new LinkedList<>();
        intMap.foreachKey(value -> {
            if (predicate.test(value)) {
                list.add(value);
            }
        });
        list.sort(Comparator.naturalOrder());
        return list;
    }

    @Override
    public int count() {
        return intMap.size();
    }

    @Override
    public int count(IntPredicate predicate) {
        AtomicInteger count = new AtomicInteger();
        intMap.foreachKey(value -> {
            if (predicate.test(value)) {
                count.getAndIncrement();
            }
        });
        return count.get();
    }

    @Override
    public int unique() {
        return intMap.iterator().next();
    }
}
