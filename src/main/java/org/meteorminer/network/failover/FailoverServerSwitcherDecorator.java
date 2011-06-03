package org.meteorminer.network.failover;

import java.net.URL;
import java.util.List;

/**
 * @author John Ericksen
 */
public class FailoverServerSwitcherDecorator implements FailoverServerDecoratorFactory {

    private List<FailoverServer> servers;
    private FailoverServerDecoratorFactory decorated;
    private long lastUpdate;

    private int currentServerIndex;

    public FailoverServerSwitcherDecorator(List<FailoverServer> servers) {
        this.servers = servers;
        this.currentServerIndex = 0;
        this.lastUpdate = System.currentTimeMillis();
    }

    @Override
    public FailoverServerDecoratorFactory decorate(FailoverServerDecoratorFactory decorated) {
        this.decorated = decorated;
        return this;
    }

    @Override
    public void updateError() {
        updateCurrentServer();
    }

    @Override
    public URL getUrl() {
        URL url = getCurrentServer().getUrl();
        if (url == null) {
            url = decorated.getUrl();
        }
        return url;
    }


    @Override
    public FailoverServerDecoratorFactory update() {

        FailoverServer currentServer = getCurrentServer();

        if (currentServer.getTimeToReturn() > 0 &&
                currentServer.getTimeToReturn() * 1000 + lastUpdate > System.currentTimeMillis()) {
            //update
            updateCurrentServer();
        }

        return this;
    }

    private void updateCurrentServer() {
        if (currentServerIndex < servers.size()) {
            currentServerIndex++;
        } else {
            currentServerIndex = 0;
        }
        lastUpdate = System.currentTimeMillis();
    }

    private FailoverServer getCurrentServer() {
        return servers.get(currentServerIndex);
    }
}
