package cn.gov.cbrc.bankriskcontrol.controller.risk;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import cn.gov.cbrc.bankriskcontrol.dto.AnalyseQueryParam;
import cn.gov.cbrc.bankriskcontrol.dto.ReportQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.AreaCity;
import cn.gov.cbrc.bankriskcontrol.entity.InfoTechnologyRiskEventCount;
import cn.gov.cbrc.bankriskcontrol.entity.Organization;
import cn.gov.cbrc.bankriskcontrol.entity.RiskCategory;
import cn.gov.cbrc.bankriskcontrol.entity.User;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.risk.InfoTechnologyRiskEventCountService;
import cn.gov.cbrc.bankriskcontrol.service.system.AreaCityService;
import cn.gov.cbrc.bankriskcontrol.service.system.OrganizationService;
import cn.gov.cbrc.bankriskcontrol.service.system.RiskCategoryService;
import cn.gov.cbrc.bankriskcontrol.util.ConvertUtils;
import cn.gov.cbrc.bankriskcontrol.util.ExportUtil;
import cn.gov.cbrc.bankriskcontrol.util.Page;
import cn.gov.cbrc.bankriskcontrol.util.RiskUtils;

@Controller
@RequestMapping(value = "/report/infotechnologyriskeventcount")
public class InfoTechnologyRiskEventCountController {
	@Autowired
	private InfoTechnologyRiskEventCountService infoTechnologyRiskEventCountService;
	
	@Autowired
	private RiskCategoryService riskCategoryService;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AreaCityService areaCityService;
	/**
	 * 跳转到添加指标页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/add_pre.do")
	public ModelAndView addInfoTechnologyRiskEventCount_pre(
			HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("/risk/infotechnologyriskeventcount-add", null);
	}
	
	@RequestMapping(value = "/getOrgs.do")
	@ResponseBody
	public String getOrgs(
			HttpServletRequest request, HttpServletResponse response) {
		 StringBuilder sb= new StringBuilder();
		 User user=(User)request.getSession().getAttribute("user");
		 List<Organization> orgslist = organizationService.getBelongBanksByUser(user,true);
		 if(CollectionUtils.isEmpty(orgslist))
		    return sb.toString();
		 else{
			 Map<Integer,List<Organization>> map = new HashMap<Integer,List<Organization>>();
			 for(Organization org:orgslist){
				int catelog = org.getCategory();
				if (catelog == 1)//不要监管机构
					continue;
				if(!org.isCanControl())//需要被监管
					continue;
				List<Organization> torgs = map.get(catelog);
				 if(CollectionUtils.isEmpty(torgs)){
					 map.put(catelog, new ArrayList<Organization>());
				 }
				 map.get(catelog).add(org);
			 }
			 for(int t:map.keySet()){
				 Organization tempOrg=new Organization();
				 tempOrg.setCategory(t);
				 sb.append("<dt class=\"open\">"+tempOrg.getCategoryStr()+"</dt><dd><ul>");
				 for(Organization org:map.get(t)){
					 sb.append("<li rel=\"");
					 sb.append(org.getOrgId());
					 sb.append("\">");
					 sb.append(org.getName());
					 sb.append("</li>");
				 }
				 sb.append("</ul></dd>");
			 }
			 return sb.toString(); 
		 }
	}

	@RequestMapping(value = "/gettypes.do")
	public void getTypes(
			HttpServletRequest request, HttpServletResponse response) {
		String code="6001";
		String defaultChoose=request.getParameter("defaultChoose");
		Map<String, List<RiskCategory>> map = riskCategoryService.getChildren(code);
		RiskUtils.initRiskCodeToView(map, code, response,defaultChoose);
	}
	
	@RequestMapping(value = "/add.do")
	public ModelAndView addInfoTechnologyRiskEventCount(
			HttpServletRequest request, HttpServletResponse response) {
		try{
			Date reportDate=(Date)ConvertUtils.convertStringToObject(request.getParameter("reportdate"), Date.class);
			String reportType = RiskUtils.getSelectedRiskCodeFromView(request);
			int count = Integer.parseInt(request.getParameter("count"));
			InfoTechnologyRiskEventCount item = new InfoTechnologyRiskEventCount();
			item.setRiskCode(reportType);
			item.setReportDate(reportDate);
			item.setCount(count);
			User user=(User)request.getSession().getAttribute("user");
			item.setUser(user);
			item.setOrganization(user.getOrganization());
			item.setOrgCode(user.getOrganization().getOrgNo());
			RiskCategory rc=riskCategoryService.getRiskCategoryByCode(reportType);
			item.setRiskCategory(rc);
			infoTechnologyRiskEventCountService.addInfoTechnologyRiskEventCounts(item);
		}catch(Exception ex){
			String listurl=(String)request.getSession().getAttribute("listurl");
			String queryparam=(String)request.getSession().getAttribute("queryparam");
			return new ModelAndView("redirect:"+listurl+"?message=adderror&"+queryparam);
		}
		String listurl=(String)request.getSession().getAttribute("listurl");
		String queryparam=(String)request.getSession().getAttribute("queryparam");
		return new ModelAndView("redirect:"+listurl+"?message=addsucess&"+queryparam);
	}

	@RequestMapping(value = "/list.do")
	public String getInfoTechnologyRiskEventCountList(
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, HttpServletRequest request) {
		Page<InfoTechnologyRiskEventCount> page = new Page<InfoTechnologyRiskEventCount>();
		page.setPageNo(pageNo);
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotEmpty(pageSize))
			  page.setPageSize(Integer.parseInt(pageSize));
		ReportQueryParam param = new ReportQueryParam();
		Page<InfoTechnologyRiskEventCount> list = infoTechnologyRiskEventCountService
				.getInfoTechnologyRiskEventCounts(param, page,false,false);
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
				   model.addAttribute("message","添加信息科技风险事件数量成功");
			else if("deletesucess".equals(message))
				   model.addAttribute("message","删除信息科技风险事件数量成功");
			else if("adderror".equals(message))
				   model.addAttribute("message","添加信息科技风险事件数量失败,请重新添加");
			else if("deleteerror".equals(message))
				   model.addAttribute("message","删除信息科技风险事件数量失败,请重新删除");
			else if("uploadsucess".equals(message))
				   model.addAttribute("message","数据导入成功");
			else if("uploadfail".equals(message))
				   model.addAttribute("message","数据导入失败，请检查数据格式是否准确");
			else
				   model.addAttribute("message",message); 
		}
		return "/risk/infotechnologyriskeventcount-list";
	}
	
	@RequestMapping(value = "/query.do")
	public String querySystemAvailableRateList(
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, HttpServletRequest request) {		
		Page<InfoTechnologyRiskEventCount> page = new Page<InfoTechnologyRiskEventCount>();
		page.setPageNo(pageNo);
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotEmpty(pageSize))
			  page.setPageSize(Integer.parseInt(pageSize));
		ReportQueryParam param = ExportUtil.getQueryParam(request, model);
		Page<InfoTechnologyRiskEventCount> list = infoTechnologyRiskEventCountService
				.getInfoTechnologyRiskEventCounts(param, page,false,false);
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
				   model.addAttribute("message","添加信息科技风险事件数量成功");
			else if("deletesucess".equals(message))
				   model.addAttribute("message","删除信息科技风险事件数量成功");
			else if("adderror".equals(message))
				   model.addAttribute("message","添加信息科技风险事件数量失败,请重新添加");
			else if("deleteerror".equals(message))
				   model.addAttribute("message","删除信息科技风险事件数量失败,请重新删除");
			else if("uploadsucess".equals(message))
				   model.addAttribute("message","数据导入成功");
			else
				   model.addAttribute("message",message); 
		}	
		return "/risk/infotechnologyriskeventcount-list";
	}
	
	@RequestMapping(value = "/report_pre.do")
	public String report_pre(
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, HttpServletRequest request) {
		Subject user = SecurityUtils.getSubject();
		String username=(String)user.getPrincipal();  
		User oldu = userService.getUserByUserName(username);
		List<AreaCity> areas=areaCityService.getSubAreaCityByUser(oldu);
		model.addAttribute("areas", areas);				
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("rates", new Page<InfoTechnologyRiskEventCount>());
		model.addAttribute("chartdata", "{}");
		model.addAttribute("flag", true);		
		return "/risk/infotechnologyriskeventcount-report";
	}
	
	
	@RequestMapping(value = "/report.do")
	public String report(
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, HttpServletRequest request) {
		Page<InfoTechnologyRiskEventCount> page1 = new Page<InfoTechnologyRiskEventCount>();
		page1.setPageNo(pageNo);
		Page<InfoTechnologyRiskEventCount> page2 = new Page<InfoTechnologyRiskEventCount>();
		page1.setPageNo(pageNo);
		page2.setPageSize(Integer.MAX_VALUE);
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotEmpty(pageSize))
			  page1.setPageSize(Integer.parseInt(pageSize));
		
		AnalyseQueryParam param = ExportUtil.getAnalyseQueryParam(request, model);
		Page<InfoTechnologyRiskEventCount> rates = infoTechnologyRiskEventCountService
				.getInfoTechnologyRiskEventCounts(param, page1);
		Page<InfoTechnologyRiskEventCount> rates_total = infoTechnologyRiskEventCountService
				.getInfoTechnologyRiskEventCounts(param, page2);
		
		Subject user = SecurityUtils.getSubject();
		String username=(String)user.getPrincipal();  
		User oldu = userService.getUserByUserName(username);
		List<AreaCity> areas=areaCityService.getSubAreaCityByUser(oldu);
		model.addAttribute("areas", areas);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("rates", rates);
		
		String echarts=ExportUtil.getEchartStringInfo(riskCategoryService, organizationService, param, rates_total.getResult());
		model.addAttribute("chartdata", echarts);
		model.addAttribute("flag", false);		
		return "/risk/infotechnologyriskeventcount-report";
	}

	@RequestMapping(value = "/delete.do")
	public ModelAndView deleteInfoTechnologyRiskEventCount(
			HttpServletRequest request, HttpServletResponse response) {
		try{
			String deleteids= request.getParameter("deleteids");
			String[] ids=deleteids.split(",");
			for(String temp:ids){
				long id = Long.parseLong(temp);
				infoTechnologyRiskEventCountService.deleteInfoTechnologyRiskEventCount(id);
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
	
	@RequestMapping(value = "/export.do")
	public String export(
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, HttpServletRequest request,HttpServletResponse response) {
		Page<InfoTechnologyRiskEventCount> page = ExportUtil.getQueryPage(pageNo, new InfoTechnologyRiskEventCount());
		ReportQueryParam param = ExportUtil.getQueryParam(request, model);
		Page<InfoTechnologyRiskEventCount> list = infoTechnologyRiskEventCountService.getInfoTechnologyRiskEventCounts(param, page,false,false);
		List<String[]> valueList=new ArrayList<String[]>();
		valueList.add(new String[]{"机构","指标类型","指标日期(期数)","风险数量"});
		for(InfoTechnologyRiskEventCount rate:list.getResult()){
			String orgName=rate.getOrganization().getName();
			long count=rate.getCount();
			valueList.add(new String[]{orgName,rate.getRiskCategory().getRiskName(),rate.getShowDate(),count+""});
		}
		ExportUtil.exportExcel(valueList, "信息科技风险事件数量", response);
		return null;
	}
	
	@RequestMapping(value="/upload.do")
	@ResponseBody
	public String upload(HttpServletRequest request, HttpServletResponse response) {
		MultipartHttpServletRequest mhs = (MultipartHttpServletRequest) request;
		List<MultipartFile> files = mhs.getFiles("files");
		StringBuilder sb = new StringBuilder();
		for (MultipartFile file : files) {
			try {
				HSSFWorkbook wb = new HSSFWorkbook(file.getInputStream());
				int sheetnum = wb.getNumberOfSheets();
				for (int i = 0; i < sheetnum; i++) {
					HSSFSheet sheet = wb.getSheetAt(i);
					int frownum = sheet.getFirstRowNum();
					int erownum = sheet.getLastRowNum();
					for (int k = frownum + 1; k <= erownum; k++) {
						try{
							HSSFRow row = sheet.getRow(k);
							if(row.getCell(0)==null)
								break;
							String reportType = ConvertUtils.doubleToInt(row.getCell(0).getNumericCellValue())+"";
							Date reportDate = row.getCell(1).getDateCellValue();
							int count = ConvertUtils.doubleToInt(row.getCell(2).getNumericCellValue());
							InfoTechnologyRiskEventCount item = new InfoTechnologyRiskEventCount();
							item.setRiskCode(reportType);
							item.setReportDate(reportDate);
							item.setCount(count);
							User user = (User) request.getSession().getAttribute("user");
							item.setUser(user);
							item.setOrganization(user.getOrganization());
							item.setOrgCode(user.getOrganization().getOrgNo());
							RiskCategory rc = riskCategoryService.getRiskCategoryByCode(reportType);
							item.setRiskCategory(rc);
							infoTechnologyRiskEventCountService.addInfoTechnologyRiskEventCounts(item);
						}catch(Exception ex){
							sb.append(k);
							sb.append(",");
						}
						
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "uploadfail";
			}
		}
		String failstr = sb.toString();
		if("".equals(failstr))
		   return "uploadsucess";
		else{
		   return "部分数据未上传成功,失败行数："+failstr+"请检查这些数据是否合法并重新上传";
		}
	}
}
