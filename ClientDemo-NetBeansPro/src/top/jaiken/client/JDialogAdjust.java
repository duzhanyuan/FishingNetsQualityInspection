/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JDialogVoiceTalk.java
 *
 * Created on 2009-11-30, 20:42:14
 */

/**
 *
 * @author Administrator
 */

package top.jaiken.client;

import com.sun.jna.NativeLong;

import top.jaiken.bean.MonitorCameraInfo;
import top.jaiken.main.MissPointChecking;
import top.jaiken.main.ThresholdValueAnalysis;
import top.jaiken.tools.ImageCut;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.opencv.core.Mat;

/**
 * 
 * 
 * @ClassName: JDialogAdjust
 * 
 * @Description: TODO 相机校准
 * 
 * @author: dell
 * 
 * @date: 2019年2月28日 下午7:50:21
 */
public class JDialogAdjust extends JFrame {

	static HCNetSDK hCNetSDK = HCNetSDK.INSTANCE;
	NativeLong m_lUserID;// 用户ID
	HCNetSDK.NET_DVR_DEVICEINFO_V30 m_strDeviceInfo;// 设备信息
	HCNetSDK.NET_DVR_WORKSTATE_V30 m_strWorkState;// 工作状态
	MonitorCameraInfo cameraInfo;

	public JDialogAdjust(java.awt.Frame parent, boolean modal, NativeLong lUserID,
			HCNetSDK.NET_DVR_DEVICEINFO_V30 strDeviceInfo) {
		// super(parent, modal);
		// initComponents()
		cameraInfo = new MonitorCameraInfo();
		cameraInfo.setCameraIP("169.254.2.2");
		cameraInfo.setCameraPort(8000);
		cameraInfo.setCameraUserName("admin");
		cameraInfo.setCameraPassWord("wh123456");
		m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
		lUserID = hCNetSDK.NET_DVR_Login_V30(cameraInfo.getCameraIP(), (short) cameraInfo.getCameraPort(),
				cameraInfo.getCameraUserName(), new String(cameraInfo.getCameraPassWord()), m_strDeviceInfo);
		cameraInfo.setUserID(lUserID);
		init();
	}

	Box sliderBox = new Box(BoxLayout.Y_AXIS);
	JTextField showVal;
	ChangeListener listener;
	JButton jButtonTest;
	JButton jButtonEnter;
	JPanel jPanel;
	JSlider slider1;
	JSlider slider2;
	JSlider slider3;
	JSlider slider4;
	JSlider slider5;
	JSlider slider6;

	public void init() {
		// 定义一个监听器，用于监听所有滑动条
		listener = new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				// 取出滑动条的值，并在文本中显示出来
				JSlider source = (JSlider) event.getSource();
				showVal.setText("当前滑动条的值为：" + source.getValue());
			}
		};
		
		// -----------添加一个普通滑动条-----------
		slider1 = new JSlider(0, 20);
		// 设置绘制刻度
		slider1.setPaintTicks(true);
		// 设置主、次刻度的间距
		slider1.setMajorTickSpacing(20);
		slider1.setMinorTickSpacing(5);
		slider1.setValue((int)(MissPointChecking.DP*10));
		addSlider(slider1, "比值倒数");

		slider2 = new JSlider(0, 500);
		// 设置绘制刻度
		slider2.setPaintTicks(true);
		// 设置主、次刻度的间距
		slider2.setMajorTickSpacing(20);
		slider2.setMinorTickSpacing(5);
		slider2.setValue((int)MissPointChecking.MINDIST);
		addSlider(slider2, "圆心距离");

		slider3 = new JSlider(0, 500);
		// 设置绘制刻度
		slider3.setPaintTicks(true);
		// 设置主、次刻度的间距
		slider3.setMajorTickSpacing(20);
		slider3.setMinorTickSpacing(5);
		slider3.setValue((int)MissPointChecking.PARAM1);
		addSlider(slider3, "检测算子");

		slider4 = new JSlider(0, 20);
		// 设置绘制刻度
		slider4.setPaintTicks(true);
		// 设置主、次刻度的间距
		slider4.setMajorTickSpacing(20);
		slider4.setMinorTickSpacing(5);
		slider4.setValue((int)MissPointChecking.PARAM2);
		addSlider(slider4, "累加器阈值");

		slider5 = new JSlider(0, 50);
		// 设置绘制刻度
		slider5.setPaintTicks(true);
		// 设置主、次刻度的间距
		slider5.setMajorTickSpacing(20);
		slider5.setMinorTickSpacing(5);
		slider5.setValue(MissPointChecking.MINRADIUS);
		addSlider(slider5, "最小半径");

		slider6 = new JSlider(0, 50);
		// 设置绘制刻度
		slider6.setPaintTicks(true);
		// 设置主、次刻度的间距
		slider6.setMajorTickSpacing(20);
		slider6.setMinorTickSpacing(5);
		slider6.setValue(MissPointChecking.MAXRADIUS);
		addSlider(slider6, "最大半径");
		
		showVal = new JTextField("当前滑动条的值为：" + slider1.getValue());
		
		jButtonEnter = new JButton("确认");
		jButtonEnter.setBackground(Color.RED);
		jButtonTest = new JButton("测试");
		ImageCut imageCut = new ImageCut();
		
		jButtonTest.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Mat mat = imageCut.getDVRPic(cameraInfo);
				MissPointChecking missPointChecking = new MissPointChecking();
				ThresholdValueAnalysis thresholdValueAnalysis=new ThresholdValueAnalysis();
				//setValue();
				//missPointChecking.getPoints(mat);
				thresholdValueAnalysis.getThresholdValueImg(mat);
				JFrameShowCheckResult jFrameShowCheckResult = new JFrameShowCheckResult();

			}
		});
		jButtonEnter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setValue();

			}
		});

		jPanel = new JPanel();
		jPanel.add(showVal, BorderLayout.WEST);
		jPanel.add(jButtonTest, BorderLayout.CENTER);
		jPanel.add(jButtonEnter, BorderLayout.EAST);
		add(sliderBox, BorderLayout.CENTER);
		add(jPanel, BorderLayout.SOUTH);
		setTitle("相机校准");
		pack();

	}

	//定义一个方法将滑动条的值设置到PointCheck算法中
	private void setValue() {

		double value1 = (double)slider1.getValue()/10;
		double value2 = slider2.getValue();
		double value3 = slider3.getValue();
		double value4 = slider4.getValue();
		int value5 = slider5.getValue();
		int value6 = slider6.getValue();
		
		System.out.println(value1+","+value2+","+value3+","+value4+","+value5+","+value6);
		MissPointChecking.DP = value1;
		MissPointChecking.MINDIST = value2;
		MissPointChecking.PARAM1 = value3;
		MissPointChecking.PARAM2 = value4;
		MissPointChecking.MINRADIUS = value5;
		MissPointChecking.MAXRADIUS = value6;
	}

	// 定义一个方法，用于将滑动条添加到容器中
	public void addSlider(JSlider slider, String description) {
		slider.addChangeListener(listener);
		Box box = new Box(BoxLayout.X_AXIS);
		box.add(new JLabel(description + "："));
		box.add(slider);
		sliderBox.add(box);
	}

}
