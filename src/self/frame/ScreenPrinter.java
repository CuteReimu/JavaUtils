package self.frame;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import self.robot.MyRobot;

public class ScreenPrinter extends JFrame{
	private MyRobot robot;
	private int left;
	private int top;
	private final int right;
	private final int bottom;
	
	/**
	 * 截整个屏幕
	 * */
	public ScreenPrinter(){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.right = screenSize.width;
		this.bottom = screenSize.height;
		runPrinter();
	}
	
	/**
	 * 截屏
	 * @param left - 左上角横坐标
	 * @param top - 左上角纵坐标
	 * @param right - 右下角横坐标
	 * @param bottom - 右下角纵坐标
	 * */
	public ScreenPrinter(int left, int top, int right, int bottom){
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		runPrinter();
	}
	
	private void runPrinter(){
		JButton button;
		try {
			robot = new MyRobot();
		} catch (AWTException e) {
			throw new RuntimeException("初始化Robot失败");
		}
		setAlwaysOnTop(true);
		setTitle("截图");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100,100,137,87);
		
		button = new JButton("截图");
		button.addActionListener(e -> {
			ScreenPrinter.this.setVisible(false);
			try {
				Thread.sleep(0);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			BufferedImage image = ScreenPrinter.this.robot.createScreenCapture(new Rectangle(left, top, right - left, bottom - top));
			try {
				Thread.sleep(0);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			ScreenPrinter.this.setVisible(true);
			try {
				ImageIO.write(image, "bmp", new File("screenshot.bmp"));
				JOptionPane.showMessageDialog(ScreenPrinter.this, "截图成功！");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				JOptionPane.showMessageDialog(ScreenPrinter.this, "截图失败！");
			}
		});
		getContentPane().add(button, BorderLayout.CENTER);
	}

}
