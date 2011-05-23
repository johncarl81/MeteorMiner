package org.meteorminer.hash.gpu;

import com.nativelibs4java.opencl.*;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

/**
 * Contetx / Kernel / Program / Queue container associated with the provided kernel source
 *
 * @author John Ericksen
 */
public class OCL {
    public final CLProgram program;
    public final CLQueue queue;
    public final CLContext context;
    public final CLKernel kernel;

    public OCL(String srcFile, String kernelName, CLDevice device) throws CLBuildException, IOException {
        String src = IOUtils.toString(getClass().getClassLoader().getResourceAsStream(srcFile));
        context = device.getPlatform().createContext(null, device);
        queue = context.createDefaultQueue();
        program = context.createProgram(src).build();
        kernel = program.createKernel(kernelName);
    }

    public CLProgram getProgram() {
        return program;
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
