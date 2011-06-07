package org.meteorminer.network.failover;

import org.meteorminer.config.binding.BitcoinUrl;

import javax.inject.Inject;
import java.net.URL;

/**
 * @author John Ericksen
 */
public class BasicServerFactory implements FailoverServerDecoratorFactory {

    @Inject
    @BitcoinUrl
    private URL bitcoind;

    @Override
    public URL getUrl() {
        return bitcoind;
    }

    @Override
    public FailoverServerDecoratorFactory update() {
        return this; // no decoration.. this is the endpoint.
    }

    @Override
    public FailoverServerDecoratorFactory decorate(FailoverServerDecoratorFactory failoverServerFactory) {
        return this; // no decoration.. this is the endpoint.
    }

    @Override
    public void updateError() {
        //noop
    }
}
