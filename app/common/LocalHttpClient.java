package common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.text.html.parser.Entity;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.spdy.SpdyHeaders.HttpNames;

import scala.util.parsing.json.JSON;



public class LocalHttpClient {

	private static final String APPLICATION_JSON="application/json" ;
	
	private static final String TEXT_JSON="text/json";
	
	public static HttpPost Post(String uri,Map<String, String> params){
		
		HttpPost post=new HttpPost(uri);
		
		List<NameValuePair> pairs=new ArrayList<NameValuePair>();
		NameValuePair[] valuePairs;
		Set<String> keySet= params.keySet();
		try {
			for (String key : keySet) {
				pairs.add(new BasicNameValuePair(key, params.get(key)));
//				post.getParams().setParameter(key, params.get(key));
//				System.out.println(key+":"+params.get(key));
			}
//			valuePairs=(NameValuePair[]) pairs.toArray();
			post.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8)); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return post;
		
	}

	
	public static HttpPost PostJson(String uri,Map<String, String> params){
		
		HttpPost post=new HttpPost(uri);
		
		Set<String> keySet= params.keySet();
		
		post.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
		String jsonContent="{";
		try {
			for (String key : keySet) {
				jsonContent+="\""+key+"\":\""+params.get(key)+"\",";;
			}
			jsonContent=jsonContent.substring(0, jsonContent.length()-1);
			jsonContent+="}";
			System.out.println(uri);
			System.out.println(jsonContent);
			StringEntity entity=new StringEntity(jsonContent, "UTF-8");
			post.setEntity(entity); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return post;
		
	}
	public static HttpResponse sendRequest(HttpUriRequest method){
		HttpClient httpClient=new DefaultHttpClient();
		HttpResponse  response = null;
		try {
			response=httpClient.execute(method);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	
	public static HttpGet Get(String uri,Map<String, String> params){
		
		String urlParams="";
		String url=uri;
		
		if(params.size()>0){
			Set<String> keySet= params.keySet();
			for (String key : keySet) {
				urlParams+=key+"="+params.get(key)+"&";
			}
			urlParams.substring(0,urlParams.length()-2);
			url+="?"+urlParams;
		}
		
		HttpGet get=new HttpGet(url);
		
		return get;
		
	}
	
	public static HttpResponse PostUrl(String uri,Map<String, String> params){
		HttpResponse  response = null;
		HttpPost httpPost;
		StringEntity entity;
		HttpClient httpClient;
		try {
			httpPost=new HttpPost(uri);
			httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
			JSONObject jsonObject=JSONObject.fromObject(params);
			System.out.println(uri);
			System.out.println(jsonObject.toString());
			entity=new StringEntity(jsonObject.toString(), HTTP.UTF_8);
			httpPost.setEntity(entity);
			httpClient=new DefaultHttpClient();
			response=httpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	
	public static void main(String[] args) {
		/*String filename="12312312.log";
		filename=filename.substring(filename.lastIndexOf("."), filename.length());
			System.out.println(filename);*/
		Map<String, String> params=new HashMap<String, String>();
		
		/*params.put("cmd", "sms");
		params.put("username", "13710006617");*/
		/*params.put("cmd", "car");
		params.put("uuid", "15a1343d3e6d465d89b5777a3707ac0c");*/
//		params.put("cmd", "queryCarBrand");
		/*params.put("cmd", "carstyle");
		params.put("uuid", "943ed38175f54959aab8ba225f307b41");*/
		/*params.put("cmd", "elitebuycar");
		params.put("mobile", "15800004965");
		params.put("name", "xiao");
		params.put("carType", "宝马3系");
		params.put("companyType", "0");
		params.put("payType", "0");
		params.put("payPeriod", "0");*/
		
		/*params.put("cmd", "parrallelcardetail");
		params.put("uuid", "9fd59592a50041c78ed6cbaed1411a79");*/
//		params.put("cmd", "parrallelcar");
		/*params.put("cmd", "queryCars");
		params.put("code", "B005");*/
		
//		params.put("cmd", "rentdetail");
		params.put("cmd", "qcadvert");
		params.put("qtype", "1");
		/*params.put("plans", "SEATEL_INCREASING_AMOUNT,Voice_20_Post,defualtSms");
		params.put("immediate", "2");
		params.put("phone", "0189404730");
		params.put("token", "24515e69c4b24685b42ff57091b55679");*/
//		params.put("uuid", "7fe5e79a7d584fac95b3c1bfafcbd7b1");
		/*params.put("phone", "0189404730");
		params.put("password", "123456");*/
//		params.put("payType", "零首付");
//		params.put("payPeriod", "24");
		
		/*params.put("cmd", "parrallelbuycar");
		params.put("uuid", "943ed38175f54959aab8ba225f307b41");
		params.put("mobile", "15800004965");
		params.put("name", "xiao");
		params.put("carType", "测试车");
		params.put("province", "广东");
		params.put("city", "广州");
		params.put("price", "100万");
		params.put("isLoan", "是");*/
		
//		HttpResponse response=LocalHttpClient.PostUrl("http://127.0.0.1:9000//appPlanList", params);
//		
//		HttpResponse response= LocalHttpClient.sendRequest(LocalHttpClient.Post("http://120.24.49.116/rqInvokeController", params));

//		HttpResponse response= LocalHttpClient.sendRequest(LocalHttpClient.PostJson("http://120.24.49.116/rqInvokeController", params));
//		HttpResponse response= LocalHttpClient.sendRequest(LocalHttpClient.PostJson("http://127.0.0.1:9000/rqInvokeController", params));
//		HttpResponse response= LocalHttpClient.sendRequest(LocalHttpClient.PostJson("http://120.24.49.116:9000/rqInvokeController", params));
		/*for (int i = 1; i < 32; i++) {
		HttpResponse response= LocalHttpClient.sendRequest(LocalHttpClient.Get("http://api.chedai.bitauto.com/api/Common/GetCities?provinceId="+i+"&callback=jQuery18303040370149537921_1454060007605", params));
		
//		HttpResponse response= LocalHttpClient.PostUrl("http://127.0.0.1:8080/rqInvokeController", params);

//		HttpResponse response= LocalHttpClient.sendRequest(LocalHttpClient.Get("http://baidu.com", new HashMap<String, String>()));
		System.out.println("StatusCode:"+response.getStatusLine().getStatusCode());
			try {
				String result=EntityUtils.toString(response.getEntity());
				System.out.println(result);
				result=result.substring(result.indexOf("(")+1,result.length()-1);
				JSONObject object=JSONObject.fromObject(result);
				System.out.println(object.get("Data"));
				JSONArray array= JSONArray.fromObject(object.get("Data"));
				for (int j = 0; j < array.size(); j++) {
					System.out.println(array.get(j));
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		HttpResponse response= LocalHttpClient.sendRequest(LocalHttpClient.PostJson("http://www.qchepro.com:8080/rqInvokeController", params));

		System.out.println("StatusCode:"+response.getStatusLine().getStatusCode());
		String result;
		try {
			result = EntityUtils.toString(response.getEntity());
			System.out.println(result);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
