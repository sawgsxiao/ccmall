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
@Table(name="jc_department")
public class Department extends Model{

    @Id
    private int departid;
	
	//基础信息
    private String name;
    
    private int pardepartid;
    
    public Department() {

    }
    
    public static Finder<Integer,Department> find = new Finder<Integer,Department>(
    	Integer.class, Department.class
  	); 
  	
  	public static Department findById(int departid) {
        return find.where().eq("departid", departid).findUnique();
    }
    
    public static void del(int departid) {
    	Ebean.createSqlUpdate(
            "delete from jc_department where departid = :departid "
        ).setParameter("departid", departid)
         .execute();
    }
    
    public static List<Department> findList() {
        return find.where().findList();
    }
    
    public static List<Department> findAllList() {
        return find.where().findList();
    }
    
    // Getter and Setter removed for brevity
    public int getDepartid() {
  		return departid;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public int getPardepartid() {
		return pardepartid;
	}

	public void setPardepartid(int pardepartid) {
		this.pardepartid = pardepartid;
	}
    
	
}
