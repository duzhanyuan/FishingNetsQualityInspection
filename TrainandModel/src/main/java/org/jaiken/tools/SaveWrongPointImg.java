package org.jaiken.tools;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class SaveWrongPointImg {

	public static String filepath=System.getProperty("user.dir")+"/src/main/resources/FishNetTest/Points_r"; 
	
	/**
	 * 保存出现错误的全部点的图片
	 */
	public void SaveAll() {
		File file=new File(filepath);
		String [] fileList=file.list();
		for(String str:fileList) {
			//System.out.println(filepath+"/"+str);
			File temp=new File(filepath+"/"+str);
			try {
				FileUtils.copyFile(temp, new File("E:/xccjsj/points_w/"+System.currentTimeMillis()+".jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
