package org.meteorminer;

import org.meteorminer.binding.BitcoinProxy;
import org.meteorminer.binding.BitcoinUrl;

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
    @BitcoinUrl
    private URL bitcoind;
    @Inject
    @Nullable
    @BitcoinProxy
    private Proxy proxy;

    public HttpURLConnection getBitcoinConnection() throws IOException {
        return getBitcoinConnection(bitcoind);
    }

    public HttpURLConnection getBitcoinConnection(URL url) throws IOException {
        if (proxy == null)
            return (HttpURLConnection) url.openConnection();
        else
            return (HttpURLConnection) url.openConnection(proxy);
    }
}
