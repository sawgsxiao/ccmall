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
@Table(name="cc_app_surance_image")
public class AppSuranceImage extends Model{

    @Id
    private int id;
	
	//基础信息
    private String url;
    private Date createtime;
    private String remark;
    
    @ManyToOne()
    @JoinColumn(name="suranceid", nullable=false)
    private AppSurance appSurance;
    
    public AppSuranceImage() {

    }
    
    public static Finder<Integer,AppSuranceImage> find = new Finder<Integer,AppSuranceImage>(
    	Integer.class, AppSuranceImage.class
  	); 
  	
  	public static AppSuranceImage findById(int id) {
        return find.where().eq("id", id).findUnique();
    }
    
    public static List<AppSuranceImage> findAllList() {
        return find.where().findList();
    }
    
    // Getter and Setter removed for brevity
    public int getId() {
  		return id;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public AppSurance getAppSurance() {
		return appSurance;
	}

	public void setAppSurance(AppSurance appSurance) {
		this.appSurance = appSurance;
	}

	

	
}
