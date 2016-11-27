package cn.gov.cbrc.bankriskcontrol.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.gov.cbrc.bankriskcontrol.util.ConvertUtils;


/**
 * 用户
 * 
 */
@Entity
@Table(name="users")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 226099522778737024L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="user_id")
	private long userId;

	@Column(name="area_code")
	private String areaCode;

	private String cellphone;

	@Column(name="create_time")
	private Date createTime;

	private String email;

	@Column(name="is_online")
	private boolean online;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ORG_ID")
	private Organization organization;

	private String passwd;

	private String realname;

	private int status;

	private String telephone;

	private String username;

	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "user_roles", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID") })
	private Set<Role> roles;

	public User() {
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
		if(organization!=null&&organization.getAreaCity()!=null)
			this.areaCode=organization.getAreaCity().getAreeCode();
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	@Transient
	public String getRoleStr(){
		List<String> list=new ArrayList<String>();
		for(Role role:roles){
			list.add(role.getRoleName());
		}
		return ConvertUtils.convertList2String(list, ",");
	}
}