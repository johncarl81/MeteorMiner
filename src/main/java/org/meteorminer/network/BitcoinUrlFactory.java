package org.meteorminer.network;

import org.meteorminer.network.failover.FailoverServerDecoratorFactory;

import javax.inject.Inject;
import java.net.URL;

/**
 * @author John Ericksen
 */
public class BitcoinUrlFactory {

    //in place to accommodate failover extension: https://deepbit.net/failover.php
    @Inject
    private FailoverServerDecoratorFactory failoverServerFactory;

    public URL getUrl() {

        failoverServerFactory = failoverServerFactory.update();

        return failoverServerFactory.getUrl();
    }

    public void errorUpdate() {
        failoverServerFactory.updateError();
    }

    public void pushFailoverDecoratorFactory(FailoverServerDecoratorFactory factory) {
        failoverServerFactory = factory.decorate(failoverServerFactory);
    }
}
