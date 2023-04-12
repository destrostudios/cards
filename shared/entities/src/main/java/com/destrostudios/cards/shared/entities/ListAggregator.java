package com.destrostudios.cards.shared.entities;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntPredicate;

public class ListAggregator implements Aggregator {

    public ListAggregator(IntList intList) {
        this.intList = intList;
    }
    private IntList intList;

    @Override
    public IntList list() {
        intList.sort();
        return intList;
    }

    @Override
    public IntList list(IntPredicate predicate) {
        intList.retain(predicate);
        intList.sort();
        return intList;
    }

    @Override
    public int count() {
        return intList.size();
    }

    @Override
    public int count(IntPredicate predicate) {
        AtomicInteger count = new AtomicInteger();
        intList.foreach(value -> {
            if (predicate.test(value)) {
                count.getAndIncrement();
            }
        });
        return count.get();
    }

    @Override
    public int unique() {
        return intList.data()[0];
    }
}
