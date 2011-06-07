package org.meteorminer.service;

import com.google.inject.assistedinject.Assisted;
import org.meteorminer.output.CLInterface;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author John Ericksen
 */
public class GracefulExecutorShutdownThread extends Thread {

    private ExecutorService executor;
    private CLInterface output;

    @Inject
    public GracefulExecutorShutdownThread(@Assisted ExecutorService executor, CLInterface output) {
        this.executor = executor;
        this.output = output;
    }

    public void run() {
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            output.error(e);
        }
    }
}
