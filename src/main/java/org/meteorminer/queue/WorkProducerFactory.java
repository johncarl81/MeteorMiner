package org.meteorminer.queue;

import com.google.inject.ImplementedBy;

/**
 * @author John Ericksen
 */
@ImplementedBy(WorkProducerImpl.class)
public interface WorkProducerFactory {

    WorkProducer createWorkProducer();
}
