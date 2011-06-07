package org.meteorminer.network.failover;

import java.net.URL;
import java.util.List;

/**
 * @author John Ericksen
 */
public class FailoverServerSwitcherDecorator implements FailoverServerDecoratorFactory {

    private CircularIterator<FailoverServer> serversIterator;
    private FailoverServer currentServer;
    private FailoverServerDecoratorFactory decorated;
    private long lastUpdate;

    public FailoverServerSwitcherDecorator(List<FailoverServer> servers) {
        this.serversIterator = new CircularIterator<FailoverServer>(servers);
        this.currentServer = this.serversIterator.next();
        this.lastUpdate = System.currentTimeMillis();
    }

    @Override
    public FailoverServerDecoratorFactory decorate(FailoverServerDecoratorFactory decorated) {
        this.decorated = decorated;
        return this;
    }

    @Override
    public void updateError() {
        iterateServer();
    }

    @Override
    public URL getUrl() {
        URL url = currentServer.getUrl();
        if (url == null) {
            url = decorated.getUrl();
        }
        return url;
    }


    @Override
    public FailoverServerDecoratorFactory update() {
        if (currentServer.getTimeToReturn() > 0 &&
                currentServer.getTimeToReturn() * 1000 + lastUpdate < System.currentTimeMillis()) {
            //update
            iterateServer();
        }

        return this;
    }

    private void iterateServer() {
        if (serversIterator.hasNext()) {
            currentServer = serversIterator.next();
        }
        lastUpdate = System.currentTimeMillis();
    }
}
