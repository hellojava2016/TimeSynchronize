package com.shtitan.timesynchronize.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "global_parameter")
@NamedQuery(name = "GlobalParameter.findAll", query = "SELECT a FROM GlobalParameter a")
public class GlobalParameter implements Serializable {
	private static final long serialVersionUID = 8923928712268090531L;
	private long id;
	private int systemavailable;
	private int systemtransaction;
	private int operationchanges;
	private int fakesiteattachment;
	private int continueDecline;//连续下降期数
	private boolean continueDeclineEnable;
	private int deviateAverage;//偏离平均值百分比
	private boolean deviateAverageEnable;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getSystemavailable() {
		return systemavailable;
	}

	public void setSystemavailable(int systemavailable) {
		this.systemavailable = systemavailable;
	}

	public int getSystemtransaction() {
		return systemtransaction;
	}

	public void setSystemtransaction(int systemtransaction) {
		this.systemtransaction = systemtransaction;
	}

	public int getOperationchanges() {
		return operationchanges;
	}

	public void setOperationchanges(int operationchanges) {
		this.operationchanges = operationchanges;
	}

	public int getFakesiteattachment() {
		return fakesiteattachment;
	}

	public void setFakesiteattachment(int fakesiteattachment) {
		this.fakesiteattachment = fakesiteattachment;
	}

	public int getContinueDecline() {
		return continueDecline;
	}

	public void setContinueDecline(int continueDecline) {
		this.continueDecline = continueDecline;
	}

	public boolean isContinueDeclineEnable() {
		return continueDeclineEnable;
	}

	public void setContinueDeclineEnable(boolean continueDeclineEnable) {
		this.continueDeclineEnable = continueDeclineEnable;
	}

	public int getDeviateAverage() {
		return deviateAverage;
	}

	public void setDeviateAverage(int deviateAverage) {
		this.deviateAverage = deviateAverage;
	}

	public boolean isDeviateAverageEnable() {
		return deviateAverageEnable;
	}

	public void setDeviateAverageEnable(boolean deviateAverageEnable) {
		this.deviateAverageEnable = deviateAverageEnable;
	}
}