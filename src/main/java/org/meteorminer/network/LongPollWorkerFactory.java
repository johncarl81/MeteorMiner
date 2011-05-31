package org.meteorminer.network;

import org.meteorminer.hash.HashScanner;

import java.net.URL;
import java.util.Set;

/**
 * Assisted Injection LongPollWorker Factory
 *
 * @author John Ericksen
 */
public interface LongPollWorkerFactory {

    LongPollWorker buildLongPollWorker(URL bitcoindLongpoll, Set<HashScanner> hashScanners);

}
