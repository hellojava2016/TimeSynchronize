package com.shtitan.timesynchronize.service.system;

import java.util.Date;

import com.shtitan.timesynchronize.dto.ReportQueryParam;
import com.shtitan.timesynchronize.entity.ExtraReportInfo;
import com.shtitan.timesynchronize.util.Page;


public interface ExtraReportInfoService {	
	public void addReportAdditional(ExtraReportInfo additional);
	
	public void deleteReportAdditionalById(long id);
	
	public void updateReportAdditional(ExtraReportInfo additional);
	
	public Page<ExtraReportInfo> getReportAdditionals(ReportQueryParam param,Page<ExtraReportInfo> page);
	
	public ExtraReportInfo getReportAdditionalById(long id);
	
	public void reportFromFile(String reportType, int reportIndex,
			Date reportDate, boolean append);
}
