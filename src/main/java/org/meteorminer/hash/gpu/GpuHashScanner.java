package org.meteorminer.hash.gpu;

import com.nativelibs4java.opencl.CLBuildException;
import org.meteorminer.Work;
import org.meteorminer.binding.GetWorkTimeout;
import org.meteorminer.hash.HashScanner;
import org.meteorminer.hash.LocalMinerController;
import org.meteorminer.hash.MinerController;
import org.meteorminer.hash.scanHash.ProcessHash;
import org.meteorminer.hash.scanHash.SHA256;
import org.meteorminer.queue.WorkFoundCallback;
import org.meteorminer.stats.Statistics;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Timer;
import java.util.TimerTask;

import static org.meteorminer.hash.scanHash.HexUtil.decode;

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
        workgroupSize = ocl.kernel.getWorkGroupSize().get(ocl.context.getDevices()[0]).intValue();
        localWorkSize = 1;

    }

    public void scan(Work work, WorkFoundCallback workFoundCallback) {
        scan(work, workFoundCallback, 0, (int) (0xFFFFFFFFL / workgroupSize));
    }

    public void scan(Work work, WorkFoundCallback workFoundCallback, int start, long end) {

        DiabloMiner diabloMiner = new DiabloMiner(ocl, workgroupSize, work);
        int startNonce = start / workgroupSize;

        long nonceEnd = startNonce + (end / workgroupSize);

        previousCount = nonceCount;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                long currentHashCount = nonceCount * workgroupSize;
                statistics.incrementHashCount(currentHashCount - previousCount);
                previousCount = currentHashCount;
            }
        };
        timer.schedule(task, 1000, 1000);

        final LocalMinerController localController = new LocalMinerController(minerController);

        TimerTask endTimer = new TimerTask() {
            @Override
            public void run() {
                localController.interuptProduction();
            }
        };

        timer.schedule(endTimer, getWorkTimeout * 1000);

        for (int nonce = startNonce; nonce < nonceEnd /*&& !localController.haltProduction()*/; nonce++/*, nonceCount++*/) {


            IntBuffer output = diabloMiner.hash(work, ocl, nonce, workgroupSize, localWorkSize);

            for (int i = 0; i < workgroupSize; i++) {

                if (output.get(i) > 0) {
                    int[] _data = decode(new int[16], work.getDataString().substring(128));
                    int[] _midstate = decode(decode(new int[16], work.getHash1()), work.getMidstateString());
                    int[] __state = new int[_midstate.length];
                    int[] buff = new int[64];


                    int[] __hash = SHA256.initState();
                   ProcessHash.processHash(work, _data, output.get(i), _midstate, __state, buff, __hash, workFoundCallback);
                }
            }

            //System.out.println("{" + (nonce * workgroupSize) + " - " + ((nonce+1) * workgroupSize)  + "}");

            //output.clear();
            //statistics.incrementHashCount( workgroupSize);


        }

        task.cancel();
        endTimer.cancel();
        diabloMiner.finish();


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
}
