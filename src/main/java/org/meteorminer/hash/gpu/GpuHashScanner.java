package org.meteorminer.hash.gpu;

import com.nativelibs4java.opencl.CLBuildException;
import org.meteorminer.binding.GetWorkTimeout;
import org.meteorminer.domain.Work;
import org.meteorminer.hash.HashScanner;
import org.meteorminer.hash.LocalMinerController;
import org.meteorminer.hash.MinerController;
import org.meteorminer.hash.scanHash.ProcessHash;
import org.meteorminer.logging.CLLogger;
import org.meteorminer.logging.Statistics;
import org.meteorminer.queue.WorkFoundCallback;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author John Ericksen
 */
public class GpuHashScanner implements HashScanner {

    private static final String SEARCH_KERNEL = "search";

    private static final OCL ocl;

    @Inject
    private Statistics statistics;
    @Inject
    @GetWorkTimeout
    private int getWorkTimeout;
    @Inject
    private MinerController minerController;
    @Inject
    private Timer timer;
    @Inject
    private ProcessHash processHash;
    @Inject
    private CLLogger logger;

    private long previousCount;
    private long nonceCount;
    static final int workgroupSize;
    static final int localWorkSize;

    static {
        try {
            ocl = new OCL("search.cl", SEARCH_KERNEL);
        } catch (CLBuildException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        workgroupSize = 500000;
        localWorkSize = 500;

    }

    public void scan(Work work, WorkFoundCallback workFoundCallback) {
        scan(work, workFoundCallback, 0, 0xFFFFFFFFL);
    }

    public void scan(Work work, WorkFoundCallback workFoundCallback, int start, long end) {

        DiabloMiner diabloMiner = new DiabloMiner(ocl, workgroupSize, work, 0xF);
        int startNonce = (start / workgroupSize);

        long nonceEnd = startNonce + (end / workgroupSize) + 1;

        previousCount = nonceCount * workgroupSize;

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                long currentHashCount = nonceCount * workgroupSize;
                statistics.incrementHashCount(currentHashCount - previousCount);
                previousCount = currentHashCount;
            }
        };
        timer.schedule(task, 1000, 1000);

        final LocalMinerController localController = new LocalMinerController(minerController, logger);

        TimerTask endTimer = new TimerTask() {
            @Override
            public void run() {
                localController.interuptProduction();
            }
        };

        timer.schedule(endTimer, (getWorkTimeout * 1000) - (System.currentTimeMillis() - work.getStartTime()));

        work.start();
        for (int nonce = startNonce; nonce < nonceEnd && !localController.haltProduction(); nonce++, nonceCount++) {


            IntBuffer output = diabloMiner.hash(work, ocl, nonce, workgroupSize, localWorkSize);

            for (int i = 0; i < 0xF; i++) {

                if (output.get(i) > 0) {
                    /*int[] _data = decode(new int[16], work.getDataString().substring(128));
                    int[] _midstate = decode(decode(new int[16], work.getHash1()), work.getMidstateString());
                    int[] __state = new int[_midstate.length];
                    int[] buff = new int[64];


                    int[] __hash = SHA256.initState();
                    processHash.processHash(work, _data, output.get(i), _midstate, __state, buff, __hash, workFoundCallback);
                    diabloMiner.createBuffer(ocl);*/
                    try{
                        work.found();
                        final ByteBuffer digestInput = ByteBuffer.allocate(80);

                        for (int j = 0; j < 19; j++)
                            digestInput.putInt(j * 4, work.getData()[j]);

                        digestInput.putInt(19 * 4, output.get(i));

                        final MessageDigest digestInside = MessageDigest.getInstance("SHA-256");
                        final MessageDigest digestOutside = MessageDigest.getInstance("SHA-256");

                        byte[] digestOutput = digestOutside.digest(digestInside.digest(digestInput.array()));

                        long G = ((long) ((0x000000FF & ((int) digestOutput[27])) << 24 |
                                (0x000000FF & ((int) digestOutput[26])) << 16 |
                                (0x000000FF & ((int) digestOutput[25])) << 8 |
                                (0x000000FF & ((int) digestOutput[24])))) & 0xFFFFFFFFL;

                        long H = ((long) ((0x000000FF & ((int) digestOutput[31])) << 24 |
                                (0x000000FF & ((int) digestOutput[30])) << 16 |
                                (0x000000FF & ((int) digestOutput[29])) << 8 |
                                (0x000000FF & ((int) digestOutput[28])))) & 0xFFFFFFFFL;

                        if (G <= work.getTarget()[6]) {
                            if (H == 0) {
                                workFoundCallback.found(work, output.get(i));
                            } else {
                                logger.notification("Invalid block found, possible driver or hardware issue");
                            }
                        }
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    diabloMiner.createBuffer(ocl);
                }
            }

            //System.out.println("{" + (nonce * workgroupSize) + " - " + ((nonce+1) * workgroupSize)  + "}");

            //output.clear();
            //statistics.incrementHashCount( workgroupSize);


        }

        logger.notification("Finished age:" + work.getAge());

        task.cancel();
        endTimer.cancel();
        diabloMiner.finish();
        previousCount = 0;


        minerController.unregister(localController);
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public void setGetWorkTimeout(int getWorkTimeout) {
        this.getWorkTimeout = getWorkTimeout;
    }

    public void setMinerController(MinerController minerController) {
        this.minerController = minerController;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public void setProcessHash(ProcessHash processHash) {
        this.processHash = processHash;
    }
}
