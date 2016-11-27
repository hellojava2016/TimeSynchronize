package cn.gov.cbrc.bankriskcontrol.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import cn.gov.cbrc.bankriskcontrol.dto.ReportResultException;
import cn.gov.cbrc.bankriskcontrol.entity.ElectronicActiveUserChangeRate;
import cn.gov.cbrc.bankriskcontrol.entity.ElectronicTransactionChangeRate;
import cn.gov.cbrc.bankriskcontrol.entity.FakeSiteAttachmentRate;
import cn.gov.cbrc.bankriskcontrol.entity.InfoTechnologyRiskEventCount;
import cn.gov.cbrc.bankriskcontrol.entity.OperationChangeSuccessRate;
import cn.gov.cbrc.bankriskcontrol.entity.Organization;
import cn.gov.cbrc.bankriskcontrol.entity.OutsideAttackChangeRate;
import cn.gov.cbrc.bankriskcontrol.entity.RiskCategory;
import cn.gov.cbrc.bankriskcontrol.entity.SystemAvailableRate;
import cn.gov.cbrc.bankriskcontrol.entity.SystemTransactionSuccessRate;
import cn.gov.cbrc.bankriskcontrol.entity.User;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.risk.ElectronicActiveUserChangeRateService;
import cn.gov.cbrc.bankriskcontrol.service.risk.ElectronicTransactionChangeRateService;
import cn.gov.cbrc.bankriskcontrol.service.risk.FakeSiteAttachmentRateService;
import cn.gov.cbrc.bankriskcontrol.service.risk.InfoTechnologyRiskEventCountService;
import cn.gov.cbrc.bankriskcontrol.service.risk.OperationChangeSuccessRateService;
import cn.gov.cbrc.bankriskcontrol.service.risk.OutsideAttackChangeRateService;
import cn.gov.cbrc.bankriskcontrol.service.risk.SystemAvailableRateService;
import cn.gov.cbrc.bankriskcontrol.service.risk.SystemTransactionSuccessRateService;
import cn.gov.cbrc.bankriskcontrol.service.system.OrganizationService;
import cn.gov.cbrc.bankriskcontrol.service.system.RiskCategoryService;
import cn.gov.cbrc.bankriskcontrol.util.DateUtil;
import cn.gov.cbrc.bankriskcontrol.util.RiskUtils;

/**
 * 监控指标上报服务
 * @author pl
 *
 */
//http://www.jdon.com/soa/restful.html
@Path("/")
@Produces("application/json")
public class ReportWebService {
	@Autowired
	private RiskCategoryService riskCategoryService;
	
	@Autowired
	private SystemAvailableRateService systemAvailableRateService;
	
	@Autowired
	private SystemTransactionSuccessRateService systemTransactionSuccessRateService;
	
	@Autowired
	private OperationChangeSuccessRateService operationChangeSuccessRateService;
	
	@Autowired
	private FakeSiteAttachmentRateService fakeSiteAttachmentRateService;
	
	@Autowired
	private OutsideAttackChangeRateService outsideAttackChangeRateService;
	
	@Autowired
	private InfoTechnologyRiskEventCountService infoTechnologyRiskEventCountService;
	
	@Autowired
	private ElectronicTransactionChangeRateService electronicTransactionChangeRateService;
	
	@Autowired
	private ElectronicActiveUserChangeRateService electronicActiveUserChangeRateService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrganizationService organizationService;
	
	/**
	 * 系统可用率上报
	 * @param jsonString
	 * @return
	 */
	@POST
	@Path("/systemavilabelrate")
	@Consumes("application/json")
	public Response reportSystemAvilabelRateItems(String jsonString) {
		try {
			Organization organization = RiskUtils.checkKeyAndGetOrganization(jsonString,organizationService);
			User user = userService.getUserByOrganizationCode(organization.getOrgNo());
			// 处理上报
			JSONObject json = JSONObject.fromObject(jsonString);
			JSONArray array = (JSONArray) json.get("items");
			Object[] objects = array.toArray();
			List<SystemAvailableRate> list=new ArrayList<SystemAvailableRate>();
			for (Object object : objects) {
				JSONObject jobject=(JSONObject) object;		
				SystemAvailableRate rate = new SystemAvailableRate();
				rate.setRiskCode(jobject.getString("riskCode"));
				rate.setReportDate(DateUtil.SHORT_FORMAT.parse(jobject.getString("reportDate")));
				rate.setUd(jobject.getInt("uost"));
				rate.setLtsp(jobject.getInt("tst"));
				rate.setPd(jobject.getInt("sost"));
				RiskCategory rc = riskCategoryService.getRiskCategoryByCode(rate.getRiskCode());
				if(rc==null)
					throw new ReportResultException(100, "指标代码不存在："+rate.getRiskCode());
				rate.setRiskCategory(rc);				
				rate.setUser(user);
				rate.setOrganization(organization);
				rate.setOrgCode(organization.getOrgNo());
				list.add(rate);
			}
			systemAvailableRateService.reportSystemAvailableRates(list);
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch(Exception e){
			return Response.ok(JSONObject.fromObject(new ReportResultException(1000, "异常"+e.getMessage()).getReportResult()).toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}

	/**
	 * 系统交易成功率上报
	 * @param jsonString
	 * @return
	 */
	@POST
	@Path("/systemtransactionsuccessrate")
	@Consumes("application/json")
	public Response reportSystemTransactionSuccessRateItems(String jsonString) {
		try {
			Organization organization = RiskUtils.checkKeyAndGetOrganization(jsonString,organizationService);
			User user = userService.getUserByOrganizationCode(organization.getOrgNo());
			// 处理上报
			JSONObject json = JSONObject.fromObject(jsonString);
			JSONArray array = (JSONArray) json.get("items");
			Object[] objects = array.toArray();
			List<SystemTransactionSuccessRate> list=new ArrayList<SystemTransactionSuccessRate>();
			for (Object object : objects) {
				JSONObject jobject=(JSONObject) object;	
				SystemTransactionSuccessRate rate = new SystemTransactionSuccessRate();
				rate.setRiskCode(jobject.getString("riskCode"));
				rate.setReportDate(DateUtil.SHORT_FORMAT.parse(jobject.getString("reportDate")));
				rate.setAosst(jobject.getInt("nst"));
				rate.setAost(jobject.getInt("tnt"));
				RiskCategory rc = riskCategoryService.getRiskCategoryByCode(rate.getRiskCode());
				if(rc==null)
					throw new ReportResultException(100, "指标代码不存在："+rate.getRiskCode());
				rate.setRiskCategory(rc);
				rate.setUser(user);
				rate.setOrganization(organization);
				rate.setOrgCode(organization.getOrgNo());
				list.add(rate);
			}
			systemTransactionSuccessRateService.reportSystemTransactionSuccessRates(list);
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch(Exception e){
			return Response.ok(JSONObject.fromObject(new ReportResultException(1000, "异常"+e.getMessage()).getReportResult()).toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}

	/**
	 * 投产变更成功率上报
	 * @param jsonString
	 * @return
	 */
	@POST
	@Path("/deploymentchangesuccessrate")
	@Consumes("application/json")
	public Response reportDeploymentChangeSuccessRateItems(String jsonString) {
		try {
			Organization organization = RiskUtils.checkKeyAndGetOrganization(jsonString,organizationService);
			User user = userService.getUserByOrganizationCode(organization.getOrgNo());
			// 处理上报
			JSONObject json = JSONObject.fromObject(jsonString);
			JSONArray array = (JSONArray) json.get("items");
			Object[] objects = array.toArray();
			List<OperationChangeSuccessRate> list=new ArrayList<OperationChangeSuccessRate>();
			for (Object object : objects) {
				JSONObject jobject=(JSONObject) object;	
				OperationChangeSuccessRate rate = new OperationChangeSuccessRate();
				rate.setRiskCode(jobject.getString("riskCode"));
				rate.setReportDate(DateUtil.SHORT_FORMAT.parse(jobject.getString("reportDate")));
				rate.setDc(jobject.getInt("nsdc"));
				rate.setSdc(jobject.getInt("tndc"));
				RiskCategory rc = riskCategoryService.getRiskCategoryByCode(rate.getRiskCode());
				if(rc==null)
					throw new ReportResultException(100, "指标代码不存在："+rate.getRiskCode());
				rate.setRiskCategory(rc);
				rate.setUser(user);
				rate.setOrganization(organization);
				rate.setOrgCode(organization.getOrgNo());
				list.add(rate);
			}
			operationChangeSuccessRateService.reportOperationChangeSuccessRates(list);
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch(Exception e){
			return Response.ok(JSONObject.fromObject(new ReportResultException(1000, "异常"+e.getMessage()).getReportResult()).toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}

	/**
	 * 假冒网站查封率上报
	 * @param jsonString
	 * @return
	 */
	@POST
	@Path("/closedfishingwebsiterate")
	@Consumes("application/json")  
	public Response reportClosedFishingWebsiteRateItems(String jsonString) {
		try {
			Organization organization = RiskUtils.checkKeyAndGetOrganization(jsonString,organizationService);
			User user = userService.getUserByOrganizationCode(organization.getOrgNo());
			// 处理上报
			JSONObject json = JSONObject.fromObject(jsonString);
			JSONArray array = (JSONArray) json.get("items");
			Object[] objects = array.toArray();
			List<FakeSiteAttachmentRate> list=new ArrayList<FakeSiteAttachmentRate>();
			for (Object object : objects) {
				JSONObject jobject=(JSONObject) object;	
				FakeSiteAttachmentRate rate = new FakeSiteAttachmentRate();
				rate.setRiskCode(jobject.getString("riskCode"));
				rate.setReportDate(DateUtil.SHORT_FORMAT.parse(jobject.getString("reportDate")));
				rate.setAofw(jobject.getInt("nfw"));
				rate.setCfw(jobject.getInt("ncfw"));
				RiskCategory rc = riskCategoryService.getRiskCategoryByCode(rate.getRiskCode());
				if(rc==null)
					throw new ReportResultException(100, "指标代码不存在："+rate.getRiskCode());
				rate.setRiskCategory(rc);
				rate.setUser(user);
				rate.setOrganization(organization);
				rate.setOrgCode(organization.getOrgNo());
				list.add(rate);
			}
			fakeSiteAttachmentRateService.reportFakeSiteAttachmentRates(list);
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch(Exception e){
			return Response.ok(JSONObject.fromObject(new ReportResultException(1000, "异常"+e.getMessage()).getReportResult()).toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}

	/**
	 * 外部攻击变化率上报
	 * @param jsonString
	 * @return
	 */
	@POST
	@Path("/externalattackchangerate")
	@Consumes("application/json")
	public Response reportExternalAttackChangeRateItems(String jsonString) {
		try {
			Organization organization = RiskUtils.checkKeyAndGetOrganization(jsonString,organizationService);
			User user = userService.getUserByOrganizationCode(organization.getOrgNo());
			// 处理上报
			JSONObject json = JSONObject.fromObject(jsonString);
			JSONArray array = (JSONArray) json.get("items");
			Object[] objects = array.toArray();
			List<OutsideAttackChangeRate> list=new ArrayList<OutsideAttackChangeRate>();
			for (Object object : objects) {
				JSONObject jobject=(JSONObject) object;	
				OutsideAttackChangeRate rate = new OutsideAttackChangeRate();
				rate.setRiskCode(jobject.getString("riskCode"));
				rate.setReportDate(DateUtil.SHORT_FORMAT.parse(jobject.getString("reportDate")));
				rate.setIdswp(jobject.getInt("nidscp"));
				rate.setIpswp(jobject.getInt("nipscp"));
				RiskCategory rc = riskCategoryService.getRiskCategoryByCode(rate.getRiskCode());
				if(rc==null)
					throw new ReportResultException(100, "指标代码不存在："+rate.getRiskCode());
				rate.setRiskCategory(rc);
				rate.setUser(user);
				rate.setOrganization(organization);
				rate.setOrgCode(organization.getOrgNo());
				list.add(rate);
			}
			outsideAttackChangeRateService.reportOutsideAttackChangeRates(list);
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch(Exception e){
			return Response.ok(JSONObject.fromObject(new ReportResultException(1000, "异常"+e.getMessage()).getReportResult()).toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}

	/**
	 * 信息科技风险事件数量上报
	 * @param jsonString
	 * @return
	 */
	@POST
	@Path("/informationriskevent")
	@Consumes("application/json")
	public Response reportInformationRiskEventItems(String jsonString) {
		try {
			Organization organization = RiskUtils.checkKeyAndGetOrganization(jsonString,organizationService);
			User user = userService.getUserByOrganizationCode(organization.getOrgNo());
			// 处理上报
			JSONObject json = JSONObject.fromObject(jsonString);
			JSONArray array = (JSONArray) json.get("items");
			Object[] objects = array.toArray();
			List<InfoTechnologyRiskEventCount> list=new ArrayList<InfoTechnologyRiskEventCount>();
			for (Object object : objects) {
				JSONObject jobject=(JSONObject) object;	
				InfoTechnologyRiskEventCount rate = new InfoTechnologyRiskEventCount();
				rate.setRiskCode(jobject.getString("riskCode"));
				rate.setReportDate(DateUtil.SHORT_FORMAT.parse(jobject.getString("reportDate")));
				rate.setCount(jobject.getInt("count"));
				RiskCategory rc = riskCategoryService.getRiskCategoryByCode(rate.getRiskCode());
				if(rc==null)
					throw new ReportResultException(100, "指标代码不存在："+rate.getRiskCode());
				rate.setRiskCategory(rc);
				rate.setUser(user);
				rate.setOrganization(organization);
				rate.setOrgCode(organization.getOrgNo());
				list.add(rate);
			}
			infoTechnologyRiskEventCountService.reportInfoTechnologyRiskEventCounts(list);
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch(Exception e){
			return Response.ok(JSONObject.fromObject(new ReportResultException(1000, "异常"+e.getMessage()).getReportResult()).toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}
	
	/**
	 * 主要电子渠道交易变化率上报
	 * @param jsonString
	 * @return
	 */
	@POST
	@Path("/electronicchanneltransactionchangerate")
	@Consumes("application/json")
	public Response reportMainElectronicChannelTransactionChangeRateItems(String jsonString) {
		try {
			Organization organization = RiskUtils.checkKeyAndGetOrganization(jsonString,organizationService);
			User user = userService.getUserByOrganizationCode(organization.getOrgNo());
			// 处理上报
			JSONObject json = JSONObject.fromObject(jsonString);
			JSONArray array = (JSONArray) json.get("items");
			Object[] objects = array.toArray();
			List<ElectronicTransactionChangeRate> list=new ArrayList<ElectronicTransactionChangeRate>();
			for (Object object : objects) {
				JSONObject jobject=(JSONObject) object;	
				ElectronicTransactionChangeRate rate = new ElectronicTransactionChangeRate();
				rate.setRiskCode(jobject.getString("riskCode"));
				rate.setReportDate(DateUtil.SHORT_FORMAT.parse(jobject.getString("reportDate")));
				rate.setCurrentTradingVolume(jobject.getInt("ntcp"));
				RiskCategory rc = riskCategoryService.getRiskCategoryByCode(rate.getRiskCode());
				if(rc==null)
					throw new ReportResultException(100, "指标代码不存在："+rate.getRiskCode());
				rate.setRiskCategory(rc);
				rate.setUser(user);
				rate.setOrganization(organization);
				rate.setOrgCode(organization.getOrgNo());
				list.add(rate);
			}
			electronicTransactionChangeRateService.reportElectronicTransactionChangeRates(list);
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch(Exception e){
			return Response.ok(JSONObject.fromObject(new ReportResultException(1000, "异常"+e.getMessage()).getReportResult()).toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}
	
	/**
	 * 主要电子渠道活跃用户、账户变化率上报
	 * @param jsonString
	 * @return
	 */
	@POST
	@Path("/electronicbankactiveuserchangerate")
	@Consumes("application/json")
	public Response reportMainElectronicBankActiveUserChangeRateItems(String jsonString) {
		try {
			Organization organization = RiskUtils.checkKeyAndGetOrganization(jsonString,organizationService);
			User user = userService.getUserByOrganizationCode(organization.getOrgNo());
			// 处理上报
			JSONObject json = JSONObject.fromObject(jsonString);
			JSONArray array = (JSONArray) json.get("items");
			Object[] objects = array.toArray();
			List<ElectronicActiveUserChangeRate> list=new ArrayList<ElectronicActiveUserChangeRate>();
			for (Object object : objects) {
				JSONObject jobject=(JSONObject) object;	
				ElectronicActiveUserChangeRate rate = new ElectronicActiveUserChangeRate();
				rate.setRiskCode(jobject.getString("riskCode"));
				rate.setReportDate(DateUtil.SHORT_FORMAT.parse(jobject.getString("reportDate")));
				rate.setCurrentActiveUser(jobject.getInt("naucp"));
				RiskCategory rc = riskCategoryService.getRiskCategoryByCode(rate.getRiskCode());
				if(rc==null)
					throw new ReportResultException(100, "指标代码不存在："+rate.getRiskCode());
				rate.setRiskCategory(rc);
				rate.setUser(user);
				rate.setOrganization(organization);
				rate.setOrgCode(organization.getOrgNo());
				list.add(rate);
			}
			electronicActiveUserChangeRateService.reportElectronicActiveUserChangeRates(list);
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch(Exception e){
			return Response.ok(JSONObject.fromObject(new ReportResultException(1000, "异常"+e.getMessage()).getReportResult()).toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}
}
