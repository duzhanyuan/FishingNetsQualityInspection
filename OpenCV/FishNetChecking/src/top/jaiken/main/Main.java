package top.jaiken.main;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class Main {

	public static int COLS;
	public static int ROWS;
	public static int POINTS;
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	public static void init (int cols,int rows,int points){
		COLS=cols;
		ROWS=rows;
		POINTS=points;
	}
	
	public static void main(String[] args){
		init(2,2,4);
		String filename = "F:/image/line_2.png";
		Mat mat=Imgcodecs.imread(filename);
		LineBreakChecking lineBreakChecking=new LineBreakChecking();
		System.out.println(lineBreakChecking.isLineBreak(mat));
	}

}