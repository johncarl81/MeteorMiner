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
    private static final String CPU_COUNT = "0";
    private static final String INTENSITY = "5";
    private static final String WORK_SIZE = "-1";

    private URL bitcoinUrl;
    private String username;
    private String password;
    private String proxyUsername;
    private String proxyPassword;
    private Proxy proxy = null;
    private int getWorkTimeout;
    private boolean verbose;
    private int cpuCount;
    private boolean help;
    private int intensity;
    private int worksize;

    /**
     * Sets up default parameters
     */
    public MeteorAdvice() {
        //defaults
        try {
            this.bitcoinUrl = new URL("http://" + LOCALHOST + ":" + PORT);
            this.getWorkTimeout = Integer.parseInt(GET_WORK_TIMEOUT);
            this.verbose = false;
            this.intensity = 10;
            this.worksize = -1;
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
        help = line.hasOption("help");

        String url = line.getOptionValue("host", LOCALHOST);
        String port = line.getOptionValue("port", PORT);

        bitcoinUrl = new URL("http://" + url + ":" + port);

        if (line.hasOption("proxy")) {
            String proxyString = line.getOptionValue("proxy");
            final String[] proxyParts = proxyString.split(":");

            if (proxyParts.length < 2 || proxyParts.length > 4) {
                throw new MalformedURLException("Proxy provided needs to be of the form domain:port");
            }

            if (proxyParts.length >= 3) {
                proxyUsername = proxyParts[2];
                proxyPassword = proxyParts[3];
            }

            proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyParts[0], Integer.parseInt(proxyParts[1])));
        }

        username = line.getOptionValue("user", null);
        password = line.getOptionValue("pass", null);
        getWorkTimeout = Integer.parseInt(line.getOptionValue("getwork", GET_WORK_TIMEOUT));
        verbose = line.hasOption("verbose");
        cpuCount = Integer.parseInt(line.getOptionValue("cpu", CPU_COUNT));
        intensity = Integer.parseInt(line.getOptionValue("intensity", INTENSITY));
        worksize = Integer.parseInt(line.getOptionValue("worksize", WORK_SIZE));

    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
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

    public int getCpuCount() {
        return cpuCount;
    }

    public boolean help() {
        return help;
    }

    public int getIntensity() {
        return intensity;
    }

    public int getWorksize() {
        return worksize;
    }
}
