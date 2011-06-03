package org.meteorminer.service;

import com.google.inject.Injector;
import com.nativelibs4java.opencl.CLDevice;
import com.nativelibs4java.opencl.CLPlatform;
import com.nativelibs4java.opencl.JavaCL;
import org.meteorminer.config.MeteorMinerInjector;
import org.meteorminer.config.binding.CPUCount;
import org.meteorminer.config.binding.GPUIds;
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
    @CPUCount
    private int cpuCount;
    @Inject
    @GPUIds
    private List<Integer> activatedGpus;
    @Inject
    private MinerStrategy minerStrategy;

    public void start() {
        List<CLDevice> gpuDevices = getAllDevices();

        statisticsHolder.reset();

        //output gpus found
        System.out.println("GPU devices found:");
        System.out.println("Id\tName");
        for (int i = 0; i < gpuDevices.size(); i++) {
            System.out.println(i + ":\t" + gpuDevices.get(i));
        }

        timer.schedule(loggingTimerTask, 5000, 1000);

        //gpu setup
        for (Integer id : activatedGpus) {
            if (id < gpuDevices.size()) {
                Injector gpuDeviceInjector = MeteorMinerInjector.getGPUDeviceInjector(gpuDevices.get(id), id);
                setupDevice(gpuDeviceInjector);
            } else {
                output.notification("GPU Device not found: " + id);
            }
        }

        //cpu setup
        for (int i = 0; i < cpuCount; i++) {
            Injector cpuDeviceInjector = MeteorMinerInjector.getCPUDeviceInjector(i);

            setupDevice(cpuDeviceInjector);
        }

        minerStrategy.start();
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
