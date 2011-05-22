package org.meteorminer.logging;

import javax.inject.Singleton;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author John Ericksen
 */
@Singleton
public class Statistics {

    private AtomicLong hashCount;
    private long previousHashTime;
    private AtomicLong workPass;
    private AtomicLong workFail;

    public Statistics() {
        hashCount = new AtomicLong();
        workPass = new AtomicLong();
        workFail = new AtomicLong();
        previousHashTime = System.currentTimeMillis();
    }

    public void incrementHashCount(long increment) {
        hashCount.addAndGet(increment);
    }

    public void incrementWorkPass(long increment) {
        workPass.addAndGet(increment);
    }

    public void incrementWorkFail(long increment) {
        workFail.addAndGet(increment);
    }

    public double getHashRate() {
        long currentHashTime = System.currentTimeMillis();
        double rate = (hashCount.get() / 1000.0) / (currentHashTime - previousHashTime);
        previousHashTime = currentHashTime;
        hashCount.set(0);

        return rate;
    }

    public long getWorkPassed() {
        return workPass.get();
    }

    public long getWorkFailed() {
        return workFail.get();
    }


}
