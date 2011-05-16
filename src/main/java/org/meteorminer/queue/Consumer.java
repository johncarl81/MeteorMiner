package org.meteorminer.queue;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author John Ericksen
 */
public abstract class Consumer<T> implements Runnable {

    private ArrayBlockingQueue<T> queue;

    public Consumer(ArrayBlockingQueue<T> queue) {
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
