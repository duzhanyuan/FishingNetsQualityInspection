package org.jaiken.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.jaiken.bean.PointsBean;
import org.opencv.core.KeyPoint;

public class MissPointFind {

	/**
	 * 
	 * @param points
	 * @return true 表示缺失点 false 表示并未缺点
	 */
	public boolean isMissingPoint(Vector<PointsBean> points) {

		if (points.size() < 4)
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
		int num_space = 0;
		for (int i = 1; i < spaceList.size() - 1; i++) {
			System.out.println("距离" + spaceList.get(i));
			num_space += spaceList.get(i);
		}
		if (max - min > (1.5 * num_space / (spaceList.size()))) {
			System.out.println(max + "," + min);
			return true;
		} else {
			System.out.println(max + "," + min);
			return false;
		}

	}

	public boolean isMissing(List<Vector<KeyPoint>> points) {
		if(points==null||points.size()==0)
			return false;
		ArrayList<Integer> spaceList = new ArrayList<Integer>();
		for (Vector<KeyPoint> cols : points) {
			int last_pointof_col_y = (int) cols.get(0).pt.y;
			for (KeyPoint p : cols) {
				// System.out.println((int)p.pt.x+",======,"+(int)p.pt.y);
				if (Math.abs((int) p.pt.y - last_pointof_col_y) >= 100.0) {
					spaceList.add((int) Math.abs((p.pt.y - last_pointof_col_y)));
					last_pointof_col_y = (int) p.pt.y;
				}
			}
		}
		spaceList.sort(null);
		if(spaceList.size()==0) {
			return false;
		}
		int min = 0;
		int max = 0;
		min = spaceList.get(0);
		max = spaceList.get(spaceList.size() - 1);
		int num_space = 0;
		for (int i = 0; i < spaceList.size(); i++) {
			// System.out.println("距离"+spaceList.get(i));
			num_space += spaceList.get(i);
		}
		if (min < 200) {
			System.out.println(min);
		}	
		if (max > (num_space / (spaceList.size())) * 1.5) {
			System.out.println(max);
			return true;
		} else {
			return false;
		}

	}
}
