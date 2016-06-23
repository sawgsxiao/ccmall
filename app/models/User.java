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

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
@Table(name="jc_user")
public class User extends Model{
	
    @Id
    private int userid;
	
	//基础信息
    private String username;
    private String password;
    private String name;
    private String mobile;
    private int status;
    private int roleid;
    private int departid;
    
    private int notelevel;
    
    
    /*@OneToMany(targetEntity=UserRole.class,mappedBy="user",cascade=CascadeType.ALL)
    private List<UserRole> list;*/
    
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="jc_user_role",joinColumns={@JoinColumn(name="userid",referencedColumnName="userid") },inverseJoinColumns={@JoinColumn(name="roleid",referencedColumnName="roleid")})
    private List<Role> roles;
    
    public User() {

    }
    
    public static Finder<Integer,User> find = new Finder<Integer,User>(
    	Integer.class, User.class
  	); 
  	
  	public static User findById(int userid) {
        return find.where().eq("userid", userid).findUnique();
    }
    
    /**
     * Retrieve a User from username.
     */
    public static User findByUsername(String username) {
        return find.where().eq("username", username).findUnique();
    }
    
    /**
     * Authenticate a SysUser.
     */
    public static User authenticate(String username, String password) {
        return find.where()
            .eq("username", username)
            .eq("password", password)
            .findUnique();
    }
    
	// public static void del(int userid) {
//     	Ebean.createSqlUpdate(
//             "delete from jc_user where userid = :userid "
//         ).setParameter("userid", userid)
//          .execute();
//     }
	public static void del(int userid) {
    	Ebean.createSqlUpdate(
            "update jc_user set status = 1 where userid = :userid "
        ).setParameter("userid", userid)
         .execute();
    }
    
    public static void recover(int userid) {
    	Ebean.createSqlUpdate(
            "update jc_user set status = 0 where userid = :userid "
        ).setParameter("userid", userid)
         .execute();
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

 	public void setPassword(String password) {  
        this.password = password;
    }  
    
    public String getPassword() {
  		return password;
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
