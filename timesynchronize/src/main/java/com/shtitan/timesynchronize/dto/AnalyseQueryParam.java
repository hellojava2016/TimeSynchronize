package com.shtitan.timesynchronize.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 指标分析参数
 * 
 * @author ppl
 * 
 */
public class AnalyseQueryParam implements Serializable {
	private static final long serialVersionUID = 30888899996666888L;

	private List<String> reportTypeList;// 参数列表

	private Date startDate;// 结束时间

	private Date endDate;// 结束时间

	private List<Long> departmentIdList;// 机构列表

	private String areaCode;// 区域

	private int category;// 机构类型
	
	private int sortType;//排序类型
	
	private int recentPeriod=0;//近期期数

	public List<String> getReportTypeList() {
		return reportTypeList;
	}

	public void setReportTypeList(List<String> reportTypeList) {
		this.reportTypeList = reportTypeList;
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

	public List<Long> getDepartmentIdList() {
		return departmentIdList;
	}

	public void setDepartmentIdList(List<Long> departmentIdList) {
		this.departmentIdList = departmentIdList;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getSortType() {
		return sortType;
	}

	public void setSortType(int sortType) {
		this.sortType = sortType;
	}

	public int getRecentPeriod() {
		return recentPeriod;
	}

	public void setRecentPeriod(int recentPeriod) {
		this.recentPeriod = recentPeriod;
	}
}
