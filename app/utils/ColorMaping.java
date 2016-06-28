package utils;

public enum ColorMaping {
	red("#8b0000","红色"),white("#ffffff","白色"),black("#000000","黑色");
	
	
	private String color;
	private String name;
	
	ColorMaping(String color,String name){
		this.color=color;
		this.name=name;
	}
	
	public String getColor() {
		return color;
	}

	public String getName() {
		return name;
	}

	public static String getValue(String color){
		String value="红色";
		ColorMaping[] mapings=ColorMaping.values();
		for (int i = 0; i < mapings.length; i++) {
			if(mapings[i].getColor().equals(color)){
				value=mapings[i].getName();
				break;
			}
		}
		return value;
		
	}
}
