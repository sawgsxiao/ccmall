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
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.core.JsonParseException;  

import play.api.libs.ws.Response;
import play.api.libs.ws.WS;
import play.data.DynamicForm;
import com.avaje.ebean.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import models.*;

import java.util.Arrays;
import java.util.Collections;
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
import java.lang.reflect.Array;
import java.io.ByteArrayOutputStream;
import javax.imageio.stream.FileImageInputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.Region;

import play.db.ebean.Model;



public class ReportApplication extends Controller{

//	private static String sqlstr;
//	
//	private static String sqllast;
//	
//	private static String menuid;
//	
//	private static ArrayList<String> array;
//	
//	private static List<InputItem> inputItems;
	
	
	public static Result reportList(int id) {
		
		ReportConfig config= initConfig(id);
		
		ArrayList<String> array = new ArrayList<String>();
		
		if(config.getColumnarray()!=null){
			if(config.getColumnarray().contains("|")){
				String[] strs=config.getColumnarray().split("\\|");
				Collections.addAll(array, strs);
			}else{
				array.add(config.getColumnarray());
			}
		}
		
		ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	
    	List<InputBtn> btn=createText(config);
    	
    	List<String> menus=new ArrayList<String>();
    	String clz=config.getMenuid();
    	String meNames=clz.substring(clz.indexOf("|")+1);
    	String[] arrStr= meNames.split(",");
    	for (int i = 0; i < arrStr.length; i++) {
    		menus.add(arrStr[i]);
		}
    	
//    	System.out.println("meNames:"+meNames);
    	clz=clz.substring(0,clz.indexOf("|"));
    	
//    	System.out.println("clz:"+clz);
    	//分页
    	//每页显示数据
		int pageSize = Integer.parseInt(Play.application().configuration().getString("pageSize"));
		
		//当前第N页
		int pageNow = 1;
		
		String pageNowIn = (String)in.get("pageNow");
		if(pageNowIn!=null){
		
			pageNow = Integer.parseInt(pageNowIn);
		}
		List<Map<String, String>> list=getReportData(config,array,pageSize,pageNow,btn);
		
    	//总数
		int countAll = ReportApplication.count(config,btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}
      	
		return ok(report.render(list,countAll,pageNow,pageCount,pageSize,"reportList/"+id,btn,array,clz,menus));
		
    }
    
	
	
	public static Result reportListExcel(int id,String act) {
		
		ReportConfig config= initConfig(id);
		
		ArrayList<String> array = new ArrayList<String>();
		
		if(config.getColumnarray()!=null){
			if(config.getColumnarray().contains("|")){
				String[] strs=config.getColumnarray().split("\\|");
				Collections.addAll(array, strs);
			}else{
				array.add(config.getColumnarray());
			}
		}
		
		ObjectNode resultJson = Json.newObject();
		
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	
    	
    	List<InputBtn> btn=createText(config);
    	
    	List<String> menus=new ArrayList<String>();
    	String clz=config.getMenuid();
    	String meNames=clz.substring(clz.indexOf("|")+1);
    	String[] arrStr= meNames.split(",");
    	for (int i = 0; i < arrStr.length; i++) {
    		menus.add(arrStr[i]);
		}
    	
//    	System.out.println("meNames:"+meNames);
    	clz=clz.substring(0,clz.indexOf("|"));
    	
//    	System.out.println("clz:"+clz);
    	//分页
    	//每页显示数据
		int pageSize = Integer.parseInt(Play.application().configuration().getString("pageSize"));
		
		//当前第N页
		int pageNow = 1;
		
		/*String pageNowIn = (String)in.get("pageNow");
		if(pageNowIn!=null){
		
			pageNow = Integer.parseInt(pageNowIn);
		}*/
		List<Map<String, String>> list=getReportData(config,array,10000,pageNow,btn);
		
    	/*//总数
		int countAll = ReportApplication.count(config,btn);
		
		//总页数
		int pageCount = 0;
		
		if (countAll % pageSize == 0) {
       		pageCount = countAll / pageSize;
      	} else {
       		pageCount = countAll / pageSize + 1;
      	}*/
		
		act=menus.get(menus.size()-1);

		HSSFWorkbook workbook= createExcel(list, act, array);
		
		File file=new File("report"+new Date().getTime()+".xls");
		
		FileOutputStream outputStream= null;
		try {
			outputStream=new FileOutputStream(file);
			workbook.write(outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ok(file);
		
    }
	
	public static List<InputBtn> createText(ReportConfig config){
		//输入参数
    	DynamicForm in = Form.form().bindFromRequest();
    	Map<String, String> data=in.data();
    	List<InputBtn> l=new ArrayList<InputBtn>();
    	
    	for (InputItem input : config.getItems()) {
    		InputBtn bt=new InputBtn(input.getTid(),input.getName(),input.getValue()!=null&&
    				!input.getValue().equals("")?input.getValue():"",input.getPlaceholder(),"",input.getType());
    		if(data.containsKey(input.getName())){
        		bt.setValue(data.get(input.getName()));
        	}
    		l.add(bt);
		}
		
		return l;
		
	}
	
    
    public static int count(ReportConfig config,List<InputBtn>... btn){
		
    	List<InputItem> inputItems=config.getItems();
    	
    	String where="";

    	String sqllast="";
    	
    	if(inputItems.size()>0){
    		for (int i = 0; i < inputItems.size(); i++) {
    			InputItem inputItem=inputItems.get(i); 
    			InputBtn btnitem=btn[0].get(i);
    			if(btnitem.getValue()!=null&&!btnitem.getValue().equals("")&&inputItem.getType().equals("DATE")){
    				where+=" and DATE_FORMAT("+inputItem.getName().replace("["+inputItem.getTid().substring(inputItem.getTid().length()-1)+"]", "")+",\"%Y-%m-%d\") "+inputItem.getMathtype()+"\""+btnitem.getValue()+"\"";
    			}
    			if(btnitem.getValue()!=null&&!btnitem.getValue().equals("")&&inputItem.getType().equals("TEXT")){
    				if(inputItem.getMathtype().equals("=")){
    					where+=" and "+inputItem.getName().replace("["+inputItem.getTid().substring(inputItem.getTid().length()-1)+"]", "")+" "+inputItem.getMathtype()+"\""+btnitem.getValue()+"\"";
    				}else if(inputItem.getMathtype().equals("like")){
    					where+=" and "+inputItem.getName().replace("["+inputItem.getTid().substring(inputItem.getTid().length()-1)+"]", "")+" "+inputItem.getMathtype()+"\""+btnitem.getValue()+"%\"";
    				}
    				
    			}
			}
    	}
    	sqllast="select count(1) COUNT from ("+config.getSqlstr().replace(":where", where)+") t";
    	

     	
        SqlRow row = Ebean.createSqlQuery(sqllast).findUnique(); 
        
        int i = row.getInteger("count");  
       	
     	return i;
    }
    
    private static ReportConfig loadReportConfig(int id){
    	
		return ReportConfig.findById(id);
    	
    }
    
    /*
     * 初始化report
     * 注册report参数
     */
    private static ReportConfig initConfig(int id){
    	
    	ReportConfig config= loadReportConfig(id);
    	
		return config;
    }
    
    private  static List<Map<String, String>> getReportData(ReportConfig config,List<String> array,int pageSize,int pageNow,List<InputBtn>... btn){
    	
    	int start=(pageNow-1)*pageSize;
    	
    	List<Map<String, String>> list=new ArrayList<Map<String,String>>();
    	
    	String where="";
    	
    	List<InputItem> inputItems=config.getItems();
    	
    	String sqllast="";
    	
    	if(inputItems.size()>0){
    		for (int i = 0; i < inputItems.size(); i++) {
    			InputItem inputItem=inputItems.get(i); 
    			InputBtn btnitem=btn[0].get(i);
    			if(btnitem.getValue()!=null&&!btnitem.getValue().equals("")&&inputItem.getType().equals("DATE")){
    				where+=" and DATE_FORMAT("+inputItem.getName().replace("["+inputItem.getTid().substring(inputItem.getTid().length()-1)+"]", "")+",\"%Y-%m-%d\") "+inputItem.getMathtype()+"\""+btnitem.getValue()+"\"";
    			}
    			if(btnitem.getValue()!=null&&!btnitem.getValue().equals("")&&inputItem.getType().equals("TEXT")){
    				if(inputItem.getMathtype().equals("=")){
    					where+=" and "+inputItem.getName().replace("["+inputItem.getTid().substring(inputItem.getTid().length()-1)+"]", "")+" "+inputItem.getMathtype()+"\""+btnitem.getValue()+"\"";
    				}else if(inputItem.getMathtype().equals("like")){
    					where+=" and "+inputItem.getName().replace("["+inputItem.getTid().substring(inputItem.getTid().length()-1)+"]", "")+" "+inputItem.getMathtype()+"\"%"+btnitem.getValue()+"%\"";
    				}
    				
    			}
			}
    	}
    	sqllast="select t.* from ("+config.getSqlstr().replace(":where", where)+") t limit "+start+" ,"+pageSize;
//    	System.out.println("sqllast:"+sqllast);
    	List<SqlRow> rows= Ebean.createSqlQuery(sqllast).findList();
    	
    	if(rows.size()>0){
    		Set<String> sets= rows.get(0).keySet();
        	array=new ArrayList<String>();
        	array.addAll(sets);
        	String key="";
        	for (SqlRow sqlRow : rows) {
        		Map<String, String> map =new HashMap<String, String>();
        		for (int i = 0; i < array.size(); i++) {
        			map.put(array.get(i), sqlRow.getString(array.get(i)));
    			}
        		list.add(map);
    		}
    	}

		return list;
    }
    
    
	public static HSSFWorkbook  createExcel(List<Map<String, String>> list,String fileName,List<String> columnNames){
    	
    	HSSFWorkbook workbook=new HSSFWorkbook();
    	
    	HSSFSheet sheet= workbook.createSheet();
    	
    	HSSFRow row0= sheet.createRow(0);
    	
    	HSSFCellStyle hStyle=workbook.createCellStyle();
    	
    	HSSFFont font= workbook.createFont();
    	
	    font.setFontName(HSSFFont.FONT_ARIAL);//字体
        font.setFontHeightInPoints((short) 20);//字号 
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗
        
    	hStyle.setFont(font);
    	
    	hStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    	
    	sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnNames.size()-1));
    	
    	HSSFCell cell0= row0.createCell(0);
    	
    	cell0.setCellValue(fileName);
    	
    	cell0.setCellStyle(hStyle);
    	
    	HSSFRow row1= sheet.createRow(1);
    	
    	sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, columnNames.size()-1));
    	
    	HSSFCellStyle hStyle1=workbook.createCellStyle();
    	
    	HSSFFont font1= workbook.createFont();
    	
    	font1.setFontName(HSSFFont.FONT_ARIAL);//字体
        font1.setFontHeightInPoints((short) 10);//字号 
        font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗
    	
        hStyle1.setFont(font1);
        
        hStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        
        HSSFCell cell1= row1.createCell(0);
    	
    	cell1.setCellValue("导出时间:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    	
    	cell1.setCellStyle(hStyle1);
        
    	HSSFRow row2= sheet.createRow(2);
    	
    	for (int i = 0; i < columnNames.size(); i++) {
    		row2.createCell(i).setCellValue(columnNames.get(i));
		}
    	Map<String, String> data;
    	
    	if(list.size()>0){
    		for (int i = 0; i < list.size(); i++) {
    			data=list.get(i);
    			HSSFRow row = sheet.createRow(i+3);
    			for (int j = 0; j < columnNames.size(); j++) {
    				row.createCell(j).setCellValue(data.get(columnNames.get(j)));
				}
			}
    	}
		return workbook;
    	
    }
}
