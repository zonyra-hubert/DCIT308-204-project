package com.ghana.optimizer.ds.tree;

import java.util.NoSuchElementException;
import com.ghana.optimizer.ds.list.DynamicArray;

public class BinarySearchTree<K extends Comparable<K>, V> {

    public static class Node<K, V> {
        K key;
        V value;
        Node<K, V> left, right;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node<K, V> root;
    private int size;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Node<K, V> getRoot() {
        return root;
    }

    public void insert(K key, V value) {
        if (key == null) throw new IllegalArgumentException("key must not be null");
        root = insert(root, key, value);
    }

    private Node<K, V> insert(Node<K, V> node, K key, V value) {
        if (node == null) {
            size++;
            return new Node<>(key, value);
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = insert(node.left, key, value);
        else if (cmp > 0) node.right = insert(node.right, key, value);
        else node.value = value; // overwrite
        return node;
    }

    public V search(K key) {
        if (key == null) throw new IllegalArgumentException("key must not be null");
        Node<K, V> node = getNode(key);
        return node == null ? null : node.value;
    }

    public boolean contains(K key) {
        if (key == null) throw new IllegalArgumentException("key must not be null");
        return getNode(key) != null;
    }

    private Node<K, V> getNode(K key) {
        Node<K, V> cur = root;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) return cur;
            cur = (cmp < 0) ? cur.left : cur.right;
        }
        return null;
    }

    public void remove(K key) {
        if (key == null) throw new IllegalArgumentException("key must not be null");
        if (getNode(key) == null) throw new NoSuchElementException("key not found: " + key);
        root = remove(root, key);
    }

    private Node<K, V> remove(Node<K, V> node, K key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = remove(node.left, key);
        } else if (cmp > 0) {
            node.right = remove(node.right, key);
        } else {
            if (node.left == null) {
                size--;
                return node.right;
            }
            if (node.right == null) {
                size--;
                return node.left;
            }
            Node<K, V> successor = minNode(node.right);
            node.key = successor.key;
            node.value = successor.value;
            node.right = remove(node.right, successor.key);
        }
        return node;
    }

    private Node<K, V> minNode(Node<K, V> node) {
        while (node.left != null) node = node.left;
        return node;
    }

    /** In-order traversal yields keys in ascending sorted order. */
    public DynamicArray<K> inorderKeys() {
        DynamicArray<K> result = new DynamicArray<>();
        inorder(root, result);
        return result;
    }

    private void inorder(Node<K, V> node, DynamicArray<K> result) {
        if (node == null) return;
        inorder(node.left, result);
        result.insert(node.key);
        inorder(node.right, result);
    }

    /** Height of the tree (number of edges on longest root-to-leaf path); -1 if empty. */
    public int height() {
        return height(root);
    }

    private int height(Node<K, V> node) {
        if (node == null) return -1;
        return 1 + Math.max(height(node.left), height(node.right));
    }
}
