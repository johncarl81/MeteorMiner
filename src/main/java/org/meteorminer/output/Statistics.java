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

    public Statistics() {
        reset();
    }

    //copy constructor
    public Statistics(long startTime) {
        this();
        this.startHashTime = startTime;
    }

    public void reset() {
        hashCount = new AtomicLong();
        instantHashCount = new AtomicLong();
        workPass = new AtomicLong();
        workFail = new AtomicLong();
        previousHashTime = System.currentTimeMillis();
        startHashTime = System.currentTimeMillis();
    }

    public void add(Statistics stats) {
        incrementHashCount(stats.getHashCount());
        incrementWorkFail(stats.getWorkFailed());
        incrementWorkPass(stats.getWorkPassed());
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

    public String toString() {
        return new Formatter().format("%1.2f(%1.2f)mh/s",
                getHashRate(), getInstantHashRate(), getWorkPassed(), getWorkFailed()).toString();
    }

    public void updateInstants() {
        long currentHashCount = instantHashCount.get();
        long currentHashTime = System.currentTimeMillis();
        instantHashRate = ((currentHashCount - previousHashCount) / 1000.0) / (currentHashTime - previousHashTime);
        previousHashCount = currentHashCount;
        previousHashTime = currentHashTime;
    }


    public long getStartTime() {
        return startHashTime;
    }

    public long getHashCount() {
        return hashCount.get();
    }
}
