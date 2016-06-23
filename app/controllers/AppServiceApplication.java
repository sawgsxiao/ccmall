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
public class AppServiceApplication extends Controller {
//品牌	
	public static Result appCarAssortAdd(){
		/*System.out.println(dataFormat());
		JSONArray jsonArray= dataFormat();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject= (JSONObject) jsonArray.get(i);
			AppCarAssort assort=new AppCarAssort();
			String url=jsonObject.getString("url");
			String name=jsonObject.getString("name");
			name=name.substring(2, name.length());
			assort.setLogo(url.substring(url.lastIndexOf("/")+1,url.length()));
			assort.setAssortname(name);
			String title=(UtilsObject.getPYIndexStr(name.substring(0,1).toUpperCase(), UtilsObject.checkStrChinese(name))).substring(0, 1);
			String code="";
			code=searchNextCode(title);
			code=title+code;
			assort.setTitle(title);
			assort.setAssortcode(code);
			String uuid=UUIDGenerator.getUUID();
			assort.setUuid(uuid);
			assort.setCreatetime(new Date());
			assort.save();
		}*/
		return ok(appcarassortadd.render(""));
	}
	
	public static Result appCarAssortAddDo(){
		DynamicForm in= Form.form().bindFromRequest();
		String result="";
		String name=in.data().get("name");
		
		try {
		MultipartFormData body = request().body().asMultipartFormData();
		List<FilePart> fileParts= body.getFiles();
//		File dir=new File("../webapps/images");
		String uploadimage=Play.application().configuration().getString("imageserver");
//		File dir=new File("../apache-tomcat-8.0.30/webapps/images");
		File dir=new File(uploadimage);
		
		if(!dir.exists()){
			dir.mkdir();
		}
		
		FileOutputStream os;
		FileInputStream is;
		byte[] b=new byte[1024];
		File file = null;
		File upload;
		long now=new Date().getTime();
		
			if(fileParts.size()>0){
				for (FilePart filePart : fileParts) {
						String filename=filePart.getFilename();
						file=new File(dir,now+filename);
						if(!file.exists()){
							file.createNewFile();
						}
						upload= filePart.getFile().getCanonicalFile();
						is=new FileInputStream(upload);
							os=new FileOutputStream(file);
							while(is.read(b)>0){
								os.write(b);
							}
							os.write(b);
							is.close();
							os.close();
					}
				}
			AppCarAssort assort=new AppCarAssort();
			assort.setLogo(file.getAbsoluteFile().getName());
			assort.setAssortname(name);
			String title=(UtilsObject.getPYIndexStr(in.get("name").substring(0,1).toUpperCase(), UtilsObject.checkStrChinese(in.get("name")))).substring(0, 1);
			String code="";
			code=searchNextCode(title);
			code=title+code;
			assort.setTitle(title);
			assort.setAssortcode(code);
			String uuid=UUIDGenerator.getUUID();
			assort.setUuid(uuid);
			assort.setCreatetime(new Date());
			
			assort.save();
			result="添加成功!";
			} catch (Exception e) {
				e.printStackTrace();
				result="添加失败!";
			}
		
		return ok(appcarassortadd.render(result));
		
	}
	
    public static String searchNextCode(String title){
		//sql语句       
	 	String sql = "select max(assortcode) as code from cc_app_car_assort where 1=1  and title='"+title+"'";  
     	
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
	
	public static Result appCarAssortList() {
		
		ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	List<InputBtn> btn=carAssortCreateText(false);
    	//分页
    	//每页显示数据
		int pageSize = Integer.parseInt(Play.application().configuration().getString("pageSize"));
		
		//当前第N页
		int pageNow = 1;
		
		String pageNowIn = (String)in.get("pageNow");
		if(pageNowIn!=null){
		
			pageNow = Integer.parseInt(pageNowIn);
		}
		
    	List<AppCarAssort> list = AppServiceApplication.carAssortList(pageSize,pageNow,btn);
    	
    	//总数
		int countAll = AppServiceApplication.carAssortCount(btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      	
		return ok(appcarassort.render(list,countAll,pageNow,pageCount,pageSize,"appCarAssort",btn));
		
    }
    
	public static List<InputBtn> carAssortCreateText(boolean my){
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	Map<String, String> data=in.data();
    	List<InputBtn> l=new ArrayList<InputBtn>();
    	if(!my){
    		InputBtn bt1=new InputBtn("params","params","","品牌名称","");
    		

        	if(data.containsKey(bt1.getId())){
        		bt1.setValue(data.get(bt1.getId()));
        	}

    		l.add(bt1);
    	}
		
		return l;
		
	}
	
    public static List<AppCarAssort> carAssortList(int pageSize,int pageNow,List<InputBtn>... btn){
		
		int start=(pageNow-1)*pageSize;
		
      	String paramsSql = "";
		
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (assortname like '%"+params+"%')";
      	}
      	

		//sql语句       
	 	String sql = "select id from cc_app_car_assort where 1=1 and stand='1' "+paramsSql+" order by assortcode limit "+start+" ,"+pageSize;   
	
  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")   
  		.create();
  		
  		Query<AppCarAssort> eQ = Ebean.find(AppCarAssort.class);
  		eQ.setRawSql(rawSql);
  		
  		List<AppCarAssort> list = eQ.findList();

		return list;
    }
    
    public static int carAssortCount(List<InputBtn>... btn){
		
      	String paramsSql = "";

      	
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (assortname like '%"+params+"%')";
      	}
      	
		//sql语句       
	 	String sql = "select count(id) as count from cc_app_car_assort where  1=1 and stand='1' "+paramsSql;  
     	
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }
    
    public static Result appCarAssortDel(int id){
    	AppCarAssort.findById(id).delete();
    	return appCarAssortList();
    }
    
    public static Result appCarAssortEdit(int id){
    	AppCarAssort assort= AppCarAssort.findById(id);;
    	return ok(appcarassortedit.render(assort,""));
    }
    
    
    public static Result appCarAssortEditDo(){
		DynamicForm in= Form.form().bindFromRequest();
		String result="";
		String id=in.data().get("id");
		String name=in.data().get("name");
		String remark=in.data().get("remark");
		try {
		MultipartFormData body = request().body().asMultipartFormData();
		List<FilePart> fileParts= body.getFiles();
//		File dir=new File("../webapps/images");
		String uploadimage=Play.application().configuration().getString("imageserver");
//		File dir=new File("../apache-tomcat-8.0.30/webapps/images");
		File dir=new File(uploadimage);
		
		if(!dir.exists()){
			dir.mkdir();
		}
		
		FileOutputStream os;
		FileInputStream is;
		byte[] b=new byte[1024];
		File file = null;
		File upload;
		long now=new Date().getTime();
		
			if(fileParts.size()>0){
				for (FilePart filePart : fileParts) {
						String filename=filePart.getFilename();
						file=new File(dir,now+filename);
						if(!file.exists()){
							file.createNewFile();
						}
						upload= filePart.getFile().getCanonicalFile();
						is=new FileInputStream(upload);
							os=new FileOutputStream(file);
							while(is.read(b)>0){
								os.write(b);
							}
							os.write(b);
							is.close();
							os.close();
					}
				}
			AppCarAssort assort=AppCarAssort.findById(Integer.parseInt(id));
			if(file==null){
				assort.setLogo(in.get("logo"));
			}else{
				assort.setLogo(file.getAbsoluteFile().getName());
			}
			assort.setAssortname(name);
			String title=(UtilsObject.getPYIndexStr(in.get("name").substring(0,1).toUpperCase(), UtilsObject.checkStrChinese(in.get("name")))).substring(0, 1);
			String code="";
			code=searchNextCode(title);
			code=title+code;
//			assort.setTitle(title);
//			assort.setAssortcode(code);
			String uuid=UUIDGenerator.getUUID();
//			assort.setUuid(uuid);
			assort.setCreatetime(new Date());
			assort.update();
			result="编辑成功!";
			} catch (Exception e) {
				e.printStackTrace();
				result="编辑失败!";
			}
		
		return appCarAssortList();
		
	}
    
    public static Result appCarAssortDetailed(int id){
    	AppCarAssort assort=AppCarAssort.findById(id);
    	return ok(appcarassortdetail.render(assort,""));
    }
    
    
    public static JSONArray dataFormat(){

    	File dir=new File("./");
    	
    	File jfile=new File(dir,"data.json");
    	
		JSONArray jsonArray=null;
		
		BufferedReader reader;
		try {
			FileInputStream fis=new FileInputStream(jfile);
			
			reader = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
		
			String json="";
			
			String line="";
			
			while((line=reader.readLine())!=null){
				json+=line;
			}		
			jsonArray=JSONArray.fromObject(json);
			
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject= (JSONObject) jsonArray.get(i);
			}
		
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return jsonArray;
	}
    
 //车型   
    public static Result appCarAdd() {
		
		List<AppCarAssort> assorts=AppCarAssort.findAllList();
		
		return ok(appcaradd.render(assorts));
		
	}
    
    
	public static Result appCarAddDo(){
		DynamicForm in = Form.form().bindFromRequest();
		String carname=in.get("carname");
		String carassort=in.get("carassort");
		String productcode=in.get("productcode");
		String pricerange=in.get("pricerange");
		String remark=in.get("remark");
		String colorlist=in.get("colorlist");
		String size=in.get("size");
		
		AppCar car=new AppCar();
		car.setAssortcode(carassort);
		car.setAssortname(AppCarAssort.findById(Integer.parseInt(carassort)).getAssortname());
		car.setCarname(carname);
		car.setCreatetime(new Date());
		car.setProductcode(productcode);
		car.setRemark(remark);
		car.setPricerange(pricerange);
		car.setColorlist(colorlist);
		car.setSize(size);
		car.setCode(AppCarAssort.findById(Integer.parseInt(carassort)).getAssortcode());
		car.setUuid(UUIDGenerator.getUUID());
		List<AppCarImage> carImages=new ArrayList<AppCarImage>();
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
			 AppCarImage image=new AppCarImage();
			 image.setCreatetime(new Date());
			 image.setUrl(in.get(array[i]+""));
			 carImages.add(image);
		}
		car.setImages(carImages);
		
		Pattern nameReg=Pattern.compile("^stylename\\d+$"); 
		
		Pattern priceReg=Pattern.compile("^styleprice\\d+$"); 
		
		List<String> stylenames=new ArrayList<String>();
		
		List<String> styleprices=new ArrayList<String>();
		
		List<AppCarStyle> carStyles=new ArrayList<AppCarStyle>();
		
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			if(nameReg.matcher(string).find()){
				stylenames.add(string);
			}else if(priceReg.matcher(string).find()){
				styleprices.add(string);
			}
		}
		Object[] stylens=stylenames.toArray();
		Arrays.sort(stylens);
		Object[] styleps=styleprices.toArray();
		Arrays.sort(styleps);
		for (int i = 0; i < stylens.length; i++) {
			AppCarStyle carStyle=new AppCarStyle();
			carStyle.setCreatetime(new Date());
			carStyle.setPrice(in.get(styleps[i]+""));
			carStyle.setStand("1");
			carStyle.setStylename(in.get(stylens[i]+""));
			carStyle.setUuid(UUIDGenerator.getUUID());
			carStyle.setHot("0");
			carStyles.add(carStyle);
		}
		car.setCarStyles(carStyles);
		
		car.save();
		
		return ok(appcaradd.render(AppCarAssort.findAllList()));
	}
	
	
	public static Result appCarList() {
		
		ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	List<InputBtn> btn=appCarCreateText(false);
    	//分页
    	//每页显示数据
		int pageSize = Integer.parseInt(Play.application().configuration().getString("pageSize"));
		
		//当前第N页
		int pageNow = 1;
		
		String pageNowIn = (String)in.get("pageNow");
		if(pageNowIn!=null){
		
			pageNow = Integer.parseInt(pageNowIn);
		}
		
    	List<AppCar> list = AppServiceApplication.appCarList(pageSize,pageNow,btn);
    	
    	//总数
		int countAll = AppServiceApplication.appCarCount(btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      	
		return ok(appcar.render(list,countAll,pageNow,pageCount,pageSize,"appCar",btn));
		
    }
    
	public static List<InputBtn> appCarCreateText(boolean my){
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
	
    public static List<AppCar> appCarList(int pageSize,int pageNow,List<InputBtn>... btn){
		
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
      	String params1="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(2).getValue()!=null&&!btn[0].get(2).getValue().equals("")){
      		params1=btn[0].get(2).getValue().trim();
      		paramsSql+=" and (code ='"+params1+"')";
      	}
		//sql语句       
	 	String sql = "select id from cc_app_car where 1=1 "+paramsSql+" order by createtime desc limit "+start+" ,"+pageSize;   
	
  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")   
  		.create();
  		
  		Query<AppCar> eQ = Ebean.find(AppCar.class);
  		eQ.setRawSql(rawSql);
  		
  		List<AppCar> list = eQ.findList();

		return list;
    }
    
    public static int appCarCount(List<InputBtn>... btn){
		
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
      	String params1="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(2).getValue()!=null&&!btn[0].get(2).getValue().equals("")){
      		params1=btn[0].get(2).getValue().trim();
      		paramsSql+=" and (code ='"+params1+"')";
      	}
		//sql语句       
	 	String sql = "select count(id) as count from cc_app_car where 1=1 "+paramsSql;  
     	
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }

    public static Result appCarDetailed(int id) {
		
		List<AppCarAssort> assorts=AppCarAssort.findAllList();
		
		AppCar car= AppCar.findById(id);
		
		return ok(appcardetail.render(assorts,car));
		
	}

    public static Result appCarEdit(int id) {
		
		List<AppCarAssort> assorts=AppCarAssort.findAllList();
		
		AppCar car= AppCar.findById(id);
		
		return ok(appcaredit.render(assorts,car));
		
	}
    
    public static Result appCarTried(int id) {
		
    	List<AppCarAssort> assorts=AppCarAssort.findAllList();
		
		AppCar car= AppCar.findById(id);
		
		return ok(appcartried.render(assorts,car));
		
	}
    
    public static Result appCarEditDo() {
		
    	DynamicForm in = Form.form().bindFromRequest();
    	String id=in.get("id");
		String carname=in.get("carname");
		String carassort=in.get("carassort");
		String productcode=in.get("productcode");
		String pricerange=in.get("pricerange");
		String remark=in.get("remark");
		String colorlist=in.get("colorlist");
		String size=in.get("size");
		
		
		AppCar car=AppCar.findById(Integer.parseInt(id));
		car.setAssortcode(carassort);
		car.setAssortname(AppCarAssort.findById(Integer.parseInt(carassort)).getAssortname());
		car.setCarname(carname);
		car.setCreatetime(new Date());
		car.setProductcode(productcode);
		car.setRemark(remark);
		car.setPricerange(pricerange);
		car.setColorlist(colorlist);
		car.setSize(size);
		car.setCode(AppCarAssort.findById(Integer.parseInt(carassort)).getAssortcode());
		
		List<AppCarImage> carImages=new ArrayList<AppCarImage>();
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
				 AppCarImage image=new AppCarImage();
				 image.setCreatetime(new Date());
				 image.setUrl(in.get(array[i]+""));
				 carImages.add(image);
			}
			car.setImages(carImages);
		}
		
		car.update();
		
		return appCarList();
		
	}    

    public static Result appCarStand(int id) {
		AppCar car= AppCar.findById(id);
		car.setStand(1);
		car.update();
		
		return appCarList();
		
	}
    
    public static Result appCarStandCh(int id) {
    	AppCar car= AppCar.findById(id);
		car.setStand(0);
		car.update();
		
		return appCarList();
		
	}
    
    
    public static Result appCarDel(int id) {
				
    	AppCar car= AppCar.findById(id);
    	
    	car.delete();
		return appCarList();
		
	}
    
    
    
 //热门   
    public static Result appCarStyleHot(int id){
    	AppCarStyle car= AppCarStyle.findById(id);
    	if(car.getHot().equals("0")){
//    		car.setHot("1");
    		/*AppHotCar hotCar=new AppHotCar();
    		hotCar.setUuid(UUIDGenerator.getUUID());
    		hotCar.setCreatetime(new Date());
//    		hotCar.setThedesc("");
//    		hotCar.setFirstprice("");
    		car.setHotCar(hotCar);
    		car.update();*/
    		AppHotCar hotCar=   new AppHotCar();
    		List<AppCarDiscount> discounts= AppCarDiscount.findAllList();
    		return ok(apphotcaradd.render(car,discounts));
    	}else{
    		car.setHot("0");
    		car.update();
    		car.getHotCar().delete();
    	}
    	return appCarStyleList();
    }
    
    public static Result appHotCarAddDo() {
    	//参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	String id=in.get("id");
    	
    	String thedesc=in.get("thedesc");
    	
    	String firstprice=in.get("firstprice");
    	
    	String discount=in.get("cardiscount");
    	
    	AppCarStyle car= AppCarStyle.findById(Integer.parseInt(id));
    	
    	car.setHot("1");
		AppHotCar hotCar=new AppHotCar();
		hotCar.setUuid(UUIDGenerator.getUUID());
		hotCar.setCreatetime(new Date());
    	
    	AppCarDiscount carDiscount= AppCarDiscount.findById(Integer.parseInt(discount));
    			
		hotCar.setFirstprice(firstprice);
		
		hotCar.setDiscount(carDiscount.getDiscount());
		
		hotCar.setDisdesc(carDiscount.getThedesc());
		
		hotCar.setThedesc(thedesc);		
		
		car.setHotCar(hotCar);
		
		car.update();
		
		return appHotCarList();
		
	} 
    
    
	public static Result appHotCarList() {
		
		ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	List<InputBtn> btn=appHotCarCreateText(false);
    	//分页
    	//每页显示数据
		int pageSize = Integer.parseInt(Play.application().configuration().getString("pageSize"));
		
		//当前第N页
		int pageNow = 1;
		
		String pageNowIn = (String)in.get("pageNow");
		if(pageNowIn!=null){
		
			pageNow = Integer.parseInt(pageNowIn);
		}
		
    	List<AppHotCar> list = AppServiceApplication.appHotCarList(pageSize,pageNow,btn);
    	
    	//总数
		int countAll = AppServiceApplication.appHotCarCount(btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      	
		return ok(apphotcar.render(list,countAll,pageNow,pageCount,pageSize,"appHotCar",btn));
		
    }
    
	public static List<InputBtn> appHotCarCreateText(boolean my){
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
	
    public static List<AppHotCar> appHotCarList(int pageSize,int pageNow,List<InputBtn>... btn){
		
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
      	String params1="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(2).getValue()!=null&&!btn[0].get(2).getValue().equals("")){
      		params1=btn[0].get(2).getValue().trim();
      		paramsSql+=" and (code ='"+params1+"')";
      	}
      	
		//sql语句       
	 	String sql = "select hot.id as id from cc_app_hot_car hot,cc_app_car_style car,cc_app_car acar where  1=1 and car.hotcarid=hot.id and acar.id=car.carid "+paramsSql+" order by hot.createtime desc limit "+start+" ,"+pageSize;   
	
  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")   
  		.create();
  		
  		Query<AppHotCar> eQ = Ebean.find(AppHotCar.class);
  		eQ.setRawSql(rawSql);
  		
  		List<AppHotCar> list = eQ.findList();

		return list;
    }
    
    public static int appHotCarCount(List<InputBtn>... btn){
		
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
      	
      	String params1="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(2).getValue()!=null&&!btn[0].get(2).getValue().equals("")){
      		params1=btn[0].get(2).getValue().trim();
      		paramsSql+=" and (code ='"+params1+"')";
      	}
		//sql语句       
	 	String sql = "select count(hot.id) as count  from cc_app_hot_car hot,cc_app_car_style car,cc_app_car acar  where  1=1 and car.hotcarid=hot.id and acar.id=car.carid "+paramsSql;  
     	
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }
    
    public static Result appHotCarEdit(int id) {
		
		
		AppHotCar hotCar=   AppHotCar.findById(id);
		
		List<AppCarDiscount> discounts= AppCarDiscount.findAllList();
		
		return ok(apphotcaredit.render(hotCar,discounts));
		
	}
    
    public static Result appHotCarEditDo() {
    	//参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	String id=in.get("id");
    	
    	String thedesc=in.get("thedesc");
    	
    	String firstprice=in.get("firstprice");
    	
    	String discount=in.get("cardiscount");
    	
    	AppCarDiscount carDiscount= AppCarDiscount.findById(Integer.parseInt(discount));
    	
		AppHotCar hotCar=   AppHotCar.findById(Integer.parseInt(id));
		
		hotCar.setFirstprice(firstprice);
		
		hotCar.setDiscount(carDiscount.getDiscount());
		
		hotCar.setDisdesc(carDiscount.getThedesc());
		
		hotCar.setThedesc(thedesc);
		
		hotCar.update();
		
		return appHotCarList();
		
	}
    
    
    public static Result appHotCarDetailed(int id) {
		
		List<AppCarAssort> assorts=AppCarAssort.findAllList();
		
		AppHotCar hotCar=   AppHotCar.findById(id);
		
		return ok(apphotcardetail.render(hotCar,assorts));
		
	}
    
    public static Result appHotCarStand(int id) {
		AppHotCar hotCar=AppHotCar.findById(id);
		AppCar car= hotCar.getAppCarStyle().getAppCar();
		car.setStand(1);
		car.update();
		
		return appHotCarList();
		
	}
    
    public static Result appHotCarStandCh(int id) {
    	AppHotCar hotCar=AppHotCar.findById(id);
    	AppCar car= hotCar.getAppCarStyle().getAppCar();
		car.setStand(0);
		car.update();
		
		return appHotCarList();
		
	}
    
    
    public static Result appHotCarDel(int id) {
				
		AppHotCar hotCar=   AppHotCar.findById(id);
		hotCar.getAppCarStyle().setHot("0");
		hotCar.getAppCarStyle().update();
		hotCar.delete();
		return appHotCarList();
		
	}
    

  //平行  
    public static Result appCarStyleParrallel(int id){
    	AppCarStyle car= AppCarStyle.findById(id);
    	if(car.getParrallel()==null||car.getParrallel().equals("0")){
    		car.setParrallel("1");
    		AppParrallelCar parrallelCar=new AppParrallelCar();
    		parrallelCar.setUuid(UUIDGenerator.getUUID());
    		parrallelCar.setCreatetime(new Date());
//    		hotCar.setThedesc("");
//    		hotCar.setFirstprice("");
    		car.setParrallelCar(parrallelCar);
    		car.update();
    		return ok(appparrallelcaredit.render(AppParrallelCar.findById(parrallelCar.getId()),AppCarSpec.findAllList(),AppCarStock.findAllList()));
    	}else{
    		car.setParrallel("0");
    		car.update();
    		car.getParrallelCar().delete();
    		return appCarStyleList();
    	}
    	
    }
    
    public static Result appParrallelCarEdit(int id){
    	
    	return ok(appparrallelcaredit.render(AppParrallelCar.findById(id),AppCarSpec.findAllList(),AppCarStock.findAllList()));

    }
    
    public static Result appParrallelCarEditDo() {
    	//参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	String id=in.get("id");
    	
    	String thedesc=in.get("thedesc");
    	
    	String spec=in.get("spec");
    	
    	String stock=in.get("stock");
    	
    	AppCarSpec specval= AppCarSpec.findById(Integer.parseInt(spec));
    	
    	AppCarStock stockval= AppCarStock.findById(Integer.parseInt(stock));
    	
		AppParrallelCar car=   AppParrallelCar.findById(Integer.parseInt(id));
		
		car.setSpec(specval.getSpec());
		
		car.setStock(stockval.getStock());
		
		car.setThedesc(thedesc);
		
		car.update();
		
		return appParrallelCarList();
		
	}
    
    public static Result appParrallelCarDetailed(int id){
    	
    	return ok(appparrallelcardetail.render(AppParrallelCar.findById(id)));

    }
    
    public static Result appParrallelCarDel(int id) {
		
    	AppParrallelCar PCar=   AppParrallelCar.findById(id);
    	PCar.getAppCarStyle().setParrallel("0");
    	PCar.getAppCarStyle().update();
    	PCar.delete();
		return appParrallelCarList();
		
	}
    
	public static Result appParrallelCarList() {
		
		ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	List<InputBtn> btn=appParrallelCarCreateText(false);
    	//分页
    	//每页显示数据
		int pageSize = Integer.parseInt(Play.application().configuration().getString("pageSize"));
		
		//当前第N页
		int pageNow = 1;
		
		String pageNowIn = (String)in.get("pageNow");
		if(pageNowIn!=null){
		
			pageNow = Integer.parseInt(pageNowIn);
		}
		
    	List<AppParrallelCar> list = AppServiceApplication.appParrallelCarList(pageSize,pageNow,btn);
    	
    	//总数
		int countAll = AppServiceApplication.appParrallelCarCount(btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      	
		return ok(appparrallelcar.render(list,countAll,pageNow,pageCount,pageSize,"appParrallelCar",btn));
		
    }
    
	public static List<InputBtn> appParrallelCarCreateText(boolean my){
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
	
    public static List<AppParrallelCar> appParrallelCarList(int pageSize,int pageNow,List<InputBtn>... btn){
		
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
      	String params1="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(2).getValue()!=null&&!btn[0].get(2).getValue().equals("")){
      		params1=btn[0].get(2).getValue().trim();
      		paramsSql+=" and (code ='"+params1+"')";
      	}
      	
		//sql语句       
	 	String sql = "select hot.id as id from cc_app_parrallel_car hot,cc_app_car_style car,cc_app_car acar where  1=1 and car.parrallelid=hot.id and acar.id=car.carid "+paramsSql+" order by hot.createtime desc limit "+start+" ,"+pageSize;   
	
  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")   
  		.create();
  		
  		Query<AppParrallelCar> eQ = Ebean.find(AppParrallelCar.class);
  		eQ.setRawSql(rawSql);
  		
  		List<AppParrallelCar> list = eQ.findList();

		return list;
    }
    
    public static int appParrallelCarCount(List<InputBtn>... btn){
		
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
      	
      	String params1="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(2).getValue()!=null&&!btn[0].get(2).getValue().equals("")){
      		params1=btn[0].get(2).getValue().trim();
      		paramsSql+=" and (code ='"+params1+"')";
      	}
		//sql语句       
	 	String sql = "select count(hot.id) as count  from cc_app_parrallel_car hot,cc_app_car_style car,cc_app_car acar where  1=1 and car.parrallelid=hot.id and acar.id=car.carid "+paramsSql;  
     	
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }
//车款
    
	public static Result appCarStyleList() {
		
		ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	List<InputBtn> btn=appCarCreateText(false);
    	//分页
    	//每页显示数据
		int pageSize = Integer.parseInt(Play.application().configuration().getString("pageSize"));
		
		//当前第N页
		int pageNow = 1;
		
		String pageNowIn = (String)in.get("pageNow");
		if(pageNowIn!=null){
		
			pageNow = Integer.parseInt(pageNowIn);
		}
		
    	List<AppCarStyle> list = AppServiceApplication.appCarStyleList(pageSize,pageNow,btn);
    	
    	//总数
		int countAll = AppServiceApplication.appCarStyleCount(btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      	
		return ok(appcarstyle.render(list,countAll,pageNow,pageCount,pageSize,"appCarStyle",btn));
		
    }
    
	public static List<InputBtn> appCarStyleCreateText(boolean my){
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
	
    public static List<AppCarStyle> appCarStyleList(int pageSize,int pageNow,List<InputBtn>... btn){
		
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
    
    public static int appCarStyleCount(List<InputBtn>... btn){
		
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
 
	public static Result appCarStyleAdd(int carid){
		return ok(appcarstyleadd.render(carid+""));
	}
    
	public static Result appCarStyleAddDo(){
		String result="";
		try{
			DynamicForm in = Form.form().bindFromRequest();
			String carid=in.get("carid");
			String stylename=in.get("stylename");
			String price=in.get("price");
			String remark=in.get("remark");
	
			List<AppCarStyle> appCarStyles=new ArrayList<AppCarStyle>();
			AppCarStyle carStyle=new AppCarStyle();
			carStyle.setPrice(price);
			carStyle.setStylename(stylename);
			carStyle.setCreatetime(new Date());
			carStyle.setUuid(UUIDGenerator.getUUID());
			carStyle.setHot("0");
			List<AppCarStyleImage> images=new ArrayList<AppCarStyleImage>();
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
				 AppCarStyleImage image=new AppCarStyleImage();
				 image.setCreatetime(new Date());
				 image.setUrl(in.get(array[i]+""));
				 images.add(image);
			}
			carStyle.setImages(images);
			AppCar car= AppCar.findById(Integer.parseInt(carid));
			appCarStyles.add(carStyle);
			car.setCarStyles(appCarStyles);
			if(AppCarStyle.findByStyleName(stylename,carid)==null){
				car.update();
			}
			
			result="success";
		}catch (Exception e) {
			// TODO: handle exception
			result="fail";
		}
		System.out.println(result);
		return appCarList();
	}
	
	public static Result appCarStyleEdit(int id){
		AppCarStyle carStyle= AppCarStyle.findById(id);
		
		return ok(appcarstyleedit.render(carStyle));
	}
	
	public static Result appCarStyleEditDo(){
		String result="";
		try{
			DynamicForm in = Form.form().bindFromRequest();
			String styleid=in.get("styleid");
			String stylename=in.get("stylename");
			String price=in.get("price");
			String remark=in.get("remark");
	
			AppCarStyle carStyle=AppCarStyle.findById(Integer.parseInt(styleid));
			carStyle.setPrice(price);
			carStyle.setStylename(stylename);
			List<AppCarStyleImage> images=new ArrayList<AppCarStyleImage>();
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
				 AppCarStyleImage image=new AppCarStyleImage();
				 image.setCreatetime(new Date());
				 image.setUrl(in.get(array[i]+""));
				 images.add(image);
			}
			carStyle.setImages(images);
			carStyle.update();
			result="success";
		}catch (Exception e) {
			// TODO: handle exception
			result="fail";
		}
		System.out.println(result);
		return appCarStyleList();
	}
	
	
    public static Result appCarStyleDel(int id) {
		
    	AppCarStyle car= AppCarStyle.findById(id);
    	AppHotCar hotCar=null;
    	if(car.getHotCar()!=null){
    		hotCar=AppHotCar.findById(car.getHotCar().getId());
    	}
    	
    	if(hotCar==null){
    		car.setHotCar(new AppHotCar());
    		car.update();
    	}
    	AppCarStyle delCar= AppCarStyle.findById(id);
    	delCar.delete();
		return appCarStyleList();
		
	}
	
    
    public static Result appCarStyleDetailed(int carid){
    	AppCar appCar= AppCar.findById(carid);
		return ok(appcarstyledetail.render(appCar));
	}
    
    //车款业务
	public static Result appCarStyleBusinessList() {
		
		ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	List<InputBtn> btn=appCarCreateText(false);
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
      	
		return ok(appcarstylebusiness.render(list,countAll,pageNow,pageCount,pageSize,"appCarStyleBusiness",btn));
		
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
    
    //新能源
    
    public static Result appYueanCarList() {
		
		ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	List<InputBtn> btn=appYueanCarCreateText(false);
    	//分页
    	//每页显示数据
		int pageSize = Integer.parseInt(Play.application().configuration().getString("pageSize"));
		
		//当前第N页
		int pageNow = 1;
		
		String pageNowIn = (String)in.get("pageNow");
		if(pageNowIn!=null){
		
			pageNow = Integer.parseInt(pageNowIn);
		}
		
    	List<AppYueanCar> list = AppServiceApplication.appYueanCarList(pageSize,pageNow,btn);
    	
    	//总数
		int countAll = AppServiceApplication.appYueanCarCount(btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      	
		return ok(appyueancar.render(list,countAll,pageNow,pageCount,pageSize,"appYueanCar",btn));
		
    }
    
	public static List<InputBtn> appYueanCarCreateText(boolean my){
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
	
    public static List<AppYueanCar> appYueanCarList(int pageSize,int pageNow,List<InputBtn>... btn){
		
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
      	String params1="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(2).getValue()!=null&&!btn[0].get(2).getValue().equals("")){
      		params1=btn[0].get(2).getValue().trim();
      		paramsSql+=" and (code ='"+params1+"')";
      	}
      	
		//sql语句       
	 	String sql = "select hot.id as id from cc_app_yuean_car hot,cc_app_car_style car,cc_app_car acar where  1=1 and car.yueanid=hot.id and acar.id=car.carid "+paramsSql+" order by hot.createtime desc limit "+start+" ,"+pageSize;   
	
  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")   
  		.create();
  		
  		Query<AppYueanCar> eQ = Ebean.find(AppYueanCar.class);
  		eQ.setRawSql(rawSql);
  		
  		List<AppYueanCar> list = eQ.findList();

		return list;
    }
    
    public static int appYueanCarCount(List<InputBtn>... btn){
		
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
      	
      	String params1="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(2).getValue()!=null&&!btn[0].get(2).getValue().equals("")){
      		params1=btn[0].get(2).getValue().trim();
      		paramsSql+=" and (code ='"+params1+"')";
      	}
		//sql语句       
	 	String sql = "select count(hot.id) as count  from cc_app_yuean_car hot,cc_app_car_style car,cc_app_car acar  where  1=1 and car.yueanid=hot.id and acar.id=car.carid "+paramsSql;  
     	
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }
    
    
    public static Result appCarStyleYuean(int id){
    	AppCarStyle car= AppCarStyle.findById(id);
    	if(car.getYuean()==null||car.getYuean().equals("0")){
    		/*car.setYuean("1");
    		AppYueanCar yueanCar=new AppYueanCar();
    		yueanCar.setUuid(UUIDGenerator.getUUID());
    		yueanCar.setCreatetime(new Date());
//    		hotCar.setThedesc("");
//    		hotCar.setFirstprice("");
    		car.setYueanCar(yueanCar);
    		car.update();*/
    		List<AppCarDiscount> discounts= AppCarDiscount.findAllList();
    		return ok(appyueancaradd.render(car,discounts));
    	}else{
    		car.setYuean("0");
    		car.update();
    		car.getYueanCar().delete();
    		return appYueanCarList();
    	}
    	
    }
    
    public static Result appYueanCarAddDo(){
    	DynamicForm in = Form.form().bindFromRequest();
		String id=in.get("id");
		String discount=in.get("cardiscount");
		String thedesc=in.get("thedesc");
		String firstprice=in.get("firstprice");
		AppCarDiscount carDiscount= AppCarDiscount.findById(Integer.parseInt(discount));
    	AppCarStyle car= AppCarStyle.findById(Integer.parseInt(id));
		car.setYuean("1");
		AppYueanCar yueanCar=new AppYueanCar();
		yueanCar.setUuid(UUIDGenerator.getUUID());
		yueanCar.setCreatetime(new Date());
		yueanCar.setThedesc(thedesc);
		yueanCar.setFirstprice(firstprice);
		yueanCar.setDiscount(carDiscount.getDiscount());
		yueanCar.setDisdesc(carDiscount.getThedesc());
		car.setYueanCar(yueanCar);
		car.update();
		return appYueanCarList();
    	
    }
    
    public static Result appYueanCarDetailed(int id) {
		
		List<AppCarAssort> assorts=AppCarAssort.findAllList();
		
		AppYueanCar hotCar=   AppYueanCar.findById(id);
		
		return ok(appyueancardetail.render(hotCar,assorts));
		
	}
    
    public static Result appYueanCarEdit(int id) {
		
		
    	AppYueanCar hotCar=   AppYueanCar.findById(id);
		
		List<AppCarDiscount> discounts= AppCarDiscount.findAllList();
		
		return ok(appyueancaredit.render(hotCar,discounts));
		
	}
    
    public static Result appYueanCarEditDo() {
    	//参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	String id=in.get("id");
    	
    	String thedesc=in.get("thedesc");
    	
    	String firstprice=in.get("firstprice");
    	
    	String discount=in.get("cardiscount");
    	
    	AppCarDiscount carDiscount= AppCarDiscount.findById(Integer.parseInt(discount));
    	
    	AppYueanCar hotCar=   AppYueanCar.findById(Integer.parseInt(id));
		
		hotCar.setFirstprice(firstprice);
		
		hotCar.setDiscount(carDiscount.getDiscount());
		
		hotCar.setDisdesc(carDiscount.getThedesc());
		
		hotCar.setThedesc(thedesc);
		
		hotCar.update();
		
		return appYueanCarList();
		
	}
    
    public static Result appYueanCarDel(int id) {
		
    	AppYueanCar hotCar=   AppYueanCar.findById(id);
		hotCar.getAppCarStyle().setHot("0");
		hotCar.getAppCarStyle().update();
		hotCar.delete();
		return appYueanCarList();
		
	}
    
    //租代购

    
    public static Result appRentCarList() {
		
		ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	List<InputBtn> btn=appRentCarCreateText(false);
    	//分页
    	//每页显示数据
		int pageSize = Integer.parseInt(Play.application().configuration().getString("pageSize"));
		
		//当前第N页
		int pageNow = 1;
		
		String pageNowIn = (String)in.get("pageNow");
		if(pageNowIn!=null){
		
			pageNow = Integer.parseInt(pageNowIn);
		}
		
    	List<AppRentCar> list = AppServiceApplication.appRentCarList(pageSize,pageNow,btn);
    	
    	//总数
		int countAll = AppServiceApplication.appRentCarCount(btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      	
		return ok(apprentcar.render(list,countAll,pageNow,pageCount,pageSize,"appYueanCar",btn));
		
    }
    
	public static List<InputBtn> appRentCarCreateText(boolean my){
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
	
    public static List<AppRentCar> appRentCarList(int pageSize,int pageNow,List<InputBtn>... btn){
		
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
      	String params1="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(2).getValue()!=null&&!btn[0].get(2).getValue().equals("")){
      		params1=btn[0].get(2).getValue().trim();
      		paramsSql+=" and (code ='"+params1+"')";
      	}
      	
		//sql语句       
	 	String sql = "select hot.id as id from cc_app_rent_car hot,cc_app_car_style car,cc_app_car acar where  1=1 and car.yueanid=hot.id and acar.id=car.carid "+paramsSql+" order by hot.createtime desc limit "+start+" ,"+pageSize;   
	
  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")   
  		.create();
  		
  		Query<AppRentCar> eQ = Ebean.find(AppRentCar.class);
  		eQ.setRawSql(rawSql);
  		
  		List<AppRentCar> list = eQ.findList();

		return list;
    }
    
    public static int appRentCarCount(List<InputBtn>... btn){
		
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
      	
      	String params1="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(2).getValue()!=null&&!btn[0].get(2).getValue().equals("")){
      		params1=btn[0].get(2).getValue().trim();
      		paramsSql+=" and (code ='"+params1+"')";
      	}
		//sql语句       
	 	String sql = "select count(hot.id) as count  from cc_app_rent_car hot,cc_app_car_style car,cc_app_car acar  where  1=1 and car.yueanid=hot.id and acar.id=car.carid "+paramsSql;  
     	
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }
    
    
    public static Result appCarStyleRent(int id){
    	AppCarStyle car= AppCarStyle.findById(id);
    	if(car.getRent()==null||car.getRent().equals("0")){
    		/*car.setYuean("1");
    		AppYueanCar yueanCar=new AppYueanCar();
    		yueanCar.setUuid(UUIDGenerator.getUUID());
    		yueanCar.setCreatetime(new Date());
//    		hotCar.setThedesc("");
//    		hotCar.setFirstprice("");
    		car.setYueanCar(yueanCar);
    		car.update();*/
    		List<AppCarDiscount> discounts= AppCarDiscount.findAllList();
    		return ok(apprentcaradd.render(car,discounts));
    	}else{
    		car.setRent("0");
    		car.update();
    		car.getRentCar().delete();
    		return appRentCarList();
    	}
    	
    }
    
    public static Result appRentCarAddDo(){
    	DynamicForm in = Form.form().bindFromRequest();
		String id=in.get("id");
		String discount=in.get("cardiscount");
		String thedesc=in.get("thedesc");
		String firstprice=in.get("firstprice");
		AppCarDiscount carDiscount= AppCarDiscount.findById(Integer.parseInt(discount));
    	AppCarStyle car= AppCarStyle.findById(Integer.parseInt(id));
		car.setRent("1");
		AppRentCar yueanCar=new AppRentCar();
		yueanCar.setUuid(UUIDGenerator.getUUID());
		yueanCar.setCreatetime(new Date());
		yueanCar.setThedesc(thedesc);
		yueanCar.setFirstprice(firstprice);
		yueanCar.setDiscount(carDiscount.getDiscount());
		yueanCar.setDisdesc(carDiscount.getThedesc());
		car.setRentCar(yueanCar);
		car.update();
		return appRentCarList();
    	
    }
    
    public static Result appRentCarDetailed(int id) {
		
		List<AppCarAssort> assorts=AppCarAssort.findAllList();
		
		AppRentCar hotCar=   AppRentCar.findById(id);
		
		return ok(apprentcardetail.render(hotCar,assorts));
		
	}
    
    public static Result appRentCarEdit(int id) {
		
		
    	AppRentCar hotCar=   AppRentCar.findById(id);
		
		List<AppCarDiscount> discounts= AppCarDiscount.findAllList();
		
		return ok(apprentcaredit.render(hotCar,discounts));
		
	}
    
    public static Result appRentCarEditDo() {
    	//参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	String id=in.get("id");
    	
    	String thedesc=in.get("thedesc");
    	
    	String firstprice=in.get("firstprice");
    	
    	String discount=in.get("cardiscount");
    	
    	AppCarDiscount carDiscount= AppCarDiscount.findById(Integer.parseInt(discount));
    	
    	AppRentCar hotCar=   AppRentCar.findById(Integer.parseInt(id));
		
		hotCar.setFirstprice(firstprice);
		
		hotCar.setDiscount(carDiscount.getDiscount());
		
		hotCar.setDisdesc(carDiscount.getThedesc());
		
		hotCar.setThedesc(thedesc);
		
		hotCar.update();
		
		return appRentCarList();
		
	}
    
    public static Result appRentCarDel(int id) {
		
    	AppRentCar hotCar=   AppRentCar.findById(id);
		hotCar.getAppCarStyle().setRent("0");
		hotCar.getAppCarStyle().update();
		hotCar.delete();
		return appRentCarList();
	}    
    
//车险
//品牌	
	public static Result appSuranceAssortAdd(){
		
		return ok(appsuranceassortadd.render(""));
	}
	
	public static Result appSuranceAssortAddDo(){
		DynamicForm in= Form.form().bindFromRequest();
		String result="";
		String name=in.data().get("name");
		
		try {
		MultipartFormData body = request().body().asMultipartFormData();
		List<FilePart> fileParts= body.getFiles();
	//	File dir=new File("../webapps/images");
		String uploadimage=Play.application().configuration().getString("imageserver");
	//	File dir=new File("../apache-tomcat-8.0.30/webapps/images");
		File dir=new File(uploadimage);
		
		if(!dir.exists()){
			dir.mkdir();
		}
		
		FileOutputStream os;
		FileInputStream is;
		byte[] b=new byte[1024];
		File file = null;
		File upload;
		long now=new Date().getTime();
		
			if(fileParts.size()>0){
				for (FilePart filePart : fileParts) {
						String filename=filePart.getFilename();
						file=new File(dir,now+filename);
						if(!file.exists()){
							file.createNewFile();
						}
						upload= filePart.getFile().getCanonicalFile();
						is=new FileInputStream(upload);
							os=new FileOutputStream(file);
							while(is.read(b)>0){
								os.write(b);
							}
							os.write(b);
							is.close();
							os.close();
					}
				}
			AppSuranceAssort assort=new AppSuranceAssort();
			assort.setLogo(file.getAbsoluteFile().getName());
			assort.setAssortname(name);
			String title=(UtilsObject.getPYIndexStr(in.get("name").substring(0,1).toUpperCase(), UtilsObject.checkStrChinese(in.get("name")))).substring(0, 1);
			String code="";
			code=searchNextSuranceCode(title);
			code=title+code;
			assort.setTitle(title);
			assort.setAssortcode(code);
			String uuid=UUIDGenerator.getUUID();
			assort.setUuid(uuid);
			assort.setCreatetime(new Date());
			assort.save();
			result="添加成功!";
			} catch (Exception e) {
				e.printStackTrace();
				result="添加失败!";
			}
		
		return ok(appsuranceassortadd.render(result));
		
	}
	
    public static String searchNextSuranceCode(String title){
		//sql语句       
	 	String sql = "select max(assortcode) as code from cc_app_surance_assort where 1=1  and title='"+title+"'";  
     	
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
    
    
	public static Result appSuranceAssortList() {
		
		ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	List<InputBtn> btn=suranceAssortCreateText(false);
    	//分页
    	//每页显示数据
		int pageSize = Integer.parseInt(Play.application().configuration().getString("pageSize"));
		
		//当前第N页
		int pageNow = 1;
		
		String pageNowIn = (String)in.get("pageNow");
		if(pageNowIn!=null){
		
			pageNow = Integer.parseInt(pageNowIn);
		}
		
    	List<AppSuranceAssort> list = AppServiceApplication.appSuranceAssortList(pageSize,pageNow,btn);
    	
    	//总数
		int countAll = AppServiceApplication.appSuranceAssortCount(btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      	
		return ok(appsuranceassort.render(list,countAll,pageNow,pageCount,pageSize,"appSuranceAssort",btn));
		
    }
    
	public static List<InputBtn> suranceAssortCreateText(boolean my){
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	Map<String, String> data=in.data();
    	List<InputBtn> l=new ArrayList<InputBtn>();
    	if(!my){
    		InputBtn bt1=new InputBtn("params","params","","品牌名称","");
    		

        	if(data.containsKey(bt1.getId())){
        		bt1.setValue(data.get(bt1.getId()));
        	}

    		l.add(bt1);
    	}
		
		return l;
		
	}
	
    public static List<AppSuranceAssort> appSuranceAssortList(int pageSize,int pageNow,List<InputBtn>... btn){
		
		int start=(pageNow-1)*pageSize;
		
      	String paramsSql = "";
		
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (name like '%"+params+"%')";
      	}
      	

		//sql语句       
	 	String sql = "select id from cc_app_surance_assort where 1=1   "+paramsSql+" order by title limit "+start+" ,"+pageSize;   
	
  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")   
  		.create();
  		
  		Query<AppSuranceAssort> eQ = Ebean.find(AppSuranceAssort.class);
  		eQ.setRawSql(rawSql);
  		
  		List<AppSuranceAssort> list = eQ.findList();

		return list;
    }
    
    public static int appSuranceAssortCount(List<InputBtn>... btn){
		
      	String paramsSql = "";

      	
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (name like '%"+params+"%')";
      	}
      	
		//sql语句       
	 	String sql = "select count(id) as count from cc_app_surance_assort where  1=1  "+paramsSql;  
     	
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }
    
    public static Result appSuranceAssortDel(int id){
    	AppSuranceAssort.findById(id).delete();
    	return appSuranceAssortList();
    }
    
    public static Result appSuranceAssortEdit(int id){
    	AppSuranceAssort assort= AppSuranceAssort.findById(id);;
    	return ok(appsuranceassortedit.render(assort,""));
    }
    
    public static Result appSuranceAssortDetailed(int id){
    	AppSuranceAssort assort=AppSuranceAssort.findById(id);
    	return ok(appsuranceassortdetail.render(assort,""));
    }
    
    public static Result appSuranceAssortEditDo(){
		DynamicForm in= Form.form().bindFromRequest();
		String result="";
		String id=in.data().get("id");
		String name=in.data().get("name");
		String remark=in.data().get("remark");
		try {
		MultipartFormData body = request().body().asMultipartFormData();
		List<FilePart> fileParts= body.getFiles();
//		File dir=new File("../webapps/images");
		String uploadimage=Play.application().configuration().getString("imageserver");
//		File dir=new File("../apache-tomcat-8.0.30/webapps/images");
		File dir=new File(uploadimage);
		
		if(!dir.exists()){
			dir.mkdir();
		}
		
		FileOutputStream os;
		FileInputStream is;
		byte[] b=new byte[1024];
		File file = null;
		File upload;
		long now=new Date().getTime();
		
			if(fileParts.size()>0){
				for (FilePart filePart : fileParts) {
						String filename=filePart.getFilename();
						file=new File(dir,now+filename);
						if(!file.exists()){
							file.createNewFile();
						}
						upload= filePart.getFile().getCanonicalFile();
						is=new FileInputStream(upload);
							os=new FileOutputStream(file);
							while(is.read(b)>0){
								os.write(b);
							}
							os.write(b);
							is.close();
							os.close();
					}
				}
			AppSuranceAssort assort=AppSuranceAssort.findById(Integer.parseInt(id));
			if(file==null){
				assort.setLogo(in.get("logo"));
			}else{
				assort.setLogo(file.getAbsoluteFile().getName());
			}
			assort.setAssortname(name);
			String title=(UtilsObject.getPYIndexStr(in.get("name").substring(0,1).toUpperCase(), UtilsObject.checkStrChinese(in.get("name")))).substring(0, 1);
			String code="";
			code=searchNextSuranceCode(title);
			code=title+code;
			assort.setTitle(title);
			assort.setAssortcode(code);
			String uuid=UUIDGenerator.getUUID();
			assort.setUuid(uuid);
			assort.setCreatetime(new Date());
			assort.update();
			result="编辑成功!";
			} catch (Exception e) {
				e.printStackTrace();
				result="编辑失败!";
			}
		
		return appSuranceAssortList();
		
	}
    
//车险   
    public static Result appSuranceAdd() {
		
		List<AppSuranceAssort> assorts=AppSuranceAssort.findAllList();
		
		return ok(appsuranceadd.render(assorts));
		
	}
    
	public static Result appSuranceAddDo(){
		DynamicForm in = Form.form().bindFromRequest();
		String surancename=in.get("surancename");
		String suranceassort=in.get("suranceassort");
		String productcode=in.get("productcode");
		String price=in.get("price");
		String remark=in.get("remark");
		String inusetime=in.get("inusetime");
		
		AppSurance  surance=new AppSurance();
		surance.setAssortcode(suranceassort);
		surance.setAssortname(AppSuranceAssort.findById(Integer.parseInt(suranceassort)).getAssortname());
		surance.setSurancename(surancename);
		surance.setCreatetime(new Date());
		surance.setProductcode(productcode);
		surance.setRemark(remark);
		surance.setPrice(price);
		surance.setInusetime(inusetime);
		
		List<AppSuranceImage> images=new ArrayList<AppSuranceImage>();
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
			 AppSuranceImage image=new AppSuranceImage();
			 image.setCreatetime(new Date());
			 image.setUrl(in.get(array[i]+""));
			 images.add(image);
		}
		surance.setImages(images);
		surance.save();
		List<AppSuranceAssort> assorts=AppSuranceAssort.findAllList();
		return ok(appsuranceadd.render(assorts));
	}
    
	
	
	public static Result appSuranceList() {
		
		ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	List<InputBtn> btn=appSuranceCreateText(false);
    	//分页
    	//每页显示数据
		int pageSize = Integer.parseInt(Play.application().configuration().getString("pageSize"));
		
		//当前第N页
		int pageNow = 1;
		
		String pageNowIn = (String)in.get("pageNow");
		if(pageNowIn!=null){
		
			pageNow = Integer.parseInt(pageNowIn);
		}
		
    	List<AppSurance> list = AppServiceApplication.appSuranceList(pageSize,pageNow,btn);
    	
    	//总数
		int countAll = AppServiceApplication.appSuranceCount(btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      	
		return ok(appsurance.render(list,countAll,pageNow,pageCount,pageSize,"appSurance",btn));
		
    }
    
	public static List<InputBtn> appSuranceCreateText(boolean my){
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	Map<String, String> data=in.data();
    	List<InputBtn> l=new ArrayList<InputBtn>();
    	if(!my){
    		InputBtn bt1=new InputBtn("params","params","","车险车型","");
    		

    		InputBtn bt2=new InputBtn("params0","params0","","车险类别","");
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
	
    public static List<AppSurance> appSuranceList(int pageSize,int pageNow,List<InputBtn>... btn){
		
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
	 	String sql = "select id from cc_app_surance where 1=1 "+paramsSql+" order by createtime desc limit "+start+" ,"+pageSize;   
	
  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")   
  		.create();
  		
  		Query<AppSurance> eQ = Ebean.find(AppSurance.class);
  		eQ.setRawSql(rawSql);
  		
  		List<AppSurance> list = eQ.findList();

		return list;
    }
    
    public static int appSuranceCount(List<InputBtn>... btn){
		
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
	 	String sql = "select count(id) as count from cc_app_surance where 1=1 "+paramsSql;  
     	
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }
    
    public static Result appSuranceDetailed(int id) {
		
		List<AppSuranceAssort> assorts=AppSuranceAssort.findAllList();
		
		AppSurance surance= AppSurance.findById(id);
		
		return ok(appsurancedetail.render(assorts,surance));
		
	}
    
    public static Result appSuranceDel(int id) {
				
    	AppSurance surance= AppSurance.findById(id);
		surance.delete();

		return appSuranceList();
		
	}
    
    public static Result appSuranceEdit(int id) {
		
		List<AppSuranceAssort> assorts=AppSuranceAssort.findAllList();
		
		AppSurance surance= AppSurance.findById(id);
		
		return ok(appsuranceedit.render(assorts,surance));
		
	}
    
    public static Result appSuranceTried(int id) {
		
    	List<AppSuranceAssort> assorts=AppSuranceAssort.findAllList();
		
    	AppSurance surance= AppSurance.findById(id);
		
		return ok(appsurancetried.render(assorts,surance));
		
	}
    
    
    public static Result appSuranceEditDo() {
		
    	DynamicForm in = Form.form().bindFromRequest();
    	String suranceid=in.get("suranceid");
		String surancename=in.get("surancename");
		String suranceassort=in.get("suranceassort");
		String productcode=in.get("productcode");
		String price=in.get("price");
		String inusetime=in.get("inusetime");
		String remark=in.get("remark");
		
		AppSurance surance=AppSurance.findById(Integer.parseInt(suranceid));
		surance.setAssortcode(suranceassort);
		surance.setAssortname(AppSuranceAssort.findById(Integer.parseInt(suranceassort)).getAssortname());
		surance.setSurancename(surancename);
		surance.setCreatetime(new Date());
		surance.setProductcode(productcode);
		surance.setRemark(remark);
		surance.setPrice(price);
		surance.setInusetime(inusetime);
		
		List<AppSuranceImage> suranceImages=new ArrayList<AppSuranceImage>();
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
				 AppSuranceImage image=new AppSuranceImage();
				 image.setCreatetime(new Date());
				 image.setUrl(in.get(array[i]+""));
				 suranceImages.add(image);
			}
			 surance.setImages(suranceImages);
		}
		
		surance.update();
		
		return appSuranceList();
		
	}
    
    public static Result appSuranceStand(int id) {
		
		AppSurance surance= AppSurance.findById(id);
		surance.setStand(1);
		surance.update();
		
		return appSuranceList();
		
	}
    
    public static Result appSuranceStandCh(int id) {
		
    	AppSurance surance= AppSurance.findById(id);
    	surance.setStand(0);
    	surance.update();
		
		return appSuranceList();
		
	}
    
    
}
