package org.meteorminer.domain;

import com.google.gson.annotations.SerializedName;
import org.meteorminer.config.MeteorMinerRuntimeException;
import org.meteorminer.hash.PreProcessWork;

import java.util.HashMap;
import java.util.Map;

import static org.meteorminer.hash.HexUtil.decode;

/**
 * Contains the data provided from the bitcoin server to be processed.
 *
 * @author John Ericksen
 */
public class Work {

    @SerializedName("midstate")
    private String midstateString;
    @SerializedName("data")
    private String dataString;
    @SerializedName("hash1")
    private String hash1;
    @SerializedName("target")
    private String targetString;
    private transient final int[] data = new int[32];
    private transient final int[] midstate = new int[8];
    private transient final long[] target = new long[8];

    private Map<String, PreProcessWork> preProcessedWork = new HashMap<String, PreProcessWork>();

    private long created;
    private boolean stale;

    public Work() {
        this.stale = false;
        this.created = System.currentTimeMillis();
    }

    public Work(String data, String midstate, String hash1, String target) {
        this();
        this.hash1 = hash1;
        this.dataString = data;
        this.midstateString = midstate;
        this.targetString = target;

        processStrings();
    }

    public final void processStrings() {

        if (dataString.length() != 256) {
            throw new MeteorMinerRuntimeException("Input Data in wrong format, needs to be of length 256 chars");
        }

        decode(data, dataString);

        if (midstateString.length() != 64) {
            throw new MeteorMinerRuntimeException("Input Midstate in wrong format, needs to be of length 64 chars");
        }

        decode(midstate, midstateString);

        if (targetString.length() != 64) {
            throw new MeteorMinerRuntimeException("Input Target in wrong format, needs to be of length 64 chars");
        }

        decode(target, targetString);
    }


    public String getHash1() {
        return hash1;
    }

    public int[] getData() {
        return data;
    }

    public int[] getMidstate() {
        return midstate;
    }

    public long[] getTarget() {
        return target;
    }

    public String getDataString() {
        return dataString;
    }

    public String getMidstateString() {
        return midstateString;
    }

    public boolean isStale() {
        return stale;
    }

    public void setStale(boolean stale) {
        this.stale = stale;
    }

    public long getCreated() {
        return created;
    }

    public void updateTime() {
        created = System.currentTimeMillis();
    }

    public Map<String, PreProcessWork> getPreProcessedWork() {
        return preProcessedWork;
    }
}
