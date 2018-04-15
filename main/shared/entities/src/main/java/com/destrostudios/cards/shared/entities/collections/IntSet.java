package com.destrostudios.cards.shared.entities.collections;

import java.util.Arrays;
import java.util.function.IntConsumer;

/**
 *
 * @author Philipp
 */
public final class IntSet {

    private final static int FILL_NOMINATOR = 3, FILL_DENOMINATOR = 4;
    private final static int FREE_KEY = 0;

    private int[] keys;
    private int mask;
    private boolean hasFreeKey;
    private int count = 0;

    public IntSet() {
        this(8);
    }

    public IntSet(int capacity) {
        this.mask = capacity - 1;
        assert mask != 0;
        assert (mask & capacity) == 0;
        keys = new int[capacity];
    }

    public void foreach(IntConsumer consumer) {
        if (hasFreeKey) {
            consumer.accept(FREE_KEY);
        }
        for (int i = 0; i < keys.length; i++) {
            int key = keys[i];
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
            indexKey = keys[index];
            if (indexKey == key) {
                return true;
            }
            if (indexKey == FREE_KEY) {
                return false;
            }
            index = (index + 1) & mask;
        }
    }

    public void set(int key) {
        assert count < keys.length;
        if (key == FREE_KEY) {
            if (!hasFreeKey) {
                hasFreeKey = true;
                count++;
            }
            return;
        }
        if ((count + 1) * FILL_DENOMINATOR >= capacity() * FILL_NOMINATOR) {
            resize(capacity() << 1);
        }
        if (_set(key)) {
            count++;
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
            indexKey = keys[index];
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
                if ((key = keys[pos]) == FREE_KEY) {
                    keys[last] = FREE_KEY;
                    return;
                }
                slot = key & mask; //calculate the starting slot for the current key
                if (last <= pos ? last >= slot || slot > pos : last >= slot && slot > pos) {
                    break;
                }
                pos = (pos + 1) & mask; //go to the next entry
            }
            keys[last] = key;
        }
    }

    private boolean _set(int key) {
        assert key != FREE_KEY;
        int index = key & mask;
        int indexKey;
        while (true) {
            indexKey = keys[index];
            if (indexKey == key) {
                return false;
            }
            if (indexKey == FREE_KEY) {
                keys[index] = key;
                return true;
            }
            index = (index + 1) & mask;
        }
    }

    private void resize(int capacity) {
        assert count < capacity;
        mask = capacity - 1;
        assert (mask & capacity) == 0;
        int[] oldKeys = keys;
        keys = new int[capacity];
        Arrays.fill(keys, FREE_KEY);
        for (int index = 0; index < oldKeys.length; index++) {
            int key = oldKeys[index];
            if (key == FREE_KEY) {
                continue;
            }
            _set(key);
        }
    }

    public int size() {
        return count;
    }

    public int capacity() {
        return keys.length;
    }
}
