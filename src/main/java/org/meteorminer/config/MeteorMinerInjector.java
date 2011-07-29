package org.meteorminer.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.nativelibs4java.opencl.CLDevice;
import org.meteorminer.config.advice.CPUDeviceAdvice;
import org.meteorminer.config.advice.GPUDeviceAdvice;
import org.meteorminer.config.advice.MeteorAdvice;
import org.meteorminer.config.module.*;

/**
 * Injector helper class providing the various Guice injectors.
 *
 * @author John Ericksen
 */
public class MeteorMinerInjector {

    private static Injector applicationInjector;
    private static Injector minerInjector;

    /**
     * General Application level injection.  Provides the basic layers of the applicaiton.
     *
     * @param advice
     * @return meteor application injector
     */
    public synchronized static Injector getInjector(MeteorAdvice advice) {
        if (applicationInjector == null) {
            applicationInjector = Guice.createInjector(
                    new MeteorApplicationModule(advice));
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

    /**
     * GPU level injector.  Provides the GPU child injector.
     *
     * @param device
     * @param advice
     * @return GPU Injector
     */
    public synchronized static Injector getGPUDeviceInjector(CLDevice device, GPUDeviceAdvice advice) {
        return getMinerInjector().createChildInjector(new DeviceModule(), new GPUDeviceModule(device, advice));
    }

    /**
     * CPU level injector.  Provides the CPU child injector.
     *
     * @param cpuNumber
     * @return CPU Injector
     */
    public synchronized static Injector getCPUDeviceInjector(CPUDeviceAdvice cpuNumber) {
        return getMinerInjector().createChildInjector(new DeviceModule(), new CPUDeviceModule(cpuNumber));
    }

    /**
     * Miner level injector.
     *
     * @return Injector for the miner.
     */
    public synchronized static Injector getMinerInjector() {
        if (minerInjector == null) {
            minerInjector = getApplicationInjector().createChildInjector(new MinerModule(),
                    new FailoverExtensionModule(),
                    new LongPollExtensionModule());
        }
        return minerInjector;
    }
}
