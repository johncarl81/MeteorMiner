package org.meteorminer.hash.gpu;

import com.google.inject.Inject;
import org.meteorminer.binding.Preferred;
import org.meteorminer.domain.Work;
import org.meteorminer.queue.WorkFoundCallback;

import java.nio.IntBuffer;

/**
 * @author John Ericksen
 */
public class AsynchronousHashChecker implements HashChecker {

    @Inject
    @Preferred
    private HashChecker delegate;

    @Override
    public void check(final IntBuffer output, final Work work, final WorkFoundCallback workFoundCallback) {
        new Thread() {
            public void run() {
                delegate.check(output, work, workFoundCallback);
            }
        }.start();
    }
}
