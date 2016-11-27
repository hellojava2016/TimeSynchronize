package cn.gov.cbrc.bankriskcontrol.dto;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 通用上报指标参数，一般是无下级指标的叶子指标
 * 
 * @author ppl
 * 
 */
@XmlRootElement(name = "CommonReportItem")
public class InformationRiskEventItem implements Serializable {
	private static final long serialVersionUID = -1253315257282130038L;
	/**
	 * 报送指标类型编码
	 */
	private String riskCode;
	/**
	 * 报送日期
	 */
	private Date reportDate;
	/**
	 * 报送指标值
	 */
	private int count;

	public String getRiskCode() {
		return riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
}
