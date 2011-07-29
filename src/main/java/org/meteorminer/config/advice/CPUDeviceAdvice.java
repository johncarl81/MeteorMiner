package org.meteorminer.config.advice;

/**
 * @author John Ericksen
 */
public class CPUDeviceAdvice {
    private int id;

    public CPUDeviceAdvice() {
        //empty bean constructor
    }

    public CPUDeviceAdvice(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
