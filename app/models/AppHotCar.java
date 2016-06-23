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
public class AppHotCar extends Model{

    @Id
    private int id;
	
	//基础信息
    private String thedesc;
    private String firstprice;
    private Date createtime;
    private String uuid;
    private String discount;
    private String disdesc;
    
    
    @OneToOne(mappedBy="hotCar")
    private AppCarStyle appCarStyle;
    
    public AppHotCar() {

    }
    
    public static Finder<Integer,AppHotCar> find = new Finder<Integer,AppHotCar>(
    	Integer.class, AppHotCar.class
  	); 
  	
  	public static AppHotCar findById(int id) {
        return find.where().eq("id", id).findUnique();
    }
    
    public static AppHotCar findByUuid(String uuid) {
        return find.where().eq("uuid", uuid).findUnique();
    }
    
    public static List<AppHotCar> findAllList() {
        return find.where().orderBy("createtime").findList();
    }
    
    public static List<AppHotCar> findStandList() {
        return find.where().orderBy("createtime").findList();
    }
    
    // Getter and Setter removed for brevity
    public int getId() {
  		return id;
	}

	public String getFirstprice() {
		return firstprice;
	}

	public void setFirstprice(String firstprice) {
		this.firstprice = firstprice;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getThedesc() {
		return thedesc;
	}

	public void setThedesc(String thedesc) {
		this.thedesc = thedesc;
	}

	public AppCarStyle getAppCarStyle() {
		return appCarStyle;
	}

	public void setAppCarStyle(AppCarStyle appCarStyle) {
		this.appCarStyle = appCarStyle;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getDisdesc() {
		return disdesc;
	}

	public void setDisdesc(String disdesc) {
		this.disdesc = disdesc;
	}

	
}
