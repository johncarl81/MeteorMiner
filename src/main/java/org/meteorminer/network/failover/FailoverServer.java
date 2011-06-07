package org.meteorminer.network.failover;

import org.meteorminer.output.CLInterface;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author John Ericksen
 */
public class FailoverServer {

    //{"host":"server.tld","port":8332,"ttr":0}

    private String host;
    private int port;
    private int timeToReturn;
    private CLInterface output;

    public FailoverServer(String host, int port, int timeToReturn, CLInterface output) {
        this.host = host;
        this.port = port;
        this.timeToReturn = timeToReturn;
        this.output = output;
    }

    public int getTimeToReturn() {
        return timeToReturn;
    }

    public URL getUrl() {
        try {
            return new URL("http://" + host + ":" + port);
        } catch (MalformedURLException e) {
            output.error(e);
            return null;
        }
    }

    @Override
    public String toString() {
        return '{' + host + ':' + port + ", ttr=" + timeToReturn + '}';
    }
}
