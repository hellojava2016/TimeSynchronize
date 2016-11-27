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
 * The persistent class for the database_info database table.
 * 
 */
@Entity
@Table(name="database_info")
@NamedQuery(name="DatabaseInfo.findAll", query="SELECT d FROM DatabaseInfo d")
public class DatabaseInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long dbId;
	private String areaCode;
	private String databaseName;//数据库名称
	private String operateSystem;//数据库所属操作系统 uniqueVal	
	private OperateSystemInfo osinfo;
	private String patch;//数据库补丁
	private Date recordTime;
	private Date serverTime;//数据库开始服务的时间
	private String type;//数据库的类型
	private String uniqueVal;//唯一值
	private Date updateTime;
	private String version;//数据库的版本
	private Organization organization;
	private String appSystem;//数据库支撑的业务系统 允许多个	
	private Set<AppSystem> appSystems;
	
	public DatabaseInfo() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="DB_ID")
	public Long getDbId() {
		return this.dbId;
	}

	public void setDbId(Long dbId) {
		this.dbId = dbId;
	}


	@Column(name="AREA_CODE")
	public String getAreaCode() {
		return this.areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}


	@Column(name="DATABASE_NAME")
	public String getDatabaseName() {
		return this.databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
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

	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "database_app", joinColumns = { @JoinColumn(name = "DB_ID") }, inverseJoinColumns = { @JoinColumn(name = "APP_ID") })
	public Set<AppSystem> getAppSystems() {
		return appSystems;
	}

	public void setAppSystems(Set<AppSystem> appSystems) {
		this.appSystems = appSystems;
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