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
@Table(name="cc_contacts")
public class Contact extends Model{

    @Id
    private int id;
	
	//基础信息
    private String contactname;
    private String type;
    private String phone;
    private Date createtime;
    
    @ManyToOne
	@JoinColumn(name="cutomerid", nullable=false)
    private Customer guest;
    private String contactwith;
    
    public Contact() {

    }
    
    public static Finder<Integer,Contact> find = new Finder<Integer,Contact>(
    	Integer.class, Contact.class
  	); 
  	
  	public static Contact findById(int id) {
        return find.where().eq("id", id).findUnique();
    }
    
    public static List<Contact> findAllList() {
        return find.where().findList();
    }
    
    // Getter and Setter removed for brevity
    public int getId() {
  		return id;
	}

	public String getContactname() {
		return contactname;
	}

	public void setContactname(String contactname) {
		this.contactname = contactname;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getContactwith() {
		return contactwith;
	}

	public void setContactwith(String contactwith) {
		this.contactwith = contactwith;
	}

	public Customer getGuest() {
		return guest;
	}

	public void setGuest(Customer guest) {
		this.guest = guest;
	}

}
