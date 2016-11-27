package cn.gov.cbrc.bankriskcontrol.service.risk.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.gov.cbrc.bankriskcontrol.dao.risk.OutsideAttackChangeRateDao;
import cn.gov.cbrc.bankriskcontrol.dao.risk.ReportAdditionalDao;
import cn.gov.cbrc.bankriskcontrol.dto.AnalyseQueryParam;
import cn.gov.cbrc.bankriskcontrol.dto.ReportQueryParam;
import cn.gov.cbrc.bankriskcontrol.dto.ReportResultException;
import cn.gov.cbrc.bankriskcontrol.entity.ExtraReportInfo;
import cn.gov.cbrc.bankriskcontrol.entity.OutsideAttackChangeRate;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.risk.OutsideAttackChangeRateService;
import cn.gov.cbrc.bankriskcontrol.service.system.RiskCategoryService;
import cn.gov.cbrc.bankriskcontrol.util.Page;
import cn.gov.cbrc.bankriskcontrol.util.RiskUtils;

@Service("outsideAttackChangeRateService")
@Transactional
public class OutsideAttackChangeRateServiceImpl implements
		OutsideAttackChangeRateService {
	@Autowired
	private OutsideAttackChangeRateDao outsideAttackChangeRateDao;
	
	@Autowired
	private ReportAdditionalDao reportAdditionalDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RiskCategoryService riskCategoryService;
	
	@Override
	public void reportOutsideAttackChangeRates(List<OutsideAttackChangeRate> rates) {
		for(OutsideAttackChangeRate rate:rates){
			addOutsideAttackChangeRates(rate);
		}		
	}

	@Override
	public void addOutsideAttackChangeRates(OutsideAttackChangeRate rate) {
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
		OutsideAttackChangeRate exist = outsideAttackChangeRateDao.getUniqueEntityByPropNames(new String[] {
				"dataYear", "period", "riskCode", "orgCode" }, new Object[] { year, peroid, riskType, organ });
		if (exist != null) {
			exist.copyReportDatas(rate);
			outsideAttackChangeRateDao.update(exist);
			//获取下期的，以保证本期更改后下期的数据也准确，便于后续报表
			OutsideAttackChangeRate nextRate = outsideAttackChangeRateDao.getUniqueEntityByPropNames(new String[] {
					"dataYear", "period", "riskCode", "orgCode" }, new Object[] { year, peroid+1, riskType, organ });
			if(nextRate!=null){
				nextRate.setLdswp(rate.getIdswp());
				nextRate.setLpswp(rate.getIpswp());
				outsideAttackChangeRateDao.update(nextRate);
			}
			return;
		}
		boolean isExtra = RiskUtils.checkRiskExtraAndValid(reportAdditionalDao, rate.getOrganization(),
				rate.getRiskCategory(), rate.getPeriod(), rate.isExtral(), now,rate.getReportDate());
		rate.setExtral(isExtra);
		//获取上期指标的数值以以便于后续报表
		OutsideAttackChangeRate lastRate = outsideAttackChangeRateDao.getUniqueEntityByPropNames(new String[] {
				"dataYear", "period", "riskCode", "orgCode" }, new Object[] { year, peroid-1, riskType, organ });
		if(lastRate!=null){
			rate.setLdswp(lastRate.getIdswp());
			rate.setLpswp(lastRate.getIpswp());
		}
		//获取下期的，以保证本期更改后下期的数据也准确，便于后续报表
		OutsideAttackChangeRate nextRate = outsideAttackChangeRateDao.getUniqueEntityByPropNames(new String[] {
				"dataYear", "period", "riskCode", "orgCode" }, new Object[] { year, peroid+1, riskType, organ });
		if(nextRate!=null){
			nextRate.setLdswp(rate.getIdswp());
			nextRate.setLpswp(rate.getIpswp());
			outsideAttackChangeRateDao.update(nextRate);
		}
		outsideAttackChangeRateDao.save(rate);
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
	public void deleteOutsideAttackChangeRate(long id) {
		outsideAttackChangeRateDao.delete(id);
	}

	@Override
	public Page<OutsideAttackChangeRate> getOutsideAttackChangeRates(
			ReportQueryParam param, Page<OutsideAttackChangeRate> page,boolean riskCodeLike,boolean checkControl) {
		return outsideAttackChangeRateDao.findPage(page, RiskUtils.getHqlByQueryParam(param,userService,OutsideAttackChangeRate.class,riskCategoryService,riskCodeLike,checkControl));
	}

	@Override
	public Page<OutsideAttackChangeRate> getOutsideAttackChangeRates(
			AnalyseQueryParam param, Page<OutsideAttackChangeRate> page) {
		return outsideAttackChangeRateDao.findPage(page, RiskUtils.getHqlByQueryParam(param,userService,OutsideAttackChangeRate.class,riskCategoryService));
	}
}
