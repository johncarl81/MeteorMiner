package org.meteorminer.hash.gpu;

import com.google.inject.assistedinject.Assisted;
import org.meteorminer.config.binding.Synchronous;
import org.meteorminer.domain.Work;

import javax.inject.Inject;

/**
 * Runnable implmeentation calling the HashChecker.check() method with the available arguments
 *
 * @author John Ericksen
 */
public class RunnableHashChecker implements Runnable {

    private MinerResult output;
    private Work work;
    private HashChecker delegate;

    @Inject
    public RunnableHashChecker(@Assisted MinerResult output,
                               @Assisted Work work,
                               @Synchronous HashChecker delegate) {
        this.output = output;
        this.work = work;
        this.delegate = delegate;
    }

    @Override
    public void run() {
        delegate.check(output, work);
    }
}
