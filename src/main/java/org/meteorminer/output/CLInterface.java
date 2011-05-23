package org.meteorminer.output;

import com.google.inject.Inject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool.ObjectPool;
import org.meteorminer.binding.CLIntBufferPool;
import org.meteorminer.binding.IntBufferPool;
import org.meteorminer.binding.Verbose;

import javax.inject.Singleton;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.Formatter;

/**
 * @author John Ericksen
 */
@Singleton
public class CLInterface {

    @Inject
    private Statistics statistics;
    @Inject
    private DateFormat dateFormat;
    @Inject
    @Verbose
    private boolean verbose;
    @Inject
    @IntBufferPool
    private ObjectPool intBufferPool;
    @Inject
    @CLIntBufferPool
    private ObjectPool clIntBufferPool;

    private BufferedWriter out;

    public CLInterface() {
        out = new BufferedWriter(new OutputStreamWriter(System.out), 512);
    }

    private String main = "";

    public void outputMain() {
        int prevMainSize = main.length();
        main = new Formatter().format("\r[%1.2f(%1.2f)mh/s %1d pass %1d fail | buffers: %1d %1d %1d %1d %1d]",
                statistics.getInstantHashRate(), statistics.getLongHashRate(), statistics.getWorkPassed(), statistics.getWorkFailed(),
                intBufferPool.getNumActive(), intBufferPool.getNumIdle(), clIntBufferPool.getNumActive(), clIntBufferPool.getNumIdle(), statistics.getSavedTime()).toString();
        try {
            out.write(StringUtils.leftPad(main, prevMainSize - main.length() + 1));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void notification(String input, Object... args) {
        try {
            String notification = new Formatter().format("\r" + dateFormat.format(new Date()) + ": " + input, args).toString();
            out.write(notification);
            out.write(StringUtils.leftPad("\n", main.length() - notification.length() + 1));
            out.write(main);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void verbose(String input, Object... args) {
        if (verbose) {
            notification(input, args);
        }
    }
}