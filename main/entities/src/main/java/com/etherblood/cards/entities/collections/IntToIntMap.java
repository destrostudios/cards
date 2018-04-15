package com.etherblood.cards.entities.collections;

import com.etherblood.cards.entities.IntIntConsumer;
import java.util.Arrays;
import java.util.function.LongConsumer;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 *
 * @author Philipp
 */
public class IntToIntMap {

    private static final long VALUE_MASK = 0xffffffff00000000L;
    private final static int FILL_NOMINATOR = 3, FILL_DENOMINATOR = 4;
    private final static int FREE_KEY = 0;

    private long[] data;
    private int mask;
    private int freeValue;
    private boolean hasFreeKey;
    private int count;

    public IntToIntMap() {
        this(8);
    }

    public IntToIntMap(int capacity) {
        this.mask = capacity - 1;
        assert mask != 0;
        assert (mask & capacity) == 0;
        data = new long[capacity];
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

    public void foreach(LongConsumer consumer) {
        if (hasFreeKey) {
            consumer.accept(dataKey(FREE_KEY) | dataValue(freeValue));
        }
        for (int i = 0; i < data.length; i++) {
            long keyValue = data[i];
            int key = key(keyValue);
            if (key != FREE_KEY) {
                consumer.accept(keyValue);
            }
        }
    }

    public IntStream stream() {
        IntStream stream = LongStream.of(data).mapToInt(IntToIntMap::key).filter(key -> key != FREE_KEY);
        if (hasFreeKey) {
            stream = IntStream.concat(IntStream.of(freeValue), stream);
        }
        return stream;
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

//    public Integer getOrNull(int key) {
//        Integer defaultValue = null;
//        if (key == FREE_KEY) {
//            return hasFreeKey ? freeValue : defaultValue;
//        }
//        int index = key & mask;
//        int indexKey;
//        while (true) {
//            long keyValue = data[index];
//            indexKey = key(keyValue);
//            if (indexKey == key) {
//                return value(keyValue);
//            }
//            if (indexKey == FREE_KEY) {
//                return defaultValue;
//            }
//            index = (index + 1) & mask;
//        }
//    }

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
        if ((count + 1) * FILL_DENOMINATOR >= capacity() * FILL_NOMINATOR) {
            resize(capacity() << 1);
        }
        if (_set(key, dataValue(value))) {
            count++;
        }
    }

    private boolean _set(int key, long shiftedValue) {
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
        for (int index = 0; index < oldData.length; index++) {
            long keyValue = oldData[index];
            int key = key(keyValue);
            if (key == FREE_KEY) {
                continue;
            }
            _set(key, keyValue & VALUE_MASK);
        }
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
}
