package org.jaiken.main;

import java.io.File;
import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class DataClean {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	/**
	 * @TODO 数据清洗
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		Mat src = new Mat();
		Mat circles = new Mat();
		System.out.println(System.currentTimeMillis());
		File picfile = new File("E://ImageCut_Notime");
		File[] pictempFileList = picfile.listFiles();

		for (int i = 0; i < pictempFileList.length; i++) {
			System.out.println(pictempFileList[i].toString());
			try {
				src = Imgcodecs.imread(pictempFileList[i].toString());
				Mat dst = src.clone();
				Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGR2GRAY);
				Imgproc.GaussianBlur(dst, dst, new Size(9, 9), 0);
				Imgproc.HoughCircles(dst, circles, Imgproc.HOUGH_GRADIENT, 1, 280, 60, 1, 15, 25);
				System.out.print(circles.cols());
				for (int j = 0; j < circles.cols(); j++) {

					double[] vCircle = circles.get(0, j);
					Point center = new Point(vCircle[0], vCircle[1]);
					int radius = (int) Math.round(vCircle[2]);
					Imgproc.circle(src, center, 3, new Scalar(0, 255, 0), -1, 8, 0);
					Imgproc.circle(src, center, radius, new Scalar(0, 0, 255), 3, 8, 0);
				}
				Imgcodecs.imwrite("E://ImageHough/" + i + ".png", src);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				// i++;
			}
		}

		System.out.print(System.currentTimeMillis());

		/**
		 * ================================================================================
		 * String path = "E://Points"; String videopathString = "E://2019-03-02"; String
		 * picpath="E://ImageCut_NoTime";
		 * 
		 * 
		 * MissPointChecking missPointChecking = new MissPointChecking(); Mat src = new
		 * Mat(); ImageCut imageCut = new ImageCut(); PointCut pointCut = new
		 * PointCut(); File file = new File(path); File picfile=new File(picpath);
		 * File[] pictempFileList=picfile.listFiles(); /* for (int i =
		 * pictempFileList.length-1; i >=0; i--) {
		 * System.out.println(pictempFileList[i].toString()); try {
		 * pointCut.deleteTimeLable(pictempFileList[i].toString()); } catch (Exception
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); }finally { i--;
		 * } }
		 */

		// =============================================

		/*
		 * File[] tempList = picfile.listFiles(); for (int i = tempList.length-1; i >=0;
		 * i--) { src = Imgcodecs.imread(tempList[i].toString()); Mat circles =
		 * missPointChecking.getPoints(src); System.out.println(tempList[i].toString());
		 * SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); Date date =
		 * new Date(); pointCut.getPointsPic(tempList[i].toString(), circles); }
		 * 
		 * //===============================================
		 * 
		 * 
		 * File[] tempList = file.listFiles(); for (int i = tempList.length-1; i >0;
		 * i--) { if (tempList[i].isFile()) { src =
		 * Imgcodecs.imread(tempList[i].toString()); Mat circles =
		 * missPointChecking.getPoints(src); System.out.println(tempList[i].toString());
		 * SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); Date date =
		 * new Date(); System.out.println(i); String wrongpath = "E://Points_NT_w/" +
		 * sdf.format(date) + i + ".jpg"; String rightpath = "E://Points_NT_r/" +
		 * sdf.format(date) + i + ".jpg"; if (circles.cols() > 0) { // FileInputStream
		 * ins = new FileInputStream(tempList[i]); // FileOutputStream out = new
		 * FileOutputStream(new File(rightpath)); // byte[] b = new byte[1024]; // int n
		 * = 0; // while ((n = ins.read(b)) != -1) { // out.write(b, 0, n); // } // //
		 * ins.close(); // out.close(); } else if(circles.cols()==0) {
		 * 
		 * FileInputStream ins = new FileInputStream(tempList[i]); FileOutputStream out
		 * = new FileOutputStream(new File(wrongpath)); byte[] b = new byte[1024]; int n
		 * = 0; while ((n = ins.read(b)) != -1) { out.write(b, 0, n); }
		 * 
		 * ins.close(); out.close();
		 * 
		 * } } else if (tempList[i].isDirectory()) { System.out.println("文件夹：" +
		 * tempList[i]); } }
		 * 
		 * /* //======================================================= File videofile =
		 * new File(videopathString); // File[] videotempList = videofile.listFiles();
		 * for (int i = 0; i < videotempList.length; i++) { // if
		 * (videotempList[i].isFile()) { // SimpleDateFormat sdf = new
		 * SimpleDateFormat("HHmmssmsms"); // Date date = new Date(); // // for (int j =
		 * 0; j < 60; j++) { float s = 0.1f + j;
		 * 
		 * imageCut.getImgFromVideo(videotempList[i].toString(), "E://ImageCut/" +
		 * sdf.format(date) + i + "" + j + ".jpg", 2560, 1440, 0, 1, s); //
		 * System.out.println(s); } } else if (videotempList[i].isDirectory()) {
		 * System.out.println("文件夹：" + videotempList[i]); } else {
		 * 
		 * } }
		 */
	}
}
