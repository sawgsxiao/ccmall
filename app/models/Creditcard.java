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
@Table(name="cc_credit_card")
public class Creditcard extends Model{

    @Id
    private int id;
	
	//基础信息
    private int creditquota;
    private String bank;
    private int quotainuser;
    private Date createtime;
    private String company;
    
    @ManyToOne
	@JoinColumn(name="cutomerid", nullable=false)
    private Customer guest;
    
    public Creditcard() {

    }
    
    public static Finder<Integer,Creditcard> find = new Finder<Integer,Creditcard>(
    	Integer.class, Creditcard.class
  	); 
  	
  	public static Creditcard findById(int id) {
        return find.where().eq("id", id).findUnique();
    }
    
    public static List<Creditcard> findAllList() {
        return find.where().findList();
    }
    
    // Getter and Setter removed for brevity
    public int getId() {
  		return id;
	}

	public int getCreditquota() {
		return creditquota;
	}

	public void setCreditquota(int creditquota) {
		this.creditquota = creditquota;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public int getQuotainuser() {
		return quotainuser;
	}

	public void setQuotainuser(int quotainuser) {
		this.quotainuser = quotainuser;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Customer getGuest() {
		return guest;
	}

	public void setGuest(Customer guest) {
		this.guest = guest;
	}

}
