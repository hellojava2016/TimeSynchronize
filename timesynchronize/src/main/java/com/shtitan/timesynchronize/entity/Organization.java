package com.shtitan.timesynchronize.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * The persistent class for the organization database table.
 * 
 */
@Entity
@Table(name="organization")
public class Organization implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long orgId;
	private String address;
	private int category;//参见getCategoryStr
	private String contactsCellphone;
	private String contactsMail;
	private String contactsName;
	private String contactsPhone;
	private String name;
	private String orgNo;
	private String secretKey;
	private long moneyCount;
	private boolean canControl;

	private AreaCity areaCity;
	private Organization organization;


	public Organization() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="org_id", unique=true, nullable=false)
	public Long getOrgId() {
		return this.orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}


	@Column(length=256)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}


	public int getCategory() {
		return this.category;
	}

	public void setCategory(int category) {
		this.category = category;
	}


	@Column(name="contacts_cellphone", length=32)
	public String getContactsCellphone() {
		return this.contactsCellphone;
	}

	public void setContactsCellphone(String contactsCellphone) {
		this.contactsCellphone = contactsCellphone;
	}


	@Column(name="contacts_mail", length=128)
	public String getContactsMail() {
		return this.contactsMail;
	}

	public void setContactsMail(String contactsMail) {
		this.contactsMail = contactsMail;
	}


	@Column(name="contacts_name", length=32)
	public String getContactsName() {
		return this.contactsName;
	}

	public void setContactsName(String contactsName) {
		this.contactsName = contactsName;
	}


	@Column(name="contacts_phone", length=32)
	public String getContactsPhone() {
		return this.contactsPhone;
	}

	public void setContactsPhone(String contactsPhone) {
		this.contactsPhone = contactsPhone;
	}


	@Column(length=128)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="ORG_NO")	public String getOrgNo() {		return this.orgNo;	}	public void setOrgNo(String orgNo) {		this.orgNo = orgNo;	}
	@Column(name="secret_key", length=255)
	public String getSecretKey() {
		return this.secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	//bi-directional many-to-one association to AreaCity
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="area_id")
	public AreaCity getAreaCity() {
		return this.areaCity;
	}

	public void setAreaCity(AreaCity areaCity) {
		this.areaCity = areaCity;
	}


	//bi-directional many-to-one association to Organization
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parent_id")
	public Organization getOrganization() {
		return this.organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	
	public long getMoneyCount() {
		return moneyCount;
	}

	public void setMoneyCount(long moneyCount) {
		this.moneyCount = moneyCount;
	}

	public boolean isCanControl() {
		return canControl;
	}

	public void setCanControl(boolean canControl) {
		this.canControl = canControl;
	}

	@Transient
	public String getCategoryStr() {
		if (category == 1)
			return "监管机构";
		else if (category == 2)
			return "政策性及国家开发银行";
		else if (category == 3)
			return "大型商业银行";
		else if (category == 4)
			return "股份制商业银行";
		else if (category == 5)
			return "城市商业银行";
		else if (category == 6)
			return "农村商业银行";
		else if (category ==7)
			return "农村合作银行";
		else if (category == 8)
			return "农村信用社";
		else if (category == 9)
			return "邮政储蓄银行";
		else if (category == 10)
			return "金融资产管理公司";
		else if (category == 11)
			return "外资法人金融机构";
		else if (category == 12)
			return "中德住房储蓄银行";
		else if (category == 13)
			return "信托公司";
		else if (category == 14)
			return "企业集团财务公司";
		else if (category == 15)
			return "金融租赁公司";
		else if (category == 16)
			return "货币经纪公司";
		else if (category == 17)
			return "汽车金融公司";
		else if (category == 18)
			return "消费金融公司";
		else if (category == 19)
			return "村镇银行";
		else if (category == 20)
			return "贷款公司";
		else if (category == 21)
			return "农村资金互助社";
		else
			return "监管机构";
	}
}