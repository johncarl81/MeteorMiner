package org.meteorminer.domain;

import com.nativelibs4java.opencl.CLDevice;

/**
 * @author John Ericksen
 */
public class GPUDevice implements Device {

    private CLDevice device;
    private int id;

    public GPUDevice(CLDevice device, int id) {
        this.device = device;
        this.id = id;
    }

    @Override
    public String getName() {
        return device.getName();
    }

    public CLDevice getCLDevice() {
        return device;
    }

    public int getId() {
        return id;
    }
}
