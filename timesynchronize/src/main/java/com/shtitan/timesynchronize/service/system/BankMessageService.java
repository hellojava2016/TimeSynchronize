package com.shtitan.timesynchronize.service.system;

import java.util.Date;

import com.shtitan.timesynchronize.entity.BankMessage;
import com.shtitan.timesynchronize.entity.Organization;
import com.shtitan.timesynchronize.util.Page;


public interface BankMessageService {
	public Long addMessage(BankMessage message);
	public void updateMessage(BankMessage message);
	public void deleteMessage(long id);
	public Page<BankMessage> getMessages(Date startDate, Date endDate, Organization org,Page<BankMessage> page);
    public BankMessage getMessageById(long id);
}
