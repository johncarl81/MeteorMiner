package org.meteorminer.hash;

import com.google.inject.assistedinject.Assisted;
import org.meteorminer.domain.Work;
import org.meteorminer.service.WorkFoundCallback;

import javax.inject.Inject;

import static junit.framework.Assert.assertEquals;

/**
 * @author John Ericksen
 */
public class WorkFoundCallbackTester implements WorkFoundCallback {

    private boolean found = false;
    private int expectedNonce;

    @Inject
    public WorkFoundCallbackTester(@Assisted int expectedNonce) {
        this.expectedNonce = expectedNonce;
    }

    public void found(Work work, int nonce) {
        found = true;
        assertEquals(expectedNonce, nonce);
    }

    public boolean isFound() {
        return found;
    }
}
