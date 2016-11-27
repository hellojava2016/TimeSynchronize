package cn.gov.cbrc.bankriskcontrol.controller.system;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.gov.cbrc.bankriskcontrol.entity.AreaCity;
import cn.gov.cbrc.bankriskcontrol.entity.User;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.system.AreaCityService;
import cn.gov.cbrc.bankriskcontrol.util.RiskUtils;

@Controller
@RequestMapping("/areacity")
public class AreaCityController {

	/**
	 * @author lyq
	 */
	@Autowired
	private AreaCityService areaCityService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/gettypes.do")
	public void getTypes(
			HttpServletRequest request, HttpServletResponse response) {
		String code="01";
		Map<String, List<AreaCity>> map = areaCityService.getChildren(code);
		RiskUtils.initAreaCityToView(map, code, response);
	}

	/**
	 * 添加区域信息
	 * @param  response 
	 * @param  request 
	 * */
	@RequestMapping("/add_pre.do")
	public ModelAndView add_pre(Model model, HttpServletRequest request) {	
		String parentString = request.getParameter("parentid");
		Long parentId=StringUtils.isEmpty(parentString)?null:Long.parseLong(parentString);
		AreaCity parentarea=areaCityService.getAreaCityById(parentId);
		model.addAttribute("parentname", parentarea.getName());
		model.addAttribute("parentid", parentId);
		return new ModelAndView("system/areacity-add");	
}

    /**
	 * 添加同级区域信息(省级区域)
	 * @param  response 
	 * @param  request 
	 * */
	@RequestMapping("/add.do")
	public ModelAndView add(Model model, HttpServletRequest request) {
		String name = request.getParameter("name");
		String parentString = request.getParameter("parentid");
		Long parentId=StringUtils.isEmpty(parentString)?null:Long.parseLong(parentString);
		AreaCity pac=new AreaCity();
		pac.setAreaId(parentId);
		AreaCity areaCity = new AreaCity();
		areaCity.setName(name);
		areaCity.setAreaCity(pac);
		areaCityService.addAreaCity(areaCity);
		return new ModelAndView("redirect:/areacity/list.do?parentid="+parentString);
	}
	
	
	/**
	 * 删除区域信息
	 * @param  response 
	 * @param  request 
	 * */
	@RequestMapping("/delete.do")
	@ResponseBody
	public String delete(HttpServletRequest request,HttpServletResponse response){
		try{
			String deleteids= request.getParameter("deleteids");
			String[] pstr=deleteids.split(",");
			for(String id:pstr){
				areaCityService.deleteAreaCity(Long.parseLong(id));			
			}
		}catch(Exception ex){
			return "fail";
		}
		return "success";
		
	}
	
	
	/**
	 * 跳转到修改区域信息
	 * @param  response 
	 * @param  request 
	 * */
	@RequestMapping(value="/update_pre.do",method=RequestMethod.GET)
	public String update_pre(HttpServletRequest request,Model model){		
		AreaCity areaCity =areaCityService
				.getAreaCityById(Long.parseLong(request.getParameter("areaId")));
		model.addAttribute("areacity", areaCity);
		return "system/areacity-update";
		
	}
	
	/**
	 * 保存修改区域信息（暂时提供只是对名称的修改，比如城市易名或者区县或者城市升格（比如县升级为副地级市）。）
	 * @param  pageNo 页数
	 * @param  model  携带到页面的areaCity集合
	 * @param  sortType 
	 * @param  request 
	 * */
	@RequestMapping("/update.do")
	public ModelAndView update(HttpServletRequest request,HttpServletResponse response){		
		AreaCity ac=areaCityService
				.getAreaCityById(Long.parseLong(request.getParameter("areaid")));
		ac.setName(request.getParameter("name"));
		areaCityService.updateAreaCity(ac);
		return new ModelAndView("redirect:/areacity/list.do");
		
	}

	/**
	 * 查看列表页面 
	 * @param  pageNo 页数
	 * @param  model  携带到页面的areaCity集合
	 * @param  sortType 
	 * @param  request 
	 * */
	@RequestMapping("/list.do")
	public String list(Model model, HttpServletRequest request) {
		String parentString = request.getParameter("parentid");
		List<AreaCity> areacitys;
		if (StringUtils.isEmpty(parentString)) {//点击菜单进界面
			areacitys = areaCityService.getTopAreaCityByCurrentUser();
			model.addAttribute("parentname", "区域列表");
			model.addAttribute("addlabel", "新增区域");
			Subject user = SecurityUtils.getSubject();
			String username=(String)user.getPrincipal();  
			User oldu = userService.getUserByUserName(username);
					oldu.setOnline(false);
			model.addAttribute("parentid", oldu.getOrganization().getAreaCity().getAreaId());
			//只有银监会进来可以删除下级子区域 其他只能看到本级的不能删除
			AreaCity city=oldu.getOrganization().getAreaCity();
			model.addAttribute("candelete", city.getAreaCity()==null?true:false);
			model.addAttribute("canadd", city.getAreaCity()==null?true:false);
		} else {//查询子界面
			long parentId = Long.parseLong(parentString);
			areacitys = areaCityService.getSubAreaCity(parentId);
			AreaCity parentarea = areaCityService.getAreaCityById(parentId);
			model.addAttribute("parentname", parentarea.getName() + "子区域列表");
			model.addAttribute("addlabel", "新增" + parentarea.getName() + "子区域");
			model.addAttribute("parentid", parentId);
			model.addAttribute("candelete", true);
		}
		model.addAttribute("areacitys", areacitys);
		String message = request.getParameter("message");
		if(StringUtils.isNotEmpty(message)){
			if("addsucess".equals(message))
				   model.addAttribute("message","添加区域成功");
			else if("updatesucess".equals(message))
				   model.addAttribute("message","修改区域成功");
			else if("deletesuccess".equals(message))
				   model.addAttribute("message","删除区域成功");
			else if("adderror".equals(message))
				   model.addAttribute("message","添加区域失败");
			else if("deleteerror".equals(message))
				   model.addAttribute("message","删除区域失败,请检查该区域是否和其他配置存在关联关系");
			else if("updateerror".equals(message))
				   model.addAttribute("message","修改区域失败");
		}	
		return "system/areacity-city-list";
	}
}
