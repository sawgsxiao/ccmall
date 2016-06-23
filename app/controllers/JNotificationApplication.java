package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;

import views.html.*;
import play.data.Form;
import play.data.DynamicForm;
import play.data.validation.Constraints.*;

import play.libs.Json;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.Audience.Builder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;  
import com.fasterxml.jackson.databind.JsonMappingException;  
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.core.JsonParseException;  

import play.api.libs.ws.Response;
import play.api.libs.ws.WS;
import play.data.DynamicForm;
import com.avaje.ebean.*;

import java.util.ArrayList;
import java.util.List;
import models.*;
import models.Notification;
import net.sf.json.JSONObject;

import java.text.SimpleDateFormat;
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
import common.*;

@Security.Authenticated(Secured.class)
public class JNotificationApplication extends Controller {
    
    public static Result jNotification() throws JsonParseException, JsonMappingException, IOException {
        
        ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	//分页
    	//每页显示数据
		int pageSize = Integer.parseInt(Play.application().configuration().getString("pageSize"));
		
		//当前第N页
		int pageNow = 1;
		
		String pageNowIn = (String)in.get("pageNow");
		if(pageNowIn!=null){
		
			pageNow = Integer.parseInt(pageNowIn);
		}
		
    	List<Notification> list = JNotificationApplication.list(pageSize,pageNow);
    	
    	//总数
		int countAll = JNotificationApplication.count();
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      	
      	String path = Play.application().configuration().getString("imageurl");
      	
      	return ok(notification.render(list,countAll,pageNow,pageCount,pageSize,path));
    }
    
    public static List<Notification> list(int pageSize,int pageNow){
		
		int start=(pageNow-1)*pageSize;
		
      	String paramsSql = "  ";
		
		
		//sql语句       
	 	String sql = "select id from jc_jnotification where 1=1 "+paramsSql+" order by id desc limit "+start+" ,"+pageSize;   
	
  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")   
  		.create();
  		
  		Query<Notification> eQ = Ebean.find(Notification.class);
  		eQ.setRawSql(rawSql);
  		
  		List<Notification> list = eQ.findList();

		return list;
    }
    
    public static int count(){
		
      	String paramsSql = "  ";
		
		

		//sql语句       
	 	String sql = "select count(id) as count from jc_jnotification where 1=1 "+paramsSql;  
     	
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }

    
    public static Result jNotificationAdd() {

        return ok(notificationAdd.render(""));
    }
    
    public static Result jNotificationAddDo() {
    	
    	//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	String result=null;
    	String title = in.get("title");
    	String content = in.get("content");
    	String aps = in.get("apsios");
    	String aps0 = in.get("apsandroid");
    	String receive = in.get("receive");
    	String sign = in.get("sign");
    	Audience audience=null;
    	Platform platform=null; 
    	if(aps!=null&&!aps.equals("")&&aps0!=null&&!aps0.equals("")){
    		aps=aps+"_"+aps0;
    		platform=Platform.android_ios();
    		
    	}else{
    		if(aps!=null&&!aps.equals("")){
    			platform=Platform.ios();
    		}else if(aps0!=null&&!aps0.equals("")){
    			aps=aps0;
    			platform=Platform.android();
    		}
    	}
    	
    	if(receive.equals("all")){
    		audience=Audience.all();
    	}else{
    		audience=Audience.tag(sign);
    	}
       Notification notification=new Notification();
		
		Date date=new Date();
		
		try {
			notification.setTitle(title);
			notification.setContent(content);
			notification.setCreatetime(date);
			notification.setAps(aps);
			notification.setReceive(receive);
			notification.setSign(sign);
			notification.setUserid(Integer.parseInt(session("userid")));
			notification.save();
			
			JNotification o=new JNotification(content);
			String responseContent=null;
			if(aps.equals("ios")){
				responseContent=JpushServiceV3.pushPayloadIos(audience, platform, o);
			}else{
				responseContent=JpushServiceV3.pushPayload(audience, platform, o);
			}
			
			
			System.out.println("send:"+responseContent);
			
			JSONObject jsonObject= JSONObject.fromObject(responseContent);
			
			notification.setSendno(jsonObject.get("sendno").toString());
			
			notification.setMsgid(jsonObject.get("msg_id").toString());
			
			notification.update();
			
			result=jsonObject.toString();
		}
		catch(Exception e){
		}
    	
		return ok(notificationAdd.render(result));
    }
    
    public static Result jNotificationDetail(String msgid) {
    	
    	JpushServiceV3.jReportByMsgid(msgid);
    	
        return redirect("/JNotification");
    }
    
    public static Result jNotificationDel(int id) {
    	
    	Notification.findById(id).delete();
    	
        return redirect("/JNotification");
    }
}
