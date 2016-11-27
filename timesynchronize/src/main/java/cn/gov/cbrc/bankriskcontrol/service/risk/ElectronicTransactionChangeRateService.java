package cn.gov.cbrc.bankriskcontrol.service.risk;

import java.util.List;

import cn.gov.cbrc.bankriskcontrol.dto.AnalyseQueryParam;
import cn.gov.cbrc.bankriskcontrol.dto.ReportQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.ElectronicTransactionChangeRate;
import cn.gov.cbrc.bankriskcontrol.util.Page;

public interface ElectronicTransactionChangeRateService {
	public void addElectronicTransactionChangeRates(ElectronicTransactionChangeRate rate);

	public void deleteElectronicTransactionChangeRate(long id);

	public Page<ElectronicTransactionChangeRate> getElectronicTransactionChangeRates(
			ReportQueryParam param, Page<ElectronicTransactionChangeRate> page,boolean riskCodeLike,boolean checkControl);
	
	public Page<ElectronicTransactionChangeRate> getElectronicTransactionChangeRates(
			AnalyseQueryParam param, Page<ElectronicTransactionChangeRate> page);
	
	
	public void reportElectronicTransactionChangeRates(List<ElectronicTransactionChangeRate> rates);
}
