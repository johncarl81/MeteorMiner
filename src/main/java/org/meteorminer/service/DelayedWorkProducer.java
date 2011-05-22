package org.meteorminer.service;

import com.google.inject.Inject;
import org.meteorminer.domain.Work;

import javax.inject.Singleton;

/**
 * @author John Ericksen
 */
@Singleton
public class DelayedWorkProducer implements WorkProducer {

    @Inject
    private WorkProducerImpl workProducer;
    private Work work = null;

    public void delayedProduce() {
        work = workProducer.produce();
    }

    @Override
    public Work produce() {
        Work returnWork = work;
        clear();
        return returnWork;
    }

    public boolean hasWork() {
        return work != null;
    }

    public void clear() {
        work = null;
    }
}
