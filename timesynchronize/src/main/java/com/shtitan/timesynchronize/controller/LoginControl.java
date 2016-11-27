package com.shtitan.timesynchronize.controller;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.shtitan.timesynchronize.service.account.UserService;


@Controller
public class LoginControl {
	
	@Autowired
	private UserService userService;
	
	
	@RequestMapping(value="/login.do", method = RequestMethod.GET)
	public String login() {
		return "system/login";
	}
	
	@RequestMapping(value="/relogin.do", method = RequestMethod.GET)
	public String relogin() {
		return "system/relogin";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String fail(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName, Model model) {
		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, userName);
		return "system/login";
	}

	@RequestMapping(value="/logout.do", method = RequestMethod.GET)
	public String logout(){
		return "system/relogin";
	}

}
