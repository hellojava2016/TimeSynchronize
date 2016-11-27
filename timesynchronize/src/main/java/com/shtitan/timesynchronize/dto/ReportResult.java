package com.shtitan.timesynchronize.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ReportResult")
public class ReportResult implements Serializable {
	private static final long serialVersionUID = -7525284169327940631L;
	private int code;
	private String comments;

	public ReportResult() {
		super();
	}

	public ReportResult(int code, String comments) {
		super();
		this.code = code;
		this.comments = comments;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
