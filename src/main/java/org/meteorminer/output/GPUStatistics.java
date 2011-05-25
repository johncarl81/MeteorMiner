package org.meteorminer.output;

import org.apache.commons.pool.ObjectPool;
import org.meteorminer.config.binding.CLIntBufferPool;
import org.meteorminer.config.binding.IntBufferPool;
import org.meteorminer.config.binding.Verbose;

import javax.inject.Inject;
import java.util.Formatter;

/**
 * @author John Ericksen
 */
public class GPUStatistics extends Statistics {
    @Inject
    @IntBufferPool
    private ObjectPool intBufferPool;
    @Inject
    @CLIntBufferPool
    private ObjectPool clIntBufferPool;
    @com.google.inject.Inject
    @Verbose
    private boolean verbose;

    @Override
    public String toString() {
        String statsOutput = super.toString();
        if (verbose) {
            return statsOutput + new Formatter().format(" | buffers: %1d %1d %1d %1d %1d",
                    intBufferPool.getNumActive(), intBufferPool.getNumIdle(), clIntBufferPool.getNumActive(), clIntBufferPool.getNumIdle(), getSavedTime()).toString();
        } else {
            return statsOutput;
        }
    }
}
