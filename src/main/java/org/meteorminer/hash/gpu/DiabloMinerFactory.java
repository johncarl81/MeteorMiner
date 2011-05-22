package org.meteorminer.hash.gpu;

import org.meteorminer.domain.Work;

/**
 * Assisted Injection DiabloMiner Factory
 *
 * @author John Ericksen
 */
public interface DiabloMinerFactory {

    DiabloMiner createDiabloMiner(Work work);

}
