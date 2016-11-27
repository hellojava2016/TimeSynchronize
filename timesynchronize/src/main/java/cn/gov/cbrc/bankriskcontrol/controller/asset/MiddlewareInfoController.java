package cn.gov.cbrc.bankriskcontrol.controller.asset;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.gov.cbrc.bankriskcontrol.dto.AssetQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.AppSystem;
import cn.gov.cbrc.bankriskcontrol.entity.MiddlewareInfo;
import cn.gov.cbrc.bankriskcontrol.entity.OperateSystemInfo;
import cn.gov.cbrc.bankriskcontrol.entity.Organization;
import cn.gov.cbrc.bankriskcontrol.entity.User;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.asset.AppSystemService;
import cn.gov.cbrc.bankriskcontrol.service.asset.MiddlewareInfoService;
import cn.gov.cbrc.bankriskcontrol.service.asset.OperateSystemInfoService;
import cn.gov.cbrc.bankriskcontrol.service.system.OrganizationService;
import cn.gov.cbrc.bankriskcontrol.util.ExportUtil;
import cn.gov.cbrc.bankriskcontrol.util.Page;
import cn.gov.cbrc.bankriskcontrol.util.RiskUtils;

@Controller
@RequestMapping(value = "/asset/middlewareinfo")
public class MiddlewareInfoController {
	@Autowired
	private MiddlewareInfoService middlewareInfoService;
	
	@Autowired
    private OperateSystemInfoService operateSystemInfoService;

	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
    private UserService userService;
	
	@Autowired
    private AppSystemService appSystemService;

	@RequestMapping(value = "/add_pre.do")
	public ModelAndView add_pre(HttpServletRequest request,Model model) {
		User user=(User)request.getSession().getAttribute("user");
		List<OperateSystemInfo> list=operateSystemInfoService.getOperateSystemInfosByOrganization(user.getOrganization().getOrgId());
		model.addAttribute("oss", list);
		model.addAttribute("apps", RiskUtils.getJsonApps(appSystemService,null));
		model.addAttribute("id",0);
		return new ModelAndView("/asset/middlewareinfo-add", null);
	}
	
	@RequestMapping(value = "/add.do")
	public ModelAndView add(HttpServletRequest request,
			HttpServletResponse response) {
		try{
			MiddlewareInfo info=new MiddlewareInfo();
	        getValue(request, info);
	        middlewareInfoService.addMiddlewareInfo(info);
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
	public ModelAndView update_pre(HttpServletRequest request,
			HttpServletResponse response,ModelMap mode) {
		long id=Long.parseLong(request.getParameter("id"));
		MiddlewareInfo mw=middlewareInfoService.getMiddlewareInfoById(id);
		
		User user=(User)request.getSession().getAttribute("user");
		List<OperateSystemInfo> list=operateSystemInfoService.getOperateSystemInfosByOrganization(user.getOrganization().getOrgId());
		mode.addAttribute("oss", list);
		mode.addAttribute("apps", RiskUtils.getJsonApps(appSystemService,mw.getAppSystems()));
		mode.addAttribute("mw",mw);
		mode.addAttribute("id",id);
		return new ModelAndView("/asset/middlewareinfo-add", null);
	}

	@RequestMapping(value = "/update.do")
	public ModelAndView update(HttpServletRequest request,
			HttpServletResponse response) {
		try{
			long id=Long.parseLong(request.getParameter("id"));
			MiddlewareInfo info=middlewareInfoService.getMiddlewareInfoById(id);
	        getValue(request, info);
	        middlewareInfoService.updateMiddlewareInfo(info);
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
		Page<MiddlewareInfo> page = new Page<MiddlewareInfo>();
		page.setPageNo(pageNo);
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotEmpty(pageSize))
			  page.setPageSize(Integer.parseInt(pageSize));
		Page<MiddlewareInfo> list=middlewareInfoService.getMiddlewareinfos(param, page);
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
				   model.addAttribute("message","添加中间件成功");
			else if("updatesucess".equals(message))
				   model.addAttribute("message","修改中间件成功");
			else if("deletesucess".equals(message))
				   model.addAttribute("message","删除中间件成功");
			else if("adderror".equals(message))
				   model.addAttribute("message","修改中间件失败,请重新添加");
			else if("deleteerror".equals(message))
				   model.addAttribute("message","删除中间件失败,请重新删除");
			else if("updateerror".equals(message))
				   model.addAttribute("message","修改中间件失败,请检查数据是否合法");
		}	
		return "/asset/middlewareinfo-list";
	}

	@RequestMapping(value = "/query.do")
	public String query(
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, HttpServletRequest request) {
		String name=request.getParameter("name");
		String uniqueVal=request.getParameter("uniqueVal");
		long orgId=Long.parseLong(request.getParameter("Organization"));
		AssetQueryParam param=new AssetQueryParam();
		param.setName(name);
		param.setUniqueVal(uniqueVal);
		param.setOrganizationId(orgId);
		Page<MiddlewareInfo> page = new Page<MiddlewareInfo>();
		page.setPageNo(pageNo);
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotEmpty(pageSize))
			  page.setPageSize(Integer.parseInt(pageSize));
		Page<MiddlewareInfo> list=middlewareInfoService.getMiddlewareinfos(param, page);
		model.addAttribute("rates", list);

		Subject user = SecurityUtils.getSubject();
		String username=(String)user.getPrincipal();  
		User oldu = userService.getUserByUserName(username);
				oldu.setOnline(false);
		List<Organization> orgs=organizationService.getBelongBanksByUser(oldu);
		model.addAttribute("orgs", orgs);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("uniqueVal", uniqueVal);
		model.addAttribute("selectedorgid", orgId);
		model.addAttribute("name", name);
		String message = request.getParameter("message");
		if(StringUtils.isNotEmpty(message)){
			if("addsucess".equals(message))
				   model.addAttribute("message","添加中间件成功");
			else if("updatesucess".equals(message))
				   model.addAttribute("message","修改中间件成功");
			else if("deletesucess".equals(message))
				   model.addAttribute("message","删除中间件成功");
			else if("adderror".equals(message))
				   model.addAttribute("message","修改中间件失败,请重新添加");
			else if("deleteerror".equals(message))
				   model.addAttribute("message","删除中间件失败,请重新删除");
			else if("updateerror".equals(message))
				   model.addAttribute("message","修改中间件失败,请检查数据是否合法");
		}	
		return "/asset/middlewareinfo-list";
	}
	
	@RequestMapping(value = "/export.do")
	public String export(
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, HttpServletRequest request,HttpServletResponse response) {
		String name=request.getParameter("name");
		String uniqueVal=request.getParameter("uniqueVal");
		long orgId=Long.parseLong(request.getParameter("Organization"));
		AssetQueryParam param=new AssetQueryParam();
		param.setName(name);
		param.setUniqueVal(uniqueVal);
		param.setOrganizationId(orgId);
		Page<MiddlewareInfo> page = new Page<MiddlewareInfo>();
		page.setPageSize(Integer.MAX_VALUE);
		page.setPageNo(pageNo);
		Page<MiddlewareInfo> list=middlewareInfoService.getMiddlewareinfos(param, page);	
		List<String[]> valueList=new ArrayList<String[]>();
		valueList.add(new String[]{"所属机构","编号","中间件名称","类型","操作系统","版本","补丁","应用系统"});
		for(MiddlewareInfo mc:list.getResult()){
			String orgName=mc.getOrganization().getName();
			valueList.add(new String[]{orgName ,mc.getUniqueVal(),mc.getMiddlewareName(), mc.getType(),mc.getOsinfo().getName(),mc.getVersion(),mc.getPatch(),mc.getAppSystem()});
		}
		ExportUtil.exportExcel(valueList, "中间件列表", response);
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
				middlewareInfoService.deleteMiddlewareInfoById(id);
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
		String uniqueVal= request.getParameter("uniqueVal");
		if(null==uniqueVal)
			return false;
		MiddlewareInfo middle=middlewareInfoService.getMiddlewareInfoByUniqueVal(uniqueVal);
	      if(null== middle)
	    	  return true;
		return false;
	}
	
	private void getValue(HttpServletRequest request,MiddlewareInfo info){
		String uniqueVal=request.getParameter("uniqueVal");
        String operateSystem=request.getParameter("operateSystem");
        String type=request.getParameter("type");
        String version=request.getParameter("version");
        String patch=request.getParameter("patch");
        String middlewareName=request.getParameter("middlewareName");
        User user=(User)request.getSession().getAttribute("user");
        OperateSystemInfo os=operateSystemInfoService.getOperateSystemInfoById(Long.parseLong(operateSystem));
        
        info.setUniqueVal(uniqueVal);
        info.setType(type);
        info.setVersion(version);
        info.setPatch(patch);
        info.setMiddlewareName(middlewareName);
        info.setRecordTime(new Date());
        info.setOperateSystem(os.getUniqueVal());
        info.setOsinfo(os);
        info.setOrganization(user.getOrganization());
        String apps = request.getParameter("apps");
        info.setAppSystem(apps);
        Set<AppSystem> set = new HashSet<AppSystem>();
        if(StringUtils.isNotEmpty(apps)){
        	 for(String app :apps.split(",")){
        		 set.add(appSystemService.getAppSystemByName(app));
        	 }
        }
        info.setAppSystems(set);
	}
}
