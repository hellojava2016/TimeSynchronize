package com.shtitan.timesynchronize.scheduler;

import org.quartz.Trigger;

/**
 * <p>
 * Title: ScheduleJob
 * </p>
 * <p>
 * Description: 任务调度任务
 * </p>
 * 
 * @author yangtao
 * @created 2010-2-18 上午09:40:46
 * @modified [who date description]
 * @check [who date description]
 */
public interface SchedulerJob {

    /**
     * 初始化调度任务资源
     */
    void init();

    /**
     * 任务执行方法体,根据具体实现
     */
    void execute();

    /**
     * 获取该任务执行的Trigger
     * 
     * @return 任务Trigger
     */
    Trigger getTrigger();

    public void setJobName(String jobName);
    
    public void setJobGroupName(String jobGroupName);
    /**
     * 获取SchedulerJob名称
     * 
     * @return
     */
    String getJobName();

    /**
     * 获取SchedulerJob job组名称
     * 
     * @return
     */
    String getJobGroupName();
    
    /**
     * 是否暂停调度任务
     * @return
     */
    boolean isPaused();
    
    /**
     * 暂停调度任务
     */
    void pause();
    
    /**
     * 恢复调度任务
     */
    void resume();

}
