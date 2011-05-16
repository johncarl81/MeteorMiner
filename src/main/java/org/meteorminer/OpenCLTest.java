package org.meteorminer;

import com.nativelibs4java.opencl.*;
import com.nativelibs4java.util.NIOUtils;
import org.meteorminer.queue.Consumer;
import org.meteorminer.queue.Producer;

import java.nio.DoubleBuffer;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author John Ericksen
 */
public class OpenCLTest {


    public void start() {
        try {
            System.out.println("hello");

            CLPlatform platform = JavaCL.listPlatforms()[0]; // take first platform available
            CLDevice[] devices = platform.listAllDevices(true);
            //CLDevice[] devices = platform.listCPUDevices();
            //CLDevice[] devices = platform.listGPUDevices();
            CLContext context = JavaCL.createBestContext();

            CLQueue queue = context.createDefaultQueue();

            int dataSize = 1000000;
            String src = "#pragma OPENCL EXTENSION cl_khr_fp64: enable\n" +
                    "__kernel void aSinB(                                                       \n" +
                    "   __global const double* a,                                       \n" +
                    "   __global const double* b,                                       \n" +
                    "   __global double* output)                                        \n" +
                    "{                                                                             \n" +
                    "   int i = get_global_id(0);                                      \n" +
                    "   output[i] = a[i] * sin(b[i]) + 1;                            \n" +
                    "for(int i = 0; i < 100; i++){}" +
                    "}                                                                             \n";

            CLProgram program = context.createProgram(src).build();
            CLKernel kernel = program.createKernel("aSinB");


            // Allocate OpenCL-hosted memory for inputs and output
            CLDoubleBuffer memIn1 = context.createDoubleBuffer(CLMem.Usage.Input, dataSize * 4);
            CLDoubleBuffer memIn2 = context.createDoubleBuffer(CLMem.Usage.Input, dataSize * 4);
            CLDoubleBuffer memOut = context.createDoubleBuffer(CLMem.Usage.Output, dataSize * 4);

            // Bind these memory objects to the arguments of the kernel
            kernel.setArgs(memIn1, memIn2, memOut);

            /// Map input buffers to populate them with some data
            DoubleBuffer a = memIn1.map(queue, CLMem.MapFlags.Write);
            DoubleBuffer b = memIn2.map(queue, CLMem.MapFlags.Write);

            // Fill the mapped input buffers with data
            for (int i = 0; i < dataSize; i++) {
                a.put(i, i);
                b.put(i, i);
            }

            /// Unmap input buffers
            memIn1.unmap(queue, a);
            memIn2.unmap(queue, b);

            long start = System.currentTimeMillis();

            // Ask for execution of the kernel with global size = dataSize
            //   and workgroup size = 1
            CLEvent clEvent = kernel.enqueueNDRange(queue, new int[]{dataSize}, new int[]{1});

            // Wait for all operations to be performed
            queue.finish();

            long end = System.currentTimeMillis();

            // Copy the OpenCL-hosted array back to RAM
            DoubleBuffer output = NIOUtils.directDoubles(dataSize, context.getByteOrder());
            memOut.read(queue, output, false, clEvent);


            // Compute absolute and relative average errors wrt Java implem
            double totalAbsoluteError = 0, totalRelativeError = 0;
            for (int i = 0; i < dataSize; i++) {
                double expected = i * Math.sin(i) + 1;
                double result = output.get(i);

                //System.out.println(result);

                double d = result - expected;
                if (expected != 0)
                    totalRelativeError += d / expected;

                totalAbsoluteError += d < 0 ? -d : d;
            }
            double avgAbsoluteError = totalAbsoluteError / dataSize;
            double avgRelativeError = totalRelativeError / dataSize;
            System.out.println("Average absolute error = " + avgAbsoluteError);
            System.out.println("Average relative error = " + avgRelativeError);

            long finalEnd = System.currentTimeMillis();

            System.out.println("Times: " + (end - start) + " : " + (finalEnd - end));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String argsp[]) {


        ArrayBlockingQueue<Long> queue = new ArrayBlockingQueue<Long>(2);

        Producer<Long> producer = new Producer<Long>(queue) {
            public Long produce() {
                long value = System.currentTimeMillis();
                System.out.println("Producing: " + value);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return value;
            }
        };
        Consumer<Long> consumer[] = new Consumer[10];

        new Thread(producer).start();

        for (int i = 0; i < 1; i++) {
            consumer[i] = new Consumer<Long>(queue) {
                public void consume(Long take) {
                    System.out.println(" Consuming: " + take);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            new Thread(consumer[i]).start();
        }

        while (true) {
            queue.clear();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("clear");
        }
    }
}
