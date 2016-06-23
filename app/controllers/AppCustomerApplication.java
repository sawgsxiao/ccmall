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


public class AppCustomerApplication extends Controller {

	@BodyParser.Of(BodyParser.Json.class)
	public static ObjectNode rqInvokeControllerInit(Map<String, String> data) {
		String type=data.get("type");
		ObjectNode resultJson = Json.newObject();
		if(type==null||type.equals("")){
		}
		return resultJson;
	}
	
	@BodyParser.Of(BodyParser.Json.class)
	public static ObjectNode createVerifycode(Map<String, String> data) {
		// TODO Auto-generated method stub
		ObjectNode resultJson = Json.newObject();
		JSONObject jsonObject = null;
		try{
			String username =data.get("username");
			System.out.println(username);
			if(username == null || username.equals("")) { 
	    		resultJson.put("code", "1"); 
	    		resultJson.put("msg", "参数错误！");
	     		return resultJson; 
			} 
			VerifyCode verifyCode= AppVerifyCodeApplication.findVerifycode(username);
			if(verifyCode==null||verifyCode.equals("")){
				VerifyCode.del(username);
				verifyCode =new VerifyCode();
				verifyCode.setUsername(username);
				verifyCode.setUuid(UUIDGenerator.getUUID());
				verifyCode.setVcode(UtilsObject.randomNumeric6());
				verifyCode.setCreatetime(new Date());
				verifyCode.save();
			}

			String code="0";
			String msg="发送短信成功!";
			String mobile=verifyCode.getUsername();
			String vcode=verifyCode.getVcode();
			String smsContent="您注册的验证码是:"+verifyCode.getVcode()+",请在5分钟内使用【Q车网】";
			smsContent=new String(smsContent.getBytes(), "UTF-8");
			String httpUrl =Play.application().configuration().getString("httpSmsUrl");
			String httpArg = "accesskey="+Play.application().configuration().getString("accesskey")+"&secretkey="+Play.application().configuration().getString("secretkey")+"&mobile="+mobile+"&content="+smsContent;			
			jsonObject= JSONObject.fromObject(SmsPlatformApplication.request(httpUrl, httpArg));
			System.out.println("result:"+jsonObject.toString());
			if(jsonObject.get("result").equals("01")){
				resultJson.put("code", code);
				resultJson.put("msg", msg);
				resultJson.put("vcode", vcode);
			}else{
				code="3";
				msg="下发短信失败!";
				resultJson.put("code", code);
				resultJson.put("msg", msg);
				resultJson.put("msgResult",jsonObject.toString());
			}
		}catch (Exception e) {
			Logger.info("解析报文错误!"+jsonObject.toString());
			resultJson.put("code", "4");
			resultJson.put("msg", "新建验证码失败!");
		}
		return resultJson;
	}
	
	
}
