package org.meteorminer.config.module;

import com.google.inject.AbstractModule;
import org.meteorminer.config.advice.CPUDeviceAdvice;
import org.meteorminer.domain.CPUDevice;
import org.meteorminer.domain.Device;
import org.meteorminer.hash.HashScanner;
import org.meteorminer.hash.scanHash.ScanHash;
import org.meteorminer.output.Statistics;

/**
 * Module defining CPU device specific configuration
 *
 * @author John Ericksen
 */
public class CPUDeviceModule extends AbstractModule {

    private CPUDeviceAdvice advice;

    public CPUDeviceModule(CPUDeviceAdvice advice) {
        this.advice = advice;
    }

    @Override
    protected void configure() {
        //Assisted injection factories

        bind(Device.class).toInstance(new CPUDevice(advice.getId()));

        bind(HashScanner.class).to(ScanHash.class);

        bind(Statistics.class).asEagerSingleton();
    }
}
