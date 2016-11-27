package cn.gov.cbrc.bankriskcontrol.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;


/**
 * 消息通知
 * 
 */
@Entity
@Table(name="bank_message")
@NamedQuery(name="BankMessage.findAll", query="SELECT l FROM BankMessage l")
public class BankMessage implements Serializable {
	private static final long serialVersionUID = 5637832407156968582L;
	private Long messageId;
	private Date sendTime;//发送时间
	private String title;//消息标题
	private String message;//消息内容
	private Organization sendUser;//发送人
	private String orgs;
	private int critical;//紧急度 1为普通 2为重要 3为紧急

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	@Column(length=255)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(length=1024)
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@OneToOne(fetch=FetchType.EAGER)
	public Organization getSendUser() {
		return sendUser;
	}

	public void setSendUser(Organization sendUser) {
		this.sendUser = sendUser;
	}

	public String getOrgs() {
		return orgs;
	}

	public void setOrgs(String orgs) {
		this.orgs = orgs;
	}

	public int getCritical() {
		return critical;
	}

	public void setCritical(int critical) {
		this.critical = critical;
	}

	@Transient
	public String getCriticalStr() {
		if (critical == 1)
			return "普通";
		else if (critical == 2)
			return "重要";
		else
			return "紧急";
	}
}