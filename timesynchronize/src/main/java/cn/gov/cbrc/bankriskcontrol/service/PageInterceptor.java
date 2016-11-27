package cn.gov.cbrc.bankriskcontrol.service;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class PageInterceptor extends HandlerInterceptorAdapter{
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		try{
			StringBuilder sb = new StringBuilder();
			Enumeration<String> names=request.getParameterNames();
			while(names.hasMoreElements()){
				String name = names.nextElement();
				if((!name.equals("page")) &&(!name.equals("pageSize")) && (!name.equals("sortType")) && (!name.equals("message"))){
					sb.append(name);
					sb.append("=");
					sb.append(request.getParameter(name));
					sb.append("&");
				}
			}
			String result = sb.toString();
			if (modelAndView != null && modelAndView.getModelMap() != null) {
				modelAndView.getModelMap().addAttribute("searchParams", result);
			}
		} catch (Exception exs){
			exs.printStackTrace();
			System.out.println("page interveptor error");
		}
		try{
			String urlstr =request.getServletPath();
			String querystr=request.getQueryString(); 
			if(StringUtils.isEmpty(querystr))
				querystr="";
			if(StringUtils.isEmpty(urlstr))
				urlstr="";
			if(StringUtils.isNotEmpty(querystr))
				querystr=removeParameter(querystr);
			if(urlstr.contains("list.do") || urlstr.contains("query.do")){
				request.getSession().setAttribute("listurl", urlstr);
				request.getSession().setAttribute("queryparam", querystr);
			}
		}catch(Exception ex2){
			ex2.printStackTrace();
		}
		try{
			super.postHandle(request, response, handler, modelAndView);
		}catch(Exception ex){
			ex.printStackTrace();
		}
			
	}
	
	private  String removeParameter(String url){
		if(url.contains("message=")){
			int start = url.indexOf("message=");
			if(0==start)
				start=1;
			String startstr= url.substring(0, start-1);
			String tail = url.substring(start);
			int end = tail.indexOf("&");
			if(end<=0)
				tail="";
			else
			    tail = tail.substring(tail.indexOf("&"));
			String result = startstr+tail;
			if(result.startsWith("&")){
				return result.substring(1);
			}else{
				return result;
			}
		}else{
			return url;
		}
			
	}
	
	
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}
}
