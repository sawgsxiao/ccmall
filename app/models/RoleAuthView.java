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
@Table(name="jc_role_auth_view")
public class RoleAuthView extends Model{

    @Id
    private int id;

	//基础信息
	private int roleid;
    private String authid;
    private String shortname;
    private String name;
    
    public RoleAuthView() {

    }
    
    public static Finder<Integer,RoleAuthView> find = new Finder<Integer,RoleAuthView>(
    	Integer.class, RoleAuthView.class
  	); 
  	
  	public static List<RoleAuthView> findListById(int roleid) {
        return find.where().eq("roleid", roleid).findList();
    }
    
    // Getter and Setter removed for brevity
    public int getId() {
  		return id;
	}
	
    public int getRoleid() {
  		return roleid;
	}
	
	public String getAuthid() {
		return authid;
	}
	
	public String getShortname() {
		return shortname;
	}

	public String getName() {
		return name;
	}
}
