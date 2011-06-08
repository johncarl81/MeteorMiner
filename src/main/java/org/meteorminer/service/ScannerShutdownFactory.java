package org.meteorminer.service;

import org.meteorminer.hash.HashScanner;

import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * Assisted injection factory to create a ScannerShutdownThread
 *
 * @author John Ericksen
 */
public interface ScannerShutdownFactory {
    ScannerShutdownThread buildScannerShutdown(ExecutorService executor, Set<HashScanner> scanners);
}
