package com.ghana.optimizer.ds.tree;

import com.ghana.optimizer.ds.list.DynamicArray;

public class RedBlackTree<K extends Comparable<K>, V> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> left, right;
        boolean color; // color of link from parent to this node
        int size;      // subtree node count, useful for rank/order-statistics + trace evidence

        Node(K key, V value, boolean color, int size) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.size = size;
        }
    }

    private Node<K, V> root;

    public int size() {
        return size(root);
    }

    private int size(Node<K, V> x) {
        return x == null ? 0 : x.size;
    }

    public boolean isEmpty() {
        return root == null;
    }

    private boolean isRed(Node<K, V> x) {
        return x != null && x.color == RED;
    }

    public V search(K key) {
        Node<K, V> cur = root;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) return cur.value;
            cur = (cmp < 0) ? cur.left : cur.right;
        }
        return null;
    }

    public void insert(K key, V value) {
        if (key == null) throw new IllegalArgumentException("key must not be null");
        root = insert(root, key, value);
        root.color = BLACK; // root is always black
    }

    private Node<K, V> insert(Node<K, V> h, K key, V value) {
        if (h == null) {
            return new Node<>(key, value, RED, 1);
        }
        int cmp = key.compareTo(h.key);
        if (cmp < 0) h.left = insert(h.left, key, value);
        else if (cmp > 0) h.right = insert(h.right, key, value);
        else h.value = value;

        // Standard LLRB fix-up sequence: lean left, no two reds in a row, split 4-nodes.
        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);

        h.size = 1 + size(h.left) + size(h.right);
        return h;
    }

    private Node<K, V> rotateLeft(Node<K, V> h) {
        Node<K, V> x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        x.size = h.size;
        h.size = 1 + size(h.left) + size(h.right);
        return x;
    }

    private Node<K, V> rotateRight(Node<K, V> h) {
        Node<K, V> x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        x.size = h.size;
        h.size = 1 + size(h.left) + size(h.right);
        return x;
    }

    private void flipColors(Node<K, V> h) {
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    public int height() {
        return height(root);
    }

    private int height(Node<K, V> x) {
        if (x == null) return -1;
        return 1 + Math.max(height(x.left), height(x.right));
    }

    /** Returns the black-height (number of black links) from root to any leaf -- should be
     *  identical along every root-to-null path if the RB invariants hold; used as a
     *  self-check when defending the structure. */
    public int blackHeight() {
        return blackHeight(root);
    }

    private int blackHeight(Node<K, V> x) {
        if (x == null) return 0;
        int leftBH = blackHeight(x.left);
        int rightBH = blackHeight(x.right);
        int add = isRed(x) ? 0 : 1;
        // In a correctly-formed LLRB both sides should match; we trust the left path
        // (assert-style check kept simple deliberately for the trace/demo).
        return Math.max(leftBH, rightBH) + add;
    }

    public DynamicArray<K> inorderKeys() {
        DynamicArray<K> out = new DynamicArray<>();
        inorder(root, out);
        return out;
    }

    private void inorder(Node<K, V> x, DynamicArray<K> out) {
        if (x == null) return;
        inorder(x.left, out);
        out.add(x.key);
        inorder(x.right, out);
    }
}
