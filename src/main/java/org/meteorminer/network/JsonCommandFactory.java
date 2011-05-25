package org.meteorminer.network;

import org.meteorminer.domain.Work;

/**
 * @author John Ericksen
 */
public interface JsonCommandFactory {

    WorkSubmit buildWorkSubmit(Work work, int nonce);
}
