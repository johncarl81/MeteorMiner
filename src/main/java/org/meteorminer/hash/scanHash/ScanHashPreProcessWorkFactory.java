package org.meteorminer.hash.scanHash;

import org.meteorminer.domain.Work;
import org.meteorminer.hash.PreProcessWork;
import org.meteorminer.hash.PreProcessWorkFactory;

/**
 * @author John Ericksen
 */
public class ScanHashPreProcessWorkFactory implements PreProcessWorkFactory {

    public static final String PRE_PROCESS_NAME = "ScanHash";

    @Override
    public PreProcessWork buildPreProcessWork(Work work) {
        return new ScanHashPreProcessWork(work);
    }

    @Override
    public String getPreProcessWorkName() {
        return PRE_PROCESS_NAME;
    }
}
