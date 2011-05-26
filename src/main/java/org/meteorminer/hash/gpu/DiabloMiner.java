package org.meteorminer.hash.gpu;

import com.google.inject.assistedinject.Assisted;
import com.nativelibs4java.opencl.CLEvent;
import com.nativelibs4java.opencl.CLIntBuffer;
import org.apache.commons.pool.ObjectPool;
import org.meteorminer.config.binding.CLIntBufferPool;
import org.meteorminer.config.binding.IntBufferPool;
import org.meteorminer.config.binding.SearchKernel;
import org.meteorminer.domain.Work;

import javax.inject.Inject;
import java.nio.IntBuffer;
import java.util.Arrays;

/**
 * Guts of the DiabloMiner, rearranged for use within Meteor Miner.
 * <p/>
 * https://github.com/Diablo-D3/DiabloMiner
 * http://forum.bitcoin.org/?topic=1721.0
 * <p/>
 * Thanks to Diablo-D3
 *
 * @author John Ericksen
 */
public class DiabloMiner {

    private final KernelContext kernelContext;

    private ObjectPool clIntBufferPool;
    private ObjectPool intBufferPool;

    @Inject
    public DiabloMiner(@Assisted Work work,
                       @SearchKernel KernelContext kernelContext,
                       @CLIntBufferPool ObjectPool clIntBufferPool,
                       @IntBufferPool ObjectPool intBufferPool) {

        this.kernelContext = kernelContext;
        this.clIntBufferPool = clIntBufferPool;
        this.intBufferPool = intBufferPool;

        int[] midstate2 = Arrays.copyOf(work.getMidstate(), 8);

        sharound(midstate2, 0, 1, 2, 3, 4, 5, 6, 7, work.getData()[16], 0x428A2F98);
        sharound(midstate2, 7, 0, 1, 2, 3, 4, 5, 6, work.getData()[17], 0x71374491);
        sharound(midstate2, 6, 7, 0, 1, 2, 3, 4, 5, work.getData()[18], 0xB5C0FBCF);

        int fW0 = work.getData()[16] + (rot(work.getData()[17], 7) ^ rot(work.getData()[17], 18) ^
                (work.getData()[17] >>> 3));
        int fW1 = work.getData()[17] + (rot(work.getData()[18], 7) ^ rot(work.getData()[18], 18) ^
                (work.getData()[18] >>> 3)) + 0x01100000;
        int fW2 = work.getData()[18] + (rot(fW0, 17) ^ rot(fW0, 19) ^ (fW0 >>> 10));
        int fW3 = 0x11002000 + (rot(fW1, 17) ^ rot(fW1, 19) ^ (fW1 >>> 10));
        int fW15 = 0x00000280 + (rot(fW0, 7) ^ rot(fW0, 18) ^ (fW0 >>> 3));
        int fW01r = fW0 + (rot(fW1, 7) ^ rot(fW1, 18) ^ (fW1 >>> 3));

        int fcty_e = work.getMidstate()[4] + (rot(midstate2[1], 6) ^ rot(midstate2[1], 11) ^ rot(midstate2[1], 25)) +
                (midstate2[3] ^ (midstate2[1] & (midstate2[2] ^ midstate2[3]))) + 0xe9b5dba5;
        int fcty_e2 = (rot(midstate2[5], 2) ^ rot(midstate2[5], 13) ^ rot(midstate2[5], 22)) + ((midstate2[5] & midstate2[6]) |
                (midstate2[7] & (midstate2[5] | midstate2[6])));

        kernelContext.getKernel().setArgs(fW0, fW1, fW2, fW3, fW15, fW01r, fcty_e, fcty_e2,
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
                midstate2[7]);
    }

    /**
     * Execute the hash search using the given nonceStart starting value, worsize and localworksize
     * <p/>
     * Returns a MinerResult which contains the CLEvent, CLIntBuffer and IntBuffer for waiting and closing
     * asynchronously:
     * <p/>
     * CLEvent - Wait on this to finish
     * CLIntBuffer - Return to @CLIntBufferPool ObjectPool when event finishes
     * IntBuffer - Contains result.  Return to @IntBufferPool ObjectPool when finished using
     *
     * @param nonceStart
     * @param worksize
     * @param localWorkSize
     * @return MinerResult
     */
    public MinerResult hash(int nonceStart, int worksize, int localWorkSize) {

        MinerResult result = null;
        try {
            CLIntBuffer outputBuffer = (CLIntBuffer) clIntBufferPool.borrowObject();
            IntBuffer output = (IntBuffer) intBufferPool.borrowObject();

            kernelContext.getKernel().setArg(22, nonceStart * worksize);
            kernelContext.getKernel().setArg(23, outputBuffer);

            CLEvent event = kernelContext.getKernel().enqueueNDRange(kernelContext.getQueue(), new int[]{worksize}, new int[]{localWorkSize});
            result = new MinerResult(outputBuffer.read(kernelContext.getQueue(), 0, 0xF, output, false, event), output, outputBuffer);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

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
