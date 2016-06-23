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
public class AuthApplication extends Controller {
/*
 * 废弃
 */
	/*public static Result authList() {
	
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	String roleid = (String)in.get("roleid");

    	List<Auth> firstlist = Auth.findListForParentid(1,"0");
    	
    	List<AuthFirst> authFirstList = new ArrayList<AuthFirst>();
    	
    	int num = 0;
    	for(Auth first:firstlist){

    		AuthFirst authFirst = new AuthFirst();
    		authFirst.setId(first.getId());
    		authFirst.setName(first.getName());
    		authFirst.setAuthid(first.getAuthid());
    		authFirst.setParentid(first.getParentid());
    		authFirst.setLevels(first.getLevels());
    		authFirst.setSeq(num);
    		
    		List<Auth> secondlist = Auth.findListForParentid(2,authFirst.getAuthid());
    		List<AuthSecond> authSecondList = new ArrayList<AuthSecond>();
    		
    		for(Auth second:secondlist){
    			AuthSecond authSecond = new AuthSecond();
    			authSecond.setId(second.getId());
    			authSecond.setName(second.getName());
    			authSecond.setAuthid(second.getAuthid());
    			authSecond.setParentid(second.getParentid());
    			authSecond.setLevels(second.getLevels());
    			
    			List<Auth> thirdlist = Auth.findListForParentid(3,authSecond.getAuthid());
    			
    			authSecond.setList(thirdlist);
    			authSecondList.add(authSecond);
    		}
    		
    		authFirst.setList(authSecondList);
    		
    		authFirstList.add(authFirst);
    		
    		num++;
    	}
//     	authlist.setList(nodelist);

		//个人权限
		List<RoleAuth> roleAuthList = RoleAuthApplication.list(Integer.parseInt(roleid));

		return ok(auth.render(authFirstList,roleid,roleAuthList));
		
    }*/
	public static Result authTree() {
		return ok(getTreeData().toString());
	}
    private static ArrayNode getTreeData(){
    	DynamicForm in = Form.form().bindFromRequest();
  		
  		String roleid = (String)in.get("roleid");
    	String sql = "select * from jc_auth order by authid" ;
    	String sqlrole="select * from jc_role_auth where roleid='"+roleid+"'";
		List<SqlRow> lists = Ebean.createSqlQuery(sql).findList();
		List<SqlRow> listr = Ebean.createSqlQuery(sqlrole).findList();
		ArrayNode ja = JsonNodeFactory.instance.arrayNode();
		//ArrayNode childja = JsonNodeFactory.instance.arrayNode();

		ObjectNode jo;
		jo = Json.newObject();
		jo.put("id", 0);
		jo.put("pid", "");
		jo.put("name", "根目录");
		jo.put("click", "");
		if(listr.size()>0){
			jo.put("checked", true);
		}else{
			jo.put("checked", false);
		}
		
		ja.add(jo);
		String rid="";
		if(lists.size()>0){
			for(int i = 0; i < lists.size(); i++){
				
					jo = Json.newObject();
					rid=lists.get(i).getString("authid");
					jo.put("id", rid);
					jo.put("pid", lists.get(i).getString("parentid"));
					jo.put("name", lists.get(i).getString("name"));
					jo.put("click", "");
					jo.put("checked", false);
					for (int j = 0; j < listr.size(); j++) {
						if(rid.equals(listr.get(j).getString("authid"))){
							jo.put("checked", true);
							break;
						}
					}
					ja.add(jo);
				
				
			}
			return ja;
		}
		return null;
		
    }
    
    public static Result authTreeInit(){
    	DynamicForm in = Form.form().bindFromRequest();
  		
  		String roleid = (String)in.get("roleid");
    	
		return ok(authtree.render(roleid));
    }
	
    public static Result authAddDo() {
    	
    	//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	String roleid = (String)in.get("roleid");
		
    	String arrays=(String)in.get("arrays");
    	
    	String sql="delete from jc_role_auth where roleid="+roleid;
    	
    	Ebean.createSqlUpdate(sql).execute();
    	
		Map<String, String> map = new HashMap<String, String>();
		map = in.data();
		String[] arrs=arrays.split(",");
		
		
		String msg="{msg:操作失败!}";
		for (int i = 0; i < arrs.length; i++) {
			if(arrs[i].equals("")){
				continue;
			}
			RoleAuth roleAuth = new RoleAuth();
			try{
   				roleAuth.setRoleid(Integer.parseInt(roleid));
   				roleAuth.setAuthid(arrs[i]);
   				roleAuth.save();
   			}catch(Exception e){
   				
				Logger.info("保存权限失败:e:"+e);
				return ok(msg.toString());
			} 
		}
    	return redirect("/roleList");
    }
    
    //app使用
    public static Result getMyAuthList(){
    	String StrUid = session("userid");
    	if(StrUid == "")
    		return ok("[{authid:0,authname:\"无\"}]");
    	User user = User.findById(Integer.parseInt(StrUid));
    	List<Role> roles=user.getRoles();
		List<RoleAuthView> roleAuthList =new ArrayList<RoleAuthView>();
		for (Role role : roles) {
			roleAuthList.addAll(RoleAuthView.findListById(role.getRoleid()));
		}
		ArrayNode jsonArray = JsonNodeFactory.instance.arrayNode();
		ObjectNode jsonObject;
		for(RoleAuthView roleAuth:roleAuthList){
			jsonObject = Json.newObject();
			jsonObject.put("authid", roleAuth.getAuthid());
			jsonObject.put("authname", roleAuth.getName());
			jsonArray.add(jsonObject);
		}
		return ok(jsonArray.toString());
    }
  
}
