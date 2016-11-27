package com.shtitan.timesynchronize.dao.system;

import org.springframework.stereotype.Repository;

import com.shtitan.timesynchronize.dao.HibernateDao;
import com.shtitan.timesynchronize.entity.BankMessage;


@Repository
public class BankMessageDao extends HibernateDao<BankMessage, Long>{
   public long save2(BankMessage bmessage){
		return (Long)getSession().save(bmessage);
   }
}
