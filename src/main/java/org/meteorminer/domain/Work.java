package org.meteorminer.domain;

import org.meteorminer.config.MeteorMinerRuntimeException;

import static org.meteorminer.hash.HexUtil.decode;

/**
 * Contains the data provided from the bitcoin server to be processed.=
 *
 * @author John Ericksen
 */
public class Work {

    private String hash1;
    private String dataString;
    private String midstateString;
    private String targetString;
    private final int[] data = new int[32];
    private final int[] midstate = new int[8];
    private final long[] target = new long[8];

    public Work(String data, String midstate, String hash1, String target) {
        this.hash1 = hash1;

        this.dataString = data;
        this.midstateString = midstate;
        this.targetString = target;

        if (data.length() != 256) {
            throw new MeteorMinerRuntimeException("Input Data in wrong format, needs to be of length 256 chars");
        }

        decode(this.data, data);

        if (midstate.length() != 64) {
            throw new MeteorMinerRuntimeException("Input Midstate in wrong format, needs to be of length 64 chars");
        }

        decode(this.midstate, midstate);

        if (target.length() != 64) {
            throw new MeteorMinerRuntimeException("Input Target in wrong format, needs to be of length 64 chars");
        }

        decode(this.target, target);
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
}
