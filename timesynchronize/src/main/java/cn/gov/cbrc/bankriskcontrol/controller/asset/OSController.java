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
import cn.gov.cbrc.bankriskcontrol.entity.OperateSystemInfo;
import cn.gov.cbrc.bankriskcontrol.entity.Organization;
import cn.gov.cbrc.bankriskcontrol.entity.PcServer;
import cn.gov.cbrc.bankriskcontrol.entity.User;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.asset.AppSystemService;
import cn.gov.cbrc.bankriskcontrol.service.asset.OperateSystemInfoService;
import cn.gov.cbrc.bankriskcontrol.service.asset.PcServerService;
import cn.gov.cbrc.bankriskcontrol.service.system.OrganizationService;
import cn.gov.cbrc.bankriskcontrol.util.ExportUtil;
import cn.gov.cbrc.bankriskcontrol.util.Page;
import cn.gov.cbrc.bankriskcontrol.util.RiskUtils;

@Controller
@RequestMapping(value = "/asset/os")
public class OSController {
	@Autowired
    private OperateSystemInfoService operateSystemInfoService;
	
	@Autowired
    private PcServerService pcServerService;
   
    @Autowired
	private OrganizationService organizationService;
    
    @Autowired
    private UserService userService;
    

	@Autowired
    private AppSystemService appSystemService;
    
    @RequestMapping(value = "/add_pre.do")
	public ModelAndView add_pre(HttpServletRequest request,Model model) {
    	User user=(User)request.getSession().getAttribute("user");
    	List<PcServer> list=pcServerService.getPcServersByOrganization(user.getOrganization().getOrgId());
		model.addAttribute("hosts", list);
		model.addAttribute("apps", RiskUtils.getJsonApps(appSystemService,null));
		model.addAttribute("id", 0);
		return new ModelAndView("/asset/os-add", null);
	}

	@RequestMapping(value = "/add.do")
	public ModelAndView add(HttpServletRequest request,
			HttpServletResponse response) {
		try{
			OperateSystemInfo info = new OperateSystemInfo();
			getValue(request, info);
	        operateSystemInfoService.addOperateSystemInfo(info); 
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
		OperateSystemInfo info=operateSystemInfoService.getOperateSystemInfoById(id);
		User user=(User)request.getSession().getAttribute("user");
    	List<PcServer> list=pcServerService.getPcServersByOrganization(user.getOrganization().getOrgId());
    	mode.addAttribute("hosts", list);
    	mode.addAttribute("apps", RiskUtils.getJsonApps(appSystemService,info.getAppSystems()));
    	mode.addAttribute("id", id);
    	mode.addAttribute("os", info);
		return new ModelAndView("/asset/os-add", null);
	}

	@RequestMapping(value = "/update.do")
	public ModelAndView update(HttpServletRequest request,
			HttpServletResponse response) {
		try{
			long id=Long.parseLong(request.getParameter("id"));
			OperateSystemInfo info=operateSystemInfoService.getOperateSystemInfoById(id);
			getValue(request, info);
			operateSystemInfoService.updateOperateSystemInfo(info);
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
		Page<OperateSystemInfo> page = new Page<OperateSystemInfo>();
		page.setPageNo(pageNo);
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotEmpty(pageSize))
			  page.setPageSize(Integer.parseInt(pageSize));
		Page<OperateSystemInfo> list=operateSystemInfoService.getOperateSystemInfos(param, page);
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
				   model.addAttribute("message","添加操作系统成功");
			else if("updatesucess".equals(message))
				   model.addAttribute("message","修改操作系统成功");
			else if("deletesucess".equals(message))
				   model.addAttribute("message","删除操作系统成功");
			else if("adderror".equals(message))
				   model.addAttribute("message","修改操作系统失败,请重新添加");
			else if("deleteerror".equals(message))
				   model.addAttribute("message","删除操作系统失败,请重新删除");
			else if("updateerror".equals(message))
				   model.addAttribute("message","修改操作系统失败,请检查数据是否合法");
		}	
		return "/asset/os-list";
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
		Page<OperateSystemInfo> page = new Page<OperateSystemInfo>();
		page.setPageNo(pageNo);
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotEmpty(pageSize))
			  page.setPageSize(Integer.parseInt(pageSize));
		Page<OperateSystemInfo> list=operateSystemInfoService.getOperateSystemInfos(param, page);
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
				   model.addAttribute("message","添加操作系统成功");
			else if("updatesucess".equals(message))
				   model.addAttribute("message","修改操作系统成功");
			else if("deletesucess".equals(message))
				   model.addAttribute("message","删除操作系统成功");
			else if("adderror".equals(message))
				   model.addAttribute("message","修改操作系统失败,请重新添加");
			else if("deleteerror".equals(message))
				   model.addAttribute("message","删除操作系统失败,请重新删除");
			else if("updateerror".equals(message))
				   model.addAttribute("message","修改操作系统失败,请检查数据是否合法");
		}	
		return "/asset/os-list";
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
		Page<OperateSystemInfo> page = new Page<OperateSystemInfo>();
		page.setPageSize(Integer.MAX_VALUE);
		page.setPageNo(pageNo);
		Page<OperateSystemInfo> list=operateSystemInfoService.getOperateSystemInfos(param, page);
		List<String[]> valueList=new ArrayList<String[]>();
		valueList.add(new String[]{"所属机构","编号","所属主机","操作系统名称","操作系统类型","版本","补丁","应用系统"});
		for(OperateSystemInfo os:list.getResult()){
			String orgName=os.getOrganization().getName();
			valueList.add(new String[]{orgName ,os.getUniqueVal(), os.getHost(),os.getName(),os.getType(),os.getVersion(),os.getPatch(),os.getAppSystem()});
		}
		ExportUtil.exportExcel(valueList, "操作系统列表", response);
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
				operateSystemInfoService.deleteOperateSystemInfoById(id);
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
		OperateSystemInfo info=operateSystemInfoService.getOperateSystemInfoByUniqueVal(uniqueVal);
	      if(null== info)
	    	  return true;
		return false;
	}
	
	private void getValue(HttpServletRequest request,OperateSystemInfo info){
		String uniqueVal=request.getParameter("uniqueVal");
        String name=request.getParameter("name");
        String type=request.getParameter("type");
        String version=request.getParameter("version");
        String patch=request.getParameter("patch");
        PcServer pcServer=pcServerService.getPcServerById(Long.parseLong(request.getParameter("host")));
        
        info.setUniqueVal(uniqueVal);
        info.setPatch(patch);
        info.setName(name);
        info.setType(type);
        info.setVersion(version);
        info.setPcserver(pcServer);
        info.setRecordTime(new Date());
        info.setHost(pcServer.getUniqueVal());
        String apps = request.getParameter("apps");
        info.setAppSystem(apps);
        Set<AppSystem> set = new HashSet<AppSystem>();
        if(StringUtils.isNotEmpty(apps)){
        	 for(String app :apps.split(",")){
        		 set.add(appSystemService.getAppSystemByName(app));
        	 }
        }
        info.setAppSystems(set);
        User user=(User)request.getSession().getAttribute("user");
        info.setOrganization(user.getOrganization());
	}
}
