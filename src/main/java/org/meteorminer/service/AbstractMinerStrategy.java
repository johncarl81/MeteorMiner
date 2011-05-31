package org.meteorminer.service;

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
    @Inject
    private MinerFactory minerFactory;

    private Set<HashScanner> scanners = new HashSet<HashScanner>();


    @Override
    public void add(HashScanner hashScanner) {
        scanners.add(hashScanner);
    }

    public AsynchronousFactory getAsynchronousFactory() {
        return asynchronousFactory;
    }

    public MinerFactory getMinerFactory() {
        return minerFactory;
    }

    public Set<HashScanner> getScanners() {
        return scanners;
    }
}
