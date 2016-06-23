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

import javax.persistence.OneToMany;
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
@Table(name="cc_customer")
public class Customer extends Model{

    @Id
    private int id;
	
	//基础信息
    private String name;
    private String sex;
    private String identity;
    private Date createtime;
    private String type;
    private String phone;
    private String email;
    private String company;
    private Date entrytime;
    private String companyaddress;
    private String job;
    private String salary;
    private String companyphone;
    private String address;
    private Date staytime;
    private String issecurity;
    
    @Column(name="security_company")
    private String securitycompany;
    private String marry;
    private String haschild;
    private String spousename;
    private String spousephone;
    private String cartype;
    private String paidtype;
    private int fristmoney;
    private String integral;
    private String actualintegral;
    private int crmoney;
    private String creffect;
    private String crorigin;
    
    @OneToMany(mappedBy="guest",cascade= CascadeType.ALL)
    private List<Asset> assets;
    
    @OneToMany(mappedBy="guest",cascade= CascadeType.ALL)
    private List<Contact> contacts;
    
    @OneToMany(mappedBy="guest",cascade= CascadeType.ALL)
    private List<Creditcard> creditcards;
    
    public Customer() {

    }
    
    public static Finder<Integer,Customer> find = new Finder<Integer,Customer>(
    	Integer.class, Customer.class
  	); 
  	
  	public static Customer findById(int id) {
        return find.where().eq("id", id).findUnique();
    }
    
    public static List<Customer> findAllList() {
        return find.where().findList();
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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Date getEntrytime() {
		return entrytime;
	}

	public void setEntrytime(Date entrytime) {
		this.entrytime = entrytime;
	}

	public String getCompanyaddress() {
		return companyaddress;
	}

	public void setCompanyaddress(String companyaddress) {
		this.companyaddress = companyaddress;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getCompanyphone() {
		return companyphone;
	}

	public void setCompanyphone(String companyphone) {
		this.companyphone = companyphone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getStaytime() {
		return staytime;
	}

	public void setStaytime(Date staytime) {
		this.staytime = staytime;
	}

	public String getIssecurity() {
		return issecurity;
	}

	public void setIssecurity(String issecurity) {
		this.issecurity = issecurity;
	}

	public String getSecuritycompany() {
		return securitycompany;
	}

	public void setSecuritycompany(String securitycompany) {
		this.securitycompany = securitycompany;
	}

	public String getMarry() {
		return marry;
	}

	public void setMarry(String marry) {
		this.marry = marry;
	}

	public String getHaschild() {
		return haschild;
	}

	public void setHaschild(String haschild) {
		this.haschild = haschild;
	}

	public String getSpousename() {
		return spousename;
	}

	public void setSpousename(String spousename) {
		this.spousename = spousename;
	}

	public String getSpousephone() {
		return spousephone;
	}

	public void setSpousephone(String spousephone) {
		this.spousephone = spousephone;
	}

	public String getCartype() {
		return cartype;
	}

	public void setCartype(String cartype) {
		this.cartype = cartype;
	}

	public String getPaidtype() {
		return paidtype;
	}

	public void setPaidtype(String paidtype) {
		this.paidtype = paidtype;
	}

	public int getFristmoney() {
		return fristmoney;
	}

	public void setFristmoney(int fristmoney) {
		this.fristmoney = fristmoney;
	}

	public String getIntegral() {
		return integral;
	}

	public void setIntegral(String integral) {
		this.integral = integral;
	}

	public String getActualintegral() {
		return actualintegral;
	}

	public void setActualintegral(String actualintegral) {
		this.actualintegral = actualintegral;
	}

	public int getCrmoney() {
		return crmoney;
	}

	public void setCrmoney(int crmoney) {
		this.crmoney = crmoney;
	}

	public String getCreffect() {
		return creffect;
	}

	public void setCreffect(String creffect) {
		this.creffect = creffect;
	}

	public String getCrorigin() {
		return crorigin;
	}

	public void setCrorigin(String crorigin) {
		this.crorigin = crorigin;
	}

	public List<Asset> getAssets() {
		return assets;
	}

	public void setAssets(List<Asset> assets) {
		this.assets = assets;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	public List<Creditcard> getCreditcards() {
		return creditcards;
	}

	public void setCreditcards(List<Creditcard> creditcards) {
		this.creditcards = creditcards;
	}
    
}
