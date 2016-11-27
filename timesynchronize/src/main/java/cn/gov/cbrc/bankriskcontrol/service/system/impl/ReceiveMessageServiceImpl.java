package cn.gov.cbrc.bankriskcontrol.service.system.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.gov.cbrc.bankriskcontrol.dao.system.BankMessageReceiveDao;
import cn.gov.cbrc.bankriskcontrol.entity.BankMessage;
import cn.gov.cbrc.bankriskcontrol.entity.BankMessageReceiver;
import cn.gov.cbrc.bankriskcontrol.entity.User;
import cn.gov.cbrc.bankriskcontrol.service.system.ReceiveMessageService;
import cn.gov.cbrc.bankriskcontrol.util.DateUtil;
import cn.gov.cbrc.bankriskcontrol.util.Page;

@Service("receiveMessageService")
@Transactional
public class ReceiveMessageServiceImpl implements ReceiveMessageService {
	@Autowired
	private BankMessageReceiveDao bankMessageReceiveDao;

	@Override
	public void addReiceiveMessage(BankMessageReceiver m) {
		bankMessageReceiveDao.save(m);
		
	}

	@Override
	public void deleteReiceiveMessage(long l) {
		bankMessageReceiveDao.delete(l);
		
	}

	@Override
	public void addBatchRMessage(List<BankMessageReceiver> list) {
		 for(BankMessageReceiver mr:list)
			 bankMessageReceiveDao.save(mr);
	}

	@Override
	public void deleteBatchRMessage(List<BankMessageReceiver> list) {
		 for(BankMessageReceiver mr:list)
			 bankMessageReceiveDao.delete(mr);
		
	}

	@Override
	public void readMessage(BankMessageReceiver mr) {
		bankMessageReceiveDao.update(mr);
	}

	@Override
	public Page<BankMessageReceiver> getMessages(User u,Date startDate, Date endDate,boolean isread,Page<BankMessageReceiver> page) {
		String hql = "select distinct a from " + BankMessageReceiver.class.getSimpleName() + " a";
		List<String> list = new ArrayList<String>();
		list.add("a.receiver.orgId='" + u.getOrganization().getOrgId() + "'");
		if (startDate!=null)
			list.add("a.message.sendTime>='" + DateUtil.getShortDate(startDate) + "'");
		if (endDate!=null)
			list.add("a.message.sendTime<='" + DateUtil.getShortDate(DateUtils.addDays(endDate, 1)) + "'");		
		if (list.size() == 3) {
			hql = hql + " where " + list.get(0) + " and " + list.get(1)+ " and " + list.get(2);
		}
		if (list.size() == 2) {
			hql = hql + " where " + list.get(0) + " and " + list.get(1);
		}
		if (list.size() == 1) {
			hql = hql + " where " + list.get(0);
		}
		return bankMessageReceiveDao.findPage(page, hql+" order by a.message.sendTime desc");
	}

	@Override
	public BankMessageReceiver getBankMessageReceiverById(long id) {
		return bankMessageReceiveDao.get(id);
	}

	@Override
	public List<BankMessageReceiver> getNewMessage(User u) {
		return bankMessageReceiveDao.find("select distinct a from BankMessageReceiver a where a.receiver.orgId='"+u.getOrganization().getOrgId()+"' and a.hasRead='false' order by a.hasRead desc");
	}

	@Override
	public List<BankMessageReceiver> getNotDownloadMessage(long orgId) {
		return bankMessageReceiveDao.find("select distinct a from BankMessageReceiver a where a.receiver.orgId='"+orgId+"' and a.download='false'");
	}
	
	@Override
	public void updateBankMessageReceiver(BankMessageReceiver receiver) {
		bankMessageReceiveDao.update(receiver);
	}
}
