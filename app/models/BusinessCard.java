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
import java.util.Date;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;
import javax.persistence.*;

import models.*;

@Entity
@Table(name="jc_business_card")
public class BusinessCard extends Model{

    @Id
    private int cardid;
	
	//基础信息
    private String contact;
    private String company;
    private String address;
    private String mobile;
    private String qq;
    private String email;
 
    private String remark;
    private int userid;
    private Date createtime;
    
    private int privstatus;
    
    public BusinessCard() {

    }
    
    public static Finder<Integer,BusinessCard> find = new Finder<Integer,BusinessCard>(
    	Integer.class, BusinessCard.class
  	); 
  	
  	public static BusinessCard findById(int cardid) {
        return find.where().eq("cardid", cardid).findUnique();
    }
	
	public static void del(int cardid) {
    	Ebean.createSqlUpdate(
            "delete from jc_business_card where cardid = :cardid "
        ).setParameter("cardid", cardid)
         .execute();
    }
    
    // Getter and Setter removed for brevity
    public int getCardid() {
  		return cardid;
	}
	
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public int getPrivstatus() {
		return privstatus;
	}

	public void setPrivstatus(int privstatus) {
		this.privstatus = privstatus;
	}
    
}
