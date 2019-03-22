package org.jaiken.main;

import java.util.Vector;

import org.jaiken.bean.PointsBean;
import org.opencv.core.Mat;

public class NetCheck {
	
	/**
	 * @param src  输入截取的图像，以此进行阈值处理，点信息收集，缺失点检测，断线检测，溢胶分析
	 * @return 检测的结果 Point:点缺失;Line:断线;OverFlow:溢胶
	 */
	public String getResult(Mat src) {
		LineBreakChecking lineBreakChecking = new LineBreakChecking();
		SelectPoints selcetPoints = new SelectPoints();
		MissPointFind missPointFind = new MissPointFind();

		Vector<PointsBean> points = new Vector<PointsBean>();

		points = selcetPoints.getPointsInfo(src);
		System.out.println(points.size());
		boolean miss_flag = missPointFind.isMissingPoint(points);
		if (miss_flag) {
			System.out.println("点缺失");
			return new String("Point");
		}
		boolean isLineB_flag = lineBreakChecking.isLineBreak(src, points);
		if (isLineB_flag) {
			System.out.println("断线");
			return new String("Line");
		}
		
		boolean isOver_flag=false;
		if(isOver_flag){
			// 溢胶检测应用模式匹配
			System.out.println("溢胶");
			return new String("OverFlow");
		}

		return "right";

	}
}
