package org.meteorminer.queue;

import com.google.inject.assistedinject.Assisted;
import org.meteorminer.Work;

import javax.inject.Inject;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author John Ericksen
 */
public class WorkConsumer extends Consumer<Work> {

    private Miner miner;

    @Inject
    public WorkConsumer(@Assisted ArrayBlockingQueue<Work> queue, Miner miner) {
        super(queue);
        this.miner = miner;
    }

    @Override
    public void consume(Work work) {
        miner.mine(work);
    }
}
