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
import play.data.validation.*;

import com.avaje.ebean.*;
import javax.persistence.*;

import models.*;

@Entity
@Table(name="jc_transport_tools")
public class TransportTools extends Model{
	
    @Id
    private int toolsid;
	
	//基础信息
    private String name;
    private String type;
    private String nums;
    private String price;
    private String remark;
    private Date createtime;
    
    private String userid;
    
    private String toolsno;
    
    private int seating;
    
    private int status;
    
    private int carid;
    
    private String typecode;
    
    private int typeid;
    
    private String sequence;
    
    public TransportTools() {

    }
    
    public static Finder<Integer,TransportTools> find = new Finder<Integer,TransportTools>(
    	Integer.class, TransportTools.class
  	); 
  	
  	public static TransportTools findById(int toolsid) {
        return find.where().eq("toolsid", toolsid).findUnique();
    }
    
    public static List<TransportTools> list() {
    	List<TransportTools> list = find.where().findList();
        return list;
    }
	
	public static void del(int toolsid) {
    	Ebean.createSqlUpdate(
            "delete from jc_transport_tools where toolsid = :toolsid "
        ).setParameter("toolsid", toolsid)
         .execute();
    }
    

    // Getter and Setter removed for brevity    
    public int getToolsid() {
  		return toolsid;
	}
	
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getNums() {
		return nums;
	}
	public void setNums(String nums) {
		this.nums = nums;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
    
    public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getToolsno() {
		return toolsno;
	}

	public void setToolsno(String toolsno) {
		this.toolsno = toolsno;
	}

	public int getSeating() {
		return seating;
	}

	public void setSeating(int seating) {
		this.seating = seating;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getCarid() {
		return carid;
	}

	public void setCarid(int carid) {
		this.carid = carid;
	}

	public String getTypecode() {
		return typecode;
	}

	public void setTypecode(String typecode) {
		this.typecode = typecode;
	}

	public int getTypeid() {
		return typeid;
	}

	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	
}
