package org.jaiken.tools;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class DeleteFishNetTest {

	public static String filepath=System.getProperty("user.dir")+"/src/main/resources/FishNetTest/Points_r"; 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file=new File(filepath);
		String [] fileList=file.list();
		for(String str:fileList) {
			new File(str).delete();
		}
	}
	/**
	 * 删除上一次剪裁下来的全部点的图片
	 */
	public void deleteAll() {
		File file=new File(filepath);
		String [] fileList=file.list();
		for(String str:fileList) {
			//System.out.println(filepath+"/"+str);
			File temp=new File(filepath+"/"+str);
			try {
				FileUtils.copyFile(temp, new File("E:/xccjsj/points_r/"+System.currentTimeMillis()+".jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			temp.delete();
		}
		
	}

}
