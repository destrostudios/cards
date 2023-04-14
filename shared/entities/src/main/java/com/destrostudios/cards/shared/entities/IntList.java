package com.destrostudios.cards.shared.entities;

import java.util.Arrays;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author Philipp
 */
public class IntList implements Iterable<Integer> {

    private static final int DEFAULT_CAPACITY = 8;

    private int size = 0;
    private int[] data;

    public IntList(int... data) {
        this.data = Arrays.copyOf(data, Math.max(data.length, DEFAULT_CAPACITY));
        size = data.length;
    }

    public IntList() {
        this(DEFAULT_CAPACITY);
    }

    public IntList(int capacity) {
        data = new int[capacity];
    }

    public int get(int index) {
        return data[index];
    }

    public void set(int index, int value) {
        data[index] = value;
    }

    public void add(int value) {
        if (size == data.length) {
            grow();
        }
        data[size++] = value;
    }

    public int removeLast() {
        return data[--size];
    }

    public void insertAt(int index, int value) {
        if (size == data.length) {
            grow();
        }
        System.arraycopy(data, index, data, index + 1, size++ - index);
        data[index] = value;
    }

    public void swapInsertAt(int index, int value) {
        if (size == data.length) {
            grow();
        }
        data[size++] = data[index];
        data[index] = value;
    }

    public int removeAt(int index) {
        int previous = data[index];
        System.arraycopy(data, index + 1, data, index, --size - index);
        return previous;
    }

    public void swapRemove(int value) {
        int index = indexOf(value);
        if (index >= 0) {
            data[index] = data[--size];
        }
    }

    public int swapRemoveAt(int index) {
        int previous = data[index];
        data[index] = data[--size];
        return previous;
    }

    public int indexOf(int value) {
        for (int i = 0; i < size; i++) {
            if (data[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public void clear() {
        size = 0;
    }

    public void sort() {
        Arrays.sort(data, 0, size);
    }

    public void shuffle(IntUnaryOperator random) {
        for (int i = 0; i < size; i++) {
            swap(i, i + random.applyAsInt(size - i));
        }
    }

    public int getRandomItem(IntUnaryOperator random) {
        return get(random.applyAsInt(size));
    }

    public IntStream stream() {
        return Arrays.stream(data, 0, size);
    }

    private void grow() {
        grow(Math.max(data.length * 2, DEFAULT_CAPACITY));
    }

    private void grow(int targetCapacity) {
        if (targetCapacity <= data.length) {
            return;
        }
        data = Arrays.copyOf(data, targetCapacity);
    }

    public int size() {
        return size;
    }

    public int[] data() {
        return data;
    }

    public int[] toArray() {
        return Arrays.copyOf(data, size);
    }

    public List<Integer> boxed() {
        return Arrays.stream(data, 0, size).boxed().collect(Collectors.toList());
    }

    public void foreach(IntConsumer consumer) {
        for (int i = 0; i < size; i++) {
            consumer.accept(data[i]);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + Arrays.toString(Arrays.copyOfRange(data, 0, size));
    }

    public void swap(int index1, int index2) {
        int tmp = get(index1);
        set(index1, get(index2));
        set(index2, tmp);
    }

    public boolean contains(int value) {
        return indexOf(value) != -1;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean nonEmpty() {
        return size() != 0;
    }

    @Override
    public PrimitiveIterator.OfInt iterator() {
        return new PrimitiveIterator.OfInt() {
            private int i = 0;

            @Override
            public int nextInt() {
                return get(i++);
            }

            @Override
            public boolean hasNext() {
                return i < size();
            }

            @Override
            public void remove() {
                removeAt(--i);
            }
        };
    }

    private boolean inRange(int index) {
        return 0 <= index && index < size;
    }

    public void addAll(IntList intList) {
        int oldSize = size;
        grow(size + intList.size);
        size += intList.size;
        System.arraycopy(intList.data, 0, data, oldSize, intList.size);
    }

    public void retain(IntPredicate predicate) {
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (predicate.test(data[i])) {
                data[newSize++] = data[i];
            }
        }
        size = newSize;
    }

    public boolean anyMatch(IntPredicate predicate) {
        for (int i = 0; i < size; i++) {
            if (predicate.test(data[i])) {
                return true;
            }
        }
        return false;
    }

    public static IntList singletonList(int value) {
        IntList intList = new IntList(1);
        intList.add(value);
        return intList;
    }
}
