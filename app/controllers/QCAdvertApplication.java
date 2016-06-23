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

import java.util.ArrayList;
import java.util.List;
import models.*;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.regex.Matcher;
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

import org.dom4j.util.PerThreadSingleton;

import play.db.ebean.Model;

//@Security.Authenticated(Secured.class)
public class QCAdvertApplication extends Controller {
	
	public static Result qCAdvertAdd(){
		return ok(qcadvertadd.render(""));
	}
	
	public static Result qCAdvertAddDo(){
		DynamicForm in= Form.form().bindFromRequest();
		String result="";
		String name=in.data().get("name");
		String remark=in.data().get("remark");
		String type=in.data().get("type");
		try {
		MultipartFormData body = request().body().asMultipartFormData();
		List<FilePart> fileParts= body.getFiles();
		//File dir=new File("../webapps/images");
		String uploadimage=Play.application().configuration().getString("imageserver");
//		File dir=new File("../apache-tomcat-8.0.30/webapps/images");
		String date=new SimpleDateFormat("yyyyMMdd").format(new Date());
		File dir=new File(uploadimage+"/"+date+"/");
		
		if(!dir.exists()){
			dir.mkdirs();
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
			QCAdvert advert=new QCAdvert();
			if(file==null){
				advert.setImgurl(in.get("imgurl"));
			}else{
				advert.setImgurl(file.getAbsoluteFile().getName());
			}
			String filePath=file.getPath();
			String reg="/(images)/[0-9]{8}/[a-zA-Z0-9-_]+\\.(?i)(png|gif|jepg)$";
			Pattern pattern=Pattern.compile(reg);
			Matcher matcher=pattern.matcher(filePath);
			while(matcher.find()){
				filePath=matcher.group();
			}
			advert.setImgurl(filePath);
			advert.setName(name);
			advert.setRemark(remark);
			String uuid=UUIDGenerator.getUUID();
			advert.setUuid(uuid);
			advert.setType(type);
			advert.setCreatetime(new Date());
			advert.save();
			result="添加成功!";
			} catch (Exception e) {
				e.printStackTrace();
				result="添加失败!";
			}
		
		return ok(qcadvertadd.render(result));
		
	}
	
	public static Result advertList() {
		
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
		
    	List<Advert> list = QCAdvertApplication.list(pageSize,pageNow,btn);
    	
    	//总数
		int countAll = QCAdvertApplication.count(btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      	
		return ok(advert.render(list,countAll,pageNow,pageCount,pageSize,"advert",btn));
		
    }
    
	public static List<InputBtn> createText(boolean my){
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	Map<String, String> data=in.data();
    	List<InputBtn> l=new ArrayList<InputBtn>();
    	if(!my){
    		InputBtn bt1=new InputBtn("params","params","","广告名称","");
    		

        	if(data.containsKey(bt1.getId())){
        		bt1.setValue(data.get(bt1.getId()));
        	}

    		l.add(bt1);
    	}
		
		return l;
		
	}
	
    public static List<Advert> list(int pageSize,int pageNow,List<InputBtn>... btn){
		
		int start=(pageNow-1)*pageSize;
		
      	String paramsSql = "";
		
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (name like '%"+params+"%')";
      	}
      	

		//sql语句       
	 	String sql = "select id from cc_advert where 1=1 "+paramsSql+" order by createtime desc limit "+start+" ,"+pageSize;   
	
  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")   
  		.create();
  		
  		Query<Advert> eQ = Ebean.find(Advert.class);
  		eQ.setRawSql(rawSql);
  		
  		List<Advert> list = eQ.findList();

		return list;
    }
    
    public static int count(List<InputBtn>... btn){
		
      	String paramsSql = "";

      	
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (name like '%"+params+"%')";
      	}
      	
		//sql语句       
	 	String sql = "select count(id) as count from cc_advert where 1=1 "+paramsSql;  
     	
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }
    
    public static Result advertDel(int id){
    	Advert.findById(id).delete();
    	return advertList();
    }
    
    public static Result advertEdit(int id){
    	Advert advert= Advert.findById(id);;
    	return ok(advertedit.render(advert,""));
    }
    
    
    public static Result advertEditDo(){
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
			Advert advert=Advert.findById(Integer.parseInt(id));
			advert.setImgurl(file.getAbsoluteFile().getName());
			advert.setName(name);
			advert.setRemark(remark);
			advert.update();
			result="编辑成功!";
			} catch (Exception e) {
				e.printStackTrace();
				result="编辑失败!";
			}
		
		return advertList();
		
	}
    
    public static Result advertDetailed(int id){
    	Advert advert= Advert.findById(id);;
    	return ok(advertdetail.render(advert,""));
    }
    
    
    
    public static ObjectNode advert(Map<String, String> data){
//    	DynamicForm in= Form.form().bindFromRequest();
    	ObjectNode resultJson = Json.newObject();
    	String type=data.get("qtype");
    	List<QCAdvert> adverts = QCAdvert.findListByType(type);
    	resultJson.put("code", "0");
    	resultJson.put("result", Json.toJson(adverts));
    	System.out.println(resultJson);
    	return resultJson;
    }
    
    
}
