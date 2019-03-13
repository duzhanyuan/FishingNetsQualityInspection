package top.jaiken.main;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * 
 * @ClassName: MissPointChecking
 * 
 * @Description: TODO check missed points(检测点的数目)
 * 
 * @author: JaikenWong
 * 
 * @date: 2019年1月13日 下午8:51:19
 */
public class MissPointChecking {

	public static double DP = 1;
	public static double MINDIST = 280;
	public static double PARAM1 = 300;
	public static double PARAM2 = 3;
	public static int MINRADIUS =10;
	public static int MAXRADIUS =20;


	/**
	 * @method 节点缺失检测
	 * 
	 * @return Mat circles(x,y,r) 节点的位置坐标(x,y)和半径r
	 */
	public Mat getPoints(Mat src) {

		// 灰度图像
		Mat dst = src.clone();
		Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGR2GRAY);
		Mat circles = new Mat();

		/**
		 * HoughCircle检测 函数： Imgproc.HoughCircles(Mat image, Mat circles, int
		 * method, double dp, double minDist, double param1, double param2, int
		 * minRadius, int maxRadius) 参数说明： image：源图像 circles：检测到的圆的输出矢量(x,y,r)
		 * method：使用的检测方法，目前只有一种Imgproc.HOUGH_GRADIENT dp：检测圆心的累加器图像与源图像之间的比值倒数
		 * minDist：检测到的圆的圆心之间的最小距离
		 * param1：method设置的检测方法对应参数，针对HOUGH_GRADIENT，表示边缘检测算子的高阈值（低阈值是高阈值的一半），默认值100
		 * param2：method设置的检测方法对应参数，针对HOUGH_GRADIENT，表示累加器的阈值。值越小，检测到的无关的圆
		 * minRadius：圆半径的最小半径，默认为0
		 * maxRadius：圆半径的最大半径，默认为0（若minRadius和maxRadius都默认为0，则HoughCircles函数会自动计算半径）
		 * ---------------------
		 */
		// 霍夫圆形检测
		Imgproc.HoughCircles(dst, circles, Imgproc.HOUGH_GRADIENT, DP, MINDIST, PARAM1, PARAM2, MINRADIUS, MAXRADIUS);
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
		
		Imgcodecs.imwrite("image/point_check_temp.png", src);
		return circles;

	}

}
