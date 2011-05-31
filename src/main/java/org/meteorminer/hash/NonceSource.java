package org.meteorminer.hash;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author John Ericksen
 */
public class NonceSource {

    private static final long MAXIMUM = 0xFFFFFFFFL;
    private boolean finished = false;
    private AtomicLong nonce = new AtomicLong();

    public Integer reserveNext(int workSize) {

        if (!finished) {
            long next = nonce.getAndAdd(workSize);
            if (next > MAXIMUM) {
                finished = true;
                return null;
            }
            return (int) next;
        }
        return null;
    }

    public void reset() {
        nonce.lazySet(0);
        finished = false;
    }
}
