package com.lym.UI;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lym.UI.StarComponents.StarOptionFrame;
import com.lym.Util.StarToolkit;
import com.lym.obj.ResortServiceSettings;
import com.lym.resort.Resort;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ResortSetUI extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private JFrame father;
	private Point OldLocation = new Point(650,300);//�Զ����㷨ʵ�ִ����ƶ�
	private void setFramePosition(MouseEvent m){
		Point p = this.getLocation();
		this.setLocation(
		        p.x + (m.getX() - OldLocation.x), 
		        p.y + (m.getY() - OldLocation.y)); 
	}
	
	//��ʼ����������Ҫ�Ĳ���
	JLabel MyipLabel;//ip
	JLabel MyPortLabel;//port
	ResortServiceSettings mySetting;
	
	public ResortSetUI(JFrame fatherFrame) {
		father = fatherFrame;
		this.setTitle("�ǿ�������");
		this.setSize(450, 400);
		this.getContentPane().setLayout(null);
		this.setResizable(false);
		this.setUndecorated(true);//ȥ���߿�
		this.setBackground(new Color(0,0,0,0));
		this.setIconImage((new ImageIcon("imge/StarICON.png")).getImage());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent m) {
				OldLocation.x = m.getX();
				OldLocation.y = m.getY();
			}
			public void mouseReleased(MouseEvent m) {
				setFramePosition(m);
			}
			
		});
		StarToolkit.setJFrameOnCenter(this);
		
		mySetting = new ResortServiceSettings();
		//���������ļ���ʽ����
		
		JPanel BackGroudPanel = new JPanel();//��͸�����
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
				ResortSetUI.this.setVisible(false);
				father.setVisible(true);
			}
		});
		BackButton.setBounds(260, 180, 120, 40);
		BackGroudPanel.add(BackButton);		
		
		//������ť
		JButton StartControlButton = new JButton("����");
		//StartControlButton.setForeground(Color.white);
		StartControlButton.setFont(new Font("����", Font.BOLD, 40));
		StartControlButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				Thread ResortThread = new Thread(new Runnable() {
					public void run() {
						ResortSetUI.this.setVisible(false);
						int port = getPort();
						if(port!=-1) {
							new Resort(mySetting).Start();
						}
						else
							new StarOptionFrame("����˿ں�",StarOptionFrame.ErrorMessage).setVisible(true);	
						ResortSetUI.this.setVisible(true);
						
					}
				});
				ResortThread.setName("ResortThread");
				ResortThread.setPriority(Thread.MAX_PRIORITY);//���������ȼ����
				ResortThread.start();
				
			}
		});
		StartControlButton.setBounds(80, 180, 150, 90);
		BackGroudPanel.add(StartControlButton);

		
		JLabel ipLabel = new JLabel("�ҵ�IP: ");
		ipLabel.setFont(new Font("����", Font.BOLD, 20));
		ipLabel.setForeground(Color.white);
		ipLabel.setBounds(40, 50, 100, 35);
		BackGroudPanel.add(ipLabel);
		
		MyipLabel = new JLabel(StarToolkit.getMyIp());
		MyipLabel.setFont(new Font("����", Font.BOLD, 20));
		MyipLabel.setForeground(Color.white);
		MyipLabel.setBounds(120, 50, 180, 35);//������Ҫע��һ��ip��ַ�ĳ��Ȼ�Ӱ�쵽label���ȴӶ�Ӱ�����ݵ���ʾ������xxx.xxx.xxx.xxx����������ʾ����
		BackGroudPanel.add(MyipLabel);
		
		JLabel PortLabel = new JLabel("�˿�:");
		PortLabel.setFont(new Font("����", Font.BOLD, 20));
		PortLabel.setForeground(Color.white);
		PortLabel.setBounds(295, 50, 70, 35);
		BackGroudPanel.add(PortLabel);
		
		MyPortLabel = new JLabel(mySetting.getPORT() + "");
		MyPortLabel.setFont(new Font("����", Font.BOLD, 20));
		MyPortLabel.setForeground(Color.white);
		MyPortLabel.setBounds(350, 50, 60, 35);
		BackGroudPanel.add(MyPortLabel);
		
		JButton AdvanceSettButtpm = new JButton("�߼�����");
		AdvanceSettButtpm.setFont(new Font("����", Font.PLAIN, 20));
		AdvanceSettButtpm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ResortAdvanceSetUI(ResortSetUI.this,mySetting);
				MyPortLabel.setText(mySetting.getPORT() + "");
			}
		});
		AdvanceSettButtpm.setBounds(80, 120, 130, 35);
		BackGroudPanel.add(AdvanceSettButtpm);
		
		
		
		JButton TestConnectButtpm = new JButton("�����ǿ���");
		TestConnectButtpm.setFont(new Font("����", Font.PLAIN, 20));
		TestConnectButtpm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ip = StarToolkit.getMyIp();
				int port = mySetting.getPORT();
				if(StarToolkit.createStarCommandToClipboard(ip,port))
					new StarOptionFrame("���� �ǿ��������� �ɹ����Ͻ������С����",StarOptionFrame.OKMessage).setVisible(true);

				
			}
		});
		TestConnectButtpm.setBounds(230, 120, 150, 35);
		BackGroudPanel.add(TestConnectButtpm);
		
		this.getContentPane().add(BackGroudPanel);
		this.setVisible(true);
	}	
	
	public void RefreshUI(ResortServiceSettings Setting) {//ˢ��ip��port
		mySetting = Setting;
		MyipLabel.setText(StarToolkit.getMyIp());
		MyPortLabel.setText(mySetting.getPORT()+"");
		super.repaint();
	}
	
	public void setPort(String port) {
		MyPortLabel.setText(port);
	}
	public int getPort() {
		try {
			return Integer.parseInt(MyPortLabel.getText());
		} catch (Exception e) {
		}
		return -1;
	}
	
}
