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

public class AppAdvertApplication extends Controller {

	@BodyParser.Of(BodyParser.Json.class)
	public static ObjectNode advertList(Map<String, String> data) throws JsonProcessingException {
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
		
		List<Advert> list =advertList(data,pageSize,pageNow);
		
		
		//总页数
		int countAll = advertCount(data);
		
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
		
		for (Advert advert : list) {
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
	
	
	public static List<Advert> advertList(Map<String, String> data,int pageSize,int pageNow) {
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
    }
		
}
