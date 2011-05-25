package org.meteorminer.service;

/**
 * Assisted Injection WorkConsumer Factory
 *
 * @author John Ericksen
 */
public interface WorkConsumerFactory {

    public Miner createWorkConsumer();
}
