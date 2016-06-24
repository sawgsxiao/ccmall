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
@Table(name="cc_city")
public class City extends Model{

    @Id
    private int id;
	
	//基础信息
    private String name;
    
    private String parentid;
    
    private String netid;
    
    private String spell;
    
    private String fullname;
    
    private String level;
    
    private String ordernumber;
    
    private String regionid;
    
    private Date createtime;
    
    public City() {

    }
    
    public static Finder<Integer,City> find = new Finder<Integer,City>(
    	Integer.class, City.class
  	); 
  	
  	public static City findById(int id) {
        return find.where().eq("id", id).findUnique();
    }
    
    public static void del(int id) {
    	Ebean.createSqlUpdate(
            "delete from cc_city where id = :id "
        ).setParameter("id", id)
         .execute();
    }
    
    public static List<City> findList() {
        return find.where().findList().subList(0, 3);
    }
    
    public static City findByUuid(String uuid) {
        return find.where().eq("uuid", uuid).findUnique();
    }
    
    public static List<City> findAllList() {
        return find.where().findList();
    }
    
    public static List<City> findListByParent(String parentid) {
        return find.where().eq("parentid", parentid).findList();
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

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getNetid() {
		return netid;
	}

	public void setNetid(String netid) {
		this.netid = netid;
	}

	public String getSpell() {
		return spell;
	}

	public void setSpell(String spell) {
		this.spell = spell;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getOrdernumber() {
		return ordernumber;
	}

	public void setOrdernumber(String ordernumber) {
		this.ordernumber = ordernumber;
	}

	public String getRegionid() {
		return regionid;
	}

	public void setRegionid(String regionid) {
		this.regionid = regionid;
	}
	
	
}
