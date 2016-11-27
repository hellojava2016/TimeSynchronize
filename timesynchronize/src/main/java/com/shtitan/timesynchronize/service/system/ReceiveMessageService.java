package com.shtitan.timesynchronize.service.system;

import java.util.Date;
import java.util.List;

import com.shtitan.timesynchronize.entity.BankMessageReceiver;
import com.shtitan.timesynchronize.entity.User;
import com.shtitan.timesynchronize.util.Page;


public interface ReceiveMessageService {
  public void addReiceiveMessage(BankMessageReceiver m);
  
  public void deleteReiceiveMessage(long l);
  
  public void addBatchRMessage(List<BankMessageReceiver> list);
  
  public void deleteBatchRMessage(List<BankMessageReceiver> list);
  
  public void readMessage(BankMessageReceiver m);
  
  public BankMessageReceiver getBankMessageReceiverById(long id);
  
  public Page<BankMessageReceiver> getMessages(User u,Date startDate, Date endDate,boolean isread,Page<BankMessageReceiver> page);
  
  public List<BankMessageReceiver> getNewMessage(User u);
  
  public List<BankMessageReceiver> getNotDownloadMessage(long orgId);
  
  public void updateBankMessageReceiver(BankMessageReceiver receiver);
}
