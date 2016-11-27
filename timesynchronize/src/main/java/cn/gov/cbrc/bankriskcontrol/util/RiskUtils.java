package cn.gov.cbrc.bankriskcontrol.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import cn.gov.cbrc.bankriskcontrol.dao.risk.ReportAdditionalDao;
import cn.gov.cbrc.bankriskcontrol.dto.AnalyseQueryParam;
import cn.gov.cbrc.bankriskcontrol.dto.AssetQueryParam;
import cn.gov.cbrc.bankriskcontrol.dto.InformationRiskEventItem;
import cn.gov.cbrc.bankriskcontrol.dto.ReportQueryParam;
import cn.gov.cbrc.bankriskcontrol.dto.ReportResultException;
import cn.gov.cbrc.bankriskcontrol.entity.AppSystem;
import cn.gov.cbrc.bankriskcontrol.entity.AreaCity;
import cn.gov.cbrc.bankriskcontrol.entity.DatabaseInfo;
import cn.gov.cbrc.bankriskcontrol.entity.ElectronicActiveUserChangeRate;
import cn.gov.cbrc.bankriskcontrol.entity.ElectronicTransactionChangeRate;
import cn.gov.cbrc.bankriskcontrol.entity.ExtraReportInfo;
import cn.gov.cbrc.bankriskcontrol.entity.FakeSiteAttachmentRate;
import cn.gov.cbrc.bankriskcontrol.entity.InfoTechnologyRiskEventCount;
import cn.gov.cbrc.bankriskcontrol.entity.MiddlewareInfo;
import cn.gov.cbrc.bankriskcontrol.entity.OperationChangeSuccessRate;
import cn.gov.cbrc.bankriskcontrol.entity.Organization;
import cn.gov.cbrc.bankriskcontrol.entity.OutsideAttackChangeRate;
import cn.gov.cbrc.bankriskcontrol.entity.Permission;
import cn.gov.cbrc.bankriskcontrol.entity.RiskCategory;
import cn.gov.cbrc.bankriskcontrol.entity.Role;
import cn.gov.cbrc.bankriskcontrol.entity.SystemAvailableRate;
import cn.gov.cbrc.bankriskcontrol.entity.SystemTransactionSuccessRate;
import cn.gov.cbrc.bankriskcontrol.entity.User;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.asset.AppSystemService;
import cn.gov.cbrc.bankriskcontrol.service.system.OrganizationService;
import cn.gov.cbrc.bankriskcontrol.service.system.RiskCategoryService;
import cn.gov.cbrc.bankriskcontrol.util.encode.EncodeUtils;

@SuppressWarnings("rawtypes")
public class RiskUtils {	
	/**
	 * 从页面获取级联选择控件的值
	 * @param request
	 * @return
	 */
	public static String getSelectedRiskCodeFromView(HttpServletRequest request) {
		String dist = request.getParameter("dist");
		if (StringUtils.isNotEmpty(dist) &&  !"null".equals(dist))
			return dist;
		String city = request.getParameter("city");
		if (StringUtils.isNotEmpty(city)  &&  !"null".equals(city))
			return city;
		String prov = request.getParameter("prov");
		if (StringUtils.isNotEmpty(prov)  && !"null".equals(prov))
			return prov;
		return null;
	}

	/**
	 * 从指标列表生成级联选择控件
	 * @param map
	 * @param code
	 * @param response
	 * @param defaultChoose 为null表示不缺省选一个指标，有"--请选择风险指标--"；不为null表示必须缺省选一个指标，没有"--请选择风险指标--"
	 */
	public static void initRiskCodeToView(Map<String, List<RiskCategory>> map,
			String code, HttpServletResponse response,String defaultChoose) {
		List<RiskCategory> topList = map.get(code);

		JSONArray arrays = new JSONArray();		
		if (StringUtils.isEmpty(defaultChoose)) {
			JSONObject vjson = new JSONObject();
			vjson.accumulate("p", "--请选择风险指标--");
			vjson.accumulate("v", "");
			arrays.add(vjson);
		}	
		
		for (RiskCategory ca : topList) {
			String code1 = ca.getRiskCode();
			JSONObject json = new JSONObject();
			json.accumulate("p", ca.getRiskName());
			json.accumulate("v", ca.getRiskCode());
			List<RiskCategory> childs = map.get(code1);

			if (CollectionUtils.isNotEmpty(childs)) {
				JSONArray array = new JSONArray();
				// 如果自身可以上报则在下级选择框中添加一个本级指标
				if (ca.getAllowReport()) {
					JSONObject self = new JSONObject();
					self.accumulate("n", "本级指标");
					self.accumulate("v", ca.getRiskCode());
					array.add(self);
				}
				for (RiskCategory child : childs) {
					JSONObject object = getJSONObject_child(child,
							map.get(child.getRiskCode()));
					array.add(object);
				}
				json.accumulate("c", array);
			}
			arrays.add(json);
		}
		JSONObject finalobject = new JSONObject();
		finalobject.put("citylist", arrays);

		response.setContentType("text/plain; charset=UTF-8");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		PrintWriter write;
		try {
			write = response.getWriter();
			write.print(finalobject.toString());
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static JSONObject getJSONObject_child(RiskCategory rc,
			List<RiskCategory> childList) {
		if (CollectionUtils.isNotEmpty(childList)) {
			JSONObject json = new JSONObject();
			json.accumulate("n", rc.getRiskName());
			json.accumulate("v", rc.getRiskCode());
			if (CollectionUtils.isNotEmpty(childList)) {
				JSONArray array = new JSONArray();
				// 如果自身可以上报则在下级选择框中添加一个本级指标
				if (rc.getAllowReport()) {
					JSONObject self = new JSONObject();
					self.accumulate("s", "本级指标");
					self.accumulate("v", rc.getRiskCode());
					array.add(self);
				}
				for (RiskCategory temp : childList) {
					JSONObject json11 = new JSONObject();
					json11.accumulate("s", temp.getRiskName());
					json11.accumulate("v", temp.getRiskCode());
					array.add(json11);
				}
				json.accumulate("a", array);
			}
			return json;
		} else {
			JSONObject json = new JSONObject();
			json.accumulate("n", rc.getRiskName());
			json.accumulate("v", rc.getRiskCode());
			return json;
		}
	}
	
	/**
	 * 从区域列表生成级联选择控件
	 * @param map
	 * @param code
	 * @param response
	 */
	public static void initAreaCityToView(Map<String, List<AreaCity>> map,
			String code, HttpServletResponse response) {
		List<AreaCity> topList = map.get(code);

		JSONArray arrays = new JSONArray();
		for (AreaCity ca : topList) {
			String code1 = ca.getAreeCode();
			JSONObject json = new JSONObject();
			json.accumulate("p", ca.getName());
			json.accumulate("v", ca.getAreeCode());
			List<AreaCity> childs = map.get(code1);

			if (CollectionUtils.isNotEmpty(childs)) {
				JSONArray array = new JSONArray();
				// 如果自身可以被选择则在下级选择框中添加一个本级区域
				if (true) {
					JSONObject self = new JSONObject();
					self.accumulate("n", "本级区域");
					self.accumulate("v", ca.getAreeCode());
					array.add(self);
				}
				for (AreaCity child : childs) {
					JSONObject object = getJSONObject_child(child,
							map.get(child.getAreeCode()));
					array.add(object);
				}
				json.accumulate("c", array);
			}
			arrays.add(json);
		}
		JSONObject finalobject = new JSONObject();
		finalobject.put("citylist", arrays);

		response.setContentType("text/plain; charset=UTF-8");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		PrintWriter write;
		try {
			write = response.getWriter();
			write.print(finalobject.toString());
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static JSONObject getJSONObject_child(AreaCity rc,
			List<AreaCity> childList) {
		if (CollectionUtils.isNotEmpty(childList)) {
			JSONObject json = new JSONObject();
			json.accumulate("n", rc.getName());
			json.accumulate("v", rc.getAreeCode());
			if (CollectionUtils.isNotEmpty(childList)) {
				JSONArray array = new JSONArray();
				// 如果自身可以被选择则在下级选择框中添加一个本级区域
				if (true) {
					JSONObject self = new JSONObject();
					self.accumulate("s", "本级区域");
					self.accumulate("v", rc.getAreeCode());
					array.add(self);
				}
				for (AreaCity temp : childList) {
					JSONObject json11 = new JSONObject();
					json11.accumulate("s", temp.getName());
					json11.accumulate("v", temp.getAreeCode());
					array.add(json11);
				}
				json.accumulate("a", array);
			}
			return json;
		} else {
			JSONObject json = new JSONObject();
			json.accumulate("n", rc.getName());
			json.accumulate("v", rc.getAreeCode());
			return json;
		}
	}
	
	public static int getPeroidByReportDate(RiskCategory category,Date reportDate){
		int period=0;
		Calendar cal=Calendar.getInstance();
		cal.setTime(reportDate);
		if (category.getCycle() == 1) {// 周期为日
			period=cal.get(Calendar.DAY_OF_YEAR);
		} else if (category.getCycle() == 2) {// 周期为周
			int day = cal.get(Calendar.DAY_OF_WEEK);
			if (day == Calendar.SUNDAY)//周日是最后一天
				period = cal.get(Calendar.WEEK_OF_YEAR) - 1;
			else
				period = cal.get(Calendar.WEEK_OF_YEAR);
			if(reportDate.getMonth()==11&&cal.get(Calendar.WEEK_OF_YEAR)==1){//处理2013-11-29这种跨年周的情况，应该算成52周而不是0周
				period=getPeroidByReportDate(category,DateUtils.addDays(reportDate, -7))+1;
			}
		} else if (category.getCycle() == 3) {//周期为月
			period=cal.get(Calendar.MONTH)+1;
		}
		return period;
	}
	
	/**
	 * 校验上报是否补报以及是否合法
	 * @param reportAdditionalDao
	 * @param organization
	 * @param rc
	 * @param period
	 * @param extral
	 * @param now
	 */
	public static boolean checkRiskExtraAndValid(ReportAdditionalDao reportAdditionalDao,
			Organization organization, RiskCategory rc, int period,
			boolean extral, Date now,Date reportDate) {
		boolean isExtra=false;
		// 根据上报周期和上报设置判断本次的启示结束时间判断上报周期
		List<Object> check = RiskUtils.checkRange(rc,reportDate);
		boolean ok = (Boolean) check.get(0);
		if (!ok) {//如果不在上报周期内则判断补报
			ExtraReportInfo extra = reportAdditionalDao
					.getUniqueEntityByPropNames(new String[] { "organization",
							"riskCategory", "period" }, new Object[] {
							organization, rc, period });
			if (extra != null) {// 有补报则判断补报状态和补报周期
				if (!extra.isAudit())
					throw new ReportResultException(104, "补报申请未批准");
				Date begin = extra.getBeginTime();
				Date end = extra.getEndTime();
				if (DateUtil.getShortDate(begin).compareTo(DateUtil.getShortDate(now)) > 0||DateUtil.getShortDate(end).compareTo(DateUtil.getShortDate(now)) < 0) {
					throw new ReportResultException(101, "不在补报时间段内，补报时间段为"
							+ DateUtil.getShortDate(begin) + "~"
							+ DateUtil.getShortDate(end));
				}
				isExtra=true;
			} else {//没有补报则直接报错
				Date start = (Date) check.get(1);
				Date end = (Date) check.get(2);
				throw new ReportResultException(101, "不在指标上报时间段内，上报时间段为"
						+ DateUtil.getShortDate(start) + "~"
						+ DateUtil.getShortDate(end));
			}
		}
        return isExtra;
	}

	/**
	 * 验证当前时间是否在报送周期内
	 * 
	 * @param category
	 * @param year
	 * @param peroid
	 * @return
	 */
	public static List<Object> checkRange(RiskCategory category,Date reportDate) {
		List<Object> list = new ArrayList<Object>();
//		if(true){
//			list.add(true);
//			return list;
//		}
		int endDay=category.getEndDay();
		int startDay=category.getStartDay();
		Date now = new Date();
		Date start = DateUtils.addDays(reportDate, startDay+1);
		Date end = DateUtils.addDays(reportDate, endDay);
		if (DateUtil.getShortDate(start).compareTo(DateUtil.getShortDate(now)) > 0||DateUtil.getShortDate(end).compareTo(DateUtil.getShortDate(now)) < 0) {
			list.add(false);
			list.add(start);
			list.add(end);
		} else {
			list.add(true);
		}
		return list;
	}
	
	/**
	 * 计算每期的期末日期(周末、月末)
	 * 
	 * @param category
	 * @param year
	 * @param peroid
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Date getNewDate_end(RiskCategory category,Date reportDate) {
		Date newDate=reportDate;
		if (category.getCycle() == 1) {// 周期为日
			newDate=reportDate;
		} else if (category.getCycle() == 2) {// 周期为周 每年1月1日开始为第一周开始			
			Calendar report=Calendar.getInstance();
			report.setTime(reportDate);
			int reportDay=report.get(Calendar.DAY_OF_WEEK);
			if(reportDay==Calendar.SUNDAY){
				newDate=reportDate;
			}else{
				newDate = DateUtils.addDays(reportDate, 8-reportDay);
			}			
		} else if (category.getCycle() == 3) {// 周期为月
			Calendar report=Calendar.getInstance();
			report.setTime(reportDate);
			int month=report.get(Calendar.MONDAY);
			Date nextMonthDate;
			if(month==11){
				nextMonthDate = new Date(reportDate.getYear() + 1, 0, 1, 0, 0, 0);//下年第一天
			}else{
				nextMonthDate=new Date(reportDate.getYear(), month+1, 1, 0, 0, 0);// 下月第一天
			}
			newDate = DateUtils.addDays(nextMonthDate, -1);
		}
		return newDate;
	}
	
	/**
	 * 计算上期期末日期(周末、月末)
	 * 
	 * @param category
	 * @param year
	 * @param peroid
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Date getNewDate_last(RiskCategory category,Date reportDate) {
		Date newDate=reportDate;
		if (category.getCycle() == 1) {// 周期为日
			newDate=reportDate;
		} else if (category.getCycle() == 2) {// 周期为周 每年1月1日开始为第一周开始			
			Calendar report=Calendar.getInstance();
			report.setTime(reportDate);
			int reportDay=report.get(Calendar.DAY_OF_WEEK);
			if(reportDay==Calendar.SUNDAY){
				newDate=reportDate;
			}else{
				newDate = DateUtils.addDays(reportDate, -reportDay+1);
			}			
		} else if (category.getCycle() == 3) {// 周期为月
			Calendar report=Calendar.getInstance();
			report.setTime(reportDate);
			Calendar report2=Calendar.getInstance();
			report2.setTime(DateUtils.addDays(reportDate, 1));
			if(report2.get(Calendar.MONTH)!=report.get(Calendar.MONDAY)){//月末
				newDate=reportDate;
			}else{
				newDate=DateUtils.addDays(new Date(reportDate.getYear(), reportDate.getMonth(), 1, 0, 0, 0), -1);
			}
		}
		return newDate;
	}
	
	/**
	 * 根据指标查询条件获取HQL
	 * @param param
	 * @return
	 */
	public static String getHqlByQueryParam(ReportQueryParam param, UserService userService,Class clazz,RiskCategoryService riskCategoryService,boolean riskCodeLike,boolean checkControl) {
		String hql = "select distinct a from " + clazz.getSimpleName() + " a";
		List<String> list = new ArrayList<String>();
		Date startDate=param.getStartDate();
		Date endDate=param.getEndDate();	
		Date exactDate=param.getExactDate();	
		if(startDate!=null&&endDate!=null&&startDate.after(endDate))//如果起始日期大于截止日期，两者的period可能相同 为了避免歧义，还是不显示该期的查询结果
			return hql+" where 1=0";
		if (startDate != null) {
			list.add("a.reportDate>='"+DateUtil.getShortDate(startDate)+"'");
		}
		if (endDate != null) {
			list.add("a.reportDate<='"+DateUtil.getShortDate(endDate)+"'");
		}	
		if (exactDate != null) {
			RiskCategory rc=riskCategoryService.getRiskCategoryByRiskClass(clazz);
			int period=RiskUtils.getPeroidByReportDate(rc, exactDate);
			list.add("a.period='"+period+"' and a.dataYear='"+exactDate.getYear()+"'");
		}
		if (StringUtils.isNotEmpty(param.getReportType())&&riskCodeLike)
			list.add("a.riskCode like '" + param.getReportType() + "%'");
		if (StringUtils.isNotEmpty(param.getReportType())&&!riskCodeLike)
			list.add("a.riskCode='" + param.getReportType() + "'");
		if (param.getDepartmentId() != 0)//指定机构为商行
			list.add("a.organization.orgId='" + param.getDepartmentId() + "'");
		else{//未指定机构则根据User自身机构是银监会还是商行来查询
			Subject user = SecurityUtils.getSubject();
			String username=(String)user.getPrincipal();  
			User u = userService.getUserByUserName(username);
			if(u.getOrganization().getCategory()!=1){
				//商行根据组织机构来查询
				list.add("a.organization.orgId='" + u.getOrganization().getOrgId() + "'");
			}else{
				//银监会根据区域查询
				list.add("a.areaCode like '" + u.getOrganization().getAreaCity().getAreeCode() + "%'");
			}
			
		}
		if (list.size() == 4) {
			hql = hql + " where " + list.get(0) + " and " + list.get(1) + " and " + list.get(2)+ " and " + list.get(3);
		}
		if (list.size() == 3) {
			hql = hql + " where " + list.get(0) + " and " + list.get(1) + " and " + list.get(2);
		}
		if (list.size() == 2) {
			hql = hql + " where " + list.get(0) + " and " + list.get(1);
		}
		if (list.size() == 1) {
			hql = hql + " where " + list.get(0);
		}
		if(checkControl){
			hql+=" and a.organization.canControl='1'";
		}
		hql+=" order by a.reportDate desc";
		return hql;
	}
	
	/**
	 * 根据指标查询条件获取HQL
	 * 
	 * @param param
	 * @return
	 */
	public static String getHqlByQueryParam(AnalyseQueryParam param, UserService userService, Class clazz,
			RiskCategoryService riskCategoryService) {
		Map<Class,String> orderByMap=new HashMap<Class,String>();
		orderByMap.put(SystemAvailableRate.class, "1-(a.ud+a.pd)/a.ltsp");
		orderByMap.put(SystemTransactionSuccessRate.class, "a.aosst/a.aost");
		orderByMap.put(InfoTechnologyRiskEventCount.class, "a.count");
		orderByMap.put(FakeSiteAttachmentRate.class, "a.cfw/a.aofw");
		orderByMap.put(OperationChangeSuccessRate.class, "a.sdc/a.dc");
		orderByMap.put(OutsideAttackChangeRate.class, "(a.idswp+a.ipswp)/(a.ldswp+a.lpswp)");
		orderByMap.put(ElectronicActiveUserChangeRate.class, "a.currentActiveUser/a.lastActiveUser");
		orderByMap.put(ElectronicTransactionChangeRate.class, "a.currentTradingVolume/a.lastTradingVolume");
		
		String hql = "select distinct a from " + clazz.getSimpleName() + " a";
		List<String> list = new ArrayList<String>();
		Date startDate = param.getStartDate();
		Date endDate = param.getEndDate();
		if(startDate!=null&&endDate!=null&&startDate.after(endDate))//如果起始日期大于截止日期，两者的period可能相同 为了避免歧义，还是不显示该期的查询结果
			return hql+" where 1=0";
		if (startDate != null) {
			list.add("a.reportDate>='"+DateUtil.getShortDate(startDate)+"'");
		}
		if (endDate != null) {
			list.add("a.reportDate<='"+DateUtil.getShortDate(endDate)+"'");
		}
//		if (param.getCategory() != 0) {
//			list.add("a.organization.category='" + param.getCategory() + "'");
//		}
		int recentPeriod=param.getRecentPeriod();
		if(recentPeriod!=0){
			
		}
		if(CollectionUtils.isNotEmpty(param.getDepartmentIdList())){
			list.add("a.organization.orgId in ("+ConvertUtils.convertList2String(param.getDepartmentIdList(), ",")+")");
		}else{			
//			// 未指定机构则根据User自身机构是银监会还是商行来查询
//			Subject user = SecurityUtils.getSubject();
//			String username = (String) user.getPrincipal();
//			User u = userService.getUserByUserName(username);
//			if (u.getOrganization().getCategory() != 1) {
//				// 商行根据组织机构来查询
//				list.add("a.organization.orgId='" + u.getOrganization().getOrgId() + "'");
//			} else {
//				// 银监会根据区域查询
//				list.add("a.areaCode like '" + u.getOrganization().getAreaCity().getAreeCode() + "%'");
//			}
		}
		
		if(CollectionUtils.isNotEmpty(param.getReportTypeList())){
			list.add("a.riskCode='"+param.getReportTypeList().get(0)+"'");
		}
		
		if (StringUtils.isNoneBlank(param.getAreaCode())) {
			list.add("a.areaCode like '" + param.getAreaCode() + "%'");
		}
		
		if (list.size() == 5) {
			hql = hql + " where " + list.get(0) + " and " + list.get(1) + " and " + list.get(2) + " and " + list.get(3)
					+ " and " + list.get(4);
		}
		if (list.size() == 4) {
			hql = hql + " where " + list.get(0) + " and " + list.get(1) + " and " + list.get(2) + " and " + list.get(3);
		}
		if (list.size() == 3) {
			hql = hql + " where " + list.get(0) + " and " + list.get(1) + " and " + list.get(2);
		}
		if (list.size() == 2) {
			hql = hql + " where " + list.get(0) + " and " + list.get(1);
		}
		if (list.size() == 1) {
			hql = hql + " where " + list.get(0);
		}
		hql+=" and a.organization.canControl='1'";
		if (param.getSortType() == 1) {
			hql += " order by a.reportDate desc";
		} else if (param.getSortType() == 2) {
			hql += " order by a.organization.orgId asc";
		} else if (param.getSortType() == 3) {
			hql += " order by a.riskCode asc";
		} else if (param.getSortType() == 4) {
			hql += " order by "+orderByMap.get(clazz)+" desc";
		} else if (param.getSortType() == 5) {
			hql += " order by "+orderByMap.get(clazz)+" asc";
		}
		return hql;
	}
	
	/**
	 * 根据指标查询条件获取HQL
	 * @param param
	 * @return
	 */
	public static String getHqlByQueryParam(ReportQueryParam param, UserService userService) {
		String hql = "select distinct a from ExtraReportInfo a";
		List<String> list = new ArrayList<String>();		
		if (param.getDepartmentId() != 0)//指定机构为商行
			list.add("a.organization.orgId='" + param.getDepartmentId() + "'");
		else{//未指定机构则根据User自身机构是银监会还是商行来查询
			Subject user = SecurityUtils.getSubject();
			String username=(String)user.getPrincipal();  
			User u = userService.getUserByUserName(username);
			if(u.getOrganization().getCategory()!=1){
				//商行根据组织机构来查询
				list.add("a.organization.orgId='" + u.getOrganization().getOrgId() + "'");
			}else{
				//银监会根据区域查询
				list.add("a.organization.areaCity.areeCode like '" + u.getOrganization().getAreaCity().getAreeCode() + "%'");
			}			
		}
		hql = hql + " where " + list.get(0);
		return hql;
	}
	
	/**
	 * 根据指标查询条件获取HQL
	 * @param param
	 * @return
	 */	
	public static String getHqlByQueryParam(AssetQueryParam param, UserService userService,Class clazz) {
		String hql = "select distinct a from " + clazz.getSimpleName() + " a";
		List<String> list = new ArrayList<String>();
		if (StringUtils.isNotEmpty(param.getUniqueVal()))
			list.add("a.uniqueVal  like '%" + param.getUniqueVal() + "%'");
		if (StringUtils.isNotEmpty(param.getName())) {
			String nameProperty = "name";
			if (clazz.equals(MiddlewareInfo.class))
				nameProperty = "middlewareName";
			else if (clazz.equals(DatabaseInfo.class))
				nameProperty = "databaseName";
			list.add("a." + nameProperty + " like '%" + param.getName() + "%'");
		}
		if (param.getOrganizationId() != 0)// 指定机构为商行
			list.add("a.organization.orgId='" + param.getOrganizationId() + "'");
		else {//未指定机构则根据User自身机构是银监会还是商行来查询
			Subject user = SecurityUtils.getSubject();
			String username=(String)user.getPrincipal();  
			User u = userService.getUserByUserName(username);
			if(u.getOrganization().getCategory()!=1){
				//商行根据组织机构来查询
				list.add("a.organization.orgId='" + u.getOrganization().getOrgId() + "'");
			}else{
				//银监会根据区域查询
				list.add("a.areaCode like '" + u.getOrganization().getAreaCity().getAreeCode() + "%'");
			}			
		}
		if (list.size() == 3) {
			hql = hql + " where " + list.get(0) + " and " + list.get(1) + " and " + list.get(2);
		}
		if (list.size() == 2) {
			hql = hql + " where " + list.get(0) + " and " + list.get(1);
		}
		if (list.size() == 1) {
			hql = hql + " where " + list.get(0);
		}
		return hql;
	}
	
	public static Organization checkKeyAndGetOrganization(String jsonString,OrganizationService organizationService){
		JSONObject json = JSONObject.fromObject(jsonString);
		String key = (String) json.get("key");
		String organCode = (String) json.get("organization");
		long time = (Long) json.get("time");		
		return checkKeyAndGetOrganization(key, organCode, time, organizationService);
	}
	
	public static Organization checkKeyAndGetOrganization(String key,String organNo,long time,OrganizationService organizationService){
		//校验KEY
		Organization organization=organizationService.getOrganizationByNo(organNo);
		if(organization==null){
			throw new ReportResultException(107, "机构码不存在 ");
		}
		String totalKey=organNo+":"+organization.getSecretKey()+":"+time;
		String rightKey=EncodeUtils.ecodeByMD5(totalKey);
		if(!rightKey.equals(key)){
			throw new ReportResultException(1, "用户认证失败，机构号与对应key不匹配");
		}
		return organization;
	}
	
	public static Set<AppSystem> getAppSystemList(String appSystems,AppSystemService appSystemService){
		if(StringUtils.isEmpty(appSystems))
			return null;
		String[] appNames = appSystems.split(",");
		Set<AppSystem> list = new HashSet<AppSystem>();
		for (String name : appNames) {
			AppSystem as = appSystemService.getAppSystemByName(name);
			if (as == null)
				throw new ReportResultException(201, "支撑的应用系统不存在:" + name);
			list.add(as);
		}
		return list;
	}
	
	public static void changeJsonToBo(JSONObject object, Object bo) throws Exception {
		for (Object str : object.keySet()) {
			String propertyName = (String) str;
			Class tempClass=bo.getClass().getDeclaredField(propertyName).getType();
			if ( tempClass== Date.class||tempClass==Timestamp.class) {
				PropertyUtils.setProperty(bo, propertyName, DateUtil.SHORT_FORMAT.parseObject(object.getString(propertyName)));
			} else {
				PropertyUtils.setProperty(bo, propertyName, object.get(str));
			}
		}
	}
	
	public static String getRiskCategorys(List<RiskCategory> list) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append("{'id':'10000','parent':'#',text:'所有'},");
		for (RiskCategory rc : list) {
			String code = rc.getRiskCode();
			String rcName = rc.getRiskName();
			String parentCode = getParentCode(code);
			if (parentCode.equals(""))
				sb.append("{'id':'" + code + "','parent':'" + 10000 + "','text':'" + rcName + "'},");
			else
				sb.append("{'id':'" + code + "','parent':'" + parentCode + "','text':'" + rcName + "'},");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		return sb.toString();
	}
	
	public static String getJsonApps(AppSystemService appSystemService,Set<AppSystem> sets){
    	List<AppSystem> apps =appSystemService.getAllAppSystems();
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append("{'id':'10000','parent':'#',text:'所有'},");
		if (sets == null)
			sets = new HashSet<AppSystem>();
		for (AppSystem app : apps) {
			if (ConvertUtils.getPropertyList(sets, "appId", Long.class).contains(app.getAppId()))
				sb.append("{'id':'" + app.getAppName() + "','parent':'" + 10000 + "','text':'" + app.getAppName()
						+ "','state':{'selected':'true'}},");
			else
				sb.append("{'id':'" + app.getAppName() + "','parent':'" + 10000 + "','text':'" + app.getAppName()
						+ "'},");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		return sb.toString();
	}
	
	public static String getJsonOrganizations(OrganizationService organizationService, String areaCode,int category,List<Integer> orgIds){
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append("{'id':'10000','parent':'#',text:'所有'},");
		if (orgIds == null)
			orgIds = new ArrayList<Integer>();
		
		Page<Organization> page=new Page<Organization>();
		page.setPageSize(Integer.MAX_VALUE);
		List<Organization> list=organizationService.getPage(category, areaCode, null, page,true).getResult();
		for (Organization app : list) {
			if (ConvertUtils.getPropertyList(list, "orgId", Long.class).contains(app.getOrgId()))
				sb.append("{'id':'" + app.getOrgId() + "','parent':'" + 10000 + "','text':'" + app.getName()
						+ "','state':{'selected':'true'}},");
			else
				sb.append("{'id':'" + app.getOrgId() + "','parent':'" + 10000 + "','text':'" + app.getName()
						+ "'},");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		return sb.toString();
	}
	
	public static String getParentCode(String code) {
		if(code.equals("6001"))
			return "";
		else if (code.length() >= 3)
			return code.substring(0, code.length() - 3);
		else
			return "";
	}
	
	public static boolean containRoles(Set<Role> set1,Set<Role> set2){
		Set<String> permissions1=new HashSet<String>();
		Set<String> permissions2=new HashSet<String>();
		for(Role role:set1){
			for(Permission p1:role.getPermissions()){
				permissions1.add(p1.getPermissionName());
			}
		}
		for(Role role:set2){
			for(Permission p2:role.getPermissions()){
				permissions2.add(p2.getPermissionName());
			}
		}
		List<String> specialPermissions=ConvertUtils.newArrayList("message:view","risk:add","risk:import","asset:add");
		permissions2.removeAll(specialPermissions);
		return permissions1.containsAll(permissions2);
	}
	
	public static boolean containPermissions(Set<Permission> currentSet, Set<Permission> newSet) {
		Set<String> permissions1 = new HashSet<String>();
		Set<String> permissions2 = new HashSet<String>();
		for (Permission p1 : currentSet) {
			permissions1.add(p1.getPermissionName());
		}
		for (Permission p1 : newSet) {
			permissions2.add(p1.getPermissionName());
		}
		List<String> specialPermissions = ConvertUtils.newArrayList("message:view", "risk:add", "risk:import",
				"asset:add");
		permissions2.removeAll(specialPermissions);
		return permissions1.containsAll(permissions2);
	}
	
	public static void main(String[] args) throws ParseException {
		System.out.println( Integer.parseInt(new java.text.DecimalFormat("0").format(3.1)));
		System.out.println( Integer.parseInt(new java.text.DecimalFormat("0").format(100.0)));
		System.out.println(new Date().getTime());
		Date temp1 = new Date(2014 - 1900,0, 1, 0, 0, 0);
		Date temp2 = new Date(2014 - 1900, 0, 5, 0, 0, 0);
		Date temp3 = new Date(2014 - 1900, 0, 6, 0, 0, 0);
		Date temp4 = new Date(2013 - 1900, 9, 3, 0, 0, 0);
		Calendar c=Calendar.getInstance();
		c.setTime(temp1);
		System.out.println(c.get(Calendar.WEEK_OF_YEAR));
		System.out.println(c.get(Calendar.DAY_OF_WEEK));
		c.setTime(temp2);
		System.out.println(c.get(Calendar.WEEK_OF_YEAR));
		System.out.println(c.get(Calendar.DAY_OF_WEEK));
		c.setTime(temp3);
		System.out.println(c.get(Calendar.WEEK_OF_YEAR));
		System.out.println(c.get(Calendar.DAY_OF_WEEK));
		c.setTime(temp4);
		System.out.println(c.get(Calendar.WEEK_OF_YEAR));
		System.out.println(c.get(Calendar.DAY_OF_WEEK));
		System.out.println("a,b,c".split(","));
		System.out.println(new Date().getTime()/(24*1000*3600));
		System.out.println((DateUtil.SHORT_FORMAT.parse("2014-10-23").getTime()+10000)/(24*1000*3600));
		System.out.println(new Date(16365*24*1000*3600));
		System.out.println(DateUtil.getShortDate(DateUtil.SHORT_FORMAT.parse("2014-01-01")));
		System.out.println(new Date(1414524057941L));
		System.out.println(new Date(1414524057941L-720 * 60 * 1000));
		System.out.println(new Date().getTimezoneOffset());
	}
}
