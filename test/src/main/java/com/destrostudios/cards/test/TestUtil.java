package com.destrostudios.cards.test;

import java.util.Collections;
import java.util.List;

public class TestUtil {

    public static long getMedian(List<Long> values) {
        Collections.sort(values);
        if ((values.size() % 2) == 1) {
            return values.get(((values.size() + 1) / 2) - 1);
        } else {
            long lower = values.get((values.size() / 2) - 1);
            long upper = values.get((values.size() / 2));
            return ((lower + upper) / 2);
        }
    }

    public static long getAverage(List<Long> values) {
        return (getSum(values) / values.size());
    }

    public static long getSum(List<Long> values) {
        long sum = 0;
        for (long value : values) {
            sum += value;
        }
        return sum;
    }
}
