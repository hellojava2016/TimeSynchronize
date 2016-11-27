package cn.gov.cbrc.bankriskcontrol.service.system.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.gov.cbrc.bankriskcontrol.dao.risk.RiskCategoryDao;
import cn.gov.cbrc.bankriskcontrol.dto.ReportQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.ElectronicActiveUserChangeRate;
import cn.gov.cbrc.bankriskcontrol.entity.ElectronicTransactionChangeRate;
import cn.gov.cbrc.bankriskcontrol.entity.FakeSiteAttachmentRate;
import cn.gov.cbrc.bankriskcontrol.entity.InfoTechnologyRiskEventCount;
import cn.gov.cbrc.bankriskcontrol.entity.OperationChangeSuccessRate;
import cn.gov.cbrc.bankriskcontrol.entity.OutsideAttackChangeRate;
import cn.gov.cbrc.bankriskcontrol.entity.RiskCategory;
import cn.gov.cbrc.bankriskcontrol.entity.SystemAvailableRate;
import cn.gov.cbrc.bankriskcontrol.entity.SystemTransactionSuccessRate;
import cn.gov.cbrc.bankriskcontrol.service.system.RiskCategoryService;
import cn.gov.cbrc.bankriskcontrol.util.ConvertUtils;
import cn.gov.cbrc.bankriskcontrol.util.Page;
import cn.gov.cbrc.bankriskcontrol.util.PropertyFilter.MatchType;
import cn.gov.cbrc.bankriskcontrol.util.RiskUtils;

@Service("riskCategoryService")
@Transactional
public class RiskCategoryServiceImpl implements RiskCategoryService {
	@Autowired
	private RiskCategoryDao riskCategoryDao;

	@Override
	public void addRiskCategory(RiskCategory additional) {
		riskCategoryDao.save(additional);
	}

	@Override
	public void deleteRiskCategoryById(long id) {
		riskCategoryDao.delete(id);
	}

	@Override
	public void updateRiskCategory(RiskCategory additional) {
		riskCategoryDao.update(additional);
	}

	@Override
	public Page<RiskCategory> getRiskCategorys(ReportQueryParam param,
			Page<RiskCategory> page) {
		return riskCategoryDao.getAll(page);
	}

	@Override
	public RiskCategory getRiskCategoryById(long id) {
		return riskCategoryDao.get(id);
	}
	
	@Override
	public RiskCategory getRiskCategoryByCode(String code){
		return riskCategoryDao.getUniqueEntityByPropNames(new String[]{"riskCode"}, new Object[]{code});
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public RiskCategory getRiskCategoryByRiskClass( Class riskClass) {
		Map<Class,String> map=new HashMap<Class,String>();
		map.put(SystemAvailableRate.class, "1101");
		map.put(SystemTransactionSuccessRate.class, "2001");
		map.put(OperationChangeSuccessRate.class, "3001");
		map.put(FakeSiteAttachmentRate.class, "4001");
		map.put(OutsideAttackChangeRate.class, "5001");
		map.put(InfoTechnologyRiskEventCount.class, "6001001");
		map.put(ElectronicTransactionChangeRate.class, "7001");
		map.put(ElectronicActiveUserChangeRate.class, "8001");
		return getRiskCategoryByCode(map.get(riskClass));
	}
	
	public Map<String, List<RiskCategory>> getChildren(String code) {
		List<RiskCategory> list;
		if (StringUtils.isEmpty(code)) {
			list = riskCategoryDao.getAll();
		} else {
			list = riskCategoryDao.findBy("riskCode", code,
					MatchType.LIKE_START);
			//去掉自身
			for (Iterator<RiskCategory> iterator = list.iterator(); iterator
					.hasNext();) {
				RiskCategory rc = iterator.next();
				if (rc.getRiskCode().equals(code)) {
					iterator.remove();
					break;
				}
			}
		}
		Map<String, List<RiskCategory>> codeChildMap = new HashMap<String, List<RiskCategory>>();
		for (RiskCategory rc : list) {
			String rcode=rc.getRiskCode();
			String pcode=RiskUtils.getParentCode(rcode);			
			if(codeChildMap.containsKey(pcode)){
				codeChildMap.get(pcode).add(rc);
			}else{
				codeChildMap.put(pcode, ConvertUtils.newArrayList(rc));
			}
		}
		return codeChildMap;
	}
	
	@Override
	public List<RiskCategory> getChildRiskCategorys(String code) {
		List<RiskCategory> list = riskCategoryDao.getAll();
		List<RiskCategory> tempList=new ArrayList<RiskCategory>();
		for (RiskCategory rc : list) {
			String rcode = rc.getRiskCode();
			if (RiskUtils.getParentCode(rcode).equals(code)) {
               tempList.add(rc);
			}
		}
		return tempList;
	}

	@Override
	public List<RiskCategory> getAllRiskCategorys() {
		return riskCategoryDao.getAll();
	}
	
	@Override
	public List<RiskCategory> getChildRiskCategoryByTopCode(String riskCode) {
		return riskCategoryDao.find("select distinct rc from RiskCategory rc where rc.riskCode like '"+riskCode+"%' order by rc.riskCode asc");
	}
}
