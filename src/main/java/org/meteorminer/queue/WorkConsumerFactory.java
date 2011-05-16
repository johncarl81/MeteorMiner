package org.meteorminer.queue;

import org.meteorminer.Work;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author John Ericksen
 */
public interface WorkConsumerFactory {

    public Consumer<Work> createWorkConsumer(ArrayBlockingQueue<Work> queue);
}
