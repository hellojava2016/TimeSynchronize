package com.xj.console.bo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 全局参数
 * 
 * @author lpeng
 * 
 */
@Entity
@Table(name = "parameter")
public class Parameter implements Serializable {
	private static final long serialVersionUID = 6026016064438568851L;

	private long id;

	/**
	 * 现金贷保险手续费利率
	 */
	private double secureCommission_cash;

	/**
	 * 分期保险手续费利率
	 */
	private double secureCommission_period;

	/**
	 * 销售部ID
	 */
	private String saleId;

	/**
	 * 邮政储蓄银行商户客户号
	 */
	private String postBankId;

	/**
	 * 客户服务热线
	 */
	private String hotline;

	/**
	 * 还款账户户名
	 */
	private String repaymentAccountName;

	/**
	 * 还款账户开户银行
	 */
	private String repaymentBank;

	/**
	 * 还款账户/账号
	 */
	private String repaymentAccountId;

	/**
	 * 灵活还款服务包：12元/月
	 */
	private String flexibleRepayment;

	/**
	 * 最大现行合同数：2，现行的合同超过该数目则不允许录入新合同
	 */
	private int maxCurrentLoan;

	/**
	 * 拒绝等待天数：90天，合同拒绝后等待天数内不允许录入新合同
	 */
	private int refuseWaitDay;

	/**
	 * 现金贷最低期数：6，只有上个合同至少还款若干期后才允许进行现金贷
	 */
	private int minRepayBeforeCash;

	/**
	 * 最小变更还款日还款期数：2，对于未购买随心包的客户，只有至少还款了若干期后才允许提前还款
	 */
	private int minRepayBeforeChangeDate;

	/**
	 * 提前还款违约金：200，对于未购买随心包的客户如果提前还款需要支付违约金
	 */
	private int repaymentPenalty;

	/**
	 * 随心包提前还款最小期数：3，对于随心包的客户至少需要还完若干期后才能提前还款而不支付违约金
	 */
	private int minReplyBeforeFlexible;

	/**
	 * 随心包提前还款违约金：200，对于随心包的客户未还完最小期数就提前还款，则需支付违约金
	 */
	private int flexibleRepaymentPenalty;

	/**
	 * 犹豫期：15天,在犹豫期内可以一次性全部还款，按照合同提交日计算
	 */
	private int hesitationDate;

	/**
	 * 审核超时时间：20分钟，当一个人进入审核流程，直到超时其他人才可以再次进行审核
	 */
	private int verifyTimeout;

	/**
	 * 合同自动拒绝标记：XJR02、XJR03、XJR04，对于标记为自动拒绝的合同，系统经在10分钟内自动拒绝，无需人工审核
	 */
	private String autoRefuseTag;

	/**
	 * 一般销售能查询的最大天数：30天，出于业务流程和保密需要
	 */
	private int salesQueryDate;

	/**
	 * 销售经理能查询的最大天数：不限，出于业务流程和保密需要
	 */
	private int salesManagerQueryDate;

	/**
	 * 合并还款日最小间隔：15天，合并还款日后的最小间隔
	 */
	private int unionRepaymentInterval;

	/**
	 * 取消保险最少还款期数：3期，至少还完若干期才能取消保险
	 */
	private int minRepaymentBeforeCancleSecure;

	/**
	 * 取消随心包最少还款期数：3期，至少还完若干期才能取消随心包
	 */
	private int minRepaymentBeforeCancleFlexible;

	/**
	 * 延期还款最少还款期数：5期，至少还完若干期才能申请延期还款
	 */
	private int minRepaymentBeforeDelay;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getSecureCommission_cash() {
		return secureCommission_cash;
	}

	public void setSecureCommission_cash(double secureCommission_cash) {
		this.secureCommission_cash = secureCommission_cash;
	}

	public double getSecureCommission_period() {
		return secureCommission_period;
	}

	public void setSecureCommission_period(double secureCommission_period) {
		this.secureCommission_period = secureCommission_period;
	}

	public String getSaleId() {
		return saleId;
	}

	public void setSaleId(String saleId) {
		this.saleId = saleId;
	}

	public String getPostBankId() {
		return postBankId;
	}

	public void setPostBankId(String postBankId) {
		this.postBankId = postBankId;
	}

	public String getHotline() {
		return hotline;
	}

	public void setHotline(String hotline) {
		this.hotline = hotline;
	}

	public String getRepaymentAccountName() {
		return repaymentAccountName;
	}

	public void setRepaymentAccountName(String repaymentAccountName) {
		this.repaymentAccountName = repaymentAccountName;
	}

	public String getRepaymentBank() {
		return repaymentBank;
	}

	public void setRepaymentBank(String repaymentBank) {
		this.repaymentBank = repaymentBank;
	}

	public String getRepaymentAccountId() {
		return repaymentAccountId;
	}

	public void setRepaymentAccountId(String repaymentAccountId) {
		this.repaymentAccountId = repaymentAccountId;
	}

	public String getFlexibleRepayment() {
		return flexibleRepayment;
	}

	public void setFlexibleRepayment(String flexibleRepayment) {
		this.flexibleRepayment = flexibleRepayment;
	}

	public int getMaxCurrentLoan() {
		return maxCurrentLoan;
	}

	public void setMaxCurrentLoan(int maxCurrentLoan) {
		this.maxCurrentLoan = maxCurrentLoan;
	}

	public int getRefuseWaitDay() {
		return refuseWaitDay;
	}

	public void setRefuseWaitDay(int refuseWaitDay) {
		this.refuseWaitDay = refuseWaitDay;
	}

	public int getMinRepayBeforeCash() {
		return minRepayBeforeCash;
	}

	public void setMinRepayBeforeCash(int minRepayBeforeCash) {
		this.minRepayBeforeCash = minRepayBeforeCash;
	}

	public int getMinRepayBeforeChangeDate() {
		return minRepayBeforeChangeDate;
	}

	public void setMinRepayBeforeChangeDate(int minRepayBeforeChangeDate) {
		this.minRepayBeforeChangeDate = minRepayBeforeChangeDate;
	}

	public int getRepaymentPenalty() {
		return repaymentPenalty;
	}

	public void setRepaymentPenalty(int repaymentPenalty) {
		this.repaymentPenalty = repaymentPenalty;
	}

	public int getMinReplyBeforeFlexible() {
		return minReplyBeforeFlexible;
	}

	public void setMinReplyBeforeFlexible(int minReplyBeforeFlexible) {
		this.minReplyBeforeFlexible = minReplyBeforeFlexible;
	}

	public int getFlexibleRepaymentPenalty() {
		return flexibleRepaymentPenalty;
	}

	public void setFlexibleRepaymentPenalty(int flexibleRepaymentPenalty) {
		this.flexibleRepaymentPenalty = flexibleRepaymentPenalty;
	}

	public int getHesitationDate() {
		return hesitationDate;
	}

	public void setHesitationDate(int hesitationDate) {
		this.hesitationDate = hesitationDate;
	}

	public int getVerifyTimeout() {
		return verifyTimeout;
	}

	public void setVerifyTimeout(int verifyTimeout) {
		this.verifyTimeout = verifyTimeout;
	}

	public String getAutoRefuseTag() {
		return autoRefuseTag;
	}

	public void setAutoRefuseTag(String autoRefuseTag) {
		this.autoRefuseTag = autoRefuseTag;
	}

	public int getSalesQueryDate() {
		return salesQueryDate;
	}

	public void setSalesQueryDate(int salesQueryDate) {
		this.salesQueryDate = salesQueryDate;
	}

	public int getSalesManagerQueryDate() {
		return salesManagerQueryDate;
	}

	public void setSalesManagerQueryDate(int salesManagerQueryDate) {
		this.salesManagerQueryDate = salesManagerQueryDate;
	}

	public int getUnionRepaymentInterval() {
		return unionRepaymentInterval;
	}

	public void setUnionRepaymentInterval(int unionRepaymentInterval) {
		this.unionRepaymentInterval = unionRepaymentInterval;
	}

	public int getMinRepaymentBeforeCancleSecure() {
		return minRepaymentBeforeCancleSecure;
	}

	public void setMinRepaymentBeforeCancleSecure(
			int minRepaymentBeforeCancleSecure) {
		this.minRepaymentBeforeCancleSecure = minRepaymentBeforeCancleSecure;
	}

	public int getMinRepaymentBeforeCancleFlexible() {
		return minRepaymentBeforeCancleFlexible;
	}

	public void setMinRepaymentBeforeCancleFlexible(
			int minRepaymentBeforeCancleFlexible) {
		this.minRepaymentBeforeCancleFlexible = minRepaymentBeforeCancleFlexible;
	}

	public int getMinRepaymentBeforeDelay() {
		return minRepaymentBeforeDelay;
	}

	public void setMinRepaymentBeforeDelay(int minRepaymentBeforeDelay) {
		this.minRepaymentBeforeDelay = minRepaymentBeforeDelay;
	}

}
