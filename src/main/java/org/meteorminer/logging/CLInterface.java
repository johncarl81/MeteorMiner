package org.meteorminer.logging;

import com.google.inject.Inject;
import org.meteorminer.binding.Verbose;

import javax.inject.Singleton;
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

    private final Formatter formatter = new Formatter();
    private String main = null;

    public void outputMain() {
        update();
        System.out.print(main);
    }

    public void update() {
        formatter.format("\r[%1.2f mhash/sec %1d pass %1d fail]", statistics.getHashRate(), statistics.getWorkPassed(), statistics.getWorkFailed());
        main = formatter.toString();
    }

    public void notification(String input, Object... args) {
        System.out.printf("\r" + dateFormat.format(new Date()) + ": " + input + "\n", args);
        if (main != null) {
            System.out.print(main);
        }
    }

    public void verbose(String input, Object... args) {
        if (verbose) {
            notification(input, args);
        }
    }
}
