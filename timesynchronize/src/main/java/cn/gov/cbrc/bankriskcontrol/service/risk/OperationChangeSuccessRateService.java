package cn.gov.cbrc.bankriskcontrol.service.risk;

import java.util.List;

import cn.gov.cbrc.bankriskcontrol.dto.AnalyseQueryParam;
import cn.gov.cbrc.bankriskcontrol.dto.ReportQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.OperationChangeSuccessRate;
import cn.gov.cbrc.bankriskcontrol.util.Page;

public interface OperationChangeSuccessRateService {
public void addOperationChangeSuccessRates(OperationChangeSuccessRate rate);
	
	public void deleteOperationChangeSuccessRate(long id);

	public Page<OperationChangeSuccessRate> getOperationChangeSuccessRates(ReportQueryParam param,Page<OperationChangeSuccessRate> page,boolean riskCodeLike,boolean checkControl);
	
	public Page<OperationChangeSuccessRate> getOperationChangeSuccessRates(AnalyseQueryParam param,Page<OperationChangeSuccessRate> page);
	
	public void reportOperationChangeSuccessRates(List<OperationChangeSuccessRate> rates);
}
