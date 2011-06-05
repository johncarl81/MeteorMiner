package org.meteorminer.service;

import com.google.inject.Singleton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author John Ericksen
 */
@Singleton
public class AsynchronousFactory {

    ExecutorService executor;
    ExecutorService gracefulShutdownExecutor;

    public AsynchronousFactory() {
        executor = Executors.newCachedThreadPool();
        gracefulShutdownExecutor = Executors.newCachedThreadPool();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                gracefulShutdownExecutor.shutdown();
                try {
                    gracefulShutdownExecutor.awaitTermination(1, TimeUnit.MINUTES);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void startRunnable(Runnable runnable) {
        executor.execute(runnable);
    }

    public void startRunnableWithGracefulShutdown(Runnable runnable) {
        gracefulShutdownExecutor.execute(runnable);
    }
}
