package org.meteorminer.logging;

import javax.inject.Singleton;

/**
 * @author John Ericksen
 */
@Singleton
public class Statistics {

    private double hashCount;
    private double previousHashTime;
    private long workPass;
    private long workFail;

    public Statistics() {
        hashCount = 0;
        workPass = 0;
        previousHashTime = System.currentTimeMillis();
    }

    public void incrementHashCount(long increment) {
        hashCount += increment;
    }

    public void incrementWorkPass(long increment) {
        workPass += increment;
    }

    public void incrementWorkFail(long increment) {
        workFail += increment;
    }

    public double getHashRate() {
        long currentHashTime = System.currentTimeMillis();
        double rate = (hashCount / 1000.0) / (currentHashTime - previousHashTime);
        previousHashTime = currentHashTime;
        hashCount = 0;

        return rate;
    }

    public long getWorkPassed() {
        return workPass;
    }

    public long getWorkFailed() {
        return workFail;
    }


}
