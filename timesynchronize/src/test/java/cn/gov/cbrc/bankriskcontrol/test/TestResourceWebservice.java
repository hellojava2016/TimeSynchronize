package cn.gov.cbrc.bankriskcontrol.test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import cn.gov.cbrc.bankriskcontrol.entity.ComputerRoomInfo;
import cn.gov.cbrc.bankriskcontrol.entity.DatabaseInfo;
import cn.gov.cbrc.bankriskcontrol.entity.MiddlewareInfo;
import cn.gov.cbrc.bankriskcontrol.entity.NetworkEquipmentInfo;
import cn.gov.cbrc.bankriskcontrol.entity.OperateSystemInfo;
import cn.gov.cbrc.bankriskcontrol.entity.PcServer;
import cn.gov.cbrc.bankriskcontrol.entity.PrecisionAcInfo;
import cn.gov.cbrc.bankriskcontrol.entity.StorageSystemInfo;
import cn.gov.cbrc.bankriskcontrol.entity.UpsInfo;
import cn.gov.cbrc.bankriskcontrol.util.encode.EncodeUtils;

//注意设置的值不能使null，否则不存在 object.put("time", null);则JSON中不会存在time这个属性s
public class TestResourceWebservice {
	private static String url = "http://localhost:9999/bankriskcontrol/rest/asset";
	private static String orgNo = "BJBK";
	private static String orgKey = "01071193387580474171410454334001";
	private static long time = 1410538279421L;
	private static String totalKey = orgNo + ":" + orgKey + ":" + time;

	public static void main(String[] args) throws Exception {
//		 reportPcserver_add();
		// reportPcserver_delete();
		 reportPcserver_update();
//		 reportOperateSystemInfo_add();
		// reportOperateSystemInfo_delete();
//		reportOperateSystemInfo_update();
//		 reportDatabaseInfo_add();
//		reportDatabaseInfo_update();
//		 reportDatabaseInfo_delete();
//		reportMiddlewareInfo_add();
//		reportMiddlewareInfo_update();
//		reportMiddlewareInfo_delete();
//		reportStorageSystemInfo_add();
//		reportStorageSystemInfo_update();
//		reportStorageSystemInfo_delete();
//		reportNetworkEquipmentInfo_add();
//		reportNetworkEquipmentInfo_update();
//		reportNetworkEquipmentInfo_delete();
//		reportComputerRoomInfo_add();
//		reportComputerRoomInfo_update();
//		reportComputerRoomInfo_delete();
//		reportUpsInfo_add();
//		reportUpsInfo_update();
//		reportUpsInfo_delete();
//		 reportPrecisionAcInfo_add();
//		 reportPrecisionAcInfo_update();
//		reportPrecisionAcInfo_delete();
	}

	public static void reportPcserver_add() throws Exception {
		JSONObject object1=new JSONObject();
		object1.put("areaCode","001");
		object1.put("uniqueVal","11-22-33-44-55-67");
				object1.put("purpose","purpose");
				object1.put("serialNumber","001002003004005006");
				object1.put("manufacturer","IBM");
				object1.put("type","PC");
				object1.put("name","IBM System x3500 M4");
				object1.put("ip","192.168.1.1");
				object1.put("serviceTime","2014-10-27");
				object1.put("cpu","E200");
				object1.put("cpuCount",1);
				object1.put("memorySize",1000);
				object1.put("hardDiskSize",2000);
				object1.put("location","机房1");
				object1.put("category",0);

		HttpPost httpPost = new HttpPost(url + "/pcserver");
		JSONObject object = new JSONObject();
		object.put("organization", orgNo);
		object.put("key", EncodeUtils.ecodeByMD5(totalKey));
		object.put("time", time);
		object.put("items", JSONArray.fromObject(object1));

		StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportPcserver_delete() throws Exception {
		HttpDelete httpDelete = new HttpDelete(url + "/pcserver/11-22-33-44-55-66");
		// 将Key封装在头部传输
		long time = new Date().getTime();
		String totalKey = "2:01037148031781818771407155761875:" + time;
		httpDelete.setHeader("key", EncodeUtils.ecodeByMD5(totalKey));
		httpDelete.setHeader("time", time + "");
		httpDelete.setHeader("organization", "2");
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpDelete);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportPcserver_update() throws Exception {
		HttpPut httpPost = new HttpPut(url + "/pcserver/11-22-33-44-55-67");
		// 封装需要更新的属性
		JSONObject object = new JSONObject();
		object.put("organization", orgNo);
		object.put("key", EncodeUtils.ecodeByMD5(totalKey));
		object.put("time", time);
		
		JSONObject item = new JSONObject();
		item.put("name", "新名字");
		item.put("serviceTime", "2014-01-02");
		item.put("cpuCount", 100);
		object.put("item", item);

		StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		@SuppressWarnings({ "resource", "deprecation" })
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportOperateSystemInfo_add() throws Exception {
		OperateSystemInfo server1 = new OperateSystemInfo();
		server1.setUniqueVal("11-22-33-44-55-66");
		server1.setHost("11-22-33-44-55-88");
		server1.setVersion("XP Family Version");
		server1.setPatch("SP3");
		server1.setType("PC");
		server1.setAppSystem("app1,app2");

		OperateSystemInfo server2 = new OperateSystemInfo();
		server2.setUniqueVal("11-22-33-44-55-88");
		server2.setHost("11-22-33-44-55-88");
		server2.setVersion("XP Family Version");
		server2.setPatch("SP3");
		server2.setType("PC");
		server2.setAppSystem("app1");

		List<OperateSystemInfo> list = new ArrayList<OperateSystemInfo>();
		list.add(server1);
		list.add(server2);

		HttpPost httpPost = new HttpPost(url + "/os");
		JSONObject object = new JSONObject();
		object.put("organization", orgNo);
		object.put("key", EncodeUtils.ecodeByMD5(totalKey));
		object.put("time", time);
		object.put("items", JSONArray.fromObject(list));

		StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportOperateSystemInfo_delete() throws Exception {
		HttpDelete httpDelete = new HttpDelete(url + "/os/11-22-33-44-55-88");
		httpDelete.setHeader("key", EncodeUtils.ecodeByMD5(totalKey));
		httpDelete.setHeader("time", time + "");
		httpDelete.setHeader("organization", orgNo);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpDelete);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportOperateSystemInfo_update() throws Exception {
		HttpPut httpPost = new HttpPut(url + "/os/11-22-33-44-55-66");
		// 封装需要更新的属性
		JSONObject object = new JSONObject();
		object.put("organization", orgNo);
		object.put("key", EncodeUtils.ecodeByMD5(totalKey));
		object.put("time", time);
		JSONObject item = new JSONObject();
		item.put("host", "11-22-33-44-55-88");
		item.put("patch", "SP4");
		item.put("appSystem", "app2");
		object.put("item", item);

		StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		@SuppressWarnings({ "resource", "deprecation" })
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportDatabaseInfo_add() throws Exception {
		DatabaseInfo server1 = new DatabaseInfo();
		server1.setUniqueVal("11-22-33-44-55-66");
		server1.setVersion("XP Family Version");
		server1.setPatch("SP3");
		server1.setType("PC");
		server1.setServerTime(new Date());
		server1.setDatabaseName("DB2");
		server1.setAppSystem("app1");
		server1.setOperateSystem("11-22-33-44-55-66");

		DatabaseInfo server2 = new DatabaseInfo();
		server2.setUniqueVal("11-22-33-44-55-88");
		server2.setVersion("XP Family Version");
		server2.setPatch("SP3");
		server2.setType("PC");
		server2.setServerTime(new Date());
		server2.setDatabaseName("DB2");
		server2.setOperateSystem("11-22-33-44-55-66");
		List<DatabaseInfo> list = new ArrayList<DatabaseInfo>();
		list.add(server1);
		list.add(server2);

		HttpPost httpPost = new HttpPost(url + "/db");
		JSONObject object = new JSONObject();
		object.put("organization", orgNo);
		object.put("key", EncodeUtils.ecodeByMD5(totalKey));
		object.put("time", time);
		object.put("items", JSONArray.fromObject(list));

		StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportDatabaseInfo_delete() throws Exception {
		HttpDelete httpDelete = new HttpDelete(url + "/db/11-22-33-44-55-66");
		// 将Key封装在头部传输
		httpDelete.setHeader("key", EncodeUtils.ecodeByMD5(totalKey));
		httpDelete.setHeader("time", time + "");
		httpDelete.setHeader("organization", orgNo);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpDelete);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportDatabaseInfo_update() throws Exception {
		HttpPut httpPost = new HttpPut(url + "/db/11-22-33-44-55-66");
		// 封装需要更新的属性
		JSONObject object = new JSONObject();		
		object.put("organization", orgNo);
		object.put("key", EncodeUtils.ecodeByMD5(totalKey));
		object.put("time", time);
		JSONObject item = new JSONObject();
		item.put("patch", "SP4");
		item.put("appSystem", "app2,app1");
		object.put("item", item);

		StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		@SuppressWarnings({ "resource", "deprecation" })
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportMiddlewareInfo_add() throws Exception {
		MiddlewareInfo server1 = new MiddlewareInfo();
		server1.setUniqueVal("11-22-33-44-55-66");
		server1.setOperateSystem("11-22-33-44-55-88");
		server1.setVersion("7.0.53");
		server1.setPatch("SP3");
		server1.setType("PC");
		server1.setServerTime(new Date());
		server1.setMiddlewareName("Tomcat");
		server1.setAppSystem("app1");

		MiddlewareInfo server2 = new MiddlewareInfo();
		server2.setUniqueVal("11-22-33-44-55-88");
		server2.setOperateSystem("11-22-33-44-55-88");
		server2.setVersion("7.0.53");
		server2.setPatch("SP3");
		server2.setType("PC");
		server2.setServerTime(new Date());
		server2.setMiddlewareName("Tomcat2");
		server2.setAppSystem("app2");

		List<MiddlewareInfo> list = new ArrayList<MiddlewareInfo>();
		list.add(server1);
		list.add(server2);

		HttpPost httpPost = new HttpPost(url + "/mw");
		JSONObject object = new JSONObject();
		object.put("organization", orgNo);
		object.put("key", EncodeUtils.ecodeByMD5(totalKey));
		object.put("time", time);
		object.put("items", JSONArray.fromObject(list));

		StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportMiddlewareInfo_delete() throws Exception {
		HttpDelete httpDelete = new HttpDelete(url + "/mw/11-22-33-44-55-66");
		// 将Key封装在头部传输
		httpDelete.setHeader("key", EncodeUtils.ecodeByMD5(totalKey));
		httpDelete.setHeader("time", time + "");
		httpDelete.setHeader("organization", orgNo);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpDelete);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportMiddlewareInfo_update() throws Exception {
		HttpPut httpPost = new HttpPut(url + "/mw/11-22-33-44-55-66");
		// 封装需要更新的属性
		JSONObject object = new JSONObject();
		object.put("organization", orgNo);
		object.put("key", EncodeUtils.ecodeByMD5(totalKey));
		object.put("time", time);
		JSONObject item = new JSONObject();
		item.put("patch", "7.0.53-1");
		item.put("appSystem", "app2");
		object.put("item", item);

		StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		@SuppressWarnings({ "resource", "deprecation" })
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportNetworkEquipmentInfo_add() throws Exception {
		NetworkEquipmentInfo server1 = new NetworkEquipmentInfo();
		server1.setUniqueVal("11-22-33-44-55-66");
		server1.setPurpose("purpose");
		server1.setSerialNumber("001002003004005006");
		server1.setManufacturer("IBM");
		server1.setType("PC");
		server1.setName("IBM System x3500 M4");
		server1.setPortCount(4);
		server1.setLocation("机房1");
		server1.setVersion("V300");
		server1.setAppSystem("app1,app2");

		NetworkEquipmentInfo server2 = new NetworkEquipmentInfo();
		server2.setUniqueVal("11-22-33-44-55-88");
		server2.setPurpose("purpose");
		server2.setSerialNumber("001002003004005006");
		server2.setManufacturer("IBM");
		server2.setType("PC");
		server2.setName("IBM System x3500 M4");
		server2.setPortCount(4);
		server2.setLocation("机房1");
		server2.setVersion("V300");
		server2.setAppSystem("app2");

		List<NetworkEquipmentInfo> list = new ArrayList<NetworkEquipmentInfo>();
		list.add(server1);
		list.add(server2);

		HttpPost httpPost = new HttpPost(url + "/ne");
		JSONObject object = new JSONObject();
		object.put("organization", orgNo);
		object.put("key", EncodeUtils.ecodeByMD5(totalKey));
		object.put("time", time);
		object.put("items", JSONArray.fromObject(list));

		StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportNetworkEquipmentInfo_delete() throws Exception {
		HttpDelete httpDelete = new HttpDelete(url + "/ne/11-22-33-44-55-88");
		// 将Key封装在头部传输
		httpDelete.setHeader("key", EncodeUtils.ecodeByMD5(totalKey));
		httpDelete.setHeader("time", time + "");
		httpDelete.setHeader("organization", orgNo);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpDelete);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportNetworkEquipmentInfo_update() throws Exception {
		HttpPut httpPost = new HttpPut(url + "/ne/11-22-33-44-55-66");
		// 封装需要更新的属性
		JSONObject object = new JSONObject();
		object.put("organization", orgNo);
		object.put("key", EncodeUtils.ecodeByMD5(totalKey));
		object.put("time", time);
		JSONObject item = new JSONObject();
		item.put("location", "7.0.53-1");
		item.put("appSystem", "");
		object.put("item", item);

		StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		@SuppressWarnings({ "resource", "deprecation" })
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportStorageSystemInfo_add() throws Exception {
		StorageSystemInfo server1 = new StorageSystemInfo();
		server1.setUniqueVal("11-22-33-44-55-66");
		server1.setPurpose("purpose");
		server1.setSerialNumber("001002003004005006");
		server1.setManufacturer("IBM");
		server1.setType("PC");
		server1.setLocation("机房1");
		server1.setVersion("V300");
		server1.setStorageArraySize(100);
		server1.setStorageRaidMode("S1");
		server1.setStorageTapeMediaCount(4);
		server1.setStorageTapeMediaType("NTFS");
		server1.setStorageCacheSize(100);
		server1.setMicrocodeVersion("V100");
		server1.setDiskSpec("Spec");
		server1.setCapacityInfo("info");
		server1.setServiceTime(new Date());
		server1.setAppSystem("app1,app2");

		StorageSystemInfo server2 = new StorageSystemInfo();
		server2.setUniqueVal("11-22-33-44-55-88");
		server2.setPurpose("purpose");
		server2.setSerialNumber("001002003004005006");
		server2.setManufacturer("IBM");
		server2.setType("PC");
		server2.setLocation("机房1");
		server2.setVersion("V300");
		server2.setStorageArraySize(100);
		server2.setStorageRaidMode("S1");
		server2.setStorageTapeMediaCount(4);
		server2.setStorageTapeMediaType("NTFS");
		server2.setStorageCacheSize(100);
		server2.setMicrocodeVersion("V100");
		server2.setDiskSpec("Spec");
		server2.setCapacityInfo("info");
		server2.setServiceTime(new Date());
		server2.setAppSystem("app1");

		List<StorageSystemInfo> list = new ArrayList<StorageSystemInfo>();
		list.add(server1);
		list.add(server2);

		HttpPost httpPost = new HttpPost(url + "/ss");
		JSONObject object = new JSONObject();
		object.put("organization", orgNo);
		object.put("key", EncodeUtils.ecodeByMD5(totalKey));
		object.put("time", time);
		object.put("items", JSONArray.fromObject(list));

		StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportStorageSystemInfo_delete() throws Exception {
		HttpDelete httpDelete = new HttpDelete(url + "/ss/11-22-33-44-55-66");
		// 将Key封装在头部传输
		httpDelete.setHeader("key", EncodeUtils.ecodeByMD5(totalKey));
		httpDelete.setHeader("time", time + "");
		httpDelete.setHeader("organization", orgNo);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpDelete);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportStorageSystemInfo_update() throws Exception {
		HttpPut httpPost = new HttpPut(url + "/ss/11-22-33-44-55-66");
		// 封装需要更新的属性
		JSONObject object = new JSONObject();
		object.put("organization", orgNo);
		object.put("key", EncodeUtils.ecodeByMD5(totalKey));
		object.put("time", time);
		JSONObject item = new JSONObject();
		item.put("location", "新地址");
		item.put("version", "V400");
		item.put("appSystem", "app2");
		object.put("item", item);

		StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		@SuppressWarnings({ "resource", "deprecation" })
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportComputerRoomInfo_add() throws Exception {
		ComputerRoomInfo server1 = new ComputerRoomInfo();
		server1.setUniqueVal("11-22-33-44-55-66");
		server1.setPurpose("purpose");
		server1.setArea(10);
		server1.setManufacturer("IBM");
		server1.setPower(20000);
		server1.setLocation("机房1");
		server1.setUpsCount(1);
		server1.setPrecisionAcCount(1);
		server1.setServerTime(new Date());

		ComputerRoomInfo server2 = new ComputerRoomInfo();
		server2.setUniqueVal("11-22-33-44-55-88");
		server2.setPurpose("purpose");
		server2.setArea(10);
		server2.setManufacturer("IBM");
		server2.setPower(20000);
		server2.setLocation("机房1");
		server2.setUpsCount(1);
		server2.setPrecisionAcCount(1);
		server2.setServerTime(new Date());

		List<ComputerRoomInfo> list = new ArrayList<ComputerRoomInfo>();
		list.add(server1);
		list.add(server2);

		HttpPost httpPost = new HttpPost(url + "/cr");
		JSONObject object = new JSONObject();
		object.put("organization", orgNo);
		object.put("key", EncodeUtils.ecodeByMD5(totalKey));
		object.put("time", time);
		object.put("items", JSONArray.fromObject(list));

		StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportComputerRoomInfo_delete() throws Exception {
		HttpDelete httpDelete = new HttpDelete(url + "/cr/11-22-33-44-55-88");
		// 将Key封装在头部传输
		httpDelete.setHeader("key", EncodeUtils.ecodeByMD5(totalKey));
		httpDelete.setHeader("time", time + "");
		httpDelete.setHeader("organization", orgNo);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpDelete);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportComputerRoomInfo_update() throws Exception {
		HttpPut httpPost = new HttpPut(url + "/cr/11-22-33-44-55-66");
		// 封装需要更新的属性
		JSONObject object = new JSONObject();
		object.put("organization", orgNo);
		object.put("key", EncodeUtils.ecodeByMD5(totalKey));
		object.put("time", time);
		JSONObject item = new JSONObject();
		item.put("location", "新地址");
		item.put("area", 20);
		object.put("item", item);

		StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		@SuppressWarnings({ "resource", "deprecation" })
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportUpsInfo_add() throws Exception {
		UpsInfo server1 = new UpsInfo();
		server1.setUniqueVal("11-22-33-44-55-66");
		server1.setRoomUV("11-22-33-44-55-66");
		server1.setType("TYPE1");

		UpsInfo server2 = new UpsInfo();
		server2.setUniqueVal("11-22-33-44-55-88");
		server2.setRoomUV("11-22-33-44-55-66");
		server2.setType("TYPE1");

		List<UpsInfo> list = new ArrayList<UpsInfo>();
		list.add(server1);
		list.add(server2);

		HttpPost httpPost = new HttpPost(url + "/ups");
		JSONObject object = new JSONObject();
		object.put("organization", orgNo);
		object.put("key", EncodeUtils.ecodeByMD5(totalKey));
		object.put("time", time);
		object.put("items", JSONArray.fromObject(list));

		StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportUpsInfo_delete() throws Exception {
		HttpDelete httpDelete = new HttpDelete(url + "/ups/11-22-33-44-55-66");
		// 将Key封装在头部传输
		httpDelete.setHeader("key", EncodeUtils.ecodeByMD5(totalKey));
		httpDelete.setHeader("time", time + "");
		httpDelete.setHeader("organization", orgNo);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpDelete);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportUpsInfo_update() throws Exception {
		HttpPut httpPost = new HttpPut(url + "/ups/11-22-33-44-55-66");
		// 封装需要更新的属性
		JSONObject object = new JSONObject();
		object.put("organization", orgNo);
		object.put("key", EncodeUtils.ecodeByMD5(totalKey));
		object.put("time", time);
		JSONObject item = new JSONObject();
		item.put("outputFrequency", "1000");
		item.put("outputVoltage", "2000");
		object.put("item", item);
		

		StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		@SuppressWarnings({ "resource", "deprecation" })
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportPrecisionAcInfo_add() throws Exception {
		PrecisionAcInfo server1 = new PrecisionAcInfo();
		server1.setUniqueVal("11-22-33-44-55-66");
		server1.setRoomUV("11-22-33-44-55-66");
		server1.setType("TYPE1");

		PrecisionAcInfo server2 = new PrecisionAcInfo();
		server2.setUniqueVal("11-22-33-44-55-88");
		server2.setRoomUV("11-22-33-44-55-66");
		server2.setType("TYPE1");

		List<PrecisionAcInfo> list = new ArrayList<PrecisionAcInfo>();
		list.add(server1);
		list.add(server2);

		HttpPost httpPost = new HttpPost(url + "/ac");
		JSONObject object = new JSONObject();
		object.put("organization", orgNo);
		object.put("key", EncodeUtils.ecodeByMD5(totalKey));
		object.put("time", time);
		object.put("items", JSONArray.fromObject(list));

		StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportPrecisionAcInfo_delete() throws Exception {
		HttpDelete httpDelete = new HttpDelete(url + "/ac/11-22-33-44-55-66");
		// 将Key封装在头部传输
		httpDelete.setHeader("key", EncodeUtils.ecodeByMD5(totalKey));
		httpDelete.setHeader("time", time + "");
		httpDelete.setHeader("organization", orgNo);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpDelete);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}

	public static void reportPrecisionAcInfo_update() throws Exception {
		HttpPut httpPost = new HttpPut(url + "/ac/11-22-33-44-55-66");
		// 封装需要更新的属性
		JSONObject object = new JSONObject();
		object.put("organization", orgNo);
		object.put("key", EncodeUtils.ecodeByMD5(totalKey));
		object.put("time", time);
		JSONObject item = new JSONObject();
		item.put("supplyHumidity", "1000");
		item.put("type", "2000");
		object.put("item", item);

		StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		@SuppressWarnings({ "resource", "deprecation" })
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream stream = response.getEntity().getContent();
			String value = IOUtils.toString(stream);
			System.out.println(value);
		}
	}
}
