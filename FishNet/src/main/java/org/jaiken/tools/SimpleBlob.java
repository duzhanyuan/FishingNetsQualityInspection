package org.jaiken.tools;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class SimpleBlob {

	/**
	 * @TODO Blob分析查找节点的信息，参数见params.xml文件
	 * @param src
	 * @return MatofKeypoint
	 */
	@SuppressWarnings("static-access")
	public MatOfKeyPoint getPointsInfo(Mat src) {
		Mat dst=new Mat();
		Imgproc.threshold(src, dst,35, 255, Imgproc.THRESH_BINARY_INV);
		MatOfKeyPoint keypoints=new MatOfKeyPoint();
		Mat keyMat=new Mat();
		FeatureDetector featureDetector=FeatureDetector.create(9);	
		featureDetector.read("params.xml");
		featureDetector.detect(dst, keypoints);
		Features2d features2d=new Features2d();
		features2d.drawKeypoints(dst, keypoints, keyMat,new Scalar(0,0,255),Features2d.DRAW_RICH_KEYPOINTS);
		Imgcodecs.imwrite("image/point_check_temp.png", keyMat);
		System.out.println(keypoints.cols()+","+keypoints.rows());
		System.out.println(keypoints.get(0, 0).length);
		
		return keypoints;
	}
}
