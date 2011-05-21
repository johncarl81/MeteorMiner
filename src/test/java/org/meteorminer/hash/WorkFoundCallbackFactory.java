package org.meteorminer.hash;

/**
 * @author John Ericksen
 */
public interface WorkFoundCallbackFactory {

    WorkFoundCallbackTester buildCallback(int expectedNonce, boolean shortCircuit);
}
