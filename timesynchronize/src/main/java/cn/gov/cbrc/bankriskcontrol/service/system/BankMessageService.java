package cn.gov.cbrc.bankriskcontrol.service.system;

import java.util.Date;

import cn.gov.cbrc.bankriskcontrol.entity.BankMessage;
import cn.gov.cbrc.bankriskcontrol.entity.Organization;
import cn.gov.cbrc.bankriskcontrol.util.Page;

public interface BankMessageService {
	public Long addMessage(BankMessage message);
	public void updateMessage(BankMessage message);
	public void deleteMessage(long id);
	public Page<BankMessage> getMessages(Date startDate, Date endDate, Organization org,Page<BankMessage> page);
    public BankMessage getMessageById(long id);
}
