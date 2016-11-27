package com.shtitan.timesynchronize.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the permission database table.
 * 
 */
@Entity
@Table(name="permission")
@NamedQuery(name="Permission.findAll", query="SELECT p FROM Permission p")
public class Permission implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long permissionId;
	private String description;
	private String groupName;
	private String moduleName;
	private String permissionName;

	public Permission() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="PERMISSION_ID")
	public Long getPermissionId() {
		return this.permissionId;
	}

	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
	}


	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	@Column(name="GROUP_NAME")
	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}


	@Column(name="MODULE_NAME")
	public String getModuleName() {
		return this.moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}


	@Column(name="PERMISSION_NAME")
	public String getPermissionName() {
		return this.permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}
	
	@Override
	public boolean equals(Object o){
		if(null==o)
			return false;
		if(o instanceof Permission){
			Permission p =(Permission)o;
			if(p.getPermissionId()==getPermissionId())
				return true;
		}
		return false;
	}
}
