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
@Table(name="cc_app_parrallel_car")
public class AppParrallelCar extends Model{

    @Id
    private int id;
	
	//基础信息
    private Date createtime;
    private String uuid;
    private String spec;
    private String stock;
    private String thedesc;
    
    
    @OneToOne(mappedBy="parrallelCar")
    private AppCarStyle appCarStyle;
    
    public AppParrallelCar() {

    }
    
    public static Finder<Integer,AppParrallelCar> find = new Finder<Integer,AppParrallelCar>(
    	Integer.class, AppParrallelCar.class
  	); 
  	
  	public static AppParrallelCar findById(int id) {
        return find.where().eq("id", id).findUnique();
    }
    
    public static AppParrallelCar findByUuid(String uuid) {
        return find.where().eq("uuid", uuid).findUnique();
    }
    
    public static List<AppParrallelCar> findAllList() {
        return find.where().orderBy("createtime").findList();
    }
    
    public static List<AppParrallelCar> findStandList() {
        return find.where().orderBy("createtime").findList();
    }
    
    // Getter and Setter removed for brevity
    public int getId() {
  		return id;
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

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public AppCarStyle getAppCarStyle() {
		return appCarStyle;
	}

	public void setAppCarStyle(AppCarStyle appCarStyle) {
		this.appCarStyle = appCarStyle;
	}

	public String getThedesc() {
		return thedesc;
	}

	public void setThedesc(String thedesc) {
		this.thedesc = thedesc;
	}


	
}
