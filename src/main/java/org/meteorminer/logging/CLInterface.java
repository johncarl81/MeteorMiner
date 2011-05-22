package org.meteorminer.logging;

import com.google.inject.Inject;
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

    private BufferedWriter out;

    public CLInterface() {
        out = new BufferedWriter(new OutputStreamWriter(System.out), 512);
    }

    private String main = null;

    public void outputMain() {
        update();
        try {
            out.write(main);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        main = new Formatter().format("\r[%1.2f mhash/sec %1.2f mhash/sec %1d pass %1d fail]",
                statistics.getInstantHashRate(), statistics.getLongHashRate(), statistics.getWorkPassed(), statistics.getWorkFailed()).toString();
    }

    public void notification(String input, Object... args) {
        try {
            out.write(new Formatter().format("\r" + dateFormat.format(new Date()) + ": " + input + "\n", args).toString());
            if (main != null) {
                out.write(main);
            }
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
