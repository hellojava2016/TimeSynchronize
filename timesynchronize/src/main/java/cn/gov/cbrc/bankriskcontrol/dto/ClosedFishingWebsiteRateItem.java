package cn.gov.cbrc.bankriskcontrol.dto;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 假冒网站查封率指标
 * 
 * @author ppl
 * 
 */
@XmlRootElement(name = "ClosedFishingWebsiteRateItem")
public class ClosedFishingWebsiteRateItem implements Serializable {
	private static final long serialVersionUID = 113273148614671111L;
	/**
	 * 指标类型
	 */
	private String riskCode;
	/**
	 * 报送日期
	 */
	private Date reportDate;

	/**
	 * 已查封数量
	 */
	private int ncfw;

	/**
	 * 总数目
	 */
	private int nfw;

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

	public int getNcfw() {
		return ncfw;
	}

	public void setNcfw(int ncfw) {
		this.ncfw = ncfw;
	}

	public int getNfw() {
		return nfw;
	}

	public void setNfw(int nfw) {
		this.nfw = nfw;
	}

}
