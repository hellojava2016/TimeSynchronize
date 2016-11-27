package cn.gov.cbrc.bankriskcontrol.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

/**
 * 补报设置
 * 
 * @author pl
 * 
 */
@Entity
@Proxy(lazy = false)
@Table(name="extra_report_info")
@NamedQuery(name="ExtraReportInfo.findAll", query="SELECT e FROM ExtraReportInfo e")
public class ExtraReportInfo implements Serializable {
	private static final long serialVersionUID = -3142759747844894182L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	/**
	 * 补报开始时间
	 */
	@Column(name="begin_time")
	private Date beginTime;

	/**
	 * 补报结束时间
	 */
	@Column(name="end_time")
	private Date endTime;

	/**
	 * 是否已审批
	 */
	@Column(name="is_audit")
	private boolean audit;

	/**
	 * 是否补报完成
	 */
	@Column(name="is_reported")
	private boolean reported;

	/**
	 * 报送期数
	 */
	private int period;

	/**
	 * 补报原因
	 */
	private String reason;

	/**
	 * 补报对应日期，和期数对应
	 */
	@Column(name="report_date")
	private Date reportDate;

	/**
	 * 补报类别 1为申请2为审批
	 */
	private int type;


	/**
	 * 所属机构
	 */
	@ManyToOne
	@JoinColumn(name="ORG_ID")
	private Organization organization;
	@ManyToOne
	@JoinColumn(name="RISK_ID")
	private RiskCategory riskCategory;
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private User user;

	public ExtraReportInfo() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public boolean isAudit() {
		return audit;
	}

	public void setAudit(boolean audit) {
		this.audit = audit;
	}

	public boolean isReported() {
		return reported;
	}

	public void setReported(boolean reported) {
		this.reported = reported;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	@Column(name="TYPES")
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}


	public Organization getOrganization() {
		return this.organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}


	//bi-directional many-to-one association to RiskCategory
	
	public RiskCategory getRiskCategory() {
		return this.riskCategory;
	}

	public void setRiskCategory(RiskCategory riskCategory) {
		this.riskCategory = riskCategory;
	}


	//bi-directional many-to-one association to User

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
