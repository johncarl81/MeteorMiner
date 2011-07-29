package org.meteorminer.config.advice;

/**
 * @author John Ericksen
 */
public class GPUDeviceAdvice {

    private int id;
    private int intensity;
    private int worksize;
    private int vectors;
    private boolean bfi_int;

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public int getWorksize() {
        return worksize;
    }

    public void setWorksize(int worksize) {
        this.worksize = worksize;
    }

    public int getVectors() {
        return vectors;
    }

    public void setVectors(int vectors) {
        this.vectors = vectors;
    }

    public boolean isBfi_int() {
        return bfi_int;
    }

    public void setBfi_int(boolean bfi_int) {
        this.bfi_int = bfi_int;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
