package controllers;

import java.util.Date;
import java.util.List;
import java.util.Map;

import models.AppQCBuyCar;
import models.City;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.*;
import play.libs.Json;
import play.mvc.*;

import views.html.*;

public class AppJsonApplication extends Controller {

    public static ObjectNode cityList(Map<String, String> data) {
    	ObjectMapper listMapper = new ObjectMapper(); 
    	ObjectNode returnJson=Json.newObject();
    	List<City> list= City.findListByParent("5");
    	if(list.size()==0){
    		returnJson.put("code", "5");
    		returnJson.put("msg", "无数据");
    		return returnJson;
    	}
    	ArrayNode arrayNode=listMapper.createArrayNode();
    	for (City city : list) {
    		ObjectNode node=Json.newObject();
    		node.put("name", city.getFullname());
    		node.put("netid", city.getNetid());
    		arrayNode.add(node);
		}
    	returnJson.put("list", arrayNode);
    	returnJson.put("code", "0");
		returnJson.put("msg", "查询数据成功");
        return returnJson;
    }
    
    /*private String name;
    private String phone;
    private String pertype;
    private String payterm;
    private Date createtime;
    private String firstpay;
    private String monthpay;
    private String fullpay;
    private String cityid;
    private String carstyleid;
    private String barepay;
    private String possiblepay;*/
    
    public static ObjectNode buyCar(Map<String, String> data) {
    	ObjectMapper listMapper = new ObjectMapper(); 
    	ObjectNode returnJson=Json.newObject();
    	
    	String name=data.get("name");
    	String phone=data.get("phone");
    	String pertype=data.get("pertype");
    	String payterm=data.get("payterm");
    	String firstpay=data.get("firstpay");
    	String monthpay=data.get("monthpay");
    	String fullpay=data.get("fullpay");
    	String cityid=data.get("cityid");
    	String carstyleid=data.get("uuid");
    	String barepay=data.get("barepay");
    	String possiblepay=data.get("possiblepay");

    	AppQCBuyCar buyCar=new AppQCBuyCar();
    	buyCar.setBarepay(barepay);
    	buyCar.setCarstyleid(carstyleid);
    	buyCar.setCityid(cityid);
    	buyCar.setCreatetime(new Date());
    	buyCar.setFirstpay(firstpay);
    	buyCar.setFullpay(fullpay);
    	buyCar.setMonthpay(monthpay);
    	buyCar.setName(name);
    	buyCar.setPayterm(payterm);
    	buyCar.setPertype(pertype);
    	buyCar.setPhone(phone);
    	buyCar.setPossiblepay(possiblepay);
    	buyCar.save();
    	returnJson.put("code", "0");
		returnJson.put("msg", "提交数据成功");
        return returnJson;
    }

}
