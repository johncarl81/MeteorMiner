package org.meteorminer.queue;

import java.util.concurrent.BlockingQueue;

/**
 * @author John Ericksen
 */
public abstract class Consumer<T> implements Runnable {

    private BlockingQueue<T> queue;

    public Consumer(BlockingQueue<T> queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            while (true) {
                consume(queue.take());
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public abstract void consume(T take);
}
