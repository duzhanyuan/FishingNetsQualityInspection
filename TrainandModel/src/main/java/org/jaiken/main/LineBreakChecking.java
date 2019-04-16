package org.jaiken.main;

import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import org.jaiken.bean.PointsBean;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * @ClassName: LineBreakChecking
 * 
 * @Description: TODO
 * 
 * @author: JaikenWong
 * 
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
	 * @return true:断线;false:没断线
	 */

	public int radius = 20;

	public boolean isLineBreak(Mat mat, Vector<PointsBean> points) {

		if (points.size() <= 4)
			return false;
		// 做轮廓检测
		Mat cannyMat = new Mat();

		Imgproc.Canny(mat, cannyMat, 20, 20 * 3);
		Imgcodecs.imwrite("E://ImagesFS/canny.jpg", cannyMat);

		int col_temp = points.get(0).getPoint_cols();
		Point p_temp = new Point(points.get(0).getPoint_x(), points.get(0).getPoint_y());

		// 轴方向检测断线
		for (PointsBean p : points) {
			if (p.getPoint_cols() == col_temp) {
				Point p1 = new Point(p.getPoint_x(), p.getPoint_y());
				if(Math.abs(p_temp.x-p1.x)<100&&Math.abs(p_temp.y-p1.y)<100)
					continue;
				boolean isConn_flag = isConnective_Y(p_temp, p1, radius, cannyMat);

				if (!isConn_flag) {
					System.out.println(p_temp + "," + p1);
					return true;
				}

				p_temp = p1;
			} else {
				Point p1 = new Point(p.getPoint_x(), p.getPoint_y());
				p_temp = p1;
				col_temp = p.getPoint_cols();
			}
		}

		// x轴方向检测断线
		int col_temp2 = points.get(0).getPoint_cols();
		flag: for (PointsBean p : points) {
			
			p_temp = new Point(p.getPoint_x(), p.getPoint_y());
			if (p.getPoint_cols() != col_temp2) {
				break flag;
			}
			for (PointsBean pt : points) {
				
				if (p.getPoint_rows() == pt.getPoint_rows()) {
					Point p1 = new Point(pt.getPoint_x(), pt.getPoint_y());
					
					if(Math.abs(p_temp.x-p1.x)<100&&Math.abs(p_temp.y-p1.y)<100)
						continue;
					boolean isConn_flag = isConnective_X(p_temp, p1, radius, cannyMat);
					
					if (!isConn_flag) {
						System.out.println(p_temp + "," + p1);
						//return true;
					}

					p_temp = p1;
				}
			}
			
		}

		return false;

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
		if (p1.x == p2.x && p1.y == p2.y) {
			return true;
		}
		int xMin = (int) (p1.x < p2.x ? p1.x : p2.x) - 2 * radius;
		int xMax = (int) (p1.x > p2.x ? p1.x : p2.x) + 2 * radius;
		if (xMin < 0)
			xMin = 0;
		if (xMax > mat.cols())
			xMax = mat.cols();

		//中间非空像素长度
		int hasLine_Flag = 0;
		for (int i = (int) p1.y + 20; i <= p2.y - 20; i++) {

			for (int j = xMin; j < xMax; j++) {
				try {
					if (mat.get(i, j)[0] != 0&&mat.get(i, j)[0]!=255) {
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

		if (Math.abs(hasLine_Flag - (int) (p2.y - p1.y - 40)) <= 200)
			return true;
		else {

			System.out.println("Y差=" + hasLine_Flag + "Y=" + (p2.y - p1.y));
			return false;

		}

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

		// System.out.println(p1+","+p2);

		if (p1 == null || p2 == null || mat == null) {
			return false;
		}

		if (p1.x == p2.x && p1.y == p2.y) {
			return true;

		}
		int yMin = (int) (p1.y < p2.y ? p1.y : p2.y) - 2 * radius;
		int yMax = (int) (p1.y > p2.y ? p1.y : p2.y) + 2 * radius;
		if (yMin < 0)
			yMin = 0;
		if (yMax > mat.rows())
			yMax = mat.rows();

		int hasLine_Flag = 0;
		for (int i = (int) p1.x + 2 * radius; i <= p2.x - 2 * radius; i++) {

			for (int j = yMin; j < yMax; j++) {
				try {
					if (mat.get(j, i)[0] != 0&&mat.get(j, i)[0]!=255) {
						hasLine_Flag++;
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Exception happened on point(" + i + "," + i + ")");
					return false;
				}
			}

		}

		if (Math.abs(hasLine_Flag - (int) (p2.x - p1.x - 4 * radius)) <= 200)
			return true;
		else {
			
			System.out.println("X差=" + hasLine_Flag + "X=" + (p2.x - p1.x));
			return false;

		}

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

	public boolean isLBreak(Mat src, List<Vector<KeyPoint>> points) {
		if (points.size() <= 4)
			return false;
		// 做轮廓检测
		Mat cannyMat = new Mat();

		Imgproc.Canny(src, cannyMat, 20, 20 * 3);
		for(Vector<KeyPoint> vector:points) {
			KeyPoint temp_p=vector.get(0);
			for(KeyPoint p:vector) {
				Point p1=new Point(temp_p.pt.x,temp_p.pt.y);
				Point p2=new Point(p.pt.x,p.pt.y);
				if(!isConnective_Y(p1,p2,radius,cannyMat)) {
					return true;
				}
				temp_p=p;
			}			
		}		
		return false;
	}

}
