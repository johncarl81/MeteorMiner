package org.meteorminer.config.factory;

import com.google.inject.Inject;
import org.meteorminer.config.module.ThreadPoolFactory;

import javax.inject.Provider;
import java.util.concurrent.ExecutorService;

/**
 * Provider to get a new cached thread pool from the thread pool factory
 *
 * @author John Ericksen
 */
public class CachedThreadPoolProvider implements Provider<ExecutorService> {

    @Inject
    private ThreadPoolFactory threadPoolFactory;


    @Override
    public ExecutorService get() {
        return threadPoolFactory.getCachedThreadPool();
    }
}
