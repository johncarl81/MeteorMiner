package org.meteorminer.hash.gpu;

import com.nativelibs4java.opencl.CLContext;
import com.nativelibs4java.opencl.CLKernel;
import com.nativelibs4java.opencl.CLQueue;

/**
 * Context / Kernel / Program / Queue container associated with the provided kernel source
 *
 * @author John Ericksen
 */
public class KernelContext {
    public final CLQueue queue;
    public final CLContext context;
    public final CLKernel kernel;

    public KernelContext(CLQueue queue, CLContext context, CLKernel kernel) {
        this.queue = queue;
        this.context = context;
        this.kernel = kernel;
    }

    public CLQueue getQueue() {
        return queue;
    }

    public CLContext getContext() {
        return context;
    }

    public CLKernel getKernel() {
        return kernel;
    }
}
