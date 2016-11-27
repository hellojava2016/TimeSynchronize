package cn.gov.cbrc.bankriskcontrol.service.risk;

import cn.gov.cbrc.bankriskcontrol.dto.AnalyseQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.RiskTca;
import cn.gov.cbrc.bankriskcontrol.util.Page;

public interface RiskTcaService {
	public Page<RiskTca> getTcas(
			AnalyseQueryParam param, Page<RiskTca> page);
	
}
