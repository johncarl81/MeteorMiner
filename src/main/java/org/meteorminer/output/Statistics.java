package org.meteorminer.output;

import java.util.Formatter;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author John Ericksen
 */
public class Statistics {

    private AtomicLong hashCount;
    private AtomicLong instantHashCount;
    private long previousHashTime;
    private long startHashTime;
    private long previousHashCount;
    private double instantHashRate;
    private AtomicLong workPass;
    private AtomicLong workFail;
    private AtomicLong savedTime;

    public Statistics() {
        reset();
    }

    public void reset() {
        hashCount = new AtomicLong();
        instantHashCount = new AtomicLong();
        workPass = new AtomicLong();
        workFail = new AtomicLong();
        previousHashTime = System.currentTimeMillis();
        startHashTime = System.currentTimeMillis();
        savedTime = new AtomicLong();
    }

    public void incrementHashCount(long increment) {
        hashCount.getAndAdd(increment);
        instantHashCount.getAndAdd(increment);
    }

    public void incrementWorkPass(long increment) {
        workPass.addAndGet(increment);
    }

    public void incrementWorkFail(long increment) {
        workFail.addAndGet(increment);
    }

    public double getInstantHashRate() {
        return instantHashRate;
    }

    public long getWorkPassed() {
        return workPass.get();
    }

    public long getWorkFailed() {
        return workFail.get();
    }


    public double getHashRate() {
        return (hashCount.get() / 1000.0) / (System.currentTimeMillis() - startHashTime);
    }

    public long getSavedTime() {
        return savedTime.get();
    }

    public String toString() {
        return new Formatter().format("%1.2f(%1.2f)mh/s %1d pass %1d fail",
                getInstantHashRate(), getHashRate(), getWorkPassed(), getWorkFailed()).toString();
    }

    public void updateInstants() {
        long currentHashCount = instantHashCount.get();
        long currentHashTime = System.currentTimeMillis();
        instantHashRate = ((currentHashCount - previousHashCount) / 1000.0) / (currentHashTime - previousHashTime);
        previousHashCount = currentHashCount;
        previousHashTime = currentHashTime;
    }
}
