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

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;
import com.avaje.ebean.Query;

import javax.persistence.*;

import models.*;

@Entity
@Table(name="jc_user_view")
public class UserView extends Model{
	
    @Id
    private int userid;
	
	//基础信息
    private String username;
    private String name;
    private String mobile;
    private int status;
    private int roleid;
    private int departid;
    private String rolename;
    private String departname;
    
    private int notelevel;
    //角色
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="jc_user_role",joinColumns={@JoinColumn(name="userid",referencedColumnName="userid") },inverseJoinColumns={@JoinColumn(name="roleid",referencedColumnName="roleid")})
    private List<Role> roles;
    
    public UserView() {

    }
    
    public static Finder<Integer,UserView> find = new Finder<Integer,UserView>(
    	Integer.class, UserView.class
  	); 
  	
  	public static UserView findById(int userid) {
        return find.where().eq("userid", userid).findUnique();
    }
    
    public static List<UserView> listOrderByDepartid() {
        return find.where().order("departid").findList();
    }
    
    
    // Getter and Setter removed for brevity    
    public int getUserid() {
  		return userid;
	}
	
    public void setUsername(String username) {  
        this.username = username;
    }  
    
    public String getUsername() {
  		return username;
	}
	
	public void setName(String name) {  
        this.name = name;
    }  
    
    public String getName() {
  		return name;
	} 
	
	public void setMobile(String mobile) {  
        this.mobile = mobile;
    }  
    
    public String getMobile() {
  		return mobile;
	} 
	
	public void setStatus(int status) {  
        this.status = status;
    }  
    
    public int getStatus() {
  		return status;
	} 
	
	public void setRoleid(int roleid) {  
        this.roleid = roleid;
    }  
    
    public int getRoleid() {
  		return roleid;
	}
	
	public void setDepartid(int departid) {  
        this.departid = departid;
    }  
    
    public int getDepartid() {
  		return departid;
	} 
    
    public String getRolename() {
  		return rolename;
	}
	
	public String getDepartname() {
  		return departname;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	public int getNotelevel() {
		return notelevel;
	}

	public void setNotelevel(int notelevel) {
		this.notelevel = notelevel;
	}
	
}
