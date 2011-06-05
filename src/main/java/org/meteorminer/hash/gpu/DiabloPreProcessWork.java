package org.meteorminer.hash.gpu;

import org.meteorminer.domain.Work;
import org.meteorminer.hash.PreProcessWork;

import java.util.Arrays;

/**
 * @author John Ericksen
 */
public class DiabloPreProcessWork implements PreProcessWork {

    private int[] midstate2;
    private int fW0;
    private int fW1;
    private int fW2;
    private int fW3;
    private int fW15;
    private int fW01r;
    private int fcty_e;
    private int fcty_e2;

    public DiabloPreProcessWork(Work work) {
        midstate2 = Arrays.copyOf(work.getMidstate(), 8);

        sharound(midstate2, 0, 1, 2, 3, 4, 5, 6, 7, work.getData()[16], 0x428A2F98);
        sharound(midstate2, 7, 0, 1, 2, 3, 4, 5, 6, work.getData()[17], 0x71374491);
        sharound(midstate2, 6, 7, 0, 1, 2, 3, 4, 5, work.getData()[18], 0xB5C0FBCF);

        fW0 = work.getData()[16] + (rot(work.getData()[17], 7) ^ rot(work.getData()[17], 18) ^
                (work.getData()[17] >>> 3));
        fW1 = work.getData()[17] + (rot(work.getData()[18], 7) ^ rot(work.getData()[18], 18) ^
                (work.getData()[18] >>> 3)) + 0x01100000;
        fW2 = work.getData()[18] + (rot(fW0, 17) ^ rot(fW0, 19) ^ (fW0 >>> 10));
        fW3 = 0x11002000 + (rot(fW1, 17) ^ rot(fW1, 19) ^ (fW1 >>> 10));
        fW15 = 0x00000280 + (rot(fW0, 7) ^ rot(fW0, 18) ^ (fW0 >>> 3));
        fW01r = fW0 + (rot(fW1, 7) ^ rot(fW1, 18) ^ (fW1 >>> 3));

        fcty_e = work.getMidstate()[4] + (rot(midstate2[1], 6) ^ rot(midstate2[1], 11) ^ rot(midstate2[1], 25)) +
                (midstate2[3] ^ (midstate2[1] & (midstate2[2] ^ midstate2[3]))) + 0xe9b5dba5;
        fcty_e2 = (rot(midstate2[5], 2) ^ rot(midstate2[5], 13) ^ rot(midstate2[5], 22)) + ((midstate2[5] & midstate2[6]) |
                (midstate2[7] & (midstate2[5] | midstate2[6])));
    }

    public int[] getMidstate2() {
        return midstate2;
    }

    public int getfW0() {
        return fW0;
    }

    public int getfW1() {
        return fW1;
    }

    public int getfW2() {
        return fW2;
    }

    public int getfW3() {
        return fW3;
    }

    public int getfW15() {
        return fW15;
    }

    public int getfW01r() {
        return fW01r;
    }

    public int getFcty_e() {
        return fcty_e;
    }

    public int getFcty_e2() {
        return fcty_e2;
    }

    private static int rot(int x, int y) {
        return (x >>> y) | (x << (32 - y));
    }

    private static void sharound(int out[], int na, int nb, int nc, int nd, int ne, int nf, int ng, int nh, int x, int K) {
        int a = out[na];
        int b = out[nb];
        int c = out[nc];
        int d = out[nd];
        int e = out[ne];
        int f = out[nf];
        int g = out[ng];
        int h = out[nh];

        int t1 = h + (rot(e, 6) ^ rot(e, 11) ^ rot(e, 25)) + ((e & f) ^ ((~e) & g)) + K + x;
        int t2 = (rot(a, 2) ^ rot(a, 13) ^ rot(a, 22)) + ((a & b) ^ (a & c) ^ (b & c));

        out[nd] = d + t1;
        out[nh] = t1 + t2;
    }
}
