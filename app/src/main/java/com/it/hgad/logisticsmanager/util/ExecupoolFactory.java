package com.it.hgad.logisticsmanager.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/2/25.
 */
public class ExecupoolFactory {
    public static ExecutorService newFixedThreadPool(int nThreads){
        return new ThreadPoolExecutor(nThreads, nThreads,0L,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>());
    }

}
