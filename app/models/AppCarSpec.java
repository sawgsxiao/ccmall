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
@Table(name="cc_app_car_spec")
public class AppCarSpec extends Model{

    @Id
    private int id;
	
	//基础信息

    private String spec;
    
    private String thedesc;
    
    public AppCarSpec() {

    }
    
    public static Finder<Integer,AppCarSpec> find = new Finder<Integer,AppCarSpec>(
    	Integer.class, AppCarSpec.class
  	); 
  	
  	public static AppCarSpec findById(int id) {
        return find.where().eq("id", id).findUnique();
    }
    
    public static AppCarSpec findByUuid(String uuid) {
        return find.where().eq("uuid", uuid).findUnique();
    }
    
    public static List<AppCarSpec> findAllList() {
        return find.where().findList();
    }
    
    // Getter and Setter removed for brevity
    public int getId() {
  		return id;
	}

	public String getThedesc() {
		return thedesc;
	}

	public void setThedesc(String thedesc) {
		this.thedesc = thedesc;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

}
