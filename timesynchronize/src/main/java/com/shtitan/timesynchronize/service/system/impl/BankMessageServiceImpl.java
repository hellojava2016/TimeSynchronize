package com.shtitan.timesynchronize.service.system.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shtitan.timesynchronize.dao.system.BankMessageDao;
import com.shtitan.timesynchronize.entity.BankMessage;
import com.shtitan.timesynchronize.entity.Organization;
import com.shtitan.timesynchronize.service.system.BankMessageService;
import com.shtitan.timesynchronize.util.DateUtil;
import com.shtitan.timesynchronize.util.Page;


@Service("bankMessageService")
@Transactional
public class BankMessageServiceImpl implements BankMessageService{
	@Autowired
	private BankMessageDao bankMessageDao;
	 
	@Override
	public Long addMessage(BankMessage message){
		return bankMessageDao.save2(message);
	}
	
	@Override
	public void updateMessage(BankMessage message){
		bankMessageDao.update(message);
	}
	
	@Override
	public void deleteMessage(long id){
		bankMessageDao.delete(id);
	}
	
	@Override
	public Page<BankMessage> getMessages(Date startDate, Date endDate, Organization org,Page<BankMessage> page) {
		String hql = "select distinct a from " + BankMessage.class.getSimpleName() + " a";
		List<String> list = new ArrayList<String>();
		if (startDate!=null)
			list.add("a.sendTime>='" + DateUtil.getLongDate(startDate) + "'");
		if (endDate!=null)
			list.add("a.sendTime<'" + DateUtil.getLongDate(DateUtils.addDays(endDate, 1)) + "'");	
		list.add("a.sendUser.orgId='" + org.getOrgId() + "'");
		if (list.size() == 3) {
			hql = hql + " where " + list.get(0) + " and " + list.get(1)+ " and " + list.get(2);
		}
		if (list.size() == 2) {
			hql = hql + " where " + list.get(0) + " and " + list.get(1);
		}
		if (list.size() == 1) {
			hql = hql + " where " + list.get(0);
		}
		return bankMessageDao.findPage(page, hql+" order by a.sendTime desc");
	}

	@Override
	public BankMessage getMessageById(long id) {
		return bankMessageDao.get(id);
	}
	
}
