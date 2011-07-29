package org.meteorminer.hash;

import org.meteorminer.config.advice.CPUDeviceAdvice;
import org.meteorminer.config.advice.GPUDeviceAdvice;
import org.meteorminer.config.advice.MeteorAdvice;
import org.meteorminer.config.advice.ServerAdvice;
import org.meteorminer.service.URLFactory;

import java.net.MalformedURLException;

/**
 * @author John Ericksen
 */
public class MockAdviceFactory {

    private static final MockAdviceFactory INSTANCE = new MockAdviceFactory();
    private static final URLFactory URL_FACTORY = new URLFactory();

    private MockAdviceFactory() {
        //noop singleton constructor
    }

    public static MockAdviceFactory getInstance() {
        return INSTANCE;
    }

    public CPUDeviceAdvice buildDefaultCPUAdvice() {
        CPUDeviceAdvice advice = new CPUDeviceAdvice();

        advice.setId(0);

        return advice;
    }

    public GPUDeviceAdvice buildDefaultGPUAdvice() {
        GPUDeviceAdvice advice = new GPUDeviceAdvice();

        advice.setId(0);
        advice.setBfi_int(false);
        advice.setVectors(1);
        advice.setIntensity(4);
        advice.setWorksize(32);

        return advice;
    }

    public MeteorAdvice buildDefaultMeteorAdvice() {
        MeteorAdvice meteorAdvice = new MeteorAdvice();

        meteorAdvice.getServers().add(buildDefaultServerAdvice());
        meteorAdvice.getGpuDevices().add(buildDefaultGPUAdvice());
        meteorAdvice.getCpuDevices().add(buildDefaultCPUAdvice());

        return meteorAdvice;
    }

    public ServerAdvice buildDefaultServerAdvice() {
        ServerAdvice serverAdvice = new ServerAdvice();

        try {
            serverAdvice.setBitcoinUrl(URL_FACTORY.buildUrl("localhost", "8332"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return serverAdvice;

    }
}
