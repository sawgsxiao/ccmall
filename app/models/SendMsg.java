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
@Table(name="jc_sendmsg")
public class SendMsg extends Model{

    @Id
    private int id;
	
	//基础信息
    private String title;
    private String content;
    private int refsequence;
    private String remark;
    private String type;
    private String status;
    
    private Date createtime;
    
    public SendMsg() {

    }
    
    public static Finder<Integer,SendMsg> find = new Finder<Integer,SendMsg>(
    	Integer.class, SendMsg.class
  	); 
  	
  	public static SendMsg findById(int id) {
        return find.where().eq("id", id).findUnique();
    }
	
	public static void del(int id) {
    	Ebean.createSqlUpdate(
            "delete from jc_sendmsg where id = :id "
        ).setParameter("id", id)
         .execute();
    }

	// Getter and Setter removed for brevity
	   
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getRefsequence() {
		return refsequence;
	}

	public void setRefsequence(int refsequence) {
		this.refsequence = refsequence;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
    
 
    
}
