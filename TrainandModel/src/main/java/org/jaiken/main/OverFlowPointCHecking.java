package org.jaiken.main;

import java.util.List;
import java.util.Vector;
import org.opencv.core.Core;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.Point;

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
	 * @Description: TODO Check out over flow point. 
	 *         
	 * @param Mat mat Mat circles
	 * 
	 * @return: true/false
	 */
	public  boolean isOverFlow(List<Vector<KeyPoint>> points) {
		if(points==null||points.size()==0) {
			return false;
		}
		flag:for(Vector<KeyPoint> v:points) {
			int size_temp=(int)v.get(0).size;
			for(KeyPoint p:v) {
				if(p.pt.x-50<0||p.pt.x+50>2560||p.pt.y-50<0||p.pt.y+50>2560)
					continue;
				if((int)p.size-size_temp>=size_temp*0.8||(int)p.size-size_temp<=-size_temp*0.8){
					System.out.println((int)p.pt.x+","+(int)p.pt.y+",size space ="+(int)Math.abs(p.size-size_temp));
					if((int)Math.abs(p.size-size_temp)<40) {
						continue flag;
					}else {
						return true;
					}
						
					
				}
				size_temp=((int)p.size+size_temp)/2;
			}
		}
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
