package com.shtitan.timesynchronize.controller.system;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shtitan.timesynchronize.entity.BankMessage;
import com.shtitan.timesynchronize.entity.BankMessageReceiver;
import com.shtitan.timesynchronize.entity.User;
import com.shtitan.timesynchronize.service.system.ReceiveMessageService;
import com.shtitan.timesynchronize.util.ConvertUtils;
import com.shtitan.timesynchronize.util.Page;


@Controller
@RequestMapping(value = "/rmessage")
public class ReceiveMessageController {
	@Autowired
	private ReceiveMessageService receiveMessageService;
	
	@RequestMapping(value = "/list.do")
	public String getMessageList(
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, HttpServletRequest request) {
		Page<BankMessageReceiver> page = new Page<BankMessageReceiver>();
		page.setPageNo(pageNo);
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotEmpty(pageSize))
			  page.setPageSize(Integer.parseInt(pageSize));
		 
		Date startDate=null;
		String startdatestr =request.getParameter("startdate");
		if(StringUtils.isNotEmpty(startdatestr)){
			startDate=(Date)ConvertUtils.convertStringToObject(startdatestr, Date.class);
		}else{
			startdatestr="";
		}
		Date endDate=null;
		String endtdatestr =request.getParameter("enddate");
		if(StringUtils.isNotEmpty(endtdatestr)){
			endDate=(Date)ConvertUtils.convertStringToObject(endtdatestr, Date.class);
		}else{
			endtdatestr="";
		}
		
		model.addAttribute("startdate", startdatestr);
		model.addAttribute("enddate", endtdatestr);
		model.addAttribute("pageNo", pageNo);
		boolean flag = false; 		
		User user=(User)request.getSession().getAttribute("user");
		Page<BankMessageReceiver> list = receiveMessageService.getMessages(user,startDate, endDate,flag, page);
		model.addAttribute("bankmessages", list);
		return "/system/reiceivemessage-list";
	}
	
	
	
	@RequestMapping(value = "/updatepre.do")
	public String readMessage(Model model, HttpServletRequest request){
		String id = request.getParameter("id");
		BankMessageReceiver bmr=receiveMessageService.getBankMessageReceiverById(Long.parseLong(id));
		if(!bmr.isHasRead()){
			bmr.setHasRead(true);
			receiveMessageService.readMessage(bmr);
		}
		model.addAttribute("bmr", bmr);
		return "/system/reiceivemessage-detail";
	}
	
	@RequestMapping(value = "/mewmessage.do")
	@ResponseBody
	public String readMessage(HttpServletRequest request){
		User user=(User)request.getSession().getAttribute("user");
		List<BankMessageReceiver> list = receiveMessageService.getNewMessage(user);
		if(CollectionUtils.isNotEmpty(list)){
			return list.get(0).getMessage().getTitle();
		}
		return "";
	}
	
	@RequestMapping(value = "/delete.do")
	@ResponseBody
	public String deleteMessage(HttpServletRequest request){
		String id = request.getParameter("id");
		receiveMessageService.deleteReiceiveMessage(Long.parseLong(id));
		return "";
	}
	
}
