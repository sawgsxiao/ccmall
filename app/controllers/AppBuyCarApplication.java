package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;

import views.html.*;
import play.data.Form;
import play.data.DynamicForm;
// import play.data.valroleidation.Constraints.*;

import play.libs.Json;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;  
import com.fasterxml.jackson.databind.JsonMappingException;  
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.core.JsonParseException;  
import com.fasterxml.jackson.core.JsonProcessingException;

import play.api.libs.ws.Response;
import play.api.libs.ws.WS;
import play.data.DynamicForm;
import com.avaje.ebean.*;

import common.UUIDGenerator;
import common.UtilsObject;

import java.util.ArrayList;
import java.util.List;
import models.*;
import net.sf.json.JSONObject;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.lang.NullPointerException;
import java.io.ByteArrayOutputStream;
import javax.imageio.stream.FileImageInputStream;

import play.db.ebean.Model;

public class AppBuyCarApplication extends Controller {

	/*
	 * 其中：payType取值：0，零首付；1，首付20%"；2，首付30%
	 *	payPeriod取值：0，24个月；1，36个月
	 */
	
	@BodyParser.Of(BodyParser.Json.class)
	public static ObjectNode buyCar(Map<String, String> data) throws JsonProcessingException {
		// TODO Auto-generated method stub
		ObjectNode resultJson = Json.newObject();
		
		String mobile= data.get("mobile");
		String name= data.get("name");
		String carType= data.get("carType");
		String carPrice= data.get("carPrice");
		String payType= data.get("payType");
		String payPeriod= data.get("payPeriod");
		
		AppBuyCar buyCar=new AppBuyCar();
		buyCar.setCarprice(carPrice);
		buyCar.setCartype(carType);
		buyCar.setCreatetime(new Date());
		buyCar.setMobile(mobile);
		buyCar.setName(name);
		buyCar.setPayperiod(payPeriod);
		buyCar.setPaytype(payType);
		buyCar.save();
		resultJson.put("code", "0");
      	resultJson.put("msg", "添加成功！");
		return resultJson;
	}
	
	
/*	public static List<Advert> advertList(Map<String, String> data,int pageSize,int pageNow) {
		// TODO Auto-generated method stub
		ObjectNode resultJson = Json.newObject();
		JSONObject jsonObject = null;		

		StringBuffer buf = new StringBuffer(); 
		
    	int start=(pageNow-1)*pageSize;
    	
		//sql语句       
	 	String sql = "select id from cc_advert where 1=1 "+buf.toString()+" order by createtime desc "+" limit " +(start)+" , "+(pageSize);  

  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")
  		.create();
  		
  		Query<Advert> eQ = Ebean.find(Advert.class);
  		eQ.setRawSql(rawSql);
  		
  		List<Advert> list = eQ.findList();

		return list;
	}
	
	public static int advertCount(Map in){
		
		StringBuffer buf = new StringBuffer(); 
		
     	String sql = "select count(id) as count from cc_advert where 1=1 "+buf.toString();
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }*/
	/**
	 *  companyType: 0-"公务员",1-"事业单位职员",2-"国企职员",3-"世界500强企业正式员工"
		 payType:0-"零首付", 1-"首付20%", 2-"首付30%"
		 payPeriod:0-"24个月", 1-"36个月"
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static ObjectNode eliteBuyCar(Map<String, String> data) throws JsonProcessingException {
		// TODO Auto-generated method stub
		ObjectNode resultJson = Json.newObject();
		
		String mobile= data.get("mobile");
		String name= data.get("name");
		String carType= data.get("carType");
		String companyType= data.get("companyType");
		String payType= data.get("payType");
		String payPeriod= data.get("payPeriod");
		
		AppEliteBuyCar buyCar=new AppEliteBuyCar();
		buyCar.setCartype(carType);
		buyCar.setCompanytype(companyType);
		buyCar.setMobile(mobile);
		buyCar.setName(name);
		buyCar.setPayperiod(payPeriod);
		buyCar.setPaytype(payType);
		buyCar.setCreatetime(new Date());
		buyCar.save();
		resultJson.put("code", "0");
      	resultJson.put("msg", "添加成功！");
		return resultJson;
	}
	
	/**
	 *  isLoan:是否贷款
	 *  province:省
	 *  city：市
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static ObjectNode parrallelBuyCar(Map<String, String> data) throws JsonProcessingException {
		// TODO Auto-generated method stub
		ObjectNode resultJson = Json.newObject();
		
		String mobile= data.get("mobile");
		String name= data.get("name");
		String uuid= data.get("uuid");
		String isloan= data.get("isLoan");
//		String province= data.get("province");
		String city= data.get("city");
		String price= data.get("price");
		String cartype= data.get("carType");
		
		AppParrallelBuyCar buyCar=new AppParrallelBuyCar();
		buyCar.setCartype(cartype);
		buyCar.setCity(city);
		buyCar.setCreatetime(new Date());
		buyCar.setIsloan(isloan);
		buyCar.setMobile(mobile);
		buyCar.setName(name);
		buyCar.setPrice(price);
//		buyCar.setProvince(province);
		buyCar.setUuid(uuid);
		buyCar.save();
		resultJson.put("code", "0");
      	resultJson.put("msg", "添加成功！");
		return resultJson;
	}
	
	@BodyParser.Of(BodyParser.Json.class)
	public static ObjectNode rentBuyCar(Map<String, String> data) throws JsonProcessingException {
		// TODO Auto-generated method stub
		ObjectNode resultJson = Json.newObject();
		
		String mobile= data.get("mobile");
		String name= data.get("name");
		String uuid= data.get("uuid");

		String price= data.get("price");
		String cartype= data.get("carType");
		
		AppRentBuyCar buyCar=new AppRentBuyCar();
		buyCar.setCartype(cartype);
		buyCar.setCreatetime(new Date());
		buyCar.setMobile(mobile);
		buyCar.setName(name);
		buyCar.setPrice(price);
		buyCar.setUuid(uuid);
		buyCar.save();
		resultJson.put("code", "0");
      	resultJson.put("msg", "添加成功！");
		return resultJson;
	}
	
	@BodyParser.Of(BodyParser.Json.class)
	public static ObjectNode yueanBuyCar(Map<String, String> data) throws JsonProcessingException {
		// TODO Auto-generated method stub
		ObjectNode resultJson = Json.newObject();
		
		String mobile= data.get("mobile");
		String name= data.get("name");
		String uuid= data.get("uuid");
		String price= data.get("price");
		String cartype= data.get("carType");
		String payType= data.get("payType");
		String payPeriod= data.get("payPeriod");
		AppYueanBuyCar buyCar=new AppYueanBuyCar();
		buyCar.setCartype(cartype);
		buyCar.setCreatetime(new Date());
		buyCar.setMobile(mobile);
		buyCar.setName(name);
		buyCar.setPrice(price);
		buyCar.setUuid(uuid);
		buyCar.setPayperiod(payPeriod);
		buyCar.setPaytype(payType);
		buyCar.save();
		resultJson.put("code", "0");
      	resultJson.put("msg", "添加成功！");
		return resultJson;
	}
}
