package com.destrostudios.cards.shared.entities;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntPredicate;

public class SimpleAggregator implements Aggregator {

    public SimpleAggregator(IntMap<?> intMap) {
        this.intMap = intMap;
    }
    private IntMap<?> intMap;

    @Override
    public IntList list() {
        IntList list = new IntList(intMap.size());
        intMap.foreachKey(list::add);
        list.sort();
        return list;
    }

    @Override
    public IntList list(IntPredicate predicate) {
        IntList list = new IntList(intMap.size());
        intMap.foreachKey(value -> {
            if (predicate.test(value)) {
                list.add(value);
            }
        });
        list.sort();
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
