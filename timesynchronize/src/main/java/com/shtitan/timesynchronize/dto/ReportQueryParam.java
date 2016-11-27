package com.shtitan.timesynchronize.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 上报查询参数
 * @author ppl
 *
 */
public class ReportQueryParam implements Serializable {
	private static final long serialVersionUID = 3073661624991840439L;

	private String reportType;

	private Date startDate;
	
	private Date endDate;

	private long departmentId;

	private long userId;
	
	private Date exactDate;//精确日期

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(long departmentId) {
		this.departmentId = departmentId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Date getExactDate() {
		return exactDate;
	}

	public void setExactDate(Date exactDate) {
		this.exactDate = exactDate;
	}
	
	public ReportQueryParam getCopyObject(){
		ReportQueryParam param=new ReportQueryParam();
		param.setDepartmentId(departmentId);
		param.setEndDate(endDate);
		param.setExactDate(exactDate);
		param.setReportType(reportType);
		param.setStartDate(startDate);
		param.setUserId(userId);
		return param;
	}
}
