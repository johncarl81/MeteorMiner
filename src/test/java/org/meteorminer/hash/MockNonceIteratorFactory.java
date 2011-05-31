package org.meteorminer.hash;

import java.util.Iterator;

/**
 * @author John Ericksen
 */
public class MockNonceIteratorFactory implements NonceIteratorFactory {

    private int start;
    private int end;

    @Override
    public Iterator<Integer> createNonceIterator(int workgroupSize) {
        return new MockNonceIterator(start, end, workgroupSize);
    }

    public void setRange(int start, int end) {
        this.start = start;
        this.end = end;
    }
}
