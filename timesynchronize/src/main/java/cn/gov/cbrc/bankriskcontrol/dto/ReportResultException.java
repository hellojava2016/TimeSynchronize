package cn.gov.cbrc.bankriskcontrol.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 上报异常
 * 
 * @author pl
 * 
 */
@XmlRootElement(name = "ReportResultException")
public class ReportResultException extends RuntimeException {
	private static final long serialVersionUID = 1588518497230079515L;
	private int code;
	private String comments;

	public ReportResultException() {
		super();
	}

	public ReportResultException(int code, String comments) {
		super(comments);
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
	
	public ReportResult getReportResult(){
		return new ReportResult(code,comments);
	}
}
