package org.meteorminer.config;

import org.apache.commons.cli.CommandLine;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

/**
 * @author John Ericksen
 */
public class MeteorAdvice {

    private static final String LOCALHOST = "localhost";
    private static final String PORT = "8332";
    private static final int GETWORK_TIMEOUT = 5;

    private CommandLine line;
    private URL bitcoinUrl;
    private Proxy proxy = null;

    public MeteorAdvice(CommandLine line) throws MalformedURLException {
        this.line = line;
        String url = line.getOptionValue("host");
        String port = line.getOptionValue("port");

        url = url == null ? LOCALHOST : url;
        port = port == null ? PORT : port;

        bitcoinUrl = new URL("http://" + url + ":" + port);

        if (line.hasOption("proxy")) {
            proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 8080));
        }
    }

    public String getUsername() {
        return line.getOptionValue("user");
    }

    public String getPassword() {
        return line.getOptionValue("pass");
    }

    public URL getBitcoinUrl() {
        return bitcoinUrl;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public int getGetWorkTimeout() {
        String getWorkTimeout = line.getOptionValue("getwork");

        if (getWorkTimeout != null) {
            return Integer.parseInt(getWorkTimeout);
        }

        return GETWORK_TIMEOUT;
    }
}
