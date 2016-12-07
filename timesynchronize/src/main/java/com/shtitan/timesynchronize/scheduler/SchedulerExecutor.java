package com.shtitan.timesynchronize.scheduler;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.shtitan.timesynchronize.exception.SchException;



/**
 * <p>
 * Title: SchedulerService
 * </p>
 * <p>
 * Description: 任务调度管理器
 * </p>
 * 
 * @author yangtao
 * @created 2010-2-18 上午09:40:46
 * @modified [who date description]
 * @check [who date description]
 */
final public class SchedulerExecutor {

    private static SchedulerExecutor schedulerService;

    public static Scheduler scheduler;

    /**
     * 获取SchedulerService实例
     * 
     * @return
     * @throws SchedulerException
     */
    public synchronized static SchedulerExecutor getInstance() {
        if (schedulerService == null) {
            schedulerService = new SchedulerExecutor();
            SchedulerFactory schedFact = new StdSchedulerFactory();
            try {
                scheduler = schedFact.getScheduler();
                scheduler.start();
            } catch (SchedulerException e) {
                SchException ex = new SchException(
                        SchException.INIT_SCHEDULER_JOB, e);
                throw ex;
            }

        }
        return schedulerService;
    }

    /**
     * 启动调度任务
     * 
     * @param scheduleJob
     */
    public void startScheduleJob(final SchedulerJob schedulerJob) {
        if (schedulerJob == null)
            throw new NullPointerException();
        
        String jobGroupName = AbstractSchedulerJob.DEFAULT_GROUP;
        schedulerJob.setJobGroupName(jobGroupName);
        String jobName = SchedulerJobNamingEngine.createSchedulerJobName();
        schedulerJob.setJobName(jobName);
        
        JobDetail jobDetail = new JobDetail(schedulerJob.getJobName(),
                schedulerJob.getJobGroupName(), ProxyJob.class);
        jobDetail.getJobDataMap().put(ProxyJob.SCHEDULER_JOB, schedulerJob);
        try {
            schedulerJob.init();// 初始化调度资源
            final Trigger trigger=schedulerJob.getTrigger();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            SchException ex = new SchException(
                    SchException.START_SCHEDULER_JOB, e, schedulerJob
                            .getJobGroupName(), schedulerJob.getJobName());
            throw ex;
        }

    }

    /**
     * 获取调度任务
     * 
     * @param groupName
     *            任务组名称
     * @param jobName
     *            任务名称
     * @return
     */
    public SchedulerJob getSchedulerJob(String jobName, String groupName) {
        try {
            JobDetail jobDetail = scheduler.getJobDetail(jobName, groupName);
            if (jobDetail == null)
                return null;
            SchedulerJob schedulerJob = (SchedulerJob) jobDetail.getJobDataMap().get(ProxyJob.SCHEDULER_JOB);
            return schedulerJob;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 关闭调度jobName务
     * 
     * @param jobName
     *            任务名称;
     */
    public void shutdownSchedulerJob(String jobName, String groupName) {
        if (jobName == null)
            throw new NullPointerException();
        try {
            scheduler.deleteJob(jobName, groupName);
        } catch (SchedulerException e) {
            SchException ex = new SchException(
                    SchException.SHUTDOWN_SCHEDULER_JOB, e, groupName, jobName);
            throw ex;
        }
    }

    /**
     * 暂停调度任务
     * 
     * @param jobName
     */
    public void pauseSchedulerJob(String jobName, String groupName) {
        if (jobName == null)
            throw new NullPointerException();
        try {
            SchedulerJob schedulerJob = getSchedulerJob(jobName, groupName);
            if (schedulerJob == null)
                return;
            schedulerJob.pause();
        } catch (Exception e) {
            SchException ex = new SchException(
                    SchException.PAUSE_SCHEDULER_JOB, e, groupName, jobName);
            throw ex;
        }
    }

    /**
     * 重新设备调度任务
     * 
     * @param scheduleJob
     */
	public void resetSchedulerJob(final SchedulerJob schedulerJob) {
        if (schedulerJob == null)
            throw new NullPointerException();
        try {
        	Trigger trigger=scheduler.getTrigger(schedulerJob.getJobName(),schedulerJob.getJobGroupName());
        	scheduler.unscheduleJob(trigger.getJobName(), trigger.getJobGroup());
        	startScheduleJob(schedulerJob);
        } catch (Exception e) {
            SchException ex = new SchException(
                    SchException.RESET_SCHEDULER_JOB, e, schedulerJob
                            .getJobGroupName(), schedulerJob.getJobName());
            throw ex;
        }// 撤销任务
    }

    /**
     * 恢复调度任务
     * 
     * @param jobName
     */
    public void resumeSchedulerJob(String jobName, String jobGroupName) {
        if (jobName == null)
            throw new NullPointerException();
        try {
            SchedulerJob schedulerJob = getSchedulerJob(jobName, jobGroupName);
            if (schedulerJob == null)
                return;
            schedulerJob.resume();
        } catch (Exception e) {
            SchException ex = new SchException(
                    SchException.RESUME_SCHEDULER_JOB, e, jobGroupName, jobName);
            throw ex;
        }

    }

    /**
     * 调度任务是否关闭
     * 
     * @param jobName
     *            任务名称
     * @return
     */
    public boolean isShutdown(String jobName, String jobGroupName) {
        try {
            return scheduler.getJobDetail(jobName, jobGroupName) == null;
        } catch (SchedulerException e) {
            SchException ex = new SchException(
                    SchException.COMMON_SCHEDULER_JOB, e, jobGroupName, jobName);
            throw ex;
        }

    }

}
