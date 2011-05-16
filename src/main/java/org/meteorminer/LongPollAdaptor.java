package org.meteorminer;

import org.meteorminer.binding.BitcoinUrl;

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

    private Thread longpollThread = null;

    public void setupLongpoll(HttpURLConnection connection) throws MalformedURLException {
        if (longpollThread == null) {
            String xlongpolling = connection.getHeaderField("X-Long-Polling");

            if (xlongpolling != null) {

                URL bitcoindLongpoll;

                if (xlongpolling.startsWith("http"))
                    bitcoindLongpoll = new URL(xlongpolling);
                else if (xlongpolling.startsWith("/"))
                    bitcoindLongpoll = new URL(bitcoind.getProtocol(), bitcoind.getHost(), bitcoind.getPort(),
                            xlongpolling);
                else
                    bitcoindLongpoll = new URL(bitcoind.getProtocol(), bitcoind.getHost(), bitcoind.getPort(),
                            (bitcoind.getFile() + "/" + xlongpolling).replace("//", "/"));


                System.out.println("Enabling long poll support: " + bitcoindLongpoll);

                longpollThread = new Thread(longPollWorkerFactory.buildLongPollWorker(bitcoindLongpoll));
                longpollThread.start();
            }
        }
    }
}
