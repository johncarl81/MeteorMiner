package org.meteorminer.service;

import java.util.concurrent.ExecutorService;

/**
 * @author John Ericksen
 */
public interface GracefulExecutorShutdownFactory {

    GracefulExecutorShutdownThread buildGracefulExecutorShutdown(ExecutorService gracefulShutdownExecutor);

}
