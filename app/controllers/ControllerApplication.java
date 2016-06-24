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


public class ControllerApplication extends Controller {
	
	@BodyParser.Of(BodyParser.Json.class)
	public static Result rqInvokeController() {
		
		ObjectNode resultJson = Json.newObject();
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
		
		Set<String> sets= in.data().keySet();

    	resultJson=rqInvokeControllerInit(in.data());

		return ok(resultJson.toString());
		
    }

	@BodyParser.Of(BodyParser.Json.class)
	public static ObjectNode rqInvokeControllerInit(Map<String, String> data) {
		String type=data.get("type");
		ObjectNode resultJson = Json.newObject();
		try {
			if(type==null||type.equals("")){
				resultJson=rqInvokeControllerData(data);
			}
		} catch (Exception e) {
			resultJson.put("code", "4");
			resultJson.put("msg", "系统异常");
			e.printStackTrace();
			return resultJson;
		}
		return resultJson;
	}
	
	@BodyParser.Of(BodyParser.Json.class)
	public static ObjectNode rqInvokeControllerData(Map<String, String> data) throws JsonProcessingException {
		// TODO Auto-generated method stub
		String cmd =data.get("cmd");
		ObjectNode resultJson = Json.newObject();
		
		System.out.println("cmd:"+cmd);
		if(cmd==null||cmd.equals("")){
			resultJson.put("code", "1");
			resultJson.put("msg", "参数错误!");
			return resultJson;
		}
		if(cmd.equals(ControllerType.getSms)){
			resultJson=AppCustomerApplication.createVerifycode(data);
		}else if(cmd.equals(ControllerType.Register)){
			String username =data.get("username");
			String token =data.get("token");
			resultJson=AppCustomerApplication.createVerifycode(data);
		}else if(cmd.equals(ControllerType.carAssort)){
			String username =data.get("username");
			String token =data.get("token");
			resultJson=AppCarApplication.carAssortList(data);
		}else if(cmd.equals(ControllerType.carList)){
			String username =data.get("username");
			String token =data.get("token");
			resultJson=AppCarApplication.carList(data);
		}else if(cmd.equals(ControllerType.car)){
			String username =data.get("username");
			String token =data.get("token");
			resultJson=AppCarApplication.car(data);
		}else if(cmd.equals(ControllerType.queryCarBrand)){
			String username =data.get("username");
			String token =data.get("token");
			resultJson=AppCarApplication.queryCarBrand(data);
		}else if(cmd.equals(ControllerType.queryCars)){
			String username =data.get("username");
			String token =data.get("token");
			resultJson=AppCarApplication.queryCars(data);
		}else if(cmd.equals(ControllerType.advert)){
			String username =data.get("username");
			String token =data.get("token");
			resultJson=AppAdvertApplication.advertList(data);
		}else if(cmd.equals(ControllerType.buyCar)){
			String username =data.get("username");
			String token =data.get("token");
			resultJson=AppBuyCarApplication.buyCar(data);
		}else if(cmd.equals(ControllerType.hotCar)){
			String username =data.get("username");
			String token =data.get("token");
			resultJson=AppCarApplication.queryHotCar(data);
		}else if(cmd.equals(ControllerType.carStyle)){
			String username =data.get("username");
			String token =data.get("token");
			resultJson=AppCarApplication.queryCarStyle(data);
		}else if(cmd.equals(ControllerType.parrallel)){
			String username =data.get("username");
			String token =data.get("token");
			resultJson=AppParrallelApplication.parrallelList(data);
		}else if(cmd.equals(ControllerType.parrallelcar)){
			String username =data.get("username");
			String token =data.get("token");
			resultJson=AppParrallelApplication.parrallelCarList(data);
		}else if(cmd.equals(ControllerType.elitebuycar)){
			String username =data.get("username");
			String token =data.get("token");
			resultJson=AppBuyCarApplication.eliteBuyCar(data);
		}else if(cmd.equals(ControllerType.parrallelcardetail)){
			String username =data.get("username");
			String token =data.get("token");
			resultJson=AppParrallelApplication.parrallelCarDetail(data);
		}else if(cmd.equals(ControllerType.parrallelbuycar)){
			String username =data.get("username");
			String token =data.get("token");
			resultJson=AppBuyCarApplication.parrallelBuyCar(data);
		}else if(cmd.equals(ControllerType.rentCar)){
			String username =data.get("username");
			String token =data.get("token");
			resultJson=AppCarApplication.queryRentCar(data);
		}else if(cmd.equals(ControllerType.yueanCar)){
			String username =data.get("username");
			String token =data.get("token");
			resultJson=AppCarApplication.queryYueanCar(data);
		}else if(cmd.equals(ControllerType.yueanDetail)){
			String username =data.get("username");
			String token =data.get("token");
			resultJson=AppCarApplication.appYueanCarDetail(data);
		}else if(cmd.equals(ControllerType.rentDetail)){
			String username =data.get("username");
			String token =data.get("token");
			resultJson=AppCarApplication.appRentCarDetail(data);
		}else if(cmd.equals(ControllerType.rentBuyCar)){
			String username =data.get("username");
			String token =data.get("token");
			resultJson=AppBuyCarApplication.rentBuyCar(data);
		}else if(cmd.equals(ControllerType.yueanBuyCar)){
			String username =data.get("username");
			String token =data.get("token");
			resultJson=AppBuyCarApplication.yueanBuyCar(data);
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		else if(cmd.equals(ControllerType.qcAdvert)){
			String username =data.get("username");
			String token =data.get("token");
			resultJson=QCAdvertApplication.advert(data);
		}else if(cmd.equals(ControllerType.cityList)){
			String username =data.get("username");
			String token =data.get("token");
			resultJson=AppJsonApplication.cityList(data);
		}
		
		else{
			resultJson.put("code", "6");
			resultJson.put("msg", "没有此请求");
		}
		
		return resultJson;
	}
}
