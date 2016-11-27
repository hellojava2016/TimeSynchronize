package cn.gov.cbrc.bankriskcontrol.service.risk;

import java.util.List;

import cn.gov.cbrc.bankriskcontrol.dto.AnalyseQueryParam;
import cn.gov.cbrc.bankriskcontrol.dto.ReportQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.InfoTechnologyRiskEventCount;
import cn.gov.cbrc.bankriskcontrol.util.Page;

public interface InfoTechnologyRiskEventCountService {
public void addInfoTechnologyRiskEventCounts(InfoTechnologyRiskEventCount rate);
	
	public void deleteInfoTechnologyRiskEventCount(long id);
	
	public Page<InfoTechnologyRiskEventCount> getInfoTechnologyRiskEventCounts(ReportQueryParam param,Page<InfoTechnologyRiskEventCount> page,boolean riskCodeLike,boolean checkControl);
	
	public Page<InfoTechnologyRiskEventCount> getInfoTechnologyRiskEventCounts(AnalyseQueryParam param,Page<InfoTechnologyRiskEventCount> page);
	
	public void reportInfoTechnologyRiskEventCounts(List<InfoTechnologyRiskEventCount> rates);
}
