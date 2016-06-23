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
@Table(name="jc_schedule")
public class Schedule extends Model{
	
    @Id
    private int scheduleid;
	
	//基础信息
	private int userid;
	private Date begintime;
	private Date endtime;
	private String title;
    private String content;
	private String remark;
	private Date createtime;
	
	private int msgstatus;

    public Schedule() {

    }
    
    public static Finder<Integer,Schedule> find = new Finder<Integer,Schedule>(
    	Integer.class, Schedule.class
  	); 
  	
  	public static Schedule findById(int scheduleid) {
        return find.where().eq("scheduleid", scheduleid).findUnique();
    }
	
	public static void del(int scheduleid) {
    	Ebean.createSqlUpdate(
            "delete from jc_schedule where scheduleid = :scheduleid "
        ).setParameter("scheduleid", scheduleid)
         .execute();
    }
    
    // Getter and Setter removed for brevity    
    public int getScheduleid() {
  		return scheduleid;
	}
		
	public void setUserid(int userid) {  
        this.userid = userid;
    }  
    
    public int getUserid() {
  		return userid;
	}
	
	public void setBegintime(Date begintime) {  
        this.begintime = begintime;
    }  
    
    public Date getBegintime() {
  		return begintime;
	}
	
	public void setEndtime(Date endtime) {  
        this.endtime = endtime;
    }  
    
    public Date getEndtime() {
  		return endtime;
	}
	
	public void setTitle(String title) {  
        this.title = title;
    }  
    
    public String getTitle() {
  		return title;
	}
	
	public void setContent(String content) {  
        this.content = content;
    }  
    
    public String getContent() {
  		return content;
	}
	
	public void setRemark(String remark) {  
        this.remark = remark;
    }  
    
    public String getRemark() {
  		return remark;
	}
	
	public void setCreatetime(Date createtime) {  
        this.createtime = createtime;
    }  
    
    public Date getCreatetime() {
  		return createtime;
	}

	public int getMsgstatus() {
		return msgstatus;
	}

	public void setMsgstatus(int msgstatus) {
		this.msgstatus = msgstatus;
	}
    
    
}
