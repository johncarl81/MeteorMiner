package org.meteorminer.config;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * Authenticator class for providing server and proxy authentication.
 *
 * @author John Ericksen
 */
public class ServerAuthenticator extends Authenticator {

    private PasswordAuthentication serverAuthentication;
    private PasswordAuthentication proxyAuthentication;

    public ServerAuthenticator(String username, String password, String proxyUsername, String proxyPassword) {
        if (username != null) {
            this.serverAuthentication = new PasswordAuthentication(username, nullSafeCharArray(password));
        }
        if (proxyUsername != null) {
            this.proxyAuthentication = new PasswordAuthentication(proxyUsername, nullSafeCharArray(proxyPassword));
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
        if (getRequestorType() == RequestorType.SERVER && serverAuthentication != null) {
            return serverAuthentication;
        } else if (getRequestorType() == RequestorType.PROXY && proxyAuthentication != null) {
            return proxyAuthentication;
        }
        return super.getPasswordAuthentication();
    }
}
