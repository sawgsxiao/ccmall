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
@Table(name="jc_role")
public class Role extends Model{

    @Id
    private int roleid;
	
	//基础信息
    private String name;
    
    @ManyToMany(mappedBy = "roles", fetch=FetchType.EAGER)
    private List<User> users;
    
    public Role() {

    }
    
    public static Finder<Integer,Role> find = new Finder<Integer,Role>(
    	Integer.class, Role.class
  	); 
  	
  	public static Role findById(int roleid) {
        return find.where().eq("roleid", roleid).findUnique();
    }
    
    public static void del(int roleid) {
    	Ebean.createSqlUpdate(
            "delete from jc_role where roleid = :roleid "
        ).setParameter("roleid", roleid)
         .execute();
    }
    
    public static List<Role> findList() {
        return find.where().findList();
    }
    
    public static List<Role> findAllList() {
        return find.where().findList();
    }
    
    // Getter and Setter removed for brevity
    public int getRoleid() {
  		return roleid;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
    
	
}
