package org.meteorminer.config;

import org.meteorminer.config.advice.MeteorAdvice;
import org.meteorminer.config.advice.ServerAdvice;
import org.meteorminer.network.failover.CircularIterator;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class ServerProvider implements Provider<ServerAdvice> {

    private CircularIterator<ServerAdvice> serverAdviceIterator;
    private ServerAdvice current;

    @Inject
    public ServerProvider(MeteorAdvice meteorAdvice) {
        this.serverAdviceIterator = new CircularIterator<ServerAdvice>(meteorAdvice.getServers());
        iterate();
    }

    public void iterate() {
        if (serverAdviceIterator.hasNext()) {
            current = serverAdviceIterator.next();
        }
    }

    @Override
    public ServerAdvice get() {
        return current;
    }
}
