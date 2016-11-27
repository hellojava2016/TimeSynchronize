package cn.gov.cbrc.bankriskcontrol.controller.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.gov.cbrc.bankriskcontrol.entity.Permission;
import cn.gov.cbrc.bankriskcontrol.entity.Role;
import cn.gov.cbrc.bankriskcontrol.entity.User;
import cn.gov.cbrc.bankriskcontrol.service.account.RoleService;
import cn.gov.cbrc.bankriskcontrol.util.Page;
import cn.gov.cbrc.bankriskcontrol.util.RiskUtils;

@Controller
@RequestMapping(value = "/role")
public class RoleController {

	@Autowired
	private RoleService roleService;

	@RequestMapping(value="/list.do")
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
			HttpServletRequest request){
		Page<Role> page=new Page<Role>();
		page.setPageNo(pageNo);
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotEmpty(pageSize))
			  page.setPageSize(Integer.parseInt(pageSize));
		page.setOrderBy(sortType); 
		Page<Role> list=roleService.listRolePage(page);
		model.addAttribute("roles", list);
		model.addAttribute("sortTypes", sortType);
		String message = request.getParameter("message");
		if(StringUtils.isEmpty(message)){
			message="";
		}
		if("1".equals(message)){
			model.addAttribute("message", "对不起，你所赋予的权限已超过自身的权限，请联系管理员！");
		}else if("deleteerror".equals(message)){
			model.addAttribute("message", "删除角色失败,请检查该角色是否已经被使用");
		}else if("deletesuccess".equals(message)){
			model.addAttribute("message", "删除角色成功");
		}
 	    return "/system/role-list";
	}
	
	@RequestMapping(value="/add_pre.do")
	public String add_pre(Model model,HttpServletRequest request) {
		model.addAttribute("permisionss", getJsonPermisions(null));
		return "/system/role-add";
	}
	
	@RequestMapping(value="/add.do")
	public ModelAndView save(Model model,HttpServletRequest request) {
		Role role = new Role();
		String description= request.getParameter("description");
		String permisionstr= request.getParameter("permisionstr");		
		Set<Permission> set = new HashSet<Permission>();
		if (StringUtils.isNotEmpty(permisionstr)) {
			String[] pstr = permisionstr.split(",");
			for (String pid : pstr) {
				long permissionId = Long.parseLong(pid);
				if (permissionId > 9999)
					continue;
				set.add(roleService.getPermissionById(permissionId));
			}
		}
		User currentuser=(User)request.getSession().getAttribute("user");
		Set<Permission> currentPermision =  new HashSet<Permission>();
		for(Role crole:currentuser.getRoles()){
			if(CollectionUtils.isNotEmpty(crole.getPermissions()))
			   currentPermision.addAll(crole.getPermissions());
		}
		role.setPermissions(set);
		role.setDescription(description);
		role.setRoleName(request.getParameter("roleName"));
		roleService.addRole(role);
		return new ModelAndView("redirect:/role/list.do");
	}
	
	@RequestMapping(value = "/delete.do")
	@ResponseBody
	public String delete(
			HttpServletRequest request, HttpServletResponse response) {
		try{
			String deleteids= request.getParameter("deleteids");
			String[] ids=deleteids.split(",");
			for(String temp:ids){
				long id = Long.parseLong(temp);
				roleService.deleteRoleById(id);
			}
			return "deletesuccess";
		}catch(Exception ex){
			return "deleteerror";
		}
		
	}
	
	@RequestMapping(value="/update_pre.do")
	public String update_pre(Model model,HttpServletRequest request) {
		String roleid= request.getParameter("roleId");
		long rid = Long.parseLong(roleid);
		Role r = roleService.getRoleById(rid);
		model.addAttribute("role", r);
		model.addAttribute("permisionss", getJsonPermisions(r.getPermissions()));
		return "/system/role-update";
	}
	
	@RequestMapping(value="/update.do")
	@ResponseBody
	public String update(Model model,HttpServletRequest request) {
		String roleid= request.getParameter("roleId");
		long rid = Long.parseLong(roleid);
		Role role = roleService.getRoleById(rid);
		String description= request.getParameter("description");
		String permisionstr= request.getParameter("permisionstr");
		String[] pstr=permisionstr.split(",");
		Set<Permission> set = new HashSet<Permission>();
		for(String pid:pstr){
			long permissionId = Long.parseLong(pid);
			if(permissionId>9999)
				continue;
			set.add(roleService.getPermissionById(permissionId));
		}
		User currentuser=(User)request.getSession().getAttribute("user");
		Set<Permission> currentPermision =  new HashSet<Permission>();
		for(Role crole:currentuser.getRoles()){
			if(CollectionUtils.isNotEmpty(crole.getPermissions()))
			   currentPermision.addAll(crole.getPermissions());
		}
		if(!RiskUtils.containPermissions(currentPermision,set)){
			return "1";
		}
		role.setPermissions(set);
		role.setDescription(description);
		roleService.updateRole(role);
		return "2";
	}
	
	private String getJsonPermisions(Set<Permission> selectedPermission){
		StringBuilder sb = new StringBuilder();
		List<Permission> list = roleService.getAllPermission();
		Map<String,List<Permission>> map = new HashMap<String,List<Permission>>();
		for(Permission p:list){
			if(map.containsKey(p.getModuleName()))
				  map.get(p.getModuleName()).add(p);
			else{
				 map.put(p.getModuleName(), new ArrayList<Permission>());
				 map.get(p.getModuleName()).add(p);
			}
		}
		sb.append("[");
		int basemodule = 10000;
		for(String module:map.keySet()){
			List<Permission> ps = map.get(module);
			if(CollectionUtils.isNotEmpty(ps)){
				basemodule++;
				sb.append("{'id':'"+ basemodule +"','parent':'#',text:'"+module+"'},");
				for(Permission perm:ps){
					if(!containPerm(selectedPermission,perm))
					  sb.append("{'id':'"+ perm.getPermissionId() +"','parent':'"+basemodule+"','text':'"+perm.getDescription()+"'},");
					else
					  sb.append("{'id':'"+ perm.getPermissionId() +"','parent':'"+basemodule+"','text':'"+perm.getDescription()+"','state':{'selected':'true'}},");	
				}
			}
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("]");
		return sb.toString();
	}
	
	private boolean containPerm(Set<Permission> selectedPermission,Permission perm){
		if(CollectionUtils.isEmpty(selectedPermission))
		    return false;
		for(Permission seelectedperm:selectedPermission){
			if(seelectedperm.getPermissionName().equals(perm.getPermissionName()))
				return true;
		}
		return false;
	}
	
	@RequestMapping(value="/cpu.do",method=RequestMethod.GET)
	public String cpu ( Model model, HttpServletRequest request){
		StringBuilder sb = new StringBuilder();
		sb.append("{title:{text:'CPU'},");
		sb.append("tooltip:{trigger:'axis'},");
		sb.append("legend:{data:['cpu'],show:false},");
		sb.append("toolbox:{show:true,feature:{mark:{show:true},dataView : {show: true, readOnly: false}, magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']}, restore : {show: true},saveAsImage:{show: true}}},");
		sb.append("calculable:true,");
		sb.append("xAxis:[{ type : 'category', boundaryGap : false,data:[1,2,3,4,5]}],");
		sb.append("yAxis:[{type : 'value'}],");
		sb.append("series:[{name:'cpu',type:'line', smooth:true, itemStyle: {normal: {areaStyle: {type: 'default'}}},data:[10,20,30,40,50]}]};");
		model.addAttribute("cpu", sb.toString());
 	    return "/system/cpu";
	}
}
