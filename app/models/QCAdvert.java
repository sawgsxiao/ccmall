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
@Table(name="cc_qcadvert")
public class QCAdvert extends Model{

    @Id
    private int id;
	
	//基础信息
    private String name;
    private String imgurl;
    private String remark;
    private String uuid;
    private Date createtime;
    private String type;
    
    public QCAdvert() {

    }
    
    public static Finder<Integer,QCAdvert> find = new Finder<Integer,QCAdvert>(
    	Integer.class, QCAdvert.class
  	); 
  	
  	public static QCAdvert findById(int id) {
        return find.where().eq("id", id).findUnique();
    }
    
    public static List<QCAdvert> findAllList() {
        return find.where().findList();
    }
    
    public static List<QCAdvert> findListByType(String type) {
        return find.where().eq("type", type).findList();
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
	
	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
