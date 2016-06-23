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
@Table(name="cc_car_order_track")
public class CarOrderTrack extends Model{

    @Id
    private int id;
	
	//基础信息
    private String type;
    private Date createtime;
    private String userid;
    private String content;
    private String remark;
    private String orderstatus;
    private String trackcode;
    private String uname;
    
    @ManyToOne
    @JoinColumn(name="orderid", nullable=false)
    private CarOrder carOrder;

    public CarOrderTrack() {

    }
    
    public static Finder<Integer,CarOrderTrack> find = new Finder<Integer,CarOrderTrack>(
    	Integer.class, CarOrderTrack.class
  	); 
  	
  	public static CarOrderTrack findById(int id) {
        return find.where().eq("id", id).findUnique();
    }
    
    public static List<CarOrderTrack> findAllList() {
        return find.where().findList();
    }
    
    // Getter and Setter removed for brevity
    public int getId() {
  		return id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOrderstatus() {
		return orderstatus;
	}

	public void setOrderstatus(String orderstatus) {
		this.orderstatus = orderstatus;
	}

	public String getTrackcode() {
		return trackcode;
	}

	public void setTrackcode(String trackcode) {
		this.trackcode = trackcode;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public CarOrder getCarOrder() {
		return carOrder;
	}

	public void setCarOrder(CarOrder carOrder) {
		this.carOrder = carOrder;
	}

}
