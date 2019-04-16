package org.jaiken.main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import org.opencv.core.Core;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Main {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	@SuppressWarnings("static-access")
	public static void main(String []args) {
		
		String fileString2="E://Temp/2.jpg";
		Mat mat=new Mat();
		mat=Imgcodecs.imread(fileString2,0);
		Mat dst=new Mat();
		Imgproc.threshold(mat, dst,65, 255, Imgproc.THRESH_BINARY_INV);
		MatOfKeyPoint keypoints=new MatOfKeyPoint();
		Mat keyMat=new Mat();
		FeatureDetector featureDetector=FeatureDetector.create(9);	
		featureDetector.read("params.xml");
		featureDetector.detect(dst, keypoints);
		Features2d features2d=new Features2d();
		features2d.drawKeypoints(dst, keypoints, keyMat,new Scalar(0,0,255),Features2d.DRAW_RICH_KEYPOINTS);
		Imgcodecs.imwrite("image/point_check_temp.png", keyMat);
	
		List<KeyPoint> keyPointsList=keypoints.toList();
		List<Vector<KeyPoint>> points=new ArrayList<Vector<KeyPoint>>();
		Vector<KeyPoint> tempVector=new Vector<KeyPoint>();
		
		tempVector.add(keyPointsList.get(0));
		for(KeyPoint p:keyPointsList) {
			boolean flag=true;
			for(KeyPoint p_temp:tempVector) {
				if(p_temp.pt.x==p.pt.x||Math.abs(p_temp.pt.x-p.pt.x)<200) {
					flag=false;
				}
			}
			if(flag)
					tempVector.add(p);	
		}
		
		
		for(KeyPoint p_index:tempVector) {
			Vector<KeyPoint> t=new Vector<KeyPoint>();
			for(KeyPoint p:keyPointsList) {
				if(p.pt.x==p_index.pt.x||Math.abs(p.pt.x-p_index.pt.x)<200) {
					t.add(p);
				}		
			}
			points.add(t);
		}
		
		
		points.sort(comparatorVector);
		
		for(Vector<KeyPoint> v:points) {
			for(KeyPoint p:v) {
				System.out.println((int)p.pt.x+","+(int)p.pt.y+","+(int)p.size);
			}
		}
		
		
		
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
