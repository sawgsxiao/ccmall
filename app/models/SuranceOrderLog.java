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
import java.util.List;

import play.db.ebean.*;
import play.data.format.*;
// import play.data.valroleidation.*;

import com.avaje.ebean.*;
import javax.persistence.*;

import models.*;

@Entity
@Table(name="cc_surance_order_log")
public class SuranceOrderLog extends Model{

    @Id
    private int id;
	
	//基础信息
    private String userid;
    
    private String orderid;
    
    private String operate;
    
    private Date createtime;
    
    private String status;
    
    public SuranceOrderLog() {

    }
    
    public static Finder<Integer,SuranceOrderLog> find = new Finder<Integer,SuranceOrderLog>(
    	Integer.class, SuranceOrderLog.class
  	); 
  	
  	public static SuranceOrderLog findById(int id) {
        return find.where().eq("id", id).findUnique();
    }
    
    public static void del(int id) {
    	Ebean.createSqlUpdate(
            "delete from cc_surance_order_log where id = :id "
        ).setParameter("id", id)
         .execute();
    }
    
    public static List<SuranceOrderLog> findAllList() {
        return find.where().findList();
    }

	
    
    // Getter and Setter removed for brevity
    public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public int getId() {
		return id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
	
}
