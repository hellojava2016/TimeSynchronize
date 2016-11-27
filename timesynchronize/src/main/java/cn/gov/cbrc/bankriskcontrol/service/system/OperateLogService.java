package cn.gov.cbrc.bankriskcontrol.service.system;

import java.util.Date;

import cn.gov.cbrc.bankriskcontrol.entity.OperateLog;
import cn.gov.cbrc.bankriskcontrol.util.Page;

public interface OperateLogService {	
	public void addOperateLog(OperateLog log);
	
	public Page<OperateLog> getOperateLogs(long orgId,String action,Date startDate,Date endDate,
			Page<OperateLog> page);
}
