package com.destrostudios.cards.shared.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.BinaryOperator;
import java.util.function.IntPredicate;

/**
 *
 * @author Philipp
 */
public interface Aggregator<T> {

    int count(IntPredicate predicate);

    default int count() {
        return count(x -> true);
    }

    default boolean exists(IntPredicate predicate) {
        return count(predicate) != 0;
    }

    default boolean exists() {
        return exists(x -> true);
    }

    Optional<T> compute(BinaryOperator<T> operator, IntPredicate predicate);

    default Optional<T> compute(BinaryOperator<T> operator) {
        return compute(operator, x -> true);
    }

    OptionalInt unique(IntPredicate predicate);

    default OptionalInt unique() {
        return unique(x -> true);
    }

    List<Integer> list(IntPredicate predicate);

    default List<Integer> list() {
        return list(x -> true);
    }
}
