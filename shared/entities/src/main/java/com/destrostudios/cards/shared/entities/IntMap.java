package com.destrostudios.cards.shared.entities;

import java.util.Arrays;
import java.util.Optional;
import java.util.PrimitiveIterator;
import java.util.function.IntConsumer;

public class IntMap<T> implements Iterable<Integer> {

    private static final int FREE_KEY = 0;

    private float fillFactor;
    private int[] keys;
    private Object[] values;
    private int mask;
    private T freeValue;
    private int count;
    private int fillLimit;
    private boolean hasFreeKey;

    public IntMap() {
        this(8);
    }

    public IntMap(int capacity) {
        this(capacity, 0.75f);
    }

    public IntMap(int capacity, float fillFactor) {
        this.fillFactor = fillFactor;
        this.mask = capacity - 1;
        keys = new int[capacity];
        values = new Object[capacity];
        updateFillLimit(capacity);
    }

    public IntMap(IntMap<T> other) {
        fillFactor = other.fillFactor;
        mask = other.mask;
        keys = new int[other.keys.length];
        values = new Object[other.keys.length];
        fillLimit = other.fillLimit;
        System.arraycopy(other.keys, 0, keys, 0, keys.length);
        System.arraycopy(other.values, 0, values, 0, values.length);
        hasFreeKey = other.hasFreeKey;
        freeValue = other.freeValue;
        count = other.count;
    }

    public void foreachKey(IntConsumer consumer) {
        if (hasFreeKey) {
            consumer.accept(FREE_KEY);
        }
        for (int key : keys) {
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
        while (true) {
            int keyCandidate = keys[index];
            if (keyCandidate == FREE_KEY) {
                return false;
            }
            if (keyCandidate == key) {
                return true;
            }
            index = (index + 1) & mask;
        }
    }

    public T get(int key) {
        if (key == FREE_KEY) {
            if (hasFreeKey) {
                return freeValue;
            }
            return null;
        }
        int index = key & mask;
        while (true) {
            int keyCandidate = keys[index];
            if (keyCandidate == key) {
                return (T) values[index];
            }
            if (keyCandidate == FREE_KEY) {
                return null;
            }
            index = (index + 1) & mask;
        }
    }

    public T getOrElse(int key, T defaultValue) {
        if (key == FREE_KEY) {
            return hasFreeKey ? freeValue : defaultValue;
        }
        int index = key & mask;
        while (true) {
            int keyCandidate = keys[index];
            if (keyCandidate == FREE_KEY) {
                return defaultValue;
            }
            if (keyCandidate == key) {
                return (T) values[index];
            }
            index = (index + 1) & mask;
        }
    }

    public void set(int key, T value) {
        if (key == FREE_KEY) {
            if (!hasFreeKey) {
                hasFreeKey = true;
                count++;
            }
            freeValue = value;
            return;
        }
        if (count >= fillLimit) {
            resize(2 * capacity());
        }
        if (uncheckedSet(key, value)) {
            count++;
        }
    }

    private boolean uncheckedSet(int key, Object value) {
        int index = key & mask;
        while (true) {
            int keyCandidate = keys[index];
            if (keyCandidate == FREE_KEY) {
                keys[index] = key;
                values[index] = value;
                return true;
            }
            if (keyCandidate == key) {
                values[index] = value;
                return false;
            }
            index = (index + 1) & mask;
        }
    }

    private void resize(int capacity) {
        mask = capacity - 1;
        int[] oldKeys = keys;
        Object[] oldValues = values;
        keys = new int[capacity];
        values = new Object[capacity];
        updateFillLimit(capacity);
        for (int i = 0; i < oldKeys.length; i++) {
            int key = oldKeys[i];
            if (key == FREE_KEY) {
                continue;
            }
            uncheckedSet(key, oldValues[i]);
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
        while (true) {
            int keyCandidate = keys[index];
            if (keyCandidate == FREE_KEY) {
                return;
            }
            if (keyCandidate == key) {
                shift(index);
                count--;
                return;
            }
            index = (index + 1) & mask;
        }
    }

    private void shift(int pos) {
        int last = pos;
        while (true) {
            pos = (pos + 1) & mask;
            int keyCandidate = keys[pos];
            if (keyCandidate == FREE_KEY) {
                keys[last] = FREE_KEY;
                return;
            }
            int slot = keyCandidate & mask;
            if (last <= pos ? last >= slot || slot > pos : last >= slot && slot > pos) {
                keys[last] = keyCandidate;
                values[last] = values[pos];
                last = pos;
            }
        }
    }

    public int size() {
        return count;
    }

    private int capacity() {
        return keys.length;
    }

    public void clear() {
        count = 0;
        hasFreeKey = false;
        Arrays.fill(keys, FREE_KEY);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('{');
        boolean isFirst = true;
        for (int key : this) {
            T value = get(key);
            if (isFirst) {
                isFirst = false;
            } else {
                builder.append(", ");
            }
            builder.append(key);
            builder.append("->");
            builder.append(value);
        }
        builder.append('}');
        return builder.toString();
    }

    public Optional<T> getOptional(int key) {
        return hasKey(key) ? Optional.of(get(key)) : Optional.empty();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public PrimitiveIterator.OfInt iterator() {
        return new PrimitiveIterator.OfInt() {
            private int remaining = count;
            private int currentIndex = -1;
            private boolean freeKey = hasFreeKey;

            @Override
            public int nextInt() {
                remaining--;
                if (freeKey) {
                    freeKey = false;
                    return FREE_KEY;
                }
                int result;
                do {
                    currentIndex++;
                } while ((result = keys[currentIndex]) == FREE_KEY);
                return result;
            }

            @Override
            public boolean hasNext() {
                return remaining != 0;
            }
        };
    }
}
