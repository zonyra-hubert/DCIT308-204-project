package com.ghana.optimizer.ds.queue;

public class CircularQueue<T> {
    private T[] data;
    private int front;
    private int rear;
    private int size;

    public CircularQueue(int capacity) {
        data = (T[]) new Object[capacity];
        front = 0;
        rear = -1;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == data.length;
    }

    public void enqueue(T value){
        if(isFull()){
            System.out.println("Queue is full");
            return;
        }
        rear= (rear + 1)% data.length;
        data[rear] = value;
        size++;
    }

    public T dequeue(){
        if(isEmpty()){
            System.out.println("Queue is empty");
            return null;
        }
        T value = data[front];
        data[front] = null;
        front = (front + 1)% data.length;
        size--;
        return value;
    }

    public T peek(){
        if(isEmpty()){
            return null;
        }
        return data[front];
    }

    public int size() {
        return size;
    }





}
