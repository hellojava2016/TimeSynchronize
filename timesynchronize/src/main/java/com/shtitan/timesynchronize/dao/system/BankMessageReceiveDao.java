package com.shtitan.timesynchronize.dao.system;

import org.springframework.stereotype.Repository;

import com.shtitan.timesynchronize.dao.HibernateDao;
import com.shtitan.timesynchronize.entity.BankMessageReceiver;


@Repository
public class BankMessageReceiveDao extends HibernateDao<BankMessageReceiver, Long>{

}
