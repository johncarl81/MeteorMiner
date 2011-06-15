package org.meteorminer.hash.gpu;

import com.nativelibs4java.opencl.CLBuffer;
import com.nativelibs4java.opencl.CLEvent;

import java.nio.IntBuffer;

/**
 * POJO containing the results of a mining cycle
 *
 * @author John Ericksen
 */
public class MinerResult {

    private CLEvent event;
    private IntBuffer buffer;
    private CLBuffer<Integer> clBuffer;

    public CLEvent getEvent() {
        return event;
    }

    public IntBuffer getBuffer() {
        return buffer;
    }

    public CLBuffer<Integer> getClBuffer() {
        return clBuffer;
    }

    public void setup(CLEvent event, IntBuffer outputBuff, CLBuffer<Integer> outputBuffer) {
        this.event = event;
        this.buffer = outputBuff;
        this.clBuffer = outputBuffer;
    }

    public void clear() {
        setup(null, null, null);
    }
}
