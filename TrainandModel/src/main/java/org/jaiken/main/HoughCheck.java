package org.jaiken.main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * 
 * @ClassName: MissPointChecking
 * 
 * @Description: TODO HoughCheck
 * 
 * @author: JaikenWong
 * 
 * @date: 2019年1月13日 下午8:51:19
 */
public class HoughCheck {

	public static double DP = 1;
	public static double MINDIST = 280;
	public static double PARAM1 = 300;
	public static double PARAM2 = 1;
	public static int MINRADIUS =10;
	public static int MAXRADIUS =20;


	/**
	 * @method 节点缺失检测
	 * 
	 * @return Mat circles(x,y,r) 节点的位置坐标(x,y)和半径r
	 */
	public Mat getPoints(Mat src) {

		// 灰度图像
		Mat circles = new Mat();
		Imgproc.HoughCircles(src, circles, Imgproc.HOUGH_GRADIENT, DP, MINDIST, PARAM1, PARAM2, MINRADIUS, MAXRADIUS);
		// 测试输出胶点数目
		System.out.println("获取的胶点数目：" + circles.cols());
		// 绘制出胶点位置，以及圆形外轮廓
		for (int i = 0; i < circles.cols(); i++) {
			double[] vCircle = circles.get(0, i);

			Point center = new Point(vCircle[0], vCircle[1]);
			int radius = (int) Math.round(vCircle[2]);

			// Circle center green point(r=3,circle)
			Imgproc.circle(src, center, 3, new Scalar(0, 255, 0), -1, 8, 0);
			// Circle outline red circle r=radius
			Imgproc.circle(src, center, radius, new Scalar(0, 0, 255), 3, 8, 0);
		}
		Date now = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
		Imgcodecs.imwrite("D://Data/CVFishNet/Blob/"+dateFormat.format(now)+".jpg", src);
		return circles;

	}

}
