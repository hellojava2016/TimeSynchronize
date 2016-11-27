package cn.gov.cbrc.bankriskcontrol.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import cn.gov.cbrc.bankriskcontrol.dto.ReportResultException;
import cn.gov.cbrc.bankriskcontrol.entity.AppSystem;
import cn.gov.cbrc.bankriskcontrol.entity.ComputerRoomInfo;
import cn.gov.cbrc.bankriskcontrol.entity.DatabaseInfo;
import cn.gov.cbrc.bankriskcontrol.entity.MiddlewareInfo;
import cn.gov.cbrc.bankriskcontrol.entity.NetworkEquipmentInfo;
import cn.gov.cbrc.bankriskcontrol.entity.OperateSystemInfo;
import cn.gov.cbrc.bankriskcontrol.entity.Organization;
import cn.gov.cbrc.bankriskcontrol.entity.PcServer;
import cn.gov.cbrc.bankriskcontrol.entity.PrecisionAcInfo;
import cn.gov.cbrc.bankriskcontrol.entity.StorageSystemInfo;
import cn.gov.cbrc.bankriskcontrol.entity.UpsInfo;
import cn.gov.cbrc.bankriskcontrol.service.asset.AppSystemService;
import cn.gov.cbrc.bankriskcontrol.service.asset.ComputerRoomInfoService;
import cn.gov.cbrc.bankriskcontrol.service.asset.DatabaseInfoService;
import cn.gov.cbrc.bankriskcontrol.service.asset.MiddlewareInfoService;
import cn.gov.cbrc.bankriskcontrol.service.asset.NetworkEquipmentInfoService;
import cn.gov.cbrc.bankriskcontrol.service.asset.OperateSystemInfoService;
import cn.gov.cbrc.bankriskcontrol.service.asset.PcServerService;
import cn.gov.cbrc.bankriskcontrol.service.asset.PrecisionAcInfoService;
import cn.gov.cbrc.bankriskcontrol.service.asset.StorageSystemInfoService;
import cn.gov.cbrc.bankriskcontrol.service.asset.UpsInfoService;
import cn.gov.cbrc.bankriskcontrol.service.system.OrganizationService;
import cn.gov.cbrc.bankriskcontrol.util.DateUtil;
import cn.gov.cbrc.bankriskcontrol.util.RiskUtils;

/**
 * 资源管理上报接口：提供上报和上报修改(删除、修改)
 * 
 * @author pl
 * 
 */
@Path("/")
@Produces("application/json")
public class ResourceWebService {

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private PcServerService pcServerService;

	@Autowired
	private DatabaseInfoService databaseInfoService;

	@Autowired
	private AppSystemService appSystemService;

	@Autowired
	private OperateSystemInfoService operateSystemInfoService;

	@Autowired
	private ComputerRoomInfoService computerRoomInfoService;
	
	@Autowired
	private MiddlewareInfoService middlewareInfoService;
	
	@Autowired
	private StorageSystemInfoService storageSystemInfoService;
	
	@Autowired
	private NetworkEquipmentInfoService networkEquipmentInfoService;
	
	@Autowired
	private UpsInfoService upsInfoService;
	
	@Autowired
	private PrecisionAcInfoService precisionAcInfoService;

	@POST
	@Path("/pcserver")
	@Consumes("application/json")
	public Response reportHosts(String jsonString) {
		try {
			Organization organization = RiskUtils.checkKeyAndGetOrganization(jsonString, organizationService);
			// 处理上报
			JSONObject json = JSONObject.fromObject(jsonString);
			JSONArray array = (JSONArray) json.get("items");
			Object[] objects = array.toArray();
			List<PcServer> list=new ArrayList<PcServer>();
			for (Object object : objects) {
				PcServer pcserver = (PcServer) JSONObject.toBean((JSONObject) object, PcServer.class);		
				pcserver.setServiceTime(DateUtil.SHORT_FORMAT.parse(((JSONObject) object).getString("serviceTime")));
				pcserver.setOrganization(organization);
				pcserver.setAreaCode(organization.getAreaCity().getAreeCode());
				pcserver.setRecordTime(new Date());
				PcServer exist = pcServerService.getPcServerByUniqueVal(pcserver.getUniqueVal());
				if (exist != null)
					throw new ReportResultException(220, "主机已经存在：" + pcserver.getUniqueVal());
				list.add(pcserver);
			}
			pcServerService.reportPcServer(list);
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch (Exception e) {
			return Response.ok(
					JSONObject.fromObject(new ReportResultException(1000, "异常" + e.getMessage()).getReportResult())
							.toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}

	@DELETE
	@Path("/pcserver/{uniqueVal}")
	@Consumes("application/json")
	public Response removeHosts(@PathParam("uniqueVal") String uniqueVal, @HeaderParam("key") String key,
			@HeaderParam("organization") String organCode, @HeaderParam("time") long time) {
		try {
			RiskUtils.checkKeyAndGetOrganization(key, organCode, time, organizationService);
			PcServer pcserver = pcServerService.getPcServerByUniqueVal(uniqueVal);
			if (pcserver == null)
				throw new ReportResultException(205, "主机不存在：" + uniqueVal);
			pcServerService.deletePcServerById(pcserver.getServerId());
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch (Exception e) {
			return Response.ok(
					JSONObject.fromObject(new ReportResultException(1000, "异常" + e.getMessage()).getReportResult())
							.toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}

	@PUT
	@Path("/pcserver/{uniqueVal}")
	@Consumes("application/json")
	public Response updateHosts(@PathParam("uniqueVal") String uniqueVal, String jsonString) {
		try {
			RiskUtils.checkKeyAndGetOrganization(jsonString, organizationService);
			PcServer pcserver = pcServerService.getPcServerByUniqueVal(uniqueVal);
			if (pcserver == null)
				throw new ReportResultException(205, "主机不存在：" + uniqueVal);
			// 处理更新
			JSONObject json = JSONObject.fromObject(jsonString);
			JSONObject object = (JSONObject) json.get("item");
			RiskUtils.changeJsonToBo(object, pcserver);
			pcserver.setUpdateTime(new Date());
			pcServerService.updatePcServer(pcserver);
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch (Exception e) {
			return Response.ok(
					JSONObject.fromObject(new ReportResultException(1000, "异常" + e.getMessage()).getReportResult())
							.toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}
	
	@POST
	@Path("/os")
	@Consumes("application/json")
	public Response reportOperateSystemInfos(String jsonString) {
		try {
			Organization organization = RiskUtils.checkKeyAndGetOrganization(jsonString, organizationService);
			// 处理上报
			JSONObject json = JSONObject.fromObject(jsonString);
			JSONArray array = (JSONArray) json.get("items");
			Object[] objects = array.toArray();
			List<OperateSystemInfo> temps=new ArrayList<OperateSystemInfo>();
			for (Object object : objects) {
				OperateSystemInfo os = (OperateSystemInfo) JSONObject.toBean((JSONObject) object, OperateSystemInfo.class);
				os.setOrganization(organization);
				os.setAreaCode(organization.getAreaCity().getAreeCode());
				os.setRecordTime(new Date());
				String hostVal=os.getHost();
				String appSystems=os.getAppSystem();
				OperateSystemInfo exist = operateSystemInfoService.getOperateSystemInfoByUniqueVal(os.getUniqueVal());
				if (exist != null)
					throw new ReportResultException(220, "操作系统已经存在：" + os.getUniqueVal());
				PcServer ps=pcServerService.getPcServerByUniqueVal(hostVal);
				if(ps==null){
					throw new ReportResultException(202,"操作系统对应主机不存在:"+hostVal);
				}
				os.setPcserver(ps);
				Set<AppSystem> list=RiskUtils.getAppSystemList(appSystems, appSystemService);
				os.setAppSystems(list);
				temps.add(os);
			}
			operateSystemInfoService.reportOperateSystemInfo(temps);
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch (Exception e) {
			return Response.ok(
					JSONObject.fromObject(new ReportResultException(1000, "异常" + e.getMessage()).getReportResult())
							.toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}

	@DELETE
	@Path("/os/{uniqueVal}")
	@Consumes("application/json")
	public Response removeOperateSystemInfos(@PathParam("uniqueVal") String uniqueVal, @HeaderParam("key") String key,
			@HeaderParam("organization") String organCode, @HeaderParam("time") long time) {
		try {
			RiskUtils.checkKeyAndGetOrganization(key, organCode, time, organizationService);
			OperateSystemInfo os = operateSystemInfoService.getOperateSystemInfoByUniqueVal(uniqueVal);
			if (os == null)
				throw new ReportResultException(206, "操作系统不存在：" + uniqueVal);
			operateSystemInfoService.deleteOperateSystemInfoById(os.getOsId());
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch (Exception e) {
			return Response.ok(
					JSONObject.fromObject(new ReportResultException(1000, "异常" + e.getMessage()).getReportResult())
							.toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}

	@PUT
	@Path("/os/{uniqueVal}")
	@Consumes("application/json")
	public Response updateOperateSystemInfos(@PathParam("uniqueVal") String uniqueVal, String jsonString) {
		try {
			RiskUtils.checkKeyAndGetOrganization(jsonString, organizationService);
			OperateSystemInfo os = operateSystemInfoService.getOperateSystemInfoByUniqueVal(uniqueVal);
			if (os == null)
				throw new ReportResultException(206, "操作系统不存在：" + uniqueVal);
			// 处理更新
			JSONObject json = JSONObject.fromObject(jsonString);
			JSONObject object = (JSONObject) json.get("item");
			RiskUtils.changeJsonToBo(object, os);
			os.setUpdateTime(new Date());
			PcServer ps=pcServerService.getPcServerByUniqueVal(os.getHost());
			if(ps==null){
				throw new ReportResultException(202,"操作系统对应主机不存在:"+os.getHost());
			}
			os.setPcserver(ps);
			Set<AppSystem> list=RiskUtils.getAppSystemList(os.getAppSystem(), appSystemService);
			os.setAppSystems(list);
			operateSystemInfoService.updateOperateSystemInfo(os);
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch (Exception e) {
			return Response.ok(
					JSONObject.fromObject(new ReportResultException(1000, "异常" + e.getMessage()).getReportResult())
							.toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}	

	@POST
	@Path("/db")
	@Consumes("application/json")
	public Response reportDbs(String jsonString) {
		try {
			Organization organization = RiskUtils.checkKeyAndGetOrganization(jsonString, organizationService);
			// 处理上报
			JSONObject json = JSONObject.fromObject(jsonString);
			JSONArray array = (JSONArray) json.get("items");
			Object[] objects = array.toArray();
			List<DatabaseInfo> temps=new ArrayList<DatabaseInfo>();
			for (Object object : objects) {
				DatabaseInfo database = (DatabaseInfo) JSONObject.toBean((JSONObject) object, DatabaseInfo.class);
				database.setServerTime(DateUtil.SHORT_FORMAT.parse(((JSONObject) object).getString("serverTime")));
				database.setOrganization(organization);
				database.setAreaCode(organization.getAreaCity().getAreeCode());
				database.setRecordTime(new Date());
				DatabaseInfo exist = databaseInfoService.getDatabaseInfoByUniqueVal(database.getUniqueVal());
				if (exist != null)
					throw new ReportResultException(220, "数据库已经存在：" + database.getUniqueVal());
				// 处理应用系统关联
				String appSystems = database.getAppSystem();
				Set<AppSystem> list=RiskUtils.getAppSystemList(appSystems, appSystemService);
				database.setAppSystems(list);
				// 处理操作系统关联
				OperateSystemInfo os = operateSystemInfoService.getOperateSystemInfoByUniqueVal(database
						.getOperateSystem());
				if (os == null)
					throw new ReportResultException(203, "数据库对应操作系统不存在:" + database.getOperateSystem());
				database.setOsinfo(os);
				temps.add(database);
			}
			databaseInfoService.reportDatabaseInfo(temps);
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch (Exception e) {
			return Response.ok(
					JSONObject.fromObject(new ReportResultException(1000, "异常" + e.getMessage()).getReportResult())
							.toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}

	@DELETE
	@Path("/db/{uniqueVal}")
	@Consumes("application/json")
	public Response removeDbs(@PathParam("uniqueVal") String uniqueVal, @HeaderParam("key") String key,
			@HeaderParam("organization") String organCode, @HeaderParam("time") long time) {
		try {
			RiskUtils.checkKeyAndGetOrganization(key, organCode, time, organizationService);
			DatabaseInfo db = databaseInfoService.getDatabaseInfoByUniqueVal(uniqueVal);
			if (db == null)
				throw new ReportResultException(207, "数据库不存在：" + uniqueVal);
			databaseInfoService.deleteDatabaseInfoById(db.getDbId());
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch (Exception e) {
			return Response.ok(
					JSONObject.fromObject(new ReportResultException(1000, "异常" + e.getMessage()).getReportResult())
							.toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}

	@PUT
	@Path("/db/{uniqueVal}")
	@Consumes("application/json")
	public Response updateDbs(@PathParam("uniqueVal") String uniqueVal,String jsonString) {
		try {
			RiskUtils.checkKeyAndGetOrganization(jsonString, organizationService);
			DatabaseInfo db = databaseInfoService.getDatabaseInfoByUniqueVal(uniqueVal);
			if (db == null)
				throw new ReportResultException(207, "数据库不存在：" + uniqueVal);
			// 处理更新
			JSONObject json = JSONObject.fromObject(jsonString);
			JSONObject object = (JSONObject) json.get("item");
			RiskUtils.changeJsonToBo(object, db);
			// 处理应用系统关联
			String appSystems = db.getAppSystem();
			Set<AppSystem> list=RiskUtils.getAppSystemList(appSystems, appSystemService);
			db.setAppSystems(list);
			// 处理操作系统关联
			OperateSystemInfo os = operateSystemInfoService.getOperateSystemInfoByUniqueVal(db
					.getOperateSystem());
			if (os == null)
				throw new ReportResultException(203, "数据库对应操作系统不存在:" + db.getOperateSystem());
			db.setOsinfo(os);
			db.setUpdateTime(new Date());
			databaseInfoService.updateDatabaseInfo(db);
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch (Exception e) {
			return Response.ok(
					JSONObject.fromObject(new ReportResultException(1000, "异常" + e.getMessage()).getReportResult())
							.toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}
	
	@POST
	@Path("/mw")
	@Consumes("application/json")
	public Response reportMiddlewareInfos(String jsonString) {
		try {
			Organization organization = RiskUtils.checkKeyAndGetOrganization(jsonString, organizationService);
			// 处理上报
			JSONObject json = JSONObject.fromObject(jsonString);
			JSONArray array = (JSONArray) json.get("items");
			Object[] objects = array.toArray();
			List<MiddlewareInfo> temps=new ArrayList<MiddlewareInfo>();
			for (Object object : objects) {
				MiddlewareInfo mw = (MiddlewareInfo) JSONObject.toBean((JSONObject) object, MiddlewareInfo.class);
				mw.setOrganization(organization);
				mw.setAreaCode(organization.getAreaCity().getAreeCode());
				mw.setRecordTime(new Date());
				MiddlewareInfo exist = middlewareInfoService.getMiddlewareInfoByUniqueVal(mw.getUniqueVal());
				if (exist != null)
					throw new ReportResultException(220, "中间件已经存在：" + mw.getUniqueVal());
				// 处理应用系统 关联
				String appSystems = mw.getAppSystem();
				Set<AppSystem> list=RiskUtils.getAppSystemList(appSystems, appSystemService);
				mw.setAppSystems(list);
				// 处理操作系统关联
				OperateSystemInfo os = operateSystemInfoService.getOperateSystemInfoByUniqueVal(mw
						.getOperateSystem());
				if (os == null)
					throw new ReportResultException(204, "中间件对应操作系统不存在:" + mw.getOperateSystem());
				mw.setOsinfo(os);
				temps.add(mw);
			}
			middlewareInfoService.reportMiddlewareInfo(temps);
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch (Exception e) {
			return Response.ok(
					JSONObject.fromObject(new ReportResultException(1000, "异常" + e.getMessage()).getReportResult())
							.toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}

	@DELETE
	@Path("/mw/{uniqueVal}")
	@Consumes("application/json")
	public Response removeMiddlewareInfos(@PathParam("uniqueVal") String uniqueVal, @HeaderParam("key") String key,
			@HeaderParam("organization") String organCode, @HeaderParam("time") long time) {
		try {
			RiskUtils.checkKeyAndGetOrganization(key, organCode, time, organizationService);
			MiddlewareInfo mw = middlewareInfoService.getMiddlewareInfoByUniqueVal(uniqueVal);
			if (mw == null)
				throw new ReportResultException(208, "中间件不存在：" + uniqueVal);
			middlewareInfoService.deleteMiddlewareInfoById(mw.getMwId());
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch (Exception e) {
			return Response.ok(
					JSONObject.fromObject(new ReportResultException(1000, "异常" + e.getMessage()).getReportResult())
							.toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}

	@PUT
	@Path("/mw/{uniqueVal}")
	@Consumes("application/json")
	public Response updateMiddlewareInfos(@PathParam("uniqueVal") String uniqueVal,String jsonString) {
		try {
			RiskUtils.checkKeyAndGetOrganization(jsonString, organizationService);
			MiddlewareInfo mw = middlewareInfoService.getMiddlewareInfoByUniqueVal(uniqueVal);
			if (mw == null)
				throw new ReportResultException(208, "中间件不存在：" + uniqueVal);
			// 处理更新
			JSONObject json = JSONObject.fromObject(jsonString);
			JSONObject object = (JSONObject) json.get("item");
			RiskUtils.changeJsonToBo(object, mw);
			// 处理应用系统 关联
			String appSystems = mw.getAppSystem();
			Set<AppSystem> list=RiskUtils.getAppSystemList(appSystems, appSystemService);
			mw.setAppSystems(list);
			// 处理操作系统关联
			OperateSystemInfo os = operateSystemInfoService.getOperateSystemInfoByUniqueVal(mw
					.getOperateSystem());
			if (os == null)
				throw new ReportResultException(204, "中间件对应操作系统不存在:" + mw.getOperateSystem());
			mw.setOsinfo(os);
			mw.setUpdateTime(new Date());
			middlewareInfoService.updateMiddlewareInfo(mw);
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch (Exception e) {
			return Response.ok(
					JSONObject.fromObject(new ReportResultException(1000, "异常" + e.getMessage()).getReportResult())
							.toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}
	
	@POST
	@Path("/ss")
	@Consumes("application/json")
	public Response reportStorageSystemInfos(String jsonString) {
		try {
			Organization organization = RiskUtils.checkKeyAndGetOrganization(jsonString, organizationService);
			// 处理上报
			JSONObject json = JSONObject.fromObject(jsonString);
			JSONArray array = (JSONArray) json.get("items");
			Object[] objects = array.toArray();
			List<StorageSystemInfo> temps=new ArrayList<StorageSystemInfo>();
			for (Object object : objects) {
				StorageSystemInfo ss = (StorageSystemInfo) JSONObject.toBean((JSONObject) object, StorageSystemInfo.class);
				ss.setServiceTime(DateUtil.SHORT_FORMAT.parse(((JSONObject) object).getString("serviceTime")));
				ss.setOrganization(organization);
				ss.setAreaCode(organization.getAreaCity().getAreeCode());
				ss.setRecordTime(new Date());
				StorageSystemInfo exist = storageSystemInfoService.getStorageSystemInfoByUniqueVal(ss.getUniqueVal());
				if (exist != null)
					throw new ReportResultException(220, "存储设备已经存在：" + ss.getUniqueVal());
				// 处理应用系统 关联
				String appSystems = ss.getAppSystem();
				Set<AppSystem> list=RiskUtils.getAppSystemList(appSystems, appSystemService);
				ss.setAppSystems(list);	
				temps.add(ss);
			}
			storageSystemInfoService.reportStorageSystemInfo(temps);
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch (Exception e) {
			return Response.ok(
					JSONObject.fromObject(new ReportResultException(1000, "异常" + e.getMessage()).getReportResult())
							.toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}

	@DELETE
	@Path("/ss/{uniqueVal}")
	@Consumes("application/json")
	public Response removeStorageSystemInfos(@PathParam("uniqueVal") String uniqueVal, @HeaderParam("key") String key,
			@HeaderParam("organization") String organCode, @HeaderParam("time") long time) {
		try {
			RiskUtils.checkKeyAndGetOrganization(key, organCode, time, organizationService);
			StorageSystemInfo ss = storageSystemInfoService.getStorageSystemInfoByUniqueVal(uniqueVal);
			if (ss == null)
				throw new ReportResultException(209, "存储设备不存在：" + uniqueVal);
			storageSystemInfoService.deleteStorageSystemInfoById(ss.getSsId());
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch (Exception e) {
			return Response.ok(
					JSONObject.fromObject(new ReportResultException(1000, "异常" + e.getMessage()).getReportResult())
							.toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}

	@PUT
	@Path("/ss/{uniqueVal}")
	@Consumes("application/json")
	public Response updateStorageSystemInfos(@PathParam("uniqueVal") String uniqueVal,String jsonString) {
		try {
			RiskUtils.checkKeyAndGetOrganization(jsonString, organizationService);
			StorageSystemInfo ss = storageSystemInfoService.getStorageSystemInfoByUniqueVal(uniqueVal);
			if (ss == null)
				throw new ReportResultException(209, "存储设备不存在：" + uniqueVal);
			JSONObject json = JSONObject.fromObject(jsonString);
			JSONObject object = (JSONObject) json.get("item");
			RiskUtils.changeJsonToBo(object, ss);
			// 处理应用系统 关联
			String appSystems = ss.getAppSystem();
			Set<AppSystem> list=RiskUtils.getAppSystemList(appSystems, appSystemService);
			ss.setAppSystems(list);
			ss.setUpdateTime(new Date());
			storageSystemInfoService.updateStorageSystemInfo(ss);
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch (Exception e) {
			return Response.ok(
					JSONObject.fromObject(new ReportResultException(1000, "异常" + e.getMessage()).getReportResult())
							.toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}
	
	@POST
	@Path("/ne")
	@Consumes("application/json")
	public Response reportNetworkEquipmentInfos(String jsonString) {
		try {
			Organization organization = RiskUtils.checkKeyAndGetOrganization(jsonString, organizationService);
			// 处理上报
			JSONObject json = JSONObject.fromObject(jsonString);
			JSONArray array = (JSONArray) json.get("items");
			Object[] objects = array.toArray();
			List<NetworkEquipmentInfo> temps=new ArrayList<NetworkEquipmentInfo>();
			for (Object object : objects) {
				NetworkEquipmentInfo ss = (NetworkEquipmentInfo) JSONObject.toBean((JSONObject) object, NetworkEquipmentInfo.class);
				ss.setOrganization(organization);
				ss.setAreaCode(organization.getAreaCity().getAreeCode());
				ss.setRecordTime(new Date());
				NetworkEquipmentInfo exist = networkEquipmentInfoService.getNetworkEquipmentInfoByUniqueVal(ss.getUniqueVal());
				if (exist != null)
					throw new ReportResultException(220, "网络设备已经存在：" + ss.getUniqueVal());
				// 处理应用系统 关联
				String appSystems = ss.getAppSystem();
				Set<AppSystem> list=RiskUtils.getAppSystemList(appSystems, appSystemService);
				ss.setAppSystems(list);	
				temps.add(ss);
			}
			networkEquipmentInfoService.reportNetworkEquipmentInfo(temps);
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch (Exception e) {
			return Response.ok(
					JSONObject.fromObject(new ReportResultException(1000, "异常" + e.getMessage()).getReportResult())
							.toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}

	@DELETE
	@Path("/ne/{uniqueVal}")
	@Consumes("application/json")
	public Response removeNetworkEquipmentInfos(@PathParam("uniqueVal") String uniqueVal, @HeaderParam("key") String key,
			@HeaderParam("organization") String organCode, @HeaderParam("time") long time) {
		try {
			RiskUtils.checkKeyAndGetOrganization(key, organCode, time, organizationService);
			NetworkEquipmentInfo ss = networkEquipmentInfoService.getNetworkEquipmentInfoByUniqueVal(uniqueVal);
			if (ss == null)
				throw new ReportResultException(210, "网络设备不存在：" + uniqueVal);
			networkEquipmentInfoService.deleteNetworkEquipmentInfoById(ss.getNeId());
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch (Exception e) {
			return Response.ok(
					JSONObject.fromObject(new ReportResultException(1000, "异常" + e.getMessage()).getReportResult())
							.toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}

	@PUT
	@Path("/ne/{uniqueVal}")
	@Consumes("application/json")
	public Response updateNetworkEquipmentInfos(@PathParam("uniqueVal") String uniqueVal,String jsonString) {
		try {
			RiskUtils.checkKeyAndGetOrganization(jsonString, organizationService);
			NetworkEquipmentInfo ss = networkEquipmentInfoService.getNetworkEquipmentInfoByUniqueVal(uniqueVal);
			if (ss == null)
				throw new ReportResultException(210, "网络设备不存在：" + uniqueVal);
			JSONObject json = JSONObject.fromObject(jsonString);
			JSONObject object = (JSONObject) json.get("item");
			RiskUtils.changeJsonToBo(object, ss);
			// 处理应用系统 关联
			String appSystems = ss.getAppSystem();
			Set<AppSystem> list=RiskUtils.getAppSystemList(appSystems, appSystemService);
			ss.setAppSystems(list);
			ss.setUpdateTime(new Date());
			networkEquipmentInfoService.updateNetworkEquipmentInfo(ss);
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch (Exception e) {
			return Response.ok(
					JSONObject.fromObject(new ReportResultException(1000, "异常" + e.getMessage()).getReportResult())
							.toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}
	
	@POST
	@Path("/cr")
	@Consumes("application/json")
	public Response reportComputerRoomInfos(String jsonString) {
		try {
			Organization organization = RiskUtils.checkKeyAndGetOrganization(jsonString, organizationService);
			// 处理上报
			JSONObject json = JSONObject.fromObject(jsonString);
			JSONArray array = (JSONArray) json.get("items");
			Object[] objects = array.toArray();
			List<ComputerRoomInfo> temps=new ArrayList<ComputerRoomInfo>();
			for (Object object : objects) {
				ComputerRoomInfo cr = (ComputerRoomInfo) JSONObject.toBean((JSONObject) object,
						ComputerRoomInfo.class);
				cr.setServerTime(DateUtil.SHORT_FORMAT.parse(((JSONObject) object).getString("serverTime")));
				cr.setOrganization(organization);
				cr.setAreaCode(organization.getAreaCity().getAreeCode());
				cr.setRecordTime(new Date());
				ComputerRoomInfo exist = computerRoomInfoService.getComputerRoomInfoByUniqueVal(cr.getUniqueVal());
				if (exist != null)
					throw new ReportResultException(220, "机房已经存在：" + cr.getUniqueVal());
				temps.add(cr);
			}
			computerRoomInfoService.reportComputerRoomInfo(temps);
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch (Exception e) {
			return Response.ok(
					JSONObject.fromObject(new ReportResultException(1000, "异常" + e.getMessage()).getReportResult())
							.toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}

	@DELETE
	@Path("/cr/{uniqueVal}")
	@Consumes("application/json")
	public Response removeComputerRoomInfos(@PathParam("uniqueVal") String uniqueVal, @HeaderParam("key") String key,
			@HeaderParam("organization") String organCode, @HeaderParam("time") long time) {
		try {
			RiskUtils.checkKeyAndGetOrganization(key, organCode, time, organizationService);
			ComputerRoomInfo cr = computerRoomInfoService.getComputerRoomInfoByUniqueVal(uniqueVal);
			if (cr == null)
				throw new ReportResultException(211, "机房信息不存在：" + uniqueVal);
			computerRoomInfoService.deleteComputerRoomInfoById(cr.getCrId());
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch (Exception e) {
			return Response.ok(
					JSONObject.fromObject(new ReportResultException(1000, "异常" + e.getMessage()).getReportResult())
							.toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}

	@PUT
	@Path("/cr/{uniqueVal}")
	@Consumes("application/json")
	public Response updateComputerRoomInfos(@PathParam("uniqueVal") String uniqueVal, String jsonString) {
		try {
			RiskUtils.checkKeyAndGetOrganization(jsonString, organizationService);
			ComputerRoomInfo cr = computerRoomInfoService.getComputerRoomInfoByUniqueVal(uniqueVal);
			if (cr == null)
				throw new ReportResultException(211, "机房信息不存在：" + uniqueVal);
			// 处理更新
			JSONObject json = JSONObject.fromObject(jsonString);
			JSONObject object = (JSONObject) json.get("item");
			RiskUtils.changeJsonToBo(object, cr);
			cr.setUpdateTime(new Date());
			computerRoomInfoService.updateComputerRoomInfo(cr);
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch (Exception e) {
			return Response.ok(
					JSONObject.fromObject(new ReportResultException(1000, "异常" + e.getMessage()).getReportResult())
							.toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}
	
	@POST
	@Path("/ups")
	@Consumes("application/json")
	public Response reportUpsInfos(String jsonString) {
		try {
			Organization organization = RiskUtils.checkKeyAndGetOrganization(jsonString, organizationService);
			// 处理上报
			JSONObject json = JSONObject.fromObject(jsonString);
			JSONArray array = (JSONArray) json.get("items");
			Object[] objects = array.toArray();
			List<UpsInfo> temps=new ArrayList<UpsInfo>();
			for (Object object : objects) {
				UpsInfo ups = (UpsInfo) JSONObject.toBean((JSONObject) object,
						UpsInfo.class);
				ups.setOrganization(organization);
				ups.setAreaCode(organization.getAreaCity().getAreeCode());
				ups.setRecordTime(new Date());
				String roomUv=ups.getRoomUV();
				UpsInfo exist = upsInfoService.getUpsInfoByUniqueVal(ups.getUniqueVal());
				if (exist != null)
					throw new ReportResultException(220, "UPS已经存在：" + ups.getUniqueVal());
				ComputerRoomInfo cm=computerRoomInfoService.getComputerRoomInfoByUniqueVal(roomUv);
				if(cm==null)
					throw new ReportResultException(211, "机房信息不存在：" + roomUv);	
				ups.setComputerRoomInfo(cm);
				temps.add(ups);
			}
			upsInfoService.reportUpsInfo(temps);
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch (Exception e) {
			return Response.ok(
					JSONObject.fromObject(new ReportResultException(1000, "异常" + e.getMessage()).getReportResult())
							.toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}

	@DELETE
	@Path("/ups/{uniqueVal}")
	@Consumes("application/json")
	public Response removeUpsInfos(@PathParam("uniqueVal") String uniqueVal, @HeaderParam("key") String key,
			@HeaderParam("organization") String organCode, @HeaderParam("time") long time) {
		try {
			RiskUtils.checkKeyAndGetOrganization(key, organCode, time, organizationService);
			UpsInfo ups = upsInfoService.getUpsInfoByUniqueVal(uniqueVal);
			if (ups == null)
				throw new ReportResultException(212, "UPS信息不存在：" + uniqueVal);
			upsInfoService.deleteUpsInfoById(ups.getUpsId());
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch (Exception e) {
			return Response.ok(
					JSONObject.fromObject(new ReportResultException(1000, "异常" + e.getMessage()).getReportResult())
							.toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}

	@PUT
	@Path("/ups/{uniqueVal}")
	@Consumes("application/json")
	public Response updateUpsInfos(@PathParam("uniqueVal") String uniqueVal, String jsonString) {
		try {
			RiskUtils.checkKeyAndGetOrganization(jsonString, organizationService);
			UpsInfo ups = upsInfoService.getUpsInfoByUniqueVal(uniqueVal);
			if (ups == null)
				throw new ReportResultException(212, "UPS信息不存在：" + uniqueVal);
			// 处理更新
			JSONObject json = JSONObject.fromObject(jsonString);
			JSONObject object = (JSONObject) json.get("item");
			RiskUtils.changeJsonToBo(object, ups);
			ups.setUpdateTime(new Date());
			String roomUv=ups.getRoomUV();
			ComputerRoomInfo cm=computerRoomInfoService.getComputerRoomInfoByUniqueVal(roomUv);
			if(cm==null)
				throw new ReportResultException(211, "机房信息不存在：" + roomUv);	
			ups.setComputerRoomInfo(cm);
			upsInfoService.updateUpsInfo(ups);
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch (Exception e) {
			return Response.ok(
					JSONObject.fromObject(new ReportResultException(1000, "异常" + e.getMessage()).getReportResult())
							.toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}
	
	@POST
	@Path("/ac")
	@Consumes("application/json")
	public Response reportPrecisionAcInfos(String jsonString) {
		try {
			Organization organization = RiskUtils.checkKeyAndGetOrganization(jsonString, organizationService);
			// 处理上报
			JSONObject json = JSONObject.fromObject(jsonString);
			JSONArray array = (JSONArray) json.get("items");
			Object[] objects = array.toArray();
			List<PrecisionAcInfo> temps=new ArrayList<PrecisionAcInfo>();
			for (Object object : objects) {
				PrecisionAcInfo ac = (PrecisionAcInfo) JSONObject.toBean((JSONObject) object,
						PrecisionAcInfo.class);
				ac.setOrganization(organization);
				ac.setAreaCode(organization.getAreaCity().getAreeCode());
				ac.setRecordTime(new Date());
				String roomUv=ac.getRoomUV();
				PrecisionAcInfo exist = precisionAcInfoService.getPrecisionAcInfoByUniqueVal(ac.getUniqueVal());
				if (exist != null)
					throw new ReportResultException(220, "精密空调已经存在：" + ac.getUniqueVal());
				ComputerRoomInfo cm=computerRoomInfoService.getComputerRoomInfoByUniqueVal(roomUv);
				if(cm==null)
					throw new ReportResultException(211, "机房信息不存在：" + roomUv);	
				ac.setComputerRoomInfo(cm);
				temps.add(ac);
			}
			precisionAcInfoService.reportPrecisionAcInfo(temps);
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch (Exception e) {
			return Response.ok(
					JSONObject.fromObject(new ReportResultException(1000, "异常" + e.getMessage()).getReportResult())
							.toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}

	@DELETE
	@Path("/ac/{uniqueVal}")
	@Consumes("application/json")
	public Response removePrecisionAcInfos(@PathParam("uniqueVal") String uniqueVal, @HeaderParam("key") String key,
			@HeaderParam("organization") String organCode, @HeaderParam("time") long time) {
		try {
			RiskUtils.checkKeyAndGetOrganization(key, organCode, time, organizationService);
			PrecisionAcInfo ac = precisionAcInfoService.getPrecisionAcInfoByUniqueVal(uniqueVal);
			if (ac == null)
				throw new ReportResultException(213, "精密空调信息不存在：" + uniqueVal);
			precisionAcInfoService.deletePrecisionAcInfoById(ac.getAcId());
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch (Exception e) {
			return Response.ok(
					JSONObject.fromObject(new ReportResultException(1000, "异常" + e.getMessage()).getReportResult())
							.toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}

	@PUT
	@Path("/ac/{uniqueVal}")
	@Consumes("application/json")
	public Response updatePrecisionAcInfos(@PathParam("uniqueVal") String uniqueVal, String jsonString) {
		try {
			RiskUtils.checkKeyAndGetOrganization(jsonString, organizationService);
			PrecisionAcInfo ac = precisionAcInfoService.getPrecisionAcInfoByUniqueVal(uniqueVal);
			if (ac == null)
				throw new ReportResultException(213, "精密空调信息不存在：" + uniqueVal);
			// 处理更新
			JSONObject json = JSONObject.fromObject(jsonString);
			JSONObject object = (JSONObject) json.get("item");
			RiskUtils.changeJsonToBo(object, ac);
			ac.setUpdateTime(new Date());
			String roomUv=ac.getRoomUV();
			ComputerRoomInfo cm=computerRoomInfoService.getComputerRoomInfoByUniqueVal(roomUv);
			if(cm==null)
				throw new ReportResultException(211, "机房信息不存在：" + roomUv);	
			ac.setComputerRoomInfo(cm);
			precisionAcInfoService.updatePrecisionAcInfo(ac);
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch (Exception e) {
			return Response.ok(
					JSONObject.fromObject(new ReportResultException(1000, "异常" + e.getMessage()).getReportResult())
							.toString()).build();
		}
		return Response.ok(JSONObject.fromObject(new ReportResultException(0, "上报成功").getReportResult()).toString())
				.build();
	}
}
