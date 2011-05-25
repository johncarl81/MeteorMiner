package org.meteorminer.domain;

/**
 * @author John Ericksen
 */
public class CPUDevice implements Device {

    private int number;

    public CPUDevice(int number) {
        this.number = number;
    }

    @Override
    public String getName() {
        return "CPU" + number;
    }
}
