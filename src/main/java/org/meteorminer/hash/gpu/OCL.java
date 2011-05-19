package org.meteorminer.hash.gpu;

import com.nativelibs4java.opencl.*;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

import static com.nativelibs4java.opencl.JavaCL.createBestContext;

/**
 * @author John Ericksen
 */
public class OCL {
    public final CLProgram program;
    public final CLQueue queue;
    public final CLContext context;
    public final CLKernel kernel;

    public OCL(String srcFile, String kernelName) throws CLBuildException, IOException {
        //SetupUtils.failWithDownloadProposalsIfOpenCLNotAvailable();
        String src = IOUtils.toString(getClass().getClassLoader().getResourceAsStream(srcFile));
        context = createBestContext();
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
