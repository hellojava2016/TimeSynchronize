package com.shtitan.timesynchronize.scheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import com.shtitan.timesynchronize.util.SysUtils;

/**
 * 
 * @author liming
 */
public abstract class AbstactCustomizeSchedulerTask extends CommonSchedulerJob {

	public AbstactCustomizeSchedulerTask() {
		super();
	}

	public AbstactCustomizeSchedulerTask(String cronTrigger) {
		super(cronTrigger);
	}

	/**
	 * cronTriggerExpr 格式 1. Seconds 2. Minutes 3. Hours 4. Day-of-Month 5.
	 * Month 6. Day-of-Week 7. Year (可选字段)
	 * 
	 * @param dayOfWeek
	 * @param cronTriggerExpr
	 *            如果pollingType=0；格式为"分，秒"
	 * @param pollingType
	 *            0：间隔；1：指定时间
	 */
	public void initRunningParam(String cronTriggerExpr, int pollType) {
		if (pollType == 0 && cronTriggerExpr.split(",").length == 2) {
			int m = Integer.parseInt(cronTriggerExpr.split(",")[0]);
			int s = Integer.parseInt(cronTriggerExpr.split(",")[1]);
			if (m == 0 && s > 0) {
				cronTriggerExpr = "0/" + s + " * * * * ?";
			} else if (m > 0 && s == 0) {
				cronTriggerExpr = "0 */" + m + " * * * ?";
			} else if (m > 0 && s > 0) {
				cronTriggerExpr = "0/" + s + " 0/" + m + " * * * ?";
			} else {
				return;
			}
		} else if (pollType == 0 && SysUtils.isDigit(cronTriggerExpr, 0)) {
			cronTriggerExpr = "* 0/" + (Integer.parseInt(cronTriggerExpr)) + " * * * ?";
		}
		super.setCronTrigger(cronTriggerExpr);
	}

	@Override
	public abstract void execute();

	public boolean getIsStarted() {
		return !SchedulerExecutor.getInstance().isShutdown(getJobName(),
				getJobGroupName());
	}

	public void start() {
		if (getIsStarted()) {
			SchedulerExecutor.getInstance().shutdownSchedulerJob(getJobName(), getJobGroupName());
			SchedulerExecutor.getInstance().startScheduleJob(this);
		} else {
			SchedulerExecutor.getInstance().startScheduleJob(this);
		}
	}

	public void stop() {
		if (getIsStarted()) {
			SchedulerExecutor.getInstance().shutdownSchedulerJob(getJobName(),
					getJobGroupName());
		}
	}

	public void shutdownPollExecutor(ExecutorService executor) {
		executor.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
				executor.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!executor.awaitTermination(60, TimeUnit.SECONDS))
					System.err.println("Pool did not terminate");
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			executor.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}

}
