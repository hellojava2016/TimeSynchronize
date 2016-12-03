package com.shtitan.timesynchronize.controller.topo.chassis;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/chassis")
public class ChassisController {
   
	@RequestMapping(value="/list.do")
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
			HttpServletRequest request){
		
 	    return "/chassis/chassisdemo";
	}
}
