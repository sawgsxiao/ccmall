package common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderThread extends Thread {

	private static long orderNum=01;
	
	private static String date;
	
	
	public static synchronized String getOrderNo(){
		String str=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		
		if(date==null||!date.equals(str)){
			date=str;
			orderNum=01;
		}
		orderNum++;
		
		long orderNo=Long.parseLong((date))*10000;
		
		orderNo+=orderNum;
		
		return orderNo+"";
	}
}
