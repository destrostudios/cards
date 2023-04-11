package com.destrostudios.cards.shared.entities;

import java.util.function.IntPredicate;

public interface Aggregator {

    IntList list();

    IntList list(IntPredicate predicate);

    int count();

    int count(IntPredicate predicate);

    int unique();
}
