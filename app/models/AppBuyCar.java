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
@Table(name="cc_app_buycar")
public class AppBuyCar extends Model{

    @Id
    private int id;
	
	//基础信息
    private String name;
    private String mobile;
    private String cartype;
    private String carprice;
    private Date createtime;
    private String paytype;
    private String payperiod;
    
    public AppBuyCar() {

    }
    
    public static Finder<Integer,AppBuyCar> find = new Finder<Integer,AppBuyCar>(
    	Integer.class, AppBuyCar.class
  	); 
  	
  	public static AppBuyCar findById(int id) {
        return find.where().eq("id", id).findUnique();
    }
    
    public static List<AppBuyCar> findAllList() {
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

	public String getCartype() {
		return cartype;
	}

	public void setCartype(String cartype) {
		this.cartype = cartype;
	}

	public String getCarprice() {
		return carprice;
	}

	public void setCarprice(String carprice) {
		this.carprice = carprice;
	}

	public String getPaytype() {
		return paytype;
	}

	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}

	public String getPayperiod() {
		return payperiod;
	}

	public void setPayperiod(String payperiod) {
		this.payperiod = payperiod;
	}

	
}
