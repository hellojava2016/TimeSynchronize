package com.shtitan.timesynchronize.controller.system;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value = "/template")
public class TemplateController {	

	@RequestMapping("/list.do")
	public ModelAndView list(HttpServletRequest request, Model model) {
		return new ModelAndView("/system/template");
	} 
}
