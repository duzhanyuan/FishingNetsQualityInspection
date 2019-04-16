package org.jaiken.client;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.LayoutStyle;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.jaiken.bean.MonitorCameraInfo;
import org.jaiken.main.HoughCheck;
import org.jaiken.main.LineBreakChecking;
import org.jaiken.main.NetCheck;
import org.jaiken.tools.ImageCut;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.examples.win32.W32API.HWND;
import com.sun.jna.ptr.IntByReference;

public class ClientFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	static int CAMERA_NUM;
	static int m_DeviceNum;
	static HCNetSDK hCNetSDK = HCNetSDK.INSTANCE;
	static PlayCtrl playControl = PlayCtrl.INSTANCE;
	Vector<HCNetSDK.NET_DVR_DEVICEINFO_V30> m_strDeviceInfo;// 设备信息
	Vector<HCNetSDK.NET_DVR_IPPARACFG> m_strIpparaCfg; // IP参数
	Vector<HCNetSDK.NET_DVR_CLIENTINFO> m_strClientInfo;// 用户参数

	Map<String, Boolean> bRealPlayList;// 是否在预览的<设备的IP ,是否在预览>
	boolean bRealPlay;
	Vector<String> m_sDeviceIPList;// 已登录设备的IP地址

	Vector<NativeLong> lUserIDList;// 用户句柄
	Vector<NativeLong> lPreviewHandleList;// 预览句柄

	int m_iTreeNodeNum; // 通道树节点数目
	DefaultMutableTreeNode m_DeviceRoot; // 通道树根节点

	Vector<MonitorCameraInfo> cameraInfoList; // 相机信息
	ImageCut cut; // 图像剪裁
	HoughCheck houghCheck; // 霍夫检测
	LineBreakChecking lineBreakChecking;// 断线检查
	NetCheck netCheck; // 渔网质量检查
	boolean isChecking; // 当前是否在对整张网检查
	Timer timer; // 定时器
	TimerTask task; // 定时任务

	// =============================窗口组件============================================//
	private JSplitPane jSplitPaneLeft;// 左侧所有
	// 左侧第一个Panel 输入信息和注册预览
	private JPanel jPanelLogin;

	private JLabel jLabelIPAddress;
	private JLabel jLabelPassWord;
	private JLabel jLabelPortNumber;
	private JLabel jLabelUserName;

	private JTextField jTextFieldIPAddress;
	private JPasswordField jPasswordFieldUserPassword;
	private JTextField jTextFieldUserName;
	private JTextField jTextFieldPortNumber;

	private JButton jButtonLogin;
	private JButton jButtonRealPlay;

	// 左侧第二个Panel 注册的相机树
	private JScrollPane jScrollPaneTree;
	private JTree jTreeDevice;

	// 左侧的第三个Panel 开始退出校准按钮
	private JButton jButtonStart;
	private JButton jButtonCheck;
	private JButton jButtonExit;

	// 右侧所有
	private JSplitPane jSplitPaneRight;

	// 右侧第一个,六个相机的预览界面
	private JPanel jPanelRealPlayAll;
	private Panel panelRealPlayU_1;
	private Panel panelRealPlayU_2;
	private Panel panelRealPlayU_3;
	private Panel panelRealPlayD_1;
	private Panel panelRealPlayD_2;
	private Panel panelRealPlayD_3;

	// 右侧第二个，警告列表
	private JScrollPane jScrollPaneTable;
	private JTable jTableAlarm;
	// ==============================================================================//

	public ClientFrame() {

		JPopupMenu.setDefaultLightWeightPopupEnabled(false);// 防止被播放窗口(AWT组件)覆盖
		m_DeviceNum = 0;
		m_sDeviceIPList = new Vector<String>();
		m_strDeviceInfo = new Vector<HCNetSDK.NET_DVR_DEVICEINFO_V30>();
		m_strClientInfo = new Vector<HCNetSDK.NET_DVR_CLIENTINFO>();
		m_strIpparaCfg = new Vector<HCNetSDK.NET_DVR_IPPARACFG>();
		lUserIDList = new Vector<NativeLong>();
		lPreviewHandleList = new Vector<NativeLong>();
		m_iTreeNodeNum = 0;
		isChecking = false;
		cameraInfoList = new Vector<MonitorCameraInfo>();
		cut = new ImageCut();
		houghCheck = new HoughCheck();
		lineBreakChecking = new LineBreakChecking();
		netCheck = new NetCheck();
		CAMERA_NUM = 4;
		initComponents(); // 初始化所有组件
	}

	private void initComponents() {

		// 左侧
		jSplitPaneLeft = new JSplitPane();

		/* 左一 */
		jPanelLogin = new JPanel();
		jLabelIPAddress = new JLabel();
		jLabelPassWord = new JLabel();
		jLabelUserName = new JLabel();
		jLabelPortNumber = new JLabel();
		jTextFieldIPAddress = new JTextField();
		jTextFieldPortNumber = new JTextField();
		jTextFieldUserName = new JTextField();
		jPasswordFieldUserPassword = new JPasswordField();
		jButtonLogin = new JButton();
		jButtonRealPlay = new JButton();

		/* 左二 */
		jScrollPaneTree = new JScrollPane();
		jTreeDevice = new JTree();

		/* 左三 */
		jButtonCheck = new JButton();
		jButtonStart = new JButton();
		jButtonExit = new JButton();

		// 右侧
		jSplitPaneRight = new JSplitPane();
		/* 右一 */
		jPanelRealPlayAll = new JPanel();
		panelRealPlayU_1 = new Panel();
		panelRealPlayU_2 = new Panel();
		panelRealPlayU_3 = new Panel();
		panelRealPlayD_1 = new Panel();
		panelRealPlayD_2 = new Panel();
		panelRealPlayD_3 = new Panel();

		/* 右二 */
		jScrollPaneTable = new JScrollPane();
		jTableAlarm = new JTable();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);// 退出关闭
		setExtendedState(JFrame.MAXIMIZED_BOTH);// 最大化窗体
		setTitle("世一网业渔网生产质量检测系统");// 程序名称
		setFont(new java.awt.Font("宋体", 0, 10));// 名称字体
		Image logo = Toolkit.getDefaultToolkit().getImage("image/logo.png");
		setIconImage(logo);// 设置图标

		jSplitPaneLeft.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
		jSplitPaneLeft.setDividerLocation(155);
		jSplitPaneLeft.setDividerSize(2);

		jPanelLogin.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(204, 255, 255), null));

		jButtonRealPlay.setText("预览");
		jButtonRealPlay.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonRealPlayActionPerformed(evt);
			}
		});

		jButtonLogin.setText("注册");
		jButtonLogin.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonLoginActionPerformed(evt);
			}
		});
		jLabelUserName.setText("用户名");
		jLabelIPAddress.setText("IP地址");
		jTextFieldPortNumber.setText("8000");
		jTextFieldIPAddress.setText("169.254.2.2");
		jLabelPortNumber.setText("端口");
		jLabelPassWord.setText("密码");
		jPasswordFieldUserPassword.setText("wh123456");
		jTextFieldUserName.setText("admin");

		jTreeDevice.setModel(this.initialTreeModel());
		jScrollPaneTree.setViewportView(jTreeDevice);

		jButtonExit.setText("退出");
		jButtonExit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonExitActionPerformed(evt);
			}
		});

		jButtonStart.setText("开始");
		jButtonStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonStartActionPerformed(e);
			}
		});

		jButtonCheck.setText("校准");
		jButtonCheck.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonCheckActionPerformed(evt);
			}
		});

		GroupLayout jPanelUserInfoLayout = new GroupLayout(jPanelLogin);
		jPanelLogin.setLayout(jPanelUserInfoLayout);
		jPanelUserInfoLayout.setHorizontalGroup(jPanelUserInfoLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(jPanelUserInfoLayout.createSequentialGroup().addGap(10, 10, 10).addComponent(jLabelIPAddress)
						.addGap(14, 14, 14)
						.addComponent(jTextFieldIPAddress, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE))
				.addGroup(jPanelUserInfoLayout.createSequentialGroup().addGap(10, 10, 10).addComponent(jLabelUserName)
						.addGap(14, 14, 14)
						.addComponent(jTextFieldUserName, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE))
				.addGroup(jPanelUserInfoLayout.createSequentialGroup().addGap(10, 10, 10).addComponent(jLabelPassWord)
						.addGap(26, 26, 26).addComponent(jPasswordFieldUserPassword, GroupLayout.PREFERRED_SIZE, 80,
								GroupLayout.PREFERRED_SIZE))
				.addGroup(jPanelUserInfoLayout.createSequentialGroup().addContainerGap().addComponent(jButtonLogin)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jButtonRealPlay))
				.addGroup(jPanelUserInfoLayout.createSequentialGroup().addGap(10, 10, 10).addComponent(jLabelPortNumber)
						.addGap(26, 26, 26)
						.addComponent(jTextFieldPortNumber, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE))
				.addGroup(GroupLayout.Alignment.TRAILING,
						jPanelUserInfoLayout.createSequentialGroup().addContainerGap(83, Short.MAX_VALUE)
								.addComponent(jButtonExit).addContainerGap())
				.addGroup(GroupLayout.Alignment.TRAILING,
						jPanelUserInfoLayout.createSequentialGroup().addContainerGap(83, Short.MAX_VALUE)
								.addComponent(jButtonStart).addContainerGap())
				.addGroup(GroupLayout.Alignment.TRAILING,
						jPanelUserInfoLayout.createSequentialGroup().addContainerGap(83, Short.MAX_VALUE)
								.addComponent(jButtonCheck).addContainerGap())
				.addComponent(jScrollPaneTree, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE));

		jPanelUserInfoLayout.setVerticalGroup(jPanelUserInfoLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(jPanelUserInfoLayout.createSequentialGroup().addGap(18, 18, 18)
						.addGroup(jPanelUserInfoLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(jLabelIPAddress).addComponent(jTextFieldIPAddress,
										GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGap(9, 9, 9)
						.addGroup(jPanelUserInfoLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(jLabelUserName).addComponent(jTextFieldUserName,
										GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGap(9, 9, 9)
						.addGroup(jPanelUserInfoLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(jLabelPassWord).addComponent(jPasswordFieldUserPassword,
										GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGap(9, 9, 9)
						.addGroup(jPanelUserInfoLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(jLabelPortNumber).addComponent(jTextFieldPortNumber,
										GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(jPanelUserInfoLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(jButtonLogin).addComponent(jButtonRealPlay))
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(jScrollPaneTree, GroupLayout.PREFERRED_SIZE, 500, GroupLayout.PREFERRED_SIZE)
						.addGap(10, 10, 10).addComponent(jButtonCheck).addGap(10, 10, 10).addComponent(jButtonExit)
						.addGap(10, 10, 10).addComponent(jButtonStart).addContainerGap()));

		jSplitPaneLeft.setLeftComponent(jPanelLogin);

		jSplitPaneRight.setDividerLocation(579);
		jSplitPaneRight.setDividerSize(2);
		jSplitPaneRight.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

		jPanelRealPlayAll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 255, 102)));
		panelRealPlayU_1.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				// panelRealplayMousePressed(evt);
			}
		});
		panelRealPlayU_2.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				// panelRealplayMousePressed(evt);
			}
		});
		panelRealPlayU_3.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				// panelRealplayMousePressed(evt);
			}
		});
		panelRealPlayD_1.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				// panelRealplayMousePressed(evt);
			}
		});
		panelRealPlayD_2.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				// panelRealplayMousePressed(evt);
			}
		});
		panelRealPlayD_3.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				// panelRealplayMousePressed(evt);
			}
		});

		panelRealPlayU_1.setBackground(Color.DARK_GRAY);
		panelRealPlayU_2.setBackground(Color.GRAY);
		panelRealPlayU_3.setBackground(Color.white);
		panelRealPlayD_1.setBackground(Color.BLACK);

		panelRealPlayD_2.setBackground(Color.DARK_GRAY);
		panelRealPlayD_3.setBackground(Color.GRAY);

		GridLayout gridLayout;
		if (CAMERA_NUM < 4) {
			gridLayout = new GridLayout(1, CAMERA_NUM);

		} else {
			gridLayout = new GridLayout(2, CAMERA_NUM / 2);
		}
		jPanelRealPlayAll.setLayout(gridLayout);
		jPanelRealPlayAll.setBackground(Color.blue);

		switch (CAMERA_NUM) {
		case 1:
			jPanelRealPlayAll.add(panelRealPlayU_1);
			break;
		case 2:
			jPanelRealPlayAll.add(panelRealPlayU_1);
			jPanelRealPlayAll.add(panelRealPlayU_2);
			break;
		case 3:
			jPanelRealPlayAll.add(panelRealPlayU_1);
			jPanelRealPlayAll.add(panelRealPlayU_2);
			jPanelRealPlayAll.add(panelRealPlayU_3);
			break;
		case 4:
			jPanelRealPlayAll.add(panelRealPlayU_1);
			jPanelRealPlayAll.add(panelRealPlayU_2);
			jPanelRealPlayAll.add(panelRealPlayU_3);
			jPanelRealPlayAll.add(panelRealPlayD_1);
			break;
		default:
			jPanelRealPlayAll.add(panelRealPlayU_1);
			jPanelRealPlayAll.add(panelRealPlayU_2);
			jPanelRealPlayAll.add(panelRealPlayU_3);
			jPanelRealPlayAll.add(panelRealPlayD_1);
			jPanelRealPlayAll.add(panelRealPlayD_2);
			jPanelRealPlayAll.add(panelRealPlayD_3);
			break;
		}

		jSplitPaneRight.setTopComponent(jPanelRealPlayAll);

		jScrollPaneTree.setBorder(BorderFactory.createEtchedBorder());

		jScrollPaneTable.setBorder(BorderFactory.createEtchedBorder());

		jTableAlarm.setModel(this.initialTableModel());
		jScrollPaneTable.setViewportView(jTableAlarm);

		jSplitPaneRight.setRightComponent(jScrollPaneTable);

		jSplitPaneLeft.setRightComponent(jSplitPaneRight);

		add(jSplitPaneLeft);
		pack();
		setVisible(true);

	}

	// ==============================退出键监听===============================//
	private void jButtonExitActionPerformed(java.awt.event.ActionEvent evt) {
		// 如果在预览,先停止预览, 释放句柄
		for (NativeLong lPreviewHandle : lPreviewHandleList) {
			if (lPreviewHandle.longValue() > -1) {
				hCNetSDK.NET_DVR_StopRealPlay(lPreviewHandle);

			}
		}
		// 如果已经注册,注销
		for (NativeLong lUserID : lUserIDList) {
			if (lUserID.longValue() > -1) {
				hCNetSDK.NET_DVR_Logout_V30(lUserID);
			}
		}
		// cleanup SDK
		hCNetSDK.NET_DVR_Cleanup();

		// 窗口关闭
		this.dispose();
	}

	// ==============================注册按钮监听=================================//
	private void jButtonLoginActionPerformed(java.awt.event.ActionEvent evt) {
		if (m_DeviceNum == 0) {
			m_sDeviceIPList.add(jTextFieldIPAddress.getText()); // 设备ip地址
			m_strDeviceInfo.add(new HCNetSDK.NET_DVR_DEVICEINFO_V30()); // 设备信息
			int iPort = Integer.parseInt(jTextFieldPortNumber.getText());

			lUserIDList
					.add(hCNetSDK.NET_DVR_Login_V30(m_sDeviceIPList.get(0), (short) iPort, jTextFieldUserName.getText(),
							new String(jPasswordFieldUserPassword.getPassword()), m_strDeviceInfo.get(0)));

			long userID = lUserIDList.get(0).longValue();
			if (userID == -1) {
				m_sDeviceIPList.set(m_sDeviceIPList.size() - 1, "");// 登录未成功,IP置为空
				JOptionPane.showMessageDialog(ClientFrame.this, "注册失败");
			} else {

				MonitorCameraInfo cameraInfo = new MonitorCameraInfo();

				cameraInfo.setCameraIP(m_sDeviceIPList.get(0));
				cameraInfo.setCameraPort(iPort);
				cameraInfo.setCameraUserName(jTextFieldUserName.getText());
				cameraInfo.setCameraPassWord(new String(jPasswordFieldUserPassword.getPassword()));
				cameraInfo.setUserID(lUserIDList.get(0));

				cameraInfoList.add(cameraInfo);
				m_DeviceNum++;
				CreateDeviceTree();
			}
		} else {
			// 判断IP 是否已经存在
			String ipString = jTextFieldIPAddress.getText();
			for (String str : m_sDeviceIPList) {
				if (str.equals(ipString)) {
					JOptionPane.showMessageDialog(ClientFrame.this, "请勿重复注册");
					return;
				}

			}

			m_sDeviceIPList.add(jTextFieldIPAddress.getText()); // 设备ip地址
			m_strDeviceInfo.add(new HCNetSDK.NET_DVR_DEVICEINFO_V30()); // 设备信息
			int iPort = Integer.parseInt(jTextFieldPortNumber.getText());

			lUserIDList.add(hCNetSDK.NET_DVR_Login_V30(m_sDeviceIPList.get(m_sDeviceIPList.size() - 1), (short) iPort,
					jTextFieldUserName.getText(), new String(jPasswordFieldUserPassword.getPassword()),
					m_strDeviceInfo.get(m_strDeviceInfo.size() - 1)));

			long userID = lUserIDList.get(lUserIDList.size() - 1).longValue();
			if (userID == -1) {
				m_sDeviceIPList.set(m_sDeviceIPList.size() - 1, "");// 登录未成功,IP置为空
				JOptionPane.showMessageDialog(ClientFrame.this, "注册失败");
			} else {

				MonitorCameraInfo cameraInfo = new MonitorCameraInfo();

				cameraInfo.setCameraIP(m_sDeviceIPList.get(m_sDeviceIPList.size() - 1));
				cameraInfo.setCameraPort(iPort);
				cameraInfo.setCameraUserName(jTextFieldUserName.getText());
				cameraInfo.setCameraPassWord(new String(jPasswordFieldUserPassword.getPassword()));
				cameraInfo.setUserID(lUserIDList.get(lUserIDList.size() - 1));

				cameraInfoList.add(cameraInfo);
				m_DeviceNum++;
				CreateDeviceTree();
			}
		}

	}

	// =================================预览按钮====================================//
	private void jButtonRealPlayActionPerformed(java.awt.event.ActionEvent evt) {

		if (lUserIDList.size() == 0) {
			JOptionPane.showMessageDialog(this, "请先注册");
			return;
		}

		// 如果预览窗口没打开,不在预览
		if (bRealPlay == false) {
			// 获取窗口句柄
			HWND hwnd1 = new HWND(Native.getComponentPointer(panelRealPlayU_1));
			HWND hwnd2 = new HWND(Native.getComponentPointer(panelRealPlayU_2));
			HWND hwnd3 = new HWND(Native.getComponentPointer(panelRealPlayU_3));
			HWND hwnd4 = new HWND(Native.getComponentPointer(panelRealPlayD_1));

			// 获取通道号
			int iChannelNum = 1;// 通道号
			if (iChannelNum == -1) {
				JOptionPane.showMessageDialog(this, "请选择要预览的通道");
				return;
			}

			HCNetSDK.NET_DVR_CLIENTINFO client1 = new HCNetSDK.NET_DVR_CLIENTINFO();
			client1.lChannel = new NativeLong(iChannelNum);
			m_strClientInfo.add(client1);

			HCNetSDK.NET_DVR_CLIENTINFO client2 = new HCNetSDK.NET_DVR_CLIENTINFO();
			client2.lChannel = new NativeLong(iChannelNum);
			m_strClientInfo.add(client2);

			HCNetSDK.NET_DVR_CLIENTINFO client3 = new HCNetSDK.NET_DVR_CLIENTINFO();
			client3.lChannel = new NativeLong(iChannelNum);
			m_strClientInfo.add(client3);

			HCNetSDK.NET_DVR_CLIENTINFO client4 = new HCNetSDK.NET_DVR_CLIENTINFO();
			client4.lChannel = new NativeLong(iChannelNum);
			m_strClientInfo.add(client4);

			client1.hPlayWnd = hwnd1;
			client2.hPlayWnd = hwnd2;
			client3.hPlayWnd = hwnd3;
			client4.hPlayWnd = hwnd4;

			NativeLong lPreviewHandle1 = hCNetSDK.NET_DVR_RealPlay_V30(lUserIDList.get(0), client1, null, null, true);
			NativeLong lPreviewHandle2 = hCNetSDK.NET_DVR_RealPlay_V30(lUserIDList.get(1), client2, null, null, true);
			NativeLong lPreviewHandle3 = hCNetSDK.NET_DVR_RealPlay_V30(lUserIDList.get(2), client3, null, null, true);
			NativeLong lPreviewHandle4 = hCNetSDK.NET_DVR_RealPlay_V30(lUserIDList.get(3), client4, null, null, true);

			lPreviewHandleList.add(lPreviewHandle1);
			lPreviewHandleList.add(lPreviewHandle2);
			lPreviewHandleList.add(lPreviewHandle3);
			lPreviewHandleList.add(lPreviewHandle4);

			long previewSucValue = lPreviewHandle1.longValue();
			long previewSucValue2 = lPreviewHandle2.longValue();
			long previewSucValue3=lPreviewHandle3.longValue();
			long previewSucValue4=lPreviewHandle4.longValue();

			// 预览失败时:
			if (previewSucValue == -1) {
				JOptionPane.showMessageDialog(this, "一号相机预览失败");
				return;
			} else if (previewSucValue2 == -1) {
				JOptionPane.showMessageDialog(this, "二号相机预览失败");
				return;
			} else if (previewSucValue3 == -1) {
				JOptionPane.showMessageDialog(this, "三号相机预览失败");
				return;
			} else if (previewSucValue4 == -1) {
				JOptionPane.showMessageDialog(this, "四号相机预览失败");
				return;
			}
			// 预览成功的操作
			jButtonRealPlay.setText("停止");
			bRealPlay = true;

		} else {
			for (int i = 0; i < lPreviewHandleList.size(); i++) {
				hCNetSDK.NET_DVR_StopRealPlay(lPreviewHandleList.get(i));
			}

			jButtonRealPlay.setText("预览");
			bRealPlay = false;

			panelRealPlayU_1.repaint();
			panelRealPlayU_2.repaint();
		}
	}

	// ==========================校准按钮=============================//

	private void jButtonCheckActionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
		System.out.println("etstttst");
		if (lUserIDList.size() == 0) {
			JOptionPane.showMessageDialog(this, "请先注册");
			return;
		}
		JDialogAdjustLight adjust = new JDialogAdjustLight(this, false, lUserIDList.get(0), m_strDeviceInfo.get(0));
		adjust.setBounds(0, 0, 500, 300);
		centerWindow(adjust);
		adjust.setVisible(true);
	}

	// =============================开始按钮监听============================//
	private void jButtonStartActionPerformed(ActionEvent e) {
		if (lUserIDList.size() == 0) {
			JOptionPane.showMessageDialog(this, "请先注册");
			return;
		}
		if (!isChecking) {
			timer = new Timer();
			task = new TimerTask() {
				@Override
				public void run() {
					String[] flag1 = new String[4];
					for (int i = 0; i < cameraInfoList.size(); i++) {
						List<Mat> src1 = cut.getDVRPic(cameraInfoList.get(i));

						try {
							flag1[i] = netCheck.getResult(src1);
						} catch (IOException e) {
							e.printStackTrace();
						}

					}

					DefaultTableModel alarmTableModel = ((DefaultTableModel) jTableAlarm.getModel());// 获取表格模型
					String sAlarmType = new String("");
					String[] newRow = new String[3];
					// 报警时间
					Date today = new Date();
					DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
					newRow[0] = dateFormat.format(today);
					// 报警设备IP地址
					newRow[2] = m_sDeviceIPList.get(0);

					if (flag1[0].equals("right") && flag1[1].equals("right") && flag1[2].equals("right")
							&& flag1[3].equals("right")) {
						sAlarmType = "Right";
						newRow[1] = sAlarmType;
						alarmTableModel.insertRow(0, newRow);
					} else {
						sAlarmType = "Wrong";
						newRow[1] = sAlarmType;
						alarmTableModel.insertRow(0, newRow);
					}
				}
			};
			timer.schedule(task, 0, 3000);
			jButtonStart.setText("暂止");
			isChecking = true;
		} else {
			jButtonStart.setText("开始");
			timer.cancel();
			timer = null;
			task.cancel();
			task = null;
			isChecking = false;
		}

	}

	private DefaultTreeModel initialTreeModel() {
		m_DeviceRoot = new DefaultMutableTreeNode("Device");
		DefaultTreeModel myDefaultTreeModel = new DefaultTreeModel(m_DeviceRoot);// 使用根节点创建模型
		return myDefaultTreeModel;
	}

	public DefaultTableModel initialTableModel() {
		String tabeTile[];
		tabeTile = new String[] { "时间", "报警信息", "设备信息" };
		DefaultTableModel alarmTableModel = new DefaultTableModel(tabeTile, 0);
		return alarmTableModel;
	}

	private void CreateDeviceTree() {
		IntByReference ibrBytesReturned = new IntByReference(0);// 获取IP接入配置参数
		boolean bRet = false;

		m_strIpparaCfg.add(new HCNetSDK.NET_DVR_IPPARACFG());
		m_strIpparaCfg.get(m_strIpparaCfg.size() - 1).write();
		Pointer lpIpParaConfig = m_strIpparaCfg.get(m_strIpparaCfg.size() - 1).getPointer();
		bRet = hCNetSDK.NET_DVR_GetDVRConfig(lUserIDList.get(lUserIDList.size() - 1), HCNetSDK.NET_DVR_GET_IPPARACFG,
				new NativeLong(0), lpIpParaConfig, m_strIpparaCfg.get(m_strIpparaCfg.size() - 1).size(),
				ibrBytesReturned);
		m_strIpparaCfg.get(m_strIpparaCfg.size() - 1).read();

		DefaultTreeModel TreeModel = ((DefaultTreeModel) jTreeDevice.getModel());// 获取树模型
		if (!bRet) {
			// 设备不支持,则表示没有IP通道
			for (int iChannum = 0; iChannum < m_strDeviceInfo.get(m_strDeviceInfo.size() - 1).byChanNum; iChannum++) {
				String string = m_sDeviceIPList.get(m_sDeviceIPList.size() - 1);
				int len = string.split("\\.").length;
				System.out.println(string);
				System.out.println(len);
				string = string.split("\\.")[len - 1];
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("Camera" + (iChannum + string));
				TreeModel.insertNodeInto(newNode, m_DeviceRoot, iChannum);
			}
		} else {
			// 设备支持IP通道
			for (int iChannum = 0; iChannum < m_strDeviceInfo.get(m_strDeviceInfo.size() - 1).byChanNum; iChannum++) {
				if (m_strIpparaCfg.get(m_strIpparaCfg.size() - 1).byAnalogChanEnable[iChannum] == 1) {
					DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(
							"Camera" + (iChannum + m_strDeviceInfo.get(m_strDeviceInfo.size() - 1).byStartChan));
					TreeModel.insertNodeInto(newNode, m_DeviceRoot, m_iTreeNodeNum);
					m_iTreeNodeNum++;
				}
			}
			for (int iChannum = 0; iChannum < HCNetSDK.MAX_IP_CHANNEL; iChannum++)
				if (m_strIpparaCfg.get(m_strIpparaCfg.size() - 1).struIPChanInfo[iChannum].byEnable == 1) {
					// System.out.println(" m_strDeviceInfo.byStartChan"+
					// m_strDeviceInfo.byStartChan);
					DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(
							"IPCamera" + (iChannum + m_strDeviceInfo.get(m_strDeviceInfo.size() - 1).byStartChan));
					TreeModel.insertNodeInto(newNode, m_DeviceRoot, m_iTreeNodeNum);
				}
		}
		TreeModel.reload();// 将添加的节点显示到界面
		jTreeDevice.setSelectionInterval(1, 1);// 选中第一个节点
	}

	public static void centerWindow(Container window) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int w = window.getSize().width;
		int h = window.getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;
		window.setLocation(x, y);
	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		ClientFrame clientFrame = new ClientFrame();
	}
}