package top.jaiken.view;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import top.jaiken.util.MatAndImgTrans;

@SuppressWarnings("serial")
public class MainView extends JFrame {

	public JPanel LEFT_PANEL;
	public JPanel LRIGHT_PANEL;
	public JLabel VIDEO;
	public JButton START_BUTTON;
	public JButton END_BUTTON;
	public static volatile int flag;
	static {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		flag = 0;
	}

	public MainView() {

		// 设置程序名称
		this.setTitle("世一网业渔网生产质量检测系统");
		this.setBackground(Color.blue);
		// 初始化视图组件
		VIDEO = new JLabel();
		START_BUTTON = new JButton("开始");
		END_BUTTON = new JButton("暂停");

		Image logo = Toolkit.getDefaultToolkit().getImage("image/logo.png");
		this.setIconImage(logo);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setLayout(null);

		/**
		 * VIDEO 获取监控视频图像
		 */
		VIDEO.setBounds(10, 0, 700, 550);
		VIDEO.setSize(700, 550);
		VIDEO.setBackground(Color.RED);
		VIDEO.setIcon(new ImageIcon("image/load.gif"));
		//开启监控摄像头
		Thread getV = new getVideo();
		getV.start();
		/**
		 * Button 开始 & 结束
		 */
		START_BUTTON.setBounds(200, 600, 80, 30);
		START_BUTTON.setBackground(Color.GREEN);
		START_BUTTON.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				flag = 0;

			}
		});

		END_BUTTON.setBounds(380, 600, 80, 30);
		END_BUTTON.setBackground(Color.RED);
		END_BUTTON.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				flag = 1;
			}
		});

		this.setLayout(null);
		this.add(VIDEO);
		this.add(START_BUTTON);
		this.add(END_BUTTON);
		this.pack();
		this.setVisible(true);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	class getVideo extends Thread {
															
		@Override
		public void run() {
			// TODO Auto-generated method stub
			VideoCapture camera = new VideoCapture();
			MatAndImgTrans matAndImgTrans = new MatAndImgTrans();
			camera.open(0);
			if (!camera.isOpened()) {
				System.out.println("Carame is error ,Please check it");
				return;
			} else {
				Mat frame = new Mat();// 创建一个输出帧
				while (true) {
					if (flag == 0) {
						camera.read(frame);// read方法读取摄像头的当前帧
						VIDEO.setIcon(new ImageIcon(matAndImgTrans.Mat2BufImg(frame, ".png")));// 转换图像格式并输出

						try {
							Thread.sleep(100);// 线程暂停100ms
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}else{
						//轮空等待
					}
				}
			}
		}

	}
}
