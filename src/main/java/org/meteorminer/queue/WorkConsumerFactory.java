package org.meteorminer.queue;

import org.meteorminer.Work;

import java.util.concurrent.BlockingQueue;

/**
 * @author John Ericksen
 */
public interface WorkConsumerFactory {

    public Consumer<Work> createWorkConsumer(BlockingQueue<Work> queue);
}
