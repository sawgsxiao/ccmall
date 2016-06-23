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
@Table(name="cc_car_product")
public class Car extends Model{

    @Id
    private int id;
	
	//基础信息
    private String carname;
    private String assortcode;
    private String productcode;
    private Date createtime;
    private String remark;
    private String assortname;
    private String pricerange;
    private int stand;
    private String size;
    private String colorlist;
    private String uuid;
    private String code;
    
    @OneToMany(mappedBy="car",cascade= CascadeType.ALL)
    private List<CarImage> images;

    public Car() {

    }
    
    public static Finder<Integer,Car> find = new Finder<Integer,Car>(
    	Integer.class, Car.class
  	); 
  	
  	public static Car findById(int id) {
        return find.where().eq("id", id).findUnique();
    }
    
    public static List<Car> findAllList() {
        return find.where().findList();
    }
    
    public static List<Car> findCars(String code) {
        return find.where().eq("code", code).order("carname").findList();
    }
    
    public static List<Car> findList(String stand) {
        return find.where().eq("stand", stand).findList();
    }
    
    public static Car findByUuid(String uuid) {
        return find.where().eq("uuid", uuid).findUnique();
    }
    // Getter and Setter removed for brevity
    public int getId() {
  		return id;
	}

	public String getAssortcode() {
		return assortcode;
	}

	public void setAssortcode(String assortcode) {
		this.assortcode = assortcode;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCarname() {
		return carname;
	}

	public void setCarname(String carname) {
		this.carname = carname;
	}

	public String getProductcode() {
		return productcode;
	}

	public void setProductcode(String productcode) {
		this.productcode = productcode;
	}

	public List<CarImage> getImages() {
		return images;
	}

	public void setImages(List<CarImage> images) {
		this.images = images;
	}

	public String getAssortname() {
		return assortname;
	}

	public void setAssortname(String assortname) {
		this.assortname = assortname;
	}

	public String getPricerange() {
		return pricerange;
	}

	public void setPricerange(String pricerange) {
		this.pricerange = pricerange;
	}

	public int getStand() {
		return stand;
	}

	public void setStand(int stand) {
		this.stand = stand;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getColorlist() {
		return colorlist;
	}

	public void setColorlist(String colorlist) {
		this.colorlist = colorlist;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	
}
