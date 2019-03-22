package org.jaiken.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

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

import org.jaiken.bean.MonitorCameraInfo;
import org.jaiken.bean.PointsBean;
import org.jaiken.main.SelectPoints;
import org.jaiken.main.ThresholdValueAnalysis;
import org.jaiken.tools.ImageCut;
import org.opencv.core.Mat;

import com.sun.jna.NativeLong;

public class JDialogAdjustLight extends JFrame {
	static HCNetSDK hCNetSDK = HCNetSDK.INSTANCE;
	NativeLong m_lUserID;// 用户ID
	HCNetSDK.NET_DVR_DEVICEINFO_V30 m_strDeviceInfo;// 设备信息
	HCNetSDK.NET_DVR_WORKSTATE_V30 m_strWorkState;// 工作状态
	MonitorCameraInfo cameraInfo;

	public JDialogAdjustLight(java.awt.Frame parent, boolean modal, NativeLong lUserID,
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

	public void init() {
		// 定义一个监听器，用于监听所有滑动条
		listener = new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				// 取出滑动条的值，并在文本中显示出来
				JSlider source = (JSlider) event.getSource();
				showVal.setText("阈值的值为：" + slider1.getValue()+","+"点集的值为："+slider2.getValue());
			}
		};

		// -----------添加一个阈值滑动条-----------
		slider1 = new JSlider(0, 225);
		// 设置绘制刻度
		slider1.setPaintTicks(true);
		// 设置主、次刻度的间距
		slider1.setMajorTickSpacing(20);
		slider1.setMinorTickSpacing(5);
		slider1.setValue(20);
		addSlider(slider1, "阈值");

		//-----------添加一个收集点的阈值滑动条----------
		slider2=new JSlider(0,1000);
		slider2.setPaintTicks(true);
		slider2.setMajorTickSpacing(100);
		slider2.setMinorTickSpacing(50);
		slider2.setValue(200);
		addSlider(slider2, "点集");
		
		
		showVal = new JTextField("阈值的值为：" + slider1.getValue()+","+"点集的值为："+slider2.getValue());
		jButtonEnter = new JButton("确认");
		jButtonEnter.setBackground(Color.RED);
		jButtonTest = new JButton("测试");
		ImageCut imageCut = new ImageCut();

		jButtonTest.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Mat mat = imageCut.getDVRPic(cameraInfo);
				ThresholdValueAnalysis thresholdValueAnalysis = new ThresholdValueAnalysis();
				SelectPoints selectPoints=new SelectPoints();
				setValue();
				thresholdValueAnalysis.getThresholdValueImg(mat);
				Vector<PointsBean> temp=selectPoints.getPointsInfo(mat);
				JFrameShowCheckResult jFrameShowCheckResult = new JFrameShowCheckResult(temp.size());

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
		setTitle("光线校准");
		pack();

	}

	// 定义一个方法将滑动条的值设置到PointCheck算法中
	private void setValue() {

		int value1 = slider1.getValue();
		ThresholdValueAnalysis.thresholdValue = value1;
		SelectPoints.num_min=slider2.getValue();
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
