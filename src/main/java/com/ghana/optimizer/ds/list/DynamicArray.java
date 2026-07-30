package com.ghana.optimizer.ds.list;

/**
 * Custom resizable array — a from-scratch replacement for java.util.ArrayList.
 * LinearSearch.java scans this structure directly (via get()/size()), so no
 * built-in collection is involved anywhere in the search path.
 */
public class DynamicArray<T> {

    private Object[] data;
    private int size;

    public DynamicArray() {
        data = new Object[4];
        size = 0;
    }

    public void insert(T item) {
        if (size == data.length) {
            resize();
        }
        data[size] = item;
        size++;
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        checkBounds(index);
        return (T) data[index];
    }

    public void set(int index, T item) {
        checkBounds(index);
        data[index] = item;
    }

    public void remove(int index) {
        checkBounds(index);
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[size - 1] = null;
        size--;
    }

    /** Doubles capacity when full. */
    private void resize() {
        int oldCapacity = data.length;
        int newCapacity = oldCapacity * 2;
        Object[] newData = new Object[newCapacity];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
    }

    private void checkBounds(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for size " + size);
        }
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return data.length;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
