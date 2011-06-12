package org.meteorminer.output;

import org.apache.commons.lang.StringUtils;
import org.meteorminer.config.binding.Verbose;
import org.meteorminer.domain.Device;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Map;

/**
 * @author John Ericksen
 */
@Singleton
public class CLInterface {

    @Inject
    private StatisticsHolder statisticsHolder;
    @Inject
    private DateFormat dateFormat;
    @Inject
    @Verbose
    private boolean verbose;
    @Inject
    private CLInterface output;

    private BufferedWriter out;

    public CLInterface() {
        out = new BufferedWriter(new OutputStreamWriter(System.out), 512);
    }

    private String main = "";

    public void outputMain() {
        int prevMainSize = main.length();
        StringBuilder builder = new StringBuilder();

        builder.append("\r[");

        builder.append(statisticsHolder.toString());
        if (verbose) {
            for (Map.Entry<Device, Statistics> entry : statisticsHolder.getStatistics().entrySet()) {
                builder.append(" [");
                builder.append(entry.getKey().getName());
                builder.append(':');
                builder.append(entry.getValue().toString());
                builder.append(']');
            }
        }

        builder.append(']');

        main = builder.toString();

        try {
            out.write(StringUtils.leftPad(main, prevMainSize - main.length() + 1));
            out.flush();
        } catch (IOException e) {
            output.error(e);
        }
    }

    public synchronized void notification(String input, Object... args) {
        try {
            String notification = new Formatter().format("\r" + dateFormat.format(new Date()) + ": " + input, args).toString();
            out.write(notification);
            out.write(StringUtils.leftPad("\n", main.length() - notification.length() + 1));
            out.write(main);
            out.flush();
        } catch (IOException e) {
            output.error(e);
        }
    }

    public synchronized void verbose(String input, Object... args) {
        if (verbose) {
            notification(input, args);
        }
    }

    public void error(Exception e) {
        if (verbose) {
            e.printStackTrace();
        } else {
            notification("Error: " + e.getMessage());
        }
    }
}
