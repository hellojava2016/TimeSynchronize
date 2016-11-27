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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


/**
 * The persistent class for the network_equipment_info database table.
 * 
 */
@Entity
@Table(name="network_equipment_info")
@NamedQuery(name="NetworkEquipmentInfo.findAll", query="SELECT n FROM NetworkEquipmentInfo n")
public class NetworkEquipmentInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long neId;
	private String areaCode;
	private String location;//设备的位置
	private String manufacturer;//设备生产产商
	private String name;//设备的名称
	private int portCount;//端口数量
	private String purpose;//设备的用途
	private Date recordTime;
	private String serialNumber;//序列号
	private String type;//设备类型
	private String uniqueVal;//唯一值
	private Date updateTime;
	private String version;//设备版本	
	private String appSystem;//设备支撑的业务系统
	private Set<AppSystem> appSystems;
	private Organization organization;

	public NetworkEquipmentInfo() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="NE_ID")
	public Long getNeId() {
		return this.neId;
	}

	public void setNeId(Long neId) {
		this.neId = neId;
	}


	@Column(name="AREA_CODE")
	public String getAreaCode() {
		return this.areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}


	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}


	public String getManufacturer() {
		return this.manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


	@Column(name="PORT_COUNT")
	public int getPortCount() {
		return this.portCount;
	}

	public void setPortCount(int portCount) {
		this.portCount = portCount;
	}


	public String getPurpose() {
		return this.purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}


	
	@Column(name="RECORD_TIME")
	public Date getRecordTime() {
		return this.recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}


	@Column(name="SERIAL_NUMBER")
	public String getSerialNumber() {
		return this.serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
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
	@JoinTable(name = "ne_app", joinColumns = { @JoinColumn(name = "NE_ID") }, inverseJoinColumns = { @JoinColumn(name = "APP_ID") })
	public Set<AppSystem> getAppSystems() {
		return this.appSystems;
	}

	public void setAppSystems(Set<AppSystem> appSystems) {
		this.appSystems = appSystems;
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

	public String getAppSystem() {
		return appSystem;
	}

	public void setAppSystem(String appSystem) {
		this.appSystem = appSystem;
	}
}