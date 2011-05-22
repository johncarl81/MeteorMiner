package org.meteorminer.hash.gpu;

import org.meteorminer.domain.Work;

/**
 * @author John Ericksen
 */
public interface DiabloMinerFactory {

    DiabloMiner createDiabloMiner(Work work);

}
