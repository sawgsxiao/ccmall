package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;

import views.html.*;
import play.data.Form;
import play.data.DynamicForm;
import play.data.validation.Constraints.*;

import play.mvc.Http.Request;
import play.mvc.Http.Context;
import play.mvc.Http.Session;

import play.libs.Json;
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

import java.util.Date;
import java.text.SimpleDateFormat;
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

@Security.Authenticated(Secured.class)
public class SysLogApplication extends Controller {
	
    public static void saveLog(String content,String des) {
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
    	
    	Date date = new Date();
    	
    	String s=sdf.format(date)+","+session("name")+"("+session("username")+")"+","+des+"。";
    	
    	SysLog syslog = new SysLog();
    	
        syslog.setUsername(session("username"));
        syslog.setName(session("name"));
        syslog.setContent(content);
        syslog.setDes(s);
        syslog.setCreatetime(date);
        		
        try{
			syslog.save();
		}catch(Exception e){
			Logger.info("保存日志失败:e:"+e);
		}  
    }
    
    public static Result sysLog() {
    
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();

    	
    	List<InputBtn> btn=createText(false);
    	//分页
    	//每页显示数据
		int pageSize = Integer.parseInt(Play.application().configuration().getString("pageSize"));
		
		//当前第N页
		int pageNow = 1;
		
		String pageNowIn = (String)in.get("pageNow");
		if(pageNowIn!=null){
		
			pageNow = Integer.parseInt(pageNowIn);
		}
		
		String s = session("username");
		
    	List<SysLog> logList = SysLogApplication.findAllList(pageSize,pageNow,btn);
    	
    	//总数
		int countAll = SysLogApplication.count(btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      	
		return ok(syslog.render(logList,countAll,pageNow,pageCount,pageSize,btn));
    }
    
    
    public static List<InputBtn> createText(boolean my){
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	Map<String, String> data=in.data();
    	List<InputBtn> l=new ArrayList<InputBtn>();
    	if(!my){
    		InputBtn bt1=new InputBtn("params","params","","账号/人员名称","");
    		InputBtn bt2=new InputBtn("param0","param0","","操作内容","");
        	if(data.containsKey(bt1.getId())){
        		bt1.setValue(data.get(bt1.getId()));
        	}
        	if(data.containsKey(bt2.getId())){
        		bt2.setValue(data.get(bt2.getId()));
        	}
        	
    		l.add(bt1);
    		l.add(bt2);
    		
    	}
		
		return l;
		
	}
    /**
	 *	查询列表-分页
	 **/
    public static List<SysLog> findAllList(int pageSize,int pageNow,List<InputBtn>... btn){
		
		int start=(pageNow-1)*pageSize;
		
		String paramsSql = "";
		
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (username like '%"+params+"%' or name like '%"+params+"%' )";
      	}
      	
      	String params0="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(1).getValue()!=null&&!btn[0].get(1).getValue().equals("")){
      		params0=btn[0].get(1).getValue().trim();
      		paramsSql+=" and (content like '%"+params0+"%')";
      	}
		//sql语句       
	 	String sql = "select id from sys_log order by createtime desc limit "+start+" ,"+pageSize; 

  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")   
  		.create();
  		
  		Query<SysLog> eQ = Ebean.find(SysLog.class);
  		eQ.setRawSql(rawSql);
  		
  		List<SysLog> list = eQ.findList();

		return list;
    }
    
    /**
     *  查询列表总数量
     **/
    public static int count(List<InputBtn>... btn){
		
		//sql语句  
    	String paramsSql = "";
		
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (username like '%"+params+"%' or name like '%"+params+"%' )";
      	}
      	
      	String params0="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(1).getValue()!=null&&!btn[0].get(1).getValue().equals("")){
      		params0=btn[0].get(1).getValue().trim();
      		paramsSql+=" and (content like '%"+params0+"%')";
      	}
	 	String sql = "select count(*) as count from sys_log "; 
     	
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }
}
