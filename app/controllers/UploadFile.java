package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.cache.Cache;
import play.mvc.Http.Session;

import views.html.*;
import play.data.Form;
import play.data.DynamicForm;
import play.data.validation.Constraints.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

import models.*;
import common.*;

public class UploadFile extends Controller {
	
	public static Result test(){
		return ok(testupload.render());
	}
	public static Result uploadFile() {
		MultipartFormData body = request().body().asMultipartFormData();
		List<FilePart> lists = body.getFiles();
		
		FilePart oneFile = null;
		String fileName = null;
		String contentType = null;
		File file = null;
		boolean result = true;
		for(int i = 0; i < lists.size(); i++){
			oneFile = lists.get(i);
			if (oneFile != null) {
				fileName = oneFile.getFilename();
				contentType = oneFile.getContentType();
				file = oneFile.getFile();
				if(! moveFile(file.getAbsolutePath(), "d:\\Downloads\\1" + fileName)){
					result = false;
				}
			}else{
				result = false;
			}
		}
		if(result){
			return ok("File uploaded, path:" + file.getAbsolutePath() + contentType + fileName);
		} else {
			flash("error", "Missing file");
			return ok("error");
		}
	}

	private static boolean moveFile(String oldPath, String newPath) {
		try {
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.close();
				oldfile.delete();
				return true;
			}
			//文件不存在
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	private static void recordToDB(){
		
	}
}
