package org.meteorminer.queue;

import org.meteorminer.Work;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author John Ericksen
 */
public interface WorkProducerFactory {

    Producer<Work> createWorkProducer(ArrayBlockingQueue<Work> queue);
}
