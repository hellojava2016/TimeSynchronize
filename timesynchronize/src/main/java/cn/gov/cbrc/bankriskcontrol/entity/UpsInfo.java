package cn.gov.cbrc.bankriskcontrol.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the ups_info database table.
 * 
 */
@Entity
@Table(name="ups_info")
@NamedQuery(name="UpsInfo.findAll", query="SELECT u FROM UpsInfo u")
public class UpsInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long upsId;
	private String areaCode;
	private String inputFrequency;//UPS输入频率
	private String inputVoltage;//输入电压
	private String name;//Ups名称
	private String outputFrequency;//UPS输出频率
	private String outputVoltage;//输出电压
	private String power;//各相有功功率
	private Date recordTime;
	private String roomUV;//UPS所在机房的唯一值
	private String type;//机房UPS的类型
	private String uniqueVal;//唯一值
	private Date updateTime;
	private ComputerRoomInfo computerRoomInfo;
	private Organization organization;

	public UpsInfo() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="UPS_ID")
	public Long getUpsId() {
		return this.upsId;
	}

	public void setUpsId(Long upsId) {
		this.upsId = upsId;
	}


	@Column(name="AREA_CODE")
	public String getAreaCode() {
		return this.areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}


	@Column(name="INPUT_FREQUENCY")
	public String getInputFrequency() {
		return this.inputFrequency;
	}

	public void setInputFrequency(String inputFrequency) {
		this.inputFrequency = inputFrequency;
	}


	@Column(name="INPUT_VOLTAGE")
	public String getInputVoltage() {
		return this.inputVoltage;
	}

	public void setInputVoltage(String inputVoltage) {
		this.inputVoltage = inputVoltage;
	}


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


	@Column(name="OUTPUT_FREQUENCY")
	public String getOutputFrequency() {
		return this.outputFrequency;
	}

	public void setOutputFrequency(String outputFrequency) {
		this.outputFrequency = outputFrequency;
	}


	@Column(name="OUTPUT_VOLTAGE")
	public String getOutputVoltage() {
		return this.outputVoltage;
	}

	public void setOutputVoltage(String outputVoltage) {
		this.outputVoltage = outputVoltage;
	}


	public String getPower() {
		return this.power;
	}

	public void setPower(String power) {
		this.power = power;
	}


	
	@Column(name="RECORD_TIME")
	public Date getRecordTime() {
		return this.recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}


	@Column(name="ROOM_UV")
	public String getRoomUV() {
		return this.roomUV;
	}

	public void setRoomUV(String roomUV) {
		this.roomUV = roomUV;
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