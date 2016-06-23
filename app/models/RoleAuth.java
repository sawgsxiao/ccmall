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
@Table(name="jc_role_auth")
public class RoleAuth extends Model{

    @Id
    private int id;

	//基础信息
	private int roleid;
    private String authid;
    
    public RoleAuth() {

    }
    
    public static Finder<Integer,RoleAuth> find = new Finder<Integer,RoleAuth>(
    	Integer.class, RoleAuth.class
  	); 
  	
  	public static List<RoleAuth> findListById(int roleid) {
        return find.where().eq("roleid", roleid).findList();
    }
    
    public static void del(int roleid) {
    	Ebean.createSqlUpdate(
            "delete from jc_role_auth where roleid = :roleid "
        ).setParameter("roleid", roleid)
         .execute();
    }
    
    // Getter and Setter removed for brevity
    public int getId() {
  		return id;
	}
	
    public int getRoleid() {
  		return roleid;
	}
	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}
	
	public void setAuthid(String authid) {
		this.authid = authid;
	}
	public String getAuthid() {
		return authid;
	}
    
}
