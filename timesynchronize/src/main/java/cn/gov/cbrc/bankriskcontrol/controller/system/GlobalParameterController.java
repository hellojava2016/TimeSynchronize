package cn.gov.cbrc.bankriskcontrol.controller.system;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.gov.cbrc.bankriskcontrol.entity.GlobalParameter;
import cn.gov.cbrc.bankriskcontrol.service.system.GlobalParameterService;
import cn.gov.cbrc.bankriskcontrol.util.ExportUtil;

@Controller
@RequestMapping("/globalparameter")
public class GlobalParameterController {
	@Autowired
	private GlobalParameterService globalParameterService;

	@RequestMapping(value = "/list.do")
	public String list(Model model) {
		GlobalParameter parameter = globalParameterService.getGlobalParameter();
		model.addAttribute("parameter", parameter);
		return "system/globalparameter";
	}

	@RequestMapping(value = "/update.do")
	public String update(HttpServletRequest request) {
		int systemavailable = Integer.parseInt(request.getParameter("systemavailable"));
		int systemtransaction = Integer.parseInt(request.getParameter("systemtransaction"));
		int operationchanges = Integer.parseInt(request.getParameter("operationchanges"));
		int fakesiteattachment = Integer.parseInt(request.getParameter("fakesiteattachment"));
		boolean continueDeclineEnable=Boolean.parseBoolean(request.getParameter("continueDeclineEnable"));
		boolean deviateAverageEnable=Boolean.parseBoolean(request.getParameter("deviateAverageEnable"));
		int continueDecline = Integer.parseInt(request.getParameter("continueDecline"));
		int deviateAverage = Integer.parseInt(request.getParameter("deviateAverage"));
		GlobalParameter param = globalParameterService.getGlobalParameter();
		param.setSystemavailable(systemavailable);
		param.setSystemtransaction(systemtransaction);
		param.setOperationchanges(operationchanges);
		param.setFakesiteattachment(fakesiteattachment);
		param.setContinueDeclineEnable(continueDeclineEnable);
		param.setContinueDecline(continueDecline);
		param.setDeviateAverageEnable(deviateAverageEnable);
		param.setDeviateAverage(deviateAverage);		
		globalParameterService.update(param);
		ExportUtil.parameter=param;
		return "";
	}

}
