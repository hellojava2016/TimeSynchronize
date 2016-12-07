package com.shtitan.timesynchronize.scheduler;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

/**
 * <p>
 * Title: ProxyJob
 * </p>
 * <p>
 * Description: 能记录运行状态的job
 * </p>
 * 
 * @author yangtao
 * @created 2009-2-18 下午12:47:24
 * @modified [who date description]
 * @check [who date description]
 */
final public class ProxyJob implements StatefulJob {

    public static final String SCHEDULER_JOB = "SCHEDULER_JOB";

    /**
     * 
     * (non-Javadoc)
     * 
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SchedulerJob schedulerJob = (SchedulerJob) context.getJobDetail().getJobDataMap().get(SCHEDULER_JOB);
        if(schedulerJob==null)
            return;
        if (schedulerJob.isPaused())
            return;
        schedulerJob.execute();
    }

}