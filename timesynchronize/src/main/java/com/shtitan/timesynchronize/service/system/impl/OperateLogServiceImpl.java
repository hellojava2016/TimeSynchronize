package com.shtitan.timesynchronize.service.system.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shtitan.timesynchronize.dao.system.OperateLogDao;
import com.shtitan.timesynchronize.entity.OperateLog;
import com.shtitan.timesynchronize.entity.User;
import com.shtitan.timesynchronize.service.account.UserService;
import com.shtitan.timesynchronize.service.system.OperateLogService;
import com.shtitan.timesynchronize.util.DateUtil;
import com.shtitan.timesynchronize.util.Page;



@Service("operateLogService")
@Transactional
public class OperateLogServiceImpl implements OperateLogService {
	@Autowired
	private OperateLogDao operateLogDao;
	
	@Autowired
	private UserService userService;

	@Override
	public void addOperateLog(OperateLog log) {
		operateLogDao.save(log);
	}
	
	@Override
	public Page<OperateLog> getOperateLogs(long orgId, String action,
			Date startDate, Date endDate, Page<OperateLog> page) {
		String hql = "select distinct a from " + OperateLog.class.getSimpleName() + " a";
		List<String> list = new ArrayList<String>();
		if (orgId != 0)//指定机构
			list.add("a.user.organization.orgId='" + orgId + "'");
		else{//未指定机构则根据User自身机构是银监会还是商行来查询
			Subject user = SecurityUtils.getSubject();
			String username=(String)user.getPrincipal();  
			User u = userService.getUserByUserName(username);
			if(u.getOrganization().getCategory()!=1){
				//商行根据组织机构来查询
				list.add("a.user.organization.orgId='" + u.getOrganization().getOrgId() + "'");
			}else{
				//银监会根据区域查询
				list.add("a.user.organization.areaCity.areeCode like '" + u.getOrganization().getAreaCity().getAreeCode() + "%'");
			}			
		}
		if (StringUtils.isNotEmpty(action))
			list.add("a.actionEn='" + action + "'");
		if (startDate!=null)
			list.add("a.operateTime>='" + DateUtil.getLongDate(startDate) + "'");
		if (endDate!=null)
			list.add("a.operateTime<'" + DateUtil.getLongDate(DateUtils.addDays(endDate, 1)) + "'");		
		if (list.size() == 4) {
			hql = hql + " where " + list.get(0) + " and " + list.get(1) + " and " + list.get(2)+ " and " + list.get(3);
		}
		if (list.size() == 3) {
			hql = hql + " where " + list.get(0) + " and " + list.get(1) + " and " + list.get(2);
		}
		if (list.size() == 2) {
			hql = hql + " where " + list.get(0) + " and " + list.get(1);
		}
		if (list.size() == 1) {
			hql = hql + " where " + list.get(0);
		}
		return operateLogDao.findPage(page, hql+" order by a.operateTime desc");
	}
}
