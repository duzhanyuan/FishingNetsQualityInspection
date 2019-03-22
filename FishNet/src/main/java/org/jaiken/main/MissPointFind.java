package org.jaiken.main;

import java.util.ArrayList;
import java.util.Vector;

import org.jaiken.bean.PointsBean;

public class MissPointFind {

	/**
	 * 
	 * @param points
	 * @return true 表示缺失点
	 * 			false 表示并未缺点
	 */
	public boolean isMissingPoint(Vector<PointsBean> points) {

		/*
		 * for(PointsBean p:points) { System.out.print("行："+p.getPoint_cols());
		 * System.out.print("列:"+p.getPoint_rows());
		 * System.out.print("x:"+p.getPoint_x()); System.out.print("y:"+p.getPoint_y());
		 * System.out.println("num:"+p.getPoints_info()); } //return false;
		 */
		
		
		if(points.size()<4)
			return false;
		int space = 0;
		ArrayList<Integer> spaceList = new ArrayList<Integer>();
		for (int i = 0; i < points.size() - 1; i++) {
			PointsBean p = points.get(i);
			if (p.getPoint_cols() == points.get(i + 1).getPoint_cols()) {
				space = points.get(i + 1).getPoint_y() - p.getPoint_y();
				spaceList.add(space);
			} 
		}
		spaceList.sort(null);
		int min = 0;
		int max = 0;
		min = spaceList.get(0);
		max = spaceList.get(spaceList.size() - 1);
		int num_space=0;
		for(int i=1;i<spaceList.size()-1;i++) {
			System.out.println("距离"+spaceList.get(i));
			num_space+=spaceList.get(i);
		}
		if (max - min > num_space/(spaceList.size()-2)) {
			System.out.println(max+","+min);
			return true;
		}else {
			System.out.println(max+","+min);
			return false;
		}
			

	}
}
