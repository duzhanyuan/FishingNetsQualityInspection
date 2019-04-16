package org.jaiken.main;

import java.io.File;
import java.util.Vector;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import org.jaiken.bean.PointsBean;
/**
 * @TODO 收集节点的信息
 * @author Administrator
 *
 */
public class SelectPoints {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	public static int num_min = 200;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String pathString = "E://ImageCut_Notime";
		File file = new File(pathString);
		File[] fileList = file.listFiles();

		for (int i = 0; i < 10; i++) {
			// System.out.println(fileList[i].toString());
			Mat src = new Mat();
			src = Imgcodecs.imread(fileList[i].toString(), 0);
			Mat dst = new Mat();
			Imgproc.threshold(src, dst, 30, 255, Imgproc.THRESH_BINARY_INV);
			Imgcodecs.imwrite("E://ImagesFS/" + i + ".jpg", dst);

	
			// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			int num = 0;
			int points_num = 0;
			Vector<PointsBean> points = new Vector<PointsBean>();
			int point_x = 0, point_y = 0;
			int point_cols = 0, point_rows = 0;
			// *****************************************************
			// 阈值分析后的节点检测算法
			// *****************************************************
			int leftp_x = 0;
			while (leftp_x < dst.cols() - 210) {
				// 找到最左侧的点
				flag1: for (int x = leftp_x; x < dst.cols(); x++) {
					for (int y = 0; y < dst.rows(); y++) {
						if (dst.get(y, x)[0] == 255) {
							leftp_x = x;
							point_cols++;
							point_rows = 0;
							break flag1;
						}
					}
				}
				int topp_y = 0;
				flag2: for (int y = topp_y; y < dst.rows(); y++) {
					for (int x = leftp_x; x < ((leftp_x + 200) < dst.cols() ? (leftp_x + 200) : dst.cols()); x++) {
						num = 0;
						// 找到最上方的点
						if (dst.get(y, x)[0] != 0) {
							topp_y = y;
							// 划分扫描域200*50
							for (int xx = leftp_x; xx < ((leftp_x + 200) < dst.cols() ? (leftp_x + 200)
									: dst.cols()); xx++) {
								for (int yy = topp_y; yy < ((topp_y + 50) < dst.rows() ? (topp_y + 50)
										: dst.rows()); yy++) {
									if (dst.get(yy, xx)[0] != 0) {
										num++;
										point_x = xx;
										point_y = yy;
									}
								}
							}
							if (num > num_min) {
								point_rows++;
								PointsBean pointsBean = new PointsBean();
								pointsBean.setPoint_cols(point_cols);
								pointsBean.setPoint_rows(point_rows);
								pointsBean.setPoint_x(point_x);
								pointsBean.setPoint_y(point_y);
								pointsBean.setPoints_info(num);
								points.add(pointsBean);
								points_num++;
							}
							topp_y += 50;
							y = topp_y;

							if (topp_y > dst.rows())
								break flag2;

						}
					}
				}
				leftp_x += 200;
			}
			System.out.println(points_num);

		}

	}

	/**
	 * 
	 * @param src 图像的原始Mat 存储
	 * @return 返回图像上的各个点信息
	 */
	public Vector<PointsBean> getPointsInfo(Mat src) throws NullPointerException {

		int thresholdValue = ThresholdValueAnalysis.thresholdValue;
		Mat dst = new Mat();
		Imgproc.threshold(src, dst, thresholdValue, 255, Imgproc.THRESH_BINARY_INV);

		Imgcodecs.imwrite("E://ImagesFS/" + System.currentTimeMillis() + ".jpg", dst);

		int num = 0;
		int points_num = 0;

		Vector<PointsBean> points = new Vector<PointsBean>();

		int point_x = 0, point_y = 0;
		int point_cols = 0, point_rows = 0;
		// *****************************************************
		// 阈值分析后的节点检测算法
		// *****************************************************
		int leftp_x = 50;
		while (leftp_x < dst.cols() - 210) {
			// 找到最左侧的点
			flag: for (int x = leftp_x; x < dst.cols(); x++) {
				for (int y = 0; y < dst.rows(); y++) {
					if (dst.get(y, x)[0] != 0) {
						leftp_x = x;
						point_cols++;
						point_rows = 0;
						break flag;
					}
				}
			}

			int topp_y = 0;
			flag: for (int y = topp_y; y < dst.rows() - 1; y++) {
				for (int x = leftp_x; x < ((leftp_x + 200) < dst.cols() ? (leftp_x + 200) : dst.cols()); x++) {
					num = 0;
					// 找到最上方的点
					if (y < dst.rows() && dst.get(y, x)[0] != 0) {
						topp_y = y;
						//System.out.println("topp_y:"+topp_y);
						// 划分扫描域200*50
						// 自上到下，自左到右
						for (int yy = topp_y; yy < ((topp_y + 50) < dst.rows() ? (topp_y + 50) : dst.rows()); yy++)
							for (int xx = leftp_x; xx < ((leftp_x + 200) < dst.cols() ? (leftp_x + 200)
									: dst.cols()); xx++) {
								if (dst.get(yy, xx)[0] != 0) {
									num++;
									point_x = xx;
									point_y = yy;
								}
							}

						if (num > num_min) {
							point_rows++;
							PointsBean pointsBean = new PointsBean();
							pointsBean.setPoint_cols(point_cols);
							pointsBean.setPoint_rows(point_rows);
							pointsBean.setPoint_x(point_x);
							pointsBean.setPoint_y(point_y);
							pointsBean.setPoints_info(num);
							points.add(pointsBean);
							points_num++;
						}
						topp_y += 50;
						y = topp_y;

						if (topp_y > dst.rows())
							break flag;

					}
				}
			}
			leftp_x += 200;
		}
		System.out.println(points_num);
		return points;

	}
	public Vector<PointsBean> getPointsInfoByBlob(Mat src){
		
		
		
		
		
		
		
		
		
		return null;
	}

}
