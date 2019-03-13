package top.jaiken.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.ptr.IntByReference;
import top.jaiken.bean.MonitorCameraInfo;
import top.jaiken.client.HCNetSDK;
import top.jaiken.client.HCNetSDK.NET_DVR_JPEGPARA;
import top.jaiken.client.HCNetSDK.NET_DVR_WORKSTATE_V30;

public class ImageCut {
	
	public HCNetSDK sdk;

	public ImageCut() {
		// 初始化sdk
		sdk = HCNetSDK.INSTANCE;
		if (!sdk.NET_DVR_Init()) {
			System.out.println("SDK初始化失败");
		}
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

		// 设置图片大小
		IntByReference a = new IntByReference();

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
//		sdk.NET_DVR_Logout(cameraInfo.getUserID());
//		sdk.NET_DVR_Cleanup();

		Mat mat = new Mat();
		mat = Imgcodecs.imread(fileNameString);
		return mat;
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
