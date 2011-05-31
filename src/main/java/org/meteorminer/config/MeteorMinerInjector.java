package org.meteorminer.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.nativelibs4java.opencl.CLDevice;

/**
 * @author John Ericksen
 */
public class MeteorMinerInjector {

    private static Injector applicationInjector;

    public synchronized static Injector buildInjector(MeteorAdvice advice) {
        if (applicationInjector == null) {
            applicationInjector = Guice.createInjector(new MeteorMinerModule(advice));
        }
        return applicationInjector;
    }

    public synchronized static Injector getApplicationInjector() {
        if (applicationInjector == null) {
            //setup with default advice
            buildInjector(new MeteorAdvice());
        }

        return applicationInjector;
    }

    public synchronized static Injector buildGPUDeviceInjector(CLDevice device, int id) {
        return getApplicationInjector().createChildInjector(new DeviceModule(), new GPUDeviceModule(device, id));
    }

    public synchronized static Injector buildCPUDeviceInjector(int cpuNumber) {
        return getApplicationInjector().createChildInjector(new DeviceModule(), new CPUDeviceModule(cpuNumber));
    }
}
