package com.shtitan.timesynchronize.service.account;

import java.util.List;

import com.shtitan.timesynchronize.entity.User;
import com.shtitan.timesynchronize.util.Page;


/**
 * 用户服务
 * @author pl
 *
 */
public interface UserService {
	public void addUser(User user);
	
	public User getUserByUserName(String username);
	
	public User getUserById(long userId);
	
	public void deleteUserByUserName(String username);
	
	public void deleteUserById(long id);
	
	public void updateUser(User user);
	
	public Page<User> getUsers(Page<User> page);
	
	public List<User> getAllUsers();
	
	public User getUserByOrganizationCode(String code);
}
