package cn.gov.cbrc.bankriskcontrol.dto;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 系統交易成功率指标
 * 
 * @author ppl
 * 
 */
@XmlRootElement(name = "SystemTransactionSuccessRateItem")
public class SystemTransactionSuccessRateItem implements Serializable {
	private static final long serialVersionUID = 1132731486146716432L;
	/**
	 * 指标类型
	 */
	private String riskCode;

	/**
	 * 报送日期
	 */
	private Date reportDate;

	/**
	 * 成功交易量
	 */
	private int nst;

	/**
	 * 交易总量
	 */
	private int tnt;

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

	public int getNst() {
		return nst;
	}

	public void setNst(int nst) {
		this.nst = nst;
	}

	public int getTnt() {
		return tnt;
	}

	public void setTnt(int tnt) {
		this.tnt = tnt;
	}
}
