package com.destrostudios.cards.shared.entities.collections;

/**
 *
 * @author Philipp
 */
public class LongArrayList {
    private int size = 0;
    private long[] data = new long[8];
    
    public long get(int index) {
        return data[index];
    }
    
    public void add(long value) {
        if(size == data.length) {
            grow();
        }
        data[size++] = value;
    }
    
    public long removeLast() {
        return data[--size];
    }
    
    public void insertAt(int index, long value) {
        if(size == data.length) {
            grow();
        }
        System.arraycopy(data, index, data, index + 1, size++ - index);
        data[index] = value;
    }
    
    public void swapInsertAt(int index, long value) {
        if(size == data.length) {
            grow();
        }
        data[size++] = data[index];
        data[index] = value;
    }
    
    public void swap(int index1, int index2) {
        long tmp = data[index1];
        data[index1] = data[index2];
        data[index2] = tmp;
    }
    
    public void removeAt(int index) {
        System.arraycopy(data, index + 1, data, index, --size - index);
    }
    
    public void swapRemoveAt(int index) {
        data[index] = data[--size];
    }

    public void clear() {
        size = 0;
    }
    
    private void grow() {
        long[] nextData = new long[data.length * 2];
        System.arraycopy(data, 0, nextData, 0, data.length);
        data = nextData;
    }

    public int size() {
        return size;
    }

    public long[] data() {
        return data;
    }
    
}
