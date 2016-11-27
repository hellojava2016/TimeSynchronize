package cn.gov.cbrc.bankriskcontrol.controller.asset;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.gov.cbrc.bankriskcontrol.dto.AssetQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.AppSystem;
import cn.gov.cbrc.bankriskcontrol.entity.Organization;
import cn.gov.cbrc.bankriskcontrol.entity.User;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.asset.AppSystemService;
import cn.gov.cbrc.bankriskcontrol.service.system.OrganizationService;
import cn.gov.cbrc.bankriskcontrol.util.ExportUtil;
import cn.gov.cbrc.bankriskcontrol.util.Page;

@Controller
@RequestMapping(value = "/asset/appsystem")
public class AppSystemController {
	@Autowired
    private AppSystemService appSystemService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
	private OrganizationService organizationService;
    
    @RequestMapping(value = "/add_pre.do")
	public ModelAndView add_pre(Model model) { 
    	model.addAttribute("id", 0);
		return new ModelAndView("/asset/appsystem-add", null);
	}

	@RequestMapping(value = "/add.do")
	public ModelAndView add(HttpServletRequest request,
			HttpServletResponse response) {
		try{
			AppSystem info = new AppSystem();
	        getValue(request, info);
	        appSystemService.addAppSystem(info);
		}catch(Exception ex){
			 String listurl=(String)request.getSession().getAttribute("listurl");
			 String queryparam=(String)request.getSession().getAttribute("queryparam");
			 return new ModelAndView("redirect:"+listurl+"?message=adderror&"+queryparam);
		}
		
        String listurl=(String)request.getSession().getAttribute("listurl");
		String queryparam=(String)request.getSession().getAttribute("queryparam");
		return new ModelAndView("redirect:"+listurl+"?message=addsucess&"+queryparam);
	}
	
	@RequestMapping(value = "/update_pre.do")
	public ModelAndView update_pre(HttpServletRequest request,Model model) { 
		long id=Long.parseLong(request.getParameter("id"));
		AppSystem as=appSystemService.getAppSystemById(id);		
    	model.addAttribute("id", id);
    	model.addAttribute("as", as);
		return new ModelAndView("/asset/appsystem-add", null);
	}

	@RequestMapping(value = "/update.do")
	public ModelAndView update(HttpServletRequest request,
			HttpServletResponse response) {
		try{
			long id=Long.parseLong(request.getParameter("id"));
			AppSystem as=appSystemService.getAppSystemById(id);	
	        getValue(request, as);
	        appSystemService.updateAppSystem(as);
		}catch(Exception ex){
			 String listurl=(String)request.getSession().getAttribute("listurl");
			 String queryparam=(String)request.getSession().getAttribute("queryparam");
			 return new ModelAndView("redirect:"+listurl+"?message=updateerror&"+queryparam);
		}
		
        String listurl=(String)request.getSession().getAttribute("listurl");
		String queryparam=(String)request.getSession().getAttribute("queryparam");
		return new ModelAndView("redirect:"+listurl+"?message=updatesucess&"+queryparam);
    } 

	@RequestMapping(value = "/list.do")
	public String getlist(
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, HttpServletRequest request) {
		AssetQueryParam param=new AssetQueryParam();
		Page<AppSystem> page = new Page<AppSystem>();
		page.setPageNo(pageNo);
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotEmpty(pageSize))
			  page.setPageSize(Integer.parseInt(pageSize));
		Page<AppSystem> list=appSystemService.getAppSystems(param, page);
		model.addAttribute("rates", list);
		
		Subject user = SecurityUtils.getSubject();
		String username=(String)user.getPrincipal();  
		User oldu = userService.getUserByUserName(username);
				oldu.setOnline(false);
		List<Organization> orgs=organizationService.getBelongBanksByUser(oldu);
		model.addAttribute("orgs", orgs);
		model.addAttribute("pageNo", pageNo);	
		String message = request.getParameter("message");
		if(StringUtils.isNotEmpty(message)){
			if("addsucess".equals(message))
				   model.addAttribute("message","添加应用系统成功");
			else if("updatesucess".equals(message))
				   model.addAttribute("message","修改应用系统成功");
			else if("deletesucess".equals(message))
				   model.addAttribute("message","删除应用系统成功");
			else if("adderror".equals(message))
				   model.addAttribute("message","修改应用系统失败,请重新添加");
			else if("deleteerror".equals(message))
				   model.addAttribute("message","删除应用系统失败,请重新删除");
			else if("updateerror".equals(message))
				   model.addAttribute("message","修改应用系统失败,请检查数据是否合法");
		}	
		return "/asset/appsystem-list";
	}

	@RequestMapping(value = "/query.do")
	public String query(
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, HttpServletRequest request) {
		String name=request.getParameter("name");
		AssetQueryParam param=new AssetQueryParam();
		param.setName(name);
		Page<AppSystem> page = new Page<AppSystem>();
		page.setPageNo(pageNo);
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotEmpty(pageSize))
			  page.setPageSize(Integer.parseInt(pageSize));
		Page<AppSystem> list=appSystemService.getAppSystems(param, page);
		Subject user = SecurityUtils.getSubject();
		String username=(String)user.getPrincipal();  
		User oldu = userService.getUserByUserName(username);
				oldu.setOnline(false);
		List<Organization> orgs=organizationService.getBelongBanksByUser(oldu);
		model.addAttribute("orgs", orgs);
		model.addAttribute("rates", list);
		model.addAttribute("pageNo", pageNo);	
		model.addAttribute("name", name);
		String message = request.getParameter("message");
		if(StringUtils.isNotEmpty(message)){
			if("addsucess".equals(message))
				   model.addAttribute("message","添加应用系统成功");
			else if("updatesucess".equals(message))
				   model.addAttribute("message","修改应用系统成功");
			else if("deletesucess".equals(message))
				   model.addAttribute("message","删除应用系统成功");
			else if("adderror".equals(message))
				   model.addAttribute("message","修改应用系统失败,请重新添加");
			else if("deleteerror".equals(message))
				   model.addAttribute("message","删除应用系统失败,请重新删除");
			else if("updateerror".equals(message))
				   model.addAttribute("message","修改应用系统失败,请检查数据是否合法");
		}	
		return "/asset/appsystem-list";
	}
	
	@RequestMapping(value = "/export.do")
	public String export(
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, HttpServletRequest request,HttpServletResponse response) {
		String name=request.getParameter("name");
		AssetQueryParam param=new AssetQueryParam();
		param.setName(name);
		Page<AppSystem> page = new Page<AppSystem>();
		page.setPageSize(Integer.MAX_VALUE);
		page.setPageNo(pageNo);
		Page<AppSystem> list=appSystemService.getAppSystems(param, page);
		List<String[]> valueList=new ArrayList<String[]>();
		valueList.add(new String[]{"名称","描述"});
		for(AppSystem as:list.getResult()){
			valueList.add(new String[]{as.getAppName() ,as.getDescription()});
		}
		ExportUtil.exportExcel(valueList, "应用系统列表", response);
        return null;
	}

	@RequestMapping(value = "/delete.do")
	public ModelAndView deleteSystemAvailableRate(HttpServletRequest request,
			HttpServletResponse response) {
		try{
			String deleteids = request.getParameter("deleteids");
			String[] ids = deleteids.split(",");
			for (String temp : ids) {
				long id = Long.parseLong(temp);
				appSystemService.deleteAppSystemById(id);
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
	
	@RequestMapping(value="/checkname.do")
	@ResponseBody
	public boolean checkUniqueVal (Model model,HttpServletRequest request){
		String id=request.getParameter("id");
		if(!id.equals("0"))
			return true;
		String appName= request.getParameter("appName");
		if(null==appName)
			return false;
		AppSystem as=appSystemService.getAppSystemByName(appName);
	      if(null== as)
	    	  return true;
		return false;
	}
    
	private void getValue(HttpServletRequest request,AppSystem info){
		String appName=request.getParameter("appName");
        String description=request.getParameter("description");   
        info.setAppName(appName);
        info.setDescription(description);
	}
}
