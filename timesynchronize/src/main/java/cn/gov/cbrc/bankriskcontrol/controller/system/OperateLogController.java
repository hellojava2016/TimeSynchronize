package cn.gov.cbrc.bankriskcontrol.controller.system;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.gov.cbrc.bankriskcontrol.entity.OperateLog;
import cn.gov.cbrc.bankriskcontrol.entity.Organization;
import cn.gov.cbrc.bankriskcontrol.entity.User;
import cn.gov.cbrc.bankriskcontrol.service.LogInterceptor;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.system.OperateLogService;
import cn.gov.cbrc.bankriskcontrol.service.system.OrganizationService;
import cn.gov.cbrc.bankriskcontrol.util.ConvertUtils;
import cn.gov.cbrc.bankriskcontrol.util.Page;
import cn.gov.cbrc.bankriskcontrol.util.ServletUtils;

@Controller
@RequestMapping(value = "/operatelog")
public class OperateLogController {
	@Autowired
	private OperateLogService operateLogService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrganizationService organizationService;

	@RequestMapping(value = "/list.do")
	public String list(
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, HttpServletRequest request) {
		Page<OperateLog> page = new Page<OperateLog>();
		page.setPageNo(pageNo);
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotEmpty(pageSize))
			  page.setPageSize(Integer.parseInt(pageSize));
		
		Subject user = SecurityUtils.getSubject();
		String username=(String)user.getPrincipal();  
		User oldu = userService.getUserByUserName(username);
		List<Organization> orgs=organizationService.getBelongOrganizationByUser(oldu,false);
		Page<OperateLog> list = operateLogService.getOperateLogs(oldu.getOrganization().getOrgId(), null, null,
				null, page);
		if(oldu.getOrganization().getCategory()==1)
			list = operateLogService.getOperateLogs(0, null, null,
					null, page);
		model.addAttribute("orgs", orgs);
		model.addAttribute("rates", list);
		model.addAttribute("actions",LogInterceptor.p);
		return "/system/operatelog-list";
	}

	@RequestMapping(value = "/query.do")
	public String query(
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, HttpServletRequest request) {
		Page<OperateLog> page = new Page<OperateLog>();
		page.setPageNo(pageNo);
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotEmpty(pageSize))
			  page.setPageSize(Integer.parseInt(pageSize));
		long orgId=ServletUtils.getRequestParamValue_int(request, "Organization");
		String action = request.getParameter("action");
		String startD = request.getParameter("startdate");
		String endD = request.getParameter("enddate");
		model.addAttribute("selectedorgid", orgId);
		model.addAttribute("action", action);
		model.addAttribute("startdate", startD);
		model.addAttribute("enddate", endD);
		Date startDate = StringUtils.isEmpty(startD)?null:(Date) ConvertUtils.convertStringToObject(startD,
				Date.class);
		Date endDate = StringUtils.isEmpty(endD)?null:(Date) ConvertUtils.convertStringToObject(endD,
				Date.class);
		Page<OperateLog> list = operateLogService.getOperateLogs(orgId,
				action, startDate, endDate, page);		
		model.addAttribute("rates", list);
		model.addAttribute("actions",LogInterceptor.p);
		
		Subject user = SecurityUtils.getSubject();
		String username=(String)user.getPrincipal();  
		User oldu = userService.getUserByUserName(username);
				oldu.setOnline(false);
		List<Organization> orgs=organizationService.getBelongOrganizationByUser(oldu,false);
		model.addAttribute("orgs", orgs);
		return "/system/operatelog-list";
	}
}
