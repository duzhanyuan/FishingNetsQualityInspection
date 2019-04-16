package org.jaiken.tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
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
	public List<Vector<KeyPoint>> getPointsInfo(Mat src) {
		Mat dst = new Mat();
		//增强魯棒性
		Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, 
		        new  Size(9, 9));
		Imgproc.erode(src, src, kernel);
		Imgproc.medianBlur(src, src, 19);
		
		//============================================================
		
		Imgproc.threshold(src, dst, 80, 255, Imgproc.THRESH_BINARY_INV);
		MatOfKeyPoint keypoints = new MatOfKeyPoint();
		Mat keyMat = new Mat();
		FeatureDetector featureDetector = FeatureDetector.create(9);
		featureDetector.read("params.xml");
		//修改测试
		featureDetector.detect(dst, keypoints);
		//====================================
		Features2d features2d = new Features2d();
		features2d.drawKeypoints(dst, keypoints, keyMat, new Scalar(0, 0, 255), Features2d.DRAW_RICH_KEYPOINTS);
		
		Date now = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
		
		Imgcodecs.imwrite("E://0416/blob/"+dateFormat.format(now)+".jpg", keyMat);
		
		List<KeyPoint> keyPointsList = keypoints.toList();
		System.out.println(keyPointsList.size());
		List<Vector<KeyPoint>> points = new ArrayList<Vector<KeyPoint>>();
		Vector<KeyPoint> tempVector = new Vector<KeyPoint>();
		if(keyPointsList.size()==0)
			return null;
		tempVector.add(keyPointsList.get(0));
		for (KeyPoint p : keyPointsList) {
			boolean flag = true;
			for (KeyPoint p_temp : tempVector) {
				if (p_temp.pt.x == p.pt.x || Math.abs(p_temp.pt.x - p.pt.x) < 200) {
					flag = false;
				}
			}
			if (flag)
				tempVector.add(p);
		}

		for (KeyPoint p_index : tempVector) {
			Vector<KeyPoint> t = new Vector<KeyPoint>();
			for (KeyPoint p : keyPointsList) {
				if (p.pt.x == p_index.pt.x || Math.abs(p.pt.x - p_index.pt.x) < 200) {
					t.add(p);
				}
			}
			if(t.size()<6)
				points.add(t);
		}

		points.sort(comparatorVector);
		return points;

	}

	static Comparator<Vector<KeyPoint>> comparatorVector = new Comparator<Vector<KeyPoint>>() {

		@Override
		public int compare(Vector<KeyPoint> v1, Vector<KeyPoint> v2) {
			// TODO Auto-generated method stub
			if (v1.get(0).pt.x < v2.get(0).pt.x) {
				return -1;
			} else if (v1.get(0).pt.x == v2.get(0).pt.x) {
				return 0;
			} else {
				return 1;
			}
		}
	};
}
