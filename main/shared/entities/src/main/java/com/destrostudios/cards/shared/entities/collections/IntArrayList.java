package com.destrostudios.cards.shared.entities.collections;

import java.util.Arrays;
import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.stream.IntStream;

/**
 *
 * @author Philipp
 */
public class IntArrayList implements Iterable<Integer> {

    private int size = 0;
    private int[] data;

    public IntArrayList() {
        this(8);
    }

    public IntArrayList(int capacity) {
        data = new int[capacity];
    }

    public int get(int index) {
        return data[index];
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

    public void swap(int index1, int index2) {
        int tmp = data[index1];
        data[index1] = data[index2];
        data[index2] = tmp;
    }

    public void removeAt(int index) {
        System.arraycopy(data, index + 1, data, index, --size - index);
    }

    public void swapRemoveAt(int index) {
        data[index] = data[--size];
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

    public void shuffle(Random random) {
        for (int i = 0; i < size; i++) {
            swap(i, i + random.nextInt(size - i));
        }
    }

    public IntStream stream() {
        return Arrays.stream(data, 0, size);
    }

    private void grow() {
        int[] nextData = new int[data.length * 2];
        System.arraycopy(data, 0, nextData, 0, data.length);
        data = nextData;
    }

    public int size() {
        return size;
    }

    public int[] data() {
        return data;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + Arrays.toString(Arrays.copyOfRange(data, 0, size));
    }

    @Override
    public PrimitiveIterator.OfInt iterator() {
        return new PrimitiveIterator.OfInt() {
            private int i = 0;

            @Override
            public int nextInt() {
                return data[i++];
            }

            @Override
            public boolean hasNext() {
                return i < size;
            }
        };
    }

}
