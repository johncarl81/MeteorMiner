package org.meteorminer.queue;

import org.meteorminer.Work;

import java.util.concurrent.BlockingQueue;

/**
 * @author John Ericksen
 */
public interface WorkProducerFactory {

    Producer<Work> createWorkProducer(BlockingQueue<Work> queue);
}
