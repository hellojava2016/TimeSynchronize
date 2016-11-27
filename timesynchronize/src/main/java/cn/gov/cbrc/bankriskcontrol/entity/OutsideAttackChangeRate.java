package cn.gov.cbrc.bankriskcontrol.entity;

import java.io.Serializable;

import javax.persistence.*;

import cn.gov.cbrc.bankriskcontrol.util.ConvertUtils;
import cn.gov.cbrc.bankriskcontrol.util.DateUtil;
import cn.gov.cbrc.bankriskcontrol.util.ExportUtil;

import java.util.Date;


/**
 * The persistent class for the outside_attack_change_rate database table.
 * 
 */
@Entity
@Table(name="outside_attack_change_rate")
@NamedQuery(name="OutsideAttackChangeRate.findAll", query="SELECT o FROM OutsideAttackChangeRate o")
public class OutsideAttackChangeRate implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String areaCode;
	private int dataMonth;
	private int dataWeek;
	private int dataYear;
	private int idswp;//入侵检测系统告警数
	private int ipswp;//入侵保护系统告警数
	private boolean extral;
	private String kipCode;
	private int ldswp;
	private int lpswp;
	private String orgCode;
	private int period;
	private Date recordTime;
	private Date reportDate;
	private String riskCode;
	private int status;
	private Organization organization;
	private RiskCategory riskCategory;
	private RiskDataFile riskDataFile;
	private User user;

	public OutsideAttackChangeRate() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	@Column(name="AREA_CODE")
	public String getAreaCode() {
		return this.areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}


	@Column(name="DATA_MONTH")
	public int getDataMonth() {
		return this.dataMonth;
	}

	public void setDataMonth(int dataMonth) {
		this.dataMonth = dataMonth;
	}


	@Column(name="DATA_WEEK")
	public int getDataWeek() {
		return this.dataWeek;
	}

	public void setDataWeek(int dataWeek) {
		this.dataWeek = dataWeek;
	}


	@Column(name="DATA_YEAR")
	public int getDataYear() {
		return this.dataYear;
	}

	public void setDataYear(int dataYear) {
		this.dataYear = dataYear;
	}


	public int getIdswp() {
		return this.idswp;
	}

	public void setIdswp(int idswp) {
		this.idswp = idswp;
	}


	public int getIpswp() {
		return this.ipswp;
	}

	public void setIpswp(int ipswp) {
		this.ipswp = ipswp;
	}


	@Column(name="IS_EXTRAL")
	public boolean isExtral() {
		return extral;
	}


	public void setExtral(boolean extral) {
		this.extral = extral;
	}


	@Column(name="KIP_CODE")
	public String getKipCode() {
		return this.kipCode;
	}

	public void setKipCode(String kipCode) {
		this.kipCode = kipCode;
	}


	public int getLdswp() {
		return this.ldswp;
	}

	public void setLdswp(int ldswp) {
		this.ldswp = ldswp;
	}


	public int getLpswp() {
		return this.lpswp;
	}

	public void setLpswp(int lpswp) {
		this.lpswp = lpswp;
	}


	@Column(name="ORG_CODE")
	public String getOrgCode() {
		return this.orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}


	public int getPeriod() {
		return this.period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}


	
	@Column(name="RECORD_TIME")
	public Date getRecordTime() {
		return this.recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}


	
	@Column(name="REPORT_DATE")
	public Date getReportDate() {
		return this.reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}


	@Column(name="RISK_CODE")
	public String getRiskCode() {
		return this.riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}


	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}


	//bi-directional many-to-one association to Organization
	@ManyToOne
	@JoinColumn(name="ORG_ID")
	public Organization getOrganization() {
		return this.organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
		if(organization!=null&&organization.getAreaCity()!=null)
			this.areaCode=organization.getAreaCity().getAreeCode();
	}


	//bi-directional many-to-one association to RiskCategory
	@ManyToOne
	@JoinColumn(name="RISK_ID")
	public RiskCategory getRiskCategory() {
		return this.riskCategory;
	}

	public void setRiskCategory(RiskCategory riskCategory) {
		this.riskCategory = riskCategory;
	}


	//bi-directional many-to-one association to RiskDataFile
	@ManyToOne
	@JoinColumn(name="FILE_ID")
	public RiskDataFile getRiskDataFile() {
		return this.riskDataFile;
	}

	public void setRiskDataFile(RiskDataFile riskDataFile) {
		this.riskDataFile = riskDataFile;
	}


	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="USER_ID")
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Transient
	public int getTotal(){
		return idswp+ipswp;
	}
	
	@Transient
	public double getPercent(){
		return ((idswp+ipswp+0.0)/(ldswp+lpswp)-1)*100;
	}
	
	@Transient
	public String getPercentShow() {//主要解决界面表格中正负无穷大的显示问题
		if (ldswp + lpswp == 0)
			return "";
		else
			return ConvertUtils.get2pointDouble(((idswp + ipswp + 0.0) / (ldswp + lpswp) - 1) * 100);
	}

	public void copyReportDatas(OutsideAttackChangeRate rate){
		setIdswp(rate.getIdswp());
		setIpswp(rate.getIpswp());
	}
	
	@Transient
	public String getShowDate() {
		return ExportUtil.getShowDate(riskCategory, period, reportDate);
	}
}