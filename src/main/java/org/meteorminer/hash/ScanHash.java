package org.meteorminer.hash;

import org.meteorminer.Work;
import org.meteorminer.queue.WorkFoundCallback;
import org.meteorminer.stats.Statistics;

import javax.inject.Inject;
import java.util.Timer;
import java.util.TimerTask;

import static org.meteorminer.hash.HexUtil.decode;
import static org.meteorminer.hash.HexUtil.encode;

public class ScanHash {

    @Inject
    private Statistics statistics;

    private long previousCount;
    private int nonce;
    private long nonceCount;

	public void scan(Work work, int start, int count, WorkFoundCallback workFoundCallback) {
		SHA256 sha256 = new SHA256();
        nonceCount = 0;

        long endTime = System.currentTimeMillis() + 60000;

        nonce = start;

        previousCount = nonceCount;
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                statistics.incrementHashCount(nonceCount - previousCount);
                previousCount = nonceCount;
                if(nonceCount < 0){
                    System.out.println("Alert!");
                }
            }
        }, 100, 100);

		int[] _data = decode(new int[16], work.getDataString().substring(128));
		int[] _midstate = decode(decode(new int[16], work.getHash1()), work.getMidstateString());

		int[] __state, __data, __hash1, __hash;
		for (; nonce != start + count && System.currentTimeMillis() < endTime; nonce++, nonceCount++) {
			_data[3] = nonce; // NONCE is _data[3]

			__state = new int[_midstate.length];
			System.arraycopy(_midstate, 0, __state, 0, _midstate.length);
			__data = _data;

			sha256.processBlock(__state, __data);
			__hash1 = __state;

			__state = SHA256.initState();
			sha256.processBlock(__state, __hash1);
			__hash = __state;

			if (__hash[7] == 0) {
				work.setData(work.getDataString().substring(0, 128) + encode(__data));
                workFoundCallback.found(work);
                break;
			}
		}

        statistics.incrementHashCount(nonceCount - previousCount);
	}

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public static void main(String[] args){
        long value = 0xFFFFFFFFL;
        System.out.println((int)value);
    }
}
