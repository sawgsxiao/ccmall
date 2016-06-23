package models;

import play.db.ebean.Model;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;  
import java.io.ByteArrayOutputStream;  
import java.io.DataInputStream;  
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;
import play.data.DynamicForm;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

import javax.persistence.*;

import models.*;

@Entity
@Table(name="jc_input_item")
public class InputItem extends Model{

	@Id
	private int id;
	
	private String tid;
	
	private String name;
	
	private String value;
	
	private String placeholder;
	
	private String style;
	
	private String classid;
	
	private String type ="TEXT";
	
	/*
	 * 逻辑符号
	 * 暂支持：=,>,<
	 */
	private String mathtype="=";
	
	@ManyToOne
	@JoinColumn(name="configid", nullable=false)
	private ReportConfig config;
	/*
	 * 选择条件控件类型
	 */
	//TEXT
	public final static String CTYPE_INPUT ="TEXT";
	
	//SELECT
	//public final static String CTYPE_SELECT ="SELECT";
	
	//DATE
	public final static String CTYPE_DATE ="DATE";

	public static Finder<Integer,InputItem> find = new Finder<Integer,InputItem>(
	    	Integer.class, InputItem.class
	  	); 
	  	
  	public static InputItem findById(int id) {
        return find.where().eq("id", id).findUnique();
    }
	
	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getClassid() {
		return classid;
	}

	public void setClassid(String classid) {
		this.classid = classid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public ReportConfig getConfig() {
		return config;
	}

	public void setConfig(ReportConfig config) {
		this.config = config;
	}

	public String getMathtype() {
		return mathtype;
	}

	public void setMathtype(String mathtype) {
		this.mathtype = mathtype;
	}

	
}
