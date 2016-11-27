package com.shtitan.timesynchronize.dao.user;

import org.springframework.stereotype.Repository;

import com.shtitan.timesynchronize.dao.HibernateDao;
import com.shtitan.timesynchronize.entity.Permission;


@Repository
public class PermissionDao extends HibernateDao<Permission, Long> {

}
