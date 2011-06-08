package org.meteorminer.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author John Ericksen
 */
public class ThreadPoolFactory {

    public ExecutorService getCachedThreadPool() {
        return Executors.newCachedThreadPool();
    }

    public ExecutorService getFixedThreadPool(int size) {
        return Executors.newFixedThreadPool(size);
    }

}
