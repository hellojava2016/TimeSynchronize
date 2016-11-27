package cn.gov.cbrc.bankriskcontrol.dto;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 投产变更成功率指标
 * 
 * @author ppl
 * 
 */
@XmlRootElement(name = "DeploymentChangeSuccessRateItem")
public class DeploymentChangeSuccessRateItem implements Serializable {
	private static final long serialVersionUID = 113273148614671333L;
	/**
	 * 指标类型
	 */
	private String riskCode;
	/**
	 * 报送日期
	 */
	private Date reportDate;

	/**
	 * 成功实施数量
	 */
	private int tndc;

	/**
	 * 总数目
	 */
	private int nsdc;

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

	public int getTndc() {
		return tndc;
	}

	public void setTndc(int tndc) {
		this.tndc = tndc;
	}

	public int getNsdc() {
		return nsdc;
	}

	public void setNsdc(int nsdc) {
		this.nsdc = nsdc;
	}

}
