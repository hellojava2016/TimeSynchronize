package cn.gov.cbrc.bankriskcontrol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;


/**
 *  风险指标配置
 * 
 */
@Entity
@Table(name="risk_category")
@NamedQuery(name="RiskCategory.findAll", query="SELECT r FROM RiskCategory r")
public class RiskCategory implements Serializable {
	private static final long serialVersionUID = -2405042207745724911L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="risk_id")
	private long riskId;

	/**
	 * 是否允许报送
	 */
	@Column(name="allow_report")
	private boolean allowReport;

	/**
	 * 周期 日1 周2 月3
	 */
	private int cycle;

	/**
	 * 截止报送天数
	 */
	@Column(name="end_day")
	private int endDay;

	/**
	 * 截止报送小时
	 */
	@Column(name="end_hour")
	private int endHour;

	@Column(name="is_use_threshold_value")
	private boolean useThresholdValue;

	@Column(name="max_value")
	private int maxValue=Integer.MAX_VALUE;

	@Column(name="min_value")
	private int minValue=Integer.MIN_VALUE;

	@Column(name="risk_code")
	private String riskCode;

	@Column(name="risk_name")
	private String riskName;

	/**
	 * 允许开始报送天数
	 */
	@Column(name="start_day")
	private int startDay;

	/**
	 * 允许开始报送小时
	 */
	@Column(name="start_hour")
	private int startHour;
	
	/**
	 * 是否有子指标
	 */
	@Column(name="has_leaf")
	private boolean hasLeaf;
	
	@Transient
	private String cycleString;
	
	@Transient
	private String startendString;

	public RiskCategory() {
	}

	public long getRiskId() {
		return this.riskId;
	}

	public void setRiskId(long riskId) {
		this.riskId = riskId;
	}

	public boolean getAllowReport() {
		return this.allowReport;
	}

	public void setAllowReport(boolean allowReport) {
		this.allowReport = allowReport;
	}

	public int getCycle() {
		return this.cycle;
	}

	public void setCycle(int cycle) {
		this.cycle = cycle;
	}

	public int getEndDay() {
		return this.endDay;
	}

	public void setEndDay(int endDay) {
		this.endDay = endDay;
	}

	public int getEndHour() {
		return this.endHour;
	}

	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}

	public boolean isUseThresholdValue() {
		return useThresholdValue;
	}

	public void setUseThresholdValue(boolean useThresholdValue) {
		this.useThresholdValue = useThresholdValue;
	}

	public int getMaxValue() {
		return this.maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public int getMinValue() {
		return this.minValue;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	public String getRiskCode() {
		return this.riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	public String getRiskName() {
		return this.riskName;
	}

	public void setRiskName(String riskName) {
		this.riskName = riskName;
	}

	public int getStartDay() {
		return this.startDay;
	}

	public void setStartDay(int startDay) {
		this.startDay = startDay;
	}

	public int getStartHour() {
		return this.startHour;
	}

	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}

	public boolean isHasLeaf() {
		return hasLeaf;
	}

	public void setHasLeaf(boolean hasLeaf) {
		this.hasLeaf = hasLeaf;
	}

	public String getCycleString() {
		if (cycle == 1)
			return "日";
		if (cycle == 2)
			return "周";
		return "月";
	}

	public void setCycleString(String cycleString) {
		this.cycleString = cycleString;
	}

	public String getStartendString() {
		String temp = "", str = "", str2 = "";
		if (cycle == 1)
			temp = "当天";
		else if (cycle == 2)
			temp = "周日";
		else
			temp = "月末";
		if (startDay == 0 && startHour == 0)
			str = "";
		else if (startDay == 0 && startHour != 0)
			str = ":后" + startHour + "小时";
		else if (startDay != 0 && startHour == 0)
			str = ":后" + startDay + "天";
		else
			str = ":后" + startDay + "天" + startHour + "小时";
		if (endDay == 0 && endHour == 0)
			str2 = "";
		else if (endDay == 0 && endHour != 0)
			str2 = "~后" + endHour + "小时";
		else if (endDay != 0 && endHour == 0)
			str2 = "~后" + endDay + "天";
		else
			str2 = "~后" + endDay + "天" + endHour + "小时";
		if (StringUtils.isEmpty(str + str2))
			return temp;
		else
			return temp + "(" + str + " " + str2 + ")";
	}

	public void setStartendString(String startendString) {
		this.startendString = startendString;
	}
}