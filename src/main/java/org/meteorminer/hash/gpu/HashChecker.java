package org.meteorminer.hash.gpu;

import com.google.inject.ImplementedBy;
import org.meteorminer.domain.Work;
import org.meteorminer.queue.WorkFoundCallback;

import java.nio.IntBuffer;

/**
 * @author John Ericksen
 */
@ImplementedBy(AsynchronousHashChecker.class)
public interface HashChecker {

    void check(IntBuffer output, Work work, WorkFoundCallback workFoundCallback);

}
