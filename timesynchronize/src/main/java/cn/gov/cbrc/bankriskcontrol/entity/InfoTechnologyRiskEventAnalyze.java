package cn.gov.cbrc.bankriskcontrol.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the info_technology_risk_event_analyze database table.
 * 
 */
@Entity
@Table(name="info_technology_risk_event_analyze")
@NamedQuery(name="InfoTechnologyRiskEventAnalyze.findAll", query="SELECT i FROM InfoTechnologyRiskEventAnalyze i")
public class InfoTechnologyRiskEventAnalyze implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String areaCode;
	private int dataMonth;
	private int dataWeek;
	private int dataYear;
	private int datas;
	private String orgCode;
	private int period;
	private RiskCategory riskCategory;

	public InfoTechnologyRiskEventAnalyze() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
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


	public int getDatas() {
		return this.datas;
	}

	public void setDatas(int datas) {
		this.datas = datas;
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


	//bi-directional many-to-one association to RiskCategory
	@ManyToOne
	@JoinColumn(name="RISK_ID")
	public RiskCategory getRiskCategory() {
		return this.riskCategory;
	}

	public void setRiskCategory(RiskCategory riskCategory) {
		this.riskCategory = riskCategory;
	}

}