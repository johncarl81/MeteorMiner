package org.meteorminer.network;

import org.meteorminer.binding.BitcoinUrl;
import org.meteorminer.logging.CLLogger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author John Ericksen
 */
@Singleton
public class LongPollAdaptor {

    @Inject
    @BitcoinUrl
    private URL bitcoind;
    @Inject
    private LongPollWorkerFactory longPollWorkerFactory;
    @Inject
    private CLLogger logger;

    private Thread longpollThread = null;

    public void setupLongpoll(HttpURLConnection connection) throws MalformedURLException {
        if (longpollThread == null) {
            String xlongpolling = connection.getHeaderField("X-Long-Polling");

            if (xlongpolling != null) {

                URL bitcoindLongpoll = parseLongPollURL(xlongpolling);

                longpollThread = new Thread(longPollWorkerFactory.buildLongPollWorker(bitcoindLongpoll));
                longpollThread.start();

                logger.notification("Long poll support enabled: " + bitcoindLongpoll);
            }
        }
    }

    private URL parseLongPollURL(String xlongpolling) throws MalformedURLException {
        if (xlongpolling.startsWith("http"))
            return new URL(xlongpolling);
        else if (xlongpolling.startsWith("/"))
            return new URL(bitcoind.getProtocol(), bitcoind.getHost(), bitcoind.getPort(),
                    xlongpolling);
        else
            return new URL(bitcoind.getProtocol(), bitcoind.getHost(), bitcoind.getPort(),
                    (bitcoind.getFile() + "/" + xlongpolling).replace("//", "/"));
    }
}
