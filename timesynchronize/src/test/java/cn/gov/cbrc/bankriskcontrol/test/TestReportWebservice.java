package cn.gov.cbrc.bankriskcontrol.test;

import java.io.InputStream;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import cn.gov.cbrc.bankriskcontrol.dto.ClosedFishingWebsiteRateItem;
import cn.gov.cbrc.bankriskcontrol.dto.DeploymentChangeSuccessRateItem;
import cn.gov.cbrc.bankriskcontrol.dto.ExternalAttackChangeRateItem;
import cn.gov.cbrc.bankriskcontrol.dto.InformationRiskEventItem;
import cn.gov.cbrc.bankriskcontrol.dto.MainElectronicBankActiveUserChangeRateItem;
import cn.gov.cbrc.bankriskcontrol.dto.MainElectronicChannelTransactionChangeRateItem;
import cn.gov.cbrc.bankriskcontrol.dto.SystemAvilabelRateItem;
import cn.gov.cbrc.bankriskcontrol.dto.SystemTransactionSuccessRateItem;
import cn.gov.cbrc.bankriskcontrol.util.ConvertUtils;
import cn.gov.cbrc.bankriskcontrol.util.DateUtil;
import cn.gov.cbrc.bankriskcontrol.util.encode.EncodeUtils;

public class TestReportWebservice {
	private static String sslKeyStorePath = "E:/ssl/mykey.keystore";
	private static String sslKeyStorePassword = "123456";
	private static String sslKeyStoreType = "JKS"; // 密钥库类型，有JKS PKCS12等
	private static String sslTrustStore = "E:/ssl/client.truststore";
	private static String sslTrustStorePassword = "123456";
//	private static String url = "http://180.169.30.194:8888/bankriskcontrol/rest/risk";
	private static String url = "http://localhost:9999/bankriskcontrol/rest/risk";
	private static String orgNo = "BJBK";
	private static String orgKey = "01071193387580474171410454334001";
	private static long time = 1410538279421L;
	private static String totalKey = orgNo + ":" + orgKey + ":" + time;

	public static void main(String[] args) throws Exception {
		reportSystemTransactionSuccessRateItems();
//		 reportSystemAvilabelRateItems();
//		 reportSystemTransactionSuccessRateItems_single();
//		 reportSystemTransactionSuccessRateItems_trustall();
//		 reportFishing();
//		reportExternalAttackChangeRateItems();
	}

	public static void reportSystemAvilabelRateItems() throws Exception {
		SystemAvilabelRateItem item = new SystemAvilabelRateItem();
		item.setReportDate(DateUtil.SHORT_FORMAT.parse("2014-10-16"));
		item.setRiskCode("1101");
		item.setSost(1100);
		item.setUost(910);
		item.setTst(10000);

		SystemAvilabelRateItem item2 = new SystemAvilabelRateItem();
		item2.setReportDate(DateUtil.SHORT_FORMAT.parse("2014-10-19"));
		item2.setRiskCode("1102");
		item2.setSost(1000);
		item2.setUost(900);
		item2.setTst(10000);
		
		SystemAvilabelRateItem item3 = new SystemAvilabelRateItem();
		item3.setReportDate(DateUtil.SHORT_FORMAT.parse("2014-10-16"));
		item3.setRiskCode("1103");
		item3.setSost(1000);
		item3.setUost(900);
		item3.setTst(10000);

		List<SystemAvilabelRateItem> list = new ArrayList<SystemAvilabelRateItem>();
		list.add(item);
		//list.add(item2);
		//list.add(item3);

		// 通过JAVA编码构造JSON对象
		JSONObject object = new JSONObject();
		object.put("organization", "001");
		long time = new Date().getTime();
		String totalKey = "001:01037148031781818771407166668888:" + time;
		object.put("key", EncodeUtils.ecodeByMD5(totalKey));
		object.put("time", time);
		object.put("items", JSONArray.fromObject(list));
		HttpPost httpPost = new HttpPost("http://localhost:9999/bankriskcontrol/rest/risk/systemavilabelrate");
		StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpClient client = new DefaultHttpClient();
		// 发送请求并解析返回结果
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			// 服务端返回的提示信息
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}
	
	    //大量数据
	public static void reportSystemTransactionSuccessRateItems() throws Exception {
		List<SystemTransactionSuccessRateItem> list = new ArrayList<SystemTransactionSuccessRateItem>();
//		List<String> codes = ConvertUtils.newArrayList("2001", "2001001", "2001002", "2002", "2002001", "2002002",
//				"2002003", "2002004", "2002005", "2003", "2003001", "2003002", "2003003", "2003004", "2004", "2004001",
//				"2004002", "2004003", "2004004", "2004005", "2004006", "2004007", "2005", "2006");
		List<String> codes = ConvertUtils.newArrayList("2001");
		for (int i = 1; i < 2; i++) {
			for (String code : codes) {
				SystemTransactionSuccessRateItem item = new SystemTransactionSuccessRateItem();
				item.setReportDate(DateUtils.addDays(new Date(), -i));
				item.setRiskCode(code);
				item.setNst(1000);
				item.setTnt(9000);
				list.add(item);
			}
		}
		JSONObject object1 = new JSONObject();
		object1.put("reportDate", "2014-10-29");
		object1.put("riskCode", "2001001");
		object1.put("nst", 1111);
		object1.put("tnt", 9999);
		
		HttpPost httpPost = new HttpPost(url + "/systemtransactionsuccessrate");
		JSONObject object = new JSONObject();
		object.put("organization", orgNo);
		object.put("key", EncodeUtils.ecodeByMD5(totalKey));
		object.put("time", time);
		object.put("items", JSONArray.fromObject(object1));

		StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpClient client = new DefaultHttpClient();
		// 发送请求并解析返回结果
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			// 服务端返回的提示信息
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}
	
	//单向
	public static void reportSystemTransactionSuccessRateItems_single() throws Exception {
	    System.setProperty("javax.net.ssl.trustStore", "d:/ssl/clientlocalhost.truststore");    
	    System.setProperty("javax.net.ssl.trustStorePassword","tekview12345678"); 
	    SystemTransactionSuccessRateItem item = new SystemTransactionSuccessRateItem();
		item.setReportDate(new Date());
		item.setRiskCode("002001001");
		item.setNst(1011);
		item.setTnt(9011);
		
		SystemTransactionSuccessRateItem item2 = new SystemTransactionSuccessRateItem();
		item2.setReportDate(new Date());
		item2.setRiskCode("002001002");
		item2.setNst(10022);
		item2.setTnt(900222);
		
		List<SystemTransactionSuccessRateItem> list = new ArrayList<SystemTransactionSuccessRateItem>();
		list.add(item);
		list.add(item2);
		
		//通过JAVA编码构造JSON对象
		JSONObject object = new JSONObject();
		object.put("organization", "001");
		long time=new Date().getTime();
		String totalKey="001:01037148031781818771407155761875:"+time;
		object.put("key",EncodeUtils.ecodeByMD5(totalKey));
		object.put("time", time);
		object.put("items", JSONArray.fromObject(list));
		HttpPost httpPost = new HttpPost(
				"https://127.0.0.1:443/bankriskcontrol/rest/risk/systemtransactionsuccessrate");
		StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);		
		HttpClient client = new DefaultHttpClient();
		//发送请求并解析返回结果
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			//服务端返回的提示信息
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}
	
	    //信任所有
		@SuppressWarnings({ "deprecation", "resource" })
		public static void reportSystemTransactionSuccessRateItems_trustall() throws Exception {			
		    SystemTransactionSuccessRateItem item = new SystemTransactionSuccessRateItem();
			item.setReportDate(new Date());
			item.setRiskCode("002001001");
			item.setNst(1011);
			item.setTnt(9011);
			
			SystemTransactionSuccessRateItem item2 = new SystemTransactionSuccessRateItem();
			item2.setReportDate(new Date());
			item2.setRiskCode("002001002");
			item2.setNst(10022);
			item2.setTnt(900222);
			
			List<SystemTransactionSuccessRateItem> list = new ArrayList<SystemTransactionSuccessRateItem>();
			list.add(item);
			list.add(item2);
			
			//通过JAVA编码构造JSON对象
			JSONObject object = new JSONObject();
			object.put("organization", "001");
			long time=new Date().getTime();
			String totalKey="001:01037148031781818771407155761875:"+time;
			object.put("key",EncodeUtils.ecodeByMD5(totalKey));
			object.put("time", time);
			object.put("items", JSONArray.fromObject(list));
			HttpPost httpPost = new HttpPost(
					"https://10.40.45.33:8443/bankriskcontrol/rest/risk/systemtransactionsuccessrate");
			StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
			entity.setContentType("application/json");
			httpPost.setEntity(entity);		
			
			//此处覆写缺省的证书验证机制，信任所有证书
			SSLContext sslContext = SSLContext.getInstance("TLS");
			TrustManager tm = new X509TrustManager() {
	            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	            }

	            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	            }

	            public X509Certificate[] getAcceptedIssuers() {
	                return null;
	            }
	        };
	        sslContext.init(null, new TrustManager[]{tm}, null);

			HttpClient client = new DefaultHttpClient();
			SSLSocketFactory socketFactory = new SSLSocketFactory(sslContext);
			Scheme sch = new Scheme("https", 8443, socketFactory);
			client.getConnectionManager().getSchemeRegistry().register(sch);
			
			//发送请求并解析返回结果
			HttpResponse response = client.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				InputStream stream = response.getEntity().getContent();
				//服务端返回的提示信息
				String value = IOUtils.toString(stream);
				System.out.println(value);
			}
		}

	//双向
	@SuppressWarnings("deprecation")
	public static void reportSystemTransactionSuccessRateItems_bi() throws Exception {
		System.setProperty("javax.net.ssl.keyStore", sslKeyStorePath);
		System.setProperty("javax.net.ssl.keyStorePassword", sslKeyStorePassword);
		System.setProperty("javax.net.ssl.keyStoreType", sslKeyStoreType);
		System.setProperty("javax.net.ssl.trustStore", sslTrustStore);
		System.setProperty("javax.net.ssl.trustStorePassword", sslTrustStorePassword);
		SystemTransactionSuccessRateItem item = new SystemTransactionSuccessRateItem();
		item.setReportDate(new Date());
		item.setRiskCode("002001001");
		item.setNst(1011);
		item.setTnt(9011);

		SystemTransactionSuccessRateItem item2 = new SystemTransactionSuccessRateItem();
		item2.setReportDate(new Date());
		item2.setRiskCode("002001002");
		item2.setNst(10022);
		item2.setTnt(900222);

		List<SystemTransactionSuccessRateItem> list = new ArrayList<SystemTransactionSuccessRateItem>();
		list.add(item);
		list.add(item2);

		// 通过JAVA编码构造JSON对象
		JSONObject object = new JSONObject();
		object.put("organization", "001");
		long time = new Date().getTime();
		String totalKey = "001:01037148031781818771407155761875:" + time;
		object.put("key", EncodeUtils.ecodeByMD5(totalKey));
		object.put("time", time);
		object.put("items", JSONArray.fromObject(list));
		HttpPost httpPost = new HttpPost(
				"https://localhost:8443/bankriskcontrol/rest/risk/systemtransactionsuccessrate");
		StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);

		SSLContext sslContext = SSLContext.getInstance("TLS");
		TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        sslContext.init(null, new TrustManager[]{tm}, null);

		HttpClient client = new DefaultHttpClient();
		SSLSocketFactory socketFactory = new SSLSocketFactory(sslContext);
		Scheme sch = new Scheme("https", 8443, socketFactory);
		client.getConnectionManager().getSchemeRegistry().register(sch);
		// 发送请求并解析返回结果
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			// 服务端返回的提示信息
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportDeploymentChangeSuccessRateItems() throws Exception {
		DeploymentChangeSuccessRateItem item = new DeploymentChangeSuccessRateItem();
		item.setReportDate(new Date());
		item.setRiskCode("001");
		item.setNsdc(100);
		item.setTndc(90);

		DeploymentChangeSuccessRateItem item2 = new DeploymentChangeSuccessRateItem();
		item2.setReportDate(new Date());
		item2.setRiskCode("002");
		item.setNsdc(1000);
		item.setTndc(900);

		List<DeploymentChangeSuccessRateItem> list = new ArrayList<DeploymentChangeSuccessRateItem>();
		list.add(item);
		list.add(item2);

		// 通过JAVA编码构造JSON对象
		JSONObject object = new JSONObject();
		object.put("key", "0000111122223333");
		object.put("items", JSONArray.fromObject(list));
		HttpPost httpPost = new HttpPost("http://192.168.3.14:9999/rest/risk/001/deploymentchangesuccessrate");
		StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpClient client = new DefaultHttpClient();
		// 发送请求并解析返回结果
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			// 服务端返回的提示信息
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportClosedFishingWebsiteRateItems() throws Exception {
		ClosedFishingWebsiteRateItem item = new ClosedFishingWebsiteRateItem();
		item.setReportDate(new Date());
		item.setRiskCode("001");
		item.setNcfw(100);
		item.setNfw(90);

		ClosedFishingWebsiteRateItem item2 = new ClosedFishingWebsiteRateItem();
		item2.setReportDate(new Date());
		item2.setRiskCode("002");
		item.setNcfw(1000);
		item.setNfw(900);

		List<ClosedFishingWebsiteRateItem> list = new ArrayList<ClosedFishingWebsiteRateItem>();
		list.add(item);
		list.add(item2);

		// 通过JAVA编码构造JSON对象
		JSONObject object = new JSONObject();
		object.put("key", "0000111122223333");
		object.put("items", JSONArray.fromObject(list));
		HttpPost httpPost = new HttpPost("http://192.168.3.14:9999/rest/risk/");
		StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpClient client = new DefaultHttpClient();
		// 发送请求并解析返回结果
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			// 服务端返回的提示信息
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportExternalAttackChangeRateItems() throws Exception {
		ExternalAttackChangeRateItem item = new ExternalAttackChangeRateItem();
		item.setReportDate(DateUtil.SHORT_FORMAT.parse("2014-01-05"));
		item.setRiskCode("5001");
		item.setNidscp(100);
		item.setNipscp(90);

		List<ExternalAttackChangeRateItem> list = new ArrayList<ExternalAttackChangeRateItem>();
		list.add(item);

		// 通过JAVA编码构造JSON对象
		JSONObject object = new JSONObject();
		object.put("organization", "001");
		long time = new Date().getTime();
		String totalKey = "001:01037148031781818771407166668888:" + time;
		object.put("key", EncodeUtils.ecodeByMD5(totalKey));
		object.put("time", time);
		object.put("items", JSONArray.fromObject(list));
		HttpPost httpPost = new HttpPost("http://localhost:9999/bankriskcontrol/rest/risk/externalattackchangerate");
		StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpClient client = new DefaultHttpClient();
		// 发送请求并解析返回结果
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			// 服务端返回的提示信息
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportInformationRiskEventItems() throws Exception {
		InformationRiskEventItem item = new InformationRiskEventItem();
		item.setReportDate(new Date());
		item.setRiskCode("001");
		item.setCount(100);

		InformationRiskEventItem item2 = new InformationRiskEventItem();
		item2.setReportDate(new Date());
		item2.setRiskCode("002");
		item.setCount(1000);

		List<InformationRiskEventItem> list = new ArrayList<InformationRiskEventItem>();
		list.add(item);
		list.add(item2);

		// 通过JAVA编码构造JSON对象
		JSONObject object = new JSONObject();
		object.put("key", "0000111122223333");
		object.put("items", JSONArray.fromObject(list));
		HttpPost httpPost = new HttpPost("http://192.168.3.14:9999/rest/risk/001/informationriskevent");
		StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpClient client = new DefaultHttpClient();
		// 发送请求并解析返回结果
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			// 服务端返回的提示信息
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportMainElectronicChannelTransactionChangeRateItems() throws Exception {
		MainElectronicChannelTransactionChangeRateItem item = new MainElectronicChannelTransactionChangeRateItem();
		item.setReportDate(new Date());
		item.setRiskCode("001");
		item.setNtcp(100);

		MainElectronicChannelTransactionChangeRateItem item2 = new MainElectronicChannelTransactionChangeRateItem();
		item2.setReportDate(new Date());
		item2.setRiskCode("002");
		item.setNtcp(1000);

		List<MainElectronicChannelTransactionChangeRateItem> list = new ArrayList<MainElectronicChannelTransactionChangeRateItem>();
		list.add(item);
		list.add(item2);

		// 通过JAVA编码构造JSON对象
		JSONObject object = new JSONObject();
		object.put("key", "0000111122223333");
		object.put("items", JSONArray.fromObject(list));
		HttpPost httpPost = new HttpPost(
				"http://192.168.3.14:9999/rest/risk/001/electronicchanneltransactionchangerate");
		StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpClient client = new DefaultHttpClient();
		// 发送请求并解析返回结果
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			// 服务端返回的提示信息
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportMainElectronicBankActiveUserChangeRateItems() throws Exception {
		MainElectronicBankActiveUserChangeRateItem item = new MainElectronicBankActiveUserChangeRateItem();
		item.setReportDate(new Date());
		item.setRiskCode("001");
		item.setNaucp(100);

		MainElectronicBankActiveUserChangeRateItem item2 = new MainElectronicBankActiveUserChangeRateItem();
		item2.setReportDate(new Date());
		item2.setRiskCode("002");
		item.setNaucp(1000);

		List<MainElectronicBankActiveUserChangeRateItem> list = new ArrayList<MainElectronicBankActiveUserChangeRateItem>();
		list.add(item);
		list.add(item2);

		// 通过JAVA编码构造JSON对象
		JSONObject object = new JSONObject();
		object.put("key", "0000111122223333");
		object.put("items", JSONArray.fromObject(list));
		HttpPost httpPost = new HttpPost("http://192.168.3.14:9999/rest/risk/001/electronicbankactiveuserchangerate");
		StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpClient client = new DefaultHttpClient();
		// 发送请求并解析返回结果
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			// 服务端返回的提示信息
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportFishing() throws Exception {
		ClosedFishingWebsiteRateItem item = new ClosedFishingWebsiteRateItem();
		item.setReportDate(new Date(2014 - 1900, 9, 31, 0, 0, 0));
		item.setRiskCode("4001");		
		item.setNcfw(100);
		item.setNfw(110);
		// 通过JAVA编码构造JSON对象
		JSONObject object = new JSONObject();
		object.put("organization", "001");
		long time = new Date().getTime();
		String totalKey = "001:01037148031781818771407166668888:" + time;
		object.put("key", EncodeUtils.ecodeByMD5(totalKey));
		object.put("time", time);
		object.put("items", JSONArray.fromObject(item));
		HttpPost httpPost = new HttpPost("http://localhost:9999/bankriskcontrol/rest/risk/closedfishingwebsiterate");
		StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpClient client = new DefaultHttpClient();
		// 发送请求并解析返回结果
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			// 服务端返回的提示信息
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}
}
