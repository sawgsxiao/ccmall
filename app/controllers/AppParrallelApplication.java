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

public class AppParrallelApplication extends Controller {

	@BodyParser.Of(BodyParser.Json.class)
	public static ObjectNode parrallelList(Map<String, String> data) throws JsonProcessingException {
		// TODO Auto-generated method stub
		ObjectNode resultJson = Json.newObject();
		JSONObject jsonObject = null;		

		ObjectMapper listMapper = new ObjectMapper(); 
		
		
		String pageSizeIn = (String)data.get("pagesize");
		
		//每页显示数据
		int pageSize = 0;
		
		if(pageSizeIn==null||pageSizeIn.equals("")){
			
			pageSize = Integer.parseInt(Play.application().configuration().getString("pageSize"));
		}else{
			
			pageSize = Integer.parseInt(pageSizeIn);
		}
		
		//当前第N页
		int pageNow = 1;
		
		String pageNowIn = (String)data.get("pagenow");
		if(pageNowIn!=null&&!pageNowIn.equals("0")){
		
			pageNow = Integer.parseInt(pageNowIn);
		}
		
		List<Parrallel> list =parrallelList(data,pageSize,pageNow);
		
		
		//总页数
		int countAll = parrallelCount(data);
		
		//第N页
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
		
		if(list.size()==0){
			resultJson.put("code", "5");
			resultJson.put("msg", "没有数据！");
			return resultJson;
		}
		
		ArrayNode arrayNode=listMapper.createArrayNode();
		
		for (Parrallel advert : list) {
			ObjectNode node=Json.newObject();
			node.put("uuid", advert.getUuid());
			node.put("title", advert.getName());
			node.put("image", Play.application().configuration().getString("ippath")+Play.application().configuration().getString("outpath")+"/"+advert.getImgurl());
			arrayNode.add(node);
		}
		
		
		resultJson.put("code", "0");
		resultJson.put("list", arrayNode);
      	resultJson.put("msg", "查询数据成功！");
		return resultJson;
	}
	
	
	public static List<Parrallel> parrallelList(Map<String, String> data,int pageSize,int pageNow) {
		// TODO Auto-generated method stub
		ObjectNode resultJson = Json.newObject();
		JSONObject jsonObject = null;		

		StringBuffer buf = new StringBuffer(); 
		
    	int start=(pageNow-1)*pageSize;
    	
		//sql语句       
	 	String sql = "select id from cc_parrallel where 1=1 "+buf.toString()+" order by createtime desc "+" limit " +(start)+" , "+(pageSize);  

  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")
  		.create();
  		
  		Query<Parrallel> eQ = Ebean.find(Parrallel.class);
  		eQ.setRawSql(rawSql);
  		
  		List<Parrallel> list = eQ.findList();

		return list;
	}
	
	public static int parrallelCount(Map in){
		
		StringBuffer buf = new StringBuffer(); 
		
     	String sql = "select count(id) as count from cc_parrallel where 1=1 "+buf.toString();
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }
	
	@BodyParser.Of(BodyParser.Json.class)
	public static ObjectNode parrallelCarList(Map<String, String> data) throws JsonProcessingException {
		// TODO Auto-generated method stub
		ObjectNode resultJson = Json.newObject();
		JSONObject jsonObject = null;		

		ObjectMapper listMapper = new ObjectMapper(); 
		
		
		String pageSizeIn = (String)data.get("pagesize");
		
		//每页显示数据
		int pageSize = 0;
		
		if(pageSizeIn==null||pageSizeIn.equals("")){
			
			pageSize = Integer.parseInt(Play.application().configuration().getString("pageSize"));
		}else{
			
			pageSize = Integer.parseInt(pageSizeIn);
		}
		
		//当前第N页
		int pageNow = 1;
		
		String pageNowIn = (String)data.get("pagenow");
		if(pageNowIn!=null&&!pageNowIn.equals("0")){
		
			pageNow = Integer.parseInt(pageNowIn);
		}
		
		List<AppParrallelCar> list =parrallelCarList(data,pageSize,pageNow);
		
		
		//总页数
		int countAll = parrallelCount(data);
		
		//第N页
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
		
		if(list.size()==0){
			resultJson.put("code", "5");
			resultJson.put("msg", "没有数据！");
			return resultJson;
		}
		
		ArrayNode arrayNode=listMapper.createArrayNode();
		
		for (AppParrallelCar car : list) {
			ObjectNode node=Json.newObject();
			node.put("uuid", car.getUuid());
			node.put("name", car.getAppCarStyle().getStylename());
			node.put("desc", car.getThedesc());
			node.put("price", car.getAppCarStyle().getPrice());
			node.put("spec", car.getSpec());
			node.put("stock", car.getStock());
			if(car.getAppCarStyle().getAppCar().getImages().size()>0){
				node.put("image", Play.application().configuration().getString("ippath")+Play.application().configuration().getString("outpath")+"/"+car.getAppCarStyle().getAppCar().getImages().get(0).getUrl());
			}
			arrayNode.add(node);
		}

		resultJson.put("code", "0");
		resultJson.put("list", arrayNode);
      	resultJson.put("msg", "查询数据成功！");
		return resultJson;
	}
	
	public static List<AppParrallelCar> parrallelCarList(Map<String, String> data,int pageSize,int pageNow) {
		// TODO Auto-generated method stub
		ObjectNode resultJson = Json.newObject();
		JSONObject jsonObject = null;		
		int start=(pageNow-1)*pageSize;
		
		StringBuffer buf = new StringBuffer(); 
		
		buf.append(" and car.stand='1' ");
		    	
		//sql语句       
	 	String sql = "select hot.id id from cc_app_car car,cc_app_parrallel_car hot,cc_app_car_style st where 1=1 and car.id=st.carid and st.parrallelid=hot.id "+buf.toString()+" order by hot.createtime "+" limit " +(start)+" , "+(pageSize);;  

  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")
  		.create();
  		
  		Query<AppParrallelCar> eQ = Ebean.find(AppParrallelCar.class);
  		eQ.setRawSql(rawSql);
  		
  		List<AppParrallelCar> list = eQ.findList();

		return list;
	}
	
	public static int parrallelCarCount(Map in){
		
		StringBuffer buf = new StringBuffer(); 
		
     	String sql = "select count(hot.id) as count from cc_app_car car,cc_app_parrallel_car hot,cc_app_car_style st where 1=1 and car.id=st.carid and st.parrallelid=hot.id "+buf.toString();
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }
	
	@BodyParser.Of(BodyParser.Json.class)
	public static ObjectNode parrallelCarDetail(Map<String, String> data) throws JsonProcessingException {
		// TODO Auto-generated method stub
		ObjectNode resultJson = Json.newObject();

		ObjectMapper listMapper = new ObjectMapper(); 
				
		String uuid=(String)data.get("uuid");
				
		AppParrallelCar car= AppParrallelCar.findByUuid(uuid);
		AppCar appCar= car.getAppCarStyle().getAppCar();
		List<AppCarImage> es= appCar.getImages();
		
		if(car==null){
			resultJson.put("code", "5");
			resultJson.put("msg", "没有数据！");
			return resultJson;
		}
		ArrayNode arrayNode=listMapper.createArrayNode();
		ObjectNode nodeJson = Json.newObject();
		if(es!=null&&es.size()>0){
			for (AppCarImage appCarImage : es) {
				ObjectNode imgJson = Json.newObject();
				imgJson.put("image", Play.application().configuration().getString("ippath")+Play.application().configuration().getString("outpath")+"/"+appCarImage.getUrl());
				arrayNode.add(imgJson);
			}
		}
		nodeJson.put("id", "2016"+car.getAppCarStyle().getId());
		nodeJson.put("name", car.getAppCarStyle().getStylename());
		nodeJson.put("price", car.getAppCarStyle().getPrice());
		nodeJson.put("disprice", car.getThedesc());
		nodeJson.put("spec", car.getSpec());
		nodeJson.put("stock", car.getStock());
		resultJson.put("code", "0");
		nodeJson.put("list", arrayNode);
		resultJson.put("data", nodeJson);
      	resultJson.put("msg", "查询数据成功！");
		return resultJson;
	}
}
