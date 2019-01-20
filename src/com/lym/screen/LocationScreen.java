package com.lym.screen;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * java文档注释
 *   对类的说明
 * @author guigu
 * 
 * 
 * 开发步骤：
 *   1.编写服务器
 *   2.编写客户端
 *   3.服务器发送屏幕信息到客户端
 *   4.客户端接收服务器的消息，并显示出来
 *
 */
public class LocationScreen {
	
	public static void main(String[] args) throws Exception {
		
		JFrame frame = new JFrame();
		frame.setSize(600, 600);
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);//显示到所有窗口的上面
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		//获得屏幕大小
		Dimension demension =   toolkit.getScreenSize();
		
		//创建显示屏幕的容器
		JLabel lable = new JLabel();
		frame.add(lable);
		
		//创建一个机器人
		Robot robot = new Robot();
		while(true){
			//新建一个坐标区域
			Rectangle rec = new Rectangle(frame.getWidth(), 0,
					demension.width - frame.getWidth(),frame.getHeight()); 
			//截取屏幕
			BufferedImage bufImg = robot.createScreenCapture(rec);
			lable.setIcon(new ImageIcon(bufImg));
		}
		
	}

}
