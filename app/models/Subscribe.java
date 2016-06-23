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
@Table(name="cc_subscribe")
public class Subscribe extends Model{

    @Id
    private int id;
	
	//基础信息
    private String name;
    private String phone;
    private String sex;
    private String asset;
    private String driveport;
    private String intentprice;
    private String recommend;
    private String dominatedate;
    private Date createtime;
    
    public Subscribe() {

    }
    
    public static Finder<Integer,Subscribe> find = new Finder<Integer,Subscribe>(
    	Integer.class, Subscribe.class
  	); 
  	
  	public static Subscribe findById(int id) {
        return find.where().eq("id", id).findUnique();
    }
    
    public static List<Subscribe> findAllList() {
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAsset() {
		return asset;
	}

	public void setAsset(String asset) {
		this.asset = asset;
	}

	public String getDirveport() {
		return driveport;
	}

	public void setDirveport(String driveport) {
		this.driveport = driveport;
	}

	public String getIntentprice() {
		return intentprice;
	}

	public void setIntentprice(String intentprice) {
		this.intentprice = intentprice;
	}

	public String getRecommend() {
		return recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}

	public String getDominatedate() {
		return dominatedate;
	}

	public void setDominatedate(String dominatedate) {
		this.dominatedate = dominatedate;
	}

	public void setDriveport(String driveport) {
		this.driveport = driveport;
	}

	public String getDriveport() {
		return driveport;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	
}
