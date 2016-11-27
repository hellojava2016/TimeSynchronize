package cn.gov.cbrc.bankriskcontrol.test;

import java.io.InputStream;
import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import cn.gov.cbrc.bankriskcontrol.util.encode.EncodeUtils;


public class TestMessageWebservice {
	private static String url = "http://localhost:9999/bankriskcontrol/rest/message";
	private static String orgNo = "BJBK";
	private static String orgKey = "01071193387580474171410454334001";
	private static long time = 1410538279421L;
	private static String totalKey = orgNo + ":" + orgKey + ":" + time;

	public static void main(String[] args) throws Exception {
		 message_query();
		 //message_delete();
	}

	public static void message_query() throws Exception {
		HttpPost httpPost = new HttpPost(url);		
		JSONObject object = new JSONObject();
		object.put("organization", orgNo);
		object.put("key", EncodeUtils.ecodeByMD5(totalKey));
		object.put("time", time);

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
}
