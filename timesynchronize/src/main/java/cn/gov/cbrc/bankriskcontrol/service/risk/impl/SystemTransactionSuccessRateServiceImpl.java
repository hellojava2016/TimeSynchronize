package cn.gov.cbrc.bankriskcontrol.service.risk.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.gov.cbrc.bankriskcontrol.dao.risk.ReportAdditionalDao;
import cn.gov.cbrc.bankriskcontrol.dao.risk.SystemTransactionSuccessRateDao;
import cn.gov.cbrc.bankriskcontrol.dto.AnalyseQueryParam;
import cn.gov.cbrc.bankriskcontrol.dto.ReportQueryParam;
import cn.gov.cbrc.bankriskcontrol.dto.ReportResultException;
import cn.gov.cbrc.bankriskcontrol.entity.ExtraReportInfo;
import cn.gov.cbrc.bankriskcontrol.entity.SystemTransactionSuccessRate;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.risk.SystemTransactionSuccessRateService;
import cn.gov.cbrc.bankriskcontrol.service.system.RiskCategoryService;
import cn.gov.cbrc.bankriskcontrol.util.Page;
import cn.gov.cbrc.bankriskcontrol.util.RiskUtils;

@Service("systemTransactionSuccessRateService")
@Transactional
public class SystemTransactionSuccessRateServiceImpl implements
		SystemTransactionSuccessRateService {
	@Autowired
	private SystemTransactionSuccessRateDao systemTransactionSuccessRateDao;
	
	@Autowired
	private ReportAdditionalDao reportAdditionalDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RiskCategoryService riskCategoryService;
	
	@Override
	public void reportSystemTransactionSuccessRates(List<SystemTransactionSuccessRate> rates) {
		for(SystemTransactionSuccessRate rate:rates){
			addSystemTransactionSuccessRates(rate);
		}		
	}

	@Override
	public void addSystemTransactionSuccessRates(SystemTransactionSuccessRate rate) {
		// 判断指标是否允许上报
		if (!rate.getRiskCategory().getAllowReport()) {
			throw new ReportResultException(106, "指标不允许直接上报数据:" + rate.getRiskCategory().getRiskName());
		}
		rate.setReportDate(RiskUtils.getNewDate_end(rate.getRiskCategory(), rate.getReportDate()));
		// 判断重复上报，如果是重复上报则更新
		int peroid = RiskUtils.getPeroidByReportDate(rate.getRiskCategory(), rate.getReportDate());
		rate.setPeriod(peroid);
		int year = rate.getReportDate().getYear();
		rate.setDataYear(year);
		String riskType = rate.getRiskCode();
		String organ = rate.getOrgCode();
		Date now = new Date();
		SystemTransactionSuccessRate exist = systemTransactionSuccessRateDao.getUniqueEntityByPropNames(new String[] {
				"dataYear", "period", "riskCode", "orgCode" }, new Object[] { year, peroid, riskType, organ });
		if (exist != null) {
			exist.copyReportDatas(rate);
			systemTransactionSuccessRateDao.update(exist);
			return;
		}
		boolean isExtra = RiskUtils.checkRiskExtraAndValid(reportAdditionalDao, rate.getOrganization(),
				rate.getRiskCategory(), rate.getPeriod(), rate.isExtral(), now,rate.getReportDate());
		rate.setExtral(isExtra);
		systemTransactionSuccessRateDao.save(rate);
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
	public void deleteSystemTransactionSuccessRate(long id) {
		systemTransactionSuccessRateDao.delete(id);
	}

	@Override
	public Page<SystemTransactionSuccessRate> getSystemTransactionSuccessRates(
			ReportQueryParam param, Page<SystemTransactionSuccessRate> page,boolean riskCodeLike,boolean checkControl) {		
		return systemTransactionSuccessRateDao.findPage(page, RiskUtils.getHqlByQueryParam(param,userService,SystemTransactionSuccessRate.class,riskCategoryService,riskCodeLike,checkControl));
	}

	@Override
	public Page<SystemTransactionSuccessRate> getSystemTransactionSuccessRates(
			AnalyseQueryParam param, Page<SystemTransactionSuccessRate> page) {
		// TODO Auto-generated method stub
		return systemTransactionSuccessRateDao.findPage(page, RiskUtils.getHqlByQueryParam(param,userService,SystemTransactionSuccessRate.class,riskCategoryService));
	}
}
