package org.meteorminer.service;

import javax.inject.Inject;
import java.util.concurrent.ThreadFactory;

/**
 * @author John Ericksen
 */
public class AsynchronousFactory {

    @Inject
    private ThreadFactory threadFactory;

    public void startRunnable(Runnable runnable) {
        new Thread(runnable).start();
    }
}
