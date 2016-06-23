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
@Table(name="cc_app_parrallel_buy_car")
public class AppParrallelBuyCar extends Model{

    @Id
    private int id;
	
	//基础信息
    private String name;
    private String mobile;
    private String isloan;
    private String uuid;
    private String cartype;
    private Date createtime;
    private String province;
    private String city;
    private String price;
    
    public AppParrallelBuyCar() {

    }
    
    public static Finder<Integer,AppParrallelBuyCar> find = new Finder<Integer,AppParrallelBuyCar>(
    	Integer.class, AppParrallelBuyCar.class
  	); 
  	
  	public static AppParrallelBuyCar findById(int id) {
        return find.where().eq("id", id).findUnique();
    }
    
    public static List<AppParrallelBuyCar> findAllList() {
        return find.where().findList();
    }
    
    // Getter and Setter removed for brevity
    public int getId() {
  		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getIsloan() {
		return isloan;
	}

	public void setIsloan(String isloan) {
		this.isloan = isloan;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCartype() {
		return cartype;
	}

	public void setCartype(String cartype) {
		this.cartype = cartype;
	}


}
