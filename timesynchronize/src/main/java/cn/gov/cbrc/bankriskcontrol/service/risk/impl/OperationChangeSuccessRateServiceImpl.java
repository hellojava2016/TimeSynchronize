package cn.gov.cbrc.bankriskcontrol.service.risk.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.gov.cbrc.bankriskcontrol.dao.risk.OperationChangeSuccessRateDao;
import cn.gov.cbrc.bankriskcontrol.dao.risk.ReportAdditionalDao;
import cn.gov.cbrc.bankriskcontrol.dto.AnalyseQueryParam;
import cn.gov.cbrc.bankriskcontrol.dto.ReportQueryParam;
import cn.gov.cbrc.bankriskcontrol.dto.ReportResultException;
import cn.gov.cbrc.bankriskcontrol.entity.ExtraReportInfo;
import cn.gov.cbrc.bankriskcontrol.entity.OperationChangeSuccessRate;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.risk.OperationChangeSuccessRateService;
import cn.gov.cbrc.bankriskcontrol.service.system.RiskCategoryService;
import cn.gov.cbrc.bankriskcontrol.util.Page;
import cn.gov.cbrc.bankriskcontrol.util.RiskUtils;

@Service("operationChangeSuccessRateService")
@Transactional
public class OperationChangeSuccessRateServiceImpl implements
		OperationChangeSuccessRateService {
	@Autowired
	private OperationChangeSuccessRateDao operationChangeSuccessRateDao;
	
	@Autowired
	private ReportAdditionalDao reportAdditionalDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RiskCategoryService riskCategoryService;
	
	@Override
	public void reportOperationChangeSuccessRates(List<OperationChangeSuccessRate> rates) {
		for(OperationChangeSuccessRate rate:rates){
			addOperationChangeSuccessRates(rate);
		}		
	}
	
	@Override
	public void addOperationChangeSuccessRates(OperationChangeSuccessRate rate) {
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
		OperationChangeSuccessRate exist = operationChangeSuccessRateDao.getUniqueEntityByPropNames(new String[] {
				"dataYear", "period", "riskCode", "orgCode" }, new Object[] { year, peroid, riskType, organ });
		if (exist != null) {
			exist.copyReportDatas(rate);
			operationChangeSuccessRateDao.update(exist);
			return;
		}
		boolean isExtra = RiskUtils.checkRiskExtraAndValid(reportAdditionalDao, rate.getOrganization(),
				rate.getRiskCategory(), rate.getPeriod(), rate.isExtral(), now,rate.getReportDate());
		rate.setExtral(isExtra);
		operationChangeSuccessRateDao.save(rate);
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
	public void deleteOperationChangeSuccessRate(long id) {
		operationChangeSuccessRateDao.delete(id);		
	}

	@Override
	public Page<OperationChangeSuccessRate> getOperationChangeSuccessRates(
			ReportQueryParam param, Page<OperationChangeSuccessRate> page,boolean riskCodeLike,boolean checkControl) {
		return operationChangeSuccessRateDao.findPage(page, RiskUtils.getHqlByQueryParam(param,userService,OperationChangeSuccessRate.class,riskCategoryService,riskCodeLike,checkControl));
	}
	
	@Override
	public Page<OperationChangeSuccessRate> getOperationChangeSuccessRates(AnalyseQueryParam param,
			Page<OperationChangeSuccessRate> page) {
		return operationChangeSuccessRateDao.findPage(page, RiskUtils.getHqlByQueryParam(param,userService,OperationChangeSuccessRate.class,riskCategoryService));
	}
}
