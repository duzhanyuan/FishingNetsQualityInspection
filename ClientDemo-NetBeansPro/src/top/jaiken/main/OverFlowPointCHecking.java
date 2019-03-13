package top.jaiken.main;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

/**
 * 

 * @ClassName: OverFlowPointCHecking

 * @Description: TODO

 * @author: JaikenWong

 * @date: 2019年1月22日 下午5:05:38
 */
public class OverFlowPointCHecking {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	/**
	 * 
	 * @Title: isOverFlow()
	 * 
	 * @Description: TODO Check out over flow point. Judgments based on the
	 *               black area will cut down unexpectedly.
	 *               检测依据，黑色溢胶会在远离圆心的位置进行突变，而非膨胀线的渐变方式。
	 * 
	 * @param Mat mat Mat circles
	 * 
	 * @return: true/false
	 */
	@SuppressWarnings("unused")
	public  boolean isOverFlow(Mat mat,Mat circles) {
		
		Mat src = mat;


		Mat dst = new Mat();
		dst = src.clone();

		Mat canny = new Mat();
		Imgproc.Canny(dst, canny, 40, 180, 3, true);

		Mat cannyColor = new Mat();
		Imgproc.cvtColor(canny, cannyColor, Imgproc.COLOR_GRAY2BGR);


		List<Point> centers = new ArrayList<Point>();
		int radius = 0;
		for (int i = 0; i < circles.cols(); i++) {
			// get Points and radius
			double[] vCircle = circles.get(0, i);
			centers.add(new Point(vCircle[0], vCircle[1]));
			radius = (int)vCircle[2];
		}
		/**
		 * 此时胶点并不需要排序，正常按检测到的顺序去检查; 检查圆心p在x方向+-2r，y递增检查 检查圆心p在y方向+-2r,x递增检查
		 * 
		 */
		return false;
	}

	@SuppressWarnings("unused")
	private static boolean overFlow_y(Mat mat, Point p1, int radius) {

		System.out.println(radius);
		int xMin = (int) (p1.x) - 2 * radius;
		int xMax = (int) (p1.x) + 2 * radius;
		int check_Length = 5 * radius;
		int temp = 0;
		int temp_ahead = 0;
		for (int i = (int) (p1.y - check_Length); i < (int) (p1.y + check_Length); i++) {
			int[] lines = new int[2];

			for (int j = xMin; j < xMax; j++) {
				// 最先扫描到的边界
				if (mat.get(i, j)[0] == 255) {
					if (lines[0] == 0)
						lines[0] = j;
					else
						lines[1] = j;
				}

			}
			temp = lines[1] - lines[0];
			if (temp - temp_ahead > 5) {
				return false;
			}
			temp_ahead = temp;

		}
		return true;
	}
	@SuppressWarnings("unused")
	private static boolean overFlow_x(Mat mat,Point p1,int radius){
		//
		return false;
	}
}
