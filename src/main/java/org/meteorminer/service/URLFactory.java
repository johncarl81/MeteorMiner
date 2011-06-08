package org.meteorminer.service;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * URL Factory for construction of basic urls
 *
 * @author John Ericksen
 */
public class URLFactory {

    public URL buildUrl(String host, int port) throws MalformedURLException {
        return new URL("http://" + host + ":" + port);
    }

    public URL buildUrl(String host, String port) throws MalformedURLException {
        return new URL("http://" + host + ":" + port);
    }
}
