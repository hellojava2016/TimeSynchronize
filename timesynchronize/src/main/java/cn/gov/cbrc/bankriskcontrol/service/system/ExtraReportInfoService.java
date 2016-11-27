package cn.gov.cbrc.bankriskcontrol.service.system;

import java.util.Date;

import cn.gov.cbrc.bankriskcontrol.dto.ReportQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.ExtraReportInfo;
import cn.gov.cbrc.bankriskcontrol.util.Page;

public interface ExtraReportInfoService {	
	public void addReportAdditional(ExtraReportInfo additional);
	
	public void deleteReportAdditionalById(long id);
	
	public void updateReportAdditional(ExtraReportInfo additional);
	
	public Page<ExtraReportInfo> getReportAdditionals(ReportQueryParam param,Page<ExtraReportInfo> page);
	
	public ExtraReportInfo getReportAdditionalById(long id);
	
	public void reportFromFile(String reportType, int reportIndex,
			Date reportDate, boolean append);
}
