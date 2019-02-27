package top.jaiken.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

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
		String fileNameString = "F:/ImageCut/" + sdf.format(date) + ".jpg";

		boolean is = sdk.NET_DVR_CaptureJPEGPicture(cameraInfo.getUserID(), cameraInfo.getChannel(), jpeg,
				fileNameString);
		if (is) {
			System.out.println("抓取成功,返回长度：" + a.getValue());
		} else {
			System.out.println("抓取失败：" + sdk.NET_DVR_GetLastError());
		}
		sdk.NET_DVR_Logout(cameraInfo.getUserID());
		sdk.NET_DVR_Cleanup();

		Mat mat = new Mat();
		mat = Imgcodecs.imread(fileNameString);
		return mat;
	}

}
