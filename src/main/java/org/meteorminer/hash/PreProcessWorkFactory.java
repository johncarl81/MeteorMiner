package org.meteorminer.hash;


import org.meteorminer.domain.Work;

/**
 * @author John Ericksen
 */
public interface PreProcessWorkFactory {

    PreProcessWork buildPreProcessWork(Work work);

    String getPreProcessWorkName();
}
