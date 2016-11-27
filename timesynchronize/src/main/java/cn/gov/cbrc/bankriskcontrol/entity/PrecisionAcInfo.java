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
 * The persistent class for the precision_ac_info database table.
 * 
 */
@Entity
@Table(name="precision_ac_info")
@NamedQuery(name="PrecisionAcInfo.findAll", query="SELECT p FROM PrecisionAcInfo p")
public class PrecisionAcInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long acId;
	private String areaCode;
	private String name;//精密空调名称
	private Date recordTime;
	private String returnHumidity;//精密空调回风湿度
	private String roomUV;//空调所在机房的唯一值
	private String supplyHumidity;//精密空调送风湿度
	private String supplyTemp;//精密空调送风温度
	private String type;//精密空调型号
	private String uniqueVal;//唯一值
	private Date updateTime;
	private ComputerRoomInfo computerRoomInfo;
	private Organization organization;

	public PrecisionAcInfo() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="AC_ID")
	public Long getAcId() {
		return this.acId;
	}

	public void setAcId(Long acId) {
		this.acId = acId;
	}


	@Column(name="AREA_CODE")
	public String getAreaCode() {
		return this.areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


	
	@Column(name="RECORD_TIME")
	public Date getRecordTime() {
		return this.recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}


	@Column(name="RETURN_HUMIDITY")
	public String getReturnHumidity() {
		return this.returnHumidity;
	}

	public void setReturnHumidity(String returnHumidity) {
		this.returnHumidity = returnHumidity;
	}


	@Column(name="ROOM_UV")
	public String getRoomUV() {
		return this.roomUV;
	}

	public void setRoomUV(String roomUV) {
		this.roomUV = roomUV;
	}


	@Column(name="SUPPLY_HUMIDITY")
	public String getSupplyHumidity() {
		return this.supplyHumidity;
	}

	public void setSupplyHumidity(String supplyHumidity) {
		this.supplyHumidity = supplyHumidity;
	}


	@Column(name="SUPPLY_TEMP")
	public String getSupplyTemp() {
		return this.supplyTemp;
	}

	public void setSupplyTemp(String supplyTemp) {
		this.supplyTemp = supplyTemp;
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


	//bi-directional many-to-one association to ComputerRoomInfo
	@ManyToOne
	@JoinColumn(name="CR_ID")
	public ComputerRoomInfo getComputerRoomInfo() {
		return this.computerRoomInfo;
	}

	public void setComputerRoomInfo(ComputerRoomInfo computerRoomInfo) {
		this.computerRoomInfo = computerRoomInfo;
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

}
