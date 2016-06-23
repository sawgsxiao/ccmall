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
@Table(name="cc_car_assort")
public class CarAssort extends Model{

    @Id
    private int id;
	
	//基础信息
    private String assortname;
    private String assortcode;
    private String parentid;
    private Date createtime;
    private String title;
    private String uuid;
    
    public CarAssort() {

    }
    
    public static Finder<Integer,CarAssort> find = new Finder<Integer,CarAssort>(
    	Integer.class, CarAssort.class
  	); 
  	
  	public static CarAssort findById(int id) {
        return find.where().eq("id", id).findUnique();
    }
    
    public static List<CarAssort> findAllList() {
        return find.where().order("assortcode").findList();
    }
    
    public static CarAssort findByUuid(String uuid) {
        return find.where().eq("uuid", uuid).findUnique();
    }
    
    // Getter and Setter removed for brevity
    public int getId() {
  		return id;
	}

	public String getAssortname() {
		return assortname;
	}

	public void setAssortname(String assortname) {
		this.assortname = assortname;
	}

	public String getAssortcode() {
		return assortcode;
	}

	public void setAssortcode(String assortcode) {
		this.assortcode = assortcode;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	
}
