package org.meteorminer.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.nativelibs4java.opencl.CLDevice;

/**
 * @author John Ericksen
 */
public class MeteorMinerInjector {

    private static Injector applicationInjector;
    private static Injector minerInjector;

    public synchronized static Injector getInjector(MeteorAdvice advice) {
        if (applicationInjector == null) {
            applicationInjector = Guice.createInjector(new MeteorMinerModule(advice));
        }
        return applicationInjector;
    }

    public synchronized static Injector getApplicationInjector() {
        if (applicationInjector == null) {
            //setup with default advice
            getInjector(new MeteorAdvice());
        }

        return applicationInjector;
    }

    public synchronized static Injector getGPUDeviceInjector(CLDevice device, int id) {
        return getMinerInjector().createChildInjector(new DeviceModule(), new GPUDeviceModule(device, id));
    }

    public synchronized static Injector getCPUDeviceInjector(int cpuNumber) {
        return getMinerInjector().createChildInjector(new DeviceModule(), new CPUDeviceModule(cpuNumber));
    }

    public synchronized static Injector getMinerInjector() {
        if (minerInjector == null) {
            minerInjector = getApplicationInjector().createChildInjector(new MinerModule());
        }
        return minerInjector;
    }
}
