package org.meteorminer.service;

import com.google.inject.ImplementedBy;
import org.meteorminer.domain.Work;

/**
 * @author John Ericksen
 */
@ImplementedBy(WorkProducerImpl.class)
public interface WorkProducer {

    Work produce();

}
