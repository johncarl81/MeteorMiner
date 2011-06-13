package org.meteorminer.hash.gpu;

import com.nativelibs4java.opencl.*;
import org.apache.commons.io.IOUtils;
import org.meteorminer.config.MeteorAdvice;
import org.meteorminer.config.MeteorMinerRuntimeException;
import org.meteorminer.config.binding.BufferSize;

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
    @Inject
    @BufferSize
    private int bufferSize;

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

//        if(advice.isBfi_int() && extensions.contains("cl_amd_media_ops")){
//            program.addBuildOption("-D BITALIGN");
//            //todo: add bitalign processing?
//        }

        program.addBuildOption("-D OUTPUT_MASK=" + (bufferSize - 2));
        program.addBuildOption("-D OUTPUT_SIZE=" + (bufferSize - 1));

        //device.getPreferredVectorWidthInt();
        program.addBuildOption("-D VECTORS" + advice.getVectors());
    }

}
