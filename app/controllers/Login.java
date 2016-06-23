package controllers;

import play.*;
import play.mvc.*;
import play.cache.Cache;
import play.mvc.Http.Session;

import views.html.*;
import play.data.Form;
import play.data.DynamicForm;
import play.data.validation.Constraints.*;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;

import models.*;
import common.*;

public class Login extends Controller {
	
	static Set<Integer> intset=new HashSet<Integer>();
	//登录
	public static Result login(){
		
		if(session("username")==null||session("username")==""){
			return ok(login.render("请输入您的账号和密码.",0));
		}
		else{
		
			return redirect(Play.application().configuration().getString("loginFirstPage"));
			
		}
	}
	
	//APP登录
	public static Result appLogin(){
		//输入参数
		DynamicForm in = Form.form().bindFromRequest();
		
		String username = in.get("username");
		String password = in.get("password");
		
		if(username==null||username==""){
			return ok("[{\"code\":FAIL,\"msg\":\"请输入用户名！\"}]");
		}
		
		if(password==null||password==""){
			return ok("[{\"code\":FAIL,\"msg\":\"请输入密码！\"}]");
		}
		
		if(User.authenticate(username, MD5.GetMD5Code(password)) == null) {
            return ok("[{\"code\":FAIL,\"msg\":\"用户名或密码错误！\"}]");
        }
		
		User user = User.findByUsername(username);
		int did=user.getDepartid();
		intset.add(did);
		findParentDid(did);
		String dcodes="";
		Iterator<Integer> it = intset.iterator();
		while(it.hasNext()){
			dcodes+=it.next()+",";
		}
		dcodes=dcodes.substring(0, dcodes.length()-1);

		
		if(user.getStatus()==1){
			return ok("[{\"code\":FAIL,\"msg\":\"账号已停用！\"}]");
		}
		session("name",user.getName());
		session("userid",String.valueOf(user.getUserid()));
		session("username", username);
		session("password", password);
		session("departid",String.valueOf(user.getDepartid()));
		session("roleid",String.valueOf(user.getRoleid()));
		session("dcodes",dcodes);
		return ok("[{\"code\":SUCCESS,\"msg\":\"登陆成功！\"}]");
	}
	//APP登出
	public static Result appLogout(){
		
		for (int i = 0; i < CommonConfig.LogList[0].length; i++) {
   			if("/logout".contains(CommonConfig.LogList[0][i])){
   				SysLogApplication.saveLog(CommonConfig.LogList[1][i],"对"+CommonConfig.LogList[1][i]+"-"+CommonConfig.LogList[2][i]+"进行操作");
   			}

   		}
   		
		session().clear();
		flash("成功", "成功登出！");
		
		return ok("[{\"code\":SUCCESS,\"msg\":\"成功登出！\"}]");
	}
	
	public static void findParentDid(int did){
		List<SqlRow> lists= findNodeDepartment(did);
		if(lists.size()!=0){
			for (int i = 0; i < lists.size(); i++) {
				intset.add(lists.get(i).getInteger("departid"));
				findParentDid(lists.get(i).getInteger("departid"));
			}
		}else{
			intset.add(0);
		}
	}
	
	
	public static List<SqlRow> findNodeDepartment(int did){
		String sql="select * from jc_department where pardepartid='"+did+"'";
		List<SqlRow> lists = Ebean.createSqlQuery(sql).findList();
		
		return lists;
	}
	
	
	//登录
	public static Result loginDo(){
		
		//输入参数
		DynamicForm in = Form.form().bindFromRequest();
		
		String username = in.get("username");
		String password = in.get("password");
		
		if(username==null||username==""){
			return ok(login.render("请输入用户名！",1));
		}
		
		if(password==null||password==""){
			return ok(login.render("请输入密码！",1));
		}
		
		if(User.authenticate(username, MD5.GetMD5Code(password)) == null) {
            return ok(login.render("用户名或密码错误！",1));
        }
		
		User user = User.findByUsername(username);
		int did=user.getDepartid();
		intset.add(did);
		findParentDid(did);
		String dcodes="";
		Iterator<Integer> it = intset.iterator();
		while(it.hasNext()){
			dcodes+=it.next()+",";
		}
		dcodes=dcodes.substring(0, dcodes.length()-1);
		
		if(user.getStatus()==1){
			return ok(login.render("账号已停用！",1));
		}
		session("name",user.getName());
		session("userid",String.valueOf(user.getUserid()));
		session("username", username);
		session("password", password);
		session("departid",String.valueOf(user.getDepartid()));
		session("roleid",String.valueOf(user.getRoleid()));
		session("dcodes",dcodes);
		session("notelevel",String.valueOf(user.getNotelevel()));
		List<Role> roles=user.getRoles();
		List<RoleAuthView> roleAuthList =new ArrayList<RoleAuthView>();
		for (Role role : roles) {
			roleAuthList.addAll(RoleAuthView.findListById(role.getRoleid()));
		}
		
		for(RoleAuthView roleAuth:roleAuthList){
			
			session(roleAuth.getShortname(), roleAuth.getAuthid());
		}
		
		for (int i = 0; i < CommonConfig.LogList[0].length; i++) {
   			if("/login".contains(CommonConfig.LogList[0][i])){
   				SysLogApplication.saveLog(CommonConfig.LogList[1][i],CommonConfig.LogList[1][i]);
   			}

   		}
		
		return redirect(Play.application().configuration().getString("loginFirstPage"));
	}
	
	//登出
// 	@Security.Authenticated(Secured.class)
	public static Result logout(){
		
		for (int i = 0; i < CommonConfig.LogList[0].length; i++) {
   			if("/logout".contains(CommonConfig.LogList[0][i])){
   				SysLogApplication.saveLog(CommonConfig.LogList[1][i],"对"+CommonConfig.LogList[1][i]+"-"+CommonConfig.LogList[2][i]+"进行操作");
   			}

   		}
   		
		session().clear();
		flash("成功", "成功登出！");
		
		return redirect("/login");
	}

}
