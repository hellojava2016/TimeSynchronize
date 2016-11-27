package com.shtitan.timesynchronize.service.risk;

import java.util.List;

import com.shtitan.timesynchronize.dto.AnalyseQueryParam;
import com.shtitan.timesynchronize.dto.ReportQueryParam;
import com.shtitan.timesynchronize.entity.SystemAvailableRate;
import com.shtitan.timesynchronize.util.Page;


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
