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


/**
 * The persistent class for the computer_room_info database table.
 * 
 */
@Entity
@Table(name="computer_room_info")
@NamedQuery(name="ComputerRoomInfo.findAll", query="SELECT c FROM ComputerRoomInfo c")
public class ComputerRoomInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long crId;
	private double area;//机房及机柜所占的面积
	private String areaCode;
	private String location;//位置
	private String manufacturer;//机房所属厂商
	private int power;//设施的功率
	private int precisionAcCount;//精密空调个数
	private String purpose;//设施的用途
	private Date recordTime;
	private Date serverTime;//开始服务时间
	private String uniqueVal;//唯一值
	private Date updateTime;
	private int upsCount;//机房UPS的数量
	private Organization organization;

	public ComputerRoomInfo() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="CR_ID")
	public Long getCrId() {
		return this.crId;
	}

	public void setCrId(Long crId) {
		this.crId = crId;
	}


	public double getArea() {
		return this.area;
	}

	public void setArea(double area) {
		this.area = area;
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


	public int getPower() {
		return this.power;
	}

	public void setPower(int power) {
		this.power = power;
	}


	@Column(name="PRECISION_AC_COUNT")
	public int getPrecisionAcCount() {
		return this.precisionAcCount;
	}

	public void setPrecisionAcCount(int precisionAcCount) {
		this.precisionAcCount = precisionAcCount;
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


	
	@Column(name="SERVER_TIME")
	public Date getServerTime() {
		return this.serverTime;
	}

	public void setServerTime(Date serverTime) {
		this.serverTime = serverTime;
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


	@Column(name="UPS_COUNT")
	public int getUpsCount() {
		return this.upsCount;
	}

	public void setUpsCount(int upsCount) {
		this.upsCount = upsCount;
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
}