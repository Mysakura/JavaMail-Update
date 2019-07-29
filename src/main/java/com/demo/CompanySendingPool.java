package com.demo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 发送线程池
 */
public class CompanySendingPool {
    private CompanySendingPool() {
    }
    private static class Inner{
        private static CompanySendingPool instance = new CompanySendingPool();
    }

    public static CompanySendingPool getInstance(){
        return Inner.instance;
    }

    /**
     * 核心线程数：5
     * 最大线程数：10
     * 时间单位：秒
     * 阻塞队列：10
     */
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            5,
            10,
            0L,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10));

    public CompanySendingPool addThread(CompanySending sending){
        executor.execute(sending);
        return getInstance();
    }

    public void shutDown(){
        executor.shutdown();
    }
}
