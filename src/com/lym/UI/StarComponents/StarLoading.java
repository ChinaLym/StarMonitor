package com.lym.UI.StarComponents;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.lym.Util.StarToolkit;
	
public class StarLoading {
	JFrame frame;
	int initWidth = 450;
	int initHeight = 320;
	ImageIcon loadingPicture;
	
	int timeout = 10000;//Ĭ��10���û�������ر����Զ��ر��ͷ���Դ
	
	public StarLoading() { 
		loadDefaultGif();
	}
	public StarLoading(ImageIcon picture){
		if(picture!=null) {
			loadingPicture = picture;
			this.initWidth = picture.getIconWidth();
			this.initHeight = picture.getIconHeight();
		}
		else loadDefaultGif();
	}
	public StarLoading(ImageIcon picture, int pictureWidth, int pictureHeight){
		if(picture!=null) {
			loadingPicture = picture;
			this.initWidth = pictureWidth;
			this.initHeight = pictureHeight;
		}
		else loadDefaultGif();
	}
	void loadDefaultGif() {
		//loadingPicture = new ImageIcon("imge/LoadingGIF3.gif");
		loadingPicture = new ImageIcon("imge/LoadingGIF2.gif");
		this.initWidth = loadingPicture.getIconWidth();
		this.initHeight = loadingPicture.getIconHeight();
	}

	void init() {
		frame = new JFrame("�ǿؼ�����");
		frame.setResizable(false);
		frame.setUndecorated(true);//ȥ���߿�
		frame.setBackground(new Color(0,0,0,0));
		StarToolkit.setJFrameOnCenter(frame,initWidth, initHeight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIconImage((new ImageIcon("imge/StarICON.png")).getImage());
		//frame.setType(JFrame.Type.UTILITY);//������jframe����������չʾ
		frame.setAlwaysOnTop(true);//��������ǰ
		
		JLabel LoadingLabel = new JLabel();//����ʱ���ŵĶ���
		LoadingLabel.setIcon(loadingPicture);
		LoadingLabel.setBounds(0, 0, initWidth, initHeight);
		frame.add(LoadingLabel,BorderLayout.CENTER);
		
		if(timeout>0) {
			TimeOutAndClose(timeout);
		}
	}
	public void show() {
		init();
		frame.setVisible(true);
	}
	public void stop() {
		frame.dispose();
	}
	/**
	 * ����<0������ֹͣ
	 * @param maxTime
	 */
	public void setTimeout(int maxTime) {
		timeout = maxTime;
	}
	void TimeOutAndClose(int time) {
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(time);
				} catch (Exception e) {
				}finally {
					frame.dispose();
				}
			}
		}).start();
	}
}
