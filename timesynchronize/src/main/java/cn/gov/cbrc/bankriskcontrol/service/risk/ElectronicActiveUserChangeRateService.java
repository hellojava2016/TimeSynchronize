package cn.gov.cbrc.bankriskcontrol.service.risk;

import java.util.List;

import cn.gov.cbrc.bankriskcontrol.dto.AnalyseQueryParam;
import cn.gov.cbrc.bankriskcontrol.dto.ReportQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.ElectronicActiveUserChangeRate;
import cn.gov.cbrc.bankriskcontrol.util.Page;

public interface ElectronicActiveUserChangeRateService {
	public void addElectronicActiveUserChangeRates(ElectronicActiveUserChangeRate rate);

	public void deleteElectronicActiveUserChangeRate(long id);

	public Page<ElectronicActiveUserChangeRate> getElectronicActiveUserChangeRates(
			ReportQueryParam param, Page<ElectronicActiveUserChangeRate> page,boolean riskCodeLike,boolean checkControl);
	
	public Page<ElectronicActiveUserChangeRate> getElectronicActiveUserChangeRates(
			AnalyseQueryParam param, Page<ElectronicActiveUserChangeRate> page);
	
	public void reportElectronicActiveUserChangeRates(List<ElectronicActiveUserChangeRate> rates);
}
