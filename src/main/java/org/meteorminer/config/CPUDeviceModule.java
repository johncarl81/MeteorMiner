package org.meteorminer.config;

import com.google.inject.AbstractModule;
import org.meteorminer.binding.AsyncPreferred;
import org.meteorminer.domain.CPUDevice;
import org.meteorminer.domain.Device;
import org.meteorminer.hash.HashScanner;
import org.meteorminer.hash.scanHash.ScanHash;
import org.meteorminer.output.Statistics;

/**
 * @author John Ericksen
 */
public class CPUDeviceModule extends AbstractModule {

    private int cpuNumber;

    public CPUDeviceModule(int cpuNumber) {
        this.cpuNumber = cpuNumber;
    }

    @Override
    protected void configure() {
        //Assisted injection factories

        bind(Device.class).toInstance(new CPUDevice(cpuNumber));

        bind(HashScanner.class).annotatedWith(AsyncPreferred.class).to(ScanHash.class);

        bind(Statistics.class).asEagerSingleton();
    }
}
