package com.ghana.optimizer.ds.tree;

import com.ghana.optimizer.ds.list.DynamicArray;

/**
 * A generic B-tree implementation with configurable minimum degree.
 *
 * Supports search, insert, contains, size, and inorder traversal.
 * Keys are stored in sorted order within each node.
 *
 * @param <K> key type
 * @param <V> value type
 */
public class BTree<K extends Comparable<K>, V> {

    private final int minDegree;
    private BTreeNode<K, V> root;
    private int size;

    public static class BTreeNode<K, V> {
        K[] keys;
        V[] values;
        BTreeNode<K, V>[] children;
        int keyCount;
        boolean leaf;

        @SuppressWarnings("unchecked")
        BTreeNode(int minDegree, boolean leaf) {
            this.keys = (K[]) new Comparable[2 * minDegree - 1];
            this.values = (V[]) new Object[2 * minDegree - 1];
            this.children = new BTreeNode[2 * minDegree];
            this.keyCount = 0;
            this.leaf = leaf;
        }

        public int getKeyCount() {
            return keyCount;
        }

        public K[] getKeys() {
            return keys;
        }

        public V[] getValues() {
            return values;
        }

        public BTreeNode<K, V>[] getChildren() {
            return children;
        }

        public boolean isLeaf() {
            return leaf;
        }
    }

    public BTree(int minDegree) {
        if (minDegree < 2) {
            throw new IllegalArgumentException("Minimum degree must be at least 2");
        }
        this.minDegree = minDegree;
        this.root = new BTreeNode<>(minDegree, true);
        this.size = 0;
    }

    public int getMinDegree() {
        return minDegree;
    }

    public BTreeNode<K, V> getRoot() {
        return root;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public V search(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }
        return search(root, key);
    }

    public boolean contains(K key) {
        return search(key) != null;
    }

    private V search(BTreeNode<K, V> node, K key) {
        int index = 0;

        while (index < node.keyCount && key.compareTo(node.keys[index]) > 0) {
            index++;
        }

        if (index < node.keyCount && key.compareTo(node.keys[index]) == 0) {
            return node.values[index];
        }

        if (node.leaf) {
            return null;
        }

        return search(node.children[index], key);
    }

    public void insert(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("value must not be null");
        }

        BTreeNode<K, V> currentRoot = root;
        if (currentRoot.keyCount == 2 * minDegree - 1) {
            BTreeNode<K, V> newRoot = new BTreeNode<>(minDegree, false);
            newRoot.children[0] = currentRoot;
            splitChild(newRoot, 0);
            root = newRoot;
            insertNonFull(newRoot, key, value);
        } else {
            insertNonFull(currentRoot, key, value);
        }
    }

    private void insertNonFull(BTreeNode<K, V> node, K key, V value) {
        int index = node.keyCount - 1;

        if (node.leaf) {
            while (index >= 0 && key.compareTo(node.keys[index]) < 0) {
                node.keys[index + 1] = node.keys[index];
                node.values[index + 1] = node.values[index];
                index--;
            }

            if (index >= 0 && key.compareTo(node.keys[index]) == 0) {
                node.values[index] = value;
                return;
            }

            node.keys[index + 1] = key;
            node.values[index + 1] = value;
            node.keyCount++;
            size++;
            return;
        }

        while (index >= 0 && key.compareTo(node.keys[index]) < 0) {
            index--;
        }

        if (index >= 0 && key.compareTo(node.keys[index]) == 0) {
            node.values[index] = value;
            return;
        }

        int childIndex = index + 1;
        BTreeNode<K, V> child = node.children[childIndex];

        if (child.keyCount == 2 * minDegree - 1) {
            splitChild(node, childIndex);
            if (key.compareTo(node.keys[childIndex]) > 0) {
                childIndex++;
            } else if (key.compareTo(node.keys[childIndex]) == 0) {
                node.values[childIndex] = value;
                return;
            }
        }

        insertNonFull(node.children[childIndex], key, value);
    }

    private void splitChild(BTreeNode<K, V> parent, int index) {
        BTreeNode<K, V> fullChild = parent.children[index];
        BTreeNode<K, V> newChild = new BTreeNode<>(minDegree, fullChild.leaf);
        newChild.keyCount = minDegree - 1;

        for (int i = 0; i < minDegree - 1; i++) {
            newChild.keys[i] = fullChild.keys[i + minDegree];
            newChild.values[i] = fullChild.values[i + minDegree];
        }

        if (!fullChild.leaf) {
            for (int i = 0; i < minDegree; i++) {
                newChild.children[i] = fullChild.children[i + minDegree];
            }
        }

        fullChild.keyCount = minDegree - 1;

        for (int i = parent.keyCount; i >= index + 1; i--) {
            parent.children[i + 1] = parent.children[i];
        }
        parent.children[index + 1] = newChild;

        for (int i = parent.keyCount - 1; i >= index; i--) {
            parent.keys[i + 1] = parent.keys[i];
            parent.values[i + 1] = parent.values[i];
        }

        parent.keys[index] = fullChild.keys[minDegree - 1];
        parent.values[index] = fullChild.values[minDegree - 1];
        parent.keyCount++;

        fullChild.keys[minDegree - 1] = null;
        fullChild.values[minDegree - 1] = null;
    }

    public DynamicArray<K> inorderKeys() {
        DynamicArray<K> result = new DynamicArray<>();
        if (!isEmpty()) {
            inorder(root, result);
        }
        return result;
    }

    private void inorder(BTreeNode<K, V> node, DynamicArray<K> result) {
        int i;
        for (i = 0; i < node.keyCount; i++) {
            if (!node.leaf) {
                inorder(node.children[i], result);
            }
            result.insert(node.keys[i]);
        }
        if (!node.leaf) {
            inorder(node.children[i], result);
        }
    }

    public int height() {
        if (isEmpty()) {
            return -1;
        }
        return height(root);
    }

    private int height(BTreeNode<K, V> node) {
        if (node.leaf) {
            return 0;
        }
        return 1 + height(node.children[0]);
    }

    public void clear() {
        root = new BTreeNode<>(minDegree, true);
        size = 0;
    }
}
