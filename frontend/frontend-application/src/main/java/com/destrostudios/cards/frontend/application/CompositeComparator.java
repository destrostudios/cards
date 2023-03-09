package com.destrostudios.cards.frontend.application;

import java.util.Comparator;

public class CompositeComparator<T> implements Comparator<T> {

    public CompositeComparator(Comparator<T>... comparators) {
        this.comparators = comparators;
    }
    private Comparator<T>[] comparators;

    @Override
    public int compare(T o1, T o2) {
        for (Comparator<T> comparator : comparators) {
            int result = comparator.compare(o1, o2);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }
}