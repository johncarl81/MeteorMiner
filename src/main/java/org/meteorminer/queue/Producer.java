package org.meteorminer.queue;


import java.util.concurrent.BlockingQueue;

/**
 * @author John Ericksen
 */
public abstract class Producer<T> implements Runnable {

    private BlockingQueue<T> queue;

    public Producer(BlockingQueue<T> queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            while (true) {
                queue.put(produce());
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public abstract T produce();
}