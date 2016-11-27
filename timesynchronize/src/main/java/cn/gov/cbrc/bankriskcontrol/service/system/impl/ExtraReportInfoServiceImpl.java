package cn.gov.cbrc.bankriskcontrol.service.system.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.gov.cbrc.bankriskcontrol.dao.risk.ReportAdditionalDao;
import cn.gov.cbrc.bankriskcontrol.dto.ReportQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.ExtraReportInfo;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.system.ExtraReportInfoService;
import cn.gov.cbrc.bankriskcontrol.util.Page;
import cn.gov.cbrc.bankriskcontrol.util.RiskUtils;

@Service("extraReportInfoService")
@Transactional
public class ExtraReportInfoServiceImpl implements ExtraReportInfoService {	
	@Autowired
	private ReportAdditionalDao reportAdditionalDao;
	
	@Autowired
	private UserService userService;
	
	@Override
	public void reportFromFile(String reportType, int reportIndex,
			Date reportDate, boolean append) {		
	}	
		
	@Override
	public void addReportAdditional(ExtraReportInfo additional) {		
		reportAdditionalDao.save(additional);		
	}

	@Override
	public void deleteReportAdditionalById(long id) {
		reportAdditionalDao.delete(id);		
	}
	
	@Override
	public void updateReportAdditional(ExtraReportInfo additional) {
		reportAdditionalDao.update(additional);		
	}

	@Override
	public Page<ExtraReportInfo> getReportAdditionals(ReportQueryParam param,
			Page<ExtraReportInfo> page) {
		return reportAdditionalDao.findPage(page, RiskUtils.getHqlByQueryParam(param,userService));
	}
	
	@Override
	public ExtraReportInfo getReportAdditionalById(long id) {
		return reportAdditionalDao.get(id);		
	}
}
