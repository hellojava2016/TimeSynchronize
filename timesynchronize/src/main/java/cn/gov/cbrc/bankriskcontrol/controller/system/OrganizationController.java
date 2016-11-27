package cn.gov.cbrc.bankriskcontrol.controller.system;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cn.gov.cbrc.bankriskcontrol.entity.AreaCity;
import cn.gov.cbrc.bankriskcontrol.entity.Organization;
import cn.gov.cbrc.bankriskcontrol.entity.User;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.system.AreaCityService;
import cn.gov.cbrc.bankriskcontrol.service.system.OrganizationService;
import cn.gov.cbrc.bankriskcontrol.util.Page;


@Controller
@RequestMapping(value = "/organization")
public class OrganizationController {	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private AreaCityService areaCityService;
	
	@Autowired
	private UserService userService;
	
	/**
     * 跳转到添加银行机构信息
     * @param request
     * @param model
     * */
	@RequestMapping("/add_pre.do")
	public ModelAndView add_pre(HttpServletRequest request, Model model) {
		// 在页面加载所有的省份，根据选择再进行城市的选择。
		Subject user = SecurityUtils.getSubject();
		String username=(String)user.getPrincipal();  
		User userDb = userService.getUserByUserName(username);
		List<AreaCity> areas=areaCityService.getSubAreaCityByUser(userDb);
		model.addAttribute("areas", areas);
		return new ModelAndView("/system/organization-add");
	} 
	
	
    /**
     * 添加银行机构信息
     * @param request
     * @param model
     * */
	@RequestMapping("/add.do")
	public ModelAndView add(HttpServletRequest request,Model model) {	
		try{
			Organization organization=new Organization();
			String areaCode = request.getParameter("area");
			AreaCity areaCity = areaCityService.getAreaCityByCode(areaCode);	
			organization.setAreaCity(areaCity);	
			organization.setCategory(Integer.parseInt(request.getParameter("category")));
			organization.setAddress(request.getParameter("address"));
			organization.setOrgNo(request.getParameter("orgNo"));
			organization.setContactsMail(request.getParameter("contactsMail"));
			organization.setContactsName(request.getParameter("contactsName"));
			organization.setContactsCellphone(request.getParameter("contactsCellphone"));
			organization.setContactsPhone(request.getParameter("contactsPhone"));		
			organization.setName(request.getParameter("name"));
			organization.setMoneyCount(Long.parseLong(request.getParameter("moneyCount")));
			if (organization.getCategory()!= 1) {
				organization.setCanControl(Boolean.parseBoolean(request.getParameter("canControl")));
			} else {
				organization.setCanControl(false);
			}
			organization.setOrganization(null);  
			organizationService.addOrganization(organization);
		
			model.addAttribute("organization", organization);
		}catch(Exception ex){
			String listurl=(String)request.getSession().getAttribute("listurl");
			String queryparam=(String)request.getSession().getAttribute("queryparam");
			return new ModelAndView("redirect:"+listurl+"?message=adderror&"+queryparam);
		}
		
		String listurl=(String)request.getSession().getAttribute("listurl");
		String queryparam=(String)request.getSession().getAttribute("queryparam");
		return new ModelAndView("redirect:"+listurl+"?message=addsucess&"+queryparam);
    } 
	/**
	 * 删除银行机构信息
	 * @param request
	 * @param model
	 * **/
	@RequestMapping("/delete.do")
	public ModelAndView delete(HttpServletRequest request,ModelMap model){	
		try{
			String deleteids = request.getParameter("orgId");
			String[] ids = deleteids.split(",");
			for (String temp : ids) {
				long id = Long.parseLong(temp);
				organizationService.deleteOrganization(id);
			}
		}catch(Exception ex){
			String listurl=(String)request.getSession().getAttribute("listurl");
			String queryparam=(String)request.getSession().getAttribute("queryparam");
			return new ModelAndView("redirect:"+listurl+"?message=deleteerror&"+queryparam);
		}
		String listurl=(String)request.getSession().getAttribute("listurl");
		String queryparam=(String)request.getSession().getAttribute("queryparam");
		return new ModelAndView("redirect:"+listurl+"?message=deletesucess&"+queryparam);
	}
	
	/**
	 * 修改机构Key
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/changekey.do")
	public ModelAndView changekey(HttpServletRequest request,ModelMap model){
		long orgId=Long.parseLong(request.getParameter("orgId"));
		organizationService.changeKey(orgId);
		return new ModelAndView("redirect:/organization/list.do");	
	}
	
	/**
	 * 携带机构信息跳转到修改银行机构进行页面
	 * @param request
	 * @param model
	 * */
	@RequestMapping(value="/update_pre.do",method=RequestMethod.GET)
	public ModelAndView update_pre(HttpServletRequest request,Model model){
		Organization organizations=
				organizationService.getOrganization(Long.parseLong(request.getParameter("orgId")));
	    model.addAttribute("organization", organizations);	    
		return new ModelAndView("system/organization-update");
		
	}
	/**
	 * 修改银行机构信息
	 * @param request
	 * @param model
	 * 
	 * */
	@RequestMapping("/update.do")
	public ModelAndView update(HttpServletRequest request,Model model){									
		Organization organization=organizationService.
				getOrganization(Long.parseLong(request.getParameter("orgId")));	
		organization.setAddress(request.getParameter("address"));
		organization.setOrgNo(request.getParameter("orgNo"));
		organization.setContactsMail(request.getParameter("contactsMail"));
		organization.setContactsName(request.getParameter("contactsName"));
		organization.setContactsCellphone(request.getParameter("contactsCellphone"));
		organization.setContactsPhone(request.getParameter("contactsPhone"));		
		organization.setName(request.getParameter("name"));
		organization.setCategory(Integer.parseInt(request.getParameter("category")));
		organization.setMoneyCount(Long.parseLong(request.getParameter("moneyCount")));
		if (organization.getCategory() != 1) {
			organization.setCanControl(Boolean.parseBoolean(request.getParameter("canControl")));
		} else {
			organization.setCanControl(false);
		}
		organizationService.updateOrganization(organization);
		String listurl=(String)request.getSession().getAttribute("listurl");
		String queryparam=(String)request.getSession().getAttribute("queryparam");
		return new ModelAndView("redirect:"+listurl+"?message=updatesucess&"+queryparam);
	}

	/**
	 * 银行机构信息列表 
	 * @param pageNo 页数
	 * 
	 * */
	@RequestMapping("/list.do")
	public String  getAll(@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, HttpServletRequest request){
		Page<Organization> page=new Page<Organization>();
		page.setPageNo(pageNo);	
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isEmpty(pageSize))
			pageSize="10";
		page.setPageSize(Integer.parseInt(pageSize));
		
		Subject user = SecurityUtils.getSubject();
		String username=(String)user.getPrincipal();  
		User userDb = userService.getUserByUserName(username);
		Page<Organization> list=organizationService.getPage(0,userDb.getOrganization().getAreaCity().getAreeCode(),null,page,false);	
		model.addAttribute("organization",list);
		String message = request.getParameter("message");
		if(StringUtils.isNotEmpty(message)){
			if("addsucess".equals(message))
				   model.addAttribute("message","添加机构成功");
			else if("updatesucess".equals(message))
				   model.addAttribute("message","修改机构成功");
			else if("deletesucess".equals(message))
				   model.addAttribute("message","删除机构成功");
			else if("adderror".equals(message))
				   model.addAttribute("message","添加机构失败");
			else if("deleteerror".equals(message))
				   model.addAttribute("message","删除机构失败,机构已经存在关联的用户或数据");
			else if("updateerror".equals(message))
				   model.addAttribute("message","修改机构失败");
		}	
		return "system/organization-list";	
	}	
	
	@RequestMapping(value = "/query.do")
	public String query(
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, HttpServletRequest request) {
		Subject user = SecurityUtils.getSubject();
		String username=(String)user.getPrincipal();  
		User userDb = userService.getUserByUserName(username);
		
		int category=Integer.parseInt(request.getParameter("category"));
		model.addAttribute("category", category);
		//String areaCode = RiskUtils.getSelectedRiskCodeFromView(request);
		String name=request.getParameter("name");
		model.addAttribute("name", name);
		
		Page<Organization> page = new Page<Organization>();
		page.setPageNo(pageNo);
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotEmpty(pageSize))
			  page.setPageSize(Integer.parseInt(pageSize));
		Page<Organization> list=organizationService.getPage(category,userDb.getOrganization().getAreaCity().getAreeCode(),name,page,false);	
		model.addAttribute("organization",list);
		
		
		model.addAttribute("user",userDb);
		String message = request.getParameter("message");
		if(StringUtils.isNotEmpty(message)){
			if("addsucess".equals(message))
				   model.addAttribute("message","添加机构成功");
			else if("updatesucess".equals(message))
				   model.addAttribute("message","修改机构成功");
			else if("deletesucess".equals(message))
				   model.addAttribute("message","删除机构成功");
			else if("adderror".equals(message))
				   model.addAttribute("message","添加机构失败");
			else if("deleteerror".equals(message))
				 model.addAttribute("message","删除机构失败,请检查该机构是否和其他配置存在关联关系");
			else if("updateerror".equals(message))
				   model.addAttribute("message","修改机构失败");
		}	
		return "/system/organization-list";
	}
}
