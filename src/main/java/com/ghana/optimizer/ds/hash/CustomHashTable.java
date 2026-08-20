package com.ghana.optimizer.ds.hash;

import com.ghana.optimizer.ds.list.DynamicArray;

/**
 * Custom Hash Table implementation for University of Ghana Campus Service Operations Optimizer (UG-CSOO).
 * Uses separate chaining with custom nodes for collision resolution.
 * Initial capacity defaults to System Parameter 2 (761).
 *
 *
 * @param <K> Key type
 * @param <V> Value type
 */
public class CustomHashTable<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 761;
    private static final double DEFAULT_LOAD_FACTOR_THRESHOLD = 0.75;

    private CustomHashNode<K, V>[] bucketArray;
    private int currentElementCount;
    private int currentBucketCapacity;
    private double loadFactorThreshold;

    /**
     * Inner class representing a node in the bucket's linked list chain.
     */
    public static class CustomHashNode<K, V> {
        private final K entryKey;
        private V entryValue;
        private CustomHashNode<K, V> nextNodePointer;

        public CustomHashNode(K entryKey, V entryValue, CustomHashNode<K, V> nextNodePointer) {
            this.entryKey = entryKey;
            this.entryValue = entryValue;
            this.nextNodePointer = nextNodePointer;
        }

        public K getEntryKey() {
            return entryKey;
        }

        public V getEntryValue() {
            return entryValue;
        }

        public void setEntryValue(V entryValue) {
            this.entryValue = entryValue;
        }

        public CustomHashNode<K, V> getNextNodePointer() {
            return nextNodePointer;
        }

        public void setNextNodePointer(CustomHashNode<K, V> nextNodePointer) {
            this.nextNodePointer = nextNodePointer;
        }
    }

    /**
     * Default constructor initializing with prime capacity of 547 and load factor 0.75.
     */
    @SuppressWarnings("unchecked")
    public CustomHashTable() {
        this.currentBucketCapacity = DEFAULT_INITIAL_CAPACITY;
        this.loadFactorThreshold = DEFAULT_LOAD_FACTOR_THRESHOLD;
        this.bucketArray = (CustomHashNode<K, V>[]) new CustomHashNode[DEFAULT_INITIAL_CAPACITY];
        this.currentElementCount = 0;
    }

    /**
     * Constructor allowing custom initial capacity.
     *
     * @param initialCapacity Starting bucket array capacity.
     */
    @SuppressWarnings("unchecked")
    public CustomHashTable(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be greater than zero");
        }
        this.currentBucketCapacity = initialCapacity;
        this.loadFactorThreshold = DEFAULT_LOAD_FACTOR_THRESHOLD;
        this.bucketArray = (CustomHashNode<K, V>[]) new CustomHashNode[initialCapacity];
        this.currentElementCount = 0;
    }

    /**
     * Constructor allowing custom initial capacity and load factor threshold.
     *
     * @param initialCapacity Starting bucket array capacity.
     * @param loadFactorThreshold Maximum load factor before resizing.
     */
    @SuppressWarnings("unchecked")
    public CustomHashTable(int initialCapacity, double loadFactorThreshold) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be greater than zero");
        }
        if (loadFactorThreshold <= 0.0 || Double.isNaN(loadFactorThreshold)) {
            throw new IllegalArgumentException("Load factor threshold must be greater than zero");
        }
        this.currentBucketCapacity = initialCapacity;
        this.loadFactorThreshold = loadFactorThreshold;
        this.bucketArray = (CustomHashNode<K, V>[]) new CustomHashNode[initialCapacity];
        this.currentElementCount = 0;
    }

    /**
     * Computes the bucket index for a given key.
     */
    private int calculateBucketIndex(K entryKey) {
        if (entryKey == null) {
            return 0;
        }
        int computedHashCode = entryKey.hashCode();
        int absoluteHashCode = Math.abs(computedHashCode);
        int targetBucketIndex = absoluteHashCode % currentBucketCapacity;
        return targetBucketIndex;
    }

    /**
     * Inserts or updates a key-value pair in the hash table.
     *
     * @param entryKey Key to insert/update.
     * @param entryValue Value to associate with key.
     */
    public void put(K entryKey, V entryValue) {
        if (entryKey == null) {
            throw new IllegalArgumentException("Entry key cannot be null");
        }

        double projectedLoadFactor = (double) (currentElementCount + 1) / currentBucketCapacity;
        if (projectedLoadFactor > loadFactorThreshold) {
            resizeAndRehashTable();
        }

        int targetBucketIndex = calculateBucketIndex(entryKey);
        CustomHashNode<K, V> currentBucketNode = bucketArray[targetBucketIndex];

        while (currentBucketNode != null) {
            if (currentBucketNode.getEntryKey().equals(entryKey)) {
                currentBucketNode.setEntryValue(entryValue);
                return;
            }
            currentBucketNode = currentBucketNode.getNextNodePointer();
        }

        CustomHashNode<K, V> newHeadNode = new CustomHashNode<>(entryKey, entryValue, bucketArray[targetBucketIndex]);
        bucketArray[targetBucketIndex] = newHeadNode;
        currentElementCount++;
    }

    /**
     * Retrieves the value associated with the given key.
     *
     * @param entryKey Key to search for.
     * @return Value if key exists, otherwise null.
     */
    public V get(K entryKey) {
        if (entryKey == null) {
            return null;
        }
        int targetBucketIndex = calculateBucketIndex(entryKey);
        CustomHashNode<K, V> currentBucketNode = bucketArray[targetBucketIndex];

        while (currentBucketNode != null) {
            if (currentBucketNode.getEntryKey().equals(entryKey)) {
                return currentBucketNode.getEntryValue();
            }
            currentBucketNode = currentBucketNode.getNextNodePointer();
        }
        return null;
    }

    /**
     * Removes the entry corresponding to the specified key.
     *
     * @param entryKey Key of entry to remove.
     * @return Removed value if present, otherwise null.
     */
    public V remove(K entryKey) {
        if (entryKey == null) {
            return null;
        }

        int targetBucketIndex = calculateBucketIndex(entryKey);
        CustomHashNode<K, V> currentBucketNode = bucketArray[targetBucketIndex];
        CustomHashNode<K, V> previousBucketNode = null;

        while (currentBucketNode != null) {
            if (currentBucketNode.getEntryKey().equals(entryKey)) {
                if (previousBucketNode == null) {
                    bucketArray[targetBucketIndex] = currentBucketNode.getNextNodePointer();
                } else {
                    previousBucketNode.setNextNodePointer(currentBucketNode.getNextNodePointer());
                }
                currentElementCount--;
                return currentBucketNode.getEntryValue();
            }
            previousBucketNode = currentBucketNode;
            currentBucketNode = currentBucketNode.getNextNodePointer();
        }
        return null;
    }

    /**
     * Checks if the key exists in the hash table.
     *
     * @param entryKey Key to verify.
     * @return true if key exists, false otherwise.
     */
    public boolean containsKey(K entryKey) {
        return get(entryKey) != null;
    }

    /**
     * Checks if the specified value exists in any bucket.
     *
     * @param targetValue Value to look for.
     * @return true if value exists, false otherwise.
     */
    public boolean containsValue(V targetValue) {
        for (int bucketIndex = 0; bucketIndex < currentBucketCapacity; bucketIndex++) {
            CustomHashNode<K, V> currentBucketNode = bucketArray[bucketIndex];
            while (currentBucketNode != null) {
                V currentValue = currentBucketNode.getEntryValue();
                if (currentValue == targetValue || (currentValue != null && currentValue.equals(targetValue))) {
                    return true;
                }
                currentBucketNode = currentBucketNode.getNextNodePointer();
            }
        }
        return false;
    }

    /**
     * Resizes the bucket array and rehashes all existing entries.
     */
    @SuppressWarnings("unchecked")
    private void resizeAndRehashTable() {
        int newBucketCapacity = currentBucketCapacity * 2 + 1;
        CustomHashNode<K, V>[] newBucketArray = (CustomHashNode<K, V>[]) new CustomHashNode[newBucketCapacity];
        CustomHashNode<K, V>[] oldBucketArray = bucketArray;
        int oldBucketCapacity = currentBucketCapacity;

        this.bucketArray = newBucketArray;
        this.currentBucketCapacity = newBucketCapacity;
        this.currentElementCount = 0;

        for (int oldBucketIndex = 0; oldBucketIndex < oldBucketCapacity; oldBucketIndex++) {
            CustomHashNode<K, V> currentBucketNode = oldBucketArray[oldBucketIndex];
            while (currentBucketNode != null) {
                put(currentBucketNode.getEntryKey(), currentBucketNode.getEntryValue());
                currentBucketNode = currentBucketNode.getNextNodePointer();
            }
        }
    }

    /**
     * Returns all keys in the hash table as a DynamicArray.
     */
    public DynamicArray<K> keys() {
        DynamicArray<K> keyList = new DynamicArray<>();
        for (int bucketIndex = 0; bucketIndex < currentBucketCapacity; bucketIndex++) {
            CustomHashNode<K, V> currentBucketNode = bucketArray[bucketIndex];
            while (currentBucketNode != null) {
                keyList.insert(currentBucketNode.getEntryKey());
                currentBucketNode = currentBucketNode.getNextNodePointer();
            }
        }
        return keyList;
    }

    /**
     * Returns all values in the hash table as a DynamicArray.
     */
    public DynamicArray<V> values() {
        DynamicArray<V> valueList = new DynamicArray<>();
        for (int bucketIndex = 0; bucketIndex < currentBucketCapacity; bucketIndex++) {
            CustomHashNode<K, V> currentBucketNode = bucketArray[bucketIndex];
            while (currentBucketNode != null) {
                valueList.insert(currentBucketNode.getEntryValue());
                currentBucketNode = currentBucketNode.getNextNodePointer();
            }
        }
        return valueList;
    }

    /**
     * Clears all entries from the hash table.
     */
    @SuppressWarnings("unchecked")
    public void clear() {
        this.bucketArray = (CustomHashNode<K, V>[]) new CustomHashNode[currentBucketCapacity];
        this.currentElementCount = 0;
    }

    public int size() {
        return currentElementCount;
    }

    public boolean isEmpty() {
        return currentElementCount == 0;
    }

    public int getCapacity() {
        return currentBucketCapacity;
    }

    public double getLoadFactor() {
        return (double) currentElementCount / currentBucketCapacity;
    }
}
