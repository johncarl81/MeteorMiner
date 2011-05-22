package org.meteorminer.hash.gpu;

import com.nativelibs4java.opencl.CLEvent;
import com.nativelibs4java.opencl.CLIntBuffer;

import java.nio.IntBuffer;

/**
 * POJO containing the results of a mining cycle
 *
 * @author John Ericksen
 */
public class MinerResult {

    private CLEvent event;
    private IntBuffer buffer;
    private CLIntBuffer clBuffer;

    public MinerResult(CLEvent event, IntBuffer buffer, CLIntBuffer clBuffer) {
        this.event = event;
        this.buffer = buffer;
        this.clBuffer = clBuffer;
    }

    public CLEvent getEvent() {
        return event;
    }

    public IntBuffer getBuffer() {
        return buffer;
    }

    public CLIntBuffer getClBuffer() {
        return clBuffer;
    }
}
