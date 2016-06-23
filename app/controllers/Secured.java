package controllers;

import controllers.routes;
import models.*;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.cache.Cache;
import play.mvc.Security;
import java.util.Date;
import java.util.List;

import common.*;

public class Secured extends Security.Authenticator {
	
	@Override
    public String getUsername(Context ctx) {

        // see if user is logged in
        if (ctx.session().get("username") == null)
            return null;
 		
        // see if the session is expired
        String previousTick = ctx.session().get("userTime");  
        
        if (previousTick != null && !previousTick.equals("")) {
            long previousT = Long.valueOf(previousTick);
            long currentT = new Date().getTime();
            long timeout = Long.valueOf(play.Play.application().configuration().getString("sessionTimeout")) * 1000 * 60;
            
            if ((currentT - previousT) > timeout) {
                // session expired
                ctx.session().clear();
                return null;
            } 
        }
 
        // update time in session
        String tickString = Long.toString(new Date().getTime());
        ctx.session().put("userTime", tickString);
    
        //日志记录
        for (int i = 0; i < CommonConfig.LogList[0].length; i++) {
//    			System.out.println("++"+ctx.request());
//    			System.out.println("++"+CommonConfig.LogList[0][i]);
   			if(ctx.request().toString().contains(CommonConfig.LogList[0][i])){
   				SysLogApplication.saveLog(CommonConfig.LogList[1][i],"对"+CommonConfig.LogList[1][i]+"-"+CommonConfig.LogList[2][i]+"进行操作");
   			}

   		}

        return ctx.session().get("username");
    }
    
    // @Override
//     public String getUsername(Context ctx) {
//         return ctx.session().get("username");
//     }

    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect("/login");
    }
}   
