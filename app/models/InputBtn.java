package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InputBtn implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	
	private String name;
	
	private String value;
	
	private String placeholder;
	
	private String position;
	
	private String type ="TEXT";
	
	private List<OptionsBtn> options;
	
	int  selectCount=0;
	

	/*
	 * 选择条件控件类型
	 */
	//TEXT
	public final static String CTYPE_INPUT ="TEXT";
	
	//SELECT
	public final static String CTYPE_SELECT ="SELECT";
	
	//DATE
	public final static String CTYPE_DATE ="DATE";

	
	public InputBtn(){
		
	}
	
	public InputBtn(String id, String name, String value, String placeholder, String position,String type) {
		this.id = id;
		this.name = name;
		this.value = value;
		this.placeholder = placeholder;
		this.position=position;
		this.type=type;
	}

	
	public InputBtn(String id, String name, String value, String placeholder, String position) {
		this.id = id;
		this.name = name;
		this.value = value;
		this.placeholder = placeholder;
		this.position=position;
	}
	
	public InputBtn(String id, String name, String value, String placeholder, String position,List<OptionsBtn> options) {
		this.id = id;
		this.name = name;
		this.value = value;
		this.placeholder = placeholder;
		this.position=position;
		this.options=options;
		this.type=InputBtn.CTYPE_SELECT;
		selectCount++;
	}
	
	public InputBtn(String id, String name, String value, String placeholder, String position,String type,List<OptionsBtn> options) {
		this.id = id;
		this.name = name;
		this.value = value;
		this.placeholder = placeholder;
		this.position=position;
		this.options=options;
		this.type=type;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	} 
	
	
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public List<OptionsBtn> getOptions() {
		return options;
	}

	public void setOptions(OptionsBtn option) {
		List<OptionsBtn> options=new ArrayList<OptionsBtn>();
		options.add(option);
		this.options = options;
	}
	
	
}
