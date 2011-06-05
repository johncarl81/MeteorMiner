package org.meteorminer.hash.scanHash;

import org.meteorminer.domain.Work;
import org.meteorminer.hash.PreProcessWork;

import static org.meteorminer.hash.HexUtil.decode;

/**
 * @author John Ericksen
 */
public class ScanHashPreProcessWork implements PreProcessWork {

    private int[] data;
    private int[] midstate;

    public ScanHashPreProcessWork(Work work) {
        data = decode(new int[16], work.getDataString().substring(128));
        midstate = decode(decode(new int[16], work.getHash1()), work.getMidstateString());
    }

    public int[] getData() {
        return data;
    }

    public int[] getMidstate() {
        return midstate;
    }
}
