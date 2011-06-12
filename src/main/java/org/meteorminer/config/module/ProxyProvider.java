package org.meteorminer.config.module;

import com.google.inject.Provider;

import java.net.Proxy;

/**
 * Simple provider for a Proxy instance
 *
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
