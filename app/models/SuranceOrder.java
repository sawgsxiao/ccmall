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
@Table(name="cc_surance_order")
public class SuranceOrder extends Model{

    @Id
    private int id;
	
	//基础信息
    private String ordercode;
    private String amount;
    private String customerid;
    private Date createtime;
    private String status;
    private String remainphone;
    private String remark;
    private String customername;
    private String kf;
    private String kfname;
    private String xs;
    private String xsname;
    private String yw;
    private String ywname;
    private String paid;

    @OneToMany(mappedBy="suranceOrder",cascade= CascadeType.ALL)
    private List<SuranceOrderDetail> details;
    
    @OneToMany(mappedBy="suranceOrder",cascade= CascadeType.ALL)
    private List<SuranceOrderTrack> tracks;
    
    public SuranceOrder() {

    }
    
    public static Finder<Integer,SuranceOrder> find = new Finder<Integer,SuranceOrder>(
    	Integer.class, SuranceOrder.class
  	); 
  	
  	public static SuranceOrder findById(int id) {
        return find.where().eq("id", id).findUnique();
    }
    
    public static List<SuranceOrder> findAllList() {
        return find.where().findList();
    }
    
    // Getter and Setter removed for brevity
    public int getId() {
  		return id;
	}

	public String getOrdercode() {
		return ordercode;
	}

	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemainphone() {
		return remainphone;
	}

	public void setRemainphone(String remainphone) {
		this.remainphone = remainphone;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getKf() {
		return kf;
	}

	public void setKf(String kf) {
		this.kf = kf;
	}

	public String getKfname() {
		return kfname;
	}

	public void setKfname(String kfname) {
		this.kfname = kfname;
	}

	public String getXs() {
		return xs;
	}

	public void setXs(String xs) {
		this.xs = xs;
	}

	public String getXsname() {
		return xsname;
	}

	public void setXsname(String xsname) {
		this.xsname = xsname;
	}

	public String getYw() {
		return yw;
	}

	public void setYw(String yw) {
		this.yw = yw;
	}

	public String getYwname() {
		return ywname;
	}

	public void setYwname(String ywname) {
		this.ywname = ywname;
	}

	public List<SuranceOrderDetail> getDetails() {
		return details;
	}

	public void setDetails(List<SuranceOrderDetail> details) {
		this.details = details;
	}

	public List<SuranceOrderTrack> getTracks() {
		return tracks;
	}

	public void setTracks(List<SuranceOrderTrack> tracks) {
		this.tracks = tracks;
	}

	public String getPaid() {
		return paid;
	}

	public void setPaid(String paid) {
		this.paid = paid;
	}
	
	

}
