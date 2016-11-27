package com.shtitan.timesynchronize.controller.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shtitan.timesynchronize.entity.BankMessage;
import com.shtitan.timesynchronize.entity.BankMessageReceiver;
import com.shtitan.timesynchronize.entity.Organization;
import com.shtitan.timesynchronize.entity.User;
import com.shtitan.timesynchronize.service.account.UserService;
import com.shtitan.timesynchronize.service.system.BankMessageService;
import com.shtitan.timesynchronize.service.system.OrganizationService;
import com.shtitan.timesynchronize.service.system.ReceiveMessageService;
import com.shtitan.timesynchronize.util.ConvertUtils;
import com.shtitan.timesynchronize.util.Page;


@Controller
@RequestMapping(value = "/message")
public class MessageController {
	@Autowired
	private BankMessageService bankMessageService;
	
	@Autowired
	private ReceiveMessageService receiveMessageService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrganizationService organizationService;
	
	@RequestMapping(value = "/list.do")
	public String getMessageList(
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, HttpServletRequest request) {
		Page<BankMessage> page = new Page<BankMessage>();
		page.setPageNo(pageNo);
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotEmpty(pageSize))
			  page.setPageSize(Integer.parseInt(pageSize));
		 
		Date startDate=null;
		String startdatestr =request.getParameter("startdate");
		if(StringUtils.isNotEmpty(startdatestr)){
			startDate=(Date)ConvertUtils.convertStringToObject(startdatestr, Date.class);
		}else{
			startdatestr="";
		}
		Date endDate=null;
		String endtdatestr =request.getParameter("enddate");
		if(StringUtils.isNotEmpty(endtdatestr)){
			endDate=(Date)ConvertUtils.convertStringToObject(endtdatestr, Date.class);
		}else{
			endtdatestr="";
		}
		
		model.addAttribute("startdate", startdatestr);
		model.addAttribute("enddate", endtdatestr);
		
		User user=(User)request.getSession().getAttribute("user");
		Page<BankMessage> list = bankMessageService.getMessages(startDate, endDate, user.getOrganization(),page);
		model.addAttribute("bankmessages", list);
		model.addAttribute("pageNo", pageNo);
		
		String message = request.getParameter("message");
		if(StringUtils.isNotEmpty(message)){
			if("addsucess".equals(message))
				   model.addAttribute("message","添加消息成功");
			else if("deletesucess".equals(message))
				   model.addAttribute("message","删除信息成功");
			else if("adderror".equals(message))
				   model.addAttribute("message","添加信息,请重新添加");
			else if("deleteerror".equals(message))
				   model.addAttribute("message","删除信息失败,请重新删除");
			else if("updatesucess".equals(message))
				   model.addAttribute("message","修改消息成功");
			else if("updateerror".equals(message))
				   model.addAttribute("message","修改消息失败,请重新修改");
			else
				   model.addAttribute("message",message); 
		}	
		return "/system/bankmessage-list";
	}
	
	@RequestMapping(value = "/add.do")
	public ModelAndView add(HttpServletRequest request){
		try{
			String title = request.getParameter("title");
			String message = request.getParameter("message");
			String orgs = request.getParameter("orgs");
			String critical = request.getParameter("critical");
			User user=(User)request.getSession().getAttribute("user");
			BankMessage bmessage = new BankMessage();
			bmessage.setSendUser(user.getOrganization());
			bmessage.setMessage(message);
			bmessage.setTitle(title);
			bmessage.setOrgs(orgs);
			bmessage.setCritical(Integer.parseInt(critical));
			bmessage.setSendTime(new Date(System.currentTimeMillis()));
			Long newid=bankMessageService.addMessage(bmessage);
			bmessage.setMessageId(newid);
			List<BankMessageReceiver> bmrs = new ArrayList<BankMessageReceiver>();
			for(String id:orgs.split(",")){
				Organization org=new Organization();
				long orgId=Long.parseLong(id);
				if(orgId==10000)
					continue;
				org.setOrgId(orgId);
				BankMessageReceiver bmr = new BankMessageReceiver();
				bmr.setHasRead(false);
				bmr.setMessage(bmessage);
				bmr.setReceiver(org);
				bmrs.add(bmr);
			}
			receiveMessageService.addBatchRMessage(bmrs);
			
		}catch(Exception ex){
			String listurl=(String)request.getSession().getAttribute("listurl");
			String queryparam=(String)request.getSession().getAttribute("queryparam");
			return new ModelAndView("redirect:"+listurl+"?message=adderror&"+queryparam);
		}
		String listurl=(String)request.getSession().getAttribute("listurl");
		String queryparam=(String)request.getSession().getAttribute("queryparam");
		return new ModelAndView("redirect:"+listurl+"?message=addsucess&"+queryparam);
	}
	
	@RequestMapping(value = "/update.do")
	public ModelAndView update(HttpServletRequest request){
		try{
			String title = request.getParameter("title");
			String message = request.getParameter("message");
			String id = request.getParameter("id");
			String critical = request.getParameter("critical");
			BankMessage oldmessage=bankMessageService.getMessageById(Long.parseLong(id));
			oldmessage.setMessage(message);
			oldmessage.setTitle(title);
			oldmessage.setCritical(Integer.parseInt(critical));
			oldmessage.setSendTime(new Date(System.currentTimeMillis()));
			bankMessageService.updateMessage(oldmessage);	
		}catch(Exception ex){
			String listurl=(String)request.getSession().getAttribute("listurl");
			String queryparam=(String)request.getSession().getAttribute("queryparam");
			return new ModelAndView("redirect:"+listurl+"?message=updateerror&"+queryparam);
		}
		String listurl=(String)request.getSession().getAttribute("listurl");
		String queryparam=(String)request.getSession().getAttribute("queryparam");
		return new ModelAndView("redirect:"+listurl+"?message=updatesucess&"+queryparam);
	}
	
	@RequestMapping(value = "/delete.do")
	public ModelAndView delete(HttpServletRequest request){
		try{
			String deleteids = request.getParameter("deleteids");
			String[] ids = deleteids.split(",");
			for (String temp : ids) {
				long id = Long.parseLong(temp);
				bankMessageService.deleteMessage(id);
			}
		}catch(Exception ex){
			String listurl=(String)request.getSession().getAttribute("listurl");
			String queryparam=(String)request.getSession().getAttribute("queryparam");
			return new ModelAndView("redirect:"+listurl+"?message=updateerror&"+queryparam);
		}
		String listurl=(String)request.getSession().getAttribute("listurl");
		String queryparam=(String)request.getSession().getAttribute("queryparam");
		return new ModelAndView("redirect:"+listurl+"?message=updatesucess&"+queryparam);
	}
	
	@RequestMapping(value = "/add_pre.do")
	public String add_pre(Model model){
		model.addAttribute("orgs", getJsonOrgs(""));
		return "/system/bankmessage-add";
	}
	
	private String getJsonOrgs(String oldorgs){
		StringBuilder sb = new StringBuilder();
		Set<String> set = new HashSet<String>();
		if(StringUtils.isNotEmpty(oldorgs)){
			String[] ids = oldorgs.split(",");
			for(String id:ids){
				set.add(id);
			}
		}
		Subject user = SecurityUtils.getSubject();
		String username=(String)user.getPrincipal();  
		User userDb = userService.getUserByUserName(username);
		List<Organization> list=organizationService.getOrganizationsWithPermission(userDb,true);
		sb.append("[");
		sb.append("{'id':'10000','parent':'#',text:'全部'},");
		for(Organization org:list){
			if(!set.contains(""+org.getOrgId()))
			  sb.append("{'id':'"+ org.getOrgId() +"','parent':'10000','text':'"+org.getName()+"'},");
			else
			  sb.append("{'id':'"+ org.getOrgId() +"','parent':'10000','text':'"+org.getName()+"','state':{'selected':'true'}},");	
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("]");
		return sb.toString();
	}
	
	@RequestMapping(value = "/update_pre.do")
	public String update_pre(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		BankMessage oldmessage=bankMessageService.getMessageById(Long.parseLong(id));
		model.addAttribute("oldmessage", oldmessage);
		model.addAttribute("orgs", getJsonOrgs(oldmessage.getOrgs()));
		return "/system/bankmessage-update";
	}
	
}
