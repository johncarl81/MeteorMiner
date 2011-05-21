package org.meteorminer.logging;

import com.google.inject.Inject;

import javax.inject.Singleton;
import java.text.DateFormat;
import java.util.Date;
import java.util.Formatter;

/**
 * @author John Ericksen
 */
@Singleton
public class CLLogger {

    @Inject
    private Statistics statistics;
    @Inject
    private DateFormat dateFormat;

    private String main = null;

    public void outputMain(){
        update();
        System.out.print(main);
    }
    
    public void update(){
        Formatter formatter = new Formatter().format("\r[%1.2f mhash/sec %1d pass %1d fail]", statistics.getHashRate(), statistics.getWorkPassed(), statistics.getWorkFailed());
        main = formatter.toString();
    }

    public void notification(String input, Object... args){
        System.out.printf("\r" + dateFormat.format(new Date()) + " " + input + "\n", args);
        if(main != null){
            System.out.print(main);
        }
    }
}
