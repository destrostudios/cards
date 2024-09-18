package com.destrostudios.cards.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class TestUtil {

    public static void runOnManyProcessors(Runnable runnable) {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        int processors = Math.min(1, availableProcessors / 4);
        for (int i = 0; i < processors; i++) {
            new Thread(runnable).start();
        }
    }

    public static void runInterval(Runnable runnable, int interval) {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(interval);
                    runnable.run();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }).start();
    }

    public static Float getMedian_Int(Collection<Integer> values) {
        if (values.isEmpty()) {
            return null;
        }
        ArrayList<Integer> sortedValues = new ArrayList<>(values);
        Collections.sort(sortedValues);
        if ((sortedValues.size() % 2) == 1) {
            return (float) sortedValues.get(((sortedValues.size() + 1) / 2) - 1);
        } else {
            int lower = sortedValues.get((sortedValues.size() / 2) - 1);
            int upper = sortedValues.get((sortedValues.size() / 2));
            return ((lower + upper) / 2f);
        }
    }

    public static Float getMedian_Float(Collection<Float> values) {
        if (values.isEmpty()) {
            return null;
        }
        ArrayList<Float> sortedValues = new ArrayList<>(values);
        Collections.sort(sortedValues);
        if ((sortedValues.size() % 2) == 1) {
            return sortedValues.get(((sortedValues.size() + 1) / 2) - 1);
        } else {
            float lower = sortedValues.get((sortedValues.size() / 2) - 1);
            float upper = sortedValues.get((sortedValues.size() / 2));
            return ((lower + upper) / 2);
        }
    }

    public static Long getMedian_Long(Collection<Long> values) {
        if (values.isEmpty()) {
            return null;
        }
        ArrayList<Long> sortedValues = new ArrayList<>(values);
        Collections.sort(sortedValues);
        if ((sortedValues.size() % 2) == 1) {
            return sortedValues.get(((sortedValues.size() + 1) / 2) - 1);
        } else {
            long lower = sortedValues.get((sortedValues.size() / 2) - 1);
            long upper = sortedValues.get((sortedValues.size() / 2));
            return ((lower + upper) / 2);
        }
    }

    public static Float getAverage_Int(Collection<Integer> values) {
        return (values.isEmpty() ? null : ((((float) getSum_Int(values)) / values.size())));
    }

    public static Float getAverage_Float(Collection<Float> values) {
        return (values.isEmpty() ? null : (getSum_Float(values) / values.size()));
    }

    public static Long getAverage_Long(Collection<Long> values) {
        return (values.isEmpty() ? null : (getSum_Long(values) / values.size()));
    }

    public static int getSum_Int(Collection<Integer> values) {
        return values.stream().reduce(0, Integer::sum);
    }

    public static float getSum_Float(Collection<Float> values) {
        return values.stream().reduce(0f, Float::sum);
    }

    public static long getSum_Long(Collection<Long> values) {
        return values.stream().reduce(0L, Long::sum);
    }
}
