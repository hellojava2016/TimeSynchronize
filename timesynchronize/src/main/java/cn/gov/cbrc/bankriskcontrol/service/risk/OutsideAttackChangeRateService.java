package cn.gov.cbrc.bankriskcontrol.service.risk;

import java.util.List;

import cn.gov.cbrc.bankriskcontrol.dto.AnalyseQueryParam;
import cn.gov.cbrc.bankriskcontrol.dto.ReportQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.OutsideAttackChangeRate;
import cn.gov.cbrc.bankriskcontrol.util.Page;

public interface OutsideAttackChangeRateService {
public void addOutsideAttackChangeRates(OutsideAttackChangeRate rate);
	
	public void deleteOutsideAttackChangeRate(long id);
	
	public Page<OutsideAttackChangeRate> getOutsideAttackChangeRates(ReportQueryParam param,Page<OutsideAttackChangeRate> page,boolean riskCodeLike,boolean checkControl);
	
	public Page<OutsideAttackChangeRate> getOutsideAttackChangeRates(AnalyseQueryParam param,Page<OutsideAttackChangeRate> page);

	public void reportOutsideAttackChangeRates(List<OutsideAttackChangeRate> rates);
}
