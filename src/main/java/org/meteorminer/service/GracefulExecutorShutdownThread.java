package org.meteorminer.service;

import com.google.inject.assistedinject.Assisted;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author John Ericksen
 */
public class GracefulExecutorShutdownThread extends Thread {

    private ExecutorService executor;

    @Inject
    public GracefulExecutorShutdownThread(@Assisted ExecutorService executor) {
        this.executor = executor;
    }

    public void run() {
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
