package com.xj.console.bo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 区域
 * @author lpeng
 *
 */
@Entity
@Table(name = "domain")
public class Domain implements Serializable {
	private static final long serialVersionUID = 6609949186152107985L;

	private long id;

	/**
	 * 父区域ID，如果无父区域则为0
	 */
	private long parentId;

	private String name;

	private String description;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
