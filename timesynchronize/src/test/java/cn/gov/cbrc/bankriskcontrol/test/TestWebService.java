package cn.gov.cbrc.bankriskcontrol.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;

import cn.gov.cbrc.bankriskcontrol.dto.ClosedFishingWebsiteRateItem;
import cn.gov.cbrc.bankriskcontrol.entity.DatabaseInfo;
import cn.gov.cbrc.bankriskcontrol.entity.PcServer;
import cn.gov.cbrc.bankriskcontrol.rest.ReportWebService;
import cn.gov.cbrc.bankriskcontrol.rest.ResourceWebService;

/**
 * 
 * @author pl
 * 
 */
public class TestWebService {

	public static void main(String[] args) {
		ReportWebService bankService = new ReportWebService();
		ResourceWebService web=new ResourceWebService();
		JAXRSServerFactoryBean restServer = new JAXRSServerFactoryBean();
		restServer.setResourceClasses(ClosedFishingWebsiteRateItem.class,PcServer.class,DatabaseInfo.class);
		List<Object> list = new ArrayList<Object>();
		list.add(bankService);
		list.add(web);
		restServer.setServiceBeans(list);
		restServer.setAddress("http://192.168.3.14:9999/");
		restServer.create();
	}
}
