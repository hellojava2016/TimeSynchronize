package com.shtitan.timesynchronize.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the area_city database table.
 * 
 */
@Entity
@Table(name="area_city")
@NamedQuery(name="AreaCity.findAll", query="SELECT a FROM AreaCity a")
public class AreaCity implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long areaId;
	private String areeCode;
	private String name;
	private AreaCity areaCity;
	
	public AreaCity() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="AREA_ID")
	public Long getAreaId() {
		return this.areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}


	@Column(name="AREE_CODE")
	public String getAreeCode() {
		return this.areeCode;
	}

	public void setAreeCode(String areeCode) {
		this.areeCode = areeCode;
	}


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


	@ManyToOne
	@JoinColumn(name="PARENT_AREA_ID")
	public AreaCity getAreaCity() {
		return this.areaCity;
	}

	public void setAreaCity(AreaCity areaCity) {
		this.areaCity = areaCity;
	}
}
