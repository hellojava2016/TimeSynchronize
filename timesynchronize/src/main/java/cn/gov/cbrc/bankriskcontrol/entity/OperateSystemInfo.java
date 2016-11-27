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
import javax.persistence.Transient;


/**
 * The persistent class for the operate_system_info database table.
 * 
 */
@Entity
@Table(name="operate_system_info")
@NamedQuery(name="OperateSystemInfo.findAll", query="SELECT o FROM OperateSystemInfo o")
public class OperateSystemInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long osId;
	private String areaCode;	
	private PcServer pcserver;
	private String host;//操作系统所属主机 uniqueVal	
	private Set<AppSystem> appSystems;	
	private String appSystem;//操作系统支撑的业务系统
	private String name;//名称
	private String patch;//补丁
	private Date recordTime;
	private String type;//类型
	private String uniqueVal;//唯一值
	private Date updateTime;
	private String version;//版本
	private Organization organization;

	public OperateSystemInfo() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="OS_ID")
	public Long getOsId() {
		return this.osId;
	}

	public void setOsId(Long osId) {
		this.osId = osId;
	}


	@Column(name="AREA_CODE")
	public String getAreaCode() {
		return this.areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}


	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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
	
	@OneToOne(fetch=FetchType.EAGER)
	public PcServer getPcserver() {
		return pcserver;
	}

	public void setPcserver(PcServer pcserver) {
		this.pcserver = pcserver;
	}

	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "os_app", joinColumns = { @JoinColumn(name = "OS_ID") }, inverseJoinColumns = { @JoinColumn(name = "APP_ID") })
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
}
