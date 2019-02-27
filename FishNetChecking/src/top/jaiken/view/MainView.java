package top.jaiken.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
	public JMenuBar MENUBAR;
	public JMenu MENU_SETTING;
	public JTable TABLE;
	public JPanel PANEL;
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
		 * 设置菜单
		 */

		MENUBAR = new JMenuBar();
		this.setJMenuBar(MENUBAR);
		MENU_SETTING = new JMenu("设置");
		MENUBAR.add(MENU_SETTING);
		JMenuItem item1 = new JMenuItem("参数设置");
		MENU_SETTING.add(item1);

		// 表头（列名）
		PANEL = new JPanel();

		Object[] columnNames = { "序号", "机器", "位置", "结果", "是否校验" };
		Object[][] rowData = { { 1, "张三", 80, 80, 80, 240 }, { 2, "John", 70, 80, 90, 240 },
				{ 3, "Sue", 70, 70, 70, 210 }, { 4, "Jane", 80, 70, 60, 210 }, { 5, "Joe_05", 80, 70, 60, 210 },
				{ 6, "Joe_06", 80, 70, 60, 210 }, { 7, "Joe_07", 80, 70, 60, 210 }, { 8, "Joe_08", 80, 70, 60, 210 },
				{ 9, "Joe_09", 80, 70, 60, 210 }, { 10, "Joe_10", 80, 70, 60, 210 }, { 11, "Joe_11", 80, 70, 60, 210 },
				{ 12, "Joe_12", 80, 70, 60, 210 }, { 13, "Joe_13", 80, 70, 60, 210 }, { 14, "Joe_14", 80, 70, 60, 210 },
				{ 15, "Joe_15", 80, 70, 60, 210 }, { 16, "Joe_16", 80, 70, 60, 210 }, { 17, "Joe_17", 80, 70, 60, 210 },
				{ 18, "Joe_18", 80, 70, 60, 210 }, { 19, "Joe_19", 80, 70, 60, 210 },
				{ 20, "Joe_20", 80, 70, 60, 210 } };

		TABLE = new JTable(rowData, columnNames);

		// 设置表格内容颜色
		TABLE.setForeground(Color.BLACK); // 字体颜色
		TABLE.setFont(new Font(null, Font.PLAIN, 14)); // 字体样式
		TABLE.setSelectionForeground(Color.DARK_GRAY); // 选中后字体颜色
		TABLE.setSelectionBackground(Color.LIGHT_GRAY); // 选中后字体背景
		TABLE.setGridColor(Color.GRAY); // 网格颜色

		// 设置表头
		TABLE.getTableHeader().setForeground(Color.RED); // 设置表头名称字体颜色
		TABLE.getTableHeader().setResizingAllowed(false); // 设置不允许手动改变列宽
		TABLE.getTableHeader().setReorderingAllowed(false); // 设置不允许拖动重新排序各列

		// 设置行高
		TABLE.setRowHeight(30);

		// 第一列列宽设置为40
		TABLE.getColumnModel().getColumn(0).setPreferredWidth(40);

		// 设置滚动面板视口大小（超过该大小的行数据，需要拖动滚动条才能看到）
		TABLE.setPreferredScrollableViewportSize(new Dimension(600, 500));

		// 把 表格 放到 滚动面板 中（表头将自动添加到滚动面板顶部）
		JScrollPane scrollPane = new JScrollPane(TABLE);

		// 添加 滚动面板 到 内容面板

		PANEL.add(scrollPane);
		PANEL.setBounds(730, 10, 650, 800);
		this.add(PANEL);

		/**
		 * VIDEO 获取监控视频图像
		 */
		VIDEO.setBounds(10, 0, 700, 550);
		VIDEO.setSize(700, 550);
		VIDEO.setBackground(Color.RED);
		VIDEO.setIcon(new ImageIcon("image/load.gif"));
		// 开启监控摄像头
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
					} else {
						// 轮空等待
					}
				}
			}
		}

	}
}
