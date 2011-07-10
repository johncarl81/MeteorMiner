package org.meteorminer.network.failover;

import com.google.gson.annotations.SerializedName;
import org.meteorminer.output.CLInterface;
import org.meteorminer.service.URLFactory;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author John Ericksen
 */
public class FailoverServer {

    //{"host":"server.tld","port":8332,"ttr":0}
    private String host;
    private int port;
    @SerializedName("ttr")
    private int timeToReturn;
    private transient CLInterface output;
    private transient URLFactory urlFactory;

    public FailoverServer() {
        //empty bean constructor
    }

    public FailoverServer(String host, int port, int timeToReturn, CLInterface output, URLFactory urlFactory) {
        this.host = host;
        this.port = port;
        this.timeToReturn = timeToReturn;
        this.output = output;
        this.urlFactory = urlFactory;
    }

    public int getTimeToReturn() {
        return timeToReturn;
    }

    public URL getUrl() {
        try {
            return urlFactory.buildUrl(host, port);
        } catch (MalformedURLException e) {
            output.error(e);
            return null;
        }
    }

    @Override
    public String toString() {
        return '{' + host + ':' + port + ", ttr=" + getTimeToReturn() + "s}";
    }

    public void setOutput(CLInterface output) {
        this.output = output;
    }

    public void setUrlFactory(URLFactory urlFactory) {
        this.urlFactory = urlFactory;
    }
}
