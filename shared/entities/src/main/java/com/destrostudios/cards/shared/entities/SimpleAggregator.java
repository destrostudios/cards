package com.destrostudios.cards.shared.entities;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;

public class SimpleAggregator implements Aggregator {

    public SimpleAggregator(Set<Integer> set) {
        this.set = set;
    }
    private Set<Integer> set;

    @Override
    public List<Integer> list() {
        return set.stream().sorted().collect(Collectors.toList());
    }

    @Override
    public List<Integer> list(IntPredicate predicate) {
        return set.stream().filter(predicate::test).sorted().collect(Collectors.toList());
    }

    @Override
    public int count() {
        return set.size();
    }

    @Override
    public int count(IntPredicate predicate) {
        return (int) set.stream().filter(predicate::test).count();
    }

    @Override
    public boolean exists() {
        return !set.isEmpty();
    }

    @Override
    public boolean exists(IntPredicate predicate) {
        return set.stream().anyMatch(predicate::test);
    }

    @Override
    public Optional<Integer> find() {
        return set.stream().findAny();
    }

    @Override
    public Optional<Integer> find(IntPredicate predicate) {
        return set.stream().filter(predicate::test).findAny();
    }

    @Override
    public int unique() {
        return set.iterator().next();
    }

    @Override
    public int unique(IntPredicate predicate) {
        return find(predicate).get();
    }
}
