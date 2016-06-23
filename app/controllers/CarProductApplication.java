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
import common.UtilsObject;

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
public class CarProductApplication extends Controller {
	
	/*加载树后，激活指定的组*/
	public static Result carassortListPlus(int groupId) {
		
		ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
		//加载树形文件夹
		//String treeStr = getTreeData(0).toString();
		return ok(carassort.render(groupId,"departtree"));
		
    }
	
	private static ArrayNode getTreeData(int pid){
    	String sql = "select id assortid,assortname name ,parentid parassortid " +
    				   " from cc_car_assort ";
		List<SqlRow> lists = Ebean.createSqlQuery(sql).findList();
		ArrayNode ja = JsonNodeFactory.instance.arrayNode();
		ArrayNode childja = JsonNodeFactory.instance.arrayNode();
		ObjectNode jo;
		jo = Json.newObject();
		jo.put("id", 0);
		jo.put("pid", "");
		jo.put("name", "汽车品牌");
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
	
	public static Result carassortData(int pid) {
		return ok(getTreeData(pid).toString());
	}
	
	public static Result carassortEdit(int id){
		DynamicForm in = Form.form().bindFromRequest();
		CarAssort carAssort=CarAssort.findById(id);
		String title=(UtilsObject.getPYIndexStr(in.get("name").substring(0,1).toUpperCase(), UtilsObject.checkStrChinese(in.get("name")))).substring(0, 1);
		String code="";
		if(carAssort == null){
			carAssort = new CarAssort();
			carAssort.setAssortname(in.get("name"));
			carAssort.setParentid("0");
			carAssort.setCreatetime(new Date());
			carAssort.setTitle(title);
			code=searchNextCode(title);
			code=title+code;
			carAssort.setAssortcode(code);
			carAssort.save();
			return ok("[{\"code\":1,\"msg\":\"处理成功\",\"newid\":" + carAssort.getId() + "}]");
		}
		int result = Ebean.createSqlUpdate("update cc_car_assort set assortname = :name where id=:gid").setParameter("name", in.get("name")).setParameter("gid", id).execute();
		carAssort.setAssortname(in.get("name"));
		carAssort.setParentid("0");
		carAssort.setTitle(title);
		code=searchNextCode(title);
		code=title+code;
		carAssort.setAssortcode(code);
		carAssort.update();
		if(result > 0){
			return ok("[{\"code\":1,\"msg\":\"处理成功\"}]");
		}else{
			return ok("[{\"code\":0,\"msg\":\"处理失败\"}]");
		}
	}
	public static Result carassortDel(int id){
		
		/*if(departmentInUser(id)){
			return ok("[{\"code\":2,\"msg\":\"品牌已被使用,不能删除!\"}]");
		}*/
		int result = Ebean.createSqlUpdate("delete from cc_car_assort where id=:gid").setParameter("gid", id).execute();
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
	
	public static Result carassortCheckDel(int id){
		
		if(true){
			return ok("[{\"code\":2,\"msg\":\"品牌已被使用,不能删除!\"}]");
		}
		return ok("[{\"code\":1,\"msg\":\"处理成功!\"}]");
	}
	
	
	public static Result carproductAddDo(){
		DynamicForm in = Form.form().bindFromRequest();
		String carname=in.get("carname");
		String carassort=in.get("carassort");
		String productcode=in.get("productcode");
		String pricerange=in.get("pricerange");
		String remark=in.get("remark");
		String colorlist=in.get("colorlist");
		String size=in.get("size");
		
		Car car=new Car();
		car.setAssortcode(carassort);
		car.setAssortname(CarAssort.findById(Integer.parseInt(carassort)).getAssortname());
		car.setCarname(carname);
		car.setCreatetime(new Date());
		car.setProductcode(productcode);
		car.setRemark(remark);
		car.setPricerange(pricerange);
		car.setColorlist(colorlist);
		car.setSize(size);
		car.setCode(CarAssort.findById(Integer.parseInt(carassort)).getAssortcode());
		
		List<CarImage> carImages=new ArrayList<CarImage>();
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
			 CarImage image=new CarImage();
			 image.setCreatetime(new Date());
			 image.setUrl(in.get(array[i]+""));
			 carImages.add(image);
		}
		car.setImages(carImages);
		car.save();
		
		return ok("[{\"code\":1,\"msg\":\"处理成功!\"}]");
	}
	
	
	public static Result carproductList() {
		
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
		
    	List<Car> list = CarProductApplication.list(pageSize,pageNow,btn);
    	
    	//总数
		int countAll = CarProductApplication.count(btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      	
		return ok(carproduct.render(list,countAll,pageNow,pageCount,pageSize,"carproduct",btn));
		
    }
    
	public static List<InputBtn> createText(boolean my){
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	Map<String, String> data=in.data();
    	List<InputBtn> l=new ArrayList<InputBtn>();
    	if(!my){
    		InputBtn bt1=new InputBtn("params","params","","汽车车型","");
    		

    		InputBtn bt2=new InputBtn("params0","params0","","汽车类别","");
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
	
    public static List<Car> list(int pageSize,int pageNow,List<InputBtn>... btn){
		
		int start=(pageNow-1)*pageSize;
		
      	String paramsSql = "";
		
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (carname like '%"+params+"%')";
      	}
      	
      	String params0="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(1).getValue()!=null&&!btn[0].get(1).getValue().equals("")){
      		params0=btn[0].get(1).getValue().trim();
      		paramsSql+=" and (productcode like '%"+params0+"%')";
      	}
		//sql语句       
	 	String sql = "select id from cc_car_product where 1=1 "+paramsSql+" order by createtime desc limit "+start+" ,"+pageSize;   
	
  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")   
  		.create();
  		
  		Query<Car> eQ = Ebean.find(Car.class);
  		eQ.setRawSql(rawSql);
  		
  		List<Car> list = eQ.findList();

		return list;
    }
    
    public static int count(List<InputBtn>... btn){
		
      	String paramsSql = "";

      	
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (carname like '%"+params+"%')";
      	}
      	
      	String params0="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(1).getValue()!=null&&!btn[0].get(1).getValue().equals("")){
      		params0=btn[0].get(1).getValue().trim();
      		paramsSql+=" and (productcode like '%"+params0+"%')";
      	}
		//sql语句       
	 	String sql = "select count(id) as count from cc_car_product where 1=1 "+paramsSql;  
     	
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }
    
    public static Result carproductAdd() {
		
		List<CarAssort> assorts=CarAssort.findAllList();
		
		return ok(carproductadd.render(assorts));
		
	}
    
    public static Result carproductDetailed(int id) {
		
		List<CarAssort> assorts=CarAssort.findAllList();
		
		Car car= Car.findById(id);
		
		return ok(carproductdetail.render(assorts,car));
		
	}
    
    public static Result carproductDel(int id) {
				
		Car car= Car.findById(id);
		try{
			car.delete();
		}catch (Exception e) {
			return ok("[{\"code\":2,\"msg\":\"品牌已被使用,不能删除!\"}]");
		}
		return ok("[{\"code\":1,\"msg\":\"处理成功!\"}]");
		
	}
    
    public static Result carproductEdit(int id) {
		
		List<CarAssort> assorts=CarAssort.findAllList();
		
		Car car= Car.findById(id);
		
		return ok(carproductedit.render(assorts,car));
		
	}
    
    public static Result carproductTried(int id) {
		
		List<CarAssort> assorts=CarAssort.findAllList();
		
		Car car= Car.findById(id);
		
		return ok(carproducttried.render(assorts,car));
		
	}
    
    
    public static Result carproductEditDo() {
		
    	DynamicForm in = Form.form().bindFromRequest();
    	String carid=in.get("carid");
		String carname=in.get("carname");
		String carassort=in.get("carassort");
		String productcode=in.get("productcode");
		String pricerange=in.get("pricerange");
		String remark=in.get("remark");
		String colorlist=in.get("colorlist");
		String size=in.get("size");
		
		
		Car car=Car.findById(Integer.parseInt(carid));
		car.setAssortcode(carassort);
		car.setAssortname(CarAssort.findById(Integer.parseInt(carassort)).getAssortname());
		car.setCarname(carname);
		car.setCreatetime(new Date());
		car.setProductcode(productcode);
		car.setRemark(remark);
		car.setPricerange(pricerange);
		car.setColorlist(colorlist);
		car.setSize(size);
		car.setCode(CarAssort.findById(Integer.parseInt(carassort)).getAssortcode());
		
		List<CarImage> carImages=new ArrayList<CarImage>();
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
			if(car.getImages().size()>0){
				for (int i = 0; i < car.getImages().size(); i++) {
					car.getImages().get(i).delete();
				}
			}
			
			Object[] array=strings.toArray();
			Arrays.sort(array);
			 for (int i = 0; i < array.length; i++) {
				 CarImage image=new CarImage();
				 image.setCreatetime(new Date());
				 image.setUrl(in.get(array[i]+""));
				 carImages.add(image);
			}
			car.setImages(carImages);
		}
		
		car.update();
		
		return redirect("/carproductList");
		
	}
    
    public static Result carproductStand(int id) {
		
		Car car= Car.findById(id);
		car.setStand(1);
		car.update();
		
		return redirect("/carproductList");
		
	}
    
    public static Result carproductStandCh(int id) {
		
		Car car= Car.findById(id);
		car.setStand(0);
		car.update();
		
		return redirect("/carproductList");
		
	}
    
    public static String searchNextCode(String title){
		//sql语句       
	 	String sql = "select max(assortcode) as code from cc_car_assort where 1=1  and title='"+title+"'";  
     	
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        String code = row.getString("code");  
        
        int now=0;
        if(code!=null&&!code.equals("")){
        	now=(Integer.parseInt(code.substring(1, code.length())))+1;
        	if(String.valueOf(now).length()==1){
        		code="00"+String.valueOf(now);	
        	}else if(String.valueOf(now).length()==2){
        		code="0"+String.valueOf(now);
        	}else if(String.valueOf(now).length()==3){
        		code=String.valueOf(now);
        	}
        	return code;
        }
        return "001";
	}
}
