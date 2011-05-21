package org.meteorminer.domain;

/**
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

        this.midstateString = midstate;
        this.targetString = target;

        setData(data);


        for (int i = 0; i < this.midstate.length; i++) {
            String parse = midstate.substring(i * 8, (i * 8) + 8);
            this.midstate[i] = Integer.reverseBytes((int) Long.parseLong(parse, 16));
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

    public String getTargetString() {
        return targetString;
    }

    @Override
    public String toString() {
        return "Work{" +
                "midstate='" + midstate + '\'' +
                ", data='" + data + '\'' +
                ", hash1='" + hash1 + '\'' +
                ", target='" + target + '\'' +
                '}';
    }

    public void setData(String data) {

        this.dataString = data;

        for (int i = 0; i < this.data.length; i++) {
            String parse = data.substring(i * 8, (i * 8) + 8);
            this.data[i] = Integer.reverseBytes((int) Long.parseLong(parse, 16));
        }
    }
}
