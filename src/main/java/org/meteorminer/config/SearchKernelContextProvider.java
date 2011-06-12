package org.meteorminer.config;

import com.nativelibs4java.opencl.*;
import org.apache.commons.io.IOUtils;
import org.meteorminer.hash.gpu.KernelContext;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Search Kernel Context provider, builds and returns the Search Kernel.
 *
 * @author John Ericksen
 */
public class SearchKernelContextProvider implements Provider<KernelContext> {

    private static final String SEARCH_KERNEL = "search";
    private static final String SEARCH_KERNEL_FILE = "search.cl";
    @Inject
    private CLDevice device;
    @Inject
    private MeteorAdvice advice;

    @Override
    public KernelContext get() {
        KernelContext kernelContext;

        try {

            String src = IOUtils.toString(getClass().getClassLoader().getResourceAsStream(SEARCH_KERNEL_FILE));
            CLContext context = device.getPlatform().createContext(null, device);
            CLQueue queue = context.createDefaultQueue();
            CLProgram program = context.createProgram(src);
            String[] extensions = context.getPlatform().getExtensions();

            addOptions(program, device, Arrays.asList(extensions));

            program = program.build();

            CLKernel kernel = program.createKernel(SEARCH_KERNEL);

            kernelContext = new KernelContext(queue, context, kernel);
        } catch (CLBuildException e) {
            throw new MeteorMinerRuntimeException("Error Creating Kernel", e);
        } catch (IOException e) {
            throw new MeteorMinerRuntimeException("Error Creating Kernel", e);
        }

        return kernelContext;
    }

    private void addOptions(CLProgram program, CLDevice device, List<String> extensions) {
        //loops
        if (advice.getLoops() > 1) {
            program.addBuildOption("-D DOLOOPS");
            program.addBuildOption("-D LOOPS=" + advice.getLoops());
        }

        //todo:implement
//        if(advice.isBFI_INT() && extensions.contains("cl_amd_media_ops")){
//            program.addBuildOption("-D BITALIGN");
//        }
//        program.addBuildOption("-D OUTPUT_MASK=0xFF");
//
//        if(advice.isVectors()){
//            program.addBuildOption("-D VECTORS");
//        }
    }

}
