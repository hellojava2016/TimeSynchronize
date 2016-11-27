package cn.gov.cbrc.bankriskcontrol.dto;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 外部攻击变化率指标
 * 
 * @author ppl
 * 
 */
@XmlRootElement(name = "ExternalAttackChangeRateItem")
public class ExternalAttackChangeRateItem implements Serializable {
	private static final long serialVersionUID = 113273148614672222L;
	/**
	 * 指标类型
	 */
	private String riskCode;
	/**
	 * 报送日期
	 */
	private Date reportDate;

	/**
	 * 当期入侵检测系统告警数
	 */
	private int nidscp;

	/**
	 * 当期入侵保护系统告警数
	 */
	private int nipscp;

	public String getRiskCode() {
		return riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public int getNidscp() {
		return nidscp;
	}

	public void setNidscp(int nidscp) {
		this.nidscp = nidscp;
	}

	public int getNipscp() {
		return nipscp;
	}

	public void setNipscp(int nipscp) {
		this.nipscp = nipscp;
	}
}
