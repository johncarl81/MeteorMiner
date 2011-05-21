package org.meteorminer.hash.scanHash;

import org.meteorminer.binding.GetWorkTimeout;
import org.meteorminer.domain.Work;
import org.meteorminer.hash.HashScanner;
import org.meteorminer.hash.LocalMinerController;
import org.meteorminer.hash.MinerController;
import org.meteorminer.logging.CLLogger;
import org.meteorminer.logging.Statistics;
import org.meteorminer.queue.WorkFoundCallback;

import javax.inject.Inject;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static org.meteorminer.hash.scanHash.HexUtil.decode;


//4877554
public class ScanHash implements HashScanner {

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

    public void scan(Work work, WorkFoundCallback workFoundCallback) {
        Random rand = new Random(System.currentTimeMillis());
        int start = rand.nextInt(Integer.MAX_VALUE);

        scan(work, workFoundCallback, start, start - 1);
    }

    public void scan(Work work, WorkFoundCallback workFoundCallback, int start, int end) {
        nonceCount = 0;

        previousCount = nonceCount;
        timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        long currentNonceCount = nonceCount;
                        statistics.incrementHashCount(currentNonceCount - previousCount);
                        previousCount = currentNonceCount;
                    }
                }, 1000, 1000);

        final LocalMinerController localController = new LocalMinerController(minerController, logger);

        timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        localController.interuptProduction();
                    }
                }, getWorkTimeout * 1000);
        int[] _data = decode(new int[16], work.getDataString().substring(128));
        int[] _midstate = decode(decode(new int[16], work.getHash1()), work.getMidstateString());
        int[] __state = new int[_midstate.length];
        int[] buff = new int[64];


        int[] __hash = SHA256.initState();
        for (int nonce = start; nonce != end && !localController.haltProduction(); nonce++, nonceCount++) {
            if (processHash.processHash(work, _data, nonce, _midstate, __state, buff, __hash, workFoundCallback)) {
                break;
            }
        }

        minerController.unregister(localController);
        statistics.incrementHashCount(nonceCount - previousCount);
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public void setGetWorkTimeout(int timeout) {
        this.getWorkTimeout = timeout;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public void setMinerController(MinerController minerController) {
        this.minerController = minerController;
    }

    public void setProcessHash(ProcessHash processHash) {
        this.processHash = processHash;
    }
}
