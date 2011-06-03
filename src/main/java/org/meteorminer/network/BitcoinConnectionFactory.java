package org.meteorminer.network;

import org.meteorminer.config.binding.BitcoinProxy;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

/**
 * @author John Ericksen
 */
public class BitcoinConnectionFactory {

    @Inject
    private BitcoinUrlFactory bitcoinUrlFactory;

    @Inject
    @Nullable
    @BitcoinProxy
    private Proxy proxy;

    public HttpURLConnection getBitcoinConnection() throws IOException {
        return getBitcoinConnection(bitcoinUrlFactory.getUrl());
    }

    public HttpURLConnection getBitcoinConnection(URL url) throws IOException {
        if (proxy == null)
            return (HttpURLConnection) url.openConnection();
        else
            return (HttpURLConnection) url.openConnection(proxy);
    }

    public void errorUpdate() {
        bitcoinUrlFactory.errorUpdate();
    }
}
