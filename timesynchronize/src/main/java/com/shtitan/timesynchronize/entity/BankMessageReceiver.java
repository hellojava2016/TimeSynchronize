package com.shtitan.timesynchronize.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 消息通知接收信息
 * 
 */
@Entity
@Table(name = "bank_message_receiver")
@NamedQuery(name = "BankMessageReceiver.findAll", query = "SELECT l FROM BankMessageReceiver l")
public class BankMessageReceiver implements Serializable {
	private static final long serialVersionUID = 56666666888888L;
	private Long id;
	private BankMessage message;// 消息
	private Organization receiver;// 接收人
	private boolean download;//北向接口是否已经下载
	private boolean hasRead;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToOne(fetch=FetchType.EAGER)
	public BankMessage getMessage() {
		return message;
	}

	public void setMessage(BankMessage message) {
		this.message = message;
	}

	@OneToOne(fetch=FetchType.EAGER)
	public Organization getReceiver() {
		return receiver;
	}

	public void setReceiver(Organization receiver) {
		this.receiver = receiver;
	}

	public boolean isDownload() {
		return download;
	}

	public void setDownload(boolean download) {
		this.download = download;
	}
	
	public boolean isHasRead() {
		return hasRead;
	}

	public void setHasRead(boolean hasRead) {
		this.hasRead = hasRead;
	}
}