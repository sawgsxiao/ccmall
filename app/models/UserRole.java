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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

import javax.persistence.*;

import models.*;

@Entity
@Table(name="jc_user_role")
public class UserRole {

	@Id
	private int id;
	
	//用户与角色
	/*@ManyToOne(targetEntity=User.class)
	@JoinColumn(name="userid")
	private User user;*/
	private int userid;
	
	private int roleid;
	
	public UserRole() {
		// TODO Auto-generated constructor stub
	}
	
	public static Finder<Integer,UserRole> find = new Finder<Integer,UserRole>(
    	Integer.class, UserRole.class
  	); 
  	
  	public static UserRole findById(int id) {
        return find.where().eq("id", id).findUnique();
    }
    
    public static List<UserRole> listOrderByDepartid() {
        return find.where().order("userid").findList();
    }

    public static void delByUid(int userid) {
    	Ebean.createSqlUpdate(
            "delete from jc_user_role where userid = :userid "
        ).setParameter("userid", userid)
         .execute();
    }
    
    public static void del(int id) {
    	Ebean.createSqlUpdate(
            "delete from jc_user_role where id = :id "
        ).setParameter("id", id)
         .execute();
    }

	/*public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}*/

	public int getRoleid() {
		return roleid;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}
    
    
}
