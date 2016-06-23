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

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;

import java.util.Date;
import java.util.List;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;
import javax.persistence.*;

import models.*;

@Entity
@Table(name="cc_app_hot_car")
public class AppHotCarView extends Model{

    @Id
    private int id;
	
	//基础信息
    private String thedesc;
    private String firstprice;
    private Date createtime;
    private String uuid;
    private String carname;
    private String productcode;
    private String assortcode;
    private String remark;
    private String code;
    private String hot;
    private String pricerange;
    private String assortname;
    private String stand;
    private String size;
    private String colorlist;    
    
    public AppHotCarView() {

    }
    
    public static Finder<Integer,AppHotCarView> find = new Finder<Integer,AppHotCarView>(
    	Integer.class, AppHotCarView.class
  	); 
  	
  	public static AppHotCarView findById(int id) {
        return find.where().eq("id", id).findUnique();
    }
    
    public static AppHotCarView findByUuid(String uuid) {
        return find.where().eq("uuid", uuid).findUnique();
    }
    
    // Getter and Setter removed for brevity
    public int getId() {
  		return id;
	}

	public String getThedesc() {
		return thedesc;
	}

	public String getFirstprice() {
		return firstprice;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public String getUuid() {
		return uuid;
	}

	public String getCarname() {
		return carname;
	}

	public String getProductcode() {
		return productcode;
	}

	public String getAssortcode() {
		return assortcode;
	}

	public String getRemark() {
		return remark;
	}

	public String getCode() {
		return code;
	}

	public String getHot() {
		return hot;
	}

	public String getPricerange() {
		return pricerange;
	}

	public String getAssortname() {
		return assortname;
	}

	public String getStand() {
		return stand;
	}

	public String getSize() {
		return size;
	}

	public String getColorlist() {
		return colorlist;
	}

}
