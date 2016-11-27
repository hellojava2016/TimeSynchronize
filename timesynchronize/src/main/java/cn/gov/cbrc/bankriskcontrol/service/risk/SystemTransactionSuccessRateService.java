package cn.gov.cbrc.bankriskcontrol.service.risk;

import java.util.List;

import cn.gov.cbrc.bankriskcontrol.dto.AnalyseQueryParam;
import cn.gov.cbrc.bankriskcontrol.dto.ReportQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.SystemTransactionSuccessRate;
import cn.gov.cbrc.bankriskcontrol.util.Page;

public interface SystemTransactionSuccessRateService {
public void addSystemTransactionSuccessRates(SystemTransactionSuccessRate rate);
	
	public void deleteSystemTransactionSuccessRate(long id);
	
	public Page<SystemTransactionSuccessRate> getSystemTransactionSuccessRates(ReportQueryParam param,Page<SystemTransactionSuccessRate> page,boolean riskCodeLike,boolean checkControl);
	
	public Page<SystemTransactionSuccessRate> getSystemTransactionSuccessRates(AnalyseQueryParam param,Page<SystemTransactionSuccessRate> page);
	
	public void reportSystemTransactionSuccessRates(List<SystemTransactionSuccessRate> rates);
}
