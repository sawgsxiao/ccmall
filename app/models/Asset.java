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
@Table(name="cc_assets")
public class Asset extends Model{

    @Id
    private int id;
	
	//基础信息
    private String housetype;
    private Date purchasetime;
    private int price;
    private Date createtime;
    private String address;
    
    @ManyToOne
	@JoinColumn(name="cutomerid", nullable=false)
    private Customer guest;
    
    public Asset() {

    }
    
    public static Finder<Integer,Asset> find = new Finder<Integer,Asset>(
    	Integer.class, Asset.class
  	); 
  	
  	public static Asset findById(int id) {
        return find.where().eq("id", id).findUnique();
    }
    
    public static List<Asset> findAllList() {
        return find.where().findList();
    }
    
    // Getter and Setter removed for brevity
    public int getId() {
  		return id;
	}

	public String getHousetype() {
		return housetype;
	}

	public void setHousetype(String housetype) {
		this.housetype = housetype;
	}

	public Date getPurchasetime() {
		return purchasetime;
	}

	public void setPurchasetime(Date purchasetime) {
		this.purchasetime = purchasetime;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Customer getGuest() {
		return guest;
	}

	public void setGuest(Customer guest) {
		this.guest = guest;
	}

}
