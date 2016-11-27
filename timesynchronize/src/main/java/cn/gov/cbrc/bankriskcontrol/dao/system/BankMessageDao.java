package cn.gov.cbrc.bankriskcontrol.dao.system;

import org.springframework.stereotype.Repository;

import cn.gov.cbrc.bankriskcontrol.dao.HibernateDao;
import cn.gov.cbrc.bankriskcontrol.entity.BankMessage;

@Repository
public class BankMessageDao extends HibernateDao<BankMessage, Long>{
   public long save2(BankMessage bmessage){
		return (Long)getSession().save(bmessage);
   }
}
