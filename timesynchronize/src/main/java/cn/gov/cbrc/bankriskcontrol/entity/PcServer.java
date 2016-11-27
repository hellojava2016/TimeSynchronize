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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


/**
 * The persistent class for the pc_server database table.
 * 
 */
@Entity
@Table(name="pc_server")
@NamedQuery(name="PcServer.findAll", query="SELECT p FROM PcServer p")
public class PcServer implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long serverId;
	private String areaCode;
	private int category;//分类  0PcServer  1小型机 	2大型机
	private String cpu;//CPU型号
	private int cpuCount;//CPU数量
	private int hardDiskSize;//硬盘大小
	private String ip;//主机IP 允许多个
	private String location;//位置
	private String manufacturer;//设备生产产商
	private int memorySize;//内存大小
	private String name;//主机的名称
	private String purpose;//主机的用途
	private Date recordTime;
	private String serialNumber;//序列号
	private Date serviceTime;//主机开始服务时间
	private String type;//设备生产型号
	private String uniqueVal;//唯一值
	private Date updateTime;
	private Organization organization;

	public PcServer() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="SERVER_ID")
	public Long getServerId() {
		return this.serverId;
	}

	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}


	@Column(name="AREE_CODE")
	public String getAreaCode() {
		return this.areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public int getCategory() {
		return this.category;
	}

	public void setCategory(int category) {
		this.category = category;
	}


	public String getCpu() {
		return this.cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}


	@Column(name="CPU_COUNT")
	public int getCpuCount() {
		return this.cpuCount;
	}

	public void setCpuCount(int cpuCount) {
		this.cpuCount = cpuCount;
	}


	@Column(name="HARD_DISK_SIZE")
	public int getHardDiskSize() {
		return this.hardDiskSize;
	}

	public void setHardDiskSize(int hardDiskSize) {
		this.hardDiskSize = hardDiskSize;
	}


	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
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


	@Column(name="MEMORY_SIZE")
	public int getMemorySize() {
		return this.memorySize;
	}

	public void setMemorySize(int memorySize) {
		this.memorySize = memorySize;
	}


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Column(name="SERVICE_TIME")
	public Date getServiceTime() {
		return this.serviceTime;
	}

	public void setServiceTime(Date serviceTime) {
		this.serviceTime = serviceTime;
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

	@Transient
	public String getCategoryString(){
		if(category==0)
			return "PcServer";
		else if(category==1)
			return "小型机";
		else if(category==2)
			return "大型机";
		return "";
	}
}