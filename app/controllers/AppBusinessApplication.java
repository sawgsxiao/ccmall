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

import common.UUIDGenerator;
import common.UtilsObject;

import java.util.ArrayList;
import java.util.List;
import models.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.lang.NullPointerException;
import java.io.ByteArrayOutputStream;
import javax.imageio.stream.FileImageInputStream;

import org.apache.http.HttpResponse;

import play.db.ebean.Model;

@Security.Authenticated(Secured.class)
public class AppBusinessApplication extends Controller {

    //车款业务
	public static Result appCarStyleBusinessList() {
		
		ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	List<InputBtn> btn=appCarStyleBusinessCreateText(false);
    	//分页
    	//每页显示数据
		int pageSize = Integer.parseInt(Play.application().configuration().getString("pageSize"));
		
		//当前第N页
		int pageNow = 1;
		
		String pageNowIn = (String)in.get("pageNow");
		if(pageNowIn!=null){
		
			pageNow = Integer.parseInt(pageNowIn);
		}
		
    	List<AppCarStyle> list = AppServiceApplication.appCarStyleBusinessList(pageSize,pageNow,btn);
    	
    	//总数
		int countAll = AppServiceApplication.appCarStyleBusinessCount(btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      	
		return ok(appcarstylebusinesslist.render(list,countAll,pageNow,pageCount,pageSize,"appCarStyleBusiness",btn));
		
    }
    
	public static List<InputBtn> appCarStyleBusinessCreateText(boolean my){
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	Map<String, String> data=in.data();
    	List<InputBtn> l=new ArrayList<InputBtn>();
    	if(!my){
    		InputBtn bt1=new InputBtn("params","params","","汽车车型","");
    		

    		InputBtn bt2=new InputBtn("params0","params0","","汽车类别","");
    		
    		List<OptionsBtn> opts=new ArrayList<OptionsBtn>();
    		
    		List<AppCarAssort> appCarAssorts= AppCarAssort.findAllList();
    		opts.add(new OptionsBtn("请选择",""));
    		for (AppCarAssort appCarAssort : appCarAssorts) {
    			opts.add(new OptionsBtn(appCarAssort.getAssortname(),appCarAssort.getAssortcode()));
			}
    		
    		InputBtn bt3=new InputBtn("params1","params1","","品牌","",InputBtn.CTYPE_SELECT,opts);
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
	
    public static List<AppCarStyle> appCarStyleBusinessList(int pageSize,int pageNow,List<InputBtn>... btn){
		
		int start=(pageNow-1)*pageSize;
		
      	String paramsSql = "";
		
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (car.carname like '%"+params+"%')";
      	}
      	
      	String params0="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(1).getValue()!=null&&!btn[0].get(1).getValue().equals("")){
      		params0=btn[0].get(1).getValue().trim();
      		paramsSql+=" and (car.productcode like '%"+params0+"%')";
      	}
      	String params1="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(2).getValue()!=null&&!btn[0].get(2).getValue().equals("")){
      		params1=btn[0].get(2).getValue().trim();
      		paramsSql+=" and (car.code ='"+params1+"')";
      	}
      	
		//sql语句       
	 	String sql = "select hot.id as id from cc_app_car_style hot,cc_app_car car where  1=1 and car.id=hot.carid "+paramsSql+" order by car.carname, hot.createtime desc limit "+start+" ,"+pageSize;   
	
  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")   
  		.create();
  		
  		Query<AppCarStyle> eQ = Ebean.find(AppCarStyle.class);
  		eQ.setRawSql(rawSql);
  		
  		List<AppCarStyle> list = eQ.findList();

		return list;
    }
    
    public static int appCarStyleBusinessCount(List<InputBtn>... btn){
		
      	String paramsSql = "";

      	
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (car.carname like '%"+params+"%')";
      	}
      	
      	String params0="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(1).getValue()!=null&&!btn[0].get(1).getValue().equals("")){
      		params0=btn[0].get(1).getValue().trim();
      		paramsSql+=" and (car.productcode like '%"+params0+"%')";
      	}
      	
      	String params1="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(2).getValue()!=null&&!btn[0].get(2).getValue().equals("")){
      		params1=btn[0].get(2).getValue().trim();
      		paramsSql+=" and (car.code ='"+params1+"')";
      	}
		//sql语句       
	 	String sql = "select count(hot.id) as count  from cc_app_car_style hot,cc_app_car car where  1=1 and car.id=hot.carid "+paramsSql;  
     	
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }

    public static Result appCarStyleBusinessSet(int id) {
		
		ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	AppCarStyle carStyle= AppCarStyle.findById(id);
      	
		return ok(appcarstylebusinessset.render(carStyle));
		
    }
    
    public static Result appCarStyleBusinessSetDo() {
		
		ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	String id=in.get("id");
    	String isloan=in.get("isloan");
    	String isefficient=in.get("isefficient");
    	String iselite=in.get("iselite");
    	String isflash=in.get("isflash");
    	String starttime=in.get("starttime");
    	String endtime=in.get("endtime");
    	String flashamount=in.get("flashamount");
    	String operate=in.get("operate");
    	String sale=in.get("sale");
    	
    	AppCarStyle carStyle= AppCarStyle.findById(Integer.parseInt(id));
    	if(isflash.equals("1")){
    		if(Integer.parseInt(operate)>0){
    			MultipartFormData body = request().body().asMultipartFormData();
        		List<FilePart> fileParts= body.getFiles();
        		String uploadimage=Play.application().configuration().getString("imageserver");
        		FilePart f= fileParts.get(0);
        		String dir=uploadimage+"/"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"/";
        		try {
        			File fdir=new File(dir);
        			if(!fdir.exists()){
        				fdir.mkdirs();
        			}
    				File getFile=f.getFile().getCanonicalFile();
    				File savefile=new File(dir, UUIDGenerator.getUUID()+f.getFilename().substring(f.getFilename().indexOf(".")));
    				FileOutputStream os=new FileOutputStream(savefile);
    				FileInputStream is=new FileInputStream(getFile);
    				byte[] b=new byte[1024];
    				int len;
    				while((len=is.read(b))>0){
    					os.write(b);
    				}
    				os.write(b);
    				os.flush();
    				os.close();
    				is.close();
    				carStyle.setFlashimg(savefile.getPath().replace(uploadimage, ""));
        		} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}
				carStyle.setEndtime(starttime);
				carStyle.setEndtime(endtime);
				carStyle.setFlashamount(flashamount);
			
    	}
    	
    	carStyle.setIsefficient(isefficient);
    	carStyle.setIsflash(isflash);
    	carStyle.setIselite(iselite);
    	carStyle.setSale(sale);
    	carStyle.save();
		return appCarStyleBusinessList();
		
    }
}
