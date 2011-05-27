package org.meteorminer.service;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.nativelibs4java.opencl.CLDevice;
import com.nativelibs4java.opencl.CLPlatform;
import com.nativelibs4java.opencl.JavaCL;
import org.meteorminer.config.MeteorMinerInjector;
import org.meteorminer.config.binding.CPUCount;
import org.meteorminer.domain.Device;
import org.meteorminer.output.CLInterface;
import org.meteorminer.output.LoggingTimerTask;
import org.meteorminer.output.Statistics;
import org.meteorminer.output.StatisticsHolder;

import java.util.*;

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
    private AsynchronousFactory asyncFactory;
    @Inject
    @CPUCount
    private int cpuCount;

    public void start() {
        timer.schedule(loggingTimerTask, 5000, 1000);
        //warm up period
        timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        statisticsHolder.reset();
                    }
                }, 2000);

        //gpu setup
        for (CLDevice gpuDevice : getAllDevices()) {
            Injector gpuDeviceInjector = MeteorMinerInjector.buildGPUDeviceInjector(gpuDevice);

            setupDevice(gpuDeviceInjector);
        }

        //cpu setup
        for (int i = 0; i < cpuCount; i++) {
            Injector cpuDeviceInjector = MeteorMinerInjector.buildCPUDeviceInjector(i);

            setupDevice(cpuDeviceInjector);
        }


    }

    private void setupDevice(Injector deviceInjector) {
        DeviceManager deviceManager = deviceInjector.getInstance(DeviceManager.class);
        Statistics statistics = deviceInjector.getInstance(Statistics.class);
        Device device = deviceInjector.getInstance(Device.class);

        statisticsHolder.getStatistics().put(device, statistics);

        asyncFactory.startRunnable(deviceManager);
    }

    public List<CLDevice> getAllDevices() {

        List<CLDevice> allDevices = new ArrayList<CLDevice>();

        for (CLPlatform platform : JavaCL.listPlatforms()) {
            allDevices.addAll(Arrays.asList(platform.listAllDevices(true)));
        }

        return allDevices;
    }
}
