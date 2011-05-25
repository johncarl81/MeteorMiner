package org.meteorminer.domain;

import org.meteorminer.config.MeteorMinerRuntimeException;

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

        for (int i = 0; i < this.data.length; i++) {
            String parse = data.substring(i * 8, (i * 8) + 8);
            this.data[i] = Integer.reverseBytes((int) Long.parseLong(parse, 16));
        }

        if (midstate.length() != 64) {
            throw new MeteorMinerRuntimeException("Input Midstate in wrong format, needs to be of length 64 chars");
        }

        for (int i = 0; i < this.midstate.length; i++) {
            String parse = midstate.substring(i * 8, (i * 8) + 8);
            this.midstate[i] = Integer.reverseBytes((int) Long.parseLong(parse, 16));
        }

        if (target.length() != 64) {
            throw new MeteorMinerRuntimeException("Input Target in wrong format, needs to be of length 64 chars");
        }

        for (int i = 0; i < this.target.length; i++) {
            String parse = target.substring(i * 8, (i * 8) + 8);
            this.target[i] = (Long.reverseBytes(Long.parseLong(parse, 16) << 16)) >>> 16;
        }
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
