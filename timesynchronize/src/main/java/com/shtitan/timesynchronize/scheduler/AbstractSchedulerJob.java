package com.shtitan.timesynchronize.scheduler;

import org.quartz.Scheduler;

/**
 * <p>
 * Title: AbstratSchedulerJob
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author  yangtao
 * @created 2010-2-18 下午03:00:59
 * @modified [who date description]
 * @check [who date description]
 */
public abstract class AbstractSchedulerJob implements SchedulerJob {
    public final static String DEFAULT_GROUP = Scheduler.DEFAULT_GROUP;
    /** 任务名称 */
    private String jobName;
    /** 任务组名称 */
    private String jobGroupName;

    private volatile boolean paused;

    public AbstractSchedulerJob() {
        super();
    }

    /**
     * 
     * (non-Javadoc)
     * 
     * @see com.xuntai.xtems.framework.schedule.SchedulerJob#init()
     */
    public void init() {

    }

    /**
     * (non-Javadoc)
     * 
     * @see com.xuntai.xtems.framework.schedule.SchedulerJob#getJobGroupName()
     */
    @Override
    public String getJobGroupName() {
        return jobGroupName;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.xuntai.xtems.framework.schedule.SchedulerJob#getJobName()
     */
    @Override
    public String getJobName() {
        return jobName;
    }

    /**
     * 
     * (non-Javadoc)
     * 
     * @see com.xuntai.xtems.framework.schedule.SchedulerJob#isPaused()
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * 
     * (non-Javadoc)
     * 
     * @see com.xuntai.xtems.framework.schedule.SchedulerJob#paused()
     */
    public void pause() {
        paused = true;
    }

    /**
     * 
     * (non-Javadoc)
     * 
     * @see com.xuntai.xtems.framework.schedule.SchedulerJob#resume()
     */
    public void resume() {
        paused = false;
    }
    
    public void setJobName(String jobName){
    	this.jobName=jobName;
    }
    
    public void setJobGroupName(String jobGroupName){
    	this.jobGroupName=jobGroupName;
    }

}
