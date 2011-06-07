package org.meteorminer.network.failover;

import org.apache.commons.lang.NotImplementedException;

import java.util.Iterator;
import java.util.List;

public class CircularIterator<T> implements Iterator<T> {

    private List<T> list;
    private int currentPosition;

    public CircularIterator(List<T> list) {
        this.list = list;
        currentPosition = 0;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public T next() {
        T next = list.get(currentPosition++);
        currentPosition = currentPosition % list.size();
        return next;
    }

    @Override
    public void remove() {
        throw new NotImplementedException("Remove is not implemented for this iterator");
    }
}
