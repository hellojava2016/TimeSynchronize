package com.shtitan.timesynchronize.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.shtitan.timesynchronize.entity.OperateLog;
import com.shtitan.timesynchronize.entity.User;
import com.shtitan.timesynchronize.service.system.OperateLogService;


public class LogInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	private OperateLogService operateLogService; 
	
	public static Map<String,String> p;
	static {
		URL url=LogInterceptor.class.getClassLoader().getResource("action.properties");
		try {
			InputStream is=url.openStream();
			Properties pro=new Properties();
			pro.load(is);
			p=new TreeMap<String,String>();
			for(Object key:pro.keySet()){
				p.put((String)key, pro.getProperty((String)key));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String url = request.getRequestURI();
		if (url.equals("/timesynchronize/user/login.do"))
			return super.preHandle(request, response, handler);
		else if (url.equals("/timesynchronize/report/report/testreport.do"))
			return super.preHandle(request, response, handler);
		else if (url.equals("/timesynchronize/user/logout.do")){			
			String action = url.substring(17, url.length() - 3);
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");
			OperateLog log = new OperateLog();
			log.setOperateTime(new Date());
			log.setUrl(url);
			log.setUser(user);
			String actionName = p.get(action);
			log.setAction(StringUtils.isEmpty(actionName) ? action : actionName);
			log.setActionEn(action);
			operateLogService.addOperateLog(log);
			return super.preHandle(request, response, handler);}
		else {
			Object user = request.getSession().getAttribute("user");
			if (user == null) {
				response.sendRedirect("/timesynchronize");
				return false;
			} else {
				return super.preHandle(request, response, handler);
			}
		}
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		String url = request.getRequestURI();
		Date time = new Date();
		String action = url.substring(17, url.length() - 3);
		if (needLog(action)) {
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");
			OperateLog log = new OperateLog();
			log.setOperateTime(time);
			log.setUrl(url);
			log.setUser(user);
			String actionName = p.get(action);
			log.setAction(StringUtils.isEmpty(actionName) ? action : actionName);
			log.setActionEn(action);
			operateLogService.addOperateLog(log);
		}
		super.afterCompletion(request, response, handler, ex);
	}
	
	private boolean needLog(String action){
		if(action.contains("/logout"))
			return false;
		if(action.contains("/checkname"))
			return false;
		if(action.contains("/gettypes"))
			return false;
		if(action.contains("_pre"))
			return false;
		if(action.contains("mewmessage"))
			return false;
		return true;
	}
}
