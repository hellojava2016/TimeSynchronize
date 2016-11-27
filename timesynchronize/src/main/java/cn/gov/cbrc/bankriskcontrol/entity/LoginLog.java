package cn.gov.cbrc.bankriskcontrol.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the login_log database table.
 * 
 */
@Entity
@Table(name="login_log")
@NamedQuery(name="LoginLog.findAll", query="SELECT l FROM LoginLog l")
public class LoginLog implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long logId;
	private Date loginTime;
	private int status;
	private User user;

	public LoginLog() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="LOG_ID")
	public Long getLogId() {
		return this.logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}


	
	@Column(name="LOGIN_TIME")
	public Date getLoginTime() {
		return this.loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}


	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
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