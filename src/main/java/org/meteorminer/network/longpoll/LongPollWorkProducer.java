package org.meteorminer.network.longpoll;

import org.meteorminer.domain.Work;
import org.meteorminer.service.WorkProducer;

/**
 * @author John Ericksen
 */
public class LongPollWorkProducer implements WorkProducer {

    private Work work;

    public boolean hasWork() {
        return work != null;
    }

    public void putWork(Work work) {
        this.work = work;
    }

    @Override
    public Work produce() {
        Work longPollWork = this.work;
        this.work = null; //clear work
        return longPollWork;
    }
}
