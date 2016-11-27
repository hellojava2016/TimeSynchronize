package com.shtitan.timesynchronize.service.system.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shtitan.timesynchronize.dao.risk.ReportAdditionalDao;
import com.shtitan.timesynchronize.dto.ReportQueryParam;
import com.shtitan.timesynchronize.entity.ExtraReportInfo;
import com.shtitan.timesynchronize.service.account.UserService;
import com.shtitan.timesynchronize.service.system.ExtraReportInfoService;
import com.shtitan.timesynchronize.util.Page;
import com.shtitan.timesynchronize.util.RiskUtils;


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
