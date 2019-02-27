package top.jaiken.client;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.IntByReference;

import top.jaiken.bean.MonitorCameraInfo;
import top.jaiken.client.HCNetSDK.NET_DVR_DEVICEINFO_V30;
import top.jaiken.client.HCNetSDK.NET_DVR_JPEGPARA;
import top.jaiken.client.HCNetSDK.NET_DVR_WORKSTATE_V30;

public class TestHikvision {
	public static void main(String[] args) {
		TestHikvision app = new TestHikvision();
		MonitorCameraInfo cameraInfo = new MonitorCameraInfo();// 需要新建MonitorCameraInfo类
		cameraInfo.setCameraIP("169.254.2.2");
		cameraInfo.setCameraPort((short) 8000);
		cameraInfo.setCameraUserName("admin");
		cameraInfo.setCameraPassWord("wh123456");
		app.getDVRPic(cameraInfo);
	}

	private void getDVRPic(MonitorCameraInfo cameraInfo) {
		// TODO Auto-generated method stub
		// 设置通道号，其中1正常，-1不正常
		NativeLong chanLong = new NativeLong(1);
		cameraInfo.setChannel(chanLong);

		// 初始化sdk
		HCNetSDK sdk = HCNetSDK.INSTANCE;
		if (!sdk.NET_DVR_Init()) {
			System.out.println("SDK初始化失败");
			return;
		}

		// 注册设备
		NET_DVR_DEVICEINFO_V30 devinfo = new NET_DVR_DEVICEINFO_V30();
		NativeLong id = sdk.NET_DVR_Login_V30(cameraInfo.getCameraIP(), (short) cameraInfo.getCameraPort(),
				cameraInfo.getCameraUserName(), cameraInfo.getCameraPassWord(), devinfo);
		cameraInfo.setUserID(id);
		if (cameraInfo.getUserID().intValue() < 0) {
			System.out.println("设备注册失败" + sdk.NET_DVR_GetLastError());
			return;
		} else {
			System.out.println("id：" + cameraInfo.getUserID().intValue());
		}

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
		String fileNameString = "F:/ImageCut/" + sdf.format(date)  + ".jpg";

		// 设置字节缓存
		ByteBuffer jpegBuffer = ByteBuffer.allocate(1024 * 1024);

		boolean is = sdk.NET_DVR_CaptureJPEGPicture(cameraInfo.getUserID(), cameraInfo.getChannel(), jpeg,
				fileNameString);
		if (is) {
			System.out.println("抓取成功,返回长度：" + a.getValue());
		} else {
			System.out.println("抓取失败：" + sdk.NET_DVR_GetLastError());
		}

		sdk.NET_DVR_Logout(cameraInfo.getUserID());
		sdk.NET_DVR_Cleanup();
	}

}
