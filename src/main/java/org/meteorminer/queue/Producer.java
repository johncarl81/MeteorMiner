package org.meteorminer.queue;


import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author John Ericksen
 */
public abstract class Producer<T> implements Runnable {

    private ArrayBlockingQueue<T> queue;

    public Producer(ArrayBlockingQueue<T> queue) {
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