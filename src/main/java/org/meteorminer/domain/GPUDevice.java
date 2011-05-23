package org.meteorminer.domain;

import com.nativelibs4java.opencl.CLDevice;

/**
 * @author John Ericksen
 */
public class GPUDevice implements Device {

    private CLDevice device;

    public GPUDevice(CLDevice device) {
        this.device = device;
    }

    @Override
    public String getName() {
        return device.getName();
    }
}
