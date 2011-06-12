package org.meteorminer.config;

import org.apache.commons.cli.CommandLine;
import org.meteorminer.service.URLFactory;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
    private static final String INTENSITY = "4";
    private static final String WORK_SIZE = "-1";
    private static final String LOOP_SIZE = "0";

    private URL bitcoinUrl;
    private String username;
    private String password;
    private String proxyUsername;
    private String proxyPassword;
    private Proxy proxy = null;
    private long getWorkTimeout;
    private boolean verbose;
    private int cpuCount;
    private boolean help;
    private int intensity;
    private int worksize;
    private boolean tandem;
    private List<Integer> gpuIds = new ArrayList<Integer>();
    private Long networkErrorPause = 5000L;
    private URLFactory urlFactory = new URLFactory();
    private int vectors;
    private boolean bfi_int;

    /**
     * Sets up default parameters
     */
    public MeteorAdvice() {
        //defaults
        try {
            this.bitcoinUrl = urlFactory.buildUrl(LOCALHOST, PORT);
            this.getWorkTimeout = Long.parseLong(GET_WORK_TIMEOUT) * 1000;
            this.verbose = false;
            this.intensity = Integer.parseInt(INTENSITY);
            this.worksize = -1;
            this.gpuIds.add(0);
            this.vectors = 2;
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

        bitcoinUrl = urlFactory.buildUrl(url, port);

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
        getWorkTimeout = Long.parseLong(line.getOptionValue("getwork", GET_WORK_TIMEOUT)) * 1000;
        verbose = line.hasOption("verbose");
        cpuCount = Integer.parseInt(line.getOptionValue("cpu", CPU_COUNT));
        intensity = Integer.parseInt(line.getOptionValue("intensity", INTENSITY));

        if (intensity < 0 || intensity > 16) {
            throw new MeteorMinerRuntimeException("Intensity value must be between 0 and 16");
        }

        worksize = Integer.parseInt(line.getOptionValue("worksize", WORK_SIZE));

        tandem = line.hasOption("tandem");

        if (line.hasOption("gpu")) {
            String[] gpuIdParts = line.getOptionValue("gpu").split(",");

            for (String gpuIdString : gpuIdParts) {
                int gpuId = Integer.parseInt(gpuIdString);
                gpuIds.add(gpuId);
            }
        } else {
            //default
            gpuIds.add(0);
        }

        if (line.hasOption("vectors")) {
            vectors = Integer.parseInt(line.getOptionValue("vectors"));

            boolean supported = false;
            for (int v : new int[]{2, 4, 8, 16}) {
                if (vectors == v) {
                    supported = true;
                    break;
                }
            }

            if (!supported) {
                throw new MeteorMinerRuntimeException("Only vector values of 2, 4, 8, and 16 are supported.");
            }
        } else {
            vectors = 1; //no vectors case
        }

        bfi_int = line.hasOption("bfiint");

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

    public long getGetWorkTimeout() {
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

    public List<Integer> getGpuIds() {
        return gpuIds;
    }

    public boolean isTandem() {
        return tandem;
    }

    public Long getNetworkErrorPause() {
        return networkErrorPause;
    }

    public int getVectors() {
        return vectors;
    }

    public boolean isBfi_int() {
        return bfi_int;
    }
}
