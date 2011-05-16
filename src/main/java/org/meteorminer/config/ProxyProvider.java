package org.meteorminer.config;

import com.google.inject.Provider;

import java.net.Proxy;

/**
 * @author John Ericksen
 */
public class ProxyProvider implements Provider<Proxy> {

    private Proxy proxy;

    public ProxyProvider(Proxy proxy) {
        this.proxy = proxy;
    }

    public Proxy get() {
        return proxy;
    }
}
