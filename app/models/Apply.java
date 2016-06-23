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
@Table(name="cc_apply")
public class Apply extends Model{

    @Id
    private int id;
	
	//基础信息
    private String name;
    private String phone;
    private String brand;
    private String address;
    private String bill;
    private Date securedate;
    private String recommend;
    private Date createtime;
    
    public Apply() {

    }
    
    public static Finder<Integer,Apply> find = new Finder<Integer,Apply>(
    	Integer.class, Apply.class
  	); 
  	
  	public static Apply findById(int id) {
        return find.where().eq("id", id).findUnique();
    }
    
    public static List<Apply> findAllList() {
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

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBill() {
		return bill;
	}

	public void setBill(String bill) {
		this.bill = bill;
	}

	public Date getSecuredate() {
		return securedate;
	}

	public void setSecuredate(Date securedate) {
		this.securedate = securedate;
	}

	public String getRecommend() {
		return recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

}
