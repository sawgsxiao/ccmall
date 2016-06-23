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
import models.*;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.lang.NullPointerException;
import java.io.ByteArrayOutputStream;
import javax.imageio.stream.FileImageInputStream;

import play.db.ebean.Model;

@Security.Authenticated(Secured.class)
public class SuranceProductApplication extends Controller {
	
	/*加载树后，激活指定的组*/
	public static Result suranceassortListPlus(int groupId) {
		
		ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
		//加载树形文件夹
		//String treeStr = getTreeData(0).toString();
		return ok(suranceassort.render(groupId,"departtree"));
		
    }
	
	private static ArrayNode getTreeData(int pid){
    	String sql = "select id assortid,assortname name ,parentid parassortid " +
    				   " from cc_surance_assort ";
		List<SqlRow> lists = Ebean.createSqlQuery(sql).findList();
		ArrayNode ja = JsonNodeFactory.instance.arrayNode();
		ArrayNode childja = JsonNodeFactory.instance.arrayNode();
		ObjectNode jo;
		jo = Json.newObject();
		jo.put("id", 0);
		jo.put("pid", "");
		jo.put("name", "保险品牌");
		ja.add(jo);
		if(lists.size()>0){
			for(int i = 0; i < lists.size(); i++){
				jo = Json.newObject();
				jo.put("id", lists.get(i).getInteger("assortid"));
				jo.put("pid", lists.get(i).getString("parassortid"));
				jo.put("name", lists.get(i).getString("name"));
				ja.add(jo);
			}
			
		}
		return ja;
		
    }
	
	public static Result suranceassortData(int pid) {
		return ok(getTreeData(pid).toString());
	}
	
	public static Result suranceassortEdit(int id){
		DynamicForm in = Form.form().bindFromRequest();
		SuranceAssort assort=SuranceAssort.findById(id);
		if(assort == null){
			assort = new SuranceAssort();
			assort.setAssortname(in.get("name"));
			assort.setParentid(in.get("pid"));
			assort.setCreatetime(new Date());
			assort.setAssortcode("");
			assort.save();
			return ok("[{\"code\":1,\"msg\":\"处理成功\",\"newid\":" + assort.getId() + "}]");
		}
		int result = Ebean.createSqlUpdate("update cc_surance_assort set assortname = :name where id=:gid").setParameter("name", in.get("name")).setParameter("gid", id).execute();
		if(result > 0){
			return ok("[{\"code\":1,\"msg\":\"处理成功\"}]");
		}else{
			return ok("[{\"code\":0,\"msg\":\"处理失败\"}]");
		}
	}
	public static Result suranceassortDel(int id){
		
		/*if(departmentInUser(id)){
			return ok("[{\"code\":2,\"msg\":\"品牌已被使用,不能删除!\"}]");
		}*/
		int result = Ebean.createSqlUpdate("delete from cc_surance_assort where id=:gid").setParameter("gid", id).execute();
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
	
	public static Result suranceassortCheckDel(int id){
		
		if(true){
			return ok("[{\"code\":2,\"msg\":\"品牌已被使用,不能删除!\"}]");
		}
		return ok("[{\"code\":1,\"msg\":\"处理成功!\"}]");
	}
	
    public static Result suranceproductAdd() {
		
		List<SuranceAssort> assorts=SuranceAssort.findAllList();
		
		return ok(suranceproductadd.render(assorts));
		
	}
	
	public static Result suranceproductAddDo(){
		DynamicForm in = Form.form().bindFromRequest();
		String surancename=in.get("surancename");
		String suranceassort=in.get("suranceassort");
		String productcode=in.get("productcode");
		String price=in.get("price");
		String remark=in.get("remark");
		String inusetime=in.get("inusetime");
		
		Surance  surance=new Surance();
		surance.setAssortcode(suranceassort);
		surance.setAssortname(SuranceAssort.findById(Integer.parseInt(suranceassort)).getAssortname());
		surance.setSurancename(surancename);
		surance.setCreatetime(new Date());
		surance.setProductcode(productcode);
		surance.setRemark(remark);
		surance.setPrice(price);
		surance.setInusetime(inusetime);
		
		List<SuranceImage> images=new ArrayList<SuranceImage>();
		List<String> strings=new ArrayList<String>();

		Set<String> set= in.data().keySet();
		Pattern pattern=Pattern.compile("^filename\\d+$"); 
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			if(pattern.matcher(string).find()){
				strings.add(string);
			}
		}
		Object[] array=strings.toArray();
		Arrays.sort(array);
		 for (int i = 0; i < array.length; i++) {
			 SuranceImage image=new SuranceImage();
			 image.setCreatetime(new Date());
			 image.setUrl(in.get(array[i]+""));
			 images.add(image);
		}
		surance.setImages(images);
		surance.save();
		
		return ok("[{\"code\":1,\"msg\":\"处理成功!\"}]");
	}
	
	
	public static Result suranceproductList() {
		
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
		
    	List<Surance> list = SuranceProductApplication.list(pageSize,pageNow,btn);
    	
    	//总数
		int countAll = SuranceProductApplication.count(btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      	
		return ok(suranceproduct.render(list,countAll,pageNow,pageCount,pageSize,"suranceproduct",btn));
		
    }
    
	public static List<InputBtn> createText(boolean my){
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	Map<String, String> data=in.data();
    	List<InputBtn> l=new ArrayList<InputBtn>();
    	if(!my){
    		InputBtn bt1=new InputBtn("params","params","","保险名称","");
    		

    		InputBtn bt2=new InputBtn("params0","params0","","产品编码","");
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
	
    public static List<Surance> list(int pageSize,int pageNow,List<InputBtn>... btn){
		
		int start=(pageNow-1)*pageSize;
		
      	String paramsSql = "";
		
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (surancename like '%"+params+"%')";
      	}
      	
      	String params0="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(1).getValue()!=null&&!btn[0].get(1).getValue().equals("")){
      		params0=btn[0].get(1).getValue().trim();
      		paramsSql+=" and (productcode like '%"+params0+"%')";
      	}
		//sql语句       
	 	String sql = "select id from cc_surance_product where 1=1 "+paramsSql+" order by createtime desc limit "+start+" ,"+pageSize;   
	
  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")   
  		.create();
  		
  		Query<Surance> eQ = Ebean.find(Surance.class);
  		eQ.setRawSql(rawSql);
  		
  		List<Surance> list = eQ.findList();

		return list;
    }
    
    public static int count(List<InputBtn>... btn){
		
      	String paramsSql = "";

      	
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (surancename like '%"+params+"%')";
      	}
      	
      	String params0="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(1).getValue()!=null&&!btn[0].get(1).getValue().equals("")){
      		params0=btn[0].get(1).getValue().trim();
      		paramsSql+=" and (productcode like '%"+params0+"%')";
      	}
		//sql语句       
	 	String sql = "select count(id) as count from cc_surance_product where 1=1 "+paramsSql;  
     	
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }
    
    
    public static Result suranceproductDetailed(int id) {
		
		List<SuranceAssort> assorts=SuranceAssort.findAllList();
		
		Surance surance= Surance.findById(id);
		
		return ok(suranceproductdetail.render(assorts,surance));
		
	}
    
    public static Result suranceproductDel(int id) {
				
    	Surance surance= Surance.findById(id);
		try{
			surance.delete();
		}catch (Exception e) {
			return ok("[{\"code\":2,\"msg\":\"品牌已被使用,不能删除!\"}]");
		}
		return ok("[{\"code\":1,\"msg\":\"处理成功!\"}]");
		
	}
    
    public static Result suranceproductEdit(int id) {
		
		List<SuranceAssort> assorts=SuranceAssort.findAllList();
		
		Surance surance= Surance.findById(id);
		
		return ok(suranceproductedit.render(assorts,surance));
		
	}
    
    public static Result suranceproductTried(int id) {
		
    	List<SuranceAssort> assorts=SuranceAssort.findAllList();
		
		Surance surance= Surance.findById(id);
		
		return ok(suranceproducttried.render(assorts,surance));
		
	}
    
    
    public static Result suranceproductEditDo() {
		
    	DynamicForm in = Form.form().bindFromRequest();
    	String suranceid=in.get("suranceid");
		String surancename=in.get("surancename");
		String suranceassort=in.get("suranceassort");
		String productcode=in.get("productcode");
		String price=in.get("price");
		String inusetime=in.get("inusetime");
		String remark=in.get("remark");
		
		Surance surance=Surance.findById(Integer.parseInt(suranceid));
		surance.setAssortcode(suranceassort);
		surance.setAssortname(SuranceAssort.findById(Integer.parseInt(suranceassort)).getAssortname());
		surance.setSurancename(surancename);
		surance.setCreatetime(new Date());
		surance.setProductcode(productcode);
		surance.setRemark(remark);
		surance.setPrice(price);
		surance.setInusetime(inusetime);
		
		List<SuranceImage> suranceImages=new ArrayList<SuranceImage>();
		List<String> strings=new ArrayList<String>();

		Set<String> set= in.data().keySet();
		Pattern pattern=Pattern.compile("^filename\\d+$"); 
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			if(pattern.matcher(string).find()){
				strings.add(string);
			}
		}
		
		if(strings.size()>0){
			if(surance.getImages().size()>0){
				for (int i = 0; i < surance.getImages().size(); i++) {
					surance.getImages().get(i).delete();
				}
			}
			
			Object[] array=strings.toArray();
			Arrays.sort(array);
			 for (int i = 0; i < array.length; i++) {
				 SuranceImage image=new SuranceImage();
				 image.setCreatetime(new Date());
				 image.setUrl(in.get(array[i]+""));
				 suranceImages.add(image);
			}
			 surance.setImages(suranceImages);
		}
		
		surance.update();
		
		return redirect("/suranceproductList");
		
	}
    
    public static Result suranceproductStand(int id) {
		
		Surance surance= Surance.findById(id);
		surance.setStand(1);
		surance.update();
		
		return redirect("/suranceproductList");
		
	}
    
    public static Result suranceproductStandCh(int id) {
		
    	Surance surance= Surance.findById(id);
    	surance.setStand(0);
    	surance.update();
		
		return redirect("/suranceproductList");
		
	}
}
