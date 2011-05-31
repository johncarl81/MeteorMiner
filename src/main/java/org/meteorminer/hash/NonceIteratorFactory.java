package org.meteorminer.hash;

import java.util.Iterator;

/**
 * @author John Ericksen
 */
public interface NonceIteratorFactory {

    Iterator<Integer> createNonceIterator(int workgroupSize);

}
