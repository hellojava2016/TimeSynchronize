package cn.gov.cbrc.bankriskcontrol.service.risk;

import java.util.List;

import cn.gov.cbrc.bankriskcontrol.dto.AnalyseQueryParam;
import cn.gov.cbrc.bankriskcontrol.dto.ReportQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.SystemAvailableRate;
import cn.gov.cbrc.bankriskcontrol.util.Page;

public interface SystemAvailableRateService {
	public void addSystemAvailableRates(SystemAvailableRate rate);

	public void deleteSystemAvailableRate(long id);

	public Page<SystemAvailableRate> getSystemAvailableRates(
			ReportQueryParam param, Page<SystemAvailableRate> page,boolean riskCodeLike,boolean checkControl);
	
	public Page<SystemAvailableRate> getSystemAvailableRates(
			AnalyseQueryParam param, Page<SystemAvailableRate> page);
	
	public void reportSystemAvailableRates(List<SystemAvailableRate> rates);
	
	public SystemAvailableRate getSystemAvailableRate(int year,int period,String riskCode);
	
	public List find(Class t,String hql);
	
	public List findLastPeriod(Class t,int period,String hql);
}
