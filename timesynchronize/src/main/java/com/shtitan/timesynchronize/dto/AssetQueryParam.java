package com.shtitan.timesynchronize.dto;

import java.io.Serializable;

/**
 * 资产查询参数
 * 
 * @author ppl
 * 
 */
public class AssetQueryParam implements Serializable {
	private static final long serialVersionUID = -8057724356342439815L;

	private String uniqueVal;

	private String name;

	private long organizationId;

	public String getUniqueVal() {
		return uniqueVal;
	}

	public void setUniqueVal(String uniqueVal) {
		this.uniqueVal = uniqueVal;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}
}
