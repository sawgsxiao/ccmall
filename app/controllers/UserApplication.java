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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;  
import com.fasterxml.jackson.databind.JsonMappingException;  
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.core.JsonParseException;  

import play.api.libs.ws.Response;
import play.api.libs.ws.WS;
import play.data.DynamicForm;
import com.avaje.ebean.*;

import java.util.ArrayList;
import java.util.List;
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

import models.*;
import common.*;
import play.db.ebean.Model;



@Security.Authenticated(Secured.class)
public class UserApplication extends Controller {
	public static Result appUserList() {
		List<InputBtn> btn=createText(false);
		
		List<UserView> list = UserApplication.list(500, 1, btn);
		ArrayNode jsonArray = JsonNodeFactory.instance.arrayNode();
		ObjectNode jsonObject;
		SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm:ss");
		for(int i = 0; i < list.size(); i++){
			jsonObject = Json.newObject();
			jsonObject.put("userid", list.get(i).getUserid());
			jsonObject.put("name", list.get(i).getName());
			jsonObject.put("username", list.get(i).getUsername());
			jsonArray.add(jsonObject);
		}
		return ok(jsonArray.toString());
	}
	public static Result userList() {
		
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
		
    	List<UserView> list = UserApplication.list(pageSize,pageNow,btn);
    	
    	//总数
		int countAll = UserApplication.count(btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      	
		return ok(userlist.render(list,countAll,pageNow,pageCount,pageSize,btn));
		
    }
    
	public static List<InputBtn> createText(boolean my){
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	Map<String, String> data=in.data();
    	List<InputBtn> l=new ArrayList<InputBtn>();
    	List<Department> dlist=UserApplication.departList();
    	List<OptionsBtn> olist=new ArrayList<OptionsBtn>();
    	if(!my){
    		InputBtn bt1=new InputBtn("params","params","","用户账号/名称","");
    		olist.add(new OptionsBtn("请选择", ""));
    		for (Department department : dlist) {
    			OptionsBtn ob=new OptionsBtn(department.getName(), department.getName());
    			olist.add(ob);
			}
    		InputBtn bt2=new InputBtn("param0","param0","","部门","",olist);
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
	
	
	public static List<Department> departList(){
		
		//sql语句       
	 	String sql = "select departid from jc_department";   
	
  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("departid",  "departid")   
  		.create();
  		
  		Query<Department> eQ = Ebean.find(Department.class);
  		eQ.setRawSql(rawSql);
  		
  		List<Department> list = eQ.findList();

		return list;
    }
	
    public static List<UserView> list(int pageSize,int pageNow,List<InputBtn>... btn){
		
		int start=(pageNow-1)*pageSize;
		
      	String paramsSql = " and roleid <> -1 ";
		
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (username like '%"+params+"%' or name like '%"+params+"%' )";
      	}
      	
      	String params0="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(1).getValue()!=null&&!btn[0].get(1).getValue().equals("")){
      		params0=btn[0].get(1).getValue().trim();
      		paramsSql+=" and (departname like '%"+params0+"%')";
      	}
		//sql语句       
	 	String sql = "select userid from jc_user_view where 1=1 "+paramsSql+" order by name desc limit "+start+" ,"+pageSize;   
	
  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("userid",  "userid")   
  		.create();
  		
  		Query<UserView> eQ = Ebean.find(UserView.class);
  		eQ.setRawSql(rawSql);
  		
  		List<UserView> list = eQ.findList();

		return list;
    }
    
    public static int count(List<InputBtn>... btn){
		
      	String paramsSql = " and roleid <> -1 ";

      	
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (username like '%"+params+"%' or name like '%"+params+"%' )";
      	}
      	
      	String params0="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(1).getValue()!=null&&!btn[0].get(1).getValue().equals("")){
      		params0=btn[0].get(1).getValue().trim();
      		paramsSql+=" and (departname like '%"+params0+"%')";
      	}
      	
		//sql语句       
	 	String sql = "select count(userid) as count from jc_user_view where 1=1 "+paramsSql;  
     	
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }
    
    public static Result userAdd(){
    
    	List<Role> role = Role.findList();
    	
    	List<Department> department = Department.findList();

		return ok(useradd.render(role,department));
    }
    
    public static Result userAddDo(){
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
  	
    	String username = (String)in.get("username");
    	String password = (String)in.get("password");
    	String mobile = (String)in.get("mobile");
    	//String roleid = (String)in.get("roleid");
    	String departid = (String)in.get("departid");
    	String name = (String)in.get("name");
    	String rolesid=(String)in.get("rolesid");
    	String notelevel=(String)in.get("notelevel");
    	String[] rids=rolesid.split(",");
    	List<Role> roles=new ArrayList<Role>();
    	for (int i = 0; i < rids.length; i++) {
    		if(rids[i]==null||rids[i].equals("")){
    			continue;
    		}
    		roles.add(Role.findById(Integer.parseInt(rids[i])));
		}
    	
    	
    	User user = new User();
    	try{
    		user.setUsername(username);
    		user.setPassword(MD5.GetMD5Code(password));
    		user.setName(name);
    		user.setStatus(0);
    		user.setMobile(mobile);
    		/*if(roleid!=null&&roleid!=""){
    			user.setRoleid(Integer.parseInt(roleid));
			}*/
			if(departid!=null&&departid!=""){
    			user.setDepartid(Integer.parseInt(departid));
			}
			user.setRoles(roles);
			user.setNotelevel(Integer.parseInt(notelevel));
			user.save();
		}catch(Exception e){
			Logger.info("新建失败:e:"+e);
		}  
		
    	Logger.info("新建成功。");
		return redirect("/userList");
    }
    
    public static Result userEdit(int userid){

 		User user = User.findById(userid);
 		
 		List<Role> role = Role.findList();
    	
    	List<Department> department = Department.findList();

    	return ok(useredit.render(user,role,department));
    }
    
    public static Result userinfoEdit(int userid){

 		User user = User.findById(userid);
 		
 		List<Role> role = Role.findList();
    	
    	List<Department> department = Department.findList();

    	return ok(userinfoedit.render(user,role,department));
    }
    
    public static Result userEditDo() {
    	
    	//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
  		
  		String userid = (String)in.get("userid");
    	String mobile = (String)in.get("mobile");
    	//String roleid = (String)in.get("roleid");
    	String departid = (String)in.get("departid");
    	String name = (String)in.get("name");
    	String rolesid=(String)in.get("rolesid");
    	String[] rids=rolesid.split(",");
    	String notelevel=(String)in.get("notelevel");
    	List<Role> roles=new ArrayList<Role>();
    	for (int i = 0; i < rids.length; i++) {
    		if(rids[i]==null||rids[i].equals("")){
    			continue;
    		}
    		roles.add(Role.findById(Integer.parseInt(rids[i])));
		}
    	
    	User user = User.findById(Integer.parseInt(userid));
    	try{
    		user.setName(name);
    		user.setMobile(mobile);
    		/*if(roleid!=null&&roleid!=""){
    			user.setRoleid(Integer.parseInt(roleid));
			}*/
			if(departid!=null&&departid!=""){
    			user.setDepartid(Integer.parseInt(departid));
			}
			user.setRoles(roles);
			
			user.setNotelevel(Integer.parseInt(notelevel));
			user.update();
		}catch(Exception e){
			Logger.info("编辑失败:e:"+e);
		}  
		
    	Logger.info("编辑成功。");
    	return redirect("/userList");
    }
    
    public static Result userinfoEditDo() {
    	
    	//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
  		
  		String userid = (String)in.get("userid");
    	String mobile = (String)in.get("mobile");
    	//String roleid = (String)in.get("roleid");
    	String departid = (String)in.get("departid");
    	String name = (String)in.get("name");
    	
    	String rolesid=(String)in.get("rolesid");
    	String[] rids=rolesid.split(",");
    	List<Role> roles=new ArrayList<Role>();
    	for (int i = 0; i < rids.length; i++) {
    		if(rids[i]==null||rids[i].equals("")){
    			continue;
    		}
    		roles.add(Role.findById(Integer.parseInt(rids[i])));
		}
    	User user = User.findById(Integer.parseInt(userid));
    	try{
    		user.setName(name);
    		user.setMobile(mobile);
    		/*if(roleid!=null&&roleid!=""){
    			user.setRoleid(Integer.parseInt(roleid));
			}*/
			if(departid!=null&&departid!=""){
    			user.setDepartid(Integer.parseInt(departid));
			}
			user.setRoles(roles);
			user.update();
		}catch(Exception e){
			Logger.info("编辑失败:e:"+e);
		}  
		
    	Logger.info("编辑成功。");
    	return redirect("/myportalList");
    }
    
    public static Result userDel(int userid){

 		User.del(userid);

		return redirect("/userList");
    }
    
    public static Result userRecover(int userid){

 		User.recover(userid);

		return redirect("/userList");
    }
    
    public static Result passwordEdit(){

		return ok(passwordedit.render(""));
    }
    
    public static Result passwordEditDo(){
    
    	//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
  		
  		String pwdOld = (String)in.get("pwdOld");
    	String pwdNew = (String)in.get("pwdNew");
    	
    	String message = "修改密码失败！";

    	User user = User.findById(Integer.parseInt(session("userid")));
    	
    	if(!user.getPassword().equals(MD5.GetMD5Code(pwdOld))){
    		message = "输入的旧密码错误！";
    		return ok(passwordedit.render(message));
    	}
    	
    	try{
    		user.setPassword(MD5.GetMD5Code(pwdNew));
			user.update();
			
			message = "修改密码成功！";
		}catch(Exception e){
			Logger.info("修改密码失败:e:"+e);
			message = "修改密码失败！";
		}  

		return ok(passwordedit.render(message));
    }
    
    
    public static Result userpassFail(int userid){

 		User user = User.findById(userid);
 		
 		List<Role> role = Role.findList();
    	
    	List<Department> department = Department.findList();

    	return ok(userpassfail.render(user,role,department));
    }
    
    public static Result userpassFailDo() {
    	
    	//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
  		
  		String userid = (String)in.get("userid");
    	String password="oa123456";
    	password=MD5.GetMD5Code(password);
    	User user = User.findById(Integer.parseInt(userid));
    	try{
    		user.setPassword(password);
			user.update();
		}catch(Exception e){
			Logger.info("编辑失败:e:"+e);
		}  
		
    	Logger.info("编辑成功。");
    	return redirect("/userList");
    }
}
