package cn.gov.cbrc.bankriskcontrol.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.gov.cbrc.bankriskcontrol.util.ExportUtil;

@Entity
@Table(name = "warning_info")
@NamedQuery(name = "WarningInfo.findAll", query = "SELECT a FROM WarningInfo a")
public class WarningInfo implements Serializable {
	private static final long serialVersionUID = 2883223543590184937L;
	private long id;
	private Organization org;// 所属机构
	private String riskName;// 指标类型
	private int cycle;// 周期
	private int period;// 期数
	private Date reportDate;// 指标日期
	private String value;// 指标值
	private String memos;//详情
	private String warningType;// 预警类型

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Organization getOrg() {
		return org;
	}

	public void setOrg(Organization org) {
		this.org = org;
	}

	public String getRiskName() {
		return riskName;
	}

	public void setRiskName(String riskName) {
		this.riskName = riskName;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getWarningType() {
		return warningType;
	}

	public void setWarningType(String warningType) {
		this.warningType = warningType;
	}

	public int getCycle() {
		return cycle;
	}

	public void setCycle(int cycle) {
		this.cycle = cycle;
	}

	public String getMemos() {
		return memos;
	}

	public void setMemos(String memos) {
		this.memos = memos;
	}

	@Transient
	public String getShowDate() {
		return ExportUtil.getShowDate(cycle, period, reportDate);
	}

	@Transient
	public String getWarningTypeStr() {
		if (warningType.equals("1"))
			return "指标连续下降";
		else if (warningType.equals("2"))
			return "指标偏离平均值";
		else
			return "";
	}
}