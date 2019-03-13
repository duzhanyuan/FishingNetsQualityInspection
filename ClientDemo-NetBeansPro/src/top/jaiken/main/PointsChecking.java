package top.jaiken.main;

import java.io.File;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class PointsChecking {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args) {
		String path = "E://ImageCut_Notime";
		File file = new File(path);
		File[] fileList = file.listFiles();

		for (int i = 0; i < 10; i++) {
			try {
				Mat src = new Mat();
				src = Imgcodecs.imread(fileList[i].toString(),0);
				Mat dst = new Mat();
				Mat dst2=new Mat();
				Mat dst3=new Mat();
				
				
				// 高斯模糊
				// Imgproc.GaussianBlur(src, dst, new Size(9, 9), 2,2);

//				Size size = new Size(25, 25);
//				Point point = new Point(5, 10);
				// Applying Blur effect on the Image
//				Imgproc.blur(src, dst, size, point, Core.BORDER_DEFAULT);

//				Mat kernel1 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size((2 * 2) + 1, (2 * 2) + 1));
//				// Applying erode on the Image
//				Imgproc.erode(src, dst, kernel1);
//
//				Mat kernel2 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size((6 * 6) + 1, (6 * 6) + 1));
//				// Applying dilate on the Image
//				Imgproc.dilate(dst, dst, kernel2);

//				Mat gray = new Mat();
//				Imgproc.cvtColor(dst, gray, Imgproc.COLOR_BGR2GRAY);
				
			
				
				
//				Imgproc.threshold(src, dst, 20, 255, Imgproc.THRESH_BINARY);
				Imgproc.threshold(src, dst2,30, 255, Imgproc.THRESH_BINARY_INV);
			
//				Imgproc.adaptiveThreshold(src, dst3, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 15,
//						12);
//				Imgproc.adaptiveThreshold(src, dst3, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15,
//						12);
//				
				
				Mat edges = new Mat();

				// Detecting the edges
//				Imgproc.Canny(dst3, edges, 60, 60 * 3);
//				Imgcodecs.imwrite("E:/ImagesFS/" + i+3 + ".jpg", dst3);
				Imgcodecs.imwrite("E:/ImagesFS/" + i+2 + ".jpg", dst2);
//				Imgcodecs.imwrite("E:/ImagesFS/" + i + ".jpg", edges);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
