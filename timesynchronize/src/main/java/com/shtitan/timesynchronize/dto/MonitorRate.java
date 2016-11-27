package com.shtitan.timesynchronize.dto;

import java.io.Serializable;

import com.shtitan.timesynchronize.entity.RiskCategory;


public class MonitorRate implements Serializable {

	private static final long serialVersionUID = 6774857046781719266L;
	
	private String riskmearment;
	
	private String mearmentdetail;
	
	private String riskCode;
	
	private String date;
	
	private String value;
	
	private String tongRate="0%";
	
	private String huanRate="0%";
	
	private String org;
	
	private int period;
	
	private RiskCategory riskCategory; 

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getRiskmearment() {
		return riskmearment;
	}

	public void setRiskmearment(String riskmearment) {
		this.riskmearment = riskmearment;
	}

	public String getMearmentdetail() {
		return mearmentdetail;
	}

	public void setMearmentdetail(String mearmentdetail) {
		this.mearmentdetail = mearmentdetail;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTongRate() {
		return tongRate;
	}

	public void setTongRate(String tongRate) {
		this.tongRate = tongRate;
	}

	public String getHuanRate() {
		return huanRate;
	}

	public void setHuanRate(String huanRate) {
		this.huanRate = huanRate;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getRiskCode() {
		return riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	public RiskCategory getRiskCategory() {
		return riskCategory;
	}

	public void setRiskCategory(RiskCategory riskCategory) {
		this.riskCategory = riskCategory;
	}	
	
	public String getShowDate() {
		String periodStr = period + "";
		int cycle = riskCategory.getCycle();
		if (cycle == 1) {
			periodStr = "D" + period;
		} else if (cycle == 2) {
			periodStr = "W" + period;
		} else if (cycle == 3) {
			periodStr = "M" + period;
		}
		return date + "(" + periodStr + ")";
	}
}
