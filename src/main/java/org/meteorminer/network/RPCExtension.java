package org.meteorminer.network;

import java.net.HttpURLConnection;

/**
 * @author John Ericksen
 */
public interface RPCExtension {

    void setup(HttpURLConnection connection);
}
