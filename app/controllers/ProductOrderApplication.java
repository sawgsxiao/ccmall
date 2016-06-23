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

import common.OrderThread;

import java.util.ArrayList;
import java.util.List;
import models.*;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
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

import play.db.ebean.Model;


@Security.Authenticated(Secured.class)
public class ProductOrderApplication extends Controller {
	
	public static Result carorderAdd() {
		
		List<Customer> customers= Customer.findAllList();
		
		List<CarAssort> assorts=CarAssort.findAllList();
		
		List<Car> cars=Car.findList("1");
		
		return ok(carorderadd.render(customers,assorts,cars,"carorder",""));
		
	}
	
	public static Result carorderAddDo() {
		
		List<Customer> customers= Customer.findAllList();
		
		List<CarAssort> assorts=CarAssort.findAllList();
		
		List<Car> cars=Car.findList("1");
		
		
		ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	String customerid = (String)in.get("customerid");
    	String name = (String)in.get("name");
    	String remainphone = (String)in.get("remainphone");
    	String deliveryaddress = (String)in.get("deliveryaddress");
    	
    	String paid=(String)in.get("paid");
    	List<String> strings=new ArrayList<String>();
    	Set<String> set= in.data().keySet();
    	Pattern pattern=Pattern.compile("^carid\\d+$"); 
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			if(pattern.matcher(string).find()){
				strings.add(string);
			}
		}
		Object[] array=strings.toArray();
		
		CarOrder carOrder=new CarOrder();
		carOrder.setOrdercode("");
		carOrder.setRemainphone(remainphone);
		carOrder.setPaid(paid);
		carOrder.setIncome(session("departid"));
		carOrder.setCreator(session("userid"));
		carOrder.setStatus("0");
		carOrder.setCustomerid(customerid);
		carOrder.setCustomername(Customer.findById(Integer.parseInt(customerid)).getName());
		carOrder.setDeliveryaddress(deliveryaddress);
		carOrder.setCreatetime(new Date());
		List<CarOrderDetail> details=new ArrayList<CarOrderDetail>();
		if(array.length>0){
			for (int i = 0; i < array.length; i++) {
				Car car= Car.findById(Integer.parseInt(in.get(array[i]+""))); 
				CarOrderDetail orderDetail=new CarOrderDetail();
				orderDetail.setCount("1");
				orderDetail.setCarid(car.getId()+"");
				orderDetail.setCreatetime(new Date());
				orderDetail.setProductcode(car.getProductcode());
				orderDetail.setPrice(car.getPricerange());
				orderDetail.setAssortname(car.getAssortname());
				orderDetail.setProductassortid(car.getAssortcode());
				orderDetail.setProductname(car.getCarname());
				details.add(orderDetail);
			}
			carOrder.setDetails(details);
		}
		carOrder.save();
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
		String Ordercode=df.format(new Date())+String.format("%012d", carOrder.getId());
		carOrder.setOrdercode(Ordercode);
		carOrder.update();
		CarOrderLog log=new CarOrderLog();
		log.setCreatetime(new Date());
		log.setOperate(session("name")+":新增订单!");
		log.setStatus("0");
		log.setUserid(session("userid"));
		log.setOrderid(carOrder.getId()+"");
		return ok(carorderadd.render(customers,assorts,cars,"carorder","添加成功!"));
		
	}
	
	
	public static Result carorderList() {
		
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
		
    	List<CarOrder> list = ProductOrderApplication.carorderlist(pageSize,pageNow,"",btn);
    	
    	//总数
		int countAll = ProductOrderApplication.carordercount("",btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      	
		return ok(carorder.render(list,countAll,pageNow,pageCount,pageSize,"carorder",btn));
		
    }
    
	public static List<InputBtn> createText(boolean my){
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	Map<String, String> data=in.data();
    	List<InputBtn> l=new ArrayList<InputBtn>();
    	if(!my){
    		InputBtn bt1=new InputBtn("params","params","","订单号","");
    		

    		InputBtn bt2=new InputBtn("params0","params0","","客户名称","");
    		
    		InputBtn bt3=new InputBtn("params1","params1","","预留电话","");
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
	
    public static List<CarOrder> carorderlist(int pageSize,int pageNow,String flag,List<InputBtn>... btn){
		
		int start=(pageNow-1)*pageSize;
		
      	String paramsSql = "";
      	
      	if(!session("departid").equals("5")){
      		paramsSql=" and ((income= "+session("departid")+") or (creator='"+session("userid")+"'))";
      	}
      	
      	if(!flag.equals("")){
      		paramsSql+=" and status in ("+flag+")";
      	}
		
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (ordercode = '"+params+"')";
      	}
      	
      	String params0="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(1).getValue()!=null&&!btn[0].get(1).getValue().equals("")){
      		params0=btn[0].get(1).getValue().trim();
      		paramsSql+=" and (customername like '%"+params0+"%')";
      	}
      	String params1="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(2).getValue()!=null&&!btn[0].get(2).getValue().equals("")){
      		params1=btn[0].get(2).getValue().trim();
      		paramsSql+=" and (remainphone like '%"+params1+"%')";
      	}
		//sql语句       
	 	String sql = "select id from cc_car_order where 1=1 "+paramsSql+" order by createtime desc limit "+start+" ,"+pageSize;   
	
  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")   
  		.create();
  		
  		Query<CarOrder> eQ = Ebean.find(CarOrder.class);
  		eQ.setRawSql(rawSql);
  		
  		List<CarOrder> list = eQ.findList();

		return list;
    }
    
    public static int carordercount(String flag,List<InputBtn>... btn){
		
      	String paramsSql = "";

      	if(!session("departid").equals("5")){
      		paramsSql=" and ((income= "+session("departid")+") or (creator='"+session("userid")+"'))";      	}
      	
      	if(!flag.equals("")){
      		paramsSql+=" and status in ("+flag+")";
      	}
      	
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (ordercode = '"+params+"')";
      	}
      	
      	String params0="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(1).getValue()!=null&&!btn[0].get(1).getValue().equals("")){
      		params0=btn[0].get(1).getValue().trim();
      		paramsSql+=" and (customername like '%"+params0+"%')";
      	}
      	String params1="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(2).getValue()!=null&&!btn[0].get(2).getValue().equals("")){
      		params1=btn[0].get(2).getValue().trim();
      		paramsSql+=" and (remainphone like '%"+params1+"%')";
      	}
		//sql语句       
	 	String sql = "select count(id) as count from cc_car_order where 1=1 "+paramsSql;  
     	
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }
    
    public static Result carorderDetailed(int id) {
		
		CarOrder carOrder=CarOrder.findById(id);

		Customer customer= Customer.findById(Integer.parseInt(carOrder.getCustomerid()));
		
		
		return ok(carorderdetail.render(customer,carOrder,"carorder"));
		
	}
    
	public static Result carorderkfList() {
		
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
		
    	List<CarOrder> list = ProductOrderApplication.carorderforlist(pageSize,pageNow," and ((status=0) or(status=1 and kf="+session("userid")+"))",btn);
    	
    	//总数
		int countAll = ProductOrderApplication.carorderforcount(" and ((status=0) or(status=1 and kf="+session("userid")+"))",btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      	
		return ok(carorderkf.render(list,countAll,pageNow,pageCount,pageSize,"carorder",btn));
		
    }
	
	public static Result rececarorder(int id,String status) {
		
		String forward = "";
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
		    	
		CarOrder carOrder=CarOrder.findById(id);
		
		CarOrderLog log=new CarOrderLog();
		if(status.equals("1")){
			carOrder.setKf(session("userid"));
			
			carOrder.setKfname(session("name"));
			
			forward="/carorderkfList";
			log.setOperate("机构客服("+session("name")+")接单!");
		}else if(status.equals("3")){
			carOrder.setXs(session("userid"));
			
			carOrder.setXsname(session("name"));
			forward="/carorderxsList";
			log.setOperate("总部客服("+session("name")+")接单!");
		}else if(status.equals("5")){
			carOrder.setYw(session("userid"));
			
			carOrder.setYwname(session("name"));
			forward="/carorderdkList";
			log.setOperate("银行贷款("+session("name")+")接单!");
		}
		
		carOrder.setKfdepart(session("departid"));
		carOrder.setKfdpname(Department.findById(Integer.parseInt(session("departid"))).getName());
		carOrder.setStatus(status);
		
		carOrder.update();
		
		log.setCreatetime(new Date());
		
		log.setStatus(status);
		log.setUserid(session("userid"));
		log.setOrderid(carOrder.getId()+"");
		log.save();
		return redirect(forward);
		
	}
	

    public static List<CarOrder> carorderforlist(int pageSize,int pageNow,String flag,List<InputBtn>... btn){
		
		int start=(pageNow-1)*pageSize;
		
      	String paramsSql = "";
      	
      	if(!session("departid").equals("5")){
      		paramsSql=" and ((income= "+session("departid")+") or (creator='"+session("userid")+"'))";      	}
      	
      	if(!flag.equals("")){
      		paramsSql+=flag;
      	}
		
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (ordercode = '"+params+"')";
      	}
      	
      	String params0="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(1).getValue()!=null&&!btn[0].get(1).getValue().equals("")){
      		params0=btn[0].get(1).getValue().trim();
      		paramsSql+=" and (customername like '%"+params0+"%')";
      	}
      	String params1="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(2).getValue()!=null&&!btn[0].get(2).getValue().equals("")){
      		params1=btn[0].get(2).getValue().trim();
      		paramsSql+=" and (remainphone like '%"+params1+"%')";
      	}
		//sql语句       
	 	String sql = "select id from cc_car_order where 1=1 "+paramsSql+" order by createtime desc limit "+start+" ,"+pageSize;   
	
  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")   
  		.create();
  		
  		Query<CarOrder> eQ = Ebean.find(CarOrder.class);
  		eQ.setRawSql(rawSql);
  		
  		List<CarOrder> list = eQ.findList();

		return list;
    }
    
    public static int carorderforcount(String flag,List<InputBtn>... btn){
		
      	String paramsSql = "";
      	
      	if(!session("departid").equals("5")){
      		paramsSql=" and ((income= "+session("departid")+") or (creator='"+session("userid")+"'))";
      	}
      	
      	if(!flag.equals("")){
      		paramsSql+=flag;
      	}
      	
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (ordercode = '"+params+"')";
      	}
      	
      	String params0="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(1).getValue()!=null&&!btn[0].get(1).getValue().equals("")){
      		params0=btn[0].get(1).getValue().trim();
      		paramsSql+=" and (customername like '%"+params0+"%')";
      	}
      	String params1="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(2).getValue()!=null&&!btn[0].get(2).getValue().equals("")){
      		params1=btn[0].get(2).getValue().trim();
      		paramsSql+=" and (remainphone like '%"+params1+"%')";
      	}
		//sql语句       
	 	String sql = "select count(id) as count from cc_car_order where 1=1 "+paramsSql;  
     	
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }
	
    public static Result carorderFlowView(int id) {
		
		CarOrder carOrder=CarOrder.findById(id);

		Customer customer= Customer.findById(Integer.parseInt(carOrder.getCustomerid()));
		
		
		return ok(carorderpanel.render(customer,carOrder,"carorder"));
		
	}
    
    
    public static Result carorderTrackIn(int id) {
		
    	ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	Date createtime=new Date();
    	
    	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	
    	String code="";
    	try{
    		
			CarOrder carOrder=CarOrder.findById(id);
			
			String content=in.get("content");
			
			String type=in.get("type");
						
			String orderstatus=in.get("orderstatus");
			
			List<CarOrderTrack> tracks=new ArrayList<CarOrderTrack>();
			
			CarOrderTrack track=new CarOrderTrack();
			
			track.setCarOrder(carOrder);
			
			track.setContent(content);
			
			track.setCreatetime(createtime);
			
			track.setOrderstatus(orderstatus);
			
			track.setType(type);
			
			track.setUname(session("name"));
			
			track.setUserid(session("userid"));
			
			track.save();
			
			code="1";
			
    	}catch (Exception e) {
    		code="0";
		}
		
		return ok("{\"code\":\""+code+"\",\"ctime\":\""+df.format(createtime)+"\"}");
		
	}
    
    public static Result carordercheck(int id,String status) {
		
    	String forward="";
    	
		CarOrder carOrder=CarOrder.findById(id);
		
		carOrder.setStatus(status);
		
		carOrder.update();
		
		CarOrderLog log=new CarOrderLog();
		log.setCreatetime(new Date());
		
		
		if(status.equals("2")){
			forward="/carorderkfList";
			log.setOperate("机构客服("+session("name")+")处理订单!");
		}else if(status.equals("4")){
			forward="/carorderxsList";
			log.setOperate("总部客服("+session("name")+")处理订单!");
		}else if(status.equals("6")){
			forward="/carorderdkList";
			log.setOperate("银行贷款("+session("name")+")处理订单!");
		}
		
		log.setStatus(status);
		log.setUserid(session("userid"));
		log.setOrderid(carOrder.getId()+"");
		log.save();
		
		return redirect(forward);
		
	}
    
	public static Result carorderxsList() {
		
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
		
    	List<CarOrder> list = ProductOrderApplication.carorderforlist(pageSize,pageNow," and ((status=2) or(status=3 and xs="+session("userid")+"))",btn);
    	
    	//总数
		int countAll = ProductOrderApplication.carorderforcount(" and ((status=2) or(status=3 and xs="+session("userid")+"))",btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      	
		return ok(carorderxs.render(list,countAll,pageNow,pageCount,pageSize,"carorder",btn));
		
    }
	
	public static Result carorderdkList() {
		
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
		
    	List<CarOrder> list = ProductOrderApplication.carorderforlist(pageSize,pageNow," and ((status=4) or(status=5 and yw="+session("userid")+"))",btn);
    	
    	//总数
		int countAll = ProductOrderApplication.carorderforcount(" and ((status=4) or(status=5 and yw="+session("userid")+"))",btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      	
		return ok(carorderdk.render(list,countAll,pageNow,pageCount,pageSize,"carorder",btn));
		
    }
	
	public static Result carorderTrackShow(int id) {
		
		CarOrder carOrder=CarOrder.findById(id);

		Customer customer= Customer.findById(Integer.parseInt(carOrder.getCustomerid()));
		
		
		return ok(carordertrackshow.render(customer,carOrder,"carorder"));
		
	}
	
	
    public static Result carorderFlowDetailed(int id) {
		
		CarOrder carOrder=CarOrder.findById(id);

		Customer customer= Customer.findById(Integer.parseInt(carOrder.getCustomerid()));
		
		return ok(carorderflowdetail.render(customer,carOrder,"carorder"));
		
	}
	
    public static Result carorderEdit(int id) {
		
		CarOrder carOrder=CarOrder.findById(id);

		Customer customer= Customer.findById(Integer.parseInt(carOrder.getCustomerid()));
		
		List<CarAssort> assorts=CarAssort.findAllList();
		
		List<Car> cars=Car.findList("1");
		List<Customer> customers= Customer.findAllList();
		return ok(carorderedit.render(customer,carOrder,assorts,cars,customers,"carorder"));
		
	}
    
	public static Result carorderEditDo() {
		
		List<Customer> customers= Customer.findAllList();
		
		List<CarAssort> assorts=CarAssort.findAllList();
		
		List<Car> cars=Car.findList("1");
		
		
		ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	String id = (String)in.get("orderid");
    	String customerid = (String)in.get("customerid");
    	String name = (String)in.get("name");
    	String remainphone = (String)in.get("remainphone");
    	String deliveryaddress = (String)in.get("deliveryaddress");
    	
    	String paid=(String)in.get("paid");
    	List<String> strings=new ArrayList<String>();
    	Set<String> set= in.data().keySet();
    	Pattern pattern=Pattern.compile("^carid\\d+$"); 
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			if(pattern.matcher(string).find()){
				strings.add(string);
			}
		}
		Object[] array=strings.toArray();
		
		CarOrder carOrder=CarOrder.findById(Integer.parseInt(id));
		carOrder.setRemainphone(remainphone);
		carOrder.setPaid(paid);
		carOrder.setCustomerid(customerid);
		carOrder.setCustomername(Customer.findById(Integer.parseInt(customerid)).getName());
		carOrder.setDeliveryaddress(deliveryaddress);
		List<CarOrderDetail> carorderdetails= carOrder.getDetails();
		if(carorderdetails!=null&&carorderdetails.size()>0){
			for (CarOrderDetail carOrderDetail : carorderdetails) {
				carOrderDetail.delete();
			}
		}
		List<CarOrderDetail> details=new ArrayList<CarOrderDetail>();
		if(array.length>0){
			for (int i = 0; i < array.length; i++) {
				Car car= Car.findById(Integer.parseInt(in.get(array[i]+""))); 
				CarOrderDetail orderDetail=new CarOrderDetail();
				orderDetail.setCount("1");
				orderDetail.setCarid(car.getId()+"");
				orderDetail.setCreatetime(new Date());
				orderDetail.setProductcode(car.getProductcode());
				orderDetail.setPrice(car.getPricerange());
				orderDetail.setAssortname(car.getAssortname());
				orderDetail.setProductassortid(car.getAssortcode());
				orderDetail.setProductname(car.getCarname());
				details.add(orderDetail);
			}
			carOrder.setDetails(details);
		}
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
		carOrder.update();
		CarOrderLog log=new CarOrderLog();
		log.setCreatetime(new Date());
		log.setStatus(carOrder.getStatus());
		log.setUserid(session("userid"));
		log.setOrderid(carOrder.getId()+"");
		String forward = null;
		String status=carOrder.getStatus();
		if(status.equals("1")){
			forward="/carorderkfList";
			log.setOperate("机构客服("+session("name")+")编辑订单!");
		}else if(status.equals("3")){
			forward="/carorderxsList";
			log.setOperate("总部客服("+session("name")+")编辑订单!");
		}else if(status.equals("5")){
			forward="/carorderdkList";
			log.setOperate("银行贷款("+session("name")+")编辑订单!");
		}
		log.save();
		return redirect(forward);
		
	}
 
	
	public static Result carorderbackList() {
		
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
		
    	List<CarOrder> list = ProductOrderApplication.carorderforlist(pageSize,pageNow," and ((status=7))",btn);
    	
    	//总数
		int countAll = ProductOrderApplication.carorderforcount(" and ((status=7))",btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      	
		return ok(carorderback.render(list,countAll,pageNow,pageCount,pageSize,"carorder",btn));
		
    }
	
    public static Result carorderstop(int id,String status,String checkstatus) {
		
    	String forward="";
    	
		CarOrder carOrder=CarOrder.findById(id);
		
		carOrder.setStatus(checkstatus);
		
		carOrder.update();
		
		CarOrderLog log=new CarOrderLog();
		log.setCreatetime(new Date());
		
		
		if(status.equals("2")){
			forward="/carorderkfList";
			log.setOperate("机构客服("+session("name")+")终止订单!");
		}else if(status.equals("4")){
			forward="/carorderxsList";
			log.setOperate("总部客服("+session("name")+")终止订单!");
		}else if(status.equals("6")){
			forward="/carorderdkList";
			log.setOperate("银行贷款("+session("name")+")终止订单!");
		}
		
		log.setStatus(checkstatus);
		log.setUserid(session("userid"));
		log.setOrderid(carOrder.getId()+"");
		log.save();
		
		return redirect(forward);
		
	}
    
    public static Result carorderback(int id) {
		
    	String forward="";
    	
		CarOrder carOrder=CarOrder.findById(id);
		
		carOrder.setStatus("0");
		
		carOrder.update();
		
		CarOrderLog log=new CarOrderLog();
		log.setCreatetime(new Date());
		
		
		
		forward="/carorderbackList";
		log.setOperate("工作人员("+session("name")+")重置订单!");
		
		
		log.setStatus("0");
		log.setUserid(session("userid"));
		log.setOrderid(carOrder.getId()+"");
		log.save();
		
		return redirect(forward);
		
	}
	/*
	 * 保险订单
	 */
	public static Result suranceorderList() {
		
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
		
    	List<SuranceOrder> list = ProductOrderApplication.suranceorderlist(pageSize,pageNow,"",btn);
    	
    	//总数
		int countAll = ProductOrderApplication.suranceordercount("",btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      	
		return ok(suranceorder.render(list,countAll,pageNow,pageCount,pageSize,"suranceorder",btn));
		
    }
    
	
    public static List<SuranceOrder> suranceorderlist(int pageSize,int pageNow,String flag,List<InputBtn>... btn){
		
		int start=(pageNow-1)*pageSize;
		
      	String paramsSql = "";
      	
      	if(!flag.equals("")){
      		paramsSql+=" and status in ("+flag+")";
      	}
		
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (ordercode = '"+params+"')";
      	}
      	
      	String params0="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(1).getValue()!=null&&!btn[0].get(1).getValue().equals("")){
      		params0=btn[0].get(1).getValue().trim();
      		paramsSql+=" and (customername like '%"+params0+"%')";
      	}
      	String params1="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(2).getValue()!=null&&!btn[0].get(2).getValue().equals("")){
      		params1=btn[0].get(2).getValue().trim();
      		paramsSql+=" and (remainphone like '%"+params1+"%')";
      	}
		//sql语句       
	 	String sql = "select id from cc_surance_order where 1=1 "+paramsSql+" order by createtime desc limit "+start+" ,"+pageSize;   
	
  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")   
  		.create();
  		
  		Query<SuranceOrder> eQ = Ebean.find(SuranceOrder.class);
  		eQ.setRawSql(rawSql);
  		
  		List<SuranceOrder> list = eQ.findList();

		return list;
    }
    
    public static int suranceordercount(String flag,List<InputBtn>... btn){
		
      	String paramsSql = "";

      	
      	if(!flag.equals("")){
      		paramsSql+=" and status in ("+flag+")";
      	}
      	
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (ordercode = '"+params+"')";
      	}
      	
      	String params0="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(1).getValue()!=null&&!btn[0].get(1).getValue().equals("")){
      		params0=btn[0].get(1).getValue().trim();
      		paramsSql+=" and (customername like '%"+params0+"%')";
      	}
      	String params1="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(2).getValue()!=null&&!btn[0].get(2).getValue().equals("")){
      		params1=btn[0].get(2).getValue().trim();
      		paramsSql+=" and (remainphone like '%"+params1+"%')";
      	}
		//sql语句       
	 	String sql = "select count(id) as count from cc_surance_order where 1=1 "+paramsSql;  
     	
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }
    
	public static Result suranceorderAdd() {
		
		List<Customer> customers= Customer.findAllList();
		
		List<SuranceAssort> assorts=SuranceAssort.findAllList();
		
		List<Surance> surances=Surance.findAllList();
		
		if(surances.size()>0){
			for (int i = 0; i < surances.size(); i++) {
				if(surances.get(i).getStand()==0){
					surances.remove(surances.get(i));
				}
			}
		}
		
		return ok(suranceorderadd.render(customers,assorts,surances,"suranceorder",""));
		
	}
	
	public static Result suranceorderAddDo() {
		
		List<Customer> customers= Customer.findAllList();
		
		List<SuranceAssort> assorts=SuranceAssort.findAllList();
		
		List<Surance> surances=Surance.findAllList();
		
		if(surances.size()>0){
			for (int i = 0; i < surances.size(); i++) {
				if(surances.get(i).getStand()==0){
					surances.remove(surances.get(i));
				}
			}
		}
		
		
		ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	String customerid = (String)in.get("customerid");
    	String name = (String)in.get("name");
    	String remainphone = (String)in.get("remainphone");
    	String paid = (String)in.get("paid");
    	
    	List<String> strings=new ArrayList<String>();
    	Set<String> set= in.data().keySet();
    	Pattern pattern=Pattern.compile("^suranceid\\d+$"); 
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			if(pattern.matcher(string).find()){
				strings.add(string);
			}
		}
		Object[] array=strings.toArray();
		
		SuranceOrder suranceOrder=new SuranceOrder();
		suranceOrder.setOrdercode("");
		suranceOrder.setPaid(paid);
		suranceOrder.setRemainphone(remainphone);
		suranceOrder.setStatus("0");
		suranceOrder.setCustomerid(customerid);
		suranceOrder.setCustomername(Customer.findById(Integer.parseInt(customerid)).getName());
		suranceOrder.setCreatetime(new Date());
		List<SuranceOrderDetail> details=new ArrayList<SuranceOrderDetail>();
		if(array.length>0){
			for (int i = 0; i < array.length; i++) {
				Surance surance= Surance.findById(Integer.parseInt(in.get(array[i]+""))); 
				SuranceOrderDetail orderDetail=new SuranceOrderDetail();
				orderDetail.setCount("1");
				orderDetail.setCreatetime(new Date());
				orderDetail.setProductcode(surance.getProductcode());
				orderDetail.setPrice(surance.getPrice());
				orderDetail.setAssortname(surance.getAssortname());
				orderDetail.setAssortid(surance.getAssortcode());
				orderDetail.setProductname(surance.getSurancename());
				orderDetail.setInusetime(surance.getInusetime());
				details.add(orderDetail);
			}
			suranceOrder.setDetails(details);
		}
		suranceOrder.save();
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
		String Ordercode=df.format(new Date())+String.format("%012d", suranceOrder.getId());
		suranceOrder.setOrdercode(Ordercode);
		suranceOrder.update();
		SuranceOrderLog log=new SuranceOrderLog();
		log.setCreatetime(new Date());
		log.setOperate(session("name")+":新增订单!");
		log.setStatus("0");
		log.setUserid(session("userid"));
		log.setOrderid(suranceOrder.getId()+"");
		return ok(suranceorderadd.render(customers,assorts,surances,"suranceorder","添加成功!"));
		
	}
	
    public static Result suranceorderDetailed(int id) {
		
		SuranceOrder suranceOrder=SuranceOrder.findById(id);

		Customer customer= Customer.findById(Integer.parseInt(suranceOrder.getCustomerid()));
		
		
		return ok(suranceorderdetail.render(customer,suranceOrder,"suranceorder"));
		
	}
    
	public static Result suranceorderkfList() {
		
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
		
    	List<SuranceOrder> list = ProductOrderApplication.suranceorderforlist(pageSize,pageNow," and ((status=0) or(status=1 and kf="+session("userid")+"))",btn);
    	
    	//总数
		int countAll = ProductOrderApplication.suranceorderforcount(" and ((status=0) or(status=1 and kf="+session("userid")+"))",btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      	
		return ok(suranceorderkf.render(list,countAll,pageNow,pageCount,pageSize,"suranceorder",btn));
		
    }
	
    public static List<SuranceOrder> suranceorderforlist(int pageSize,int pageNow,String flag,List<InputBtn>... btn){
		
		int start=(pageNow-1)*pageSize;
		
      	String paramsSql = "";
      	
      	if(!flag.equals("")){
      		paramsSql+=flag;
      	}
		
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (ordercode = '"+params+"')";
      	}
      	
      	String params0="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(1).getValue()!=null&&!btn[0].get(1).getValue().equals("")){
      		params0=btn[0].get(1).getValue().trim();
      		paramsSql+=" and (customername like '%"+params0+"%')";
      	}
      	String params1="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(2).getValue()!=null&&!btn[0].get(2).getValue().equals("")){
      		params1=btn[0].get(2).getValue().trim();
      		paramsSql+=" and (remainphone like '%"+params1+"%')";
      	}
		//sql语句       
	 	String sql = "select id from cc_surance_order where 1=1 "+paramsSql+" order by createtime desc limit "+start+" ,"+pageSize;   
	
  		RawSql rawSql = RawSqlBuilder.unparsed(sql)
  		.columnMapping("id",  "id")   
  		.create();
  		
  		Query<SuranceOrder> eQ = Ebean.find(SuranceOrder.class);
  		eQ.setRawSql(rawSql);
  		
  		List<SuranceOrder> list = eQ.findList();

		return list;
    }
    
    public static int suranceorderforcount(String flag,List<InputBtn>... btn){
		
      	String paramsSql = "";
      	
      	if(!flag.equals("")){
      		paramsSql+=flag;
      	}
      	
      	String params="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(0).getValue()!=null&&!btn[0].get(0).getValue().equals("")){
      		params=btn[0].get(0).getValue().trim();
      		paramsSql+=" and (ordercode = '"+params+"')";
      	}
      	
      	String params0="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(1).getValue()!=null&&!btn[0].get(1).getValue().equals("")){
      		params0=btn[0].get(1).getValue().trim();
      		paramsSql+=" and (customername like '%"+params0+"%')";
      	}
      	String params1="";
      	if(btn.length>0&&btn[0].size()>0&&btn[0].get(2).getValue()!=null&&!btn[0].get(2).getValue().equals("")){
      		params1=btn[0].get(2).getValue().trim();
      		paramsSql+=" and (remainphone like '%"+params1+"%')";
      	}
		//sql语句       
	 	String sql = "select count(id) as count from cc_surance_order where 1=1 "+paramsSql;  
     	
        SqlRow row = Ebean.createSqlQuery(sql).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }
    
    public static Result suranceorderFlowView(int id) {
		
		SuranceOrder suranceorder=SuranceOrder.findById(id);

		Customer customer= Customer.findById(Integer.parseInt(suranceorder.getCustomerid()));
		
		
		return ok(suranceorderpanel.render(customer,suranceorder,"suranceorder"));
		
	}
    
	public static Result recesuranceorder(int id,String status) {
		
		String forward = "";
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
		    	
		SuranceOrder suranceOrder=SuranceOrder.findById(id);
		
		SuranceOrderLog log=new SuranceOrderLog();
		if(status.equals("1")){
			suranceOrder.setKf(session("userid"));
			
			suranceOrder.setKfname(session("name"));
			forward="/suranceorderkfList";
			log.setOperate("客服("+session("name")+")接单!");
		}else if(status.equals("3")){
			suranceOrder.setXs(session("userid"));
			
			suranceOrder.setXsname(session("name"));
			forward="/suranceorderxsList";
			log.setOperate("销售("+session("name")+")接单!");
		}
		
		
		suranceOrder.setStatus(status);
		
		suranceOrder.update();
		
		log.setCreatetime(new Date());
		
		log.setStatus(status);
		log.setUserid(session("userid"));
		log.setOrderid(suranceOrder.getId()+"");
		log.save();
		return redirect(forward);
		
	}
	
    public static Result suranceorderTrackIn(int id) {
		
    	ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	Date createtime=new Date();
    	
    	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	
    	String code="";
    	try{
    		
			SuranceOrder suranceOrder=SuranceOrder.findById(id);
			
			String content=in.get("content");
			
			String type=in.get("type");
						
			String orderstatus=in.get("orderstatus");
			
			List<CarOrderTrack> tracks=new ArrayList<CarOrderTrack>();
			
			SuranceOrderTrack track=new SuranceOrderTrack();
			
			track.setSuranceOrder(suranceOrder);
			
			track.setContent(content);
			
			track.setCreatetime(createtime);
			
			track.setOrderstatus(orderstatus);
			
			track.setType(type);
			
			track.setUname(session("name"));
			
			track.setUserid(session("userid"));
			
			track.save();
			
			code="1";
			
    	}catch (Exception e) {
    		code="0";
		}
		
		return ok("{\"code\":\""+code+"\",\"ctime\":\""+df.format(createtime)+"\"}");
		
	}
    
    public static Result suranceorderkfcheck(int id,String status) {
		
    	String forward="";
    	
		SuranceOrder suranceOrder=SuranceOrder.findById(id);
		
		suranceOrder.setStatus(status);
		
		suranceOrder.update();
		
		SuranceOrderLog log=new SuranceOrderLog();
		log.setCreatetime(new Date());
		
		
		if(status.equals("2")){
			forward="/suranceorderkfList";
			log.setOperate("机构客服("+session("name")+")处理订单!");
		}else if(status.equals("4")){
			forward="/suranceorderxsList";
			log.setOperate("总部客服("+session("name")+")处理订单!");
		}
		
		log.setStatus(status);
		log.setUserid(session("userid"));
		log.setOrderid(suranceOrder.getId()+"");
		log.save();
		
		return redirect(forward);
		
	}
    
	public static Result suranceorderxsList() {
		
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
		
    	List<SuranceOrder> list = ProductOrderApplication.suranceorderforlist(pageSize,pageNow," and ((status=2) or(status=3 and xs="+session("userid")+"))",btn);
    	
    	//总数
		int countAll = ProductOrderApplication.suranceorderforcount(" and ((status=2) or(status=3 and xs="+session("userid")+"))",btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      	
		return ok(suranceorderxs.render(list,countAll,pageNow,pageCount,pageSize,"suranceorder",btn));
		
    }
	
	public static Result suranceorderdkList() {
		
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
		
    	List<SuranceOrder> list = ProductOrderApplication.suranceorderforlist(pageSize,pageNow," and ((status=4) or(status=5 and yw="+session("userid")+"))",btn);
    	
    	//总数
		int countAll = ProductOrderApplication.suranceorderforcount(" and ((status=4) or(status=5 and yw="+session("userid")+"))",btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      	
		return ok(suranceorderdk.render(list,countAll,pageNow,pageCount,pageSize,"suranceorder",btn));
		
    }

	public static Result suranceorderTrackShow(int id) {
		
		SuranceOrder suranceOrder=SuranceOrder.findById(id);

		Customer customer= Customer.findById(Integer.parseInt(suranceOrder.getCustomerid()));
		
		
		return ok(suranceordertrackshow.render(customer,suranceOrder,"suranceorder"));
		
	}
	
    public static Result suranceorderFlowDetailed(int id) {
		
		SuranceOrder suranceOrder=SuranceOrder.findById(id);

		Customer customer= Customer.findById(Integer.parseInt(suranceOrder.getCustomerid()));
		
		return ok(suranceorderflowdetail.render(customer,suranceOrder,"carorder"));
		
	}
}
