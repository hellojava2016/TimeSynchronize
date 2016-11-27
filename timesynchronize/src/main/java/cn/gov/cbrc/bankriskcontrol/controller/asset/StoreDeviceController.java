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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.gov.cbrc.bankriskcontrol.dto.AssetQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.AppSystem;
import cn.gov.cbrc.bankriskcontrol.entity.Organization;
import cn.gov.cbrc.bankriskcontrol.entity.StorageSystemInfo;
import cn.gov.cbrc.bankriskcontrol.entity.User;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.asset.AppSystemService;
import cn.gov.cbrc.bankriskcontrol.service.asset.StorageSystemInfoService;
import cn.gov.cbrc.bankriskcontrol.service.system.OrganizationService;
import cn.gov.cbrc.bankriskcontrol.util.ConvertUtils;
import cn.gov.cbrc.bankriskcontrol.util.DateUtil;
import cn.gov.cbrc.bankriskcontrol.util.ExportUtil;
import cn.gov.cbrc.bankriskcontrol.util.Page;
import cn.gov.cbrc.bankriskcontrol.util.RiskUtils;

@Controller
@RequestMapping(value = "/asset/store")
public class StoreDeviceController {
	@Autowired
    private StorageSystemInfoService storageSystemInfoService;
   
    @Autowired
	private OrganizationService organizationService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AppSystemService appSystemService;
    
    @RequestMapping(value = "/add_pre.do")
	public ModelAndView add_pre(HttpServletRequest request,
			HttpServletResponse response,Model model) {
    	model.addAttribute("apps", RiskUtils.getJsonApps(appSystemService,null));
    	model.addAttribute("id", 0);
		return new ModelAndView("/asset/store-add", null);
	}

	@RequestMapping(value = "/add.do")
	public ModelAndView add(HttpServletRequest request,
			HttpServletResponse response) {
		try{
			StorageSystemInfo store = new StorageSystemInfo();
	        getValue(request, store);
	        storageSystemInfoService.addStorageSystemInfo(store); 
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
			HttpServletResponse response,Model model) {
		long id=Long.parseLong(request.getParameter("id"));
		StorageSystemInfo store=storageSystemInfoService.getStorageSystemInfoById(id);		
    	model.addAttribute("apps", RiskUtils.getJsonApps(appSystemService,store.getAppSystems()));
    	model.addAttribute("store", store);
    	model.addAttribute("id", id);
		return new ModelAndView("/asset/store-add", null);
	}

	@RequestMapping(value = "/update.do")
	public ModelAndView update(HttpServletRequest request,
			HttpServletResponse response) {
		try{
			long id=Long.parseLong(request.getParameter("id"));
			StorageSystemInfo store=storageSystemInfoService.getStorageSystemInfoById(id);
			getValue(request, store);		
	        storageSystemInfoService.updateStorageSystemInfo(store);
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
		Page<StorageSystemInfo> page = new Page<StorageSystemInfo>();
		page.setPageNo(pageNo);
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotEmpty(pageSize))
			  page.setPageSize(Integer.parseInt(pageSize));
		Page<StorageSystemInfo> list=storageSystemInfoService.getStorageSystemInfos(param, page);
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
				   model.addAttribute("message","添加存储设备成功");
			else if("updatesucess".equals(message))
				   model.addAttribute("message","修改存储设备成功");
			else if("deletesucess".equals(message))
				   model.addAttribute("message","删除存储设备成功");
			else if("adderror".equals(message))
				   model.addAttribute("message","修改存储设备失败,请重新添加");
			else if("deleteerror".equals(message))
				   model.addAttribute("message","删除存储设备失败,请重新删除");
			else if("updateerror".equals(message))
				   model.addAttribute("message","修改存储设备失败,请检查数据是否合法");
		}	
		return "/asset/store-list";
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
		Page<StorageSystemInfo> page = new Page<StorageSystemInfo>();
		page.setPageNo(pageNo);
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotEmpty(pageSize))
			  page.setPageSize(Integer.parseInt(pageSize));
		Page<StorageSystemInfo> list=storageSystemInfoService.getStorageSystemInfos(param, page);
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
				   model.addAttribute("message","添加存储设备成功");
			else if("updatesucess".equals(message))
				   model.addAttribute("message","修改存储设备成功");
			else if("deletesucess".equals(message))
				   model.addAttribute("message","删除存储设备成功");
			else if("adderror".equals(message))
				   model.addAttribute("message","修改存储设备失败,请重新添加");
			else if("deleteerror".equals(message))
				   model.addAttribute("message","删除存储设备失败,请重新删除");
			else if("updateerror".equals(message))
				   model.addAttribute("message","修改存储设备失败,请检查数据是否合法");
		}	
		return "/asset/store-list";
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
		Page<StorageSystemInfo> page = new Page<StorageSystemInfo>();
		page.setPageSize(Integer.MAX_VALUE);
		page.setPageNo(pageNo);
		Page<StorageSystemInfo> list=storageSystemInfoService.getStorageSystemInfos(param, page);
		List<String[]> valueList=new ArrayList<String[]>();
		valueList.add(new String[]{"所属机构","编号","设备名称","设备类型","序列号","版本","生产厂商","用途","位置","容量信息","存储配置容量","存储CACHE容量","存储RAID方式","磁盘规格","存储微码版本","存储带介质类型","存储带介质数量","开始服务时间","应用系统"});
		for(StorageSystemInfo ss:list.getResult()){
			String orgName=ss.getOrganization().getName();
			String servertime = DateUtil.getShortDate(ss.getServiceTime());
			valueList.add(new String[]{orgName ,ss.getUniqueVal(), ss.getName(),ss.getType(),ss.getSerialNumber(),ss.getVersion(),ss.getManufacturer(),ss.getPurpose(),ss.getLocation(),ss.getCapacityInfo(),ss.getStorageArraySize()+"",ss.getStorageCacheSize()+"",ss.getStorageRaidMode(),ss.getDiskSpec(),ss.getMicrocodeVersion(),ss.getStorageTapeMediaType(),ss.getStorageTapeMediaCount()+"",servertime,ss.getAppSystem()});
		}
		ExportUtil.exportExcel(valueList, "存储设备列表", response);
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
				storageSystemInfoService.deleteStorageSystemInfoById(id);
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
		StorageSystemInfo info =storageSystemInfoService.getStorageSystemInfoByUniqueVal(uniqueVal);
	      if(null== info)
	    	  return true;
		return false;
	}
	
	private void getValue(HttpServletRequest request,StorageSystemInfo store){
		String uniqueVal=request.getParameter("uniqueVal");
        String serialNumber=request.getParameter("serialNumber");
        String name=request.getParameter("name");
        String type=request.getParameter("type");
        String manufacturer=request.getParameter("manufacturer");
        String location=request.getParameter("location");
        String capacityInfo=request.getParameter("capacityInfo");
        String storageArraySize=request.getParameter("storageArraySize");
        String storageRaidMode=request.getParameter("storageRaidMode");
        String storageCacheSize=request.getParameter("storageCacheSize");
        String diskSpec=request.getParameter("diskSpec");
        String microcodeVersion=request.getParameter("microcodeVersion");
        String storageTapeMediaType=request.getParameter("storageTapeMediaType");
        String storageTapeMediaCount=request.getParameter("storageTapeMediaCount");
        String purpose=request.getParameter("purpose");
        String version=request.getParameter("version");
        String severTime=request.getParameter("serviceTime");
        String apps = request.getParameter("apps");
        
        store.setUniqueVal(uniqueVal);
        store.setSerialNumber(serialNumber);
        store.setDiskSpec(diskSpec);
        store.setStorageArraySize(Integer.parseInt(storageArraySize));
        store.setStorageCacheSize(Integer.parseInt(storageCacheSize));
        store.setStorageRaidMode(storageRaidMode);
        store.setCapacityInfo(capacityInfo);
        store.setMicrocodeVersion(microcodeVersion);
        store.setStorageTapeMediaCount(Integer.parseInt(storageTapeMediaCount));
        store.setStorageTapeMediaType(storageTapeMediaType);
        store.setName(name);
        store.setLocation(location);
        store.setPurpose(purpose);
        store.setVersion(version);
        store.setManufacturer(manufacturer);
        store.setType(type);
        store.setServiceTime((Date) ConvertUtils.convertStringToObject(severTime,Date.class));     
        store.setRecordTime(new Date());
        store.setAppSystem(apps);
        Set<AppSystem> set = new HashSet<AppSystem>();
        if(StringUtils.isNotEmpty(apps)){
        	 for(String app :apps.split(",")){
        		 set.add(appSystemService.getAppSystemByName(app));
        	 }
        }
        store.setAppSystems(set);
        User user=(User)request.getSession().getAttribute("user");
        store.setOrganization(user.getOrganization());
	}
}
