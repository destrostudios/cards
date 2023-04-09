package com.destrostudios.cards.shared.entities;

import java.util.List;
import java.util.function.IntPredicate;

public interface Aggregator {

    List<Integer> list();

    List<Integer> list(IntPredicate predicate);

    int count();

    int count(IntPredicate predicate);

    int unique();

    int unique(IntPredicate predicate);
}
