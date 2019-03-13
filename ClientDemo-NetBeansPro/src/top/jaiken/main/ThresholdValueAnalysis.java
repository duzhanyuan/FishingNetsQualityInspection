package top.jaiken.main;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ThresholdValueAnalysis {
	public static int thresholdValue=20;
	public void getThresholdValueImg(Mat src) {
		
		Mat dst=new Mat();
		
		Imgproc.threshold(src, dst,30, 255, Imgproc.THRESH_BINARY_INV);
		
		Imgcodecs.imwrite("image/point_check_temp.png", dst);
		
	}
}
