package cn.gov.cbrc.bankriskcontrol.service.risk.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.gov.cbrc.bankriskcontrol.dao.risk.ElectronicTransactionChangeRateDao;
import cn.gov.cbrc.bankriskcontrol.dao.risk.ReportAdditionalDao;
import cn.gov.cbrc.bankriskcontrol.dto.AnalyseQueryParam;
import cn.gov.cbrc.bankriskcontrol.dto.ReportQueryParam;
import cn.gov.cbrc.bankriskcontrol.dto.ReportResultException;
import cn.gov.cbrc.bankriskcontrol.entity.ElectronicTransactionChangeRate;
import cn.gov.cbrc.bankriskcontrol.entity.ExtraReportInfo;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.risk.ElectronicTransactionChangeRateService;
import cn.gov.cbrc.bankriskcontrol.service.system.RiskCategoryService;
import cn.gov.cbrc.bankriskcontrol.util.Page;
import cn.gov.cbrc.bankriskcontrol.util.RiskUtils;

@Service("electronicTransactionChangeRateService")
@Transactional
public class ElectronicTransactionChangeRateServiceImpl implements
		ElectronicTransactionChangeRateService {
	@Autowired
	private ElectronicTransactionChangeRateDao electronicTransactionChangeRateDao;
	
	@Autowired
	private ReportAdditionalDao reportAdditionalDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RiskCategoryService riskCategoryService;
	
	@Override
	public void reportElectronicTransactionChangeRates(List<ElectronicTransactionChangeRate> rates) {
		for(ElectronicTransactionChangeRate rate:rates){
			addElectronicTransactionChangeRates(rate);
		}		
	}

	@Override
	public void addElectronicTransactionChangeRates(ElectronicTransactionChangeRate rate) {
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
		ElectronicTransactionChangeRate exist = electronicTransactionChangeRateDao.getUniqueEntityByPropNames(
				new String[] { "dataYear", "period", "riskCode", "orgCode" }, new Object[] { year, peroid, riskType,
						organ });
		if (exist != null) {
			exist.copyReportDatas(rate);
			electronicTransactionChangeRateDao.update(exist);
			//获取下期的，以保证本期更改后下期的数据也准确，便于后续报表
			ElectronicTransactionChangeRate nextRate = electronicTransactionChangeRateDao.getUniqueEntityByPropNames(
					new String[] { "dataYear", "period", "riskCode", "orgCode" }, new Object[] { year, peroid+1, riskType,
							organ });
			if(nextRate!=null){
				nextRate.setLastTradingVolume(rate.getCurrentTradingVolume());
				electronicTransactionChangeRateDao.update(nextRate);
			}
			return;
		}
		boolean isExtra = RiskUtils.checkRiskExtraAndValid(reportAdditionalDao, rate.getOrganization(),
				rate.getRiskCategory(), rate.getPeriod(), rate.isExtral(), now,rate.getReportDate());
		rate.setExtral(isExtra);
		//获取上期指标的数值以以便于后续报表
		ElectronicTransactionChangeRate lastRate = electronicTransactionChangeRateDao.getUniqueEntityByPropNames(
				new String[] { "dataYear", "period", "riskCode", "orgCode" }, new Object[] { year, peroid-1, riskType,
						organ });
		if(lastRate!=null){
			rate.setLastTradingVolume(lastRate.getCurrentTradingVolume());
		}
		//获取下期的，以保证本期更改后下期的数据也准确，便于后续报表
		ElectronicTransactionChangeRate nextRate = electronicTransactionChangeRateDao.getUniqueEntityByPropNames(
				new String[] { "dataYear", "period", "riskCode", "orgCode" }, new Object[] { year, peroid+1, riskType,
						organ });
		if(nextRate!=null){
			nextRate.setLastTradingVolume(rate.getCurrentTradingVolume());
			electronicTransactionChangeRateDao.update(nextRate);
		}
		electronicTransactionChangeRateDao.save(rate);
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
	public void deleteElectronicTransactionChangeRate(long id) {
		electronicTransactionChangeRateDao.delete(id);
	}

	@Override
	public Page<ElectronicTransactionChangeRate> getElectronicTransactionChangeRates(
			ReportQueryParam param, Page<ElectronicTransactionChangeRate> page,boolean riskCodeLike,boolean checkControl) {
		return electronicTransactionChangeRateDao.findPage(page, RiskUtils.getHqlByQueryParam(param,userService,ElectronicTransactionChangeRate.class,riskCategoryService,riskCodeLike,checkControl));
	}

	@Override
	public Page<ElectronicTransactionChangeRate> getElectronicTransactionChangeRates(
			AnalyseQueryParam param,
			Page<ElectronicTransactionChangeRate> page) {
		return electronicTransactionChangeRateDao.findPage(page, RiskUtils.getHqlByQueryParam(param,userService,ElectronicTransactionChangeRate.class,riskCategoryService));
	}
}
