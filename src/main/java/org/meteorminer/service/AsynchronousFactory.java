package org.meteorminer.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.meteorminer.config.binding.CachedThreadPool;

import java.util.concurrent.ExecutorService;

/**
 * @author John Ericksen
 */
@Singleton
public class AsynchronousFactory {

    @Inject
    @CachedThreadPool
    private ExecutorService executor;

    public void startRunnable(Runnable runnable) {
        executor.execute(runnable);
    }
}
