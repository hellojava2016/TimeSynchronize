package cn.gov.cbrc.bankriskcontrol.controller.asset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.gov.cbrc.bankriskcontrol.entity.AppSystem;
import cn.gov.cbrc.bankriskcontrol.entity.DatabaseInfo;
import cn.gov.cbrc.bankriskcontrol.entity.MiddlewareInfo;
import cn.gov.cbrc.bankriskcontrol.entity.NetworkEquipmentInfo;
import cn.gov.cbrc.bankriskcontrol.entity.OperateSystemInfo;
import cn.gov.cbrc.bankriskcontrol.entity.Organization;
import cn.gov.cbrc.bankriskcontrol.entity.PcServer;
import cn.gov.cbrc.bankriskcontrol.entity.User;
import cn.gov.cbrc.bankriskcontrol.service.asset.AppSystemService;
import cn.gov.cbrc.bankriskcontrol.service.asset.DatabaseInfoService;
import cn.gov.cbrc.bankriskcontrol.service.asset.MiddlewareInfoService;
import cn.gov.cbrc.bankriskcontrol.service.asset.NetworkEquipmentInfoService;
import cn.gov.cbrc.bankriskcontrol.service.asset.OperateSystemInfoService;
import cn.gov.cbrc.bankriskcontrol.service.asset.PcServerService;
import cn.gov.cbrc.bankriskcontrol.service.system.OrganizationService;
import cn.gov.cbrc.bankriskcontrol.util.ConvertUtils;

@Controller
@RequestMapping(value = "/asset/topo")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TopoController {
	
	public final static String hostimage = "../../topo/image/HOST.png";
	public final static String osimage = "../../topo/image/OS.png";
	public final static String neimage = "../../topo/image/NE.png";
	public final static String dbimage = "../../topo/image/DB.png";
	public final static String mwimage = "../../topo/image/MW.png";
	public final static String appimage = "../../topo/image/APP.png";
	
	@Autowired
    private OperateSystemInfoService operateSystemInfoService;
	
	@Autowired
    private PcServerService pcServerService;
	
	@Autowired
	private MiddlewareInfoService middlewareInfoService;
	
	@Autowired
    private DatabaseInfoService databaseInfoService;
	
	@Autowired
	private AppSystemService appSystemService;
	
	@Autowired
	private NetworkEquipmentInfoService networkEquipmentInfoService;
	
	@Autowired
	private OrganizationService organizationService;
	
    @RequestMapping(value = "/topo1.do")
	public ModelAndView add_pre(HttpServletRequest request,Model model) { 
    	OperateSystemInfo os = new OperateSystemInfo();
    	os.setUniqueVal("0001");
    	os.setName(os.getName());
    	PcServer   server = new PcServer();
    	server.setUniqueVal("0002");
    	server.setName(server.getName());
		model.addAttribute("jsonStr", getTopoJson(os,server,null,null));
		return new ModelAndView("/asset/topo", null);
	}
    
	@RequestMapping(value = "/pc.do")
	public ModelAndView topo_pc(HttpServletRequest request, Model model) {
		long pcid=Long.parseLong(request.getParameter("pcid"));
		PcServer pc = pcServerService.getPcServerById(pcid);
		Organization org = pc.getOrganization();
		long orgid = org.getOrgId();
		List<OperateSystemInfo> osList = operateSystemInfoService.getOperateSystemInfosByOrganization(orgid);
		List<DatabaseInfo> dbs = databaseInfoService.getDatabaseInfosByOrganization(orgid);
		List<MiddlewareInfo> mws = middlewareInfoService.getMiddlewareInfoByOrg(orgid);
		//查询和PC关联的OS
		for (Iterator<OperateSystemInfo> iterator = osList.iterator(); iterator.hasNext();) {
			OperateSystemInfo os = iterator.next();
			if (os.getPcserver().getServerId() != pcid)
				iterator.remove();
		}
		Map<Long, List<DatabaseInfo>> osDbMap = new HashMap<Long, List<DatabaseInfo>>();
		Map<Long, List<MiddlewareInfo>> osMwMap = new HashMap<Long, List<MiddlewareInfo>>();
		Map<Long, OperateSystemInfo> idOsMap = new HashMap<Long, OperateSystemInfo>();
		//查询每个OS关联 的DB和Middleware
		for (OperateSystemInfo os : osList) {
			long osid = os.getOsId();
			idOsMap.put(osid, os);
			osDbMap.put(osid, new ArrayList<DatabaseInfo>());
			osMwMap.put(osid, new ArrayList<MiddlewareInfo>());
			for (DatabaseInfo db : dbs) {
				if (db.getOsinfo().getOsId() == osid)
					osDbMap.get(osid).add(db);
			}
			for (MiddlewareInfo mw : mws) {
				if (mw.getOsinfo().getOsId() == osid)
					osMwMap.get(osid).add(mw);
			}
		}
		// 组装JSON
		StringBuffer devicesb = new StringBuffer();
		StringBuffer linesb = new StringBuffer();
		devicesb.append("{\"devices\":[");
		linesb.append("\"lines\":[");
		try {
			List<OperateSystemInfo> tempList=new ArrayList<OperateSystemInfo>();
			tempList.addAll(idOsMap.values());
			getDeviceJson_pc(ConvertUtils.newArrayList(pc), devicesb, org);
			getDeviceJson_pc(tempList, devicesb, org);
			getLineJson(tempList, pc, linesb);
			for (long osid : idOsMap.keySet()) {
				OperateSystemInfo os = idOsMap.get(osid);
				List<DatabaseInfo> dblist = osDbMap.get(osid);
				getDeviceJson_pc(dblist, devicesb, org);
				getLineJson(dblist, os, linesb);
				List<MiddlewareInfo> mwlist = osMwMap.get(osid);
				getDeviceJson_pc(mwlist, devicesb, org);
				getLineJson(mwlist, os, linesb);				
			}
			if (devicesb.toString().endsWith(","))
				devicesb.deleteCharAt(devicesb.length() - 1);
			if (linesb.toString().endsWith(","))
				linesb.deleteCharAt(linesb.length() - 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		devicesb.append("],");
		linesb.append("]}");
		model.addAttribute("jsonStr", devicesb.toString()+linesb.toString());
		return new ModelAndView("/asset/topo", null);
	}
	
	private void getDeviceJson_pc(List list,StringBuffer sb,Organization org) throws Exception {
		for (int index = 0; index < list.size(); index++) {
			Object object = list.get(index);
			String uniqueValue = "uniqueVal";
			String propertyName = "name";
			String image = "";
			String type="";
			int x =0;
			int y = 0;
			if (object instanceof MiddlewareInfo) {
				propertyName = "middlewareName";
				image = mwimage;
				type="MD";
				x = 300;
				y = 200 * index + 100;
			} else if (object instanceof DatabaseInfo) {
				propertyName = "databaseName";
				image = dbimage;
				type="DB";
				x = 500;
				y = 200 * index + 100;
			} else if (object instanceof OperateSystemInfo) {
				image = osimage;
				type="OS";
				x = 700;
				y = 200 * index + 400;
			} else if (object instanceof PcServer) {
				image = hostimage;
				type="HOST";
				x = 1000;
				y = 500;
			}
			String name = (String) PropertyUtils.getProperty(object, propertyName);
			if (object instanceof AppSystem) {
				name = name + "(" + org.getName() + ")";
			} else if (object instanceof PcServer) {
				name = name + "(" + org.getName() + ")";
			}
			sb.append("{\"id\":\"");
			// 保证id唯一性
			sb.append(object.getClass().getSimpleName() + PropertyUtils.getProperty(object, uniqueValue));
			sb.append("\",\"name\":\"");
			sb.append(name);
			sb.append("\",\"devType\":\""+type+"\",");
			sb.append("\"src\":\"");
			sb.append(image);
			sb.append("\",");
			sb.append("\"x\":" + x + ",");
			sb.append("\"y\":" + y + "}");
			sb.append(",");}
	}
    
    @RequestMapping(value = "/appsystem.do")
	public ModelAndView topo_app(HttpServletRequest request,Model model) { 
    	long appid=Long.parseLong(request.getParameter("appid"));
    	User currentuser=(User)request.getSession().getAttribute("user");
    	AppSystem as=appSystemService.getAppSystemById(appid);
    	Organization org=currentuser.getOrganization();
    	long orgid=org.getOrgId();
    	List<NetworkEquipmentInfo> nes=networkEquipmentInfoService.getNetworkEquipmentInfosByOrg(orgid);
    	List<DatabaseInfo> dbs=databaseInfoService.getDatabaseInfosByOrganization(orgid);
    	List<MiddlewareInfo> mws=middlewareInfoService.getMiddlewareInfoByOrg(orgid);
    	List<OperateSystemInfo> ops=operateSystemInfoService.getOperateSystemInfosByOrganization(orgid);
    	filterByAppSystem(nes,as);
    	filterByAppSystem(dbs,as);
    	filterByAppSystem(mws,as);
    	filterByAppSystem(ops,as);
    	StringBuffer sb=new StringBuffer();    	
    	try {
    		sb.append("{\"devices\":[");
    		getDeviceJson(ConvertUtils.newArrayList(as),sb,org);
    		getDeviceJson(nes,sb,org);
    		getDeviceJson(dbs,sb,org);
    		getDeviceJson(mws,sb,org);
    		getDeviceJson(ops,sb,org);
    		sb.deleteCharAt(sb.length()-1);
    		sb.append("],");
        	sb.append("\"lines\":[");
        	getLineJson(nes, as, sb);
        	getLineJson(dbs, as, sb);
        	getLineJson(mws, as, sb);
        	getLineJson(ops, as, sb);
        	if(nes.size()+dbs.size()+mws.size()+ops.size()>0)
        		sb.deleteCharAt(sb.length()-1);
        	sb.append("]}");
		} catch (Exception e) {
			e.printStackTrace();
		}    	
    	model.addAttribute("jsonStr", sb.toString());
		return new ModelAndView("/asset/topo", null);
	}
	
	private void getDeviceJson(List list,StringBuffer sb,Organization org) throws Exception {
		for (int index = 0; index < list.size(); index++) {
			Object object = list.get(index);
			String uniqueValue = "uniqueVal";
			String propertyName = "name";
			String image = "";
			String type="";
			int x = 100 * (index + 1);
			int y = 0;
			if (object instanceof MiddlewareInfo) {
				propertyName = "middlewareName";
				image = mwimage;
				type="MD";
				y = 100;
			} else if (object instanceof DatabaseInfo) {
				propertyName = "databaseName";
				image = dbimage;
				type="DB";
				y = 300;
			} else if (object instanceof OperateSystemInfo) {
				image = osimage;
				type="OS";
				y = 700;
			} else if (object instanceof NetworkEquipmentInfo) {
				image = neimage;
				type="NE";
				y = 900;
			} else if (object instanceof AppSystem) {
				image = appimage;
				type="APP";
				x = 1000;
				y = 500;
				propertyName = "appName";
				uniqueValue = "appId";
			} else if (object instanceof PcServer) {
				image = hostimage;
				type="HOST";
				y = 1100;
			}
			String name = (String) PropertyUtils.getProperty(object, propertyName);
			if (object instanceof AppSystem) {
				name = name + "(" + org.getName() + ")";
			} else if (object instanceof PcServer) {
				name = name + "(" + org.getName() + ")";
			}
			sb.append("{\"id\":\"");
			// 保证id唯一性
			sb.append(object.getClass().getSimpleName() + PropertyUtils.getProperty(object, uniqueValue));
			sb.append("\",\"name\":\"");
			sb.append(name);
			sb.append("\",\"devType\":\""+type+"\",");
			sb.append("\"src\":\"");
			sb.append(image);
			sb.append("\",");
			sb.append("\"x\":" + x + ",");
			sb.append("\"y\":" + y + "}");
			sb.append(",");}
	}
	
	private void getLineJson(List list,Object temp,StringBuffer sb) throws Exception {
		String uniqueVal=(temp instanceof AppSystem)?"appId":"uniqueVal";
		for(int index=0;index<list.size();index++){
			Object object=list.get(index);			
			sb.append("{\"srcDeviceId\":\"");
    		sb.append(object.getClass().getSimpleName()+PropertyUtils.getProperty(object, "uniqueVal"));
    		sb.append("\",\"dstDeviceId\":\"");
    		sb.append(temp.getClass().getSimpleName()+PropertyUtils.getProperty(temp, uniqueVal));
    		sb.append("\",\"stroke\":\"black\",");
    		sb.append("\"strokeWidth\":3},");}	
	}
    
    
    private String getTopoJson(OperateSystemInfo os,PcServer host,List<DatabaseInfo> dbs,List<MiddlewareInfo> mds){
    	StringBuilder sb = new StringBuilder();
    	sb.append("{\"devices\":[");
    	if(null!=os){
    		sb.append("{\"id\":\"");
    		sb.append(os.getUniqueVal());
    		sb.append("\",\"name\":\"");
    		sb.append(os.getName());
    		sb.append("\",\"devType\":\"OS\",");
    		sb.append("\"src\":\"");
    		sb.append(osimage);
    		sb.append("\",");
    		sb.append("\"x\":400,");
    		sb.append("\"y\":400}");
    	}
    	sb.append(",");
    	if(null!=host){
    		sb.append("{\"id\":\"");
    		sb.append(host.getUniqueVal());
    		sb.append("\",\"name\":\"");
    		sb.append(host.getName());
    		sb.append("\",\"devType\":\"HOST\",");
    		sb.append("\"src\":\"");
    		sb.append(hostimage);
    		sb.append("\",");
    		sb.append("\"x\":400,");
    		sb.append("\"y\":100}");
    	}
    	
    	sb.append("],");
    	sb.append("\"lines\":[");
    	if(null!=host){
    		sb.append("{\"srcDeviceId\":\"");
    		sb.append(os.getUniqueVal());
    		sb.append("\",\"dstDeviceId\":\"");
    		sb.append(host.getUniqueVal());
    		sb.append("\",\"stroke\":\"black\",");
    		sb.append("\"strokeWidth\":3}");
    	}	
    	sb.append("]}");
    	return sb.toString();
    }    
    
	private void filterByAppSystem( List list, AppSystem as) {
		for (Iterator<Object> iterator = list.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			Set<AppSystem> systems;
			try {
				systems = (Set<AppSystem>) PropertyUtils.getProperty(object, "appSystems");
				boolean exist = false;
				for (AppSystem ap : systems) {
					if (ap.getAppId().equals(as.getAppId())) {
						exist = true;
						break;
					}
				}
				if (!exist)
					iterator.remove();
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
	}
}
