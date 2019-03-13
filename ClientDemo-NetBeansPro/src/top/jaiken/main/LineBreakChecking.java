package top.jaiken.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
/**
 * 

 * @ClassName: LineBreakChecking

 * @Description: TODO

 * @author: JaikenWong

 * @date: 2019年1月22日 下午4:39:15
 */
public class LineBreakChecking {

	/**
	 * @Title: isLineBreak
	 * 
	 * @Description: TODO check is there broken lines
	 * 
	 * @param mat
	 * @return true/false
	 */
	
	int cols=10;
	int rows=10;
	public boolean isLineBreak(Mat mat) {
		// String filename = "F:/image/point_5.png";
		Mat src = mat;
		Mat dst = new Mat();
		dst = src.clone();
		Mat canny = new Mat();
		Imgproc.Canny(dst, canny, 50, 200, 3, false);

		Mat cannyColor = new Mat();
		Imgproc.cvtColor(canny, cannyColor, Imgproc.COLOR_GRAY2BGR);

		MissPointChecking missPointChecking = new MissPointChecking();
		Mat circles = missPointChecking.getPoints(cannyColor);
		List<Point> centers = new ArrayList<Point>();
		int radius = 0;
		for (int i = 0; i < circles.cols(); i++) {
			// get Points and radius
			double[] vCircle = circles.get(0, i);
			centers.add(new Point(vCircle[0], vCircle[1]));
			radius = (int) Math.round(vCircle[2]);
		}
		// 对点阵列进行排序，按列逐列获得,下面以2x2的规格为例
		Collections.sort(centers, comparatorx);
		List<Point> temp = centers.subList(0, 2);
		List<Point> temp_next = centers.subList(2, 4);
		Collections.sort(temp, comparatory);
		Collections.sort(temp_next, comparatory);

		for (int i = 0; i < cols - 1; i++) {
			if (!isConnective_Y(temp.get(i), temp.get(i + 1), radius, cannyColor))
				return false;
			if (!isConnective_Y(temp_next.get(i), temp_next.get(i + 1), radius, cannyColor))
				return false;
		}

		for (int i = 0; i < rows - 1; i++) {
			if (!isConnective_X(temp.get(i), temp_next.get(i), radius, cannyColor))
				return false;
			if (!isConnective_X(temp.get(i + 1), temp_next.get(i + 1), radius, cannyColor))
				return false;
		}

		return true;

	}

	/**
	 * @method isConnective_Y 逐列向y轴投影，检测投影是否连续
	 * @param p1
	 * @param p2
	 * @param radius
	 * @param mat
	 * @return true/false
	 */
	private static boolean isConnective_Y(Point p1, Point p2, int radius, Mat mat) {
		if (p1 == null || p2 == null || mat == null) {
			return false;
		}
		int xMin = (int) (p1.x < p2.x ? p1.x : p2.x) - 2 * radius;
		int xMax = (int) (p1.x > p2.x ? p1.x : p2.x) + 2 * radius;

		int hasLine_Flag = 0;
		for (int i = (int) p1.y + 2 * radius; i <= p2.y - 2 * radius; i++) {

			for (int j = xMin; j < xMax; j++) {
				try {
					if (mat.get(i, j)[0] != 0) {
						hasLine_Flag++;
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Exception happened on point(" + j + "," + i + ")");
					return false;
				} finally {
					// 空操作
				}

			}

		}

		if (Math.abs(hasLine_Flag - (int) (p2.y - p1.y - 4 * radius)) <= 2)
			return true;
		else
			return false;

	}

	/**
	 * @method isConnective_X 逐列向X轴投影，检测投影是否连续
	 * @param p1
	 * @param p2
	 * @param radius
	 * @param mat
	 * @return true/false
	 */

	private static boolean isConnective_X(Point p1, Point p2, int radius, Mat mat) {
		if (p1 == null || p2 == null || mat == null) {
			return false;
		}
		int yMin = (int) (p1.y < p2.y ? p1.y : p2.y) - 2 * radius;
		int yMax = (int) (p1.y > p2.y ? p1.y : p2.y) + 2 * radius;

		int hasLine_Flag = 0;
		for (int i = (int) p1.x + 2 * radius; i <= p2.x - 2 * radius; i++) {

			for (int j = yMin; j < yMax; j++) {
				try {
					if (mat.get(j, i)[0] != 0) {
						hasLine_Flag++;
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Exception happened on point(" + i + "," + i + ")");
					return false;
				} finally {
					// 空操作
				}

			}

		}

		if (Math.abs(hasLine_Flag - (int) (p2.x - p1.x - 4 * radius)) <= 2)
			return true;
		else
			return false;

	}

	/**
	 * @see 自定义比较器 comparatorx,以x为比较基准
	 */
	static Comparator<Point> comparatorx = new Comparator<Point>() {
		@Override
		public int compare(Point o1, Point o2) {
			// TODO Auto-generated method stub
			if (o1.x < o2.x) {
				return -1;
			} else if (o1.x == o2.x) {
				return 0;
			} else {
				return 1;
			}
		}
	};
	/**
	 * @see 自定义比较器 comparatory,以y为比较基准
	 */
	static Comparator<Point> comparatory = new Comparator<Point>() {

		@Override
		public int compare(Point o1, Point o2) {
			// TODO Auto-generated method stub
			if (o1.y < o2.y) {
				return -1;
			} else if (o1.y == o2.y) {
				return 0;
			} else {
				return 1;
			}
		}
	};

}
