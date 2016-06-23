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

public class AppCarApplication extends Controller {

	@BodyParser.Of(BodyParser.Json.class)
	public static ObjectNode carAssortList(Map<String, String> data) throws JsonProcessingException {
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
		
		List<CarAssort> list =assortList(data,pageSize,pageNow);
		//总页数
		int countAll = assortCount(data);
		
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
		
		for (CarAssort carAssort : list) {
			ObjectNode node=Json.newObject();
			node.put("id", carAssort.getId());
			node.put("assortname", carAssort.getAssortname());
			arrayNode.add(node);
		}
		
		resultJson.put("code", "0");
		resultJson.put("list", arrayNode);
		resultJson.put("countall", countAll);
      	resultJson.put("pagenow", pageNow);
      	resultJson.put("pagecount", pageCount);
      	resultJson.put("pagesize", pageSize);
      	resultJson.put("msg", "查询数据成功！");
		return resultJson;
	}
	
	
	public static List<CarAssort> assortList(Map<String, String> data,int pageSize,int pageNow) {
		// TODO Auto-generated method stub
		ObjectNode resultJson = Json.newObject();
		JSONObject jsonObject = null;		

		StringBuffer buf = new StringBuffer(); 
		
    	int start=(pageNow-1)*pageSize;
    	
		//sql语句       
	 	String sql = "select id from cc_car_assort where 1=1 "+buf.toString()+" order by createtime desc "+" limit " +(start)+" , "+(pageSize);  

  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")
  		.create();
  		
  		Query<CarAssort> eQ = Ebean.find(CarAssort.class);
  		eQ.setRawSql(rawSql);
  		
  		List<CarAssort> list = eQ.findList();

		return list;
	}
	
	public static int assortCount(Map in){
		
		StringBuffer buf = new StringBuffer(); 
		
     	String sql = "select count(id) as count from cc_car_assort where 1=1 "+buf.toString();
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }
	
	
	
	@BodyParser.Of(BodyParser.Json.class)
	public static ObjectNode carList(Map<String, String> data) throws JsonProcessingException {
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
		
		List<Car> list =carList(data,pageSize,pageNow);
		//总页数
		int countAll = carCount(data);
		
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
		
		for (Car car : list) {
			ObjectNode node=Json.newObject();
			node.put("id", car.getId());
			node.put("carname", car.getCarname());
			node.put("imagespath", Play.application().configuration().getString("outpath"));
			String images="";
			if(car.getImages().size()>0){
				for (int i = 0; i < car.getImages().size(); i++) {
					CarImage image=car.getImages().get(i);
					images+=image.getUrl()+",";
				}
				images=images.substring(0, images.length()-1);
			}
			node.put("imagesurl", images);
			node.put("uuid", car.getUuid());
			arrayNode.add(node);
		}
		
		resultJson.put("code", "0");
		resultJson.put("list", arrayNode);
		resultJson.put("countall", countAll);
      	resultJson.put("pagenow", pageNow);
      	resultJson.put("pagecount", pageCount);
      	resultJson.put("pagesize", pageSize);
      	resultJson.put("msg", "查询数据成功！");
		return resultJson;
	}
	
	
	public static List<Car> carList(Map<String, String> data,int pageSize,int pageNow) {
		// TODO Auto-generated method stub
		ObjectNode resultJson = Json.newObject();
		JSONObject jsonObject = null;		

		String assortid=data.get("assortid");
		
		StringBuffer buf = new StringBuffer(); 
		
		buf.append(" and stand='1' and assortcode="+assortid);
		
    	int start=(pageNow-1)*pageSize;
    	
		//sql语句       
	 	String sql = "select id from cc_car_product where 1=1 "+buf.toString()+" order by createtime desc "+" limit " +(start)+" , "+(pageSize);  

  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")
  		.create();
  		
  		Query<Car> eQ = Ebean.find(Car.class);
  		eQ.setRawSql(rawSql);
  		
  		List<Car> list = eQ.findList();

		return list;
	}
	
	public static int carCount(Map<String, String> data){
		
		StringBuffer buf = new StringBuffer(); 
		
		String assortid=data.get("assortid");
		
		buf.append(" and stand='1' and assortcode="+assortid);
		
     	String sql = "select count(id) as count from cc_car_product where 1=1 "+buf.toString();
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }
	
	@BodyParser.Of(BodyParser.Json.class)
	public static ObjectNode car(Map<String, String> data) throws JsonProcessingException {
		// TODO Auto-generated method stub
		ObjectNode resultJson = Json.newObject();
		JSONObject jsonObject = null;		

		String uuid=data.get("uuid");
		
		Car car= Car.findByUuid(uuid);
		
		if(car==null){
			resultJson.put("code", "5");
			resultJson.put("msg", "没有数据！");
			return resultJson;
		}
		
		
		resultJson.put("code", "0");
		resultJson.put("carname", car.getCarname());
		resultJson.put("uuid", car.getUuid());
      	resultJson.put("assortid", car.getAssortcode());
      	resultJson.put("assortname", car.getAssortname());
      	String images="";
		if(car.getImages().size()>0){
			for (int i = 0; i < car.getImages().size(); i++) {
				CarImage image=car.getImages().get(i);
				images+=image.getUrl()+",";
			}
			images=images.substring(0, images.length()-1);
		}
		resultJson.put("imagesurl", images);
		resultJson.put("imagespath", Play.application().configuration().getString("outpath"));      	
      	resultJson.put("msg", "查询数据成功！");
		return resultJson;
	}
	
	@BodyParser.Of(BodyParser.Json.class)
	public static ObjectNode queryCarBrand(Map<String, String> data) throws JsonProcessingException {
		// TODO Auto-generated method stub
		ObjectNode resultJson = Json.newObject();
		JSONObject jsonObject = null;		

		ObjectMapper listMapper = new ObjectMapper(); 
		List<AppCarAssort> assorts= AppCarAssort.findAllList();
		
		ArrayNode arrayNode=listMapper.createArrayNode();
		
		List<String> titles=carTitle();
		for (String string : titles) {
			ObjectNode node=Json.newObject();
			node.put("title", string);
			ArrayNode brandList=listMapper.createArrayNode();
			for (int i = 0; i < assorts.size(); i++) {
				if(assorts.get(i).getTitle().equals(string)){
					ObjectNode child=Json.newObject();
					child.put("name", assorts.get(i).getAssortname());
					child.put("logo", Play.application().configuration().getString("ippath")+Play.application().configuration().getString("outpath")+"/"+assorts.get(i).getLogo());
					child.put("code", assorts.get(i).getAssortcode());
					brandList.add(child);
				}
			}
			node.put("brand", brandList);
			arrayNode.add(node);
		}
		
		if(assorts.size()==0){
			resultJson.put("code", "5");
			resultJson.put("msg", "没有数据！");
			return resultJson;
		}

		resultJson.put("code", "0");
		resultJson.put("list", arrayNode);
      	resultJson.put("msg", "查询数据成功！");
		return resultJson;
	}
	
	public static List<String> carTitle() {
		// TODO Auto-generated method stub
		
		List<String> list=new ArrayList<String>();
		ObjectNode resultJson = Json.newObject();
		JSONObject jsonObject = null;		
		
		StringBuffer buf = new StringBuffer(); 
		
		    	
		//sql语句       
	 	String sql = "select title as title from cc_app_car_assort where 1=1 and stand='1' "+buf.toString()+" group by title ";  

	 	List<SqlRow> sqlRows= Ebean.createSqlQuery(sql).findList();
  		
	 	for (SqlRow sqlRow : sqlRows) {
	 		list.add(sqlRow.getString("title"));
		}
  		
		return list;
	}
	
	@BodyParser.Of(BodyParser.Json.class)
	public static ObjectNode queryCars(Map<String, String> data) throws JsonProcessingException {
		// TODO Auto-generated method stub
		ObjectNode resultJson = Json.newObject();
		JSONObject jsonObject = null;		

		String code=data.get("code");
		
		ObjectMapper listMapper = new ObjectMapper(); 
		List<AppCar> cars= AppCar.findStandCars(code);
		
		if(cars.size()==0){
			resultJson.put("code", "5");
			resultJson.put("msg", "没有数据！");
			return resultJson;
		}
		
		ArrayNode arrayNode=listMapper.createArrayNode();
		for (AppCar car : cars) {
			ObjectNode node=Json.newObject();
			node.put("name", car.getCarname());
			node.put("price", car.getPricerange());
			node.put("uuid", car.getUuid());
			node.put("desc", car.getRemark());
			if(car.getImages().size()>0){
				node.put("image", Play.application().configuration().getString("ippath")+Play.application().configuration().getString("outpath")+"/"+car.getImages().get(0).getUrl());
			}
			arrayNode.add(node);
		}
		
		resultJson.put("code", "0");
		resultJson.put("list", arrayNode);
      	resultJson.put("msg", "查询数据成功！");
		return resultJson;
	}
	
	
	@BodyParser.Of(BodyParser.Json.class)
	public static ObjectNode queryHotCar(Map<String, String> data) throws JsonProcessingException {
		// TODO Auto-generated method stub
		ObjectNode resultJson = Json.newObject();
		JSONObject jsonObject = null;				
		ObjectMapper listMapper = new ObjectMapper(); 
		List<AppHotCar> cars= appHotCarList(data);
		
		if(cars==null||cars.size()==0){
			resultJson.put("code", "5");
			resultJson.put("msg", "没有数据！");
			return resultJson;
		}
		
		ArrayNode arrayNode=listMapper.createArrayNode();
		for (AppHotCar car : cars) {
			ObjectNode node=Json.newObject();
			node.put("uuid", car.getUuid());
			node.put("name", car.getAppCarStyle().getAppCar().getCarname()+" "+car.getAppCarStyle().getStylename());
			node.put("desc", car.getThedesc());
			node.put("price", car.getAppCarStyle().getPrice());
			node.put("firstprice", car.getFirstprice());
			node.put("discount", car.getDiscount());
			node.put("discountdesc", car.getDisdesc());
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
	
	public static List<AppHotCar> appHotCarList(Map<String, String> data) {
		// TODO Auto-generated method stub
		ObjectNode resultJson = Json.newObject();
		JSONObject jsonObject = null;		

		
		StringBuffer buf = new StringBuffer(); 
		
		buf.append(" and car.stand='1' ");
		    	
		//sql语句       
	 	String sql = "select hot.id id from cc_app_car car,cc_app_hot_car hot,cc_app_car_style st where 1=1 and car.id=st.carid and st.hotcarid=hot.id "+buf.toString()+" order by hot.createtime ";  

  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")
  		.create();
  		
  		Query<AppHotCar> eQ = Ebean.find(AppHotCar.class);
  		eQ.setRawSql(rawSql);
  		
  		List<AppHotCar> list = eQ.findList();

		return list;
	}
	
	
	
	@BodyParser.Of(BodyParser.Json.class)
	public static ObjectNode queryCarStyle(Map<String, String> data) throws JsonProcessingException {
		// TODO Auto-generated method stub
		ObjectNode resultJson = Json.newObject();
		JSONObject jsonObject = null;		

		String uuid=data.get("uuid");
		
		ObjectMapper listMapper = new ObjectMapper(); 
		AppCar appCar= AppCar.findByUuid(uuid);
		List<AppCarStyle> carStyles=appCar.getCarStyles();
		
		if(carStyles==null||carStyles.size()==0){
			resultJson.put("code", "5");
			resultJson.put("msg", "没有数据！");
			return resultJson;
		}
		
		ArrayNode arrayNode=listMapper.createArrayNode();
		for (AppCarStyle car : carStyles) {
			ObjectNode node=Json.newObject();
			node.put("uuid", car.getUuid());
			node.put("name", car.getStylename());
			node.put("desc", car.getRemark());
			node.put("price", car.getPrice());
			if(appCar.getImages().size()>0){
				node.put("image", Play.application().configuration().getString("ippath")+Play.application().configuration().getString("outpath")+"/"+car.getAppCar().getImages().get(0).getUrl());
			}
			arrayNode.add(node);
		}
		
		resultJson.put("code", "0");
		resultJson.put("list", arrayNode);
      	resultJson.put("msg", "查询数据成功！");
		return resultJson;
	}
	
	
	@BodyParser.Of(BodyParser.Json.class)
	public static ObjectNode queryRentCar(Map<String, String> data) throws JsonProcessingException {
		// TODO Auto-generated method stub
		ObjectNode resultJson = Json.newObject();
		JSONObject jsonObject = null;				
		ObjectMapper listMapper = new ObjectMapper(); 
		List<AppRentCar> cars= appRentCarList(data);
		
		if(cars==null||cars.size()==0){
			resultJson.put("code", "5");
			resultJson.put("msg", "没有数据！");
			return resultJson;
		}
		
		ArrayNode arrayNode=listMapper.createArrayNode();
		for (AppRentCar car : cars) {
			ObjectNode node=Json.newObject();
			node.put("uuid", car.getUuid());
			node.put("name", car.getAppCarStyle().getAppCar().getCarname()+" "+car.getAppCarStyle().getStylename());
			node.put("desc", car.getThedesc());
			node.put("price", car.getAppCarStyle().getPrice());
			node.put("firstprice", car.getFirstprice());
			node.put("discount", car.getDiscount());
			node.put("discountdesc", car.getDisdesc());
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
	
	public static List<AppRentCar> appRentCarList(Map<String, String> data) {
		// TODO Auto-generated method stub
		ObjectNode resultJson = Json.newObject();
		JSONObject jsonObject = null;		

		
		StringBuffer buf = new StringBuffer(); 
		
		buf.append(" and car.stand='1' ");
		    	
		//sql语句       
	 	String sql = "select hot.id id from cc_app_car car,cc_app_rent_car hot,cc_app_car_style st where 1=1 and car.id=st.carid and st.rentid=hot.id "+buf.toString()+" order by hot.createtime ";  

  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")
  		.create();
  		
  		Query<AppRentCar> eQ = Ebean.find(AppRentCar.class);
  		eQ.setRawSql(rawSql);
  		
  		List<AppRentCar> list = eQ.findList();

		return list;
	}
	
	
	
	@BodyParser.Of(BodyParser.Json.class)
	public static ObjectNode queryYueanCar(Map<String, String> data) throws JsonProcessingException {
		// TODO Auto-generated method stub
		ObjectNode resultJson = Json.newObject();
		JSONObject jsonObject = null;				
		ObjectMapper listMapper = new ObjectMapper(); 
		List<AppYueanCar> cars= appYueanCarList(data);
		
		if(cars==null||cars.size()==0){
			resultJson.put("code", "5");
			resultJson.put("msg", "没有数据！");
			return resultJson;
		}
		
		ArrayNode arrayNode=listMapper.createArrayNode();
		for (AppYueanCar car : cars) {
			ObjectNode node=Json.newObject();
			node.put("uuid", car.getUuid());
			node.put("name", car.getAppCarStyle().getAppCar().getCarname()+" "+car.getAppCarStyle().getStylename());
			node.put("desc", car.getThedesc());
			node.put("price", car.getAppCarStyle().getPrice());
			node.put("firstprice", car.getFirstprice());
			node.put("discount", car.getDiscount());
			node.put("discountdesc", car.getDisdesc());
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
	
	public static List<AppYueanCar> appYueanCarList(Map<String, String> data) {
		// TODO Auto-generated method stub
		ObjectNode resultJson = Json.newObject();
		JSONObject jsonObject = null;		

		
		StringBuffer buf = new StringBuffer(); 
		
		buf.append(" and car.stand='1' ");
		    	
		//sql语句       
	 	String sql = "select hot.id id from cc_app_car car,cc_app_yuean_car hot,cc_app_car_style st where 1=1 and car.id=st.carid and st.yueanid=hot.id "+buf.toString()+" order by hot.createtime ";  

  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")
  		.create();
  		
  		Query<AppYueanCar> eQ = Ebean.find(AppYueanCar.class);
  		eQ.setRawSql(rawSql);
  		
  		List<AppYueanCar> list = eQ.findList();

		return list;
	}
	
	@BodyParser.Of(BodyParser.Json.class)
	public static ObjectNode appRentCarDetail(Map<String, String> data) throws JsonProcessingException {
		// TODO Auto-generated method stub
		ObjectNode resultJson = Json.newObject();

		ObjectMapper listMapper = new ObjectMapper(); 
				
		String uuid=(String)data.get("uuid");
				
		AppRentCar car= AppRentCar.findByUuid(uuid);
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
		nodeJson.put("spec", car.getDisdesc());
		nodeJson.put("discount", car.getDiscount());
		resultJson.put("code", "0");
		nodeJson.put("list", arrayNode);
		resultJson.put("data", nodeJson);
      	resultJson.put("msg", "查询数据成功！");
		return resultJson;
	}
	
	@BodyParser.Of(BodyParser.Json.class)
	public static ObjectNode appYueanCarDetail(Map<String, String> data) throws JsonProcessingException {
		// TODO Auto-generated method stub
		ObjectNode resultJson = Json.newObject();

		ObjectMapper listMapper = new ObjectMapper(); 
				
		String uuid=(String)data.get("uuid");
				
		AppYueanCar car= AppYueanCar.findByUuid(uuid);
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
		nodeJson.put("spec", car.getDisdesc());
		nodeJson.put("discount", car.getDiscount());
		resultJson.put("code", "0");
		nodeJson.put("list", arrayNode);
		resultJson.put("data", nodeJson);
      	resultJson.put("msg", "查询数据成功！");
		return resultJson;
	}
}
