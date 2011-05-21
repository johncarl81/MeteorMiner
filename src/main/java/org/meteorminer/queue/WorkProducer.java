package org.meteorminer.queue;

import org.meteorminer.domain.Work;

/**
 * @author John Ericksen
 */
public interface WorkProducer {

    Work produce();

}
