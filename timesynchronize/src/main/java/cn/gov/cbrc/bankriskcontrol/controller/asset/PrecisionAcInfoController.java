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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.gov.cbrc.bankriskcontrol.dto.AssetQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.ComputerRoomInfo;
import cn.gov.cbrc.bankriskcontrol.entity.Organization;
import cn.gov.cbrc.bankriskcontrol.entity.PrecisionAcInfo;
import cn.gov.cbrc.bankriskcontrol.entity.User;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.asset.ComputerRoomInfoService;
import cn.gov.cbrc.bankriskcontrol.service.asset.PrecisionAcInfoService;
import cn.gov.cbrc.bankriskcontrol.service.system.OrganizationService;
import cn.gov.cbrc.bankriskcontrol.util.ExportUtil;
import cn.gov.cbrc.bankriskcontrol.util.Page;

@Controller
@RequestMapping(value = "/asset/precisionacinfo")
public class PrecisionAcInfoController {
	@Autowired
	private PrecisionAcInfoService precisionAcInfoService;

	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
    private UserService userService;
	
	@Autowired
	private ComputerRoomInfoService computerRoomInfoService;
	
	@RequestMapping(value = "/list.do")
	public String getlist(
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, HttpServletRequest request) {
		AssetQueryParam param=new AssetQueryParam();
		Page<PrecisionAcInfo> page = new Page<PrecisionAcInfo>();
		page.setPageNo(pageNo);
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotEmpty(pageSize))
			  page.setPageSize(Integer.parseInt(pageSize));
		Page<PrecisionAcInfo> list=precisionAcInfoService.getPrecisionAcInfos(param, page);
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
				   model.addAttribute("message","添加精密空调成功");
			else if("updatesucess".equals(message))
				   model.addAttribute("message","修改精密空调成功");
			else if("deletesucess".equals(message))
				   model.addAttribute("message","删除精密空调成功");
			else if("adderror".equals(message))
				   model.addAttribute("message","修改精密空调失败,请重新添加");
			else if("deleteerror".equals(message))
				   model.addAttribute("message","删除精密空调失败,请重新删除");
			else if("updateerror".equals(message))
				   model.addAttribute("message","修改精密空调失败,请检查数据是否合法");
		}	
		return "/asset/precisionacinfo-list";
	}
	
	@RequestMapping(value = "/add_pre.do")
	public ModelAndView add_pre(HttpServletRequest request,
			HttpServletResponse response,Model model) {
		User user=(User)request.getSession().getAttribute("user");
		List<ComputerRoomInfo> crs=computerRoomInfoService.getComputerRoomInfosByOrganization(user.getOrganization().getOrgId());
		model.addAttribute("crs", crs);
		model.addAttribute("id", 0);
		return new ModelAndView("/asset/precisionacinfo-add", null);
	}
	
	@RequestMapping(value = "/add.do")
	public ModelAndView add(HttpServletRequest request,
			HttpServletResponse response) {
		try{
			PrecisionAcInfo info=new PrecisionAcInfo();
			getValue(request, info);        
	        precisionAcInfoService.addPrecisionAcInfo(info);
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
		PrecisionAcInfo ac=precisionAcInfoService.getPrecisionAcInfoById(id);
		
		User user=(User)request.getSession().getAttribute("user");
		List<ComputerRoomInfo> crs=computerRoomInfoService.getComputerRoomInfosByOrganization(user.getOrganization().getOrgId());
		model.addAttribute("crs", crs);
		model.addAttribute("ac", ac);
		model.addAttribute("id", id);
		return new ModelAndView("/asset/precisionacinfo-add", null);
	}
	
	@RequestMapping(value = "/update.do")
	public ModelAndView update(HttpServletRequest request,
			HttpServletResponse response) {
		try{
			long id=Long.parseLong(request.getParameter("id"));
			PrecisionAcInfo ac=precisionAcInfoService.getPrecisionAcInfoById(id);
			getValue(request, ac);   
	        precisionAcInfoService.updatePrecisionAcInfo(ac);
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
		Page<PrecisionAcInfo> page = new Page<PrecisionAcInfo>();
		page.setPageNo(pageNo);
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotEmpty(pageSize))
			  page.setPageSize(Integer.parseInt(pageSize));
		Page<PrecisionAcInfo> list=precisionAcInfoService.getPrecisionAcInfos(param, page);
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
				   model.addAttribute("message","添加精密空调成功");
			else if("updatesucess".equals(message))
				   model.addAttribute("message","修改精密空调成功");
			else if("deletesucess".equals(message))
				   model.addAttribute("message","删除精密空调成功");
			else if("adderror".equals(message))
				   model.addAttribute("message","修改精密空调失败,请重新添加");
			else if("deleteerror".equals(message))
				   model.addAttribute("message","删除精密空调失败,请重新删除");
			else if("updateerror".equals(message))
				   model.addAttribute("message","修改精密空调失败,请检查数据是否合法");
		}	
		return "/asset/precisionacinfo-list";
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
		Page<PrecisionAcInfo> page = new Page<PrecisionAcInfo>();
		page.setPageSize(Integer.MAX_VALUE);
		page.setPageNo(pageNo);
		Page<PrecisionAcInfo> list=precisionAcInfoService.getPrecisionAcInfos(param, page);
		List<String[]> valueList=new ArrayList<String[]>();
		valueList.add(new String[]{"所属机构","编号","所属机房编号","精密空调名称","型号","送风温度(摄氏度)","送风湿度(%RH)","回风湿度(%RH)"});
		for(PrecisionAcInfo pa:list.getResult()){
			String orgName=pa.getOrganization().getName();
			valueList.add(new String[]{orgName ,pa.getUniqueVal(), pa.getComputerRoomInfo().getUniqueVal(),pa.getName(),pa.getType(),pa.getSupplyTemp(),pa.getSupplyHumidity(),pa.getReturnHumidity()});
		}
		ExportUtil.exportExcel(valueList, "精密空调列表", response);
        return null;
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
		PrecisionAcInfo db=precisionAcInfoService.getPrecisionAcInfoByUniqueVal(uniqueVal);
	      if(null== db)
	    	  return true;
		return false;
	}
	

	@RequestMapping(value = "/delete.do")
	public ModelAndView delete(HttpServletRequest request,
			HttpServletResponse response) {
		try{
			String deleteids = request.getParameter("deleteids");
			String[] ids = deleteids.split(",");
			for (String temp : ids) {
				long id = Long.parseLong(temp);
				precisionAcInfoService.deletePrecisionAcInfoById(id);
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
	
	private void getValue(HttpServletRequest request,PrecisionAcInfo info){
		String uniqueVal=request.getParameter("uniqueVal");
        String roomUV=request.getParameter("roomUV");
        String type=request.getParameter("type");
        String name=request.getParameter("name");
        String returnHumidity=request.getParameter("returnHumidity");
        String supplyHumidity=request.getParameter("supplyHumidity");
        String supplyTemp=request.getParameter("supplyTemp");
        User user=(User)request.getSession().getAttribute("user");
        ComputerRoomInfo cr=computerRoomInfoService.getComputerRoomInfoById(Long.parseLong(roomUV));
        
        info.setUniqueVal(uniqueVal);        
        info.setType(type);
        info.setName(name);
        info.setReturnHumidity(returnHumidity);
        info.setSupplyHumidity(supplyHumidity);
        info.setSupplyTemp(supplyTemp);
        info.setRecordTime(new Date());
        info.setComputerRoomInfo(cr);     
        info.setRoomUV(roomUV);
        info.setOrganization(user.getOrganization());
	}
}
