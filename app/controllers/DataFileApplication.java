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
public class DataFileApplication extends Controller {
	
	/*
	 * 上传图片
	 */
	public static Result uploadImage() {
		MultipartFormData body = request().body().asMultipartFormData();
		List<FilePart> fileParts= body.getFiles();
//		File dir=new File("../webapps/images");
		String uploadimage=Play.application().configuration().getString("imageserver");
//		File dir=new File("../apache-tomcat-8.0.30/webapps/images");
		File dir=new File(uploadimage);
//		File dir=new File("../");
		if(!dir.exists()){
			dir.mkdir();
		}
		System.out.println(dir.exists());
		FileOutputStream os;
		FileInputStream is;
		byte[] b=new byte[1024];
		File file = null;
		File upload;
		long now=new Date().getTime();
		if(fileParts.size()>0){
			
			for (FilePart filePart : fileParts) {
				try {
					String filename=filePart.getFilename();
					filename=filename.substring(filename.lastIndexOf("."), filename.length());
					file=new File(dir,UUIDGenerator.getUUID()+now+filename);
					upload= filePart.getFile().getCanonicalFile();
					is=new FileInputStream(upload);
						os=new FileOutputStream(file);
						while(is.read(b)>0){
							os.write(b);
						}
						os.write(b);
						is.close();
						os.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
				
			}
		}

		return ok(file.getAbsoluteFile().getName());
		
		
	}
	
	public static Result uploadForward() {
		
		List<CarAssort> assorts=CarAssort.findAllList();
		
		return ok(uploadImage.render(assorts));
		
	}
}
