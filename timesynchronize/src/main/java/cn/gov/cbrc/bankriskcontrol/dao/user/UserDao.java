package cn.gov.cbrc.bankriskcontrol.dao.user;

import org.springframework.stereotype.Repository;

import cn.gov.cbrc.bankriskcontrol.dao.HibernateDao;
import cn.gov.cbrc.bankriskcontrol.entity.User;

@Repository
public class UserDao extends HibernateDao<User, Long> {

}
