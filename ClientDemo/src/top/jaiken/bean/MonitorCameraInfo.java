package top.jaiken.bean;

import com.sun.jna.NativeLong;

public class MonitorCameraInfo {
	
		private String CameraIP;
		private int CameraPort;
		private String CameraUserName;
		private String CameraPassWord;
		private NativeLong Channel;
		private NativeLong UserID;
		/**
		 * @return the cameraIP
		 */
		public String getCameraIP() {
			return CameraIP;
		}
		/**
		 * @param cameraIP the cameraIP to set
		 */
		public void setCameraIP(String cameraIP) {
			CameraIP = cameraIP;
		}
		/**
		 * @return the cameraPort
		 */
		public int getCameraPort() {
			return CameraPort;
		}
		/**
		 * @param cameraPort the cameraPort to set
		 */
		public void setCameraPort(int cameraPort) {
			CameraPort = cameraPort;
		}
		/**
		 * @return the cameraUserName
		 */
		public String getCameraUserName() {
			return CameraUserName;
		}
		/**
		 * @param cameraUserName the cameraUserName to set
		 */
		public void setCameraUserName(String cameraUserName) {
			CameraUserName = cameraUserName;
		}
		/**
		 * @return the cameraPassWord
		 */
		public String getCameraPassWord() {
			return CameraPassWord;
		}
		/**
		 * @param cameraPassWord the cameraPassWord to set
		 */
		public void setCameraPassWord(String cameraPassWord) {
			CameraPassWord = cameraPassWord;
		}
		/**
		 * @return the channel
		 */
		public NativeLong getChannel() {
			return Channel;
		}
		/**
		 * @param channel the channel to set
		 */
		public void setChannel(NativeLong channel) {
			Channel = channel;
		}
		/**
		 * @return the userID
		 */
		public NativeLong getUserID() {
			return UserID;
		}
		/**
		 * @param userID the userID to set
		 */
		public void setUserID(NativeLong userID) {
			UserID = userID;
		}
		
	
}
