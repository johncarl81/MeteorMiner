package org.meteorminer.output;

import org.apache.commons.pool.ObjectPool;
import org.meteorminer.config.binding.CLIntBufferPool;
import org.meteorminer.config.binding.ResultPool;
import org.meteorminer.config.binding.RunnableHashCheckerPool;
import org.meteorminer.config.binding.Verbose;

import javax.inject.Inject;
import java.util.Formatter;

/**
 * @author John Ericksen
 */
public class GPUStatistics extends Statistics {
    @Inject
    @RunnableHashCheckerPool
    private ObjectPool intBufferPool;
    @Inject
    @CLIntBufferPool
    private ObjectPool clIntBufferPool;
    @Inject
    @ResultPool
    private ObjectPool resultPool;
    @Inject
    @Verbose
    private boolean verbose;

    @Override
    public String toString() {
        String statsOutput = super.toString();
        if (verbose) {
            return statsOutput + new Formatter().format("|buff:%1d %1d %1d %1d %1d %1d",
                    intBufferPool.getNumActive(), intBufferPool.getNumIdle(), clIntBufferPool.getNumActive(), clIntBufferPool.getNumIdle(), resultPool.getNumActive(), resultPool.getNumIdle()).toString();
        } else {
            return statsOutput;
        }
    }
}
