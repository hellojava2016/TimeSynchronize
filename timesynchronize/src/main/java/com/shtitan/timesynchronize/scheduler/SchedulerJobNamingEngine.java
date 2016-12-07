package com.shtitan.timesynchronize.scheduler;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * 该类被用作性能轮询的组名和轮询名称的管理，以防出现重复的名称.
 * 
 * @author yangtao
 * 
 */
public class SchedulerJobNamingEngine {

    private static final String DEFAULT_SCHEDULER_JOB = "DEFAULT_SCHEDULER_JOB";

    private static AtomicLong count = new AtomicLong(0);

    /**
     * 创建调度任务
     * 
     * @param SchedulerJob
     * @return String ---SchedulerJob
     */
    public static String createSchedulerJobName() {
        return DEFAULT_SCHEDULER_JOB + count.getAndIncrement();
    }

}