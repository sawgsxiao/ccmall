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
public class CustomerApplication extends Controller {
	
	public static Result customerAdd(){
		Customer guest = new Customer();
		return ok(customeradd.render(guest, "customer",""));
		
	}
	public static Result customerEdit(int id){
		
		Customer guest =Customer.findById(id);

    	return ok(customeredit.render(guest, "customer"));
    }
	
	public static Result customerDetailed(int id){
		
    	Customer guest = Customer.findById(id);

    	return ok(customerdetailed.render(guest, "customer"));
    }
	
	public static Result customerAddDo(){
		try{
		
			DynamicForm in = Form.form().bindFromRequest();
			
			String name=in.get("name");
			
			String identity=in.get("identity");
			
			String phone=in.get("phone");
			
			String email=in.get("email");
			
			String company=in.get("company");
			
			String entrytime=in.get("entrytime");
			
			String companyaddress=in.get("companyaddress");
			
			String job=in.get("job");
			
			String sex=in.get("sex");
			
			String salary=in.get("salary");
			
			String companyphone=in.get("companyphone");
			
			String address=in.get("address");
			
			String staytime=in.get("staytime");
			
			String issecurity=in.get("issecurity");
			
			String securitycompany=in.get("securitycompany");
			
			String marry=in.get("marry");
			
			String haschild=in.get("haschild");
			
			String spousename=in.get("spousename");
			
			String spousephone=in.get("spousephone");
			
			String housetype=in.get("housetype");
			
			String purchasetime=in.get("purchasetime");
			
			String price=in.get("price");
			
			String houseaddress=in.get("houseaddress");
			
			String cartype=in.get("cartype");
			
			String paidtype=in.get("paidtype");
			
			String fristmoney=in.get("fristmoney");
			
			String crmoney=in.get("fristmoney");
			
			String creffect=in.get("creffect");
			
			String crorigin=in.get("crorigin");
			
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			
			
			
			Customer guest=new Customer();
			guest.setName(name);
			guest.setIdentity(identity);
			guest.setCreatetime(new Date());
			guest.setSex(sex);
			guest.setType("0");
			guest.setPhone(phone);
			guest.setEmail(email);
			guest.setCompany(company);
			if(entrytime!=null&&!entrytime.equals("")){
				guest.setEntrytime(df.parse(entrytime));
			}
			guest.setCompanyaddress(companyaddress);
			guest.setJob(job);
			guest.setSalary(salary);
			guest.setCompanyphone(companyphone);
			guest.setAddress(address);
			if(staytime!=null&&!staytime.equals("")){
				guest.setStaytime(df.parse(staytime));
			}
			guest.setIssecurity(issecurity);
			guest.setSecuritycompany(securitycompany);
			guest.setMarry(marry);
			guest.setHaschild(haschild);
			guest.setSpousename(spousename);
			guest.setSpousephone(spousephone);
			guest.setCartype(cartype);
			guest.setPaidtype(paidtype);
			guest.setFristmoney(Integer.parseInt(fristmoney.equals("")?"0":fristmoney));
			guest.setIntegral("0");
			guest.setActualintegral("0");
			guest.setCrmoney(Integer.parseInt(crmoney.equals("")?"0":crmoney));
			guest.setCreffect(creffect);
			guest.setCrorigin(crorigin);
			//关系
			/*if(!type1.equals("")||!type2.equals("")||!workmate1.equals("")||!workmate2.equals("")
					||!friend1.equals("")||!friend2.equals("")){
				List<Contact> contacts=new ArrayList<Contact>();
				
				if(!type1.equals("")){
					Contact contact=new Contact();
					contact.setType("0");
					contact.setContactwith(type1);
					contact.setContactname(contactname1);
					contact.setCreatetime(new Date());
					contact.setPhone(phone1);
					contacts.add(contact);
				}
				
				if(!type2.equals("")){
					Contact contact=new Contact();
					contact.setType("0");
					contact.setContactwith(type2);
					contact.setContactname(contactname2);
					contact.setCreatetime(new Date());
					contact.setPhone(phone2);
					contacts.add(contact);
				}
				
				if(!workmate1.equals("")){
					Contact contact=new Contact();
					contact.setType("1");
					contact.setContactwith("同事");
					contact.setContactname(workmate1);
					contact.setCreatetime(new Date());
					contact.setPhone(workmatephone1);
					contacts.add(contact);
				}
				
				if(!workmate2.equals("")){
					Contact contact=new Contact();
					contact.setType("1");
					contact.setContactwith("同事");
					contact.setContactname(workmate2);
					contact.setCreatetime(new Date());
					contact.setPhone(workmatephone2);
					contacts.add(contact);
				}
				
				if(!friend1.equals("")){
					Contact contact=new Contact();
					contact.setType("2");
					contact.setContactwith("朋友");
					contact.setContactname(friend1);
					contact.setCreatetime(new Date());
					contact.setPhone(friendphone1);
					contacts.add(contact);
				}
				
				if(!friend2.equals("")){
					Contact contact=new Contact();
					contact.setType("2");
					contact.setContactwith("朋友");
					contact.setContactname(friend2);
					contact.setCreatetime(new Date());
					contact.setPhone(friendphone2);
					contacts.add(contact);
				}
				guest.setContacts(contacts);
			}*/
			
			//房产
			if(!housetype.equals("")){
				List<Asset> assets=new ArrayList<Asset>(); 
				Asset asset=new Asset();
				asset.setAddress(houseaddress);
				asset.setCreatetime(new Date());
				asset.setHousetype(housetype);
				if(purchasetime!=null&&!purchasetime.equals("")){
					asset.setPurchasetime(df.parse(purchasetime));
				}
				asset.setPrice(Integer.parseInt(price.equals("")?"0":price));
				assets.add(asset);
				guest.setAssets(assets);
			}
			
			//信用卡
			/*if(!bank1.equals("")||!bank2.equals("")||!bank3.equals("")||!bank4.equals("")
					||!bank5.equals("")){
				List<Creditcard> creditcards=new ArrayList<Creditcard>();
				if(!bank1.equals("")){
					Creditcard creditcard=new Creditcard();
					creditcard.setBank(bank1);
					creditcard.setCompany(inputcompany1);
					creditcard.setCreatetime(new Date());
					creditcard.setCreditquota(Integer.parseInt(creditquota1.equals("")?"0":creditquota1));
					creditcard.setQuotainuser(Integer.parseInt(quotainuser1.equals("")?"0":quotainuser1));
					creditcards.add(creditcard);
				}
				if(!bank2.equals("")){
					Creditcard creditcard=new Creditcard();
					creditcard.setBank(bank2);
					creditcard.setCompany(inputcompany2);
					creditcard.setCreatetime(new Date());
					creditcard.setCreditquota(Integer.parseInt(creditquota2.equals("")?"0":creditquota2));
					creditcard.setQuotainuser(Integer.parseInt(quotainuser2.equals("")?"0":quotainuser2));
					creditcards.add(creditcard);
				}
				if(!bank3.equals("")){
					Creditcard creditcard=new Creditcard();
					creditcard.setBank(bank3);
					creditcard.setCompany(inputcompany3);
					creditcard.setCreatetime(new Date());
					creditcard.setCreditquota(Integer.parseInt(creditquota3.equals("")?"0":creditquota3));
					creditcard.setQuotainuser(Integer.parseInt(quotainuser3.equals("")?"0":quotainuser3));
					creditcards.add(creditcard);
				}
				if(!bank4.equals("")){
					Creditcard creditcard=new Creditcard();
					creditcard.setBank(bank4);
					creditcard.setCompany(inputcompany4);
					creditcard.setCreatetime(new Date());
					creditcard.setCreditquota(Integer.parseInt(creditquota4.equals("")?"0":creditquota4));
					creditcard.setQuotainuser(Integer.parseInt(quotainuser4.equals("")?"0":quotainuser4));
					creditcards.add(creditcard);
				}
				if(!bank5.equals("")){
					Creditcard creditcard=new Creditcard();
					creditcard.setBank(bank5);
					creditcard.setCompany(inputcompany5);
					creditcard.setCreatetime(new Date());
					creditcard.setCreditquota(Integer.parseInt(creditquota5.equals("")?"0":creditquota5));
					creditcard.setQuotainuser(Integer.parseInt(quotainuser5.equals("")?"0":quotainuser5));
					creditcards.add(creditcard);
				}
				guest.setCreditcards(creditcards);
			}*/
			guest.save();
			Logger.info("新建成功。");
		}catch(Exception e){
			Logger.info("新建失败:e:"+e);
		}
		
		return ok(customeradd.render(new Customer(), "customer","success"));
	}
	public static Result customerEditDo(){
		try{
			
			DynamicForm in = Form.form().bindFromRequest();
			
			String customerid=in.get("customerid");
			
			Customer guest=Customer.findById(Integer.parseInt(customerid));
			
			String name=in.get("name");
			
			String identity=in.get("identity");
			
			String phone=in.get("phone");
			
			String email=in.get("email");
			
			String company=in.get("company");
			
			String entrytime=in.get("entrytime");
			
			String companyaddress=in.get("companyaddress");
			
			String job=in.get("job");
			
			String sex=in.get("sex");
			
			String salary=in.get("salary");
			
			String companyphone=in.get("companyphone");
			
			String address=in.get("address");
			
			String staytime=in.get("staytime");
			
			String issecurity=in.get("issecurity");
			
			String securitycompany=in.get("securitycompany");
			
			String marry=in.get("marry");
			
			String haschild=in.get("haschild");
			
			String spousename=in.get("spousename");
			
			String spousephone=in.get("spousephone");
			
			String housetype=in.get("housetype");
			
			String purchasetime=in.get("purchasetime");
			
			String price=in.get("price");
			
			String houseaddress=in.get("houseaddress");
			
			String cartype=in.get("cartype");
			
			String paidtype=in.get("paidtype");
			
			String fristmoney=in.get("fristmoney");
			
			String crmoney=in.get("fristmoney");
			
			String creffect=in.get("creffect");
			
			String crorigin=in.get("crorigin");
			
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			
			
			guest.setName(name);
			guest.setIdentity(identity);
			guest.setCreatetime(new Date());
			guest.setSex(sex);
			guest.setType("0");
			guest.setPhone(phone);
			guest.setEmail(email);
			guest.setCompany(company);
			if(entrytime!=null&&!entrytime.equals("")){
				guest.setEntrytime(df.parse(entrytime));
			}
			guest.setCompanyaddress(companyaddress);
			guest.setJob(job);
			guest.setSalary(salary);
			guest.setCompanyphone(companyphone);
			guest.setAddress(address);
			if(staytime!=null&&!staytime.equals("")){
				guest.setStaytime(df.parse(staytime));
			}
			guest.setIssecurity(issecurity);
			guest.setSecuritycompany(securitycompany);
			guest.setMarry(marry);
			guest.setHaschild(haschild);
			guest.setSpousename(spousename);
			guest.setSpousephone(spousephone);
			guest.setCartype(cartype);
			guest.setPaidtype(paidtype);
			guest.setFristmoney(Integer.parseInt(fristmoney.equals("")?"0":fristmoney));
			guest.setIntegral("0");
			guest.setActualintegral("0");
			guest.setCrmoney(Integer.parseInt(crmoney.equals("")?"0":crmoney));
			guest.setCreffect(creffect);
			guest.setCrorigin(crorigin);
			if(!housetype.equals("")){
				List<Asset> s=guest.getAssets();
				if(s.size()>0){
					for (int i = 0; i < s.size(); i++) {
						s.get(i).delete();
					}
				}
				List<Asset> assets=new ArrayList<Asset>(); 
				Asset asset=new Asset();
				asset.setAddress(houseaddress);
				asset.setCreatetime(new Date());
				asset.setHousetype(housetype);
				if(purchasetime!=null&&!purchasetime.equals("")){
					asset.setPurchasetime(df.parse(purchasetime));
				}
				asset.setPrice(Integer.parseInt(price.equals("")?"0":price));
				assets.add(asset);
				guest.setAssets(assets);
			}
			
			
			guest.update();
		}catch(Exception e){
			Logger.info("编辑失败:e:"+e);
		}  
		
		return customerList();
	}
	
	public static Result customerList() {
		
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
		
    	List<Customer> list = CustomerApplication.list(pageSize,pageNow,btn);
    	
    	//总数
		int countAll = CustomerApplication.count(btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      	
		return ok(customer.render(list,countAll,pageNow,pageCount,pageSize,"customer",btn));
		
    }
    
	public static List<InputBtn> createText(boolean my){
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	Map<String, String> data=in.data();
    	List<InputBtn> l=new ArrayList<InputBtn>();
    	if(!my){
    		InputBtn bt1=new InputBtn("params","params","","名称","");
    		

    		InputBtn bt2=new InputBtn("params0","params0","","身份证","");
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
	
    public static List<Customer> list(int pageSize,int pageNow,List<InputBtn>... btn){
		
		int start=(pageNow-1)*pageSize;
		
      	String paramsSql = "";
		
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (name like '%"+params+"%')";
      	}
      	
      	String params0="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(1).getValue()!=null&&!btn[0].get(1).getValue().equals("")){
      		params0=btn[0].get(1).getValue().trim();
      		paramsSql+=" and (identity like '%"+params0+"%')";
      	}
		//sql语句       
	 	String sql = "select id from cc_customer where 1=1 "+paramsSql+" order by createtime desc limit "+start+" ,"+pageSize;   
	
  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")   
  		.create();
  		
  		Query<Customer> eQ = Ebean.find(Customer.class);
  		eQ.setRawSql(rawSql);
  		
  		List<Customer> list = eQ.findList();

		return list;
    }
    
    public static int count(List<InputBtn>... btn){
		
      	String paramsSql = "";

      	
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (name like '%"+params+"%')";
      	}
      	
      	String params0="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(1).getValue()!=null&&!btn[0].get(1).getValue().equals("")){
      		params0=btn[0].get(1).getValue().trim();
      		paramsSql+=" and (identity like '%"+params0+"%')";
      	}
		//sql语句       
	 	String sql = "select count(id) as count from cc_customer where 1=1 "+paramsSql;  
     	
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }
    public static Result customerDel(int id){
    	Customer.findById(id).delete();
    	return customerList();
    }
    
    public static Result familyContactDo(int id){
    	String result;
    	DynamicForm in = Form.form().bindFromRequest();
    	String type=in.get("type");
    	String contactname=in.get("contactname");
    	String phone=in.get("phone");
    	String seq=in.get("seq");
    	Contact family=null;
    	try{
    		if(seq.equals("")){
		    	family=new Contact();
		    	family.setType("0");
		    	family.setContactname(contactname);
		    	family.setContactwith(type);
		    	family.setPhone(phone);
		    	family.setGuest(Customer.findById(id));
		    	family.setCreatetime(new Date());
		    	family.save();
    		}else{
	    		family=Contact.findById(Integer.parseInt(seq));
	    		family.setType("0");
		    	family.setContactname(contactname);
		    	family.setContactwith(type);
		    	family.setPhone(phone);
		    	family.setCreatetime(new Date());
		    	family.update();
    		}
	    	result="[{\"code\":\"1\",\"msg\":\"保存成功\",\"familyid\":\""+family.getId()+"\"}]";
    	}catch (Exception e) {
    		result="[{\"code\":\"0\",\"msg\":\"保存失败\"}]";
		}
    	return ok(result);
    }
    
    public static Result familyContactDel(int id){
    	String result;
    	DynamicForm in = Form.form().bindFromRequest();
    	try{
    		Contact.findById(id).delete();
    		result="[{\"code\":\"1\",\"msg\":\"删除成功\"}]";
    	}catch (Exception e) {
    		result="[{\"code\":\"0\",\"msg\":\"删除失败\"}]";
		}
    	return ok(result);
    }
    
    public static Result workmateContactDo(int id){
    	String result;
    	DynamicForm in = Form.form().bindFromRequest();
    	String contactname=in.get("contactname");
    	String phone=in.get("phone");
    	String seq=in.get("seq");
    	Contact workmate=null;
    	try{
    		if(seq.equals("")){
    			workmate=new Contact();
    			workmate.setType("1");
    			workmate.setContactname(contactname);
    			workmate.setContactwith("同事");
		    	workmate.setPhone(phone);
		    	workmate.setGuest(Customer.findById(id));
		    	workmate.setCreatetime(new Date());
		    	workmate.save();
    		}else{
    			workmate=Contact.findById(Integer.parseInt(seq));
    			workmate.setType("1");
	    		workmate.setContactname(contactname);
	    		workmate.setPhone(phone);
	    		workmate.setCreatetime(new Date());
	    		workmate.update();
    		}
	    	result="[{\"code\":\"1\",\"msg\":\"保存成功\",\"workmateid\":\""+workmate.getId()+"\"}]";
    	}catch (Exception e) {
    		result="[{\"code\":\"0\",\"msg\":\"保存失败\"}]";
		}
    	return ok(result);
    }
    
    public static Result friendContactDo(int id){
    	String result;
    	DynamicForm in = Form.form().bindFromRequest();
    	String contactname=in.get("contactname");
    	String phone=in.get("phone");
    	String seq=in.get("seq");
    	Contact friend=null;
    	try{
    		if(seq.equals("")){
    			friend=new Contact();
    			friend.setType("2");
    			friend.setContactname(contactname);
    			friend.setContactwith("朋友");
    			friend.setPhone(phone);
    			friend.setGuest(Customer.findById(id));
    			friend.setCreatetime(new Date());
    			friend.save();
    		}else{
    			friend=Contact.findById(Integer.parseInt(seq));
    			friend.setType("2");
    			friend.setContactname(contactname);
    			friend.setPhone(phone);
    			friend.setCreatetime(new Date());
    			friend.update();
    		}
	    	result="[{\"code\":\"1\",\"msg\":\"保存成功\",\"friendid\":\""+friend.getId()+"\"}]";
    	}catch (Exception e) {
    		result="[{\"code\":\"0\",\"msg\":\"保存失败\"}]";
		}
    	return ok(result);
    }
    
    public static Result creditcardDel(int id){
    	String result;
    	DynamicForm in = Form.form().bindFromRequest();
    	try{
    		Creditcard.findById(id).delete();
    		result="[{\"code\":\"1\",\"msg\":\"删除成功\"}]";
    	}catch (Exception e) {
    		result="[{\"code\":\"0\",\"msg\":\"删除失败\"}]";
		}
    	return ok(result);
    }
    
    public static Result creditcardDo(int id){
    	String result;
    	DynamicForm in = Form.form().bindFromRequest();
    	String bank=in.get("bank");
    	String creditquota=in.get("creditquota");
    	String quotainuser=in.get("quotainuser");
    	String company=in.get("company");
    	String seq=in.get("seq");
    	Creditcard card=null;
    	try{
    		if(seq.equals("")){
    			card=new Creditcard();
    			card.setBank(bank);
    			card.setCompany(company);
    			card.setCreditquota(Integer.parseInt(creditquota));
    			card.setQuotainuser(Integer.parseInt(quotainuser));
    			card.setGuest(Customer.findById(id));
    			card.setCreatetime(new Date());
    			card.save();
    		}else{
    			card=Creditcard.findById(Integer.parseInt(seq));
    			card.setBank(bank);
    			card.setCompany(company);
    			card.setCreditquota(Integer.parseInt(creditquota));
    			card.setQuotainuser(Integer.parseInt(quotainuser));
    			card.setCreatetime(new Date());
    			card.update();
    		}
	    	result="[{\"code\":\"1\",\"msg\":\"保存成功\",\"creditcardid\":\""+card.getId()+"\"}]";
    	}catch (Exception e) {
    		result="[{\"code\":\"0\",\"msg\":\"保存失败\"}]";
		}
    	return ok(result);
    }
    
    
    public static Result customerListJson(){
    	
    	
    	String sql=" select * from cc_customer order by createtime desc limit 0,20";
    	
    	RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")   
  		.create();
  		
  		Query<Customer> eQ = Ebean.find(Customer.class);
  		eQ.setRawSql(rawSql);
  		
  		List<Customer> result = eQ.findList();
    	
    	ArrayNode jsonArray = JsonNodeFactory.instance.arrayNode();
		ObjectNode jsonObject;
		for(int i = 0; i < result.size(); i++){
			jsonObject = Json.newObject();
			jsonObject.put("pid", 0);
			jsonObject.put("id", result.get(i).getId());
			jsonObject.put("name", result.get(i).getName());
			jsonArray.add(jsonObject);
		}
		return ok(jsonArray.toString());
    }
    
    public static Result customerListJsonParam(){
    	
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	String name=in.get("username");
    	    	
    	String paramsql="";
    	if(name!=null&&!name.equals("")){
    		paramsql=" and name like '%"+name+"%'";
    	}
    	
    	String sql=" select * from cc_customer where 1=1 "+paramsql+"  order by createtime desc limit 0,20";
    	
    	RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")   
  		.create();
  		
  		Query<Customer> eQ = Ebean.find(Customer.class);
  		eQ.setRawSql(rawSql);
  		
  		List<Customer> result = eQ.findList();
    	ArrayNode jsonArray = JsonNodeFactory.instance.arrayNode();
		ObjectNode jsonObject;
		for(int i = 0; i < result.size(); i++){
			jsonObject = Json.newObject();
			jsonObject.put("pid", 0);
			jsonObject.put("id", result.get(i).getId());
			jsonObject.put("name", result.get(i).getName());
			jsonArray.add(jsonObject);
		}
		return ok(jsonArray.toString());
    }
    
}
