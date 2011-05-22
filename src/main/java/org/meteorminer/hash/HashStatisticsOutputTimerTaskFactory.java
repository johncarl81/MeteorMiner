package org.meteorminer.hash;

/**
 * @author John Ericksen
 */
public interface HashStatisticsOutputTimerTaskFactory {

    HashStatisticsOutputTimerTask buildStatisticsOutputTimerTask(HashScanner abstractHashScanner);
}
