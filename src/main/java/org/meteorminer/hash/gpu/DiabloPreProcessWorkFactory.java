package org.meteorminer.hash.gpu;

import org.meteorminer.domain.Work;
import org.meteorminer.hash.PreProcessWork;
import org.meteorminer.hash.PreProcessWorkFactory;

/**
 * @author John Ericksen
 */
public class DiabloPreProcessWorkFactory implements PreProcessWorkFactory {

    public static final String PRE_PROCESS_NAME = "Diablo";

    @Override
    public PreProcessWork buildPreProcessWork(Work work) {
        return new DiabloPreProcessWork(work);
    }

    @Override
    public String getPreProcessWorkName() {
        return PRE_PROCESS_NAME;
    }
}
