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

import play.api.libs.ws.Response;
import play.api.libs.ws.WS;
import play.data.DynamicForm;
import com.avaje.ebean.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import models.*;

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


public class GuestServiceApplication extends Controller {
	
	public static Result guestsubscribe() {
		
		ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    
      		
		return ok(guestsubscribe.render(""));
		
    }

	public static Result guestapply() {
		
		ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    
    	
      		
		return ok(guestapply.render(""));
		
    }
	
	public static Result guestsubscribeAdd() {
		
		ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    
    	String name=in.get("name");
    	String phone=in.get("phone");
    	String sex=in.get("sex");
    	String asset=in.get("asset");
    	String driveport=in.get("driveport");
    	String intentprice=in.get("intentprice");
    	String dominatedate=in.get("dominatedate");
    	String recommend=in.get("recommend");
    	
    	Subscribe subscribe=new Subscribe();
    	subscribe.setAsset(asset);
    	subscribe.setDirveport(driveport);
    	subscribe.setDominatedate(dominatedate);
    	subscribe.setIntentprice(intentprice);
    	subscribe.setName(name);
    	subscribe.setPhone(phone);
    	subscribe.setRecommend(recommend);
    	subscribe.setSex(sex);
    	subscribe.setCreatetime(new Date());
    	subscribe.save();
		return ok(guestsubscribe.render("success"));
		
    }

	public static Result guestapplyAdd() {
	
	ObjectNode resultJson = Json.newObject();
	
	//输入参数
	DynamicForm in = Form.form().bindFromRequest();

	String name=in.get("name");
	String phone=in.get("phone");
	String brand=in.get("brand");
	String province=in.get("province");
	String city=in.get("city");
	String district=in.get("district");
	String street=in.get("street");
	String bill=in.get("bill");
	String securedate=in.get("securedate");
	String recommend=in.get("recommend");
		
	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	
	String address=province+"-"+city+"-"+district+"-"+street;
	
	Apply apply=new Apply();
	
	apply.setAddress(address);
	apply.setBill(bill);
	apply.setBrand(brand);
	apply.setCreatetime(new Date());
	apply.setName(name);
	apply.setPhone(phone);
	apply.setRecommend(recommend);
	try{
		apply.setSecuredate(df.parse(securedate));
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	apply.save();
	return ok(guestapply.render("success"));
	
}
	
	public static Result guestsubscribeList() {
		
		ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    
    	List<InputBtn> btn=subscribeCreateText(false);
    	//分页
    	//每页显示数据
		int pageSize = Integer.parseInt(Play.application().configuration().getString("pageSize"));
		
		//当前第N页
		int pageNow = 1;
		
		String pageNowIn = (String)in.get("pageNow");
		if(pageNowIn!=null){
		
			pageNow = Integer.parseInt(pageNowIn);
		}
		
    	List<Subscribe> list = GuestServiceApplication.subscribeList(pageSize,pageNow,btn);
    	
    	//总数
		int countAll = GuestServiceApplication.subscribeCount(btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
    	
		return ok(subscribe.render(list,countAll,pageNow,pageCount,pageSize,"",btn));
		
    }
	
	
	public static List<InputBtn> subscribeCreateText(boolean my){
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	Map<String, String> data=in.data();
    	List<InputBtn> l=new ArrayList<InputBtn>();
    	if(!my){
    		InputBtn bt1=new InputBtn("params","params","","姓名","");
    		

    		InputBtn bt2=new InputBtn("params0","params0","","联系电话","");
    		
    		InputBtn bt3=new InputBtn("params1","params1","","创建日期","",InputBtn.CTYPE_DATE);
    		
    		InputBtn bt4=new InputBtn("params2","params2","","~~","",InputBtn.CTYPE_DATE);
        	if(data.containsKey(bt1.getId())){
        		bt1.setValue(data.get(bt1.getId()));
        	}
        	if(data.containsKey(bt2.getId())){
        		bt2.setValue(data.get(bt2.getId()));
        	}
        	if(data.containsKey(bt3.getId())){
        		bt3.setValue(data.get(bt3.getId()));
        	}
        	if(data.containsKey(bt4.getId())){
        		bt4.setValue(data.get(bt4.getId()));
        	}
    		l.add(bt1);
    		l.add(bt2);
    		l.add(bt3);
    		l.add(bt4);
    	}
		
		return l;
		
	}
	
    public static List<Subscribe> subscribeList(int pageSize,int pageNow,List<InputBtn>... btn){
		
		int start=(pageNow-1)*pageSize;
		
      	String paramsSql = "";
		
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (name like '%"+params+"%')";
      	}
      	
      	String params0="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(1).getValue()!=null&&!btn[0].get(1).getValue().equals("")){
      		params0=btn[0].get(1).getValue().trim();
      		paramsSql+=" and (phone like '%"+params0+"%')";
      	}
      	
      	String params1="";
      	String params2="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(3).getValue()!=null&&!btn[0].get(3).getValue().equals("")
      			&&btn[0].get(2).getValue()!=null&&!btn[0].get(2).getValue().equals("")){
      		params1=btn[0].get(2).getValue().trim();
      		params2=btn[0].get(3).getValue().trim();
      		paramsSql+=" and (date_format(createtime,'%Y-%m-%d') between '"+params1+"' and '"+params2+"')";
      	}
      	
		//sql语句       
	 	String sql = "select id from cc_subscribe where 1=1 "+paramsSql+" order by createtime desc limit "+start+" ,"+pageSize;   
	
	 	System.out.println(sql);
	 	
  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")   
  		.create();
  		
  		Query<Subscribe> eQ = Ebean.find(Subscribe.class);
  		eQ.setRawSql(rawSql);
  		
  		List<Subscribe> list = eQ.findList();

		return list;
    }
    
    public static int subscribeCount(List<InputBtn>... btn){
		
      	String paramsSql = "";

      	
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (name like '%"+params+"%')";
      	}
      	
      	String params0="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(1).getValue()!=null&&!btn[0].get(1).getValue().equals("")){
      		params0=btn[0].get(1).getValue().trim();
      		paramsSql+=" and (phone like '%"+params0+"%')";
      	}
      	
      	String params1="";
      	String params2="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(3).getValue()!=null&&!btn[0].get(3).getValue().equals("")
      			&&btn[0].get(2).getValue()!=null&&!btn[0].get(2).getValue().equals("")){
      		params1=btn[0].get(2).getValue().trim();
      		params2=btn[0].get(3).getValue().trim();
      		paramsSql+=" and (date_format(createtime,'%Y-%m-%d') between '"+params1+"' and '"+params2+"')";
      	}
      	
		//sql语句       
	 	String sql = "select count(id) as count from cc_subscribe where 1=1 "+paramsSql;  
     	
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }
    
    
    public static Result guestapplyList() {
		
		ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    
    	List<InputBtn> btn=applyCreateText(false);
    	//分页
    	//每页显示数据
		int pageSize = Integer.parseInt(Play.application().configuration().getString("pageSize"));
		
		//当前第N页
		int pageNow = 1;
		
		String pageNowIn = (String)in.get("pageNow");
		if(pageNowIn!=null){
		
			pageNow = Integer.parseInt(pageNowIn);
		}
		
    	List<Apply> list = GuestServiceApplication.applyList(pageSize,pageNow,btn);
    	
    	//总数
		int countAll = GuestServiceApplication.applyCount(btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
    	
		return ok(apply.render(list,countAll,pageNow,pageCount,pageSize,"",btn));
		
    }
	
	
	public static List<InputBtn> applyCreateText(boolean my){
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	Map<String, String> data=in.data();
    	List<InputBtn> l=new ArrayList<InputBtn>();
    	if(!my){
    		InputBtn bt1=new InputBtn("params","params","","姓名","");
    		

    		InputBtn bt2=new InputBtn("params0","params0","","联系电话","");
    		
    		InputBtn bt3=new InputBtn("params1","params1","","创建日期","",InputBtn.CTYPE_DATE);
    		
    		InputBtn bt4=new InputBtn("params2","params2","","~~","",InputBtn.CTYPE_DATE);
        	if(data.containsKey(bt1.getId())){
        		bt1.setValue(data.get(bt1.getId()));
        	}
        	if(data.containsKey(bt2.getId())){
        		bt2.setValue(data.get(bt2.getId()));
        	}
        	if(data.containsKey(bt3.getId())){
        		bt3.setValue(data.get(bt3.getId()));
        	}
        	if(data.containsKey(bt4.getId())){
        		bt4.setValue(data.get(bt4.getId()));
        	}
    		l.add(bt1);
    		l.add(bt2);
    		l.add(bt3);
    		l.add(bt4);
    	}
		
		return l;
		
	}
	
    public static List<Apply> applyList(int pageSize,int pageNow,List<InputBtn>... btn){
		
		int start=(pageNow-1)*pageSize;
		
      	String paramsSql = "";
		
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (name like '%"+params+"%')";
      	}
      	
      	String params0="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(1).getValue()!=null&&!btn[0].get(1).getValue().equals("")){
      		params0=btn[0].get(1).getValue().trim();
      		paramsSql+=" and (phone like '%"+params0+"%')";
      	}
      	
      	String params1="";
      	String params2="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(3).getValue()!=null&&!btn[0].get(3).getValue().equals("")
      			&&btn[0].get(2).getValue()!=null&&!btn[0].get(2).getValue().equals("")){
      		params1=btn[0].get(2).getValue().trim();
      		params2=btn[0].get(3).getValue().trim();
      		paramsSql+=" and (date_format(createtime,'%Y-%m-%d') between '"+params1+"' and '"+params2+"')";
      	}
      	
		//sql语句       
	 	String sql = "select id from cc_apply where 1=1 "+paramsSql+" order by createtime desc limit "+start+" ,"+pageSize;   
	
	 	System.out.println(sql);
	 	
  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")   
  		.create();
  		
  		Query<Apply> eQ = Ebean.find(Apply.class);
  		eQ.setRawSql(rawSql);
  		
  		List<Apply> list = eQ.findList();

		return list;
    }
    
    public static int applyCount(List<InputBtn>... btn){
		
      	String paramsSql = "";

      	
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (name like '%"+params+"%')";
      	}
      	
      	String params0="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(1).getValue()!=null&&!btn[0].get(1).getValue().equals("")){
      		params0=btn[0].get(1).getValue().trim();
      		paramsSql+=" and (phone like '%"+params0+"%')";
      	}
      	
      	String params1="";
      	String params2="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(3).getValue()!=null&&!btn[0].get(3).getValue().equals("")
      			&&btn[0].get(2).getValue()!=null&&!btn[0].get(2).getValue().equals("")){
      		params1=btn[0].get(2).getValue().trim();
      		params2=btn[0].get(3).getValue().trim();
      		paramsSql+=" and (date_format(createtime,'%Y-%m-%d') between '"+params1+"' and '"+params2+"')";
      	}
      	
		//sql语句       
	 	String sql = "select count(id) as count from cc_apply where 1=1 "+paramsSql;  
     	
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }
}
