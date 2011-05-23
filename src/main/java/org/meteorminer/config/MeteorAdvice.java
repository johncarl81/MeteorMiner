package org.meteorminer.config;

import org.apache.commons.cli.CommandLine;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

/**
 * Advice class decomposing the given CL input into defaulted and provided parameters.
 *
 * @author John Ericksen
 */
public class MeteorAdvice {

    //defaults
    private static final String LOCALHOST = "localhost";
    private static final String PORT = "8332";
    private static final String GET_WORK_TIMEOUT = "5";

    private URL bitcoinUrl;
    private String username;
    private String password;
    private Proxy proxy = null;
    private int getWorkTimeout;
    private boolean verbose;

    /**
     * Sets up default parameters
     *
     * @throws MalformedURLException
     */
    public MeteorAdvice() {
        //defaults
        try {
            this.bitcoinUrl = new URL("http://" + LOCALHOST + ":" + PORT);
            this.getWorkTimeout = Integer.parseInt(GET_WORK_TIMEOUT);
            this.verbose = false;
        } catch (MalformedURLException e) {
            throw new MeteorMinerRuntimeException("Error setting up default configuration");
        }
    }

    /**
     * Sets up default parameters, overridden by the provided commandline arguments
     *
     * @param line
     * @throws MalformedURLException
     */
    public MeteorAdvice(CommandLine line) throws MalformedURLException {
        String url = line.getOptionValue("host", LOCALHOST);
        String port = line.getOptionValue("port", PORT);

        bitcoinUrl = new URL("http://" + url + ":" + port);

        if (line.hasOption("proxy")) {
            proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 8080));
        }

        username = line.getOptionValue("user", null);
        password = line.getOptionValue("pass", null);
        getWorkTimeout = Integer.parseInt(line.getOptionValue("getwork", GET_WORK_TIMEOUT));
        verbose = line.hasOption("verbose");
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public URL getBitcoinUrl() {
        return bitcoinUrl;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public int getGetWorkTimeout() {
        return getWorkTimeout;
    }

    public Boolean isVerbose() {
        return verbose;
    }
}
