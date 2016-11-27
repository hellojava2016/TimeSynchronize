package cn.gov.cbrc.bankriskcontrol.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the operate_log database table.
 * 
 */
@Entity
@Table(name="operate_log")
@NamedQuery(name="OperateLog.findAll", query="SELECT o FROM OperateLog o")
public class OperateLog implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String action;
	private String actionEn;//英文以免出现乱码
	private Date operateTime;
	private String params;
	private String url;
	private User user;

	public OperateLog() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="ACTIONS")
	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}


	public String getActionEn() {
		return actionEn;
	}


	public void setActionEn(String actionEn) {
		this.actionEn = actionEn;
	}


	
	@Column(name="OPERATE_TIME")
	public Date getOperateTime() {
		return this.operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}


	public String getParams() {
		return this.params;
	}

	public void setParams(String params) {
		this.params = params;
	}


	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="USER_ID")
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}