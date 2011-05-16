package org.meteorminer.stats;

import javax.inject.Singleton;

/**
 * @author John Ericksen
 */
@Singleton
public class Statistics {

    private long hashCount;
    private long previousHashTime;

    public Statistics(){
        hashCount = 0;
        previousHashTime = System.currentTimeMillis();
    }

    public void incrementHashCount(long increment){
        hashCount += increment;
    }

    public double getHashRate(){
        double rate = (1.0 * hashCount) / (System.currentTimeMillis() - previousHashTime);
        previousHashTime = System.currentTimeMillis();
        hashCount = 0;

        return rate;
    }



}
