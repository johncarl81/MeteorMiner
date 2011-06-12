package org.meteorminer.hash.gpu;

import org.meteorminer.domain.Work;
import org.meteorminer.hash.PreProcessWork;
import org.meteorminer.hash.PreProcessWorkFactory;

/**
 * Factory to create the GPU Preprocess work.
 *
 * @author John Ericksen
 */
public class GPUPreProcessWorkFactory implements PreProcessWorkFactory {

    public static final String PRE_PROCESS_NAME = "GPUCore";

    @Override
    public PreProcessWork buildPreProcessWork(Work work) {
        return new GPUPreProcessWork(work);
    }

    @Override
    public String getPreProcessWorkName() {
        return PRE_PROCESS_NAME;
    }
}
