package org.meteorminer.service;

import com.google.inject.Injector;
import com.nativelibs4java.opencl.CLDevice;
import com.nativelibs4java.opencl.CLPlatform;
import com.nativelibs4java.opencl.JavaCL;
import org.meteorminer.config.MeteorMinerInjector;
import org.meteorminer.config.advice.CPUDeviceAdvice;
import org.meteorminer.config.advice.GPUDeviceAdvice;
import org.meteorminer.config.advice.MeteorAdvice;
import org.meteorminer.domain.Device;
import org.meteorminer.hash.HashScanner;
import org.meteorminer.output.CLInterface;
import org.meteorminer.output.LoggingTimerTask;
import org.meteorminer.output.Statistics;
import org.meteorminer.output.StatisticsHolder;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

/**
 * @author John Ericksen
 */
public class MiningService {

    @Inject
    private LoggingTimerTask loggingTimerTask;
    @Inject
    private Timer timer;
    @Inject
    private CLInterface output;
    @Inject
    private StatisticsHolder statisticsHolder;
    @Inject
    private MinerStrategy minerStrategy;
    @Inject
    private MeteorAdvice advice;
    @Inject
    private StatisticsDelayTimerTask statisticsDelayTimerTask;

    public void start() {

        if (advice.getGpuDevices().size() > 0) {
            List<CLDevice> gpuDevices = getAllDevices();

            //output gpus found
            output.notification("GPU devices found:");
            output.notification("Id\tName");
            for (int i = 0; i < gpuDevices.size(); i++) {
                output.notification(i + ":\t" + gpuDevices.get(i));
            }

            //gpu setup
            for (GPUDeviceAdvice gpuAdvice : advice.getGpuDevices()) {
                if (gpuAdvice.getId() < gpuDevices.size()) {
                    Injector gpuDeviceInjector = MeteorMinerInjector.getGPUDeviceInjector(gpuDevices.get(gpuAdvice.getId()), gpuAdvice);
                    setupDevice(gpuDeviceInjector);
                } else {
                    output.notification("GPU Device not found: " + gpuAdvice.getId());
                }
            }
        }

        if (advice.getCpuDevices().size() > 0) {
            //cpu setup
            for (CPUDeviceAdvice cpuAdvice : advice.getCpuDevices()) {
                Injector cpuDeviceInjector = MeteorMinerInjector.getCPUDeviceInjector(cpuAdvice);

                setupDevice(cpuDeviceInjector);
            }
        }

        minerStrategy.start();
        timer.schedule(statisticsDelayTimerTask, 3000);
        timer.schedule(loggingTimerTask, 6000, 1000);
    }

    private void setupDevice(Injector deviceInjector) {
        HashScanner hashScanner = deviceInjector.getInstance(HashScanner.class);
        Statistics statistics = deviceInjector.getInstance(Statistics.class);
        Device device = deviceInjector.getInstance(Device.class);

        statisticsHolder.getStatistics().put(device, statistics);

        minerStrategy.add(hashScanner);
    }

    public List<CLDevice> getAllDevices() {

        List<CLDevice> allDevices = new ArrayList<CLDevice>();

        for (CLPlatform platform : JavaCL.listPlatforms()) {
            allDevices.addAll(Arrays.asList(platform.listAllDevices(true)));
        }

        return allDevices;
    }
}
