package org.meteorminer.service;

import org.meteorminer.config.MeteorMinerInjector;
import org.meteorminer.hash.HashScanner;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

/**
 * @author John Ericksen
 */
public abstract class AbstractMinerStrategy implements MinerStrategy {

    @Inject
    private AsynchronousFactory asynchronousFactory;

    private Set<HashScanner> scanners = new HashSet<HashScanner>();
    private MinerFactory minerFactory;

    public AbstractMinerStrategy() {
        minerFactory = MeteorMinerInjector.getMinerInjector().getInstance(MinerFactory.class);
    }

    @Override
    public void add(HashScanner hashScanner) {
        scanners.add(hashScanner);
    }

    public AsynchronousFactory getAsynchronousFactory() {
        return asynchronousFactory;
    }

    public void setAsynchronousFactory(AsynchronousFactory asynchronousFactory) {
        this.asynchronousFactory = asynchronousFactory;
    }

    public MinerFactory getMinerFactory() {
        return minerFactory;
    }

    public Set<HashScanner> getScanners() {
        return scanners;
    }

    public void setMinerFactory(MinerFactory minerFactory) {
        this.minerFactory = minerFactory;
    }
}
