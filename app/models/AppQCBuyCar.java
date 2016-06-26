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
@Table(name="cc_qcbuy")
public class AppQCBuyCar extends Model{

    @Id
    private int id;
	
	//基础信息
    private String name;
    private String phone;
    private String pertype;
    private String payterm;
    private Date createtime;
    private String firstpay;
    private String monthpay;
    private String fullpay;
    private String cityid;
    private String carstyleid;
    private String barepay;
    private String possiblepay;
    
    public AppQCBuyCar() {

    }
    
    public static Finder<Integer,AppQCBuyCar> find = new Finder<Integer,AppQCBuyCar>(
    	Integer.class, AppQCBuyCar.class
  	); 
  	
  	public static AppQCBuyCar findById(int id) {
        return find.where().eq("id", id).findUnique();
    }
    
    public static List<AppQCBuyCar> findAllList() {
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPertype() {
		return pertype;
	}

	public void setPertype(String pertype) {
		this.pertype = pertype;
	}

	public String getPayterm() {
		return payterm;
	}

	public void setPayterm(String payterm) {
		this.payterm = payterm;
	}

	public String getFirstpay() {
		return firstpay;
	}

	public void setFirstpay(String firstpay) {
		this.firstpay = firstpay;
	}

	public String getMonthpay() {
		return monthpay;
	}

	public void setMonthpay(String monthpay) {
		this.monthpay = monthpay;
	}

	public String getFullpay() {
		return fullpay;
	}

	public void setFullpay(String fullpay) {
		this.fullpay = fullpay;
	}

	public String getCityid() {
		return cityid;
	}

	public void setCityid(String cityid) {
		this.cityid = cityid;
	}

	public String getCarstyleid() {
		return carstyleid;
	}

	public void setCarstyleid(String carstyleid) {
		this.carstyleid = carstyleid;
	}

	public String getBarepay() {
		return barepay;
	}

	public void setBarepay(String barepay) {
		this.barepay = barepay;
	}

	public String getPossiblepay() {
		return possiblepay;
	}

	public void setPossiblepay(String possiblepay) {
		this.possiblepay = possiblepay;
	}

	

	
}
