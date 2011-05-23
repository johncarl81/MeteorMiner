package org.meteorminer.output;

import javax.inject.Singleton;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author John Ericksen
 */
@Singleton
public class Statistics {

    private AtomicLong hashCount;
    private AtomicLong instantHashCount;
    private long previousHashTime;
    private long startHashTime;
    private long lastWorkUpdate;
    private AtomicLong workPass;
    private AtomicLong workFail;
    private AtomicLong workTime;
    private AtomicLong savedTime;

    public Statistics() {
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
        hashCount.addAndGet(increment);
        instantHashCount.addAndGet(increment);
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
        long tempHashCount = instantHashCount.getAndSet(0);
        long currentHashTime = System.currentTimeMillis();
        double rate = (tempHashCount / 1000.0) / (currentHashTime - previousHashTime);
        previousHashTime = currentHashTime;

        return rate;
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
        return (getWorkTime() * 1.0) / (lastWorkUpdate - startHashTime);
    }

    public long getSavedTime() {
        return savedTime.get();
    }
}
