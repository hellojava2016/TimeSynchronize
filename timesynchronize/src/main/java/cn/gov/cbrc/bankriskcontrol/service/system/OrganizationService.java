package cn.gov.cbrc.bankriskcontrol.service.system;

import java.util.List;




import cn.gov.cbrc.bankriskcontrol.entity.Organization;
import cn.gov.cbrc.bankriskcontrol.entity.User;
import cn.gov.cbrc.bankriskcontrol.util.Page;

public interface OrganizationService {	
	/**
	 * 添加机构信息
	 * @param organization
	 * */
    public void addOrganization(Organization organization);
	/**
	 * 获取机构信息
	 * @param name
	 * */
	public Organization getOrganization(String propertyName, String name);
	/**
	 * 获取机构信息
	 * @param  orgId
	 * */	
	public Organization getOrganization(long orgId);
		
	public Organization getOrganizationByNo(String orgNo);
	/**
	 * 更新机构信息
	 * @param  organization
	 * */
	public void updateOrganization(Organization organization);
	/**
	 * 删除机构信息
	 * @param orgId
	 * */	
	public void deleteOrganization(long orgId);
	/**
	 * 删除机构信息
	 * @param  propertyName
	 * @param  value
	 * */
	public void deleteOrganization(String propertyName,String value);
		
	/**
	 * 查询属于某类或者不属于某类的机构列表
	 * @param category 类别
	 * @param equals true则属于 false则不属于
	 * @return
	 */
	public List<Organization> getOrganizations(int category,boolean equals);
	
	public List<Organization> getOrganizations(String propertyName,Object value);
	/**
	 * 
	 * 
	 * */
	public List<Organization> getAllOrganization();
	
	public List<Organization> getBelongBanksByUser(User user,boolean... notContainsAll);
	
	public List<Organization> getBelongOrganizationByUser(User user,boolean... notContainsAll);
	
	public List<Organization> getOrganizationsWithPermission(User user,boolean excludeSelf);
	/**
	 * 
	 * 
	 * */	
	public Page<Organization> getPage(int category,String areaCodes, String name,Page<Organization> page,boolean onlyBank);
	
	/**
	 * 为机构设置密钥 
	 * */
	public  String setSecretKey();
	
	/**
	 * 更改机构Key
	 * @param orgId
	 */
	public void changeKey(long orgId);
}
