package controllers;

import java.util.List;
import java.util.Map;

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
    		node.put("name", city.getNetid());
    		arrayNode.add(node);
		}
    	returnJson.put("list", arrayNode);
    	returnJson.put("code", "0");
		returnJson.put("msg", "查询数据成功");
        return returnJson;
    }
    
    public static Result test() {
        return ok(test.render());
    }

}
