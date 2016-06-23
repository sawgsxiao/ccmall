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
@Table(name="cc_car_order")
public class CarOrder extends Model{

    @Id
    private int id;
	
	//基础信息
    private String ordercode;
    private String income;
    private String customerid;
    private Date createtime;
    private String deliveryaddress;
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
    private String kfdepart;
    private String kfdpname;
    private String xsdepart;
    private String xsdpname;
    private String ywdepart;
    private String ywdpname;
    private String creator;

    @OneToMany(mappedBy="carOrder",cascade= CascadeType.ALL)
    private List<CarOrderDetail> details;
    
    @OneToMany(mappedBy="carOrder",cascade= CascadeType.ALL)
    private List<CarOrderTrack> tracks;
    
    public CarOrder() {

    }
    
    public static Finder<Integer,CarOrder> find = new Finder<Integer,CarOrder>(
    	Integer.class, CarOrder.class
  	); 
  	
  	public static CarOrder findById(int id) {
        return find.where().eq("id", id).findUnique();
    }
    
    public static List<CarOrder> findAllList() {
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

	public String getDeliveryaddress() {
		return deliveryaddress;
	}

	public void setDeliveryaddress(String deliveryaddress) {
		this.deliveryaddress = deliveryaddress;
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

	public List<CarOrderDetail> getDetails() {
		return details;
	}

	public void setDetails(List<CarOrderDetail> details) {
		this.details = details;
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

	public List<CarOrderTrack> getTracks() {
		return tracks;
	}

	public void setTracks(List<CarOrderTrack> tracks) {
		this.tracks = tracks;
	}

	public String getPaid() {
		return paid;
	}

	public void setPaid(String paid) {
		this.paid = paid;
	}

	public String getKfdepart() {
		return kfdepart;
	}

	public void setKfdepart(String kfdepart) {
		this.kfdepart = kfdepart;
	}

	public String getKfdpname() {
		return kfdpname;
	}

	public void setKfdpname(String kfdpname) {
		this.kfdpname = kfdpname;
	}

	public String getXsdepart() {
		return xsdepart;
	}

	public void setXsdepart(String xsdepart) {
		this.xsdepart = xsdepart;
	}

	public String getXsdpname() {
		return xsdpname;
	}

	public void setXsdpname(String xsdpname) {
		this.xsdpname = xsdpname;
	}

	public String getYwdepart() {
		return ywdepart;
	}

	public void setYwdepart(String ywdepart) {
		this.ywdepart = ywdepart;
	}

	public String getYwdpname() {
		return ywdpname;
	}

	public void setYwdpname(String ywdpname) {
		this.ywdpname = ywdpname;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}    

	
}
