package com.ghana.optimizer.ds.tree;

/**
 * Custom disjoint-set (union-find) over integer elements 0..n-1, with union by rank
 * and path compression. Elements are expected to be pre-mapped from domain IDs
 * (e.g. locationId "L001") to a dense integer index by the caller (e.g. via CustomMap),
 * since union-find classically operates over array indices for O(1) array access.
 *
 * This is the structure Kruskal's MST algorithm uses to detect cycles
 * (see the graph algorithms module): union(a,b) fails silently (no-op) if a and b
 * are already connected, and find() reports connectivity.
 *
 * Complexity: with union-by-rank + path compression, each operation is O(alpha(n))
 * amortised (alpha = inverse Ackermann function, effectively constant in practice).
 */
public class DisjointSet {

    private final int[] parent;
    private final int[] rank;
    private int numSets;

    public DisjointSet(int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be > 0");
        parent = new int[n];
        rank = new int[n];
        numSets = n;
        for (int i = 0; i < n; i++) {
            parent[i] = i; // makeSet: each element starts as its own root
        }
    }

    private void checkIndex(int x) {
        if (x < 0 || x >= parent.length) {
            throw new IndexOutOfBoundsException("element " + x + " out of range [0," + parent.length + ")");
        }
    }

    /** Find with path compression: every node visited is re-pointed directly at the root. */
    public int find(int x) {
        checkIndex(x);
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // path compression
        }
        return parent[x];
    }

    /** Union by rank. Returns true if a merge happened, false if x and y were already connected. */
    public boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (rootX == rootY) return false;

        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
        numSets--;
        return true;
    }

    public boolean connected(int x, int y) {
        return find(x) == find(y);
    }

    /** Current number of disjoint sets (starts at n, decreases by 1 on each successful union). */
    public int countSets() {
        return numSets;
    }
}

