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
 * java�ĵ�ע��
 *   �����˵��
 * @author guigu
 * 
 * 
 * �������裺
 *   1.��д������
 *   2.��д�ͻ���
 *   3.������������Ļ��Ϣ���ͻ���
 *   4.�ͻ��˽��շ���������Ϣ������ʾ����
 *
 */
public class LocationScreen {
	
	public static void main(String[] args) throws Exception {
		
		JFrame frame = new JFrame();
		frame.setSize(600, 600);
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);//��ʾ�����д��ڵ�����
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		//�����Ļ��С
		Dimension demension =   toolkit.getScreenSize();
		
		//������ʾ��Ļ������
		JLabel lable = new JLabel();
		frame.add(lable);
		
		//����һ��������
		Robot robot = new Robot();
		while(true){
			//�½�һ����������
			Rectangle rec = new Rectangle(frame.getWidth(), 0,
					demension.width - frame.getWidth(),frame.getHeight()); 
			//��ȡ��Ļ
			BufferedImage bufImg = robot.createScreenCapture(rec);
			lable.setIcon(new ImageIcon(bufImg));
		}
		
	}

}
