package cn.gov.cbrc.bankriskcontrol.service.risk.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.gov.cbrc.bankriskcontrol.dao.risk.ElectronicActiveUserChangeRateDao;
import cn.gov.cbrc.bankriskcontrol.dao.risk.ReportAdditionalDao;
import cn.gov.cbrc.bankriskcontrol.dto.AnalyseQueryParam;
import cn.gov.cbrc.bankriskcontrol.dto.ReportQueryParam;
import cn.gov.cbrc.bankriskcontrol.dto.ReportResultException;
import cn.gov.cbrc.bankriskcontrol.entity.ElectronicActiveUserChangeRate;
import cn.gov.cbrc.bankriskcontrol.entity.ExtraReportInfo;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.risk.ElectronicActiveUserChangeRateService;
import cn.gov.cbrc.bankriskcontrol.service.system.RiskCategoryService;
import cn.gov.cbrc.bankriskcontrol.util.Page;
import cn.gov.cbrc.bankriskcontrol.util.RiskUtils;

@Service("electronicActiveUserChangeRateService")
@Transactional
public class ElectronicActiveUserChangeRateServiceImpl implements
		ElectronicActiveUserChangeRateService {
	@Autowired
	private ElectronicActiveUserChangeRateDao electronicActiveUserChangeRateDao;
	
	@Autowired
	private ReportAdditionalDao reportAdditionalDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RiskCategoryService riskCategoryService;
	
	@Override
	public void reportElectronicActiveUserChangeRates(List<ElectronicActiveUserChangeRate> rates) {
		for(ElectronicActiveUserChangeRate rate:rates){
			addElectronicActiveUserChangeRates(rate);
		}		
	}

	@Override
	public void addElectronicActiveUserChangeRates(ElectronicActiveUserChangeRate rate) {
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
		ElectronicActiveUserChangeRate exist = electronicActiveUserChangeRateDao.getUniqueEntityByPropNames(
				new String[] { "dataYear", "period", "riskCode", "orgCode" }, new Object[] { year, peroid, riskType,
						organ });
		if (exist != null) {
			exist.copyReportDatas(rate);
			electronicActiveUserChangeRateDao.update(exist);
			ElectronicActiveUserChangeRate nextRate = electronicActiveUserChangeRateDao.getUniqueEntityByPropNames(
					new String[] { "dataYear", "period", "riskCode", "orgCode" }, new Object[] { year, peroid+1, riskType,
							organ });
			if(nextRate!=null){
				nextRate.setLastActiveUser(rate.getCurrentActiveUser());
				electronicActiveUserChangeRateDao.update(nextRate);
			}
			return;
		}
		boolean isExtra = RiskUtils.checkRiskExtraAndValid(reportAdditionalDao, rate.getOrganization(),
				rate.getRiskCategory(), rate.getPeriod(), rate.isExtral(), now,rate.getReportDate());
		rate.setExtral(isExtra);
		//获取上期指标的数值以以便于后续报表
		ElectronicActiveUserChangeRate lastRate = electronicActiveUserChangeRateDao.getUniqueEntityByPropNames(
				new String[] { "dataYear", "period", "riskCode", "orgCode" }, new Object[] { year, peroid-1, riskType,
						organ });
		if(lastRate!=null){
			rate.setLastActiveUser(lastRate.getCurrentActiveUser());
		}
		ElectronicActiveUserChangeRate nextRate = electronicActiveUserChangeRateDao.getUniqueEntityByPropNames(
				new String[] { "dataYear", "period", "riskCode", "orgCode" }, new Object[] { year, peroid+1, riskType,
						organ });
		if(nextRate!=null){
			nextRate.setLastActiveUser(rate.getCurrentActiveUser());
			electronicActiveUserChangeRateDao.update(nextRate);
		}
		electronicActiveUserChangeRateDao.save(rate);
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
	public void deleteElectronicActiveUserChangeRate(long id) {
		electronicActiveUserChangeRateDao.delete(id);
	}

	@Override
	public Page<ElectronicActiveUserChangeRate> getElectronicActiveUserChangeRates(
			ReportQueryParam param, Page<ElectronicActiveUserChangeRate> page,boolean riskCodeLike,boolean checkControl) {
		return electronicActiveUserChangeRateDao.findPage(page, RiskUtils.getHqlByQueryParam(param,userService,ElectronicActiveUserChangeRate.class,riskCategoryService,riskCodeLike,checkControl));
	}

	@Override
	public Page<ElectronicActiveUserChangeRate> getElectronicActiveUserChangeRates(
			AnalyseQueryParam param, Page<ElectronicActiveUserChangeRate> page) {
		return electronicActiveUserChangeRateDao.findPage(page, RiskUtils.getHqlByQueryParam(param,userService,ElectronicActiveUserChangeRate.class,riskCategoryService));
		}
}
