package cn.gov.cbrc.bankriskcontrol.controller.system;

import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cn.gov.cbrc.bankriskcontrol.dto.ReportQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.AppSystem;
import cn.gov.cbrc.bankriskcontrol.entity.ExtraReportInfo;
import cn.gov.cbrc.bankriskcontrol.entity.Organization;
import cn.gov.cbrc.bankriskcontrol.entity.RiskCategory;
import cn.gov.cbrc.bankriskcontrol.entity.SystemAvailableRate;
import cn.gov.cbrc.bankriskcontrol.entity.User;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.system.ExtraReportInfoService;
import cn.gov.cbrc.bankriskcontrol.service.system.OrganizationService;
import cn.gov.cbrc.bankriskcontrol.service.system.RiskCategoryService;
import cn.gov.cbrc.bankriskcontrol.util.ConvertUtils;
import cn.gov.cbrc.bankriskcontrol.util.ExportUtil;
import cn.gov.cbrc.bankriskcontrol.util.Page;
import cn.gov.cbrc.bankriskcontrol.util.RiskUtils;
import cn.gov.cbrc.bankriskcontrol.util.ServletUtils;

@Controller
@RequestMapping("/reportadditional")
public class ExtraReportInfoController {
	@Autowired
	private ExtraReportInfoService extraReportInfoService;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private RiskCategoryService riskCategoryService;
	
	@Autowired
	private UserService userService; 
	
	@RequestMapping(value = "/gettypes.do")
	public void getTypes(
			HttpServletRequest request, HttpServletResponse response) {
		String code="";
		String defaultChoose=request.getParameter("defaultChoose");
		Map<String, List<RiskCategory>> map = riskCategoryService.getChildren(code);
		RiskUtils.initRiskCodeToView(map, code, response,defaultChoose);
	}
	
	/**
	 * 跳转到添加补报设置页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/add_pre.do")
	public ModelAndView addReportAdditional_pre(HttpServletRequest request,ModelMap model){
		String type=request.getParameter("type");
		int pageNo = Integer.parseInt(request.getParameter("pageNo"));
		model.addAttribute("pageNo", pageNo);
		if(type.equals("bank")){
			return new ModelAndView("system/reportadditional-add-bank", null);
		}else{
			model.addAttribute("orgs", getJsonOrgs());
			model.addAttribute("rcs", RiskUtils.getRiskCategorys(riskCategoryService.getAllRiskCategorys()));
			return new ModelAndView("system/reportadditional-add-cbrc", null);
		}
	}
	
	private String getJsonOrgs(){
		Subject user = SecurityUtils.getSubject();
		String username=(String)user.getPrincipal();  
		User userDb = userService.getUserByUserName(username);
		Page<Organization> page=new Page<Organization>();
		page.setPageSize(Integer.MAX_VALUE);
    	List<Organization> apps =organizationService.getPage(0,userDb.getOrganization().getAreaCity().getAreeCode(),null,page,true).getResult();
    	StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append("{'id':'10000','parent':'#',text:'所有'},");
		for(Organization app:apps){
					  sb.append("{'id':'"+ app.getOrgId() +"','parent':'"+10000+"','text':'"+app.getName()+"'},");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("]");
		return sb.toString();
    }
	
	@RequestMapping(value = "/add.do")
	public ModelAndView addReportAdditional(HttpServletRequest request,HttpServletResponse response){
		try{
			String type=request.getParameter("type");
			if(type.equals("bank")){
				String reportType = RiskUtils.getSelectedRiskCodeFromView(request);
				Date reportDate=(Date)ConvertUtils.convertStringToObject(request.getParameter("reportdate"), Date.class);
				Date startTime=(Date)ConvertUtils.convertStringToObject(request.getParameter("starttime"),Date.class);
				Date endTime=(Date)ConvertUtils.convertStringToObject(request.getParameter("endtime"),Date.class);
				String reportReason=request.getParameter("reportreason");
				ExtraReportInfo additional=new ExtraReportInfo();
				additional.setReportDate(reportDate);			
				RiskCategory ca=riskCategoryService.getRiskCategoryByCode(reportType);
				additional.setRiskCategory(ca);
				additional.setPeriod(RiskUtils.getPeroidByReportDate(ca, reportDate));
				User user=(User)request.getSession().getAttribute("user");
				additional.setOrganization(user.getOrganization());
				additional.setBeginTime(startTime);
				additional.setEndTime(endTime);
				additional.setReason(reportReason);
				additional.setAudit(false);
				additional.setUser(user);
				extraReportInfoService.addReportAdditional(additional);
			}else if(type.equals("cbrc")){
				Date reportDate=(Date)ConvertUtils.convertStringToObject(request.getParameter("reportdate"), Date.class);
				Date startTime=(Date)ConvertUtils.convertStringToObject(request.getParameter("starttime"),Date.class);
				Date endTime=(Date)ConvertUtils.convertStringToObject(request.getParameter("endtime"),Date.class);
				String reportReason=request.getParameter("reportreason");
				String orgIds = request.getParameter("orgs");
				String rcs = request.getParameter("rcs");
				User user=(User)request.getSession().getAttribute("user");
				if (StringUtils.isNotEmpty(orgIds) && StringUtils.isNotEmpty(rcs)) {
					for (String orgId : orgIds.split(",")) {
						for (String rc : rcs.split(",")) {
							RiskCategory ca = riskCategoryService.getRiskCategoryByCode(rc);
							if (ca==null||!ca.getAllowReport())
								continue;
							Organization org = new Organization();
							org.setOrgId(Long.parseLong(orgId));
							ExtraReportInfo additional = new ExtraReportInfo();
							additional.setOrganization(org);
							additional.setReportDate(reportDate);
							additional.setPeriod(RiskUtils.getPeroidByReportDate(ca, reportDate));
							additional.setRiskCategory(ca);
							additional.setBeginTime(startTime);
							additional.setEndTime(endTime);
							additional.setReason(reportReason);
							additional.setAudit(true);
							additional.setUser(user);
							extraReportInfoService.addReportAdditional(additional);
						}
					}
				}
			}
		}catch(Exception ex){
			String listurl=(String)request.getSession().getAttribute("listurl");
			String queryparam=(String)request.getSession().getAttribute("queryparam");
			return new ModelAndView("redirect:"+listurl+"?message=adderror&"+queryparam);
		
		}
		String listurl=(String)request.getSession().getAttribute("listurl");
		String queryparam=(String)request.getSession().getAttribute("queryparam");
		return new ModelAndView("redirect:"+listurl+"?message=addsucess&"+queryparam);
	}
	
	@RequestMapping(value = "/delete.do")  
	public ModelAndView deleteReportAdditional(HttpServletRequest request){
		try{
			String deleteids = request.getParameter("id");
			String[] ids = deleteids.split(",");
			for (String temp : ids) {
				long id = Long.parseLong(temp);
				ExtraReportInfo eri = extraReportInfoService.getReportAdditionalById(id);
				if(eri.isReported())
					throw new Exception("you can not delete");
				extraReportInfoService.deleteReportAdditionalById(id);
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
	
	@RequestMapping(value = "/auth.do")
	public ModelAndView authReportAdditional(HttpServletRequest request){
		try{
			String id=request.getParameter("id");
			ExtraReportInfo temp=extraReportInfoService.getReportAdditionalById(Long.parseLong(id));
			temp.setAudit(true);
			extraReportInfoService.updateReportAdditional(temp);
		}catch(Exception ex){
			String listurl=(String)request.getSession().getAttribute("listurl");
			String queryparam=(String)request.getSession().getAttribute("queryparam");
			return new ModelAndView("redirect:"+listurl+"?message=autherror&"+queryparam);
		}
		String listurl=(String)request.getSession().getAttribute("listurl");
		String queryparam=(String)request.getSession().getAttribute("queryparam");
		return new ModelAndView("redirect:"+listurl+"?message=authsucess&"+queryparam);
	}
	
	@RequestMapping(value = "/list.do")
	public String getReportAdditionalList(@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
			HttpServletRequest request){
		Page<ExtraReportInfo> page=new Page<ExtraReportInfo>();
		page.setPageNo(pageNo);
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isEmpty(pageSize))
			pageSize="10";
		page.setPageSize(Integer.parseInt(pageSize));
		ReportQueryParam param=new ReportQueryParam();
		Page<ExtraReportInfo> list=extraReportInfoService.getReportAdditionals(param, page);
		model.addAttribute("additionals", list);
		model.addAttribute("pageNo", pageNo);
		
		Subject user = SecurityUtils.getSubject();
		String username=(String)user.getPrincipal();  
		User oldu = userService.getUserByUserName(username);
		List<Organization> orgs=organizationService.getOrganizationsWithPermission(oldu,false);
		model.addAttribute("orgs", orgs);
		String message = request.getParameter("message");
		if(StringUtils.isEmpty(message))
			message="";
		if("authsucess".equals(message))
		   model.addAttribute("message", "授权补报配置成功");	
		else if("addsucess".equals(message))
		   model.addAttribute("message", "添加补报配置成功");
		else if("deletesucess".equals(message))
			   model.addAttribute("message", "删除补报配置成功");
		else if("autherror".equals(message))
			   model.addAttribute("message", "授权补报配置失败");
		else if("deleteerror".equals(message))
			model.addAttribute("message", "删除补报配置失败,您选择的数据中包含已经补报的");
		else if("adderror".equals(message))
				   model.addAttribute("message", "添加补报配置失败");
		return "system/reportadditional-list";
	}
	
	@RequestMapping(value = "/query.do")
	public String queryReportAdditionalList(
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, HttpServletRequest request) {
		Page<ExtraReportInfo> page = new Page<ExtraReportInfo>();
		page.setPageNo(pageNo);
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotEmpty(pageSize))
			  page.setPageSize(Integer.parseInt(pageSize));
		ReportQueryParam param = new ReportQueryParam();
		long orgId=ServletUtils.getRequestParamValue_int(request, "Organization");
		param.setDepartmentId(orgId);
		Page<ExtraReportInfo> list = extraReportInfoService
				.getReportAdditionals(param, page);
		model.addAttribute("additionals", list);
		model.addAttribute("selectedorgid", orgId);
		
		Subject user = SecurityUtils.getSubject();
		String username=(String)user.getPrincipal();  
		User oldu = userService.getUserByUserName(username);
				oldu.setOnline(false);
		List<Organization> orgs=organizationService.getOrganizationsWithPermission(oldu,false);
		model.addAttribute("orgs", orgs);
		model.addAttribute("pageNo", pageNo);	
		String message = request.getParameter("message");
		if(StringUtils.isEmpty(message))
			message="";
		if("authsucess".equals(message))
			   model.addAttribute("message", "授权补报配置成功");	
			else if("addsucess".equals(message))
			   model.addAttribute("message", "添加补报配置成功");
			else if("deletesucess".equals(message))
				   model.addAttribute("message", "删除补报配置成功");
			else if("autherror".equals(message))
				   model.addAttribute("message", "授权补报配置失败");
			else if("deleteerror".equals(message))
					   model.addAttribute("message", "删除补报配置失败,您选择的数据中包含已经授权的");
			else if("adderror".equals(message))
					   model.addAttribute("message", "添加补报配置失败");
		return "/system/reportadditional-list";
	}

	@RequestMapping(value = "/upload.do")
	public ModelAndView upload(
			@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request, ModelMap model) {

		System.out.println("开始");
		String path = request.getSession().getServletContext()
				.getRealPath("upload");
		String fileName = file.getOriginalFilename();
		System.out.println(path);
		File targetFile = new File(path, fileName);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}

		// 保存
		try {
			file.transferTo(targetFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("fileUrl", request.getContextPath() + "/upload/"
				+ fileName);
		return new ModelAndView("uploadResult", null);
	}
}
