package org.meteorminer.network.failover;

import java.net.URL;

/**
 * @author John Ericksen
 */
public class FailoverServerMaintenanceDecorator implements FailoverServerDecoratorFactory {

    private FailoverServer server;
    private long start;
    private FailoverServerDecoratorFactory decorated;

    public FailoverServerMaintenanceDecorator(FailoverServer server) {
        this.server = server;
        this.start = System.currentTimeMillis();
    }

    @Override
    public FailoverServerDecoratorFactory decorate(FailoverServerDecoratorFactory decorated) {
        this.decorated = decorated;
        return this;
    }

    @Override
    public void updateError() {
        //noop
    }

    @Override
    public URL getUrl() {
        URL url = server.getUrl();
        if (url == null) {
            url = decorated.getUrl();
        }
        return url;
    }

    @Override
    public FailoverServerDecoratorFactory update() {
        if (start + server.getTimeToReturn() < System.currentTimeMillis()) {
            return decorated.update();
        }

        return this;
    }
}
