package cn.gov.cbrc.bankriskcontrol.entity;

import java.io.Serializable;

import javax.persistence.*;

import cn.gov.cbrc.bankriskcontrol.util.ExportUtil;

import java.util.Date;

/**
 * 阈值越界记录
 * 
 */
@Entity
@Table(name = "risk_tca")
@NamedQuery(name = "RiskTca.findAll", query = "SELECT l FROM RiskTca l")
public class RiskTca implements Serializable {
	private static final long serialVersionUID = 6302280247937029409L;
	private long tcaId;
	private int period;// 期数
	private Date reportDate;// 上报日期
	private String currentValue;// 当前值
	private RiskCategory riskCategory;// 指标信息
	private Organization org;// 机构

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getTcaId() {
		return tcaId;
	}

	public void setTcaId(long tcaId) {
		this.tcaId = tcaId;
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
	
	public String getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}

	@OneToOne(fetch = FetchType.EAGER)
	public RiskCategory getRiskCategory() {
		return riskCategory;
	}

	public void setRiskCategory(RiskCategory riskCategory) {
		this.riskCategory = riskCategory;
	}

	@OneToOne(fetch = FetchType.EAGER)
	public Organization getOrg() {
		return org;
	}

	public void setOrg(Organization org) {
		this.org = org;
	}
	
	@Transient
	public String getShowDate() {
		return ExportUtil.getShowDate(riskCategory, period, reportDate);
	}
}