package com.shtitan.timesynchronize.service.system.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shtitan.timesynchronize.dao.system.OrganizationDao;
import com.shtitan.timesynchronize.entity.Organization;
import com.shtitan.timesynchronize.entity.User;
import com.shtitan.timesynchronize.service.system.OrganizationService;
import com.shtitan.timesynchronize.util.ConvertUtils;
import com.shtitan.timesynchronize.util.Page;
import com.shtitan.timesynchronize.util.ServletUtils;
import com.shtitan.timesynchronize.util.PropertyFilter.MatchType;

@Service("organizationService")
@Transactional
public class OrganizationServiceImpl implements OrganizationService{

	@Autowired
	private OrganizationDao organizationDao;

	@Override
	public void addOrganization(Organization organization) {
		organization.setSecretKey(this.setSecretKey());
		organizationDao.save(organization);
	}

	@Override
	public Organization getOrganization(String propertyName,String name) {
		return organizationDao.findUniqueBy(propertyName, name);
	}

	@Override
	public Organization getOrganization(long orgId) {
		return organizationDao.get(orgId);
	}
	
	@Override
	public Organization getOrganizationByNo(String orgNo) {
		return organizationDao.findUniqueBy("orgNo", orgNo);
	}

	@Override
	public void updateOrganization(Organization organization) {
		organizationDao.update(organization);
		
	}

	@Override
	public void deleteOrganization(long orgId) {
		organizationDao.delete(orgId);		
	}

	@Override
	public void deleteOrganization(String propertyName, String value) {
		organizationDao.deleteEntityByProperty(propertyName, value);		
	}

	@Override
	public List<Organization> getOrganizations(int category,boolean equals) {
		if(equals){
			return organizationDao.findBy("category", category);
		}else{
			return organizationDao.findBy("category", category, MatchType.NE);
		}
	}

	@Override
	public List<Organization> getOrganizations(String propertyName,
			Object value) {
		return organizationDao.findBy(propertyName, value);
	}

	@Override
	public List<Organization> getAllOrganization() {
		return organizationDao.getAll();
	}
	
	@Override
	public List<Organization> getBelongBanksByUser(User user,boolean... notContainsAll) {
		List<Organization> list=new ArrayList<Organization>();		
		Organization organization=user.getOrganization();		
		if(organization.getCategory()==1){//银监会
			if (notContainsAll.length == 0 || !notContainsAll[0]) {//用户查询界面显示
				Organization org = new Organization();
				org.setOrgId(0L);
				org.setName("全部机构");
				list.add(org);
			}
			List<Organization> banks=organizationDao.find("from Organization org where org.areaCity.areeCode like '"+organization.getAreaCity().getAreeCode()+"%' and org.category!=1");
			list.addAll(banks);
		}else{//银行
			list.add(organization);
		}
		return list;
	}
	
	@Override
	public List<Organization> getBelongOrganizationByUser(User user,boolean... notContainsAll) {
		List<Organization> list=new ArrayList<Organization>();		
		Organization organization=user.getOrganization();		
		if(organization.getCategory()==1){//银监会
			if (notContainsAll.length == 0 || !notContainsAll[0]) {//用户查询界面显示
				Organization org = new Organization();
				org.setOrgId(0L);
				org.setName("全部机构");
				list.add(org);
			}
			List<Organization> banks=organizationDao.find("from Organization org where org.areaCity.areeCode like '"+organization.getAreaCity().getAreeCode()+"%' order by org.areaCity.areeCode asc");
			list.addAll(banks);
		}else{//银行
			list.add(organization);
		}
		return list;
	}
	
	@Override
	public List<Organization> getOrganizationsWithPermission(User user,boolean excludeSelf) {
		List<Organization> list=new ArrayList<Organization>();		
		Organization organization=user.getOrganization();		
		if(organization.getCategory()==1){//银监会			
			List<Organization> banks = organizationDao.find("from Organization org where org.areaCity.areeCode like '"
					+ organization.getAreaCity().getAreeCode() + "%' order by org.category asc");
			list.addAll(banks);
			if (excludeSelf) {
				for (Iterator<Organization> iterator = list.iterator(); iterator.hasNext();) {
					Organization org = iterator.next();
					if (org.getOrgId() == user.getOrganization().getOrgId())
						iterator.remove();
				}
			}
		} else{//银行
			list.add(organization);
		}
		return list;
	}

	@Override
	public Page<Organization> getPage(int category,String areaCode,String name,
			Page<Organization> page,boolean onlybank) {
		String hql="select distinct a from Organization a";
		List<String> list = new ArrayList<String>();
		if(category!=0){
			list.add("a.category='"+category+"'");
		}else if(onlybank){
			list.add("a.category!='1'");
		}
		if(StringUtils.isNoneEmpty(areaCode)){
			list.add("a.areaCity.areeCode like '"+areaCode+"%'");
		}
		if(StringUtils.isNoneEmpty(name)){
			list.add("a.name like '%"+name+"%'");
		}
		if(list.size()==3)
			hql=hql+" where "+list.get(0)+" and "+list.get(1)+" and "+list.get(2);
		if(list.size()==2)
			hql=hql+" where "+list.get(0)+" and "+list.get(1);
		if(list.size()==1)
			hql=hql+" where "+list.get(0);
		return organizationDao.findPage(page, hql);
	}

	/**
	 * 为机构设置秘钥 32 位 数字组成。
	 * 
	 * @author lyq
	 */
	public String setSecretKey() {
		// 有时间产生13位数字和定义的010三位数字。再随机产生16位数字
		long secretKey = System.currentTimeMillis();

		Random ran = new Random();
		StringBuffer mess = new StringBuffer();
		int num1 = 0;
		for (int i = 0; i < 16 / 8; i++) {// 这里是产生16位的16/8=2次，
			while (true) {
				num1 = ran.nextInt(99999999);
				if (num1 > 10000000) {
					mess.append(num1);
					break;
				}
			}
		}
		return "010" + mess + String.valueOf(secretKey);
	}

	@Override
	public void changeKey(long orgId) {
		Organization org = getOrganization(orgId);
		org.setSecretKey(this.setSecretKey());
		updateOrganization(org);
	}
}
