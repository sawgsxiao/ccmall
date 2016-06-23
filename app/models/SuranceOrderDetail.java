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
@Table(name="cc_surance_order_detail")
public class SuranceOrderDetail extends Model{

    @Id
    private int id;
	
	//基础信息
    private String productcode;
    private String price;
    private Date createtime;
    private String remark;
    private String count;
    private String productname;
    private String assortid;
    private String assortname;
    private String inusetime;
    
    @ManyToOne
    @JoinColumn(name="orderid", nullable=false)
    private SuranceOrder suranceOrder;

    public SuranceOrderDetail() {

    }
    
    public static Finder<Integer,SuranceOrderDetail> find = new Finder<Integer,SuranceOrderDetail>(
    	Integer.class, SuranceOrderDetail.class
  	); 
  	
  	public static SuranceOrderDetail findById(int id) {
        return find.where().eq("id", id).findUnique();
    }
    
    public static List<SuranceOrderDetail> findAllList() {
        return find.where().findList();
    }
    
    // Getter and Setter removed for brevity
    public int getId() {
  		return id;
	}


	public String getProductcode() {
		return productcode;
	}

	public void setProductcode(String productcode) {
		this.productcode = productcode;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
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

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public String getAssortid() {
		return assortid;
	}

	public void setAssortid(String assortid) {
		this.assortid = assortid;
	}

	public String getAssortname() {
		return assortname;
	}

	public void setAssortname(String assortname) {
		this.assortname = assortname;
	}

	public SuranceOrder getSuranceOrder() {
		return suranceOrder;
	}

	public void setSuranceOrder(SuranceOrder suranceOrder) {
		this.suranceOrder = suranceOrder;
	}

	public String getInusetime() {
		return inusetime;
	}

	public void setInusetime(String inusetime) {
		this.inusetime = inusetime;
	}

	
}
