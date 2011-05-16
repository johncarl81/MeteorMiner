package org.meteorminer;

import java.net.URL;

/**
 * @author John Ericksen
 */
public interface LongPollWorkerFactory {

    Runnable buildLongPollWorker(URL bitcoindLongpoll);

}
