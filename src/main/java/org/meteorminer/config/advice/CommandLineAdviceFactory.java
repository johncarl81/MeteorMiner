package org.meteorminer.config.advice;

import org.apache.commons.cli.CommandLine;
import org.meteorminer.config.MeteorMinerRuntimeException;
import org.meteorminer.service.URLFactory;

import javax.inject.Inject;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;

/**
 * @author John Ericksen
 */
public class CommandLineAdviceFactory {

    //defaults
    private static final String LOCALHOST = "localhost";
    private static final String PORT = "8332";
    private static final String GET_WORK_TIMEOUT = "5";
    private static final String CPU_COUNT = "0";
    private static final String INTENSITY = "4";
    private static final String WORK_SIZE = "-1";

    private URLFactory urlFactory;

    @Inject
    public CommandLineAdviceFactory(URLFactory urlFactory) {
        this.urlFactory = urlFactory;
    }

    public MeteorAdvice buildAdvice(CommandLine line) throws MalformedURLException {

        MeteorAdvice advice = new MeteorAdvice();

        //general setup
        advice.setVerbose(line.hasOption("verbose"));
        advice.setTandem(line.hasOption("tandem"));

        //server setup
        ServerAdvice serverAdvice = new ServerAdvice();

        String url = line.getOptionValue("host", LOCALHOST);
        String port = line.getOptionValue("port", PORT);

        serverAdvice.setBitcoinUrl(urlFactory.buildUrl(url, port));

        if (line.hasOption("proxy")) {
            String proxyString = line.getOptionValue("proxy");
            final String[] proxyParts = proxyString.split(":");

            if (proxyParts.length < 2 || proxyParts.length > 4) {
                throw new MalformedURLException("Proxy provided needs to be of the form domain:port");
            }

            if (proxyParts.length > 2) {
                serverAdvice.setProxyUsername(proxyParts[2]);
                serverAdvice.setProxyPassword(proxyParts.length != 4 || proxyParts[3] == null ? "" : proxyParts[3]);
            }

            serverAdvice.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyParts[0], Integer.parseInt(proxyParts[1]))));
        }

        serverAdvice.setUsername(line.getOptionValue("user", null));
        serverAdvice.setPassword(line.getOptionValue("pass", ""));


        serverAdvice.setGetWorkTimeout(Long.parseLong(line.getOptionValue("getwork", GET_WORK_TIMEOUT)) * 1000);

        advice.getServers().add(serverAdvice);

        //cpu setup
        int cpuCount = Integer.parseInt(line.getOptionValue("cpu", CPU_COUNT));
        for (int i = 0; i < cpuCount; i++) {
            advice.getCpuDevices().add(new CPUDeviceAdvice(i));
        }

        int intensity = Integer.parseInt(line.getOptionValue("intensity", INTENSITY));

        if (intensity < 0 || intensity > 16) {
            throw new MeteorMinerRuntimeException("Intensity value must be between 0 and 16");
        }

        int worksize = Integer.parseInt(line.getOptionValue("worksize", WORK_SIZE));
        boolean bfi_int = line.hasOption("bfiint");

        int vectors = 1; //default vectors = 1
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
        }

        if (line.hasOption("gpu")) {
            String[] gpuIdParts = line.getOptionValue("gpu").split(",");

            for (String gpuIdString : gpuIdParts) {
                GPUDeviceAdvice gpuAdvice = new GPUDeviceAdvice();

                gpuAdvice.setIntensity(intensity);
                gpuAdvice.setWorksize(worksize);
                gpuAdvice.setVectors(vectors);
                gpuAdvice.setId(Integer.parseInt(gpuIdString));
                gpuAdvice.setBfi_int(bfi_int);

                advice.getGpuDevices().add(gpuAdvice);
            }
        }

        return advice;
    }
}
