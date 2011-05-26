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
    private long lastWorkUpdate;
    private long previousHashCount;
    private double instantHashRate;
    private AtomicLong workPass;
    private AtomicLong workFail;
    private AtomicLong workTime;
    private AtomicLong savedTime;

    public Statistics() {
        reset();
    }

    public void reset() {
        hashCount = new AtomicLong();
        instantHashCount = new AtomicLong();
        workPass = new AtomicLong();
        workFail = new AtomicLong();
        workTime = new AtomicLong();
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

    public void incrementSavedTime(long increment) {
        savedTime.addAndGet(increment);
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


    public double getLongHashRate() {
        return (hashCount.get() / 1000.0) / (System.currentTimeMillis() - startHashTime);
    }

    public void addWorkTime(long incremenet) {
        workTime.addAndGet(incremenet);
        lastWorkUpdate = System.currentTimeMillis();
    }

    public long getWorkTime() {
        return workTime.get();
    }

    public double getWorkRatio() {
        return (getWorkTime() * 100.0) / (lastWorkUpdate - startHashTime);
    }

    public long getSavedTime() {
        return savedTime.get();
    }

    public String toString() {
        return new Formatter().format("%1.2f(%1.2f)mh/s %1d pass %1d fail %1.4f",
                getInstantHashRate(), getLongHashRate(), getWorkPassed(), getWorkFailed(), getWorkRatio()).toString();
    }

    public void updateInstants() {
        long currentHashCount = instantHashCount.get();
        long currentHashTime = System.currentTimeMillis();
        instantHashRate = ((currentHashCount - previousHashCount) / 1000.0) / (currentHashTime - previousHashTime);
        previousHashCount = currentHashCount;
        previousHashTime = currentHashTime;
    }
}
