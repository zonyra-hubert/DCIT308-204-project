package com.ghana.optimizer.ds.stack;
import com.ghana.optimizer.ds.list.MyLinkedList;

public class MyStack<T> {
    private MyLinkedList<T> list;
   

    public MyStack() {
        list = new MyLinkedList<>();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public void push(T value){
        list.addFirst(value);
    }

    public T pop(){
        if(list.isEmpty()){
            return null;
        }
        return list.removeFirst();
    }

    public T peek(){
        if(list.isEmpty()){
            return null;
        }
        return list.getFirst();
    }

    
}
