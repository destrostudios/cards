package com.destrostudios.cards.shared.entities.collections;

import com.destrostudios.cards.shared.entities.util.IntIntConsumer;
import java.util.Arrays;
import java.util.PrimitiveIterator;
import java.util.function.IntConsumer;

/**
 *
 * @author Philipp
 */
public class IntToIntMap implements Iterable<Integer> {

    private static final long VALUE_MASK = 0xffffffff00000000L;
    private static final int FREE_KEY = 0;

    private long[] data;
    private int mask;
    private int freeValue;
    private int count;
    private int fillLimit;
    private final float fillFactor;
    private boolean hasFreeKey;

    public IntToIntMap() {
        this(8, 0.75f);
    }

    public IntToIntMap(int capacity, float fillFactor) {
        this.fillFactor = fillFactor;
        this.mask = capacity - 1;
        assert mask != 0;
        assert (mask & capacity) == 0;
        data = new long[capacity];
        updateFillLimit(capacity);
    }

    public void foreach(IntIntConsumer consumer) {
        if (hasFreeKey) {
            consumer.accept(FREE_KEY, freeValue);
        }
        for (int i = 0; i < data.length; i++) {
            long keyValue = data[i];
            int key = key(keyValue);
            if (key != FREE_KEY) {
                consumer.accept(key, value(keyValue));
            }
        }
    }

    public void foreachKey(IntConsumer consumer) {
        if (hasFreeKey) {
            consumer.accept(FREE_KEY);
        }
        for (int i = 0; i < data.length; i++) {
            long keyValue = data[i];
            int key = key(keyValue);
            if (key != FREE_KEY) {
                consumer.accept(key);
            }
        }
    }

    public boolean hasKey(int key) {
        if (key == FREE_KEY) {
            return hasFreeKey;
        }
        int index = key & mask;
        int indexKey;
        while (true) {
            indexKey = key(data[index]);
            if (indexKey == key) {
                return true;
            }
            if (indexKey == FREE_KEY) {
                return false;
            }
            index = (index + 1) & mask;
        }
    }

    public int get(int key) {
        if (key == FREE_KEY) {
            if (hasFreeKey) {
                return freeValue;
            }
            throw new NullPointerException();
        }
        int index = key & mask;
        int indexKey;
        while (true) {
            long keyValue = data[index];
            indexKey = key(keyValue);
            if (indexKey == key) {
                return value(keyValue);
            }
            if (indexKey == FREE_KEY) {
                throw new NullPointerException();
            }
            index = (index + 1) & mask;
        }
    }

    public int getOrElse(int key, int defaultValue) {
        if (key == FREE_KEY) {
            return hasFreeKey ? freeValue : defaultValue;
        }
        int index = key & mask;
        int indexKey;
        while (true) {
            long keyValue = data[index];
            indexKey = key(keyValue);
            if (indexKey == key) {
                return value(keyValue);
            }
            if (indexKey == FREE_KEY) {
                return defaultValue;
            }
            index = (index + 1) & mask;
        }
    }

    public void set(int key, int value) {
        assert count < data.length;
        if (key == FREE_KEY) {
            if (!hasFreeKey) {
                freeValue = value;
                hasFreeKey = true;
                count++;
            }
            return;
        }
        if (count >= fillLimit) {
            resize(capacity() << 1);
        }
        if (uncheckedSet(key, dataValue(value))) {
            count++;
        }
    }

    private boolean uncheckedSet(int key, long shiftedValue) {
        assert key != FREE_KEY;
        int index = key & mask;
        int indexKey;
        while (true) {
            long keyValue = data[index];
            indexKey = key(keyValue);
            if (indexKey == key) {
                data[index] = dataKey(key) | shiftedValue;
                return false;
            }
            if (indexKey == FREE_KEY) {
                data[index] = dataKey(key) | shiftedValue;
                return true;
            }
            index = (index + 1) & mask;
        }
    }

    private void resize(int capacity) {
        assert count < capacity;
        mask = capacity - 1;
        assert (mask & capacity) == 0;
        long[] oldData = data;
        data = new long[capacity];
        Arrays.fill(data, FREE_KEY);
        updateFillLimit(capacity);
        for (int index = 0; index < oldData.length; index++) {
            long keyValue = oldData[index];
            int key = key(keyValue);
            if (key == FREE_KEY) {
                continue;
            }
            uncheckedSet(key, keyValue & VALUE_MASK);
        }
    }

    private void updateFillLimit(int capacity) {
        fillLimit = (int) (fillFactor * capacity) - 1;
    }

    public void remove(int key) {
        if (key == FREE_KEY) {
            if (hasFreeKey) {
                hasFreeKey = false;
                count--;
            }
            return;
        }
        int index = key & mask;
        int indexKey;
        while (true) {
            long keyValue = data[index];
            indexKey = key(keyValue);
            if (indexKey == key) {
                shift(index);
                count--;
                return;
            }
            if (indexKey == FREE_KEY) {
                return;
            }
            index = (index + 1) & mask;
        }
    }

    private void shift(int pos) {
        // Shift entries with the same hash.
        int last, slot;
        int key;
        while (true) {
            pos = ((last = pos) + 1) & mask;
            while (true) {
                long keyValue = data[pos];
                key = key(keyValue);
                if (key == FREE_KEY) {
                    data[last] = FREE_KEY;
                    return;
                }
                slot = key & mask; //calculate the starting slot for the current key
                if (last <= pos ? last >= slot || slot > pos : last >= slot && slot > pos) {
                    break;
                }
                pos = (pos + 1) & mask; //go to the next entry
            }
            data[last] = dataKey(key) | (data[pos] & VALUE_MASK);
        }
    }

    private static int key(long keyValue) {
        return (int) keyValue;
    }

    private static int value(long keyValue) {
        return (int) (keyValue >>> 32);
    }

    private static long dataValue(int value) {
        return ((long) value) << 32;
    }

    private static long dataKey(int key) {
        return Integer.toUnsignedLong(key);
    }

    public int size() {
        return count;
    }

    public int capacity() {
        return data.length;
    }

    @Override
    public PrimitiveIterator.OfInt iterator() {
        return new PrimitiveIterator.OfInt() {
            int i;
            int next;

            {
                i = -1;
                next = FREE_KEY;
                if (!hasFreeKey) {
                    gotoNext();
                }
            }

            private void gotoNext() {
                do {
                    i++;
                } while (hasNext() && key(data[i]) == FREE_KEY);
                if (hasNext()) {
                    next = key(data[i]);
                }
            }

            @Override
            public int nextInt() {
                try {
                    return next;
                } finally {
                    gotoNext();
                }
            }

            @Override
            public boolean hasNext() {
                return i < data.length;
            }
        };
    }
}
