package org.meteorminer.hash;

import com.google.inject.assistedinject.Assisted;
import junit.framework.Assert;
import org.meteorminer.domain.Work;
import org.meteorminer.service.WorkFoundCallback;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class WorkFoundCallbackTester implements WorkFoundCallback {

    private boolean found = false;
    private boolean shortCircuit;
    private int expectedNonce;

    @Inject
    public WorkFoundCallbackTester(@Assisted int expectedNonce, @Assisted boolean shortCircuit) {
        this.expectedNonce = expectedNonce;
        this.shortCircuit = shortCircuit;
    }

    public void found(Work work, int nonce) {
        found = true;
        Assert.assertEquals(expectedNonce, nonce);
        if (shortCircuit) {
            throw new ShortCircuitException(); //short circuit
        }
    }

    public boolean isFound() {
        return found;
    }
}
