package org.meteorminer;

import com.google.inject.Injector;
import org.apache.commons.cli.*;
import org.meteorminer.config.MeteorAdvice;
import org.meteorminer.config.MeteorMinerInjector;
import org.meteorminer.output.ShutdownDetection;
import org.meteorminer.service.MiningService;

import java.net.MalformedURLException;

/**
 * Command Line interface entry point to the Meteor Miner application.  Provides a simple command line input and
 * general startup of the application.
 *
 * @author John Ericksen
 */
public class MeteorMiner {
    public static void main(String[] args) {

        try {
            Options options = buildOptions();

            CommandLine line = new PosixParser().parse(options, args);

            MeteorAdvice advice = new MeteorAdvice(line);

            if (advice.help()) {
                //print help
                HelpFormatter formatter = new HelpFormatter();

                formatter.printHelp("MeteorMiner", options);
            } else {
                Injector injector = MeteorMinerInjector.getInjector(advice);

                Runtime runtime = injector.getInstance(Runtime.class);
                runtime.addShutdownHook(injector.getInstance(ShutdownDetection.class));

                MiningService miningService = injector.getInstance(MiningService.class);

                miningService.start();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static Options buildOptions() {
        Options options = new Options();
        options.addOption("h", "help", false, "this help");
        options.addOption("u", "user", true, "optional bitcoin host username");
        options.addOption("p", "pass", true, "optional bitcoin host password");
        options.addOption("o", "host", true, "optional bitcoin host IP (default: localhost)");
        options.addOption("r", "port", true, "optional bitcoin host port (default: 8332)");
        options.addOption("g", "getwork", true, "optional seconds between getwork refresh (default: 5)");
        options.addOption("v", "verbose", false, "Verbose Output");
        options.addOption("x", "proxy", true, "optional proxy settings host:port<:username:password>");
        options.addOption("c", "cpu", true, "optional count of mining CPUs");
        options.addOption("d", "gpu", true, "optional comma separated list of gpu identifier to use: 0,1,2,3");
        options.addOption("i", "intensity", true, "optional intensity value");
        options.addOption("w", "worksize", true, "optional worksize override (> 0)");
        options.addOption("tandem", false, "optional flag to tun on tandem device mining");
        options.addOption("l", "loops", true, "optional kernel loop value");

        return options;
    }
}
