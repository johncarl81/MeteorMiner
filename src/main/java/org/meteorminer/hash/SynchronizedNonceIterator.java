package org.meteorminer.hash;

import com.google.inject.assistedinject.Assisted;
import org.apache.commons.lang.NotImplementedException;

import javax.inject.Inject;
import java.util.Iterator;

/**
 * @author John Ericksen
 */
public class SynchronizedNonceIterator implements Iterator<Integer> {

    private NonceSource nonceSource;
    private int workSize;
    private Integer nonce;

    @Inject
    public SynchronizedNonceIterator(@Assisted int workSize, NonceSource nonceSource) {
        this.nonceSource = nonceSource;
        this.workSize = workSize;
    }

    @Override
    public boolean hasNext() {
        nonce = nonceSource.reserveNext(workSize);
        return nonce != null;
    }

    @Override
    public Integer next() {
        return nonce;
    }

    @Override
    public void remove() {
        throw new NotImplementedException("Remove not applicable");
    }
}

