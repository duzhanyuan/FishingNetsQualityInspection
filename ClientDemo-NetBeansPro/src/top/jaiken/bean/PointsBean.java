package top.jaiken.bean;

import org.opencv.core.Mat;

public class PointsBean {

	private int point_rows;
	private int point_cols;
	private int point_x;
	private int point_y;
	//(x,y),nums,
	private int points_info;
	public int getPoint_rows() {
		return point_rows;
	}
	public void setPoint_rows(int point_rows) {
		this.point_rows = point_rows;
	}
	
	
	public int getPoint_x() {
		return point_x;
	}
	public void setPoint_x(int point_x) {
		this.point_x = point_x;
	}
	public int getPoint_y() {
		return point_y;
	}
	public void setPoint_y(int point_y) {
		this.point_y = point_y;
	}
	public int getPoint_cols() {
		return point_cols;
	}
	public void setPoint_cols(int point_cols) {
		this.point_cols = point_cols;
	}
	public int getPoints_info() {
		return points_info;
	}
	public void setPoints_info(int points_info) {
		this.points_info = points_info;
	}
	
}
