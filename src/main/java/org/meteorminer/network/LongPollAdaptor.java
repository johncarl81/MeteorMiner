package org.meteorminer.network;

import org.meteorminer.config.binding.BitcoinUrl;
import org.meteorminer.output.CLInterface;
import org.meteorminer.service.AsynchronousFactory;

import javax.inject.Inject;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author John Ericksen
 */
public class LongPollAdaptor {

    @Inject
    @BitcoinUrl
    private URL bitcoind;
    @Inject
    private LongPollWorkerFactory longPollWorkerFactory;
    @Inject
    private AsynchronousFactory asyncFactory;
    @Inject
    private CLInterface output;

    private Runnable longpollThread = null;

    public void setupLongpoll(HttpURLConnection connection) throws MalformedURLException {
        if (longpollThread == null) {
            String xlongpolling = connection.getHeaderField("X-Long-Polling");

            if (xlongpolling != null) {

                URL bitcoindLongpoll = parseLongPollURL(xlongpolling);

                longpollThread = longPollWorkerFactory.buildLongPollWorker(bitcoindLongpoll);

                asyncFactory.startRunnable(longpollThread);

                output.notification("Long poll support enabled: " + bitcoindLongpoll);
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
