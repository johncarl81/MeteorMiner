package org.meteorminer.hash;

import org.meteorminer.domain.Work;

/**
 * @author John Ericksen
 */
public interface HashScanner {

    void scan(Work work);

    long getNonceCount();

    void stop();
}
