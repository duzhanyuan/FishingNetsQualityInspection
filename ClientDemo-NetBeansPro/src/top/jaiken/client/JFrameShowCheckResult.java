package top.jaiken.client;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
/**
 * 

 * @ClassName: JFrameShowCheckResult

 * @Description: TODO show the adjust result

 * @author: dell

 * @date: 2019年2月28日 下午7:34:12
 */
public class JFrameShowCheckResult extends JFrame {

	JLabel jLabel;
	ImageIcon icon;
	int width;
	int height;
	static String path = "image/point_check_temp.png";

	public JFrameShowCheckResult() {
		
		width = 750;
		height = 550;
		icon = new ImageIcon(path);
		jLabel = new JLabel();

		Image img = icon.getImage();
		img = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		icon.setImage(img);
		jLabel.setIcon(icon);
		jLabel.setSize(750, 550);
		add(jLabel);
		setBounds(500, 300, 800, 600);

		pack();
		setVisible(true);

	}

}
