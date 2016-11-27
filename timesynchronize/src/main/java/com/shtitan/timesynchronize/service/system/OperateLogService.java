package com.shtitan.timesynchronize.service.system;

import java.util.Date;

import com.shtitan.timesynchronize.entity.OperateLog;
import com.shtitan.timesynchronize.util.Page;


public interface OperateLogService {	
	public void addOperateLog(OperateLog log);
	
	public Page<OperateLog> getOperateLogs(long orgId,String action,Date startDate,Date endDate,
			Page<OperateLog> page);
}
