package com.shtitan.timesynchronize.service.account.impl;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shtitan.timesynchronize.dao.user.RoleDao;
import com.shtitan.timesynchronize.dao.user.UserDao;
import com.shtitan.timesynchronize.entity.User;
import com.shtitan.timesynchronize.service.account.UserService;
import com.shtitan.timesynchronize.util.Page;
import com.shtitan.timesynchronize.util.encode.EncodeUtils;


@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Override
	public void addUser(User user) {
		User temp=userDao.findUniqueBy("username", user.getUsername());
		if(temp!=null){
			throw new RuntimeException("用户名重复");
		}
		String md5Password=EncodeUtils.ecodeByMD5(user.getPasswd());
		user.setPasswd(md5Password);
		userDao.save(user);
	}
	
	@Override
	public User getUserByUserName(String username) {
		Criteria c = userDao.getSession().createCriteria(User.class);  
        c.add(Restrictions.eq("username", username));  
        c.setFetchMode("roles", FetchMode.JOIN);  
        return (User) c.uniqueResult();
	}
	
	@Override
	public User getUserById(long userId) {
		return userDao.get(userId);
	}
	
	@Override
	public void deleteUserByUserName(String username) {
		userDao.deleteEntityByProperty("username", username);		
	}
	
	@Override
	public void deleteUserById(long id) {
		userDao.delete(id);		
	}
	
	@Override
	public void updateUser(User user) {
		userDao.update(user);		
	}
	
	@Override
	public Page<User> getUsers(Page<User> page) {
		String hql="select distinct a from User a";
		Subject user = SecurityUtils.getSubject();
		String username=(String)user.getPrincipal();  
		User u = getUserByUserName(username);
		if(u.getOrganization().getCategory()!=1){
			//商行只能看到自身机构用户
			hql+=" where a.organization.orgId='" + u.getOrganization().getOrgId() + "'";
		}else{
			//银监会根据区域查询
			hql+=" where a.organization.areaCity.areeCode like '" + u.getOrganization().getAreaCity().getAreeCode() + "%'";
		}
		return userDao.findPage(page, hql);
	}

	@Override
	public List<User> getAllUsers() {
		return (List<User>)userDao.createQuery("select distinct a from User a", null).list();
	}

	@Override
	public User getUserByOrganizationCode(String code) {
		List<User> list=(List<User>)userDao.createQuery("select distinct a from User a where a.organization.orgNo='"+code+"'", null).list();
		return list.size()>0?list.get(0):null;
	}
}
