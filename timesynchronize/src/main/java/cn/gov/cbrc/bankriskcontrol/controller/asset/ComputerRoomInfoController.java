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
import cn.gov.cbrc.bankriskcontrol.entity.ComputerRoomInfo;
import cn.gov.cbrc.bankriskcontrol.entity.Organization;
import cn.gov.cbrc.bankriskcontrol.entity.User;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.asset.ComputerRoomInfoService;
import cn.gov.cbrc.bankriskcontrol.service.system.OrganizationService;
import cn.gov.cbrc.bankriskcontrol.util.ConvertUtils;
import cn.gov.cbrc.bankriskcontrol.util.DateUtil;
import cn.gov.cbrc.bankriskcontrol.util.ExportUtil;
import cn.gov.cbrc.bankriskcontrol.util.Page;

@Controller
@RequestMapping(value = "/asset/computerroominfo")
public class ComputerRoomInfoController {
	
	@Autowired
	private ComputerRoomInfoService computerRoomInfoService;

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
		Page<ComputerRoomInfo> page = new Page<ComputerRoomInfo>();
		page.setPageNo(pageNo);
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotEmpty(pageSize))
			  page.setPageSize(Integer.parseInt(pageSize));
		Page<ComputerRoomInfo> list=computerRoomInfoService.getComputerRoomInfos(param, page);
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
				   model.addAttribute("message","添加机房成功");
			else if("updatesucess".equals(message))
				   model.addAttribute("message","修改机房成功");
			else if("deletesucess".equals(message))
				   model.addAttribute("message","删除机房成功");
			else if("adderror".equals(message))
				   model.addAttribute("message","修改机房失败,请重新添加");
			else if("deleteerror".equals(message))
				   model.addAttribute("message","删除机房失败,请重新删除");
			else if("updateerror".equals(message))
				   model.addAttribute("message","修改机房失败,请检查数据是否合法");
		}	
		return "/asset/computerroominfo-list";
	}
	
	@RequestMapping(value = "/query.do")
	public String query(
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, HttpServletRequest request) {
		String uniqueVal=request.getParameter("uniqueVal");
		long orgId=Long.parseLong(request.getParameter("Organization"));
		AssetQueryParam param=new AssetQueryParam();
		param.setUniqueVal(uniqueVal);
		param.setOrganizationId(orgId);
		Page<ComputerRoomInfo> page = new Page<ComputerRoomInfo>();
		page.setPageNo(pageNo);
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotEmpty(pageSize))
			  page.setPageSize(Integer.parseInt(pageSize));
		Page<ComputerRoomInfo> list=computerRoomInfoService.getComputerRoomInfos(param, page);
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
		String message = request.getParameter("message");
		if(StringUtils.isNotEmpty(message)){
			if("addsucess".equals(message))
				   model.addAttribute("message","添加机房成功");
			else if("updatesucess".equals(message))
				   model.addAttribute("message","修改机房成功");
			else if("deletesucess".equals(message))
				   model.addAttribute("message","删除机房成功");
			else if("adderror".equals(message))
				   model.addAttribute("message","修改机房失败,请重新添加");
			else if("deleteerror".equals(message))
				   model.addAttribute("message","删除机房失败,请重新删除");
			else if("updateerror".equals(message))
				   model.addAttribute("message","修改机房失败,请检查数据是否合法");
		}	
		return "/asset/computerroominfo-list";
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
		Page<ComputerRoomInfo> page = new Page<ComputerRoomInfo>();
		page.setPageSize(Integer.MAX_VALUE);
		page.setPageNo(pageNo);
		Page<ComputerRoomInfo> list=computerRoomInfoService.getComputerRoomInfos(param, page);	
		List<String[]> valueList=new ArrayList<String[]>();
		valueList.add(new String[]{"所属机构","编号","生产产商","设备用途","所占面积(平方米)","设备位置","设备功率(W)","开始服务时间","UPS数量","精密空调个数"});
		for(ComputerRoomInfo cr:list.getResult()){
			String orgName=cr.getOrganization().getName();
			String servertime=DateUtil.getShortDate(cr.getServerTime());
			valueList.add(new String[]{orgName ,cr.getUniqueVal(),cr.getManufacturer(),cr.getPurpose(),cr.getArea()+"",cr.getLocation(),cr.getPower()+"", servertime,cr.getUpsCount()+"",cr.getPrecisionAcCount()+""});
		}
		ExportUtil.exportExcel(valueList, "机房列表", response);
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
				computerRoomInfoService.deleteComputerRoomInfoById(id);
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
	
	@RequestMapping(value = "/add_pre.do")
	public ModelAndView add_pre(HttpServletRequest request,
			HttpServletResponse response,Model model) {
		model.addAttribute("id", 0);
		return new ModelAndView("/asset/computerroominfo-add", null);
	}
	
	@RequestMapping(value = "/add.do")
	public ModelAndView add(HttpServletRequest request,
			HttpServletResponse response) {
		try{
			ComputerRoomInfo info = new ComputerRoomInfo();
			getValue(request, info);
	        computerRoomInfoService.addComputerRoomInfo(info); 
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
		ComputerRoomInfo cr=computerRoomInfoService.getComputerRoomInfoById(id);
		mode.addAttribute("cr", cr);
		mode.addAttribute("id", id);
		return new ModelAndView("/asset/computerroominfo-add", mode);
	}

	@RequestMapping(value = "/update.do")
	public ModelAndView update(HttpServletRequest request,
			HttpServletResponse response) {
		try{
			long id=Long.parseLong(request.getParameter("id"));
			ComputerRoomInfo info=computerRoomInfoService.getComputerRoomInfoById(id);
			getValue(request, info);
			computerRoomInfoService.updateComputerRoomInfo(info);
		}catch(Exception ex){
			String listurl=(String)request.getSession().getAttribute("listurl");
			String queryparam=(String)request.getSession().getAttribute("queryparam");
			return new ModelAndView("redirect:"+listurl+"?message=updateerror&"+queryparam);
		}
		String listurl=(String)request.getSession().getAttribute("listurl");
		String queryparam=(String)request.getSession().getAttribute("queryparam");
		return new ModelAndView("redirect:"+listurl+"?message=updatesucess&"+queryparam);
	}
	
	@RequestMapping(value="/checkname.do")
	@ResponseBody
	public boolean checkUniqueVal(Model model,HttpServletRequest request){
		String id=request.getParameter("id");
		if(!id.equals("0"))
			return true;
		String uniqueVal= request.getParameter("uniqueVal");
		if(null==uniqueVal)
			return false;
		ComputerRoomInfo db=computerRoomInfoService.getComputerRoomInfoByUniqueVal(uniqueVal);
	      if(null== db)
	    	  return true;
		return false;
	}
	
	private void getValue(HttpServletRequest request,ComputerRoomInfo info){
		String uniqueVal=request.getParameter("uniqueVal");
        String manufacturer=request.getParameter("manufacturer");
        String purpose=request.getParameter("purpose");
        double area=Double.parseDouble(request.getParameter("area"));
        String location=request.getParameter("location");
        int power=Integer.parseInt(request.getParameter("power"));
        Date serverTime=(Date)ConvertUtils.convertStringToObject(request.getParameter("serverTime"),Date.class);
        int upsCount=Integer.parseInt(request.getParameter("upsCount"));
        int precisionAcCount=Integer.parseInt(request.getParameter("precisionAcCount"));        
        info.setUniqueVal(uniqueVal);
        info.setManufacturer(manufacturer);
        info.setPurpose(purpose);
        info.setArea(area);
        info.setLocation(location);
        info.setPower(power);
        info.setServerTime(serverTime);
        info.setUpsCount(upsCount);
        info.setPrecisionAcCount(precisionAcCount);
        info.setRecordTime(new Date());
        User user=(User)request.getSession().getAttribute("user");
        info.setOrganization(user.getOrganization());
	}
}
