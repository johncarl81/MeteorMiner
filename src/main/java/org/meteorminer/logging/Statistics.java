package org.meteorminer.logging;

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
    private AtomicLong workPass;
    private AtomicLong workFail;

    public Statistics() {
        hashCount = new AtomicLong();
        instantHashCount = new AtomicLong();
        workPass = new AtomicLong();
        workFail = new AtomicLong();
        previousHashTime = System.currentTimeMillis();
        startHashTime = System.currentTimeMillis();
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

    public double getInstantHashRate() {
        long currentHashTime = System.currentTimeMillis();
        double rate = (instantHashCount.get() / 1000.0) / (currentHashTime - previousHashTime);
        previousHashTime = currentHashTime;
        instantHashCount.set(0);

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
}
