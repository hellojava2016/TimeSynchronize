package com.shtitan.timesynchronize.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.shtitan.timesynchronize.dto.ReportResultException;
import com.shtitan.timesynchronize.entity.BankMessageReceiver;
import com.shtitan.timesynchronize.entity.Organization;
import com.shtitan.timesynchronize.service.system.OrganizationService;
import com.shtitan.timesynchronize.service.system.ReceiveMessageService;
import com.shtitan.timesynchronize.util.DateUtil;
import com.shtitan.timesynchronize.util.RiskUtils;


/**
 * 消息查询接口：提供查询功能
 * 
 * @author pl
 * 
 */
@Path("/")
@Produces("application/json")
public class MessageWebService {
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private ReceiveMessageService receiveMessageService; 

	@POST
	@Path("/")
	@Consumes("application/json")
	public Response query(String jsonString) {
		try {
			Organization organization = RiskUtils.checkKeyAndGetOrganization(jsonString, organizationService);
			System.out.println(organization.getName());
			List<BankMessageReceiver> list=receiveMessageService.getNotDownloadMessage(organization.getOrgId());
			JSONArray array=new JSONArray();
			for(BankMessageReceiver re:list){
				JSONObject temp=new JSONObject();
				temp.put("message", re.getMessage().getMessage());
				temp.put("title", re.getMessage().getTitle());
				temp.put("sendUser", re.getMessage().getSendUser().getName());
				temp.put("sendTime", DateUtil.getShortDate(re.getMessage().getSendTime()));
				temp.put("messageId", re.getMessage().getMessageId());
				temp.put("critical", re.getMessage().getCritical());
				array.add(temp);
			}
			JSONObject object=new JSONObject();
			object.put("items", array);
			
			//更新状态
			for(BankMessageReceiver re:list){
				re.setDownload(true);
				receiveMessageService.updateBankMessageReceiver(re);
			}
			return Response.ok(object.toString()).build();
		} catch (ReportResultException rre) {
			return Response.ok(JSONObject.fromObject(rre.getReportResult()).toString()).build();
		} catch (Exception e) {
			return Response.ok(
					JSONObject.fromObject(new ReportResultException(1000, "异常" + e.getMessage()).getReportResult())
							.toString()).build();
		}
	}
}
