package org.meteorminer.hash;

import org.meteorminer.domain.Work;
import org.meteorminer.queue.WorkFoundCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class HashCacheScanner implements HashScanner{

    private Map<String, Integer> cache = new HashMap<String, Integer>();

    private HashScanner delegate;

    public HashCacheScanner(HashScanner delegate) {
        this.delegate = delegate;
    }

    @Override
    public void scan(Work work, WorkFoundCallback workFoundCallback) {
       if(cache.containsKey(work.getDataString())){
           System.out.println("***WORK FOUND IN CACHE!!!***");
           workFoundCallback.found(work, cache.get(work.getDataString()));
       }
       else{
           delegate.scan(work, workFoundCallback);
       }
    }

    public void add(Work work, Integer nonce){
        cache.put(work.getDataString(), nonce);
    }
}
