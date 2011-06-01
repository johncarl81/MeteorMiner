package org.meteorminer.network;

import java.net.URL;

/**
 * Assisted Injection LongPollWorker Factory
 *
 * @author John Ericksen
 */
public interface LongPollWorkerFactory {

    LongPollWorker buildLongPollWorker(URL bitcoindLongpoll);

}
