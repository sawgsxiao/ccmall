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
@Table(name="cc_verify_code")
public class VerifyCode extends Model{

	private static final long serialVersionUID = 1L;

    @Id
    private int id;
	
	//基础信息
    private String username;
    private String vcode;
    private String uuid;
    private Date createtime;
    
    
    public VerifyCode() {

    }
    
    public static Finder<Integer,VerifyCode> find = new Finder<Integer,VerifyCode>(
    	Integer.class, VerifyCode.class
  	); 
  	
  	public static VerifyCode findById(int id) {
        return find.where().eq("id", id).findUnique();
    }
    
    public static List<VerifyCode> findAllList() {
        return find.where().findList();
    }
    
    public static void del(String username) {
    	Ebean.createSqlUpdate(
            "delete from cc_verify_code where username = :username "
        ).setParameter("username", username)
         .execute();
    }
    
    // Getter and Setter removed for brevity
    public int getId() {
  		return id;
	}

	public String getVcode() {
		return vcode;
	}

	public void setVcode(String vcode) {
		this.vcode = vcode;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}


}
