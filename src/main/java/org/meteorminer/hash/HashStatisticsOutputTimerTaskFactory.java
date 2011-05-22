package org.meteorminer.hash;

/**
 * Assisted Injection HashStatisticsOutputTimerTask Factory
 *
 * @author John Ericksen
 */
public interface HashStatisticsOutputTimerTaskFactory {

    HashStatisticsOutputTimerTask buildStatisticsOutputTimerTask(HashScanner abstractHashScanner);
}
