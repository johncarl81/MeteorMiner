package org.meteorminer.network.longpoll;

import org.meteorminer.config.binding.BitcoinUrl;
import org.meteorminer.network.RPCExtension;
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
public class LongPollExtension implements RPCExtension {

    @Inject
    @BitcoinUrl
    private URL bitcoind;
    @Inject
    private CLInterface output;
    @Inject
    private LongPollWorkerFactory longPollWorkerFactory;
    @Inject
    private AsynchronousFactory asynchronousFactory;

    //private Thread longPollThread = null;
    private boolean started = false;

    public synchronized void setup(HttpURLConnection connection) {
        if (!started) {
            String xlongpolling = connection.getHeaderField("X-Long-Polling");

            if (xlongpolling != null) {
                try {
                    URL bitcoindLongpoll = parseLongPollURL(xlongpolling);

                    asynchronousFactory.startRunnable(longPollWorkerFactory.buildLongPollWorker(bitcoindLongpoll));

                    output.notification("Long Poll Extension: Enabled");
                    started = true;
                } catch (MalformedURLException e) {
                    output.error(e);
                }
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
