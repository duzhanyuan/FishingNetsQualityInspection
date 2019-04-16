package org.jaiken.tools;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.Point;

public class PointCut {

	Rectangle rect;
	BufferedImage areaImage;

	public BufferedImage getPointsPic(String path, Mat circles) {
		try {

			BufferedImage image = ImageIO.read(new File(path));
			System.out.println(circles.cols());
			for (int i = 0; i < circles.cols(); i++) {

				// 截取图片
				int x = ((int) (circles.get(0, i))[0] - 90) > 0 ? ((int) (circles.get(0, i))[0] - 90) : 0;
				int y = ((int) (circles.get(0, i))[1] - 90) > 0 ? ((int) (circles.get(0, i))[1] - 90) : 0;
				if (x + 180 > image.getWidth()) {
					x = image.getWidth() - 180;
				}
				if (y + 180 > image.getHeight()) {
					y = image.getHeight() - 180;
				}
				int cutH = 180;
				int cutW = 180;
				rect = new Rectangle(x, y, cutW, cutH);
				areaImage = image.getSubimage(rect.x, rect.y, rect.width, rect.height);
				// 新建一个180*180的Image
				BufferedImage buffImg = new BufferedImage(180, 180, BufferedImage.TYPE_INT_RGB);
				buffImg.getGraphics().drawImage(areaImage.getScaledInstance(180, 180, java.awt.Image.SCALE_SMOOTH), 0,
						0, null);
				SimpleDateFormat sdf = new SimpleDateFormat("HHmmssmsms");
				Date date = new Date();
				ImageIO.write(buffImg, "jpg", new File("E:/Points_NT/" + sdf.format(date) + i + ".jpg"));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public BufferedImage getPointsImg(Mat src, List<Vector<KeyPoint>> p) {
		List<Point> points=new ArrayList<Point>();
		for(Vector<KeyPoint> vector:p) {
			for(KeyPoint point:vector) {
				points.add(new Point(point.pt.x,point.pt.y));
			}
		}
		MatAndImgTrans mat2Img=new MatAndImgTrans();
		DeleteFishNetTest deleteFishNetTest=new DeleteFishNetTest();
		deleteFishNetTest.deleteAll();
		try {
			BufferedImage image = mat2Img.Mat2BufImg(src, ".jpg");
			for (int i = 0; i < points.size(); i++) {
				if(points.get(i).x<110||points.get(i).x>image.getWidth()-110)
					continue;
				if(points.get(i).y<110||points.get(i).y>image.getHeight()-110)
					continue;
				// 截取图片
				int x = ((int) (points.get(i).x - 110) > 50 ? (int) (points.get(i).x - 110) : 0);
				int y = ((int) (points.get(i).y - 110) > 50 ? (int) (points.get(i).y - 110) : 0);
				if (x + 220 > image.getWidth()) {
					x = image.getWidth() - 220;
				}
				if (y + 220 > image.getHeight()) {
					y = image.getHeight() - 220;
				}
				int cutH = 220;
				int cutW = 220;
				rect = new Rectangle(x, y, cutW, cutH);
				areaImage = image.getSubimage(rect.x, rect.y, rect.width, rect.height);
				// 新建一个220*220的Image
				BufferedImage buffImg = new BufferedImage(220, 220, BufferedImage.TYPE_INT_RGB);
				buffImg.getGraphics().drawImage(areaImage.getScaledInstance(220, 220, java.awt.Image.SCALE_SMOOTH), 0,
						0, null);
				ImageIO.write(buffImg, "jpg", new File(System.getProperty("user.dir")+"/src/main/resources/FishNetTest/Points_r/"+ i + ".jpg"));
				
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public BufferedImage pointCutbySize_180(String path) {
		BufferedImage buffImg = null;
		try {

			BufferedImage image = ImageIO.read(new File(path));

			for (int i = 0; i < image.getHeight()-180; i+=180) {
				for (int j = 0; j < image.getWidth()-180; j+=180) {
					// 截取图片
					int x = j;
					int y = i;
					int cutH = 180;
					int cutW = 180;
					if(y+180>image.getHeight())
						cutH=image.getHeight()-y;
					if(x+180>image.getWidth())
						cutW=image.getWidth()-x;

					
					rect = new Rectangle(x, y, cutW, cutH);
					areaImage = image.getSubimage(rect.x, rect.y, rect.width, rect.height);
					// 新建一个180*180的Image
					buffImg = new BufferedImage(180, 180, BufferedImage.TYPE_INT_RGB);
					buffImg.getGraphics().drawImage(areaImage.getScaledInstance(180, 180, java.awt.Image.SCALE_SMOOTH),
							0, 0, null);
					//SimpleDateFormat sdf = new SimpleDateFormat("HHmmssmsms");
					//Date date = new Date();
					ImageIO.write(buffImg, "jpg", new File("E:/ImageCut_BySize/" +i+" "+j + ".jpg"));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffImg;
	}

	public BufferedImage pointCutbySize_160(String path) {
		BufferedImage buffImg = null;
		try {

			BufferedImage image = ImageIO.read(new File(path));

			for (int i = 0; i < image.getHeight()-160; i+=160) {
				for (int j = 0; j < image.getWidth()-160; j+=160) {
					// 截取图片
					int x = j;
					int y = i;
					int cutH = 160;
					int cutW = 160;
					if(y+160>image.getHeight())
						cutH=image.getHeight()-y;
					if(x+160>image.getWidth())
						cutW=image.getWidth()-x;

					
					rect = new Rectangle(x, y, cutW, cutH);
					areaImage = image.getSubimage(rect.x, rect.y, rect.width, rect.height);
					// 新建一个180*180的Image
					buffImg = new BufferedImage(160, 160, BufferedImage.TYPE_INT_RGB);
					buffImg.getGraphics().drawImage(areaImage.getScaledInstance(160, 160, java.awt.Image.SCALE_SMOOTH),
							0, 0, null);
					//SimpleDateFormat sdf = new SimpleDateFormat("HHmmssmsms");
					//Date date = new Date();
					ImageIO.write(buffImg, "jpg", new File("E:/ImageCut_BySize_2/" +i+" "+j + ".jpg"));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffImg;
	}	public BufferedImage pointCutbySize_200(String path) {
		BufferedImage buffImg = null;
		try {

			BufferedImage image = ImageIO.read(new File(path));

			for (int i = 0; i < image.getHeight()-200; i+=200) {
				for (int j = 0; j < image.getWidth()-200; j+=200) {
					// 截取图片
					int x = j;
					int y = i;
					int cutH = 200;
					int cutW = 200;
					if(y+200>image.getHeight())
						cutH=image.getHeight()-y;
					if(x+200>image.getWidth())
						cutW=image.getWidth()-x;

					
					rect = new Rectangle(x, y, cutW, cutH);
					areaImage = image.getSubimage(rect.x, rect.y, rect.width, rect.height);
					// 新建一个180*180的Image
					buffImg = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
					buffImg.getGraphics().drawImage(areaImage.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH),
							0, 0, null);
					//SimpleDateFormat sdf = new SimpleDateFormat("HHmmssmsms");
					//Date date = new Date();
					ImageIO.write(buffImg, "jpg", new File("E:/ImageCut_BySize_3/" +i+" "+j + ".jpg"));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffImg;
	}
	
	public void deleteTimeLable(String path) throws IOException, Exception {

		BufferedImage image = ImageIO.read(new File(path));
		rect = new Rectangle(0, 0, 2560, 1350);
		areaImage = image.getSubimage(rect.x, rect.y, rect.width, rect.height);
		// 新建一个180*180的Image
		BufferedImage buffImg = new BufferedImage(2560, 1350, BufferedImage.TYPE_INT_RGB);
		buffImg.getGraphics().drawImage(areaImage.getScaledInstance(2560, 1350, java.awt.Image.SCALE_SMOOTH), 0, 0,
				null);
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmssmsms");
		Date date = new Date();
		Random r = new Random(1);
		int ran1 = r.nextInt(100);
		try {
			ImageIO.write(buffImg, "jpg", new File("E:/ImageCut_NoTime/" + sdf.format(date) + ran1 + ".jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
