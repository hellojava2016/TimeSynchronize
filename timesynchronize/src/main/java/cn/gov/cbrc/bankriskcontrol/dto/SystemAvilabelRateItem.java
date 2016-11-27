package cn.gov.cbrc.bankriskcontrol.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 系统可用率三级指标
 * 
 * @author ppl
 * 
 */
@XmlRootElement(name = "SystemAvilabelRateItem")
public class SystemAvilabelRateItem implements Serializable {
	private static final long serialVersionUID = 1132731486146716049L;
	/**
	 * 指标类型
	 */
	private String riskCode;
	/**
	 * 报送日期
	 */
	private Date reportDate;
	/**
	 * 系统提供服务总时间 单位分钟
	 */
	private int tst;
	/**
	 * 计划停止时长
	 */
	private int sost;
	/**
	 * 意外停止时间
	 */
	private int uost;

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

	public int getTst() {
		return tst;
	}

	public void setTst(int tst) {
		this.tst = tst;
	}

	public int getSost() {
		return sost;
	}

	public void setSost(int sost) {
		this.sost = sost;
	}

	public int getUost() {
		return uost;
	}

	public void setUost(int uost) {
		this.uost = uost;
	}

	public static SystemAvilabelRateItem getRandomItem() {
		SystemAvilabelRateItem item = new SystemAvilabelRateItem();
		item.setRiskCode("1-1");
		item.setReportDate(new Date());
		item.setSost(75);
		item.setUost(80);
		item.setTst(9000);
		return item;
	}
}
