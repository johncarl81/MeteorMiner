package org.meteorminer.service;

import com.google.inject.Singleton;

/**
 * @author John Ericksen
 */
@Singleton
public class AsynchronousFactory {

    public void startRunnable(Runnable runnable) {
        new Thread(runnable).start();
    }
}
