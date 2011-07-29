package org.meteorminer.network.failover;

import org.meteorminer.config.ServerProvider;

import javax.inject.Inject;
import java.net.URL;

/**
 * @author John Ericksen
 */
public class BasicServerFactory implements FailoverServerDecoratorFactory {

    @Inject
    private ServerProvider serverProvider;

    @Override
    public URL getUrl() {
        return serverProvider.get().getBitcoinUrl();
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
        serverProvider.iterate();
    }

    public void setServerProvider(ServerProvider serverProvider) {
        this.serverProvider = serverProvider;
    }
}
