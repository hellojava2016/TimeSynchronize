package com.shtitan.timesynchronize.service.risk.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shtitan.timesynchronize.dao.risk.ReportAdditionalDao;
import com.shtitan.timesynchronize.dao.risk.SystemAvailableRateDao;
import com.shtitan.timesynchronize.dto.AnalyseQueryParam;
import com.shtitan.timesynchronize.dto.ReportQueryParam;
import com.shtitan.timesynchronize.dto.ReportResultException;
import com.shtitan.timesynchronize.entity.ExtraReportInfo;
import com.shtitan.timesynchronize.entity.SystemAvailableRate;
import com.shtitan.timesynchronize.service.account.UserService;
import com.shtitan.timesynchronize.service.risk.SystemAvailableRateService;
import com.shtitan.timesynchronize.service.system.RiskCategoryService;
import com.shtitan.timesynchronize.util.ConvertUtils;
import com.shtitan.timesynchronize.util.DateUtil;
import com.shtitan.timesynchronize.util.Page;
import com.shtitan.timesynchronize.util.RiskUtils;


@Service("systemAvailableRateService")
@Transactional
public class SystemAvailableRateServiceImpl implements
		SystemAvailableRateService {
	@Autowired
	private SystemAvailableRateDao systemAvailableRateDao;
	
	private ReportAdditionalDao reportAdditionalDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RiskCategoryService riskCategoryService;
	
	@Override
	public void reportSystemAvailableRates(List<SystemAvailableRate> rates) {
		for(SystemAvailableRate rate:rates){
			addSystemAvailableRates(rate);
		}		
	}

	@Override
	public void addSystemAvailableRates(SystemAvailableRate rate) {
		//判断指标是否允许上报
		if(!rate.getRiskCategory().getAllowReport()){
			throw new ReportResultException(106,"指标不允许直接上报数据:"+rate.getRiskCategory().getRiskName());
		}
		rate.setReportDate(RiskUtils.getNewDate_end(rate.getRiskCategory(), rate.getReportDate()));
		//判断重复上报，如果是重复上报则更新
		int peroid=RiskUtils.getPeroidByReportDate(rate.getRiskCategory(), rate.getReportDate());
		rate.setPeriod(peroid);
		int year=rate.getReportDate().getYear();
		rate.setDataYear(year);		
		String riskType=rate.getRiskCode();
		String organ=rate.getOrgCode();
		Date now=new Date();
		SystemAvailableRate exist=systemAvailableRateDao.getUniqueEntityByPropNames(new String[]{"dataYear","period","riskCode","orgCode"}, new Object[]{year,peroid,riskType,organ});
		if(exist!=null){			
			exist.copyReportDatas(rate);
			systemAvailableRateDao.update(exist);
			return;
		}
		boolean isExtra=RiskUtils.checkRiskExtraAndValid(reportAdditionalDao, rate.getOrganization(), rate.getRiskCategory(), rate.getPeriod(), rate.isExtral(), now,rate.getReportDate());
		rate.setExtral(isExtra);
		systemAvailableRateDao.save(rate);
		// 更新补报记录
		if (isExtra) {
			ExtraReportInfo extra = reportAdditionalDao.getUniqueEntityByPropNames(new String[] { "organization",
					"riskCategory", "period" },
					new Object[] { rate.getOrganization(), rate.getRiskCategory(), rate.getPeriod() });
			extra.setReported(true);
			reportAdditionalDao.update(extra);
		}
	}

	@Override
	public void deleteSystemAvailableRate(long id) {
		systemAvailableRateDao.delete(id);
	}

	@Override
	public Page<SystemAvailableRate> getSystemAvailableRates(
			ReportQueryParam param, Page<SystemAvailableRate> page,boolean riskCodeLike,boolean checkControl) {
		return systemAvailableRateDao.findPage(page, RiskUtils.getHqlByQueryParam(param,userService,SystemAvailableRate.class,riskCategoryService,riskCodeLike,checkControl));
	}
	
	@Override
	public Page<SystemAvailableRate> getSystemAvailableRates(
			AnalyseQueryParam param, Page<SystemAvailableRate> page) {
		return systemAvailableRateDao.findPage(page, RiskUtils.getHqlByQueryParam(param,userService,SystemAvailableRate.class,riskCategoryService));
		}
	
	@Override
	public SystemAvailableRate getSystemAvailableRate(int year, int period, String riskCode) {
		return systemAvailableRateDao.findUnique("select distinct a from SystemAvailableRate a where a.dataYear='"+year+"' and a.period='"+period+"' and a.riskCode='"+riskCode+"'");		
	}

	@Override
	public List find(Class t,String hql) {
		if(t==SystemAvailableRate.class)
			return systemAvailableRateDao.find(hql);
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List findLastPeriod(Class t,int period,String hql){
		Object dao=null;
		if(t==SystemAvailableRate.class)
			dao=systemAvailableRateDao;
		Page page=new Page();
		page.setPageNo(1);
		page.setPageSize(period);
		String hql1="select distinct a.reportDate from "+t.getSimpleName()+" a order by a.reportDate desc";
		try {
			Method method1=dao.getClass().getMethod("findPage", new Class[]{String.class,Page.class});
			Page temp = (Page) method1.invoke(dao, new Object[]{hql1,page});
			List<Object> objectList=temp.getResult();
			List<String> dateList=new ArrayList<String>();
			for(Object object:objectList){
				dateList.add("'"+DateUtil.getLongDate((Date)object)+"'");
			}
			String dateStr=ConvertUtils.convertList2String(dateList, ",");
			String hql2="select distinct a from "+t.getSimpleName()+" a where a.reportDate in ("+dateStr+") "+hql;
			Method method2=dao.getClass().getMethod("find", new Class[]{String.class,Object[].class});
			return (List)method2.invoke(dao, new Object[]{hql2,null});
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList();
		} 		
	}
}
