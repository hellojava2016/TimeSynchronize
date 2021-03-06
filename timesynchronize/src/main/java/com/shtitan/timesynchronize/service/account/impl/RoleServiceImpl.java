package com.shtitan.timesynchronize.service.account.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shtitan.timesynchronize.dao.user.PermissionDao;
import com.shtitan.timesynchronize.dao.user.RoleDao;
import com.shtitan.timesynchronize.entity.Permission;
import com.shtitan.timesynchronize.entity.Role;
import com.shtitan.timesynchronize.service.account.RoleService;
import com.shtitan.timesynchronize.util.Page;


@Service("roleService")
@Transactional
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private PermissionDao permissionDao;

	/**
	 * 获取角色分页对象
	 * 
	 * @param page
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<Role> listRolePage(Page<Role> page) {
		return roleDao.findPage(page, "select distinct a from Role a");
	}

	/**
	 * 根据ID 获取角色对象
	 * 
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public Role getRoleById(long id) {
		return roleDao.get(id);
	}

	/**
	 * 保存或更新角色对象
	 * 
	 * @param role
	 */
	@Override
	public void saveOrUpdateRole(Role role) {
		roleDao.save(role);

	}

	/**
	 * 删除指定ID 的角色对象
	 * 
	 * @param id
	 */
	@Override
	public void deleteRoleById(long id) {
		roleDao.delete(id);

	}
	
	@Override
	public void addRole(Role role) {
		roleDao.save(role);
	}

	@Override
	public void deleteRole(long id) {
		roleDao.delete(id);
	}
	
	@Override
	public List<Role> getAllRoles() {
		return roleDao.find("select distinct a from Role a");	
	}
	
	@Override
	public List<Permission> getAllPermission() {
		return permissionDao.getAll();
	}
	
	@Override
	public Permission getPermissionById(long id) {
		return permissionDao.get(id);
	}

	@Override
	public void updateRole(Role role) {
		roleDao.update(role);
		
	}

}
