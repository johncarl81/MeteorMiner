package org.meteorminer.network.failover;

/**
 * @author John Ericksen
 */

import com.google.inject.ImplementedBy;

import java.net.URL;

@ImplementedBy(FailoverServerDecoratorFactoryImpl.class)
public interface FailoverServerDecoratorFactory {

    FailoverServerDecoratorFactory decorate(FailoverServerDecoratorFactory failoverServerFactory);

    void updateError();

    URL getUrl();

    FailoverServerDecoratorFactory update();

}
