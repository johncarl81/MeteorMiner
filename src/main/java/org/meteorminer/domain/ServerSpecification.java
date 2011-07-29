package org.meteorminer.domain;

import org.meteorminer.config.advice.ServerAdvice;

/**
 * @author John Ericksen
 */
public class ServerSpecification {

    private ServerAdvice serverAdvice;

    public ServerSpecification(ServerAdvice serverAdvice) {
        this.serverAdvice = serverAdvice;
    }

    public ServerAdvice getServerAdvice() {
        return serverAdvice;
    }
}
