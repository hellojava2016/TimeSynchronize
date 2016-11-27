package cn.gov.cbrc.bankriskcontrol.service.account;

import java.util.List;

import cn.gov.cbrc.bankriskcontrol.entity.User;
import cn.gov.cbrc.bankriskcontrol.util.Page;

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
