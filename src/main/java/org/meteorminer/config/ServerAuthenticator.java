package org.meteorminer.config;

import org.meteorminer.config.advice.ServerAdvice;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Authenticator class for providing server and proxy authentication.
 *
 * @author John Ericksen
 */
public class ServerAuthenticator extends Authenticator {

    private Map<String, ServerAuthentication> domainAuthenticationMap = new HashMap<String, ServerAuthentication>();

    private class ServerAuthentication {
        private PasswordAuthentication serverAuthentication;
        private PasswordAuthentication proxyAuthentication;

        private ServerAuthentication(String username, String password, String proxyUsername, String proxyPassword) {
            if (username != null) {
                this.serverAuthentication = new PasswordAuthentication(username, nullSafeCharArray(password));
            }
            if (proxyUsername != null) {
                this.proxyAuthentication = new PasswordAuthentication(proxyUsername, nullSafeCharArray(proxyPassword));
            }
        }

        public PasswordAuthentication getServerAuthentication() {
            return serverAuthentication;
        }

        public PasswordAuthentication getProxyAuthentication() {
            return proxyAuthentication;
        }
    }

    public ServerAuthenticator(List<ServerAdvice> serverAdvice) {

        for (ServerAdvice advice : serverAdvice) {
            domainAuthenticationMap.put(advice.getBitcoinUrl().getHost(),
                    new ServerAuthentication(advice.getUsername(), advice.getPassword(),
                            advice.getProxyUsername(), advice.getProxyPassword()));
        }
    }

    private char[] nullSafeCharArray(String proxyPassword) {
        if (proxyPassword == null) {
            return new char[0];
        }
        return proxyPassword.toCharArray();
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {

        if (domainAuthenticationMap.containsKey(getRequestingHost())) {
            ServerAuthentication authentication = domainAuthenticationMap.get(getRequestingHost());

            if (getRequestorType() == RequestorType.SERVER && authentication.getServerAuthentication() != null) {
                return authentication.getServerAuthentication();
            } else if (getRequestorType() == RequestorType.PROXY && authentication.getProxyAuthentication() != null) {
                return authentication.getProxyAuthentication();
            }
        }
        return super.getPasswordAuthentication();
    }
}
