package com.ghana.optimizer.ds.list;

public class MyLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    public MyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public void addFirst(T value){
        Node<T> newNode = new Node<>(value);

        if(isEmpty()){
            head = newNode;
            tail = newNode;
        }
        else{
            newNode.setNext(head);
            head.setPrevious(newNode);
            head = newNode;
        }
        size++;
    }

    public void addLast(T value){
        Node<T> newNode = new Node<>(value);

        if(isEmpty()){
            head = newNode;
            tail = newNode;
        }
        else{
            tail.setNext(newNode);
            newNode.setPrevious(tail);
            tail = newNode;
        }
        size++;
    }

    public boolean insertAfter(T target, T value){
        Node<T> current = head;

        while(current != null){
            if(current.getData().equals(target)){
                Node<T> newNode = new Node<>(value);
                newNode.setNext(current.getNext());
                newNode.setPrevious(current);

                if(current.getNext() != null){
                    current.getNext().setPrevious(newNode);
                }
                else{
                    tail = newNode;
                }
                current.setNext(newNode);
                size++;
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    public boolean remove(T value){
        Node<T> current = head;

        while(current != null){
            if(current.getData().equals(value)){
                if(current == head){
                    head = current.getNext();
                    if(head != null){
                        head.setPrevious(null);
                    }
                    else{
                        tail = null;
                    }
                }
                else if(current == tail){
                    tail = current.getPrevious();

                    if(tail != null){
                        tail.setNext(null);
                    }
                }
                else{
                    current.getPrevious().setNext(current.getNext());
                    current.getNext().setPrevious(current.getPrevious());
                }
                size--;
                return true;
            }
                current = current.getNext();
        }
        return false;
    }

    public T removeFirst(){
        if(isEmpty()){
            return null;
        }

        T value = head.getData();
        if(head == tail){
            head = null;
            tail = null;
        }
        else{
            head = head.getNext();
            head.setPrevious(null);
        }
        size--;
        return value;   
    }

    public T removeLast(){
        if(isEmpty()){
            return null;
        }

        T value = tail.getData();
        if(head == tail){
            head = null;
            tail = null;
        }
        else{
            tail = tail.getPrevious();
            tail.setPrevious(null);
        }
        size--;
        return value;
    }

    public T getFirst(){
        if(isEmpty()){
            return null;
        }
        return head.getData();
    }

    public T getLast(){
        if(isEmpty()){
            return null;
        }
        return tail.getData();
    }

    public int size(){
        return size;
    }
    
    public MyLinkedListIterator<T> iterator() {
        return new MyLinkedListIterator<>(head);
    }

    }

