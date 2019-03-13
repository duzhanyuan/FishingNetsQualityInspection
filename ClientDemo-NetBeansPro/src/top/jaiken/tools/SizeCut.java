package top.jaiken.tools;

import java.io.File;

public class SizeCut {

	private static String path;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		path="E://ImageCut_Notime";
		File file = new File(path);
		File[] fileList=file.listFiles();
		PointCut pointCut=new PointCut();
		for (int i = 0; i <1; i++) {
			try {
				pointCut.pointCutbySize_200(fileList[i].toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
