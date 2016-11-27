package com.shtitan.timesynchronize.service.account;

import java.util.List;

import com.shtitan.timesynchronize.entity.Permission;
import com.shtitan.timesynchronize.entity.Role;
import com.shtitan.timesynchronize.util.Page;


public interface RoleService {
	
	/**
	 * 获取角色分页对象
	 * @param page
	 * @return
	 */
	public Page<Role> listRolePage(Page<Role> page);
	
	/**
	 * 根据ID 获取角色对象
	 * @param id
	 * @return
	 */
	public Role getRoleById(long id);
	
	/**
	 * 保存或更新角色对象
	 * @param role
	 */
	public void saveOrUpdateRole(Role role);
	
	
	/**
	 * 删除指定ID 的角色对象
	 * @param id
	 */
	public void deleteRoleById(long id);
	
    public void addRole(Role role);
	
	public void deleteRole(long id);
	
	public List<Role> getAllRoles();
	
	public List<Permission> getAllPermission();
	
	public Permission getPermissionById(long id);
	
	public void updateRole(Role role);
}
