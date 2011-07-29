package org.meteorminer.network;

import org.meteorminer.config.advice.ServerAdvice;

import javax.inject.Inject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author John Ericksen
 */
public class BitcoinConnectionFactory {

    @Inject
    private BitcoinUrlFactory bitcoinUrlFactory;

    @Inject
    private ServerAdvice serverAdvice;

    public HttpURLConnection getBitcoinConnection() throws IOException {
        return getBitcoinConnection(bitcoinUrlFactory.getUrl());
    }

    public HttpURLConnection getBitcoinConnection(URL url) throws IOException {
        if (serverAdvice.getProxy() == null) {
            return (HttpURLConnection) url.openConnection();
        } else {
            return (HttpURLConnection) url.openConnection(serverAdvice.getProxy());
        }
    }

    public void errorUpdate() {
        bitcoinUrlFactory.errorUpdate();
    }
}
