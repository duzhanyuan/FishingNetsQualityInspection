package org.jaiken.client;

import java.awt.BorderLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
/**
 * 

 * @ClassName: JFrameShowCheckResult

 * @Description: TODO show the adjust result

 * @author: dell

 * @date: 2019年2月28日 下午7:34:12
 */
public class JFrameShowCheckResult extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel jLabel;
	ImageIcon icon;
	JTextField jTextField;
	int width;
	int height;
	static String path = "image/point_check_temp.png";
	
	public JFrameShowCheckResult(int size) {
		
		width = 750;
		height = 550;
		icon = new ImageIcon(path);
		jLabel = new JLabel();
		jTextField=new JTextField(size+" ");
		
		Image img = icon.getImage();
		img = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		icon.setImage(img);
		jLabel.setIcon(icon);
		jLabel.setSize(750, 550);
		add(jLabel,BorderLayout.NORTH);
		add(jTextField,BorderLayout.SOUTH);
		setBounds(500, 300, 800, 600);

		pack();
		setVisible(true);

	}


}
