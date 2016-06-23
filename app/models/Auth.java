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
import java.util.List;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;
import javax.persistence.*;

import models.*;

@Entity
@Table(name="jc_auth")
public class Auth extends Model{

    @Id
    private int id;
	
	//基础信息
    private String name;
    private String authid;
    private String parentid;
    private int levels;
    
    public Auth() {

    }
    
    public static Finder<Integer,Auth> find = new Finder<Integer,Auth>(
    	Integer.class, Auth.class
  	); 
  	
  	public static Auth findById(int id) {
        return find.where().eq("id", id).findUnique();
    }
    
    public static List<Auth> findAllList() {
        return find.where().findList();
    }
    
    public static List<Auth> findListForParentid(int levels,String parentid) {
        return find.where().eq("levels", levels).eq("parentid", parentid).order("authid").findList();
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
	public void setAuthid(String authid) {
		this.authid = authid;
	}
	public String getAuthid() {
		return authid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public String getParentid() {
		return parentid;
	}
	
	public void setLevels(int levels) {
		this.levels = levels;
	}
	public int getLevels() {
		return levels;
	}
    
}
