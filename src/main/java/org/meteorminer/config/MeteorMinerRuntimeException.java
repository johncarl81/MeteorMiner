package org.meteorminer.config;

/**
 * Runtime Exception wrapper for Meteor Miner RuntimeExceptions.
 *
 * @author John Ericksen
 */
public class MeteorMinerRuntimeException extends RuntimeException {
    public MeteorMinerRuntimeException() {
    }

    public MeteorMinerRuntimeException(String s) {
        super(s);
    }

    public MeteorMinerRuntimeException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public MeteorMinerRuntimeException(Throwable throwable) {
        super(throwable);
    }
}
