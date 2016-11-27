package cn.gov.cbrc.bankriskcontrol.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the middleware_info database table.
 * 
 */
@Entity
@Table(name="middleware_info")
@NamedQuery(name="MiddlewareInfo.findAll", query="SELECT m FROM MiddlewareInfo m")
public class MiddlewareInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long mwId;
	private String areaCode;
	@Column(name="NAME")
	private String middlewareName;//中间件名称
	private String operateSystem;//数据库所属操作系统
	private OperateSystemInfo osinfo;
	private String patch;//中间件补丁
	private Date recordTime;
	private Date serverTime;
	private String type;//中间件的类型
	private String uniqueVal;//唯一值
	private Date updateTime;
	private String version;//中间件的版本	
	private String appSystem;
	private Set<AppSystem> appSystems;//中间件支撑的业务系统
	private Organization organization;

	public MiddlewareInfo() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="MW_ID")
	public Long getMwId() {
		return this.mwId;
	}

	public void setMwId(Long mwId) {
		this.mwId = mwId;
	}


	@Column(name="AREA_CODE")
	public String getAreaCode() {
		return this.areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getMiddlewareName() {
		return middlewareName;
	}
	
	public void setMiddlewareName(String middlewareName) {
		this.middlewareName = middlewareName;
	}

	@Column(name="OPERATE_SYSTEM")
	public String getOperateSystem() {
		return this.operateSystem;
	}

	public void setOperateSystem(String operateSystem) {
		this.operateSystem = operateSystem;
	}


	public String getPatch() {
		return this.patch;
	}

	public void setPatch(String patch) {
		this.patch = patch;
	}


	
	@Column(name="RECORD_TIME")
	public Date getRecordTime() {
		return this.recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}


	
	@Column(name="SERVER_TIME")
	public Date getServerTime() {
		return this.serverTime;
	}

	public void setServerTime(Date serverTime) {
		this.serverTime = serverTime;
	}

	@Column(name="TYPES")
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}


	@Column(name="UNIQUE_VAL")
	public String getUniqueVal() {
		return this.uniqueVal;
	}

	public void setUniqueVal(String uniqueVal) {
		this.uniqueVal = uniqueVal;
	}


	
	@Column(name="UPDATE_TIME")
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}


	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "middleware_app", joinColumns = { @JoinColumn(name = "MW_ID") }, inverseJoinColumns = { @JoinColumn(name = "APP_ID") })
	public Set<AppSystem> getAppSystems() {
		return this.appSystems;
	}

	public void setAppSystems(Set<AppSystem> appSystems) {
		this.appSystems = appSystems;
	}

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

	public String getAppSystem() {
		return appSystem;
	}

	public void setAppSystem(String appSystem) {
		this.appSystem = appSystem;
	}

	@OneToOne(fetch=FetchType.EAGER)
	public OperateSystemInfo getOsinfo() {
		return osinfo;
	}

	public void setOsinfo(OperateSystemInfo osinfo) {
		this.osinfo = osinfo;
	}
}
