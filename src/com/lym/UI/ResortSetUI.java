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
	private Point OldLocation = new Point(650,300);//自定义算法实现窗口移动
	private void setFramePosition(MouseEvent m){
		Point p = this.getLocation();
		this.setLocation(
		        p.x + (m.getX() - OldLocation.x), 
		        p.y + (m.getY() - OldLocation.y)); 
	}
	
	//初始化服务器需要的参数
	JLabel MyipLabel;//ip
	JLabel MyPortLabel;//port
	ResortServiceSettings mySetting;
	
	public ResortSetUI(JFrame fatherFrame) {
		father = fatherFrame;
		this.setTitle("星控求助向导");
		this.setSize(450, 400);
		this.getContentPane().setLayout(null);
		this.setResizable(false);
		this.setUndecorated(true);//去掉边框
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
		//尝试配置文件形式加载
		
		JPanel BackGroudPanel = new JPanel();//半透明面板
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
				ResortSetUI.this.setVisible(false);
				father.setVisible(true);
			}
		});
		BackButton.setBounds(260, 180, 120, 40);
		BackGroudPanel.add(BackButton);		
		
		//求助按钮
		JButton StartControlButton = new JButton("求助");
		//StartControlButton.setForeground(Color.white);
		StartControlButton.setFont(new Font("黑体", Font.BOLD, 40));
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
							new StarOptionFrame("请检查端口号",StarOptionFrame.ErrorMessage).setVisible(true);	
						ResortSetUI.this.setVisible(true);
						
					}
				});
				ResortThread.setName("ResortThread");
				ResortThread.setPriority(Thread.MAX_PRIORITY);//设置其优先级最高
				ResortThread.start();
				
			}
		});
		StartControlButton.setBounds(80, 180, 150, 90);
		BackGroudPanel.add(StartControlButton);

		
		JLabel ipLabel = new JLabel("我的IP: ");
		ipLabel.setFont(new Font("黑体", Font.BOLD, 20));
		ipLabel.setForeground(Color.white);
		ipLabel.setBounds(40, 50, 100, 35);
		BackGroudPanel.add(ipLabel);
		
		MyipLabel = new JLabel(StarToolkit.getMyIp());
		MyipLabel.setFont(new Font("黑体", Font.BOLD, 20));
		MyipLabel.setForeground(Color.white);
		MyipLabel.setBounds(120, 50, 180, 35);//这里需要注意一下ip地址的长度会影响到label长度从而影响内容的显示，测试xxx.xxx.xxx.xxx可以正常显示即可
		BackGroudPanel.add(MyipLabel);
		
		JLabel PortLabel = new JLabel("端口:");
		PortLabel.setFont(new Font("黑体", Font.BOLD, 20));
		PortLabel.setForeground(Color.white);
		PortLabel.setBounds(295, 50, 70, 35);
		BackGroudPanel.add(PortLabel);
		
		MyPortLabel = new JLabel(mySetting.getPORT() + "");
		MyPortLabel.setFont(new Font("黑体", Font.BOLD, 20));
		MyPortLabel.setForeground(Color.white);
		MyPortLabel.setBounds(350, 50, 60, 35);
		BackGroudPanel.add(MyPortLabel);
		
		JButton AdvanceSettButtpm = new JButton("高级设置");
		AdvanceSettButtpm.setFont(new Font("黑体", Font.PLAIN, 20));
		AdvanceSettButtpm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ResortAdvanceSetUI(ResortSetUI.this,mySetting);
				MyPortLabel.setText(mySetting.getPORT() + "");
			}
		});
		AdvanceSettButtpm.setBounds(80, 120, 130, 35);
		BackGroudPanel.add(AdvanceSettButtpm);
		
		
		
		JButton TestConnectButtpm = new JButton("复制星控令");
		TestConnectButtpm.setFont(new Font("黑体", Font.PLAIN, 20));
		TestConnectButtpm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ip = StarToolkit.getMyIp();
				int port = mySetting.getPORT();
				if(StarToolkit.createStarCommandToClipboard(ip,port))
					new StarOptionFrame("复制 星控求助口令 成功，赶紧分享给小伙伴吧",StarOptionFrame.OKMessage).setVisible(true);

				
			}
		});
		TestConnectButtpm.setBounds(230, 120, 150, 35);
		BackGroudPanel.add(TestConnectButtpm);
		
		this.getContentPane().add(BackGroudPanel);
		this.setVisible(true);
	}	
	
	public void RefreshUI(ResortServiceSettings Setting) {//刷新ip和port
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
