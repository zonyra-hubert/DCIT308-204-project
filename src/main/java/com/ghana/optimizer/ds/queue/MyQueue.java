package com.ghana.optimizer.ds.queue;
import com.ghana.optimizer.ds.list.MyLinkedList;

public class MyQueue<T> {
    private MyLinkedList<T>list;

    public MyQueue(){
        list = new MyLinkedList<T>();
    }

    public boolean isEmpty(){
        return list.isEmpty();
    }

    public void enqueue(T value){
        list.addLast(value);
    }

    public T dequeue(){
        if(list.isEmpty()){
            return null;
        }

        return list.removeFirst();
    }

    public T peek() {
    if (list.isEmpty()) {
        return null;
    }

    return list.getFirst();
}



}
