package com.lym.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.lym.UI.StarComponents.StarOptionFrame;
import com.lym.Util.StarToolkit;
import com.lym.control.Control;

import java.awt.Font;
import java.awt.Point;

public class ControlSetUI {

	private JFrame frame;
	private JFrame father;
	private JPanel BackGroudPanel;

	private JTextField ipText1;
	private JTextField ipText2;
	private JTextField ipText3;
	private JTextField ipText4;
	private JTextField portText;
	int initWidth = 450;
	int initHeight = 320;
	private Point OldLocation = new Point(650,300);
	private void setFramePosition(MouseEvent m){
		Point p = frame.getLocation();

		frame.setLocation(
		        p.x + (m.getX() - OldLocation.x), 
		        p.y + (m.getY() - OldLocation.y)); 

	}
	ControlSetUI(JFrame fatherFrame) {
		father = fatherFrame;
		frame = new JFrame("星控雷达向导");
		//frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		frame.setUndecorated(true);//去掉边框
		frame.setBackground(new Color(0,0,0,0));
		frame.setIconImage((new ImageIcon("imge/StarICON.png")).getImage());
		
		OldLocation = StarToolkit.getCenterPoint(initWidth, initHeight);
		frame.setBounds(OldLocation.x, OldLocation.y, initWidth, initHeight);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.setVisible(false);
				//father.setVisible(true);
			}
		});
		frame.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent m) {
				OldLocation.x = m.getX();
				OldLocation.y = m.getY();
			}
			public void mouseReleased(MouseEvent m) {
				setFramePosition(m);
			}
			
		});
		
		
		BackGroudPanel = new JPanel();//半透明面板
		BackGroudPanel.setBackground(new Color(0,0,0,200));
		BackGroudPanel.setBounds(0, 0, 450, 320);
		BackGroudPanel.setLayout(null);
			
		JButton ExitButton = new JButton("\u9000\u51FA");//退出程序
		ExitButton.setFont(new Font("黑体", Font.BOLD, 25));
		ExitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		ExitButton.setBounds(260, 230, 120, 40);
		BackGroudPanel.add(ExitButton);
		
		JButton BackButton = new JButton("\u8FD4\u56DE");//返回按钮
		BackButton.setFont(new Font("黑体", Font.BOLD, 25));
		BackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				father.setVisible(true);
			}
		});
		BackButton.setBounds(260, 180, 120, 40);
		BackGroudPanel.add(BackButton);		
		
		
		JButton StartControlButton = new JButton("\u8FDE\u63A5");
		//StartControlButton.setForeground(Color.white);
		StartControlButton.setFont(new Font("黑体", Font.BOLD, 40));
		StartControlButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				//控制对方
				String ip = getIp();
				Thread ControlThread = new Thread(new Runnable() {
					public void run() {
						frame.setVisible(false);
						new Control(ip,Integer.parseInt(portText.getText()));
						frame.setVisible(true);
						
					}
				});
				ControlThread.setName("ControlThread");
				ControlThread.setPriority(Thread.MAX_PRIORITY);//设置其优先级最高
				ControlThread.start();
			}
		});
		StartControlButton.setBounds(80, 180, 150, 90);
		BackGroudPanel.add(StartControlButton);

		
		JLabel ipLabel = new JLabel("目标IP");
		ipLabel.setFont(new Font("黑体", Font.BOLD, 20));
		ipLabel.setForeground(Color.white);
		ipLabel.setBounds(40, 50, 80, 35);
		BackGroudPanel.add(ipLabel);
		
		ipText1 = new JTextField("192");
		ipText1.setBounds(110, 50, 50, 35);
		BackGroudPanel.add(ipText1);
		
		
		ipText2 = new JTextField("168");
		ipText2.setBounds(185, 50, 50, 35);
		BackGroudPanel.add(ipText2);
		
		ipText3 = new JTextField("8");
		ipText3.setBounds(260, 50, 50, 35);
		BackGroudPanel.add(ipText3);
		
		ipText4 = new JTextField("40");
		ipText4.setBounds(335, 50, 50, 35);
		BackGroudPanel.add(ipText4);
		
		SetNumberOnly(3,ipText1,ipText2,ipText3,ipText4);
		
		JLabel portLabel = new JLabel("端口号");
		portLabel.setFont(new Font("黑体", Font.BOLD, 20));
		portLabel.setForeground(Color.white);
		portLabel.setBounds(40, 120, 80, 35);
		BackGroudPanel.add(portLabel);
		
		portText = new JTextField("9999");
		portText.setBounds(130, 120, 80, 35);
		SetNumberOnly(5,portText);
		BackGroudPanel.add(portText);
		
		JButton TestConnectButtpm = new JButton("测试连接");
		TestConnectButtpm.setFont(new Font("黑体", Font.PLAIN, 20));
		TestConnectButtpm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					try {
						String ip = getIp();
						int port = Integer.parseInt(portText.getText());
						new Socket(ip,port).close();
						new StarOptionFrame(" 测试通过√",StarOptionFrame.OKMessage).setVisible(true);
						//JOptionPane.showConfirmDialog(frame, "对方已启动服务！", "测试结果", JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
					} catch (Exception excep) {
						new StarOptionFrame("连接不成功",StarOptionFrame.ErrorMessage).setVisible(true);
						//JOptionPane.showConfirmDialog(frame, "连接不可用，请检查ip、port、网络、防火墙或通知对方开启服务！", "测试结果", JOptionPane.OK_CANCEL_OPTION,JOptionPane.ERROR_MESSAGE);
					}
				
				
			}
		});
		TestConnectButtpm.setBounds(250, 120, 120, 35);
		BackGroudPanel.add(TestConnectButtpm);
		
		frame.add(BackGroudPanel,BorderLayout.CENTER);
		//frame.getContentPane().add(BackGroudPanel);
		frame.setVisible(true);
		
		tryUseStarCommandConnect();
	}
	private void tryUseStarCommandConnect() {
		if(!StarToolkit.analysisStar()) //解析星控令不成功则直接返回
			return;
		
		Thread ControlThread = new Thread(new Runnable() {
			public void run() {
				frame.setVisible(false);
				
				//模仿如果网络不好需要等待
				try {//由于是尝试星控令登录，若登录失败或者异常产生不进行任何操作，
					
					new Control(StarToolkit.getIP(),StarToolkit.getPORT());
				} catch (Exception e1) {
					//new StarOptionFrame("星口令错误",StarOptionFrame.WarnMessage).setVisable(true);
					JOptionPane.showConfirmDialog(frame, "连接到"+StarToolkit.IpAndPort +"失败，请检查星控令或手动尝试连接","星口令连接无效", JOptionPane.OK_CANCEL_OPTION,JOptionPane.ERROR_MESSAGE);
				}finally {
					frame.setVisible(true);
					
					
				}
			}
		});
		ControlThread.setName("ControlThread");
		ControlThread.setPriority(Thread.MAX_PRIORITY);//设置其优先级最高
		ControlThread.start();
	}
	
	private String getIp() {
		return ipText1.getText() + "." + ipText2.getText() + "." + ipText3.getText() + "." + ipText4.getText();
	}
	//对4个ip文字框统一设置
	private void SetNumberOnly(int maxLength,JTextField...jTextField) {
		for (JTextField tf : jTextField) {
			StarToolkit.addNumLimitToTextJField(tf, maxLength);
			tf.setFont(new Font("黑体", Font.BOLD, 20));
			tf.setHorizontalAlignment(JTextField.CENTER);
		}
	}
}
