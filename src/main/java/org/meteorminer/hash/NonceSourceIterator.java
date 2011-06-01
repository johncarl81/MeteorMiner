package org.meteorminer.hash;

import com.google.inject.assistedinject.Assisted;
import org.apache.commons.lang.NotImplementedException;

import javax.inject.Inject;
import java.util.Iterator;

/**
 * @author John Ericksen
 */
public class NonceSourceIterator implements Iterator<Integer> {

    private WorkConsumer workSource;
    private int workSize;
    private Integer nonce;

    @Inject
    public NonceSourceIterator(@Assisted int workSize, WorkConsumer workSource) {
        this.workSource = workSource;
        this.workSize = workSize;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Integer next() {
        return workSource.reserveNext(workSize);
    }

    @Override
    public void remove() {
        throw new NotImplementedException("Remove not applicable");
    }
}

