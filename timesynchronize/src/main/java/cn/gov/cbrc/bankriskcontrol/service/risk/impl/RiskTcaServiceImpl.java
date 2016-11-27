package cn.gov.cbrc.bankriskcontrol.service.risk.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.gov.cbrc.bankriskcontrol.dao.risk.RiskTcaDao;
import cn.gov.cbrc.bankriskcontrol.dto.AnalyseQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.RiskTca;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.risk.RiskTcaService;
import cn.gov.cbrc.bankriskcontrol.service.system.RiskCategoryService;
import cn.gov.cbrc.bankriskcontrol.util.ConvertUtils;
import cn.gov.cbrc.bankriskcontrol.util.Page;

@Service("riskTcaService")
@Transactional
public class RiskTcaServiceImpl implements RiskTcaService {

	@Autowired
	private RiskTcaDao riskTcaDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RiskCategoryService riskCategoryService;
	
	@Override
	public Page<RiskTca> getTcas(AnalyseQueryParam param, Page<RiskTca> page) {		
		return riskTcaDao.findPage(page,getHql(param));
	}
	
	private String getHql(AnalyseQueryParam param){
		String hql = "select distinct a from RiskTca"+ " a";
		List<String> list = new ArrayList<String>();
		Date startDate = param.getStartDate();
		Date endDate = param.getEndDate();
		if(startDate!=null&&endDate!=null&&startDate.after(endDate))//如果起始日期大于截止日期，两者的period可能相同 为了避免歧义，还是不显示该期的查询结果
			return hql+" where 1=0";
		if (startDate != null) {
			list.add("a.reportDate>="+startDate);
		}
		if (endDate != null) {
			list.add("a.reportDate<="+endDate);
		}
		if(CollectionUtils.isNotEmpty(param.getDepartmentIdList())){
			list.add("a.org.orgId in ("+ConvertUtils.convertList2String(param.getDepartmentIdList(), ",")+")");
		}
		
		if(CollectionUtils.isNotEmpty(param.getReportTypeList())){
			list.add("a.riskCategory.riskCode='"+param.getReportTypeList().get(0)+"'");
		}
		
		if (StringUtils.isNoneBlank(param.getAreaCode())) {
			list.add("a.org.areaCity.areaCode like '" + param.getAreaCode() + "%'");
		}
		
		if (list.size() == 5) {
			hql = hql + " where " + list.get(0) + " and " + list.get(1) + " and " + list.get(2) + " and " + list.get(3)
					+ " and " + list.get(4);
		}
		if (list.size() == 4) {
			hql = hql + " where " + list.get(0) + " and " + list.get(1) + " and " + list.get(2) + " and " + list.get(3);
		}
		if (list.size() == 3) {
			hql = hql + " where " + list.get(0) + " and " + list.get(1) + " and " + list.get(2);
		}
		if (list.size() == 2) {
			hql = hql + " where " + list.get(0) + " and " + list.get(1);
		}
		if (list.size() == 1) {
			hql = hql + " where " + list.get(0);
		}
		return hql;
	}

}
