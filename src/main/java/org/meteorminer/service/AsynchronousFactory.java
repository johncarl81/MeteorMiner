package org.meteorminer.service;

import com.google.inject.Singleton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author John Ericksen
 */
@Singleton
public class AsynchronousFactory {

    ExecutorService executor;

    public AsynchronousFactory() {
        executor = Executors.newCachedThreadPool();
    }

    public void startRunnable(Runnable runnable) {
        executor.execute(runnable);
    }
}
