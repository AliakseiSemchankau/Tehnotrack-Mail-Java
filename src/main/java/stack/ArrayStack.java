package stack;

import java.util.Collection;

/**
 *
 */
public class ArrayStack<E> implements Stack<E> {

    private Object[] elements;
    private int capacity;
    private int size;

    public ArrayStack(int capacity) {
        this.capacity = capacity;
        size = 0;
        elements = new Object[capacity];
    }

    @Override
    public void push(E element) throws StackException {
        if (isFull()) {
            throw new StackException("Stack overflow");
        }
        elements[size] = element;
        size++;
    }

    @Override
    public boolean isFull() {
        return size == capacity;
    }

    @Override
    public E pop() throws StackException {
        if (size > 0) {
            E element = (E) elements[size - 1];
            elements[size - 1] = null;
            --size;
            return element;
        } else {
            throw new StackException("stack is empty");
        }
    }

    @Override
    public E peek() {
        if (size > 0) {
            try {
                return pop();
            } catch (StackException sExc) {
                System.out.println("that is impossible");
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    @Override
    public void pushAll(Collection<E> src) throws StackException {
        for(E e : src) {
            push(e);
        }
    }

    @Override
    public void popAll(Collection<E> dst) throws StackException {
        while(!isEmpty()) {
            dst.add(pop());
        }
    }
}