package cn.gov.cbrc.bankriskcontrol.service.risk.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.gov.cbrc.bankriskcontrol.dao.risk.FakeSiteAttachmentRateDao;
import cn.gov.cbrc.bankriskcontrol.dao.risk.ReportAdditionalDao;
import cn.gov.cbrc.bankriskcontrol.dto.AnalyseQueryParam;
import cn.gov.cbrc.bankriskcontrol.dto.ReportQueryParam;
import cn.gov.cbrc.bankriskcontrol.dto.ReportResultException;
import cn.gov.cbrc.bankriskcontrol.entity.ExtraReportInfo;
import cn.gov.cbrc.bankriskcontrol.entity.FakeSiteAttachmentRate;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.risk.FakeSiteAttachmentRateService;
import cn.gov.cbrc.bankriskcontrol.service.system.RiskCategoryService;
import cn.gov.cbrc.bankriskcontrol.util.Page;
import cn.gov.cbrc.bankriskcontrol.util.RiskUtils;

@Service("fakeSiteAttachmentRateService")
@Transactional
public class FakeSiteAttachmentRateServiceImpl implements
		FakeSiteAttachmentRateService {
	@Autowired
	private FakeSiteAttachmentRateDao fakeSiteAttachmentRateDao;
	
	@Autowired
	private ReportAdditionalDao reportAdditionalDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RiskCategoryService riskCategoryService;
	
	@Override
	public void reportFakeSiteAttachmentRates(List<FakeSiteAttachmentRate> rates) {
		for(FakeSiteAttachmentRate rate:rates){
			addFakeSiteAttachmentRates(rate);
		}		
	}

	@Override
	public void addFakeSiteAttachmentRates(FakeSiteAttachmentRate rate) {
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
		FakeSiteAttachmentRate exist = fakeSiteAttachmentRateDao.getUniqueEntityByPropNames(new String[] { "dataYear",
				"period", "riskCode", "orgCode" }, new Object[] { year, peroid, riskType, organ });
		if (exist != null) {
			exist.copyReportDatas(rate);
			fakeSiteAttachmentRateDao.update(exist);
			return;
		}
		boolean isExtra = RiskUtils.checkRiskExtraAndValid(reportAdditionalDao, rate.getOrganization(),
				rate.getRiskCategory(), rate.getPeriod(), rate.isExtral(), now,rate.getReportDate());
		rate.setExtral(isExtra);
		fakeSiteAttachmentRateDao.save(rate);
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
	public void deleteFakeSiteAttachmentRate(long id) {
		fakeSiteAttachmentRateDao.delete(id);
	}

	@Override
	public Page<FakeSiteAttachmentRate> getFakeSiteAttachmentRates(
			ReportQueryParam param, Page<FakeSiteAttachmentRate> page,boolean riskCodeLike,boolean checkControl) {
		return fakeSiteAttachmentRateDao.findPage(page, RiskUtils.getHqlByQueryParam(param,userService,FakeSiteAttachmentRate.class,riskCategoryService,riskCodeLike,checkControl));
	}
	
	@Override
	public Page<FakeSiteAttachmentRate> getFakeSiteAttachmentRates(AnalyseQueryParam param,Page<FakeSiteAttachmentRate> page) {
		return fakeSiteAttachmentRateDao.findPage(page,RiskUtils.getHqlByQueryParam(param,userService,FakeSiteAttachmentRate.class,riskCategoryService));
	}
}
