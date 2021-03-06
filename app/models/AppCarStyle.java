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
import javax.persistence.OneToOne;
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
@Table(name="cc_app_car_style")
public class AppCarStyle extends Model{

    @Id
    private int id;
	
	//基础信息
    private String stylename;
    private Date createtime;
    private String price;
    private String stand;
    private String remark;
    private String uuid;
    private String hot;
    private String parrallel;
    private String yuean;
    private String rent;
    private String isloan;
    private String isefficient;
    private String isflash;
    private String iselite;
    private String flashimg;
    private String flashamount;
    
    private Date starttime;
    private Date endtime;
    
    private String sale;
    
    private String discount;
    
    private String engineout;
    
    private String oil;
    
    private String pubdate;
    
    @OneToOne( cascade = CascadeType.ALL)
    @JoinColumn(name="hotcarid")
    private AppHotCar hotCar;
    
    @ManyToOne()
    @JoinColumn(name="carid", nullable=false)
    private AppCar appCar;
    
    @OneToMany(mappedBy="appCarStyle",cascade= CascadeType.ALL)
    private List<AppCarStyleImage> images;
    
    @OneToOne( cascade = CascadeType.ALL)
    @JoinColumn(name="parrallelid")
    private AppParrallelCar parrallelCar;
    
    @OneToOne( cascade = CascadeType.ALL)
    @JoinColumn(name="yueanid")
    private AppYueanCar yueanCar;
    
    
    @OneToOne( cascade = CascadeType.ALL)
    @JoinColumn(name="rentid")
    private AppRentCar rentCar;
    
    public AppCarStyle() {

    }
    
    public static Finder<Integer,AppCarStyle> find = new Finder<Integer,AppCarStyle>(
    	Integer.class, AppCarStyle.class
  	); 
  	
  	public static AppCarStyle findById(int id) {
        return find.where().eq("id", id).findUnique();
    }
    
    public static List<AppCarStyle> findAllList() {
        return find.where().findList();
    }
    
    public static List<AppCarStyle> findAllList(String target) {
    	ExpressionList<AppCarStyle> f= null;
    	if(target.equals("all")){
    		f=find.where().isNotNull("id");
    	}else if(target.equals("isefficient")){
    		f=find.where().eq("isefficient", "1");
    	}else if(target.equals("isflash")){
    		f=find.where().eq("isflash", "1");
    	}else if(target.equals("iselite")){
    		f=find.where().eq("iselite", "1");
    	}
        return f.findList();
    }
    
    public static AppCarStyle findByStyleName(String stylename,String carid) {
        return find.where().eq("stylename", stylename).eq("carid", carid).findUnique();
    }
    
    
    public static AppCarStyle findByStyleUuid(String uuid) {
        return find.where().eq("uuid", uuid).findUnique();
    }
    // Getter and Setter removed for brevity
    public int getId() {
  		return id;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public AppCar getAppCar() {
		return appCar;
	}

	public void setAppCar(AppCar appCar) {
		this.appCar = appCar;
	}

	public String getStylename() {
		return stylename;
	}

	public void setStylename(String stylename) {
		this.stylename = stylename;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getStand() {
		return stand;
	}

	public void setStand(String stand) {
		this.stand = stand;
	}

	public List<AppCarStyleImage> getImages() {
		return images;
	}

	public void setImages(List<AppCarStyleImage> images) {
		this.images = images;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getHot() {
		return hot;
	}

	public void setHot(String hot) {
		this.hot = hot;
	}

	public AppHotCar getHotCar() {
		return hotCar;
	}

	public void setHotCar(AppHotCar hotCar) {
		this.hotCar = hotCar;
	}

	public AppParrallelCar getParrallelCar() {
		return parrallelCar;
	}

	public void setParrallelCar(AppParrallelCar parrallelCar) {
		this.parrallelCar = parrallelCar;
	}

	public String getParrallel() {
		return parrallel;
	}

	public void setParrallel(String parrallel) {
		this.parrallel = parrallel;
	}

	public AppYueanCar getYueanCar() {
		return yueanCar;
	}

	public void setYueanCar(AppYueanCar yueanCar) {
		this.yueanCar = yueanCar;
	}

	public String getYuean() {
		return yuean;
	}

	public void setYuean(String yuean) {
		this.yuean = yuean;
	}

	public AppRentCar getRentCar() {
		return rentCar;
	}

	public void setRentCar(AppRentCar rentCar) {
		this.rentCar = rentCar;
	}

	public String getRent() {
		return rent;
	}

	public void setRent(String rent) {
		this.rent = rent;
	}

	public String getIsloan() {
		return isloan;
	}

	public void setIsloan(String isloan) {
		this.isloan = isloan;
	}

	public String getFlashimg() {
		return flashimg;
	}

	public void setFlashimg(String flashimg) {
		this.flashimg = flashimg;
	}

	public String getFlashamount() {
		return flashamount;
	}

	public void setFlashamount(String flashamount) {
		this.flashamount = flashamount;
	}

	public String getIsefficient() {
		return isefficient;
	}

	public void setIsefficient(String isefficient) {
		this.isefficient = isefficient;
	}

	public String getIsflash() {
		return isflash;
	}

	public void setIsflash(String isflash) {
		this.isflash = isflash;
	}

	public String getIselite() {
		return iselite;
	}

	public void setIselite(String iselite) {
		this.iselite = iselite;
	}


	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	public String getSale() {
		return sale;
	}

	public void setSale(String sale) {
		this.sale = sale;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getEngineout() {
		return engineout;
	}

	public void setEngineout(String engineout) {
		this.engineout = engineout;
	}

	public String getOil() {
		return oil;
	}

	public void setOil(String oil) {
		this.oil = oil;
	}

	public String getPubdate() {
		return pubdate;
	}

	public void setPubdate(String pubdate) {
		this.pubdate = pubdate;
	}
	
	
}
