package org.jaiken.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.jaiken.model.FishNetSingleClassification;
import org.jaiken.tools.SimpleBlob;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;

public class NetCheck {

	/**
	 * @param src 输入截取的图像，以此进行阈值处理，点信息收集，缺失点检测，断线检测，溢胶分析
	 * @return 检测的结果 Point:点缺失;Line:断线;OverFlow:溢胶
	 * @throws IOException
	 */
	FishNetSingleClassification f;

	public NetCheck() {
		f = new FishNetSingleClassification();
	}

	public String getResult(List<Mat> src) throws IOException {

		//断线判断
		LineBreakChecking lineBreakChecking = new LineBreakChecking();
		//Blob分析找到点
		SimpleBlob simpleBlob = new SimpleBlob();
		//以节点为中心剪裁图象
		//PointCut pointsCut = new PointCut();
		
		//图像溢胶判断
		OverFlowPointCHecking overFlowPointCHecking = new OverFlowPointCHecking();
		
		//缺点判断
		MissPointFind missPointFind = new MissPointFind();
		
		
		List<Vector<KeyPoint>> points = new ArrayList<Vector<KeyPoint>>();
		points = simpleBlob.getPointsInfo(src.get(0));

		
		boolean miss_flag = missPointFind.isMissing(points);

		if (miss_flag) {
			return new String("Point");
		}
		
		boolean over_flag = overFlowPointCHecking.isOverFlow(points);
		if (over_flag) {
			System.out.println("溢胶");
			return new String("OverFlow");
		}
		
		/*boolean isLineB_flag = lineBreakChecking.isLBreak(src.get(1), points);
		if (isLineB_flag) {
			return new String("Line");
		}*/
		/*pointsCut.getPointsImg(src.get(0), points);
		boolean Over_flag = f.detectWrong(new File(System.getProperty("user.dir") + "/src/main/resources/FishNetTest"));
		if (!Over_flag) {
			// 残缺检测应用模式匹配
			return new String("Error");
		}*/

		return "right";

	}
}
