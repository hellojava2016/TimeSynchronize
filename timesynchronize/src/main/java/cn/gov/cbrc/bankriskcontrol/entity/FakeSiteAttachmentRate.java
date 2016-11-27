package cn.gov.cbrc.bankriskcontrol.entity;

import java.io.Serializable;

import javax.persistence.*;

import cn.gov.cbrc.bankriskcontrol.util.DateUtil;
import cn.gov.cbrc.bankriskcontrol.util.ExportUtil;

import java.util.Date;


/**
 * The persistent class for the fake_site_attachment_rate database table.
 * 
 */
@Entity
@Table(name="fake_site_attachment_rate")
@NamedQuery(name="FakeSiteAttachmentRate.findAll", query="SELECT f FROM FakeSiteAttachmentRate f")
public class FakeSiteAttachmentRate implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private int aofw;//假冒网站数量
	private String areaCode;
	private int cfw;//已查封数量
	private int dataMonth;
	private int dataWeek;
	private int dataYear;
	private boolean extral;
	private String kipCode;
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

	public FakeSiteAttachmentRate() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public int getAofw() {
		return this.aofw;
	}

	public void setAofw(int aofw) {
		this.aofw = aofw;
	}


	@Column(name="AREA_CODE")
	public String getAreaCode() {
		return this.areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}


	public int getCfw() {
		return this.cfw;
	}

	public void setCfw(int cfw) {
		this.cfw = cfw;
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

	public void copyReportDatas(FakeSiteAttachmentRate rate){
		setAofw(rate.getAofw());
		setCfw(rate.getCfw());
	}
	
	@Transient
	public double getPercent(){
		return (cfw+0.0)/aofw*100;
	}
	
	@Transient
	public String getShowDate() {
		return ExportUtil.getShowDate(riskCategory, period, reportDate);
	}
}