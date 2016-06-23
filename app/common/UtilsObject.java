package common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.UUID; 
import java.io.ByteArrayInputStream;  
import java.io.ByteArrayOutputStream;  
import java.io.InputStream; 

import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.Calendar;
import java.util.ArrayList;  
import java.util.HashMap;  
import java.util.Random;


import controllers.*;

/**
 * 共有类方法
 *
 */
public class UtilsObject {
   
    private static UtilsObject instance=null;
    
    private UtilsObject(){}
    
    public static UtilsObject getInstance(){
        if(instance==null){
            instance= new UtilsObject();
        }
        return instance;
    }
    
    public static String getUUID(){
    
    	UUID uuid = UUID.randomUUID();   
       	String str = uuid.toString();   
       		
       	// 去掉"-"符号   
      	String result = str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);
    	
    	return result;
    }
    
    public static Date addDateOneDay(Date date) {  
        if (null == date) {  
            return date;  
        }  
        Calendar c = Calendar.getInstance();  
        c.setTime(date);   //设置当前日期  
        c.add(Calendar.DATE, 1); //日期加1天  
//     c.add(Calendar.DATE, -1); //日期减1天  
        date = c.getTime();  
        return date;  
    }  
    
    public static String randomNumeric6() {  
    	int count=6;
    	Random random=new Random();
    	
    	char[] cs =new char[6];
    	while(count-->0){
    		cs[count]=(char) (random.nextInt(10)+'0');
    	}
        return new String(cs);  
    }
    
    
  //随机6位验证码
	public static String randomNumeric61() {
        int count = 6;
        char start = '0';
        char end = '9';
 
        Random rnd = new Random();
 
        char[] result = new char[count];
        int len = end - start + 1;
 
        while (count-- > 0) {
            result[count] = (char) (rnd.nextInt(len) + start);
        }
 
        return new String(result);
    }
	
    public static void main(String[] args) {
//		System.out.println(UtilsObject.randomNumeric6());
    	System.out.println(UtilsObject.getPYIndexStr("宝马".substring(0,1).toUpperCase(),UtilsObject.checkStrChinese("宝马")));
    	
    	System.out.println(UtilsObject.getPYIndexStr("阿斯顿.马丁".substring(0,1).toUpperCase(),UtilsObject.checkStrChinese("阿斯顿.马丁")));

	}
    
    
    public static boolean checkStrChinese(String str){
    	
    	if(str==null||str.equals("")){
    		return false;
    	}
    	char[] chs= str.toCharArray();
    	
    	System.out.println(chs[0]);
    	if(Character.toString(chs[0]).matches("[\\u4E00-\\u9FA5]")){

    		return true;
    	}
    	
		return false;
    }
    
    /**

    * 返回首字母

    * @param strChinese

    * @param bUpCase

    * @return

    */

   public static String getPYIndexStr(String strChinese, boolean bUpCase){

       try{

           StringBuffer buffer = new StringBuffer();

           byte b[] = strChinese.getBytes("GBK");//把中文转化成byte数组

           for(int i = 0; i < b.length; i++){

               if((b[i] & 255) > 128){

                   int char1 = b[i++] & 255;

                   char1 <<= 8;//左移运算符用“<<”表示，是将运算符左边的对象，向左移动运算符右边指定的位数，并且在低位补零。其实，向左移n位，就相当于乘上2的n次方

                   int chart = char1 + (b[i] & 255);

                   buffer.append(getPYIndexChar((char)chart, bUpCase));

                   continue;

               }

               char c = (char)b[i];

               if(!Character.isJavaIdentifierPart(c))//确定指定字符是否可以是 Java 标识符中首字符以外的部分。

                   c = 'A';

               buffer.append(c);

           }

           return buffer.toString();

       }catch(Exception e){

           System.out.println((new StringBuilder()).append("\u53D6\u4E2D\u6587\u62FC\u97F3\u6709\u9519").append(e.getMessage()).toString());

       }

       return null;

   }

   /**

    * 得到首字母

    * @param strChinese

    * @param bUpCase

    * @return

    */

   private static char getPYIndexChar(char strChinese, boolean bUpCase){

       int charGBK = strChinese;

       char result;

       if(charGBK >= 45217 && charGBK <= 45252)

           result = 'A';

       else

       if(charGBK >= 45253 && charGBK <= 45760)

           result = 'B';

       else

       if(charGBK >= 45761 && charGBK <= 46317)

           result = 'C';

       else

       if(charGBK >= 46318 && charGBK <= 46825)

           result = 'D';

       else

       if(charGBK >= 46826 && charGBK <= 47009)

           result = 'E';

       else

       if(charGBK >= 47010 && charGBK <= 47296)

           result = 'F';

       else

       if(charGBK >= 47297 && charGBK <= 47613)

           result = 'G';

       else

       if(charGBK >= 47614 && charGBK <= 48118)

           result = 'H';

       else

       if(charGBK >= 48119 && charGBK <= 49061)

           result = 'J';

       else

       if(charGBK >= 49062 && charGBK <= 49323)

           result = 'K';

       else

       if(charGBK >= 49324 && charGBK <= 49895)

           result = 'L';

       else

       if(charGBK >= 49896 && charGBK <= 50370)

           result = 'M';

       else

       if(charGBK >= 50371 && charGBK <= 50613)

           result = 'N';

       else

       if(charGBK >= 50614 && charGBK <= 50621)

           result = 'O';

       else

       if(charGBK >= 50622 && charGBK <= 50905)

           result = 'P';

       else

       if(charGBK >= 50906 && charGBK <= 51386)

           result = 'Q';

       else

       if(charGBK >= 51387 && charGBK <= 51445)

           result = 'R';

       else

       if(charGBK >= 51446 && charGBK <= 52217)

           result = 'S';

       else

       if(charGBK >= 52218 && charGBK <= 52697)

           result = 'T';

       else

       if(charGBK >= 52698 && charGBK <= 52979)

           result = 'W';

       else

       if(charGBK >= 52980 && charGBK <= 53688)

           result = 'X';

       else

       if(charGBK >= 53689 && charGBK <= 54480)

           result = 'Y';

       else

       if(charGBK >= 54481 && charGBK <= 55289)

           result = 'Z';

       else

           result = (char)(65 + (new Random()).nextInt(25));

       if(!bUpCase)

           result = Character.toLowerCase(result);

       return result;

   }
}
