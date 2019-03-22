package org.jaiken.main;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.Params;
import org.opencv.imgcodecs.Imgcodecs;

public class Main {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	@SuppressWarnings("static-access")
	public static void main(String []args) {
		
		String fileString="E://ImageCut/1.jpg";
		Mat mat=new Mat();
		mat=Imgcodecs.imread(fileString);
		Mat dst=new Mat();
		//Imgproc.threshold(mat, dst,40, 255, Imgproc.THRESH_BINARY_INV);
		//Imgcodecs.imwrite("image/point_check_temp.png", dst);
		MatOfKeyPoint keypoints=new MatOfKeyPoint();
		
		Params parmas=new Params();
		parmas.set_filterByArea(true);
		parmas.set_minArea(100f);
		parmas.set_maxArea(1400f);
		
		parmas.set_filterByCircularity(false);
		parmas.set_filterByColor(true);
		
		parmas.set_thresholdStep(10f);
		parmas.set_minThreshold(20);
		parmas.set_maxThreshold(200);
		
		
		parmas.set_filterByConvexity(false);
		parmas.set_filterByInertia(false);
		
		FeatureDetector featureDetector=FeatureDetector.create(9);	
		
		featureDetector.detect(mat, keypoints);
		Features2d features2d=new Features2d();
		features2d.drawKeypoints(mat, keypoints, dst,new Scalar(0,0,255),4);
		Imgcodecs.imwrite("image/point_check_temp.png", dst);
		System.out.print(keypoints.cols());
		
		
	}
}
