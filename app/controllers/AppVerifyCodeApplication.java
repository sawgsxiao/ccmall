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

import play.api.libs.ws.Response;
import play.api.libs.ws.WS;
import play.data.DynamicForm;
import com.avaje.ebean.*;

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
import java.lang.NullPointerException;
import java.io.ByteArrayOutputStream;
import javax.imageio.stream.FileImageInputStream;

import play.db.ebean.Model;


public class AppVerifyCodeApplication extends Controller {

	
	
	public static VerifyCode findVerifycode(String username) {
		// TODO Auto-generated method stub
		String sql =" select * from cc_verify_code where username='"+username+"' and createtime<ADDDATE(now(),INTERVAL "+Integer.parseInt(Play.application().configuration().getString("verifyCodeTimeout"))+" MINUTE)";
		
		System.out.println(sql);
		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")   
  		.create();
		
		Query<VerifyCode> eQ = Ebean.find(VerifyCode.class);
  		eQ.setRawSql(rawSql);
		
  		List<VerifyCode> list =eQ.findList();
  		
  		if(list.size()==1){
  			return list.get(0);
  		}
		return null;
	}
	
	
}
