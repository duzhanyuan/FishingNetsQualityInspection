package org.jaiken.tools;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import com.sun.jna.NativeLong;
import org.jaiken.bean.MonitorCameraInfo;
import org.jaiken.client.HCNetSDK;
import org.jaiken.client.HCNetSDK.NET_DVR_JPEGPARA;
import org.jaiken.client.HCNetSDK.NET_DVR_WORKSTATE_V30;

public class ImageCut {
	
	public HCNetSDK sdk;

	public Mat dst;
	public Mat cameraMatrix;
	public Mat distCoeffs;
	public ImageCut() {
		// 初始化sdk
		sdk = HCNetSDK.INSTANCE;
		if (!sdk.NET_DVR_Init()) {
			System.out.println("SDK初始化失败");
		}
		//初始化去畸变矩阵
		dst=new Mat();
		
		cameraMatrix = new Mat(3, 3, CvType.CV_64F);
		cameraMatrix.put(0, 0, 2.0129 *1000);
		cameraMatrix.put(0, 1, 0);
		cameraMatrix.put(0, 2, 1.3043 *1000);
		cameraMatrix.put(1, 1, 2.0169 *1000);
		cameraMatrix.put(1, 2, 0.7256 *1000);
		cameraMatrix.put(2, 2, 0.0010 *1000);

		distCoeffs = new Mat(4, 1, CvType.CV_64F);
		distCoeffs.put(0, 0,  -0.4341);
		distCoeffs.put(1, 0,  0.1818);
		distCoeffs.put(2, 0, 0);
		distCoeffs.put(3, 0, 0);
		
		
	}

	public Mat getDVRPic(MonitorCameraInfo cameraInfo) {

		// 设置通道号，其中1正常，-1不正常
		NativeLong chanLong = new NativeLong(1);
		cameraInfo.setChannel(chanLong);

		// 返回Boolean值，判断是否获取设备能力
		NET_DVR_WORKSTATE_V30 devwork = new NET_DVR_WORKSTATE_V30();
		if (!sdk.NET_DVR_GetDVRWorkState_V30(cameraInfo.getUserID(), devwork)) {
			System.out.println("返回设备状态失败");
		}

		// JPEG图像信息结构体
		NET_DVR_JPEGPARA jpeg = new NET_DVR_JPEGPARA();
		jpeg.wPicSize = 2;// 设置图片的分辨率
		jpeg.wPicQuality = 2;// 设置图片质量


		// 创建图片目录
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String fileNameString = "E:/ImageCut/" + sdf.format(date) + ".jpg";
		
		boolean is = sdk.NET_DVR_CaptureJPEGPicture(cameraInfo.getUserID(), cameraInfo.getChannel(), jpeg,
				fileNameString);
		if (is) {
			System.out.println("抓取成功!" );
		} else {
			System.out.println("抓取失败：" + sdk.NET_DVR_GetLastError());
		}

		
		Mat src = new Mat();
		src = Imgcodecs.imread(fileNameString,0);
		Mat map1=new Mat();
		Mat map2=new Mat();
		Mat p=new Mat();
	    Size imageSize=new Size(src.cols(),src.rows()) ;
	    //System.out.println(System.currentTimeMillis());
	    Calib3d.fisheye_initUndistortRectifyMap(cameraMatrix, distCoeffs, p,
	        Calib3d.getOptimalNewCameraMatrix(cameraMatrix, distCoeffs, imageSize, 0),
	        imageSize, CvType.CV_16SC2, map1, map2);
	    Imgproc.remap(src, dst, map1, map2, Imgproc.INTER_LINEAR);
	    Imgcodecs.imwrite("image/qjb.jpg", dst);
	    
	    //System.out.println(System.currentTimeMillis());
		
		return dst;
	}
	/**
     * 获取指定时间内的图片
     * @param ffmpegPath (ffmpeg路径)
     * @param videoPath (视频路径)
     * @param imgPath (图片存放路径)
     * @param width (图片宽度)
     * @param height (图片高度)
     * 以下为需要指定的时间
     * @param hour
     * @param min
     * @param sec
     * @return
     */
    public  boolean getImgFromVideo(String videoPath, String imgPath, int width,
            int height, int hour, int min, float sec) {
    	
    	
    	String ffmpegPath="D://ffmpeg/bin/ffmpeg.exe";
    	
    	
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(ffmpegPath, "-y",
                    "-i", videoPath, "-vframes", "1", "-ss", 
                    hour + ":" + min
                            + ":" + sec, "-f", "mjpeg", "-s", width + "*" + height,
                    "-an", imgPath);

            Process process = processBuilder.start();
            InputStream stderr = process.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            while ((br.readLine()) != null) {
            	process.waitFor();
            }
            if (br != null)
                br.close();
            if (isr != null)
                isr.close();
            if (stderr != null)
                stderr.close();
            process.destroy();
            System.gc();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
   
}
