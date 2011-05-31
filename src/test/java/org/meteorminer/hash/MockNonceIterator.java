package org.meteorminer.hash;

import java.util.Iterator;

/**
 * @author John Ericksen
 */
public class MockNonceIterator implements Iterator<Integer> {

    private int end;
    private int worksize;
    private int current;

    public MockNonceIterator(int start, int end, int worksize) {
        this.current = start;
        this.end = start + end;
        this.worksize = worksize;
    }

    @Override
    public boolean hasNext() {
        return current < end;
    }

    @Override
    public Integer next() {
        int currentTemp = current;
        current += worksize;
        return currentTemp;
    }

    @Override
    public void remove() {
        //noop
    }
}
