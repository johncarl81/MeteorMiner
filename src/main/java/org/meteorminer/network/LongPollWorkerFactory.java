package org.meteorminer.network;

import java.net.URL;

/**
 * Assisted Injection LongPollWorker Factory
 *
 * @author John Ericksen
 */
public interface LongPollWorkerFactory {

    Runnable buildLongPollWorker(URL bitcoindLongpoll);

}
