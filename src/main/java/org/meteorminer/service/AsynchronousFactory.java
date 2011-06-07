package org.meteorminer.service;

import com.google.inject.Singleton;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author John Ericksen
 */
@Singleton
public class AsynchronousFactory {

    @Inject
    private GracefulExecutorShutdownFactory gracefulExecutorShutdownFactory;
    private boolean shutdownHookAdded = false;

    private ExecutorService executor;
    private ExecutorService gracefulShutdownExecutor;

    public AsynchronousFactory() {
        executor = Executors.newCachedThreadPool();
        gracefulShutdownExecutor = Executors.newCachedThreadPool();
    }

    public void startRunnable(Runnable runnable) {
        executor.execute(runnable);
    }

    public void startRunnableWithGracefulShutdown(Runnable runnable) {
        if (!shutdownHookAdded) {
            shutdownHookAdded = true;
            Runtime.getRuntime().addShutdownHook(
                    gracefulExecutorShutdownFactory.buildGracefulExecutorShutdown(gracefulShutdownExecutor));
        }
        if (gracefulShutdownExecutor.isShutdown()) {
            //if the graceful shutdown has occurred, try to execute anyways
            startRunnable(runnable);
        } else {
            gracefulShutdownExecutor.execute(runnable);
        }
    }
}
