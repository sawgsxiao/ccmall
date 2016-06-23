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
import java.util.List;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;
import play.data.DynamicForm;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

import javax.persistence.*;

import models.*;

@Entity
@Table(name="jc_report_config")
public class ReportConfig {

	@Id
	private int id;
	
	private String sqlstr;
	
	private String menuid;
	
	private String columnarray;
	
	@OneToMany(mappedBy="config")
	private List<InputItem> items;
	
	public ReportConfig() {

    }
	
	public static Finder<Integer,ReportConfig> find = new Finder<Integer,ReportConfig>(
	    	Integer.class, ReportConfig.class
	  	); 
	  	
  	public static ReportConfig findById(int id) {
        return find.where().eq("id", id).findUnique();
    }

	public String getSqlstr() {
		return sqlstr;
	}

	public void setSqlstr(String sqlstr) {
		this.sqlstr = sqlstr;
	}

	public String getMenuid() {
		return menuid;
	}

	public void setMenuid(String menuId) {
		menuid = menuId;
	}

	public int getId() {
		return id;
	}

	public List<InputItem> getItems() {
		return items;
	}

	public void setItems(List<InputItem> items) {
		this.items = items;
	}

	public String getColumnarray() {
		return columnarray;
	}

	public void setColumnarray(String columnarray) {
		this.columnarray = columnarray;
	}  	

}
