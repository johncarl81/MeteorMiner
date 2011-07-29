package org.meteorminer.config.advice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class MeteorAdvice {

    private List<ServerAdvice> servers = new ArrayList<ServerAdvice>();
    private Set<CPUDeviceAdvice> cpuDevices = new HashSet<CPUDeviceAdvice>();
    //private int cpuCount;
    private Set<GPUDeviceAdvice> gpuDevices = new HashSet<GPUDeviceAdvice>();
    //private List<Integer> gpuIds = new ArrayList<Integer>();

    //mining strategy
    private boolean tandem;

    //general mining parameters
    private Long networkErrorPause = 5000L;
    private boolean verbose;

    public List<ServerAdvice> getServers() {
        return servers;
    }

    public Set<CPUDeviceAdvice> getCpuDevices() {
        return cpuDevices;
    }

    public Set<GPUDeviceAdvice> getGpuDevices() {
        return gpuDevices;
    }

    public boolean isTandem() {
        return tandem;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public void setTandem(boolean tandem) {
        this.tandem = tandem;
    }

    public Long getNetworkErrorPause() {
        return networkErrorPause;
    }

    public void setNetworkErrorPause(Long networkErrorPause) {
        this.networkErrorPause = networkErrorPause;
    }
}
