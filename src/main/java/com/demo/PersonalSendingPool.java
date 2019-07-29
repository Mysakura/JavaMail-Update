package com.demo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 发送线程池
 */
public class PersonalSendingPool {

    private PersonalSendingPool() {
    }
    private static class Inner{
        private static PersonalSendingPool instance = new PersonalSendingPool();
    }

    public static PersonalSendingPool getInstance(){
        return PersonalSendingPool.Inner.instance;
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

    public PersonalSendingPool addThread(PersonalSending sending){
        executor.execute(sending);
        return getInstance();
    }

    public void shutDown(){
        executor.shutdown();
    }
}
