package org.meteorminer.hash.gpu;

import com.nativelibs4java.opencl.CLBuffer;
import com.nativelibs4java.opencl.CLEvent;
import org.apache.commons.pool.ObjectPool;
import org.bridj.Pointer;
import org.meteorminer.config.binding.*;
import org.meteorminer.domain.GPUDevice;
import org.meteorminer.domain.Work;
import org.meteorminer.output.CLInterface;

import javax.inject.Inject;
import java.nio.IntBuffer;

/**
 * Guts of the Miner, rearranged for use within Meteor Miner.
 * <p/>
 * Inspired by the Diablo miner software:
 * <p/>
 * https://github.com/Diablo-D3/DiabloMiner
 * http://forum.bitcoin.org/?topic=1721.0
 * <p/>
 * Thanks to Diablo-D3
 *
 * @author John Ericksen
 */
public class MinerCore {

    private final KernelContext kernelContext;

    private ObjectPool clIntBufferPool;
    private ObjectPool intBufferPool;

    private int localWorkSize;
    private int workgroupSize;
    private CLInterface output;
    private Work work;
    private int bufferSize;

    @Inject
    public MinerCore(GPUDevice device,
                     @Intensity int intensity,
                     @WorkSize int worksize,
                     @SearchKernel KernelContext kernelContext,
                     @CLIntBufferPool ObjectPool clIntBufferPool,
                     @IntBufferPool ObjectPool intBufferPool, CLInterface output,
                     @BufferSize int bufferSize) {
        this.output = output;
        this.bufferSize = bufferSize;

        if (worksize == -1) {
            localWorkSize = kernelContext.getKernel().getWorkGroupSize().get(device.getCLDevice()).intValue();
        } else {
            localWorkSize = worksize;
        }
        this.workgroupSize = new Double(Math.pow(2, 16 + intensity)).intValue();

        this.kernelContext = kernelContext;
        this.clIntBufferPool = clIntBufferPool;
        this.intBufferPool = intBufferPool;
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
     * @return MinerResult
     */
    public MinerResult hash(int nonceStart, Work work) {
        MinerResult result = null;
        try {
            CLBuffer<Integer> outputBuffer = (CLBuffer<Integer>) clIntBufferPool.borrowObject();
            IntBuffer outputBuff = (IntBuffer) intBufferPool.borrowObject();

            synchronized (kernelContext) {

                if (this.work != work) {
                    updateCommonVariables(work);
                }

                kernelContext.getKernel().setArg(22, nonceStart);
                kernelContext.getKernel().setArg(23, outputBuffer);

                CLEvent event = kernelContext.getKernel().enqueueNDRange(kernelContext.getQueue(), new int[]{workgroupSize}, new int[]{localWorkSize});
                result = new MinerResult(outputBuffer.read(kernelContext.getQueue(), Pointer.pointerToInts(outputBuff), false, event), outputBuff, outputBuffer);
            }
        } catch (Exception e) {
            output.error(e);
        }

        return result;

    }

    private void updateCommonVariables(Work work) {
        this.work = work;
        //grabs the preprocessed variables
        GPUPreProcessWork preProcessWork = (GPUPreProcessWork) work.getPreProcessedWork().get(GPUPreProcessWorkFactory.PRE_PROCESS_NAME);

        kernelContext.getKernel().setArgs(preProcessWork.getfW0(),
                preProcessWork.getfW1(),
                preProcessWork.getfW2(),
                preProcessWork.getfW3(),
                preProcessWork.getfW15(),
                preProcessWork.getfW01r(),
                preProcessWork.getFcty_e(),
                preProcessWork.getFcty_e2(),
                work.getMidstate()[0],
                work.getMidstate()[1],
                work.getMidstate()[2],
                work.getMidstate()[3],
                work.getMidstate()[4],
                work.getMidstate()[5],
                work.getMidstate()[6],
                work.getMidstate()[7],
                preProcessWork.getMidstate2()[1],
                preProcessWork.getMidstate2()[2],
                preProcessWork.getMidstate2()[3],
                preProcessWork.getMidstate2()[5],
                preProcessWork.getMidstate2()[6],
                preProcessWork.getMidstate2()[7]);
    }


    public int getWorkgroupSize() {
        return workgroupSize;
    }
}
