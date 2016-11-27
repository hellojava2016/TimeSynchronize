package cn.gov.cbrc.bankriskcontrol.dao.user;

import org.springframework.stereotype.Repository;

import cn.gov.cbrc.bankriskcontrol.dao.HibernateDao;
import cn.gov.cbrc.bankriskcontrol.entity.Role;

@Repository
public class RoleDao extends HibernateDao<Role, Long> {
}
