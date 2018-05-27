package com.destrostudios.cards.shared.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BinaryOperator;
import java.util.function.IntPredicate;

public class SimpleAggregator<T> implements Aggregator<T> {

    private final Map<Integer, T> map;

    public SimpleAggregator(Map<Integer, T> map) {
        this.map = map;
    }

    @Override
    public int count(IntPredicate predicate) {
        return Math.toIntExact(map.keySet().stream().mapToInt(x -> x).filter(predicate).count());
    }

    @Override
    public Optional<T> compute(BinaryOperator<T> operator, IntPredicate predicate) {
        AtomicReference<T> state = new AtomicReference<>();
        AtomicBoolean first = new AtomicBoolean();
        map.entrySet().forEach(entry -> {
            int entity = entry.getKey();
            if (predicate.test(entity)) {
                T value = entry.getValue();
                if (first.get()) {
                    state.set(value);
                    first.set(false);
                } else {
                    state.accumulateAndGet(value, operator);
                }
            }
        });
        return first.get() ? Optional.empty() : Optional.of(state.get());
    }

    @Override
    public List<Integer> list(IntPredicate predicate) {
        List<Integer> result = new ArrayList<>();
        list(result, predicate);
        return result;
    }

    public void list(List<Integer> out, IntPredicate predicate) {
        map.keySet().stream().mapToInt(x -> x).filter(predicate).sorted().boxed().forEach(out::add);
    }

    @Override
    public OptionalInt unique(IntPredicate predicate) {
        Integer result = null;
        for (Integer entity : map.keySet()) {
            if (predicate.test(entity)) {
                if (result != null) {
                    throw new IllegalStateException("multiple results for unique query");
                }
                result = entity;
            }
        }
        return result != null ? OptionalInt.of(result) : OptionalInt.empty();
    }

}
