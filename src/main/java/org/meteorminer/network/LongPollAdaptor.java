package org.meteorminer.network;

import org.meteorminer.config.binding.BitcoinUrl;
import org.meteorminer.output.CLInterface;
import org.meteorminer.service.AsynchronousFactory;

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
    private CLInterface output;
    @Inject
    private LongPollWorkerFactory longPollWorkerFactory;
    @Inject
    private AsynchronousFactory asynchronousFactory;

    private Thread longPollThread = null;

    public synchronized void setupLongpoll(HttpURLConnection connection) throws MalformedURLException {
        if (longPollThread == null) {
            String xlongpolling = connection.getHeaderField("X-Long-Polling");

            if (xlongpolling != null) {

                URL bitcoindLongpoll = parseLongPollURL(xlongpolling);

                longPollThread = new Thread(longPollWorkerFactory.buildLongPollWorker(bitcoindLongpoll));
                longPollThread.start();

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
