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

import java.util.ArrayList;
import java.util.List;
import models.*;

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
public class RoleApplication extends Controller {
	
	public static Result roleList() {
		
		ObjectNode resultJson = Json.newObject();
		
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
		
    	List<Role> list = RoleApplication.list(pageSize,pageNow,btn);
    	
    	//总数
		int countAll = RoleApplication.count(btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      		
		return ok(role.render(list,countAll,pageNow,pageCount,pageSize,btn));
		
    }

	public static List<InputBtn> createText(boolean my){
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	Map<String, String> data=in.data();
    	List<InputBtn> l=new ArrayList<InputBtn>();
    	if(!my){
    		InputBtn bt1=new InputBtn("params","params","","角色名称","");

        	if(data.containsKey(bt1.getId())){
        		bt1.setValue(data.get(bt1.getId()));
        	}

    		l.add(bt1);
    		
    	}
		
		return l;
		
	}
	
    public static List<Role> list(int pageSize,int pageNow,List<InputBtn>... btn){
		
		int start=(pageNow-1)*pageSize;
		
      	String paramsSql = "";
		
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (name like '%"+params+"%' )";
      	}
      	
		//sql语句       
	 	String sql = "select roleid from jc_role where 1=1 "+paramsSql+" order by roleid desc limit "+start+" ,"+pageSize;   
	
  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("roleid",  "roleid")   
  		.create();
  		
  		Query<Role> eQ = Ebean.find(Role.class);
  		eQ.setRawSql(rawSql);
  		
  		List<Role> list = eQ.findList();

		return list;
    }
    
    public static int count(List<InputBtn>... btn){
		
      	String paramsSql = "";

      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (name like '%"+params+"%' )";
      	}
		//sql语句       
	 	String sql = "select count(roleid) as count from jc_role where 1=1 "+paramsSql;  
     	
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }
    
    public static Result roleAdd() {
    	
    	return ok(roleadd.render());
    }
    
    public static Result roleAddDo() {
    	
    	//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
  	
    	String name = (String)in.get("name");
    	
    	Role role = new Role();
    	try{
    		role.setName(name);
			role.save();
		}catch(Exception e){
			Logger.info("新建失败:e:"+e);
		}  
		
    	Logger.info("新建成功。");
    	return redirect("/roleList");
    }
    
    public static Result roleDel(int roleid){

 		Role.del(roleid);

		return redirect("/roleList");
    }
    
    public static Result roleEdit(int roleid){

 		Role role = Role.findById(roleid);

    	return ok(roleedit.render(role));
    }
    
    public static Result roleEditDo() {
    	
    	//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
  		
  		String roleid = (String)in.get("roleid");
    	String name = (String)in.get("name");

    	Role role = Role.findById(Integer.parseInt(roleid));
    	try{
    		role.setName(name);
			role.update();
		}catch(Exception e){
			Logger.info("编辑失败:e:"+e);
		}  
		
    	Logger.info("编辑成功。");
    	return redirect("/roleList");
    }
    
    public static Result roleCheckDel(int roleid){

    	String sql = "select count(roleid) as count from jc_user_role where 1=1 ";  
     	
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
        
        if(i>0){
        	return ok("[{\"code\":1,\"msg\":\"角色已被使用，不能删除!\"}]");
        }
        
		return ok("[{\"code\":0,\"msg\":\"角色删除!\"}]");
    }
    
}
