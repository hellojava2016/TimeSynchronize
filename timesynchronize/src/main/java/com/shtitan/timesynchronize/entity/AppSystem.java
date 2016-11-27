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
 * The persistent class for the app_system database table.
 * 
 */
@Entity
@Table(name="app_system")
@NamedQuery(name="AppSystem.findAll", query="SELECT a FROM AppSystem a")
public class AppSystem implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long appId;
	private String appName;
	private String description;

	public AppSystem() {
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="APP_ID")
	public Long getAppId() {
		return this.appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}


	@Column(name="APP_NAME")
	public String getAppName() {
		return this.appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}


	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}