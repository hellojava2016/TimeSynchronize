package cn.gov.cbrc.bankriskcontrol.controller.system;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cn.gov.cbrc.bankriskcontrol.entity.RiskCategory;
import cn.gov.cbrc.bankriskcontrol.service.system.RiskCategoryService;
import cn.gov.cbrc.bankriskcontrol.util.RiskUtils;

@Controller
@RequestMapping(value = "/riskcategory")
public class RiskCategoryController {
	@Autowired
	private RiskCategoryService riskCategoryService;

	/**
	 * 跳转到添加补报设置页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/update_pre.do")
	public ModelAndView update_pre(HttpServletRequest request,
			HttpServletResponse response,ModelMap mode) {
		long riskId=Long.parseLong(request.getParameter("id"));
		RiskCategory rc=riskCategoryService.getRiskCategoryById(riskId);
		mode.addAttribute("rc", rc);
		mode.addAttribute("reportenable",!RiskUtils.getParentCode(rc.getRiskCode()).equals(""));
		return new ModelAndView("/system/riskcategory-update", mode);
	}

	@RequestMapping(value = "/update.do")
	public ModelAndView update(HttpServletRequest request,
			HttpServletResponse response) {
		long riskId=Long.parseLong(request.getParameter("id"));
		RiskCategory rc=riskCategoryService.getRiskCategoryById(riskId);
		int minValue = Integer.parseInt(request.getParameter("minvalue"));
		int maxValue = Integer.parseInt(request.getParameter("maxValuedp"));
		int startDay = Integer.parseInt(request.getParameter("startDay"));
		int endDay = Integer.parseInt(request.getParameter("endDay"));
		boolean useThresholdValue=Boolean.parseBoolean(request.getParameter("usethresholdvalue"));
		boolean allowReport=Boolean.parseBoolean(request.getParameter("allowReport"));
		rc.setUseThresholdValue(useThresholdValue);
		rc.setMinValue(minValue);
		rc.setMaxValue(maxValue);
		rc.setStartDay(startDay);
		rc.setEndDay(endDay);
		rc.setAllowReport(allowReport);		
		riskCategoryService.updateRiskCategory(rc);
		if(rc.isHasLeaf()){
			boolean cascade=Boolean.parseBoolean(request.getParameter("cascade"));	
			if(cascade){
				List<RiskCategory> categorys = riskCategoryService.getChildRiskCategoryByTopCode(rc.getRiskCode());
				for(RiskCategory category:categorys){
					category.setStartDay(startDay);
					category.setEndDay(endDay);
					riskCategoryService.updateRiskCategory(category);
				}
			}
		}
		return new ModelAndView("redirect:/riskcategory/list.do?riskCode="+RiskUtils.getParentCode(rc.getRiskCode()));
	}

	@RequestMapping(value = "/delete.do")
	public ModelAndView deleteRiskCategory(HttpServletRequest request,
			ModelMap mode) {
		String id = request.getParameter("id");
		riskCategoryService.deleteRiskCategoryById(Long.parseLong(id));
		mode.addAttribute("message", "删除成功");
		return new ModelAndView("redirect:/riskcategory/list.do", mode);
	}

	@RequestMapping(value = "/list.do")
	public String getRiskCategoryList(
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, HttpServletRequest request) {
		String riskCode=request.getParameter("riskCode");
		List<RiskCategory> rcs=riskCategoryService.getChildRiskCategorys(riskCode);
		model.addAttribute("rates", rcs);
		return "/system/riskcategory-list";
	}
}
