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
import cn.gov.cbrc.bankriskcontrol.entity.NetworkEquipmentInfo;
import cn.gov.cbrc.bankriskcontrol.entity.Organization;
import cn.gov.cbrc.bankriskcontrol.entity.User;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.asset.AppSystemService;
import cn.gov.cbrc.bankriskcontrol.service.asset.NetworkEquipmentInfoService;
import cn.gov.cbrc.bankriskcontrol.service.system.OrganizationService;
import cn.gov.cbrc.bankriskcontrol.util.ExportUtil;
import cn.gov.cbrc.bankriskcontrol.util.Page;
import cn.gov.cbrc.bankriskcontrol.util.RiskUtils;
import cn.gov.cbrc.bankriskcontrol.util.ServletUtils;

@Controller
@RequestMapping(value = "/asset/networkequipmentinfo")
public class NetworkEquipmentInfoController {

	@Autowired
    private AppSystemService appSystemService;
	
	@Autowired
	private NetworkEquipmentInfoService networkEquipmentInfoService;

	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
    private UserService userService;
	
	@RequestMapping(value = "/list.do")
	public String getlist(
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, HttpServletRequest request) {
		AssetQueryParam param=new AssetQueryParam();
		Page<NetworkEquipmentInfo> page = new Page<NetworkEquipmentInfo>();
		page.setPageNo(pageNo);
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotEmpty(pageSize))
			  page.setPageSize(Integer.parseInt(pageSize));
		Page<NetworkEquipmentInfo> list=networkEquipmentInfoService.getNetworkEquipmentInfos(param, page);
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
				   model.addAttribute("message","添加网络设备成功");
			else if("updatesucess".equals(message))
				   model.addAttribute("message","修改网络设备成功");
			else if("deletesucess".equals(message))
				   model.addAttribute("message","删除网络设备成功");
			else if("adderror".equals(message))
				   model.addAttribute("message","修改网络设备失败,请重新添加");
			else if("deleteerror".equals(message))
				   model.addAttribute("message","删除网络设备失败,请重新删除");
			else if("updateerror".equals(message))
				   model.addAttribute("message","修改网络设备失败,请检查数据是否合法");
		}	
		return "/asset/networkequipmentinfo-list";
	}
	
	@RequestMapping(value = "/add_pre.do")
	public ModelAndView add_pre(HttpServletRequest request,
			HttpServletResponse response,Model model) {
		model.addAttribute("apps", RiskUtils.getJsonApps(appSystemService,null));
		model.addAttribute("id", 0);
		return new ModelAndView("/asset/networkequipmentinfo-add", null);
	}
	
	@RequestMapping(value = "/add.do")
	public ModelAndView add(HttpServletRequest request,
			HttpServletResponse response) {
		try{
			 NetworkEquipmentInfo info=new NetworkEquipmentInfo();
			 getValue(request, info);
	        networkEquipmentInfoService.addNetworkEquipmentInfo(info);
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
		NetworkEquipmentInfo ne=networkEquipmentInfoService.getNetworkEquipmentInfoById(id);
		
		mode.addAttribute("apps", RiskUtils.getJsonApps(appSystemService,ne.getAppSystems()));
		mode.addAttribute("network",ne);
		mode.addAttribute("id",id);
		return new ModelAndView("/asset/networkequipmentinfo-add", null);
	}

	@RequestMapping(value = "/update.do")
	public ModelAndView update(HttpServletRequest request,
			HttpServletResponse response) {
		try{
			long id=Long.parseLong(request.getParameter("id"));
			NetworkEquipmentInfo ne=networkEquipmentInfoService.getNetworkEquipmentInfoById(id);
	        getValue(request, ne);
	        networkEquipmentInfoService.updateNetworkEquipmentInfo(ne);
		}catch(Exception ex){
			String listurl=(String)request.getSession().getAttribute("listurl");
			String queryparam=(String)request.getSession().getAttribute("queryparam");
			return new ModelAndView("redirect:"+listurl+"?message=updateerror&"+queryparam);
		}
		String listurl=(String)request.getSession().getAttribute("listurl");
		String queryparam=(String)request.getSession().getAttribute("queryparam");
		return new ModelAndView("redirect:"+listurl+"?message=updatesucess&"+queryparam);
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
		Page<NetworkEquipmentInfo> page = new Page<NetworkEquipmentInfo>();
		page.setPageNo(pageNo);
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotEmpty(pageSize))
			  page.setPageSize(Integer.parseInt(pageSize));
		Page<NetworkEquipmentInfo> list=networkEquipmentInfoService.getNetworkEquipmentInfos(param, page);
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
				   model.addAttribute("message","添加网络设备成功");
			else if("updatesucess".equals(message))
				   model.addAttribute("message","修改网络设备成功");
			else if("deletesucess".equals(message))
				   model.addAttribute("message","删除网络设备成功");
			else if("adderror".equals(message))
				   model.addAttribute("message","修改网络设备失败,请重新添加");
			else if("deleteerror".equals(message))
				   model.addAttribute("message","删除网络设备失败,请重新删除");
			else if("updateerror".equals(message))
				   model.addAttribute("message","修改网络设备失败,请检查数据是否合法");
		}	
		return "/asset/networkequipmentinfo-list";
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
		Page<NetworkEquipmentInfo> page = new Page<NetworkEquipmentInfo>();
		page.setPageSize(Integer.MAX_VALUE);
		page.setPageNo(pageNo);
		Page<NetworkEquipmentInfo> list=networkEquipmentInfoService.getNetworkEquipmentInfos(param, page);
		List<String[]> valueList=new ArrayList<String[]>();
		valueList.add(new String[]{"所属机构","编号","名称","类型","生产厂商","序列号","设备版本","用途","位置","端口数量","应用系统"});
		for(NetworkEquipmentInfo ne:list.getResult()){
			String orgName=ne.getOrganization().getName();
			valueList.add(new String[]{orgName ,ne.getUniqueVal(), ne.getName(),ne.getType(),ne.getManufacturer(),ne.getSerialNumber(),ne.getVersion(),ne.getPurpose(),ne.getLocation(),ne.getPortCount()+"",ne.getAppSystem()});
		}
		ExportUtil.exportExcel(valueList, "网络设备列表", response);
        return null;
	}


	@RequestMapping(value = "/delete.do")
	public ModelAndView delete(HttpServletRequest request,
			HttpServletResponse response) {
		try{
			String deleteids = request.getParameter("deleteids");
			String[] ids = deleteids.split(",");
			for (String temp : ids) {
				long id = Long.parseLong(temp);
				networkEquipmentInfoService.deleteNetworkEquipmentInfoById(id);
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
		NetworkEquipmentInfo db=networkEquipmentInfoService.getNetworkEquipmentInfoByUniqueVal(uniqueVal);
	      if(null== db)
	    	  return true;
		return false;
	}

	private void getValue(HttpServletRequest request, NetworkEquipmentInfo info) {
		String uniqueVal = request.getParameter("uniqueVal");
		String manufacturer = request.getParameter("manufacturer");
		String type = request.getParameter("type");
		String serialNumber = request.getParameter("serialNumber");
		String name = request.getParameter("name");
		String version = request.getParameter("version");
		String purpose = request.getParameter("purpose");
		String location = request.getParameter("location");
		int portCount = ServletUtils.getRequestParamValue_int(request, "portCount");
		User user = (User) request.getSession().getAttribute("user");

		info.setUniqueVal(uniqueVal);
		info.setManufacturer(manufacturer);
		info.setType(type);
		info.setSerialNumber(serialNumber);
		info.setName(name);
		info.setVersion(version);
		info.setPurpose(purpose);
		info.setLocation(location);
		info.setPortCount(portCount);
		info.setRecordTime(new Date());
		String apps = request.getParameter("apps");
		info.setAppSystem(apps);
		Set<AppSystem> set = new HashSet<AppSystem>();
		if (StringUtils.isNotEmpty(apps)) {
			for (String app : apps.split(",")) {
				set.add(appSystemService.getAppSystemByName(app));
			}
		}
		info.setAppSystems(set);
		info.setOrganization(user.getOrganization());
	}
}
