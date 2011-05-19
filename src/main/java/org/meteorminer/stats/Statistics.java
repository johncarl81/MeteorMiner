package org.meteorminer.stats;

import javax.inject.Singleton;

/**
 * @author John Ericksen
 */
@Singleton
public class Statistics {

    private double hashCount;
    private double previousHashTime;

    public Statistics() {
        hashCount = 0;
        previousHashTime = System.currentTimeMillis();
    }

    public void incrementHashCount(long increment) {
        hashCount += increment;
    }

    public double getHashRate() {
        long currentHashTime = System.currentTimeMillis();
        double rate = (1000.0 * hashCount) / (currentHashTime - previousHashTime);
        previousHashTime = currentHashTime;
        hashCount = 0;

        return rate;
    }


}
