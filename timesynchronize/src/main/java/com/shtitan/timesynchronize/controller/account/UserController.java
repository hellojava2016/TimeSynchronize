package com.shtitan.timesynchronize.controller.account;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shtitan.timesynchronize.entity.Organization;
import com.shtitan.timesynchronize.entity.Permission;
import com.shtitan.timesynchronize.entity.Role;
import com.shtitan.timesynchronize.entity.User;
import com.shtitan.timesynchronize.service.account.RoleService;
import com.shtitan.timesynchronize.service.account.UserService;
import com.shtitan.timesynchronize.service.system.GlobalParameterService;
import com.shtitan.timesynchronize.service.system.OrganizationService;
import com.shtitan.timesynchronize.util.ExportUtil;
import com.shtitan.timesynchronize.util.Page;
import com.shtitan.timesynchronize.util.RiskUtils;
import com.shtitan.timesynchronize.util.encode.EncodeUtils;


/**
 * 
 * @author pl
 * 
 */
@Controller
@RequestMapping("/user")
public class UserController {
	private Logger logger = LoggerFactory.getLogger(UserController.class); 
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private GlobalParameterService globalParameterService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}	

	@RequestMapping("/logout.do")
	@ResponseBody
	public String loginout(HttpServletRequest request,HttpServletResponse response) {
		Subject user = SecurityUtils.getSubject();
		String username=(String)user.getPrincipal();  
		User oldu = userService.getUserByUserName(username);
				oldu.setOnline(false);	
		userService.updateUser(oldu);
		user.logout();
		try{
			request.getSession().invalidate();
		}catch(Exception ex){}
		return "success";
	}
	
	@RequestMapping(value = "/login.do")
	@ResponseBody
	public String login(User currUser,HttpSession session, HttpServletRequest request,Model model) {
		//在登陆的时候将报表参数设置一下，以免后续查询
		if(ExportUtil.parameter==null){
			ExportUtil.parameter=globalParameterService.getGlobalParameter();
		}
		String username = currUser.getUsername();
		String passwd = currUser.getPasswd();
		logger.info(username+" login");
		if(StringUtils.isBlank(username)||StringUtils.isBlank(passwd)){
			return "3";
		}
		Subject user = SecurityUtils.getSubject();
		if (!user.isAuthenticated() ) {			
			UsernamePasswordToken token = new UsernamePasswordToken(username,EncodeUtils.ecodeByMD5(passwd));
			token.setRememberMe(true);
			try {
				user.login(token);
				User userdb = userService.getUserByUserName(username);
				if(0!=userdb.getStatus()){
					token.clear();
					return "4";
				}
				request.getSession().setAttribute("user", userdb);
				Set<String> permisions = new HashSet<String>();
				for(Role role: userdb.getRoles() ){  
	                 for(Permission permission :role.getPermissions()){
	                	 permisions.add(permission.getPermissionName());
	                 } 
	             }  
				request.getSession().setAttribute("permisions", permisions);
				if (permisions.contains("risk:analyse"))
					return "1";
				else
					return "5";
			} catch (AuthenticationException e) {
				token.clear();
				return "2";
			} 
		}else{
			User userdb = userService.getUserByUserName(username);
			if(0!=userdb.getStatus()){
				return "4";
			}
			request.getSession().setAttribute("user", userdb);
			Set<String> permisions = new HashSet<String>();
			for(Role role: userdb.getRoles() ){  
                 for(Permission permission :role.getPermissions()){
                	 permisions.add(permission.getPermissionName());
                 } 
             }
			request.getSession().setAttribute("permisions", permisions);
			if (permisions.contains("risk:analyse"))
				return "1";
			else
				return "5";
		}
	}
	
	
	@RequestMapping("/loginerror.do")
	public ModelAndView loginError() {
		return new ModelAndView("/system/loginerror", null);
	}
	
	@RequestMapping("/permissionerror.do")
	public ModelAndView permissionError() {
		return new ModelAndView("/system/permissionerror", null);
	}	
	
	@RequestMapping(value="/add_pre.do")
	public String add_pre(Model model,HttpServletRequest request) {		
		model.addAttribute("roles", getJsonRoles(new HashSet<Role>()));
		String  pageNo = request.getParameter("pageNo");
		model.addAttribute("pageNo", pageNo);
		Subject user = SecurityUtils.getSubject();
		String username=(String)user.getPrincipal();  
		User oldu = userService.getUserByUserName(username);
		List<Organization> orgs=organizationService.getOrganizationsWithPermission(oldu,false);
		model.addAttribute("orgs", orgs);
		
		model.addAttribute("roles", getJsonRoles(new HashSet<Role>()));
		
		return "/system/user-input";
	}
	
	private String getJsonRoles(Set<Role> roles){
		StringBuilder sb = new StringBuilder();
		List<Role> list = roleService.getAllRoles();
		sb.append("[");
		sb.append("{'id':'10000','parent':'#',text:'所有'},");
		for(Role r:list){
					if(!containsRole(roles,r))
					  sb.append("{'id':'"+ r.getRoleId() +"','parent':'"+10000+"','text':'"+r.getRoleName()+"'},");
					else
					  sb.append("{'id':'"+ r.getRoleId() +"','parent':'"+10000+"','text':'"+r.getRoleName()+"','state':{'selected':'true'}},");	
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("]");
		return sb.toString();
	}
	
	private boolean containsRole(Set<Role> roles,Role r){
		for(Role rw:roles){
			if(rw.getRoleName().equals(r.getRoleName()))
				return true;
		}
		return false;
	}
	
	@RequestMapping(value="/add.do")
	@ResponseBody
	public String addUser(HttpServletRequest request,HttpServletResponse response) {
		long orgId=Long.parseLong(request.getParameter("Organization"));
		String username=request.getParameter("username");
		String passwd=request.getParameter("passwd");
		String realname=request.getParameter("realname");
		String telephone=request.getParameter("telephone");
		String cellphone=request.getParameter("cellphone");
		String email=request.getParameter("email");
		String[] pstr= request.getParameterValues("roles");
		String[] ids=pstr[0].split(",");
		Set<Role> set = new HashSet<Role>();
		for(String ridstr:ids){
			long rid = Long.parseLong(ridstr);
			if(rid>999)
				continue;
			Role r =roleService.getRoleById(rid);
			set.add(r);
		}
		User currentuser=(User)request.getSession().getAttribute("user");
		Set<Role> currentrole=currentuser.getRoles();
		if(!RiskUtils.containRoles(currentrole, set)){
			return "1";
		}
		
		Organization org=new Organization();
		org.setOrgId(orgId);
		
		User user=new User();
		user.setRoles(set);
		user.setOrganization(org);
		user.setUsername(username);
		user.setCellphone(cellphone);
		user.setCreateTime(new Date());
		user.setEmail(email);
		user.setPasswd(passwd);
		user.setRealname(realname);
		user.setTelephone(telephone);
		
		userService.addUser(user);
		return "2";
	}	
	
	@RequestMapping(value="/disable.do")
	public ModelAndView disable(Model model,HttpServletRequest request){
		long userId=Long.parseLong(request.getParameter("userId"));
		User user=userService.getUserById(userId);
		user.setStatus(2);
		userService.updateUser(user);	
		return new ModelAndView("redirect:/user/list.do");
	}
	
	@RequestMapping(value="/resetpassword.do")
	public ModelAndView resetPassword(Model model,HttpServletRequest request){
		long userId=Long.parseLong(request.getParameter("userId"));
		User user=userService.getUserById(userId);
		user.setPasswd(EncodeUtils.ecodeByMD5("admin"));
		userService.updateUser(user);	
		return new ModelAndView("redirect:/user/list.do");
	}
	
	@RequestMapping(value="/update_pre.do")
	public String update_pre(Model model,HttpServletRequest request){
		long userId=Long.parseLong(request.getParameter("userId"));
		User user=userService.getUserById(userId);
		model.addAttribute("modifyuser", user);
		String  pageNo = request.getParameter("pageNo");
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("roles", getJsonRoles(user.getRoles()));
		
		return "/system/user-update";
	}
	
	@RequestMapping(value="/update.do")
	@ResponseBody
	public String update(HttpServletRequest request,HttpServletResponse response){
		long userId=Long.parseLong(request.getParameter("userId"));
		User user=userService.getUserById(userId);
		
		String username=request.getParameter("username");
		String passwd=request.getParameter("passwd");
		String updatepassword=request.getParameter("updatepassword");
		boolean updatepasswordflag= Boolean.parseBoolean(updatepassword);
		String realname=request.getParameter("realname");
		String telephone=request.getParameter("telephone");
		String cellphone=request.getParameter("cellphone");
		String email=request.getParameter("email");
		String areaCode=request.getParameter("areaCode");
		String[] pstr= request.getParameterValues("roles");
		String[] ids=pstr[0].split(",");
		Set<Role> set = new HashSet<Role>();
		for(String ridstr:ids){
			long rid = Long.parseLong(ridstr);
			if(rid>999)
				continue;
			Role r =roleService.getRoleById(rid);
			set.add(r);
		}
		User currentuser=(User)request.getSession().getAttribute("user");
		Set<Role> currentrole=currentuser.getRoles();
		if(!RiskUtils.containRoles(currentrole, set)){
			return "1";
		}
		
		user.setRoles(set);
		user.setUsername(username);
		user.setAreaCode(areaCode);
		user.setCellphone(cellphone);
		user.setCreateTime(new Date());
		user.setEmail(email);
		if(!updatepasswordflag)
		    user.setPasswd(EncodeUtils.ecodeByMD5(passwd));
		user.setRealname(realname);
		user.setTelephone(telephone);
		
		userService.updateUser(user);
		return "2";
	}
	
	@RequestMapping(value="/delete.do")
	public ModelAndView delete(HttpServletRequest request,HttpServletResponse response){
		long userId=Long.parseLong(request.getParameter("deleteids"));
		User user=userService.getUserById(userId);
		int status = user.getStatus();
		if(0==status)
		   user.setStatus(1);
		else
		  user.setStatus(0);
		userService.updateUser(user);
		return null;
	}
	
	@RequestMapping(value="/list.do")
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
			HttpServletRequest request){
		Page<User> page=new Page<User>();
		page.setPageNo(pageNo);
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotEmpty(pageSize))
			  page.setPageSize(Integer.parseInt(pageSize));
		Page<User> list=userService.getUsers(page);
		model.addAttribute("users", list);
		model.addAttribute("sortTypes", sortType);
		model.addAttribute("pageNo", pageNo);
		User currentuser=(User)request.getSession().getAttribute("user");
		model.addAttribute("currentuserid", currentuser.getUserId());
		String message = request.getParameter("message");
		if(StringUtils.isEmpty(message)){
			message="";
		}
		if("1".equals(message)){
			model.addAttribute("message", "对不起，你所赋予的角色已超过自身的角色，请联系管理员！");
		}else if("2".equals(message)){
			model.addAttribute("message", "修改用户相关数据成功");
		}
 	    return "/system/user-list";
	}
	
	@RequestMapping(value="/checkname.do")
	@ResponseBody
	public boolean checkUserName(Model model,HttpServletRequest request){
		String username= request.getParameter("username");
		if(null==username)
			return false;
	      User user=userService.getUserByUserName(username);
	      if(null== user)
	    	  return true;
		return false;
	}
 
}
