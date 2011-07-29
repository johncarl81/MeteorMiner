package org.meteorminer.config.advice;

import java.net.Proxy;
import java.net.URL;

/**
 * @author John Ericksen
 */
public class ServerAdvice {
    private URL bitcoinUrl;
    private String username;
    private String password;
    private String proxyUsername;
    private String proxyPassword;
    private Proxy proxy = null;
    private long getWorkTimeout;

    public URL getBitcoinUrl() {
        return bitcoinUrl;
    }

    public void setBitcoinUrl(URL bitcoinUrl) {
        this.bitcoinUrl = bitcoinUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public void setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public long getGetWorkTimeout() {
        return getWorkTimeout;
    }

    public void setGetWorkTimeout(long getWorkTimeout) {
        this.getWorkTimeout = getWorkTimeout;
    }
}
