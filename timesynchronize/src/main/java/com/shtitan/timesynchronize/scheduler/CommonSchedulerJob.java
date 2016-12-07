package com.shtitan.timesynchronize.scheduler;
/*
 * $Id: CommonSchedulerJob.java, 2009-2-23 上午10:34:56 yangtao Exp $
 * 
 * Copyright (c) 2008 Wuhan Yangtze Optical Technology Co., Ltd 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by YOTC or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */

import java.text.ParseException;
import java.util.Date;

import org.quartz.CronTrigger;
import org.quartz.Trigger;

/**
 * <p>
 * Title: CommonSchedulerJob
 * </p>
 * <p>
 * Description: 通用调度任务;需要根据调度任务需要定义cronTrigger字符串; <br>
 * 表达式可以参考http://airdream.javaeye.com/blog/53472 <br>
 * "s m h d w y" <br>
 * 秒 0-59,分 0-59,小时 0-23,日期 1-31,月份 1-12,星期 1-7 年（可选）留空,1970-2099 <br>
 * 0/n表示每n个时间间隔执行一次，1,3,5,7,10表示第1,3,5,7,10时执行一次
 * </p>
 * 
 * @author yangtao
 * @created 2009-2-23 上午10:34:56
 * @modified [who date description]
 * @check [who date description]
 */
public abstract class CommonSchedulerJob extends AbstractSchedulerJob {

    private String cronTrigger = null;
    private Date startTime = null;
    private Date endTime = null;
    
    public CommonSchedulerJob(){
    	super();
    }

    /**
     * 
     * @param cronTrigger
     */
    public CommonSchedulerJob(String cronTrigger) {
        super();
        if (cronTrigger == null)
            throw new NullPointerException();
        this.cronTrigger = cronTrigger;
    }

    /**
     * 
     * @param startTime
     *            起始时间
     * @param endTime
     *            结束时间
     * @param cronTrigger
     */
    public CommonSchedulerJob(Date startTime, Date endTime, String cronTrigger) {
        super();
        if (startTime == null || endTime == null || cronTrigger == null)
            throw new NullPointerException();
        this.startTime = startTime;
        this.endTime = endTime;
        this.cronTrigger = cronTrigger;

    }
    
    public void setCronTrigger(String cronTrigger){
    	this.cronTrigger = cronTrigger;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.yotc.opview.framework.schedule.SchedulerJob#getTrigger()
     */
    @Override
    public Trigger getTrigger() {
        CronTrigger trigger;
        try {
            trigger = new CronTrigger(this.getJobName(), this.getJobGroupName(), cronTrigger);
            if (startTime != null)
                trigger.setStartTime(startTime);
            if (endTime != null)
                trigger.setEndTime(endTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return trigger;
    }

}
