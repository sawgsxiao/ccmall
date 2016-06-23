package models;

import play.db.ebean.Model;

import java.io.IOException;

import java.util.*;
import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;


@Entity
@Table(name="sys_log")
public class SysLog extends Model{
	
	private static final long serialVersionUID = 1L;
	
    @Id
    private int id;
	
	private String username;
    private String name;
    private String content;
    private String des;
    private Date createtime;
    
    public SysLog() {

    }

    public static Finder<Long,SysLog> find = new Finder<Long,SysLog>(Long.class, SysLog.class);
    
    
   	/**
     * Retrieve a User from authname.
     */
    public static SysLog findById(String id) {
        return find.where().eq("ID", id).findUnique();
    }
    
    // Getter and Setter removed for brevity
	public int getId() {
  		return id;
	}
	
	public String getUsername() {
  		return username;
	}
	
	public void setUsername(String username) {  
        this.username = username;  
    }
	
	public String getName() {
  		return name;
	}
	
	public void setName(String name) {  
        this.name = name;  
    }
    
    public String getContent() {
  		return content;
	}
	
	public void setContent(String content) {  
        this.content = content;  
    }
    
    public String getDes() {
  		return des;
	}
	
	public void setDes(String des) {  
        this.des = des;  
    }
    
    public void setCreatetime(Date createtime) {  
        this.createtime = createtime;  
    }  
    
    public Date getCreatetime() {
  		return createtime;
	}
}
