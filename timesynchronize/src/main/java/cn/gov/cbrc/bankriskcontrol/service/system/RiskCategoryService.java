package cn.gov.cbrc.bankriskcontrol.service.system;

import java.util.List;
import java.util.Map;

import cn.gov.cbrc.bankriskcontrol.dto.ReportQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.RiskCategory;
import cn.gov.cbrc.bankriskcontrol.util.Page;

public interface RiskCategoryService {	
	public void addRiskCategory(RiskCategory additional);
	
	public void deleteRiskCategoryById(long id);
	
	public void updateRiskCategory(RiskCategory additional);
	
	public Page<RiskCategory> getRiskCategorys(ReportQueryParam param,Page<RiskCategory> page);
	
	public RiskCategory getRiskCategoryById(long id);
	
	public RiskCategory getRiskCategoryByCode(String code);
	
	public Map<String,List<RiskCategory>> getChildren(String code);
	
	public RiskCategory getRiskCategoryByRiskClass(Class riskClass);
	
	public List<RiskCategory> getChildRiskCategorys(String riskCode);
	
	public List<RiskCategory> getAllRiskCategorys();
	
	public List<RiskCategory> getChildRiskCategoryByTopCode(String riskCode);
}
