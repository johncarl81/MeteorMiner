package org.meteorminer;

import com.google.inject.Injector;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.meteorminer.config.MeteorAdvice;
import org.meteorminer.config.MeteorMinerInjector;
import org.meteorminer.service.MiningService;

import java.net.MalformedURLException;

/**
 * @author John Ericksen
 */
public class MeteorMiner {
    public static void main(String[] args) {

        try {
            CommandLine line = new PosixParser().parse(buildOptions(), args);

            MeteorAdvice advice = new MeteorAdvice(line);

            if (advice.help()) {
                //print help
                //todo:add help
            } else {
                Injector injector = MeteorMinerInjector.buildInjector(advice);

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
        options.addOption("u", "user", true, "bitcoin host username");
        options.addOption("p", "pass", true, "bitcoin host password");
        options.addOption("h", "help", false, "this help");
        options.addOption("o", "host", true, "bitcoin host IP");
        options.addOption("r", "port", true, "bitcoin host port");
        options.addOption("g", "getwork", true, "seconds between getwork refresh");
        options.addOption("v", "verbose", false, "Verbose Output");
        options.addOption("x", "proxy", true, "optional proxy settings IP:PORT<:username:password>");
        options.addOption("c", "cpu", true, "optional count of mining CPUs");
        /*options.addOption("f", "fps", true, "target execution timing");
        options.addOption("w", "worksize", true, "override worksize");
        options.addOption("z", "loops", true, "kernel loops (power of two, 0 is off)");
        */
        return options;
    }
}
