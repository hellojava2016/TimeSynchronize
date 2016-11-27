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
import javax.persistence.Transient;


/**
 * The persistent class for the storage_system_info database table.
 * 
 */
@Entity
@Table(name="storage_system_info")
@NamedQuery(name="StorageSystemInfo.findAll", query="SELECT s FROM StorageSystemInfo s")
public class StorageSystemInfo implements Serializable {
	private static final long serialVersionUID = 1L;	
	private long ssId;	
	private String areaCode;	
	private String capacityInfo;//容量信息	
	private String diskSpec;//磁盘规格
	private String location;//设备的位置
	private String manufacturer;//设备生产厂商	
	private String microcodeVersion;//存储微码版本
	private String purpose;//设备的用途	
	private Date recordTime;	
	private String serialNumber;//序列号	
	private Date serviceTime;//开始服务的时间	
	private int storageArraySize;//存储配置容量	
	private int storageCacheSize;//存储CACHE容量	
	private String storageRaidMode;//存储RAID方式	
	private int storageTapeMediaCount;//存储带介质数量	
	private String storageTapeMediaType;//存储带介质类型	
	private String type;//设备的类型	
	private String uniqueVal;//唯一值	
	private Date updateTime;
	private String version;//设备版本及补丁	
	private String name;//设备的名称		
	private String appSystem;	
	private Set<AppSystem> appSystems;//设备支撑的业务系统	
	private Organization organization;
	
	public String getAppSystem() {
		return appSystem;
	}

	public void setAppSystem(String appSystem) {
		this.appSystem = appSystem;
	}

	public StorageSystemInfo() {
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ss_id")
	public long getSsId() {
		return this.ssId;
	}

	public void setSsId(long ssId) {
		this.ssId = ssId;
	}
	@Column(name="area_code")
	public String getAreaCode() {
		return this.areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	@Column(name="capacity_info")
	public String getCapacityInfo() {
		return this.capacityInfo;
	}

	public void setCapacityInfo(String capacityInfo) {
		this.capacityInfo = capacityInfo;
	}
	@Column(name="disk_spec")
	public String getDiskSpec() {
		return this.diskSpec;
	}

	public void setDiskSpec(String diskSpec) {
		this.diskSpec = diskSpec;
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
	@Column(name="microcode_version")
	public String getMicrocodeVersion() {
		return this.microcodeVersion;
	}

	public void setMicrocodeVersion(String microcodeVersion) {
		this.microcodeVersion = microcodeVersion;
	}

	public String getPurpose() {
		return this.purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	@Column(name="record_time")
	public Date getRecordTime() {
		return this.recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}
	@Column(name="serial_number")
	public String getSerialNumber() {
		return this.serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	@Column(name="sever_time")
	public Date getServiceTime() {
		return serviceTime;
	}
	
	public void setServiceTime(Date serviceTime) {
		this.serviceTime = serviceTime;
	}
	@Column(name="storage_array_size")
	public int getStorageArraySize() {
		return this.storageArraySize;
	}

	public void setStorageArraySize(int storageArraySize) {
		this.storageArraySize = storageArraySize;
	}
	@Column(name="storage_cache_size")
	public int getStorageCacheSize() {
		return this.storageCacheSize;
	}

	public void setStorageCacheSize(int storageCacheSize) {
		this.storageCacheSize = storageCacheSize;
	}
	@Column(name="storage_raid_mode")
	public String getStorageRaidMode() {
		return this.storageRaidMode;
	}

	public void setStorageRaidMode(String storageRaidMode) {
		this.storageRaidMode = storageRaidMode;
	}
	@Column(name="storage_tape_media_count")
	public int getStorageTapeMediaCount() {
		return this.storageTapeMediaCount;
	}

	public void setStorageTapeMediaCount(int storageTapeMediaCount) {
		this.storageTapeMediaCount = storageTapeMediaCount;
	}
	@Column(name="storage_tape_media_type")
	public String getStorageTapeMediaType() {
		return this.storageTapeMediaType;
	}

	public void setStorageTapeMediaType(String storageTapeMediaType) {
		this.storageTapeMediaType = storageTapeMediaType;
	}
	@Column(name="TYPES")
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}
	@Column(name="unique_val")
	public String getUniqueVal() {
		return this.uniqueVal;
	}

	public void setUniqueVal(String uniqueVal) {
		this.uniqueVal = uniqueVal;
	}
	@Column(name="update_time")
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
	@JoinTable(name = "ss_app", joinColumns = { @JoinColumn(name = "SS_ID") }, inverseJoinColumns = { @JoinColumn(name = "APP_ID") })
	public Set<AppSystem> getAppSystems() {
		return this.appSystems;
	}

	public void setAppSystems(Set<AppSystem> appSystems) {
		this.appSystems = appSystems;
	}

	@ManyToOne
	@JoinColumn(name="org_id")
	public Organization getOrganization() {
		return this.organization;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
		if(organization!=null&&organization.getAreaCity()!=null)
			this.areaCode=organization.getAreaCity().getAreeCode();
	}

}