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
import com.fasterxml.jackson.core.JsonProcessingException;

import play.api.libs.ws.Response;
import play.api.libs.ws.WS;
import play.data.DynamicForm;
import com.avaje.ebean.*;

import common.UUIDGenerator;
import common.UtilsObject;

import java.util.ArrayList;
import java.util.List;
import models.*;
import net.sf.json.JSONObject;

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

public class SimpleOrderApplication extends Controller {

	/*
	 * 其中：payType取值：0，零首付；1，首付20%"；2，首付30%
	 *	payPeriod取值：0，24个月；1，36个月
	 */
	
	public static Result buyCarList() {
		
		ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	List<InputBtn> btn=buyCarCreateText(false);
    	//分页
    	//每页显示数据
		int pageSize = Integer.parseInt(Play.application().configuration().getString("pageSize"));
		
		//当前第N页
		int pageNow = 1;
		
		String pageNowIn = (String)in.get("pageNow");
		if(pageNowIn!=null){
		
			pageNow = Integer.parseInt(pageNowIn);
		}
		
    	List<AppBuyCar> list = SimpleOrderApplication.buyCarList(pageSize,pageNow,btn);
    	
    	//总数
		int countAll = SimpleOrderApplication.buyCarCount(btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      	
		return ok(buycar.render(list,countAll,pageNow,pageCount,pageSize,"elite",btn));
		
    }
    
	public static List<InputBtn> buyCarCreateText(boolean my){
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	Map<String, String> data=in.data();
    	List<InputBtn> l=new ArrayList<InputBtn>();
    	if(!my){
    		InputBtn bt1=new InputBtn("params","params","","客户名称","");
    		InputBtn bt2=new InputBtn("params0","params0","","手机号码","");
    		
    		List<OptionsBtn> opts=new ArrayList<OptionsBtn>();
    		opts.add(new OptionsBtn("请选择",""));
    		opts.add(new OptionsBtn("零首付","0"));
    		opts.add(new OptionsBtn("首付20%","1"));
    		opts.add(new OptionsBtn("首付30%","2"));
    		InputBtn bt3=new InputBtn("params1","params1","","付款方式","",InputBtn.CTYPE_SELECT,opts);
    		
    		List<OptionsBtn> opt=new ArrayList<OptionsBtn>();
    		opt.add(new OptionsBtn("请选择",""));
    		opt.add(new OptionsBtn("24个月","0"));
    		opt.add(new OptionsBtn("36个月","1"));
    		InputBtn bt4=new InputBtn("params2","params2","","分期","",InputBtn.CTYPE_SELECT,opt);
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
	
    public static List<AppBuyCar> buyCarList(int pageSize,int pageNow,List<InputBtn>... btn){
		
		int start=(pageNow-1)*pageSize;
		
      	String paramsSql = "";
		
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (name like '%"+params+"%')";
      	}
      	String params0="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(1).getValue()!=null&&!btn[0].get(1).getValue().equals("")){
      		params=btn[0].get(1).getValue().trim();
      		paramsSql+=" and (mobile like '%"+params+"%')";
      	}

      	String params1="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(2).getValue()!=null&&!btn[0].get(2).getValue().equals("")){
      		params1=btn[0].get(2).getValue().trim();
      		paramsSql+=" and (paytype like '%"+params1+"%')";
      	}
      	
      	String params2="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(3).getValue()!=null&&!btn[0].get(3).getValue().equals("")){
      		params2=btn[0].get(3).getValue().trim();
      		paramsSql+=" and (payperiod like '%"+params2+"%')";
      	}
		//sql语句       
	 	String sql = "select id from cc_app_buycar where 1=1 "+paramsSql+" order by createtime desc limit "+start+" ,"+pageSize;   
	
  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")   
  		.create();
  		
  		Query<AppBuyCar> eQ = Ebean.find(AppBuyCar.class);
  		eQ.setRawSql(rawSql);
  		
  		List<AppBuyCar> list = eQ.findList();

		return list;
    }
    
    public static int buyCarCount(List<InputBtn>... btn){
		
      	String paramsSql = "";

      	
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (name like '%"+params+"%')";
      	}
      	String params0="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(1).getValue()!=null&&!btn[0].get(1).getValue().equals("")){
      		params=btn[0].get(1).getValue().trim();
      		paramsSql+=" and (mobile like '%"+params+"%')";
      	}
      	
      	String params1="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(2).getValue()!=null&&!btn[0].get(2).getValue().equals("")){
      		params1=btn[0].get(2).getValue().trim();
      		paramsSql+=" and (paytype like '%"+params1+"%')";
      	}
      	
      	String params2="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(3).getValue()!=null&&!btn[0].get(3).getValue().equals("")){
      		params2=btn[0].get(3).getValue().trim();
      		paramsSql+=" and (payperiod like '%"+params2+"%')";
      	}
		//sql语句       
	 	String sql = "select count(id) as count from cc_app_buycar where 1=1 "+paramsSql;  
     	
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }
	

	/**
	 *  companyType: 0-"公务员",1-"事业单位职员",2-"国企职员",3-"世界500强企业正式员工"
		 payType:0-"零首付", 1-"首付20%", 2-"首付30%"
		 payPeriod:0-"24个月", 1-"36个月"
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static ObjectNode eliteBuyCar(Map<String, String> data) throws JsonProcessingException {
		// TODO Auto-generated method stub
		ObjectNode resultJson = Json.newObject();
		
		String mobile= data.get("mobile");
		String name= data.get("name");
		String carType= data.get("carType");
		String companyType= data.get("companyType");
		String payType= data.get("payType");
		String payPeriod= data.get("payPeriod");
		
		AppEliteBuyCar buyCar=new AppEliteBuyCar();
		buyCar.setCartype(carType);
		buyCar.setCompanytype(companyType);
		buyCar.setMobile(mobile);
		buyCar.setName(name);
		buyCar.setPayperiod(payPeriod);
		buyCar.setPaytype(payType);
		buyCar.save();
		resultJson.put("code", "0");
      	resultJson.put("msg", "添加成功！");
		return resultJson;
	}
	
	public static Result eliteBuyCarList() {
		
		ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	List<InputBtn> btn=eliteBuyCarCreateText(false);
    	//分页
    	//每页显示数据
		int pageSize = Integer.parseInt(Play.application().configuration().getString("pageSize"));
		
		//当前第N页
		int pageNow = 1;
		
		String pageNowIn = (String)in.get("pageNow");
		if(pageNowIn!=null){
		
			pageNow = Integer.parseInt(pageNowIn);
		}
		
    	List<AppEliteBuyCar> list = SimpleOrderApplication.eliteBuyCarList(pageSize,pageNow,btn);
    	
    	//总数
		int countAll = SimpleOrderApplication.eliteBuyCarCount(btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      	
		return ok(elitebuycar.render(list,countAll,pageNow,pageCount,pageSize,"elite",btn));
		
    }
    
	public static List<InputBtn> eliteBuyCarCreateText(boolean my){
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	Map<String, String> data=in.data();
    	List<InputBtn> l=new ArrayList<InputBtn>();
    	if(!my){
    		InputBtn bt1=new InputBtn("params","params","","客户名称","");
    		InputBtn bt2=new InputBtn("params0","params0","","手机号码","");
    		
    		List<OptionsBtn> opts=new ArrayList<OptionsBtn>();
    		opts.add(new OptionsBtn("请选择",""));
    		opts.add(new OptionsBtn("零首付","0"));
    		opts.add(new OptionsBtn("首付20%","1"));
    		opts.add(new OptionsBtn("首付30%","2"));
    		InputBtn bt3=new InputBtn("params1","params1","","付款方式","",InputBtn.CTYPE_SELECT,opts);
    		
    		List<OptionsBtn> opt=new ArrayList<OptionsBtn>();
    		opt.add(new OptionsBtn("请选择",""));
    		opt.add(new OptionsBtn("24个月","0"));
    		opt.add(new OptionsBtn("36个月","1"));
    		InputBtn bt4=new InputBtn("params2","params2","","分期","",InputBtn.CTYPE_SELECT,opt);
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
	
    public static List<AppEliteBuyCar> eliteBuyCarList(int pageSize,int pageNow,List<InputBtn>... btn){
		
		int start=(pageNow-1)*pageSize;
		
      	String paramsSql = "";
		
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (name like '%"+params+"%')";
      	}
      	String params0="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(1).getValue()!=null&&!btn[0].get(1).getValue().equals("")){
      		params=btn[0].get(1).getValue().trim();
      		paramsSql+=" and (mobile like '%"+params+"%')";
      	}

      	String params1="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(2).getValue()!=null&&!btn[0].get(2).getValue().equals("")){
      		params1=btn[0].get(2).getValue().trim();
      		paramsSql+=" and (paytype like '%"+params1+"%')";
      	}
      	
      	String params2="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(3).getValue()!=null&&!btn[0].get(3).getValue().equals("")){
      		params2=btn[0].get(3).getValue().trim();
      		paramsSql+=" and (payperiod like '%"+params2+"%')";
      	}
		//sql语句       
	 	String sql = "select id from cc_app_elite_buy_car where 1=1 "+paramsSql+" order by createtime desc limit "+start+" ,"+pageSize;   
	
  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")   
  		.create();
  		
  		Query<AppEliteBuyCar> eQ = Ebean.find(AppEliteBuyCar.class);
  		eQ.setRawSql(rawSql);
  		
  		List<AppEliteBuyCar> list = eQ.findList();

		return list;
    }
    
    public static int eliteBuyCarCount(List<InputBtn>... btn){
		
      	String paramsSql = "";

      	
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (name like '%"+params+"%')";
      	}
      	String params0="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(1).getValue()!=null&&!btn[0].get(1).getValue().equals("")){
      		params=btn[0].get(1).getValue().trim();
      		paramsSql+=" and (mobile like '%"+params+"%')";
      	}
      	
      	String params1="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(2).getValue()!=null&&!btn[0].get(2).getValue().equals("")){
      		params1=btn[0].get(2).getValue().trim();
      		paramsSql+=" and (paytype like '%"+params1+"%')";
      	}
      	
      	String params2="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(3).getValue()!=null&&!btn[0].get(3).getValue().equals("")){
      		params2=btn[0].get(3).getValue().trim();
      		paramsSql+=" and (payperiod like '%"+params2+"%')";
      	}
		//sql语句       
	 	String sql = "select count(id) as count from cc_app_elite_buy_car where 1=1 "+paramsSql;  
     	
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }
    
	public static Result parrallelBuyCarList() {
		
		ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	List<InputBtn> btn=parrallelBuyCarCreateText(false);
    	//分页
    	//每页显示数据
		int pageSize = Integer.parseInt(Play.application().configuration().getString("pageSize"));
		
		//当前第N页
		int pageNow = 1;
		
		String pageNowIn = (String)in.get("pageNow");
		if(pageNowIn!=null){
		
			pageNow = Integer.parseInt(pageNowIn);
		}
		
    	List<AppParrallelBuyCar> list = SimpleOrderApplication.parrallelBuyCarList(pageSize,pageNow,btn);
    	
    	//总数
		int countAll = SimpleOrderApplication.parrallelBuyCarCount(btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      	
		return ok(parrallelbuycar.render(list,countAll,pageNow,pageCount,pageSize,"parrallel",btn));
		
    }
    
	public static List<InputBtn> parrallelBuyCarCreateText(boolean my){
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	Map<String, String> data=in.data();
    	List<InputBtn> l=new ArrayList<InputBtn>();
    	if(!my){
    		InputBtn bt1=new InputBtn("params","params","","客户名称","");
    		InputBtn bt2=new InputBtn("params0","params0","","手机号码","");
    		
    		List<OptionsBtn> opts=new ArrayList<OptionsBtn>();
    		opts.add(new OptionsBtn("请选择",""));
    		opts.add(new OptionsBtn("是","是"));
    		opts.add(new OptionsBtn("否","否"));
    		InputBtn bt3=new InputBtn("params1","params1","","(是/否)贷款","",InputBtn.CTYPE_SELECT,opts);
    		
    		
        	if(data.containsKey(bt1.getId())){
        		bt1.setValue(data.get(bt1.getId()));
        	}
        	if(data.containsKey(bt2.getId())){
        		bt2.setValue(data.get(bt2.getId()));
        	}
        	if(data.containsKey(bt3.getId())){
        		bt3.setValue(data.get(bt3.getId()));
        	}

    		l.add(bt1);
    		l.add(bt2);
    		l.add(bt3);
    	}
		
		return l;
		
	}
	
    public static List<AppParrallelBuyCar> parrallelBuyCarList(int pageSize,int pageNow,List<InputBtn>... btn){
		
		int start=(pageNow-1)*pageSize;
		
      	String paramsSql = "";
		
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (name like '%"+params+"%')";
      	}
      	String params0="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(1).getValue()!=null&&!btn[0].get(1).getValue().equals("")){
      		params=btn[0].get(1).getValue().trim();
      		paramsSql+=" and (mobile like '%"+params+"%')";
      	}

      	String params1="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(2).getValue()!=null&&!btn[0].get(2).getValue().equals("")){
      		params1=btn[0].get(2).getValue().trim();
      		paramsSql+=" and (isloan like '%"+params1+"%')";
      	}
      	
		//sql语句       
	 	String sql = "select id from cc_app_parrallel_buy_car where 1=1 "+paramsSql+" order by createtime desc limit "+start+" ,"+pageSize;   
	
  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")   
  		.create();
  		
  		Query<AppParrallelBuyCar> eQ = Ebean.find(AppParrallelBuyCar.class);
  		eQ.setRawSql(rawSql);
  		
  		List<AppParrallelBuyCar> list = eQ.findList();

		return list;
    }
    
    public static int parrallelBuyCarCount(List<InputBtn>... btn){
		
      	String paramsSql = "";

      	
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (name like '%"+params+"%')";
      	}
      	String params0="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(1).getValue()!=null&&!btn[0].get(1).getValue().equals("")){
      		params=btn[0].get(1).getValue().trim();
      		paramsSql+=" and (mobile like '%"+params+"%')";
      	}
      	
      	String params1="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(2).getValue()!=null&&!btn[0].get(2).getValue().equals("")){
      		params1=btn[0].get(2).getValue().trim();
      		paramsSql+=" and (isloan like '%"+params1+"%')";
      	}
      	

		//sql语句       
	 	String sql = "select count(id) as count from cc_app_parrallel_buy_car where 1=1 "+paramsSql;  
     	
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }
}
