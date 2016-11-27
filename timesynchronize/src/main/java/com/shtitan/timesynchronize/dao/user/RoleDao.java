package com.shtitan.timesynchronize.dao.user;

import org.springframework.stereotype.Repository;

import com.shtitan.timesynchronize.dao.HibernateDao;
import com.shtitan.timesynchronize.entity.Role;


@Repository
public class RoleDao extends HibernateDao<Role, Long> {
}
