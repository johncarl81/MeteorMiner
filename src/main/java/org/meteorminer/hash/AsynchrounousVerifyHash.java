package org.meteorminer.hash;

import com.google.inject.Inject;
import org.meteorminer.binding.Preferred;
import org.meteorminer.domain.Work;
import org.meteorminer.queue.WorkFoundCallback;

/**
 * @author John Ericksen
 */
public class AsynchrounousVerifyHash implements VerifyHash {

    @Inject
    @Preferred
    private VerifyHash delegate;

    @Override
    public void verify(final Work work, final int nonce, final WorkFoundCallback callback) {
        new Thread() {
            @Override
            public void run() {
                delegate.verify(work, nonce, callback);
            }
        }.start();
    }
}
