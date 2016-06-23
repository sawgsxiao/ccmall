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

@Security.Authenticated(Secured.class)
public class DepartmentApplication extends Controller {
	
	public static Result departmentList() {
		
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
		
    	List<Department> list = DepartmentApplication.list(pageSize,pageNow);
    	
    	//总数
		int countAll = DepartmentApplication.count();
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      		
		return ok(department.render(list,countAll,pageNow,pageCount,pageSize));
		
    }

    public static List<Department> list(int pageSize,int pageNow){
		
		int start=(pageNow-1)*pageSize;
		
      	String paramsSql = "";
		
		//sql语句       
	 	String sql = "select departid from jc_department where 1=1 "+paramsSql+" order by departid desc limit "+start+" ,"+pageSize;   
	
  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("departid",  "departid")   
  		.create();
  		
  		Query<Department> eQ = Ebean.find(Department.class);
  		eQ.setRawSql(rawSql);
  		
  		List<Department> list = eQ.findList();

		return list;
    }
    
    public static int count(){
		
      	String paramsSql = "";

		//sql语句       
	 	String sql = "select count(departid) as count from jc_department where 1=1 "+paramsSql;  
     	
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }
    
    public static Result departmentAdd() {
    	
    	return ok(departmentadd.render());
    }
    
    public static Result departmentAddDo() {
    	
    	//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
  	
    	String name = (String)in.get("name");
    	
    	Department department = new Department();
    	try{
    		department.setName(name);
			department.save();
		}catch(Exception e){
			Logger.info("新建失败:e:"+e);
		}  
		
    	Logger.info("新建成功。");
    	return redirect("/departmentList");
    }
    
    public static Result departmentDel(int departid){

 		Department.del(departid);

		return redirect("/departmentList");
    }
    
    public static Result departmentEdit(int departid){

 		Department department = Department.findById(departid);

    	return ok(departmentedit.render(department));
    }
    
    public static Result departmentEditDo() {
    	
    	//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
  		
  		String departid = (String)in.get("departid");
    	String name = (String)in.get("name");

    	Department department = Department.findById(Integer.parseInt(departid));
    	try{
    		department.setName(name);
			department.update();
		}catch(Exception e){
			Logger.info("编辑失败:e:"+e);
		}  
		
    	Logger.info("编辑成功。");
    	return redirect("/departmentList");
    }
    
    
    /*加载树后，激活指定的组*/
	public static Result departmentListPlus(int groupId) {
		
		ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
		//加载树形文件夹
		//String treeStr = getTreeData(0).toString();
		return ok(departmenttree.render(groupId,"departtree"));
		
    }
	
	private static ArrayNode getTreeData(int pid){
    	String sql = "select departid id, name,pardepartid pargroupid " +
    				   "from jc_department ";
		List<SqlRow> lists = Ebean.createSqlQuery(sql).findList();
		ArrayNode ja = JsonNodeFactory.instance.arrayNode();
		ArrayNode childja = JsonNodeFactory.instance.arrayNode();
		ObjectNode jo;
		jo = Json.newObject();
		jo.put("id", 0);
		jo.put("pid", "");
		jo.put("name", "根目录");
		ja.add(jo);
		if(lists.size()>0){
			for(int i = 0; i < lists.size(); i++){
				jo = Json.newObject();
				jo.put("id", lists.get(i).getString("id"));
				jo.put("pid", lists.get(i).getString("pargroupid"));
				jo.put("name", lists.get(i).getString("name"));
				ja.add(jo);
			}
			return ja;
		}
		return null;
		
    }
	
	public static Result departmentData(int pid) {
		return ok(getTreeData(pid).toString());
	}
	
	public static Result departmentGroupEdit(int id){
		DynamicForm in = Form.form().bindFromRequest();
		Department department = Department.findById(id);
		if(department == null){
			department = new Department();
			department.setName(in.get("name"));
			department.setPardepartid(Integer.parseInt(in.get("pid")));
			department.save();
			System.out.println("department.getDepartid():"+department.getDepartid());
			return ok("[{\"code\":1,\"msg\":\"处理成功\",\"newid\":" + department.getDepartid() + "}]");
		}
		int result = Ebean.createSqlUpdate("update jc_department set name = :name where departid=:gid").setParameter("name", in.get("name")).setParameter("gid", id).execute();
		if(result > 0){
			return ok("[{\"code\":1,\"msg\":\"处理成功\"}]");
		}else{
			return ok("[{\"code\":0,\"msg\":\"处理失败\"}]");
		}
	}
	public static Result departmentGroupDel(int id){
		
		if(departmentInUser(id)){
			return ok("[{\"code\":2,\"msg\":\"部门已被使用,不能删除!\"}]");
		}
		int result = Ebean.createSqlUpdate("delete from jc_department where departid=:gid").setParameter("gid", id).execute();
		if(result > 0){
			return ok("[{\"code\":1,\"msg\":\"处理成功\"}]");
		}else{
			return ok("[{\"code\":0,\"msg\":\"处理失败\"}]");
		}
	}
	
	public static boolean departmentInUser(int id){
		//sql语句       
	 	String sql = "select count(departid) as count from jc_user where 1=1  and departid="+id;  
     	
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
        if(i>0){
        	return true;
        }
        return false;
	}
	
	public static Result departmentCheckDel(int id){
		
		if(departmentInUser(id)){
			return ok("[{\"code\":2,\"msg\":\"部门已被使用,不能删除!\"}]");
		}
		return ok("[{\"code\":1,\"msg\":\"处理成功!\"}]");
	}
}
