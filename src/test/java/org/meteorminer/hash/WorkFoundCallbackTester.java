package org.meteorminer.hash;

import org.meteorminer.domain.Work;
import org.meteorminer.service.WorkFoundCallback;

import static junit.framework.Assert.assertEquals;

/**
 * @author John Ericksen
 */
public class WorkFoundCallbackTester implements WorkFoundCallback {

    private boolean found = false;
    private int expectedNonce;

    public void found(Work work, int nonce) {
        found = true;
        assertEquals(expectedNonce, nonce);
    }

    public boolean isFound() {
        return found;
    }

    public void setExpectedNonce(int expectedNonce) {
        this.expectedNonce = expectedNonce;
    }
}
