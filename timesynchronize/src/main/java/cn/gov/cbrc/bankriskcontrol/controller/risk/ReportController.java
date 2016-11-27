package cn.gov.cbrc.bankriskcontrol.controller.risk;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.gov.cbrc.bankriskcontrol.dto.MonitorRate;
import cn.gov.cbrc.bankriskcontrol.dto.ReportQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.AreaCity;
import cn.gov.cbrc.bankriskcontrol.entity.ElectronicActiveUserChangeRate;
import cn.gov.cbrc.bankriskcontrol.entity.ElectronicTransactionChangeRate;
import cn.gov.cbrc.bankriskcontrol.entity.FakeSiteAttachmentRate;
import cn.gov.cbrc.bankriskcontrol.entity.InfoTechnologyRiskEventCount;
import cn.gov.cbrc.bankriskcontrol.entity.OperationChangeSuccessRate;
import cn.gov.cbrc.bankriskcontrol.entity.Organization;
import cn.gov.cbrc.bankriskcontrol.entity.OutsideAttackChangeRate;
import cn.gov.cbrc.bankriskcontrol.entity.RiskCategory;
import cn.gov.cbrc.bankriskcontrol.entity.RiskTca;
import cn.gov.cbrc.bankriskcontrol.entity.SystemAvailableRate;
import cn.gov.cbrc.bankriskcontrol.entity.SystemTransactionSuccessRate;
import cn.gov.cbrc.bankriskcontrol.entity.User;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.risk.ElectronicActiveUserChangeRateService;
import cn.gov.cbrc.bankriskcontrol.service.risk.ElectronicTransactionChangeRateService;
import cn.gov.cbrc.bankriskcontrol.service.risk.FakeSiteAttachmentRateService;
import cn.gov.cbrc.bankriskcontrol.service.risk.InfoTechnologyRiskEventCountService;
import cn.gov.cbrc.bankriskcontrol.service.risk.OperationChangeSuccessRateService;
import cn.gov.cbrc.bankriskcontrol.service.risk.OutsideAttackChangeRateService;
import cn.gov.cbrc.bankriskcontrol.service.risk.RiskTcaService;
import cn.gov.cbrc.bankriskcontrol.service.risk.SystemAvailableRateService;
import cn.gov.cbrc.bankriskcontrol.service.risk.SystemTransactionSuccessRateService;
import cn.gov.cbrc.bankriskcontrol.service.system.AreaCityService;
import cn.gov.cbrc.bankriskcontrol.service.system.OrganizationService;
import cn.gov.cbrc.bankriskcontrol.service.system.RiskCategoryService;
import cn.gov.cbrc.bankriskcontrol.util.ConvertUtils;
import cn.gov.cbrc.bankriskcontrol.util.DateUtil;
import cn.gov.cbrc.bankriskcontrol.util.ExportUtil;
import cn.gov.cbrc.bankriskcontrol.util.Page;
import cn.gov.cbrc.bankriskcontrol.util.RiskUtils;
import cn.gov.cbrc.bankriskcontrol.util.encode.EncodeUtils;

@Controller
@RequestMapping(value = "/report/report")
public class ReportController {
	
	@Autowired
	private RiskCategoryService riskCategoryService;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RiskTcaService riskTcaService;
	
	@Autowired
	private AreaCityService areaCityService;
	
	@Autowired
	private ElectronicActiveUserChangeRateService electronicActiveUserChangeRateService;
	
	@Autowired
	private ElectronicTransactionChangeRateService electronicTransactionChangeRateService;
	
	@Autowired
	private FakeSiteAttachmentRateService fakeSiteAttachmentRateService;
	
	@Autowired
	private InfoTechnologyRiskEventCountService infoTechnologyRiskEventCountService;
	
	@Autowired
	private OperationChangeSuccessRateService operationChangeSuccessRateService;
	
	@Autowired
	private OutsideAttackChangeRateService outsideAttackChangeRateService;
	
	@Autowired
	private SystemAvailableRateService systemAvailableRateService;
	
	@Autowired
	private SystemTransactionSuccessRateService systemTransactionSuccessRateService;
	
	private static Map<String,List<String>> map;
	
	private static List<String> typeList;
	
	private static Map<String,List<String>> propertyMap;
	
	static {
		map = new HashMap<String, List<String>>();
		map.put("systemavilabelrate", ConvertUtils.newArrayList("1101", "1102", "1103", "1104", "1105", "1106", "1107", "1108", "1109"));
		map.put("systemtransactionsuccessrate", ConvertUtils.newArrayList("2001", "2001001", "2001002", "2002",
				"2002001", "2002002", "2002003", "2002004", "2002005", "2003", "2003001", "2003002", "2003003",
				"2003004", "2004", "2004001", "2004002", "2004003", "2004004", "2004005", "2004006", "2004007", "2005",
				"2006"));
		map.put("deploymentchangesuccessrate", ConvertUtils.newArrayList("3001"));
		map.put("closedfishingwebsiterate", ConvertUtils.newArrayList("4001"));
		map.put("externalattackchangerate", ConvertUtils.newArrayList("5001"));
		map.put("informationriskevent", ConvertUtils.newArrayList("6001001001", "6001001002", "6001001003",
				"6001001004", "6001001005", "6001001006", "6001001007", "6001001008", "6001001009", "6001001010",
				"6001001011", "6001001012", "6001001013", "6001001014", "6001001015", "6001001016", "6001001017",
				"6001001018","6001002","6001003","6001004","6001005","6001006","6001007"));
		map.put("electronicchanneltransactionchangerate",
				ConvertUtils.newArrayList("7001", "7002", "7003", "7004", "7005", "7006", "7007"));
		map.put("electronicbankactiveuserchangerate", ConvertUtils.newArrayList("8001", "8002", "8003"));
		typeList = ConvertUtils.newArrayList("systemavilabelrate", "systemtransactionsuccessrate",
				"deploymentchangesuccessrate", "closedfishingwebsiterate", "externalattackchangerate",
				"informationriskevent", "electronicchanneltransactionchangerate", "electronicbankactiveuserchangerate");
		propertyMap=new HashMap<String,List<String>>();
		propertyMap.put("systemavilabelrate", ConvertUtils.newArrayList("sost", "uost", "tst"));
		propertyMap.put("systemtransactionsuccessrate", ConvertUtils.newArrayList("nst", "tnt"));
		propertyMap.put("deploymentchangesuccessrate", ConvertUtils.newArrayList("nsdc","tndc"));
		propertyMap.put("closedfishingwebsiterate", ConvertUtils.newArrayList("ncfw","nfw"));
		propertyMap.put("externalattackchangerate", ConvertUtils.newArrayList("nidscp","nipscp"));
		propertyMap.put("informationriskevent", ConvertUtils.newArrayList("count"));
		propertyMap.put("electronicchanneltransactionchangerate",
				ConvertUtils.newArrayList("ntcp"));
		propertyMap.put("electronicbankactiveuserchangerate", ConvertUtils.newArrayList("naucp"));
	}
	
	private List<RiskCategory> getAllRiskCategory(){
		List<RiskCategory> list =riskCategoryService.getChildRiskCategorys("");		
		return list;
	}
	
	@RequestMapping(value = "/report1.do")
	public String report1(
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, HttpServletRequest request) {
		ReportQueryParam param = ExportUtil.getExcelQueryParam(request, model);
		String reporttype=param.getReportType();
		Page<MonitorRate> page3 = new Page<MonitorRate>();
		page3.setPageNo(pageNo);
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotEmpty(pageSize))
			  page3.setPageSize(Integer.parseInt(pageSize));
		List<MonitorRate> result = null;
		if (reporttype != null) {
			RiskCategory riskCategory = riskCategoryService.getRiskCategoryByCode(reporttype);
			Date exactDate = param.getExactDate();
			int period = RiskUtils.getPeroidByReportDate(riskCategory, exactDate);

			ReportQueryParam lastYearParam = param.getCopyObject();
			lastYearParam.setExactDate(ExportUtil.getDateByPeriod(riskCategory, exactDate.getYear() - 1, period));
			ReportQueryParam lastPeriodParam = param.getCopyObject();
			lastPeriodParam.setExactDate(ExportUtil.getDateByPeriod(riskCategory, exactDate.getYear(), period - 1));
			List<RiskCategory> categorys = riskCategoryService.getChildRiskCategoryByTopCode(reporttype);
			if ("1".equals(reporttype)) {
				result = ExportUtil.getAnalyseResult(SystemAvailableRate.class, systemAvailableRateService,
						"getSystemAvailableRates", categorys, "percentActual", true, param, lastYearParam,
						lastPeriodParam);
			} else if ("2".equals(reporttype)) {
				result = ExportUtil.getAnalyseResult(SystemTransactionSuccessRate.class,
						systemTransactionSuccessRateService, "getSystemTransactionSuccessRates", categorys, "percent",
						true, param, lastYearParam, lastPeriodParam);
			} else if ("3".equals(reporttype)) {
				result = ExportUtil.getAnalyseResult(OperationChangeSuccessRate.class,
						operationChangeSuccessRateService, "getOperationChangeSuccessRates", categorys, "percent",
						true, param, lastYearParam, lastPeriodParam);
			} else if ("4".equals(reporttype)) {
				result = ExportUtil
						.getAnalyseResult(FakeSiteAttachmentRate.class, fakeSiteAttachmentRateService,
								"getFakeSiteAttachmentRates", categorys, "percent", true, param, lastYearParam,
								lastPeriodParam);
			} else if ("5".equals(reporttype)) {
				result = ExportUtil
						.getAnalyseResult(OutsideAttackChangeRate.class, outsideAttackChangeRateService,
								"getOutsideAttackChangeRates", categorys, "percent", true, param, lastYearParam,
								lastPeriodParam);
			} else if ("6001".equals(reporttype)) {
				result = ExportUtil.getAnalyseResult(InfoTechnologyRiskEventCount.class,
						infoTechnologyRiskEventCountService, "getInfoTechnologyRiskEventCounts", categorys, "count",
						false, param, lastYearParam, lastPeriodParam);
			} else if ("7".equals(reporttype)) {
				result = ExportUtil.getAnalyseResult(ElectronicTransactionChangeRate.class,
						electronicTransactionChangeRateService, "getElectronicTransactionChangeRates", categorys,
						"percent", true, param, lastYearParam, lastPeriodParam);
			} else if ("8".equals(reporttype)) {
				result = ExportUtil.getAnalyseResult(ElectronicActiveUserChangeRate.class,
						electronicActiveUserChangeRateService, "getElectronicActiveUserChangeRates", categorys,
						"percent", true, param, lastYearParam, lastPeriodParam);
			}
		}else{
			model.addAttribute("exactDate", DateUtil.getShortCurrentDate()); 
		}
		page3.setResult(result);

		model.addAttribute("rates", page3);
		Subject user = SecurityUtils.getSubject();
		String username=(String)user.getPrincipal();  
		User oldu = userService.getUserByUserName(username);
				oldu.setOnline(false);
		List<Organization> orgs=organizationService.getBelongBanksByUser(oldu,true);
		model.addAttribute("orgs", orgs);
		model.addAttribute("pageNo", pageNo);	
		model.addAttribute("risktargets", getAllRiskCategory()); 
		String risktarget = request.getParameter("risktarget");
		if(StringUtils.isEmpty(risktarget))
			risktarget="";
		model.addAttribute("target", risktarget);	
		return "/risk/report1";
	}
	
	@RequestMapping(value = "/report2.do")
	public String report2(
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, HttpServletRequest request) {
		ReportQueryParam param = ExportUtil.getExcelQueryParam(request, model);
		String reporttype=param.getReportType();
		Page<MonitorRate> page3 = new Page<MonitorRate>();
		page3.setPageNo(pageNo);
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotEmpty(pageSize))
			  page3.setPageSize(Integer.parseInt(pageSize));
		List<MonitorRate> result = null;
		RiskCategory riskCategory = riskCategoryService.getRiskCategoryByCode(reporttype);			
			List<RiskCategory> categorys = riskCategoryService.getChildRiskCategoryByTopCode(reporttype);
			if ("1".equals(reporttype)) {
				result=ExportUtil.getAnalyseResult(SystemAvailableRate.class,
						systemAvailableRateService, "getSystemAvailableRates", categorys, "percentActual",
						true, param,page3,riskCategory,false);
			} else if ("2".equals(reporttype)) {
				result = ExportUtil.getAnalyseResult(SystemTransactionSuccessRate.class,
						systemTransactionSuccessRateService, "getSystemTransactionSuccessRates", categorys, "percent",
						true, param,page3,riskCategory,false);
			} else if ("3".equals(reporttype)) {
				result = ExportUtil.getAnalyseResult(OperationChangeSuccessRate.class,
						operationChangeSuccessRateService, "getOperationChangeSuccessRates", categorys, "percent",
						true, param,page3,riskCategory,false);
			} else if ("4".equals(reporttype)) {
				result = ExportUtil
						.getAnalyseResult(FakeSiteAttachmentRate.class, fakeSiteAttachmentRateService,
								"getFakeSiteAttachmentRates", categorys, "percent", true, param,page3,riskCategory,false);
			} else if ("5".equals(reporttype)) {
				result = ExportUtil
						.getAnalyseResult(OutsideAttackChangeRate.class, outsideAttackChangeRateService,
								"getOutsideAttackChangeRates", categorys, "percent", true, param,page3,riskCategory,false);
			} else if ("6001".equals(reporttype)) {
				result = ExportUtil.getAnalyseResult(InfoTechnologyRiskEventCount.class,
						infoTechnologyRiskEventCountService, "getInfoTechnologyRiskEventCounts", categorys, "count",
						false, param,page3,riskCategory,false);
			} else if ("7".equals(reporttype)) {
				result = ExportUtil.getAnalyseResult(ElectronicTransactionChangeRate.class,
						electronicTransactionChangeRateService, "getElectronicTransactionChangeRates", categorys,
						"percent", true, param,page3,riskCategory,false);
			} else if ("8".equals(reporttype)) {
				result = ExportUtil.getAnalyseResult(ElectronicActiveUserChangeRate.class,
						electronicActiveUserChangeRateService, "getElectronicActiveUserChangeRates", categorys,
						"percent", true, param,page3,riskCategory,false);
			}
		page3.setResult(result);

		model.addAttribute("rates", page3);
		Subject user = SecurityUtils.getSubject();
		String username=(String)user.getPrincipal();  
		User oldu = userService.getUserByUserName(username);
				oldu.setOnline(false);
		List<Organization> orgs=organizationService.getBelongBanksByUser(oldu,true);
		model.addAttribute("orgs", orgs);
		model.addAttribute("pageNo", pageNo);	
		model.addAttribute("risktargets", getAllRiskCategory()); 
		String risktarget = request.getParameter("risktarget");
		if(StringUtils.isEmpty(risktarget))
			risktarget="";
		model.addAttribute("target", risktarget);	
		return "/risk/report2";
	}
	
	@RequestMapping(value = "/report3_pre.do")
	public String report3_pre(@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, HttpServletRequest request,HttpServletResponse response){
		Subject user = SecurityUtils.getSubject();
		String username=(String)user.getPrincipal();  
		User oldu = userService.getUserByUserName(username);
		List<AreaCity> areas=areaCityService.getSubAreaCityByUser(oldu);
		model.addAttribute("areas", areas);		
		model.addAttribute("period", 3);	
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("rates", new Page<MonitorRate>());
		return "/risk/report3";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/report3.do")
	public String report3(
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		String periodStr = request.getParameter("period");
		int period = StringUtils.isEmpty(periodStr)?3:Integer.parseInt(periodStr);
		String area =request.getParameter("area");
		String category =request.getParameter("category");
		if(StringUtils.isEmpty(category)){
			category="";
		} 
		String reportorder =request.getParameter("reportorder");
		String hql="";
		if(StringUtils.isNotEmpty(category)){
			String[] departlist = category.split(",");
			List<Long> llist = new ArrayList<Long>();
			for(String str:departlist)
				llist.add(Long.parseLong(str));
			hql+=" and a.organization.orgId in ("+ConvertUtils.convertList2String(llist, ",")+")";
		} 
		if(StringUtils.isNotEmpty(area)){
			hql+=" and a.areaCode like '" + area + "%'";
		}
		hql+=" and a.organization.canControl='1'";
		
		List<SystemAvailableRate> list1 = systemAvailableRateService.findLastPeriod(SystemAvailableRate.class, period,hql);
		List<SystemTransactionSuccessRate> list2 = systemAvailableRateService.findLastPeriod(
				SystemTransactionSuccessRate.class, period,hql);
		List<ElectronicActiveUserChangeRate> list3 = systemAvailableRateService.findLastPeriod(
				ElectronicActiveUserChangeRate.class, period,hql);
		List<ElectronicTransactionChangeRate> list4 = systemAvailableRateService.findLastPeriod(
				ElectronicTransactionChangeRate.class, period,hql);
		List<OperationChangeSuccessRate> list5 = systemAvailableRateService.findLastPeriod(
				OperationChangeSuccessRate.class, period,hql);
		List<InfoTechnologyRiskEventCount> list6 = systemAvailableRateService.findLastPeriod(
				InfoTechnologyRiskEventCount.class, period,hql);
		List<FakeSiteAttachmentRate> list7 = systemAvailableRateService.findLastPeriod(FakeSiteAttachmentRate.class,
				period,hql);
		List<OutsideAttackChangeRate> list8 = systemAvailableRateService.findLastPeriod(OutsideAttackChangeRate.class,
				period,hql);
		List<MonitorRate> rates = new ArrayList<MonitorRate>();
		List<Object> list = new ArrayList<Object>();		
		list.addAll(list2);
		list.addAll(list3);
		list.addAll(list4);
		list.addAll(list5);
		list.addAll(list7);
		list.addAll(list8);
		List<RiskCategory> categorys=riskCategoryService.getChildRiskCategorys("");
		
		for (Object object : list) {
			rates.add(ExportUtil.getMonitorRate(object, getTopRiskCategory(categorys,object),"percent", true));
		}
		for (SystemAvailableRate object : list1) {
			MonitorRate rate = ExportUtil.getMonitorRate(object, getTopRiskCategory(categorys,object),"percentActual", true);
			rate.setValue("名义可用率"+ConvertUtils.get2pointDouble(object.getPercentExpected()) + "%"+" 实际可用率"+ConvertUtils.get2pointDouble(object.getPercentActual()) + "%");
			rates.add(rate);
		}
		for (Object object : list6) {
			rates.add(ExportUtil.getMonitorRate(object, getTopRiskCategory(categorys,object),"count", false));
		}
		
		Page<MonitorRate> page3 = new Page<MonitorRate>();
		page3.setPageNo(pageNo);
		String pageSizeStr = request.getParameter("pageSize");
		if(StringUtils.isNotEmpty(pageSizeStr))
			  page3.setPageSize(Integer.parseInt(pageSizeStr));
		int size=rates.size();
		int pageSize=page3.getPageSize();
		int firstIndex=(pageNo-1)*pageSize;
		int lastIndex=pageNo*pageSize;
		List<MonitorRate> result=new ArrayList<MonitorRate>();
		if(lastIndex<size){
			for(int i=firstIndex;i<lastIndex;i++){
				result.add(rates.get(i));
			}
		}else{
			for(int i=firstIndex;i<size;i++){
				result.add(rates.get(i));
			}
		}
		page3.setResult(result);
		page3.setTotalCount(size);
		model.addAttribute("rates", page3);
		
		Subject user = SecurityUtils.getSubject();
		String username=(String)user.getPrincipal();  
		User oldu = userService.getUserByUserName(username);
		List<AreaCity> areas=areaCityService.getSubAreaCityByUser(oldu);
		model.addAttribute("areas", areas);	
		model.addAttribute("reportorder", reportorder);
		model.addAttribute("area", area);
		model.addAttribute("category", category);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("period", periodStr);
		
		return "/risk/report3";
	}
	
	@RequestMapping(value = "/report4_pre.do")
	public String report4_pre(@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, HttpServletRequest request,HttpServletResponse response){
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("rates", new Page<RiskTca>());
		return "/risk/report4";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/report4.do")
	public String report4(
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, HttpServletRequest request,HttpServletResponse response){
		Page<RiskTca> page3 = new Page<RiskTca>();
		page3.setPageNo(pageNo);
		String pageSizeStr = request.getParameter("pageSize");
		if(StringUtils.isNotEmpty(pageSizeStr))
			  page3.setPageSize(Integer.parseInt(pageSizeStr));
		
		List<RiskTca> tcas=new ArrayList<RiskTca>();
		Date startDate=null;
		Date endDate=null;
		String startdatestr =request.getParameter("startdate");
		if(StringUtils.isNotEmpty(startdatestr)){
			startDate=(Date)ConvertUtils.convertStringToObject(startdatestr, Date.class);
		}else{
			startdatestr="";
		}
		String endtdatestr =request.getParameter("endtdate");
		if(StringUtils.isNotEmpty(endtdatestr)){
			endDate=(Date)ConvertUtils.convertStringToObject(endtdatestr, Date.class);
		}else{
			endtdatestr="";
		}
		model.addAttribute("startdate", startdatestr);
		model.addAttribute("enddate", endtdatestr);
		
		String dateSql="a.organization.canControl='1'";
		if (startDate != null) {
			dateSql+=" and a.reportDate>='"+DateUtil.getShortDate(startDate)+"'";
		}
		if (endDate != null) {
			dateSql+=" and a.reportDate<='"+DateUtil.getShortDate(endDate)+"'";
		}
		List<SystemAvailableRate> avas=systemAvailableRateService.find(SystemAvailableRate.class,"select distinct a from SystemAvailableRate a,RiskCategory rc where "+dateSql+" and rc.riskCode=a.riskCode and rc.useThresholdValue='1' and ((1-(a.ud+a.pd+0.0)/a.ltsp)*100>rc.maxValue or (1-(a.ud+a.pd+0.0)/a.ltsp)*100<rc.minValue) order by a.reportDate desc");
		for(SystemAvailableRate rate:avas){
			RiskTca tca=getRiskTca(rate);
			tca.setCurrentValue(ConvertUtils.get2pointDouble(rate.getPercentActual()) + "%");
			tcas.add(tca);
		}
		List<SystemTransactionSuccessRate> trans=systemAvailableRateService.find(SystemTransactionSuccessRate.class,"select distinct a from SystemTransactionSuccessRate a,RiskCategory rc where "+dateSql+" and rc.riskCode=a.riskCode and rc.useThresholdValue='1' and ((a.aosst+0.0)/a.aost*100>rc.maxValue or (a.aosst+0.0)/a.aost*100<rc.minValue) order by a.reportDate desc");
		for(SystemTransactionSuccessRate rate:trans){
			RiskTca tca=getRiskTca(rate);
			tca.setCurrentValue(ConvertUtils.get2pointDouble(rate.getPercent()) + "%");
			tcas.add(tca);
		}
		List<OperationChangeSuccessRate> opers=systemAvailableRateService.find(OperationChangeSuccessRate.class,"select distinct a from OperationChangeSuccessRate a,RiskCategory rc where "+dateSql+" and rc.riskCode=a.riskCode and rc.useThresholdValue='1' and ((a.sdc+0.0)/a.dc*100>rc.maxValue or (a.sdc+0.0)/a.dc*100<rc.minValue) order by a.reportDate desc");
		for(OperationChangeSuccessRate rate:opers){
			RiskTca tca=getRiskTca(rate);
			tca.setCurrentValue(ConvertUtils.get2pointDouble(rate.getPercent()) + "%");
			tcas.add(tca);
		}
		List<FakeSiteAttachmentRate> fakes=systemAvailableRateService.find(FakeSiteAttachmentRate.class,"select distinct a from FakeSiteAttachmentRate a,RiskCategory rc where "+dateSql+" and rc.riskCode=a.riskCode and rc.useThresholdValue='1' and ((a.cfw+0.0)/a.aofw*100>rc.maxValue or (a.cfw+0.0)/a.aofw*100<rc.minValue) order by a.reportDate desc");
		for(FakeSiteAttachmentRate rate:fakes){
			RiskTca tca=getRiskTca(rate);
			tca.setCurrentValue(ConvertUtils.get2pointDouble(rate.getPercent()) + "%");
			tcas.add(tca);
		}
		List<InfoTechnologyRiskEventCount> infos=systemAvailableRateService.find(InfoTechnologyRiskEventCount.class,"select distinct a from InfoTechnologyRiskEventCount a,RiskCategory rc where "+dateSql+" and rc.riskCode=a.riskCode and rc.useThresholdValue='1' and (a.count>rc.maxValue or a.count<rc.minValue) order by a.reportDate desc");
		for(InfoTechnologyRiskEventCount rate:infos){
			RiskTca tca=getRiskTca(rate);
			tca.setCurrentValue(rate.getCount()+"");
			tcas.add(tca);
		}
		List<OutsideAttackChangeRate> outs=systemAvailableRateService.find(OutsideAttackChangeRate.class,"select distinct a from OutsideAttackChangeRate a,RiskCategory rc where "+dateSql+" and rc.riskCode=a.riskCode and rc.useThresholdValue='1' and (((a.idswp+a.ipswp+0.0)/(a.ldswp+a.lpswp)-1)*100>rc.maxValue or ((a.idswp+a.ipswp+0.0)/(a.ldswp+a.lpswp)-1)*100<rc.minValue) order by a.reportDate desc");
		for(OutsideAttackChangeRate rate:outs){
			RiskTca tca=getRiskTca(rate);
			tca.setCurrentValue(ConvertUtils.get2pointDouble(rate.getPercent()) + "%");
			tcas.add(tca);
		}
		List<ElectronicActiveUserChangeRate> users=systemAvailableRateService.find(ElectronicActiveUserChangeRate.class,"select distinct a from ElectronicActiveUserChangeRate a,RiskCategory rc where "+dateSql+" and rc.riskCode=a.riskCode and rc.useThresholdValue='1' and (((a.currentActiveUser+0.0)/a.lastActiveUser-1)*100>rc.maxValue or ((a.currentActiveUser+0.0)/a.lastActiveUser-1)*100<rc.minValue) order by a.reportDate desc");
		for(ElectronicActiveUserChangeRate rate:users){
			RiskTca tca=getRiskTca(rate);
			tca.setCurrentValue(ConvertUtils.get2pointDouble(rate.getPercent()) + "%");
			tcas.add(tca);
		}
		List<ElectronicTransactionChangeRate> eletrans=systemAvailableRateService.find(ElectronicTransactionChangeRate.class,"select distinct a from ElectronicTransactionChangeRate a,RiskCategory rc where "+dateSql+" and rc.riskCode=a.riskCode and rc.useThresholdValue='1' and (((a.currentTradingVolume+0.0)/a.lastTradingVolume-1)*100>rc.maxValue or ((a.currentTradingVolume+0.0)/a.lastTradingVolume-1)*100<rc.minValue) order by a.reportDate desc");
		for(ElectronicTransactionChangeRate rate:eletrans){
			RiskTca tca=getRiskTca(rate);
			tca.setCurrentValue(ConvertUtils.get2pointDouble(rate.getPercent()) + "%");
			tcas.add(tca);
		}
		int size=tcas.size();
		int pageSize=page3.getPageSize();
		int firstIndex=(pageNo-1)*pageSize;
		int lastIndex=pageNo*pageSize;
		List<RiskTca> result=new ArrayList<RiskTca>();
		if(lastIndex<size){
			for(int i=firstIndex;i<lastIndex;i++){
				result.add(tcas.get(i));
			}
		}else{
			for(int i=firstIndex;i<size;i++){
				result.add(tcas.get(i));
			}
		}
		page3.setResult(result);
		page3.setTotalCount(size);
		model.addAttribute("rates", page3);	
		model.addAttribute("pageNo", pageNo);
		return "/risk/report4";
	}
	
	@RequestMapping(value = "/export1.do")
	public String export1(
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, HttpServletRequest request,HttpServletResponse response) {		
		ReportQueryParam param = ExportUtil.getExcelQueryParam(request, model);
		String reporttype=param.getReportType();
		RiskCategory riskCategory=riskCategoryService.getRiskCategoryByCode(reporttype);
		Date exactDate=param.getExactDate();
		int period=RiskUtils.getPeroidByReportDate(riskCategory, exactDate);
		
		ReportQueryParam lastYearParam=param.getCopyObject();
		lastYearParam.setExactDate(ExportUtil.getDateByPeriod(riskCategory, exactDate.getYear()-1, period));
		ReportQueryParam lastPeriodParam=param.getCopyObject();
		lastPeriodParam.setExactDate(ExportUtil.getDateByPeriod(riskCategory, exactDate.getYear(), period-1));
		
		Page<MonitorRate> page3 = new Page<MonitorRate>();
		page3.setPageNo(pageNo);
		page3.setPageSize(Integer.MAX_VALUE);
		List<MonitorRate> result=new ArrayList<MonitorRate>(); 
		
		List<RiskCategory> categorys = riskCategoryService.getChildRiskCategoryByTopCode(reporttype);
		
		if ("1".equals(reporttype)) {
			result = ExportUtil.getAnalyseResult(SystemAvailableRate.class, systemAvailableRateService,
					"getSystemAvailableRates", categorys, "percentActual", true, param, lastYearParam, lastPeriodParam);
		} else if ("2".equals(reporttype)) {
			result = ExportUtil.getAnalyseResult(SystemTransactionSuccessRate.class,
					systemTransactionSuccessRateService, "getSystemTransactionSuccessRates", categorys, "percent",
					true, param, lastYearParam, lastPeriodParam);
		} else if ("3".equals(reporttype)) {
			result = ExportUtil
					.getAnalyseResult(OperationChangeSuccessRate.class, operationChangeSuccessRateService,
							"getOperationChangeSuccessRates", categorys, "percent", true, param, lastYearParam,
							lastPeriodParam);
		} else if ("4".equals(reporttype)) {
			result = ExportUtil.getAnalyseResult(FakeSiteAttachmentRate.class, fakeSiteAttachmentRateService,
					"getFakeSiteAttachmentRates", categorys, "percent", true, param, lastYearParam, lastPeriodParam);
		} else if ("5".equals(reporttype)) {
			result = ExportUtil.getAnalyseResult(OutsideAttackChangeRate.class, outsideAttackChangeRateService,
					"getOutsideAttackChangeRates", categorys, "percent", true, param, lastYearParam, lastPeriodParam);
		} else if ("6001".equals(reporttype)) {
			result = ExportUtil.getAnalyseResult(InfoTechnologyRiskEventCount.class,
					infoTechnologyRiskEventCountService, "getInfoTechnologyRiskEventCounts", categorys, "count", false,
					param, lastYearParam, lastPeriodParam);
		} else if ("7".equals(reporttype)) {
			result = ExportUtil.getAnalyseResult(ElectronicTransactionChangeRate.class,
					electronicTransactionChangeRateService, "getElectronicTransactionChangeRates", categorys,
					"percent", true, param, lastYearParam, lastPeriodParam);
		} else if ("8".equals(reporttype)) {
			result = ExportUtil.getAnalyseResult(ElectronicActiveUserChangeRate.class,
					electronicActiveUserChangeRateService, "getElectronicActiveUserChangeRates", categorys,
					"percent", true, param, lastYearParam, lastPeriodParam);
		}
		page3.setResult(result);						
	    long orgId=param.getDepartmentId();
	    Organization org=organizationService.getOrganization(orgId);
		String fileName = org.getName()+"第" + period + "期同比环比风险分析表";
		ExportUtil.exportExcel_tongbi(fileName, period, categorys, result, response);
		return null;
	}
	
	@RequestMapping(value = "/export2.do")
	public String export2(
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, HttpServletRequest request,HttpServletResponse response) {
		ReportQueryParam param = ExportUtil.getExcelQueryParam(request, model);
		String reporttype=param.getReportType();
		Page<MonitorRate> page3 = new Page<MonitorRate>();
		page3.setPageNo(pageNo);
		page3.setPageSize(Integer.MAX_VALUE);
		List<MonitorRate> result = null;		
        RiskCategory riskCategory = riskCategoryService.getRiskCategoryByCode(reporttype);
			
			List<RiskCategory> categorys = riskCategoryService.getChildRiskCategoryByTopCode(reporttype);
			if ("1".equals(reporttype)) {
				result=ExportUtil.getAnalyseResult(SystemAvailableRate.class,
						systemAvailableRateService, "getSystemAvailableRates", categorys, "percentActual",
						true, param,page3,riskCategory,true);
			} else if ("2".equals(reporttype)) {
				result = ExportUtil.getAnalyseResult(SystemTransactionSuccessRate.class,
						systemTransactionSuccessRateService, "getSystemTransactionSuccessRates", categorys, "percent",
						true, param,page3,riskCategory,true);
			} else if ("3".equals(reporttype)) {
				result = ExportUtil.getAnalyseResult(OperationChangeSuccessRate.class,
						operationChangeSuccessRateService, "getOperationChangeSuccessRates", categorys, "percent",
						true, param,page3,riskCategory,true);
			} else if ("4".equals(reporttype)) {
				result = ExportUtil
						.getAnalyseResult(FakeSiteAttachmentRate.class, fakeSiteAttachmentRateService,
								"getFakeSiteAttachmentRates", categorys, "percent", true, param,page3,riskCategory,true);
			} else if ("5".equals(reporttype)) {
				result = ExportUtil
						.getAnalyseResult(OutsideAttackChangeRate.class, outsideAttackChangeRateService,
								"getOutsideAttackChangeRates", categorys, "percent", true, param,page3,riskCategory,true);
			} else if ("6001".equals(reporttype)) {
				result = ExportUtil.getAnalyseResult(InfoTechnologyRiskEventCount.class,
						infoTechnologyRiskEventCountService, "getInfoTechnologyRiskEventCounts", categorys, "count",
						false, param,page3,riskCategory,true);
			} else if ("7".equals(reporttype)) {
				result = ExportUtil.getAnalyseResult(ElectronicTransactionChangeRate.class,
						electronicTransactionChangeRateService, "getElectronicTransactionChangeRates", categorys,
						"percent", true, param,page3,riskCategory,true);
			} else if ("8".equals(reporttype)) {
				result = ExportUtil.getAnalyseResult(ElectronicActiveUserChangeRate.class,
						electronicActiveUserChangeRateService, "getElectronicActiveUserChangeRates", categorys,
						"percent", true, param,page3,riskCategory,true);
			}
		page3.setResult(result);

		model.addAttribute("rates", page3);
		Subject user = SecurityUtils.getSubject();
		String username=(String)user.getPrincipal();  
		User oldu = userService.getUserByUserName(username);
				oldu.setOnline(false);
		List<Organization> orgs=organizationService.getBelongBanksByUser(oldu,true);
		model.addAttribute("orgs", orgs);
		model.addAttribute("pageNo", pageNo);	
		model.addAttribute("risktargets", getAllRiskCategory()); 
		String risktarget = request.getParameter("risktarget");
		if(StringUtils.isEmpty(risktarget))
			risktarget="";
		model.addAttribute("target", risktarget);	
		List<RiskCategory> newList=ExportUtil.dealwithSystemAvailabelCategorys(categorys);
		List<Integer> periods = ExportUtil.getPeriods(param, riskCategoryService);
		List<Date> periodDates = ExportUtil.getPeriodDates(param, riskCategoryService);
		String periodStr = periods.get(0) + "期至第" + periods.get(periods.size() - 1);
		if (periods.size() == 1)
			periodStr = periods.get(0) + "";
		long orgId=param.getDepartmentId();
	    Organization org=organizationService.getOrganization(orgId);
		String fileName = org.getName()+"第" + periodStr + "期"+riskCategory.getRiskName()+"风险分析表";
		ExportUtil.exportExcel(fileName, periods, periodDates,newList, result, response);
		return null;
	}
	
	
	
	@RequestMapping(value = "/export3.do")
	public String export3(
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, HttpServletRequest request,HttpServletResponse response) {	
		int period = StringUtils.isEmpty(request.getParameter("period"))?3:Integer.parseInt(request.getParameter("period"));
		String area =request.getParameter("area");
		String category =request.getParameter("category");
		String reportorder =request.getParameter("reportorder");
		if(StringUtils.isEmpty(category)){
			category="";
		} 
		List<Organization> orgs=new ArrayList<Organization>();
		String hql="";
		if (StringUtils.isNotEmpty(category)) {
			String[] departlist = category.split(",");
			List<Long> llist = new ArrayList<Long>();
			for (String str : departlist) {
				llist.add(Long.parseLong(str));
				orgs.add(organizationService.getOrganization(Long.parseLong(str)));
			}
			hql += " and a.organization.orgId in (" + ConvertUtils.convertList2String(llist, ",") + ")";
		} else if (StringUtils.isNotEmpty(area)) {
			hql += " and a.areaCode like '" + area + "%'";
			Subject user = SecurityUtils.getSubject();
			String username = (String) user.getPrincipal();
			User oldu = userService.getUserByUserName(username);
			oldu.setOnline(false);
			orgs = organizationService.getBelongBanksByUser(oldu, true);
		}
		hql+=" and a.organization.canControl='1'";
		
		List<SystemAvailableRate> list1 = systemAvailableRateService.findLastPeriod(SystemAvailableRate.class, period,hql);
		List<SystemTransactionSuccessRate> list2 = systemAvailableRateService.findLastPeriod(
				SystemTransactionSuccessRate.class, period,hql);
		List<OperationChangeSuccessRate> list3 = systemAvailableRateService.findLastPeriod(
				OperationChangeSuccessRate.class, period,hql);
		List<FakeSiteAttachmentRate> list4 = systemAvailableRateService.findLastPeriod(FakeSiteAttachmentRate.class,
				period,hql);
		List<OutsideAttackChangeRate> list5 = systemAvailableRateService.findLastPeriod(OutsideAttackChangeRate.class,
				period,hql);
		List<InfoTechnologyRiskEventCount> list6 = systemAvailableRateService.findLastPeriod(
				InfoTechnologyRiskEventCount.class, period,hql);		
		List<ElectronicTransactionChangeRate> list7 = systemAvailableRateService.findLastPeriod(
				ElectronicTransactionChangeRate.class, period,hql);
		List<ElectronicActiveUserChangeRate> list8 = systemAvailableRateService.findLastPeriod(
				ElectronicActiveUserChangeRate.class, period,hql);		
		
		
		List<List<MonitorRate>> rates = new ArrayList<List<MonitorRate>>();
		rates.add(ExportUtil.getMonitorRates_Available(list1));
		rates.add(ExportUtil.getMonitorRates(list2, "percent", true));
		rates.add(ExportUtil.getMonitorRates(list3, "percent", true));
		rates.add(ExportUtil.getMonitorRates(list4, "percent", true));
		rates.add(ExportUtil.getMonitorRates(list5, "percent", true));
		rates.add(ExportUtil.getMonitorRates(list6, "count", false));
		rates.add(ExportUtil.getMonitorRates(list7, "percent", true));
		rates.add(ExportUtil.getMonitorRates(list8, "percent", true));		
		
		List<RiskCategory> categorys = riskCategoryService.getChildRiskCategoryByTopCode("");
		List<RiskCategory> newList=ExportUtil.dealwithSystemAvailabelCategorys(categorys);
		String fileName = "机构最近" + period + "期全部指标监测";
		if (reportorder.equals("1"))
			ExportUtil.exportExce3(fileName, period, orgs, rates, newList, response);
		else
			ExportUtil.exportExce3_org(fileName, period, orgs, rates, newList, response);
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/export4.do")
	public String export4(
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, HttpServletRequest request,HttpServletResponse response){
		List<RiskTca> tcas=new ArrayList<RiskTca>();
		Date startDate=null;
		Date endDate=null;
		String startdatestr =request.getParameter("startdate");
		if(StringUtils.isNotEmpty(startdatestr)){
			startDate=(Date)ConvertUtils.convertStringToObject(startdatestr, Date.class);
		}else{
			startdatestr="";
		}
		String endtdatestr =request.getParameter("endtdate");
		if(StringUtils.isNotEmpty(endtdatestr)){
			endDate=(Date)ConvertUtils.convertStringToObject(endtdatestr, Date.class);
		}else{
			endtdatestr="";
		}
		model.addAttribute("startdate", startdatestr);
		model.addAttribute("enddate", endtdatestr);
		
		String dateSql="a.organization.canControl='1'";
		if (startDate != null) {
			dateSql+=" and a.reportDate>='"+DateUtil.getShortDate(startDate)+"'";
		}
		if (endDate != null) {
			dateSql+=" and a.reportDate<='"+DateUtil.getShortDate(endDate)+"'";
		}
		List<SystemAvailableRate> avas=systemAvailableRateService.find(SystemAvailableRate.class,"select distinct a from SystemAvailableRate a,RiskCategory rc where "+dateSql+" and rc.riskCode=a.riskCode and rc.useThresholdValue='1' and ((1-(a.ud+a.pd+0.0)/a.ltsp)*100>rc.maxValue or (1-(a.ud+a.pd+0.0)/a.ltsp)*100<rc.minValue) order by a.reportDate desc");
		for(SystemAvailableRate rate:avas){
			RiskTca tca=getRiskTca(rate);
			tca.setCurrentValue(ConvertUtils.get2pointDouble(rate.getPercentActual()) + "%");
			tcas.add(tca);
		}
		List<SystemTransactionSuccessRate> trans=systemAvailableRateService.find(SystemTransactionSuccessRate.class,"select distinct a from SystemTransactionSuccessRate a,RiskCategory rc where "+dateSql+" and rc.riskCode=a.riskCode and rc.useThresholdValue='1' and ((a.aosst+0.0)/a.aost*100>rc.maxValue or (a.aosst+0.0)/a.aost*100<rc.minValue) order by a.reportDate desc");
		for(SystemTransactionSuccessRate rate:trans){
			RiskTca tca=getRiskTca(rate);
			tca.setCurrentValue(ConvertUtils.get2pointDouble(rate.getPercent()) + "%");
			tcas.add(tca);
		}
		List<OperationChangeSuccessRate> opers=systemAvailableRateService.find(OperationChangeSuccessRate.class,"select distinct a from OperationChangeSuccessRate a,RiskCategory rc where "+dateSql+" and rc.riskCode=a.riskCode and rc.useThresholdValue='1' and ((a.sdc+0.0)/a.dc*100>rc.maxValue or (a.sdc+0.0)/a.dc*100<rc.minValue) order by a.reportDate desc");
		for(OperationChangeSuccessRate rate:opers){
			RiskTca tca=getRiskTca(rate);
			tca.setCurrentValue(ConvertUtils.get2pointDouble(rate.getPercent()) + "%");
			tcas.add(tca);
		}
		List<FakeSiteAttachmentRate> fakes=systemAvailableRateService.find(FakeSiteAttachmentRate.class,"select distinct a from FakeSiteAttachmentRate a,RiskCategory rc where "+dateSql+" and rc.riskCode=a.riskCode and rc.useThresholdValue='1' and ((a.cfw+0.0)/a.aofw*100>rc.maxValue or (a.cfw+0.0)/a.aofw*100<rc.minValue) order by a.reportDate desc");
		for(FakeSiteAttachmentRate rate:fakes){
			RiskTca tca=getRiskTca(rate);
			tca.setCurrentValue(ConvertUtils.get2pointDouble(rate.getPercent()) + "%");
			tcas.add(tca);
		}
		List<InfoTechnologyRiskEventCount> infos=systemAvailableRateService.find(InfoTechnologyRiskEventCount.class,"select distinct a from InfoTechnologyRiskEventCount a,RiskCategory rc where "+dateSql+" and rc.riskCode=a.riskCode and rc.useThresholdValue='1' and (a.count>rc.maxValue or a.count<rc.minValue) order by a.reportDate desc");
		for(InfoTechnologyRiskEventCount rate:infos){
			RiskTca tca=getRiskTca(rate);
			tca.setCurrentValue(rate.getCount()+"");
			tcas.add(tca);
		}
		List<OutsideAttackChangeRate> outs=systemAvailableRateService.find(OutsideAttackChangeRate.class,"select distinct a from OutsideAttackChangeRate a,RiskCategory rc where "+dateSql+" and rc.riskCode=a.riskCode and rc.useThresholdValue='1' and (((a.idswp+a.ipswp+0.0)/(a.ldswp+a.lpswp)-1)*100>rc.maxValue or ((a.idswp+a.ipswp+0.0)/(a.ldswp+a.lpswp)-1)*100<rc.minValue) order by a.reportDate desc");
		for(OutsideAttackChangeRate rate:outs){
			RiskTca tca=getRiskTca(rate);
			tca.setCurrentValue(ConvertUtils.get2pointDouble(rate.getPercent()) + "%");
			tcas.add(tca);
		}
		List<ElectronicActiveUserChangeRate> users=systemAvailableRateService.find(ElectronicActiveUserChangeRate.class,"select distinct a from ElectronicActiveUserChangeRate a,RiskCategory rc where "+dateSql+" and rc.riskCode=a.riskCode and rc.useThresholdValue='1' and (((a.currentActiveUser+0.0)/a.lastActiveUser-1)*100>rc.maxValue or ((a.currentActiveUser+0.0)/a.lastActiveUser-1)*100<rc.minValue) order by a.reportDate desc");
		for(ElectronicActiveUserChangeRate rate:users){
			RiskTca tca=getRiskTca(rate);
			tca.setCurrentValue(ConvertUtils.get2pointDouble(rate.getPercent()) + "%");
			tcas.add(tca);
		}
		List<ElectronicTransactionChangeRate> eletrans=systemAvailableRateService.find(ElectronicTransactionChangeRate.class,"select distinct a from ElectronicTransactionChangeRate a,RiskCategory rc where "+dateSql+" and rc.riskCode=a.riskCode and rc.useThresholdValue='1' and (((a.currentTradingVolume+0.0)/a.lastTradingVolume-1)*100>rc.maxValue or ((a.currentTradingVolume+0.0)/a.lastTradingVolume-1)*100<rc.minValue) order by a.reportDate desc");
		for(ElectronicTransactionChangeRate rate:eletrans){
			RiskTca tca=getRiskTca(rate);
			tca.setCurrentValue(ConvertUtils.get2pointDouble(rate.getPercent()) + "%");
			tcas.add(tca);
		}
		String fileName = "机构超阈值风险分析表";
		ExportUtil.exportExcel_tca(fileName, tcas, response);
		return null;
	}
	
	//@RequestMapping("/testreport.do")
	public String testreport(HttpServletRequest request,HttpServletResponse response){		
		String[] orgs=new String[]{"BJBK","BHBK","BJNS"};
		String[] keys=new String[]{"01071193387580474171410454334001","01071193387580474171410454123875","01071193387580474171410454334002"};
		List<Integer> list=ConvertUtils.newArrayList(0,1,2,3,4,5,6,7);
		for(int j:list){
			for(int i=0;i<3;i++){			
				String type=typeList.get(j);
				for(int k=0;k<365;k++){
					Date date = DateUtils.addDays(new Date(2013-1900,0,1),k);
					long time=System.currentTimeMillis();
					send(map.get(type), date, type,request.getServerPort(),orgs[i],keys[i]);
					System.out.println((System.currentTimeMillis()-time)+" report type="+type+" date="+DateUtil.getShortDate(date));
				}
			}
		}
		return null;
	}
	
	private void send(List<String> codes, Date date, String type,int serverPort,String orgNo,String orgKey) {
		String url = "http://localhost:"+serverPort+"/bankriskcontrol/rest/risk";
		long time = 1410538279421L;
		String totalKey = orgNo + ":" + orgKey + ":" + time;
		JSONArray array = new JSONArray();
		for (String code : codes) {
			JSONObject object = new JSONObject();
			object.put("reportDate", DateUtil.getShortDate(date));
			object.put("riskCode", code);
			for (String propertyName : propertyMap.get(type)) {
				object.put(propertyName, RandomUtils.nextInt(100));
				//保证总数和成功数的大小关系
				if(propertyName.equals("tst"))
					object.put(propertyName, 7*24*60);
				if(propertyName.equals("sost"))
					object.put(propertyName, RandomUtils.nextInt(100));
				if(propertyName.equals("uost"))
					object.put(propertyName, RandomUtils.nextInt(50));
				if(propertyName.equals("tnt"))
					object.put(propertyName, 100000+RandomUtils.nextInt(10000));
				if(propertyName.equals("nst"))
					object.put(propertyName, 90000+RandomUtils.nextInt(10000));
				if(propertyName.equals("nsdc"))
					object.put(propertyName, 100+RandomUtils.nextInt(100));
				if(propertyName.equals("nfw"))
					object.put(propertyName, 100+RandomUtils.nextInt(100));
				if(propertyName.equals("ntcp"))
					object.put(propertyName, 100000+RandomUtils.nextInt(10000));
				if(propertyName.equals("naucp"))
					object.put(propertyName, 10000+RandomUtils.nextInt(1000));
			}
			array.add(object);
		}

		HttpPost httpPost = new HttpPost(url + "/" + type);
		JSONObject object = new JSONObject();
		object.put("organization", orgNo);
		object.put("key", EncodeUtils.ecodeByMD5(totalKey));
		object.put("time", time);
		object.put("items", array);

		StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpClient client = new DefaultHttpClient();
		// 发送请求并解析返回结果
		try {
			client.execute(httpPost);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private RiskTca getRiskTca(Object object) {
		RiskTca tca = new RiskTca();
		try {			
			PropertyUtils.setProperty(tca, "period", PropertyUtils.getProperty(object, "period"));
			PropertyUtils.setProperty(tca, "reportDate", PropertyUtils.getProperty(object, "reportDate"));
			PropertyUtils.setProperty(tca, "riskCategory", PropertyUtils.getProperty(object, "riskCategory"));
			PropertyUtils.setProperty(tca, "org", PropertyUtils.getProperty(object, "organization"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tca;
	}
	
	private RiskCategory getTopRiskCategory(List<RiskCategory> categorys,Object object){
		try {
			String code = (String)PropertyUtils.getProperty(object, "riskCode");
			for(RiskCategory rc:categorys){
				if(code.startsWith(rc.getRiskCode()))
					return rc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 		
		return null;
	}
}
