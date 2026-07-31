package com.ghana.optimizer.ds.list;

public class MyLinkedListIterator<T>{
    private Node<T> current;

    public MyLinkedListIterator(Node<T> startNode){
        this.current = startNode;
    }

    public boolean hasNext(){
        return current != null;
    }

    public T next(){
        if(current == null){
            return null;
        }
        T value = current.getData();
        current = current.getNext();
        return value;
    }

}