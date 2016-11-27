package cn.gov.cbrc.bankriskcontrol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="backupdb")
@NamedQuery(name="BackupDB.findAll", query="SELECT a FROM BackupDB a")
public class BackupDB implements Serializable{
 
	private static final long serialVersionUID = 8617493968177339021L;
	private Long dbid;
	
	private String ip;
	
	private int port;
	
	private String username;
	
	private String password;
	
	private boolean taskstatus;
	
	private int daily;
	
	private Long checknum = 1L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="db_id")
	public Long getDbid() {
		return dbid;
	}

	public void setDbid(Long dbid) {
		this.dbid = dbid;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isTaskstatus() {
		return taskstatus;
	}

	public void setTaskstatus(boolean taskstatus) {
		this.taskstatus = taskstatus;
	}

	public int getDaily() {
		return daily;
	}

	public void setDaily(int daily) {
		this.daily = daily;
	}

	public Long getChecknum() {
		return checknum;
	}

	public void setChecknum(Long checknum) {
		this.checknum = checknum;
	}
	
}
