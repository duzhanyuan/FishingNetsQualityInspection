package org.jaiken.tools;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
/**
 * @TODO 相机校准，并去畸变
 * @author Administrator
 *
 */
public class CanmeraCalibration {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	

	public static void main(String[] args) {
		String path = "E://qjb2.jpg";
		Mat src = new Mat();
		src = Imgcodecs.imread(path,0);

		Mat dst=new Mat();
		Mat cameraMatrix = new Mat(3, 3, CvType.CV_64F);
		cameraMatrix.put(0, 0, 2.0129 *1000);
		cameraMatrix.put(0, 1, 0);
		cameraMatrix.put(0, 2, 1.3043 *1000);
		cameraMatrix.put(1, 1, 2.0169 *1000);
		cameraMatrix.put(1, 2, 0.7256 *1000);
		cameraMatrix.put(2, 2, 0.0010 *1000);

		Mat distCoeffs = new Mat(4, 1, CvType.CV_64F);
		distCoeffs.put(0, 0,  -0.4341);
		distCoeffs.put(1, 0,  0.1818);
		distCoeffs.put(2, 0, 0);
		distCoeffs.put(3, 0, 0);
		
		Mat map1=new Mat();
		Mat map2=new Mat();
		Mat p=new Mat();
	    Size imageSize=new Size(src.cols(),src.rows()) ;
	   System.out.println(System.currentTimeMillis());
	    Calib3d.fisheye_initUndistortRectifyMap(cameraMatrix, distCoeffs, p,
	        Calib3d.getOptimalNewCameraMatrix(cameraMatrix, distCoeffs, imageSize, 0),
	        imageSize, CvType.CV_16SC2, map1, map2);
	    Imgproc.remap(src, dst, map1, map2, Imgproc.INTER_LINEAR);
	    System.out.println(System.currentTimeMillis());
		Imgcodecs.imwrite("image/qjb.jpg", dst);

	}

}
