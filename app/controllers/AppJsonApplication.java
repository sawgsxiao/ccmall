package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import models.AppCarImage;
import models.AppCarStyle;
import models.AppQCBuyCar;
import models.City;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.*;
import play.libs.Json;
import play.mvc.*;


import utils.ColorMaping;
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
    
    
    public static ObjectNode carStyle(Map<String, String> data) {
    	ObjectMapper listMapper = new ObjectMapper(); 
    	ObjectNode returnJson=Json.newObject();
    	
    	String target=data.get("target");
    	List<AppCarStyle> list= AppCarStyle.findAllList(target);
    	if(list.size()==0){
    		returnJson.put("code", "5");
    		returnJson.put("msg", "无数据");
    		return returnJson;
    	}
    	ArrayNode arrayNode=listMapper.createArrayNode();
    	SimpleDateFormat df =new SimpleDateFormat("yyyy年MM月dd日");
    	int index=0;
    	if(target.equals("isflash")){
    		for (AppCarStyle car : list) {
        		ObjectNode node=Json.newObject();
        		node.put("uuid", car.getUuid());
    			node.put("actime", df.format(car.getStarttime())+"-"+df.format(car.getEndtime()));
    			if(car.getAppCar().getImages().size()>0){
    				node.put("image", Play.application().configuration().getString("ippath")+Play.application().configuration().getString("outpath")+"/"+car.getFlashimg());
    			}
    			arrayNode.add(node);
    			index++;
    			if(index==10){
    				break;
    			}
    		}
    	}else{
    		for (AppCarStyle car : list) {
        		ObjectNode node=Json.newObject();
    			node.put("uuid", car.getUuid());
    			node.put("name", car.getStylename());
    			node.put("desc", car.getRemark());
    			node.put("price", car.getPrice());
    			node.put("sale", car.getSale());
    			node.put("discount", car.getDiscount());
    			if(car.getAppCar().getImages().size()>0){
    				node.put("image", Play.application().configuration().getString("ippath")+Play.application().configuration().getString("outpath")+"/"+car.getAppCar().getImages().get(0).getUrl());
    			}
    			arrayNode.add(node);
    			index++;
    			if(index==10){
    				break;
    			}
    		}
    	}
    	
    	returnJson.put("list", arrayNode);
    	returnJson.put("code", "0");
		returnJson.put("msg", "查询数据成功");
        return returnJson;
    }

    public static ObjectNode carStyleDetail(Map<String, String> data) {
    	ObjectMapper listMapper = new ObjectMapper(); 
    	ObjectNode returnJson=Json.newObject();
    	ObjectNode carNode=Json.newObject();
    	String uuid=data.get("uuid");
    	AppCarStyle car= AppCarStyle.findByStyleUuid(uuid);
    	if(car==null){
    		returnJson.put("code", "5");
    		returnJson.put("msg", "无数据");
    		return returnJson;
    	}
    	SimpleDateFormat df =new SimpleDateFormat("yyyy年MM月dd日");
    	ArrayNode colorlist=listMapper.createArrayNode();
    	ArrayNode imagelist=listMapper.createArrayNode();
    	String colors=car.getAppCar().getColorlist();
    	if(colors!=null&&!colors.equals("")){
    		if(colors.contains(",")){
    			String[] cls=colors.split(",");
    			for (int i = 0; i < cls.length; i++) {
    				ObjectNode node=Json.newObject();
    				node.put("color", cls[i]);
    				node.put("name", ColorMaping.getValue(cls[i]));
    				colorlist.add(node);
				}
    		}else{
    			ObjectNode node=Json.newObject();
				node.put("color", colors.replace("#", ""));
				node.put("name", ColorMaping.getValue(colors));
				colorlist.add(node);
    		}
    		carNode.put("colorlist", colorlist);
    	}else{
    		carNode.put("colorlist", colorlist);
    	}
    	
    	carNode.put("uuid", car.getUuid());
    	carNode.put("name", car.getStylename());
    	carNode.put("desc", car.getRemark());
    	carNode.put("price", car.getPrice());
    	carNode.put("sale", car.getSale());
    	carNode.put("discount", car.getDiscount());
    	carNode.put("oil", car.getOil());
    	carNode.put("isflash", car.getIsflash()!=null?car.getIsflash():"0");
    	carNode.put("engineout", car.getEngineout());
    	carNode.put("pubdate", car.getPubdate());
    	carNode.put("actime", car.getStarttime()!=null?df.format(car.getStarttime())+"-"+df.format(car.getEndtime()):"");
    	if(car.getAppCar().getImages().size()>0){
    		for (int i = 0; i < car.getAppCar().getImages().size(); i++) {
    			AppCarImage carImage= car.getAppCar().getImages().get(i);
    			ObjectNode node=Json.newObject();
    			node.put("image", Play.application().configuration().getString("ippath")+Play.application().configuration().getString("outpath")+"/"+car.getAppCar().getImages().get(i).getUrl());
    			imagelist.add(node);
			}
		}
    	carNode.put("imagelist", imagelist);
    	returnJson.put("result", carNode);
    	returnJson.put("code", "0");
		returnJson.put("msg", "查询数据成功");
        return returnJson;
    }
}
