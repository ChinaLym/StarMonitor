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
		frame = new JFrame("�ǿ��״���");
		//frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		frame.setUndecorated(true);//ȥ���߿�
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
		
		
		BackGroudPanel = new JPanel();//��͸�����
		BackGroudPanel.setBackground(new Color(0,0,0,200));
		BackGroudPanel.setBounds(0, 0, 450, 320);
		BackGroudPanel.setLayout(null);
			
		JButton ExitButton = new JButton("\u9000\u51FA");//�˳�����
		ExitButton.setFont(new Font("����", Font.BOLD, 25));
		ExitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		ExitButton.setBounds(260, 230, 120, 40);
		BackGroudPanel.add(ExitButton);
		
		JButton BackButton = new JButton("\u8FD4\u56DE");//���ذ�ť
		BackButton.setFont(new Font("����", Font.BOLD, 25));
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
		StartControlButton.setFont(new Font("����", Font.BOLD, 40));
		StartControlButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				//���ƶԷ�
				String ip = getIp();
				Thread ControlThread = new Thread(new Runnable() {
					public void run() {
						frame.setVisible(false);
						new Control(ip,Integer.parseInt(portText.getText()));
						frame.setVisible(true);
						
					}
				});
				ControlThread.setName("ControlThread");
				ControlThread.setPriority(Thread.MAX_PRIORITY);//���������ȼ����
				ControlThread.start();
			}
		});
		StartControlButton.setBounds(80, 180, 150, 90);
		BackGroudPanel.add(StartControlButton);

		
		JLabel ipLabel = new JLabel("Ŀ��IP");
		ipLabel.setFont(new Font("����", Font.BOLD, 20));
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
		
		JLabel portLabel = new JLabel("�˿ں�");
		portLabel.setFont(new Font("����", Font.BOLD, 20));
		portLabel.setForeground(Color.white);
		portLabel.setBounds(40, 120, 80, 35);
		BackGroudPanel.add(portLabel);
		
		portText = new JTextField("9999");
		portText.setBounds(130, 120, 80, 35);
		SetNumberOnly(5,portText);
		BackGroudPanel.add(portText);
		
		JButton TestConnectButtpm = new JButton("��������");
		TestConnectButtpm.setFont(new Font("����", Font.PLAIN, 20));
		TestConnectButtpm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					try {
						String ip = getIp();
						int port = Integer.parseInt(portText.getText());
						new Socket(ip,port).close();
						new StarOptionFrame(" ����ͨ����",StarOptionFrame.OKMessage).setVisible(true);
						//JOptionPane.showConfirmDialog(frame, "�Է�����������", "���Խ��", JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
					} catch (Exception excep) {
						new StarOptionFrame("���Ӳ��ɹ�",StarOptionFrame.ErrorMessage).setVisible(true);
						//JOptionPane.showConfirmDialog(frame, "���Ӳ����ã�����ip��port�����硢����ǽ��֪ͨ�Է���������", "���Խ��", JOptionPane.OK_CANCEL_OPTION,JOptionPane.ERROR_MESSAGE);
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
		if(!StarToolkit.analysisStar()) //�����ǿ���ɹ���ֱ�ӷ���
			return;
		
		Thread ControlThread = new Thread(new Runnable() {
			public void run() {
				frame.setVisible(false);
				
				//ģ��������粻����Ҫ�ȴ�
				try {//�����ǳ����ǿ����¼������¼ʧ�ܻ����쳣�����������κβ�����
					
					new Control(StarToolkit.getIP(),StarToolkit.getPORT());
				} catch (Exception e1) {
					//new StarOptionFrame("�ǿ������",StarOptionFrame.WarnMessage).setVisable(true);
					JOptionPane.showConfirmDialog(frame, "���ӵ�"+StarToolkit.IpAndPort +"ʧ�ܣ������ǿ�����ֶ���������","�ǿ���������Ч", JOptionPane.OK_CANCEL_OPTION,JOptionPane.ERROR_MESSAGE);
				}finally {
					frame.setVisible(true);
					
					
				}
			}
		});
		ControlThread.setName("ControlThread");
		ControlThread.setPriority(Thread.MAX_PRIORITY);//���������ȼ����
		ControlThread.start();
	}
	
	private String getIp() {
		return ipText1.getText() + "." + ipText2.getText() + "." + ipText3.getText() + "." + ipText4.getText();
	}
	//��4��ip���ֿ�ͳһ����
	private void SetNumberOnly(int maxLength,JTextField...jTextField) {
		for (JTextField tf : jTextField) {
			StarToolkit.addNumLimitToTextJField(tf, maxLength);
			tf.setFont(new Font("����", Font.BOLD, 20));
			tf.setHorizontalAlignment(JTextField.CENTER);
		}
	}
}
