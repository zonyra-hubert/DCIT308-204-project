package com.ghana.optimizer.ds;

import com.ghana.optimizer.ds.disjoint.DisjointSet;
import com.ghana.optimizer.ds.list.DynamicArray;
import com.ghana.optimizer.ds.list.MyLinkedList;
import com.ghana.optimizer.ds.queue.CircularQueue;
import com.ghana.optimizer.ds.queue.MyQueue;
import com.ghana.optimizer.ds.stack.MyStack;
import com.ghana.optimizer.ds.tree.BinarySearchTree;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DataStructuresTest {

    @Test
    public void testDynamicArrayDynamicGrowth() {
        DynamicArray<Integer> array = new DynamicArray<>();
        assertEquals(0, array.size());
        assertEquals(4, array.capacity());

        for (int i = 0; i < 10; i++) {
            array.add(i * 10);
        }

        assertEquals(10, array.size());
        assertTrue(array.capacity() >= 16);
        assertEquals(0, array.get(0));
        assertEquals(90, array.get(9));

        array.remove(0);
        assertEquals(9, array.size());
        assertEquals(10, array.get(0));
    }

    @Test
    public void testMyLinkedListOperations() {
        MyLinkedList<String> list = new MyLinkedList<>();
        assertTrue(list.isEmpty());

        list.addFirst("Middle");
        list.addFirst("Head");
        list.addLast("Tail");

        assertFalse(list.isEmpty());
        assertEquals("Head", list.getFirst());
        assertEquals("Tail", list.getLast());

        assertEquals("Head", list.removeFirst());
        assertEquals("Tail", list.removeLast());
        assertEquals("Middle", list.getFirst());
    }

    @Test
    public void testMyStackLIFO() {
        MyStack<Integer> stack = new MyStack<>();
        assertTrue(stack.isEmpty());

        stack.push(10);
        stack.push(20);
        stack.push(30);

        assertEquals(30, stack.peek());
        assertEquals(30, stack.pop());
        assertEquals(20, stack.pop());
        assertEquals(10, stack.pop());
        assertTrue(stack.isEmpty());
        assertNull(stack.pop());
    }

    @Test
    public void testMyQueueFIFO() {
        MyQueue<String> queue = new MyQueue<>();
        assertTrue(queue.isEmpty());

        queue.enqueue("First");
        queue.enqueue("Second");
        queue.enqueue("Third");

        assertEquals("First", queue.peek());
        assertEquals("First", queue.dequeue());
        assertEquals("Second", queue.dequeue());
        assertEquals("Third", queue.dequeue());
        assertTrue(queue.isEmpty());
        assertNull(queue.dequeue());
    }

    @Test
    public void testCircularQueueCyclicWrap() {
        CircularQueue<Integer> queue = new CircularQueue<>(3);
        assertTrue(queue.isEmpty());
        assertFalse(queue.isFull());

        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        assertTrue(queue.isFull());

        assertEquals(1, queue.dequeue());
        assertFalse(queue.isFull());

        queue.enqueue(4); // Cyclic wrap around
        assertEquals(2, queue.dequeue());
        assertEquals(3, queue.dequeue());
        assertEquals(4, queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testBinarySearchTreeInorderAndHeight() {
        BinarySearchTree<Integer, String> bst = new BinarySearchTree<>();
        assertTrue(bst.isEmpty());

        bst.insert(50, "Root");
        bst.insert(30, "Left");
        bst.insert(70, "Right");
        bst.insert(20, "Left-Left");
        bst.insert(40, "Left-Right");

        assertEquals(5, bst.size());
        assertEquals(2, bst.height());
        assertEquals("Left", bst.search(30));
        assertNull(bst.search(999));

        DynamicArray<Integer> keys = bst.inorderKeys();
        assertEquals(5, keys.size());
        assertEquals(20, keys.get(0));
        assertEquals(30, keys.get(1));
        assertEquals(40, keys.get(2));
        assertEquals(50, keys.get(3));
        assertEquals(70, keys.get(4));
    }

    @Test
    public void testDisjointSetUnionFind() {
        DisjointSet ds = new DisjointSet(6);
        assertEquals(6, ds.countSets());

        assertTrue(ds.union(0, 1));
        assertTrue(ds.union(1, 2));
        assertFalse(ds.union(0, 2)); // Already connected

        assertTrue(ds.connected(0, 2));
        assertFalse(ds.connected(0, 3));

        assertTrue(ds.union(3, 4));
        assertEquals(3, ds.countSets());

        assertTrue(ds.union(2, 4));
        assertTrue(ds.connected(0, 3));
        assertEquals(2, ds.countSets());
    }
}
