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

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name="cc_surance_product")
public class Surance extends Model{

    @Id
    private int id;
	
	//基础信息
    private String surancename;
    private String assortcode;
    private String productcode;
    private Date createtime;
    private String remark;
    private String assortname;
    private String price;
    private String inusetime;
    private int stand;
    
    @OneToMany(mappedBy="Surance",cascade= CascadeType.ALL)
    private List<SuranceImage> images;

    public Surance() {

    }
    
    public static Finder<Integer,Surance> find = new Finder<Integer,Surance>(
    	Integer.class, Surance.class
  	); 
  	
  	public static Surance findById(int id) {
        return find.where().eq("id", id).findUnique();
    }
    
    public static List<Surance> findAllList() {
        return find.where().findList();
    }
    
    // Getter and Setter removed for brevity
    public int getId() {
  		return id;
	}


	public String getAssortcode() {
		return assortcode;
	}

	public void setAssortcode(String assortcode) {
		this.assortcode = assortcode;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getProductcode() {
		return productcode;
	}

	public void setProductcode(String productcode) {
		this.productcode = productcode;
	}

	public String getAssortname() {
		return assortname;
	}

	public void setAssortname(String assortname) {
		this.assortname = assortname;
	}

	public int getStand() {
		return stand;
	}

	public void setStand(int stand) {
		this.stand = stand;
	}

	public String getSurancename() {
		return surancename;
	}

	public void setSurancename(String surancename) {
		this.surancename = surancename;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getInusetime() {
		return inusetime;
	}

	public void setInusetime(String inusetime) {
		this.inusetime = inusetime;
	}

	public List<SuranceImage> getImages() {
		return images;
	}

	public void setImages(List<SuranceImage> images) {
		this.images = images;
	}

	
}
