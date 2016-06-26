package controllers;

public class ControllerType {

	/**
	 * cmd 类型定义
	 * code 0：成功,1:参数错误,2：系统异常,3：下发短信失败,4：解析报文错误,5:没有数据,6:没有此请求
	 */
	public static final String QueryTypeList = "list";//获取信息列表-带分页10,分页配置在/conf/application.conf中,pageSize=20
	public static final String QueryTypeDetail = "detail";//获取单个信息详细
	public static final String QueryTypeAdd = "add";//新增信息
	public static final String QueryTypeUpdate = "update";//修改信息
	public static final String QueryTypeOther = "other";//其它操作
	
	//用户
	public static final String Login="login";
	public static final String getSms="sms";
	public static final String Register="register";
	public static final String carAssort="carassort";
	public static final String carList="carlist";
	public static final String car="car";
	
	public static final String queryCarBrand="queryCarBrand";
	
	public static final String queryCars="queryCars";
	
	public static final String advert="advert";
	
	public static final String buyCar="buycar";
	
	public static final String hotCar="hotcar";
	
	public static final String carStyle="carstyle";
	
	public static final String parrallel="parrallel";
	
	public static final String parrallelcar="parrallelcar";
	
	public static final String elitebuycar="elitebuycar";
	
	public static final String parrallelcardetail="parrallelcardetail";
	
	public static final String parrallelbuycar="parrallelbuycar";
	
	public static final String rentCar="rentcar";
	
	public static final String yueanCar="yueancar";
	
	public static final String rentDetail="rentdetail";
	
	public static final String yueanDetail="yueandetail";
	
	public static final String rentBuyCar="rentbuycar";
	
	public static final String yueanBuyCar="yueanbuycar";
	
	
	public static final String qcAdvert="qcadvert";
	
	public static final String cityList="citylist";
	
	public static final String qcBuyCar="qcbuycar";
	
	public static final String qcCarStyle="qccarstyle";
	
}
