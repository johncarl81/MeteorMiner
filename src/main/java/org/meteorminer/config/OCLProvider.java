package org.meteorminer.config;

import com.google.inject.Inject;
import com.nativelibs4java.opencl.CLBuildException;
import com.nativelibs4java.opencl.CLDevice;
import org.meteorminer.hash.gpu.OCL;

import javax.inject.Provider;
import java.io.IOException;

/**
 * @author John Ericksen
 */
public class OCLProvider implements Provider<OCL> {

    private static final String SEARCH_KERNEL = "search";
    private static final String SEARCH_KERNEL_FILE = "search.cl";
    @Inject
    private CLDevice device;

    @Override
    public OCL get() {
        OCL ocl;

        try {
            ocl = new OCL(SEARCH_KERNEL_FILE, SEARCH_KERNEL, device);
        } catch (CLBuildException e) {
            throw new MeteorMinerRuntimeException("Error Creating Kernel", e);
        } catch (IOException e) {
            throw new MeteorMinerRuntimeException("Error Creating Kernel", e);
        }


        return ocl;
    }
}
