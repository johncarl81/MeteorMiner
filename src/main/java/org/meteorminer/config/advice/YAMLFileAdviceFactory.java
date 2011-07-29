package org.meteorminer.config.advice;

import org.yaml.snakeyaml.Yaml;

/**
 * @author John Ericksen
 */
public class YAMLFileAdviceFactory {

    public void run() {
        Yaml yaml = new Yaml();
    }

    public MeteorAdvice buildAdvice(String configFile) {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }
}
