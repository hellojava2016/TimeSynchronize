package com.shtitan.timesynchronize.service.system.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shtitan.timesynchronize.dao.risk.RiskCategoryDao;
import com.shtitan.timesynchronize.dto.ReportQueryParam;
import com.shtitan.timesynchronize.entity.RiskCategory;
import com.shtitan.timesynchronize.entity.SystemAvailableRate;
import com.shtitan.timesynchronize.service.system.RiskCategoryService;
import com.shtitan.timesynchronize.util.ConvertUtils;
import com.shtitan.timesynchronize.util.Page;
import com.shtitan.timesynchronize.util.RiskUtils;
import com.shtitan.timesynchronize.util.PropertyFilter.MatchType;


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
