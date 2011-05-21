package org.meteorminer.hash.gpu;

import com.nativelibs4java.opencl.CLEvent;
import com.nativelibs4java.opencl.CLIntBuffer;
import com.nativelibs4java.opencl.CLMem;
import com.nativelibs4java.util.NIOUtils;
import org.meteorminer.domain.Work;

import java.nio.IntBuffer;

/**
 * @author John Ericksen
 */
public class DiabloMiner {

    int fW0;
    int fW1;
    int fW2;
    int fW3;
    int fW15;
    int fW01r;
    int fcty_e;
    int fcty_e2;
    final int[] midstate2 = new int[8];
    CLIntBuffer outputBuffer;
    IntBuffer output;

    public DiabloMiner(OCL ocl, int worksize, Work work, int size) {

        output = NIOUtils.directInts(size, ocl.getContext().getByteOrder());

        createBuffer(ocl);

        System.arraycopy(work.getMidstate(), 0, midstate2, 0, 8);

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

    public void finish() {
        outputBuffer.release();
    }

    public void createBuffer(OCL ocl) {
        if(outputBuffer != null){
            outputBuffer.release();
        }

        int[] input = new int[0xF];

        for(int i = 0; i < 0xF; i++){
            input[i] = 0;
        }
        outputBuffer = ocl.getContext().createIntBuffer(CLMem.Usage.InputOutput, IntBuffer.wrap(input), true);
    }

    public IntBuffer hash(Work work, OCL ocl, int nonceStart, int worksize, int localWorkSize) {


        ocl.getKernel().setArgs(fW0, fW1, fW2, fW3, fW15, fW01r, fcty_e, fcty_e2,
                work.getMidstate()[0],
                work.getMidstate()[1],
                work.getMidstate()[2],
                work.getMidstate()[3],
                work.getMidstate()[4],
                work.getMidstate()[5],
                work.getMidstate()[6],
                work.getMidstate()[7],
                midstate2[1],
                midstate2[2],
                midstate2[3],
                midstate2[5],
                midstate2[6],
                midstate2[7],
                nonceStart * worksize,
                outputBuffer);

        synchronized (ocl.getKernel()) {
            CLEvent event = ocl.getKernel().enqueueNDRange(ocl.getQueue(), new int[]{worksize}, new int[]{localWorkSize});
            outputBuffer.read(ocl.getQueue(), 0, 0xF, output, true, event);
        }

        return output;

    }

    static int rot(int x, int y) {
        return (x >>> y) | (x << (32 - y));
    }

    static void sharound(int out[], int na, int nb, int nc, int nd, int ne, int nf, int ng, int nh, int x, int K) {
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
