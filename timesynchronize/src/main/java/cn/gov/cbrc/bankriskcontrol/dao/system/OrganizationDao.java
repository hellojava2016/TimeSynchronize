package cn.gov.cbrc.bankriskcontrol.dao.system;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Repository;

import cn.gov.cbrc.bankriskcontrol.dao.HibernateDao;
import cn.gov.cbrc.bankriskcontrol.entity.Organization;
import cn.gov.cbrc.bankriskcontrol.util.Page;

@Repository
public class OrganizationDao extends HibernateDao<Organization, Long>{
	
	
	 /**
	  * 为 组合查询 和 模糊查询 提供的方法
	  * 接受传入参数（名称省份）
	  */
	 public Page<Organization> getPage(){
		return null;
		 
	 }
	 /**
	  *获取机构类型的列表
	  * 
	  * */
	 public List<Organization> getCategory(){
		 //构建具体的参数列表
		 
		 List list=find();
		return list;
		 
	 }
	
	

}
