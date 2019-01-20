package com.lym.UI;

import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.lym.Util.StarToolkit;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
public class StartUi {

	private JFrame frame;

	int initWidth = 800;
	int initHeight = 600;
	
	private Point OldLocation = new Point(650,300);
	private void setFramePosition(MouseEvent m){
		Point p = frame.getLocation();
		frame.setLocation(
		        p.x + (m.getX() - OldLocation.x), 
		        p.y + (m.getY() - OldLocation.y)); 
	}
	static JLabel TipText ;
	static JLabel resortLabel ;
	static JLabel ControlLabel;
	static JLabel GifLabel;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartUi window = new StartUi();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public StartUi() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("星控远程协助");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		frame.setUndecorated(true);//去掉边框
		frame.setBackground(new Color(0,0,0,0));
		frame.setIconImage((new ImageIcon("imge/StarICON.png")).getImage());
		//将窗口位置设置在屏幕中间
		OldLocation = StarToolkit.getCenterPoint(initWidth, initHeight);
		frame.setBounds(OldLocation.x, OldLocation.y, initWidth, initHeight);
		
		ImageIcon DefaultImage = new ImageIcon("imge/card.png");
		ImageIcon FocusOnResortImage = new ImageIcon("imge/ResortICO.png");
		ImageIcon FocusOnControlImage = new ImageIcon("imge/ControlICO.png");
		
		TipText = new JLabel();
		TipText.setBounds(30, 0, 560, 80);
		TipText.setIcon(new ImageIcon("imge/Title.png"));
		TipText.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent m) {
							
				OldLocation.x = m.getX();
				OldLocation.y = m.getY();
			}
			public void mouseReleased(MouseEvent m) {
				setFramePosition(m);
			}
			
		});
		frame.getContentPane().add(TipText);
		
		
		GifLabel = new JLabel();
		GifLabel.setIcon(new ImageIcon("imge/BigLighting.gif"));
		GifLabel.setBounds(0, 0, 800, 600);
		GifLabel.setVisible(false);
		frame.add(GifLabel);
		
		resortLabel = new JLabel();
		resortLabel.setIcon(DefaultImage);
		resortLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				frame.setVisible(false);
				new ResortSetUI(frame);
			}
		
		    public void mouseEntered(MouseEvent e) {
		    	resortLabel.setIcon(FocusOnResortImage);
		    	
		    }

		    public void mouseExited(MouseEvent e) {
		    	resortLabel.setIcon(DefaultImage);
		    }
		});
		resortLabel.setToolTipText("向其他用户求助，点击了解详情");
		resortLabel.setBounds(0, 100, 300, 490);
		frame.getContentPane().add(resortLabel);
		
		
		ControlLabel = new JLabel();
		ControlLabel.setIcon(DefaultImage);
		ControlLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				new Thread(new Runnable() {
					public void run() {
						showGif(true);
						try {
							Thread.sleep(900);
						} catch (InterruptedException e1) {
						}
						
						frame.setVisible(false);
						new ControlSetUI(frame);
						showGif(false);
					}
				}).start();
				
			}
		
		    public void mouseEntered(MouseEvent e) {
		    	ControlLabel.setIcon(FocusOnControlImage);
		    }

		    public void mouseExited(MouseEvent e) {
		    	ControlLabel.setIcon(DefaultImage);
		    }
		});
		ControlLabel.setToolTipText("帮助其他用户，点击了解详情");
		ControlLabel.setBounds(330, 100, 300, 490);
		frame.getContentPane().add(ControlLabel);
	}
	 
	public static void showGif(boolean ifShow) {
		GifLabel.setVisible(ifShow);
		resortLabel.setVisible(!ifShow);
		TipText.setVisible(!ifShow);
		ControlLabel.setVisible(!ifShow);
	}
}
