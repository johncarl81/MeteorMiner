package org.meteorminer.hash;

/**
 * @author John Ericksen
 */
public interface WorkFoundCallbackTesterFactory {

    WorkFoundCallbackTester buildCallback(int expectedNonce);
}
