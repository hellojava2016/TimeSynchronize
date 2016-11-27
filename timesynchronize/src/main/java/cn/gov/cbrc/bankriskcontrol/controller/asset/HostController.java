package cn.gov.cbrc.bankriskcontrol.controller.asset;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import cn.gov.cbrc.bankriskcontrol.entity.Organization;
import cn.gov.cbrc.bankriskcontrol.entity.PcServer;
import cn.gov.cbrc.bankriskcontrol.entity.User;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.asset.PcServerService;
import cn.gov.cbrc.bankriskcontrol.service.system.OrganizationService;
import cn.gov.cbrc.bankriskcontrol.util.ConvertUtils;
import cn.gov.cbrc.bankriskcontrol.util.DateUtil;
import cn.gov.cbrc.bankriskcontrol.util.ExportUtil;
import cn.gov.cbrc.bankriskcontrol.util.Page;

@Controller
@RequestMapping(value = "/asset/host")
public class HostController {
	@Autowired
    private PcServerService pcServerService;
   
    @Autowired
	private OrganizationService organizationService;
    
    @Autowired
    private UserService userService;
    
    @RequestMapping(value = "/add_pre.do")
	public ModelAndView add_pre(HttpServletRequest request,
			HttpServletResponse response,ModelMap mode) {
    	mode.addAttribute("id", 0);
		return new ModelAndView("/asset/host-add", mode);
	}

	@RequestMapping(value = "/add.do")
	public ModelAndView add(HttpServletRequest request,
			HttpServletResponse response) {
		try{
			PcServer host = new PcServer();
	        getValue(request, host);
	        pcServerService.addPcServer(host);
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
		long hostId=Long.parseLong(request.getParameter("id"));
		PcServer ps=pcServerService.getPcServerById(hostId);
		mode.addAttribute("host", ps);
		mode.addAttribute("id", hostId);
		return new ModelAndView("/asset/host-add", mode);
	}

	@RequestMapping(value = "/update.do")
	public ModelAndView update(HttpServletRequest request,
			HttpServletResponse response) {
		try{
			long hostId=Long.parseLong(request.getParameter("id"));
			PcServer host = pcServerService.getPcServerById(hostId);
	        getValue(request, host);
	        pcServerService.updatePcServer(host);
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
		Page<PcServer> page = new Page<PcServer>();
		page.setPageNo(pageNo);
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotEmpty(pageSize))
			  page.setPageSize(Integer.parseInt(pageSize));
		Page<PcServer> list=pcServerService.getPcServrs(param, page);
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
				   model.addAttribute("message","添加主机成功");
			else if("updatesucess".equals(message))
				   model.addAttribute("message","修改主机成功");
			else if("deletesucess".equals(message))
				   model.addAttribute("message","删除主机成功");
			else if("adderror".equals(message))
				   model.addAttribute("message","修改主机失败,请重新添加");
			else if("deleteerror".equals(message))
				   model.addAttribute("message","删除主机失败,请重新删除");
			else if("updateerror".equals(message))
				   model.addAttribute("message","修改主机失败,请检查数据是否合法");
		}	
		return "/asset/host-list";
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
		Page<PcServer> page = new Page<PcServer>();
		page.setPageNo(pageNo);
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotEmpty(pageSize))
			  page.setPageSize(Integer.parseInt(pageSize));
		Page<PcServer> list=pcServerService.getPcServrs(param, page);
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
				   model.addAttribute("message","添加主机成功");
			else if("updatesucess".equals(message))
				   model.addAttribute("message","修改主机成功");
			else if("deletesucess".equals(message))
				   model.addAttribute("message","删除主机成功");
			else if("adderror".equals(message))
				   model.addAttribute("message","修改主机失败,请重新添加");
			else if("deleteerror".equals(message))
				   model.addAttribute("message","删除主机失败,请重新删除");
			else if("updateerror".equals(message))
				   model.addAttribute("message","修改主机失败,请检查数据是否合法");
		}	
		return "/asset/host-list";
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
		Page<PcServer> page = new Page<PcServer>();
		page.setPageSize(Integer.MAX_VALUE);
		page.setPageNo(pageNo);
		Page<PcServer> list=pcServerService.getPcServrs(param, page);	
		List<String[]> valueList=new ArrayList<String[]>();
		valueList.add(new String[]{"所属机构","编号","主机名称","主机分类","主机设备型号","序列号","IP","CPU型号","CPU数量","内存大小(G)","硬盘大小(G)","主机用途","开始服务时间","主机位置","生产产商"});
		for(PcServer ps:list.getResult()){
			String orgName=ps.getOrganization().getName();
			valueList.add(new String[]{orgName,ps.getUniqueVal(),ps.getName(),ps.getCategoryString(),ps.getType(),ps.getSerialNumber(),ps.getIp(),ps.getCpu(),ps.getCpuCount()+"",ps.getMemorySize()+"",ps.getHardDiskSize()+"",ps.getPurpose(),DateUtil.getShortDate(ps.getServiceTime()),ps.getLocation(),ps.getManufacturer()});
		}
		ExportUtil.exportExcel(valueList, "主机列表", response);
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
				pcServerService.deletePcServerById(id);
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
		PcServer host=pcServerService.getPcServerByUniqueVal(uniqueVal);
	      if(null== host)
	    	  return true;
		return false;
	}
	
	private void getValue(HttpServletRequest request,PcServer host){
		String uniqueVal=request.getParameter("uniqueVal");
        String serialNumber=request.getParameter("serialNumber");
        String name=request.getParameter("name");
        String type=request.getParameter("type");
        String manufacturer=request.getParameter("manufacturer");
        String location=request.getParameter("location");
        String cpu=request.getParameter("cpu");
        String cpuCount=request.getParameter("cpuCount");
        String hardDiskSize=request.getParameter("hardDiskSize");
        String ip=request.getParameter("ip");
        String memorySize=request.getParameter("memorySize");
        String purpose=request.getParameter("purpose");
        String serviceTime=request.getParameter("serviceTime");
        String category=request.getParameter("category");
        host.setCpuCount(Integer.parseInt(cpuCount));
        host.setCpu(cpu);
        host.setUniqueVal(uniqueVal);
        host.setIp(ip);
        host.setSerialNumber(serialNumber);
        host.setName(name);
        host.setLocation(location);
        host.setPurpose(purpose);
        host.setManufacturer(manufacturer);
        host.setMemorySize(Integer.parseInt(memorySize));
        host.setHardDiskSize(Integer.parseInt(hardDiskSize));
        host.setType(type);
        host.setCategory(Integer.parseInt(category));
        host.setServiceTime((Date) ConvertUtils.convertStringToObject(serviceTime,Date.class));
        host.setRecordTime(new Date());
        User user=(User)request.getSession().getAttribute("user");
        host.setOrganization(user.getOrganization());
	}
}
