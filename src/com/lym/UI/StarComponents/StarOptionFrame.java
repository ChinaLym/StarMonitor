package com.lym.UI.StarComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.lym.Util.StarToolkit;
/**
 * 该类是显示一个临时的提示信息，需要自己调用setVisable方法
 * @author Administrator
 *
 */
public class StarOptionFrame extends JFrame{
	//用于直接测试，可以删�?
	public static void main(String[] args) {
		new StarOptionFrame("测试",StarOptionFrame.ErrorMessage).setVisible(true);
	}
	
	private static final long serialVersionUID = 1L;

	static final int DefaultCloseTime = 4000;//默认关闭的窗口的时间
	public static final int NormalMessage = 1;
	public static final int WarnMessage = 2;
	public static final int ErrorMessage = 3;
	public static final int OKMessage = 4;
	
	static final int defaultWidth = 1200;
	static final int defaultHeight = 100;
	
	static final String HappyExpression = "( ^_^ ) ";
	static final String CryExpression = "o(╥﹏�?)o ";
	static final String NotHappyExpression = "(⊙_�?) ";
	int Color_R = 0;
	int Color_G = 0;
	int Color_B = 0;
	int PanelAlpha =  200;//初始的�?�明�?
	Color PanelColor = new Color(Color_R,Color_G,Color_B,PanelAlpha);//默认的颜�?
	
	int MessageType = NormalMessage;
	
	//设置为类变量，方便以后对该窗体的扩展
	JPanel DefaultBackGroudPanel;
	JLabel MessageLabel;
	
	public StarOptionFrame(String message,int MessageType) {
		this(message);
		switch(MessageType) {
			case NormalMessage:
				break;
			case WarnMessage:
				Color_R = 255;
				Color_G = 255;
				Color_B = 30;
				DefaultBackGroudPanel.setBackground(new Color(Color_R,Color_G,Color_B,PanelAlpha));
				MessageLabel.setText(NotHappyExpression + MessageLabel.getText());
				break;
			case ErrorMessage:
				Color_R = 255;
				DefaultBackGroudPanel.setBackground(new Color(Color_R,Color_G,Color_B,PanelAlpha));
				MessageLabel.setText(CryExpression + MessageLabel.getText());
				break;
			case OKMessage:
				Color_R = 50;
				Color_G = 200;
				Color_B = 50;
				DefaultBackGroudPanel.setBackground(new Color(Color_R,Color_G,Color_B,PanelAlpha));
				MessageLabel.setText(HappyExpression + MessageLabel.getText());
				break;
		}
	}
	//自定义长�?
	public StarOptionFrame(String message,boolean DesignMessageLength,int length) {
		this(message);
		
	}
	
	public StarOptionFrame(String message){
		
		this.setSize(defaultWidth, defaultHeight);
		StarToolkit.setJFrameOnCenter(this);//设置居中显示
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);//设置不可改变大小
		this.setUndecorated(true);//去掉边框
		this.setBackground(new Color(0,0,0,0));//设置frame为�?�明
		this.setIconImage((new ImageIcon("imge/StarICON.png")).getImage());//该软件统�?的图标，尽管不展�?
		this.setType(JFrame.Type.UTILITY);//不将该jframe在任务栏上展�?
		this.setAlwaysOnTop(true);//总是在最�?
		this.addMouseListener(new MouseAdapter() {//点击即可关闭
			public void mouseClicked(MouseEvent e) {
				close();
			}
		});
		

		
		DefaultBackGroudPanel = new JPanel(new BorderLayout());//半�?�明面板
		DefaultBackGroudPanel.setBackground(new Color(0,0,0,PanelAlpha));
		this.add(DefaultBackGroudPanel,BorderLayout.CENTER);//将面板放置于jframe的最中间，默认占满frame的大�?
		
		MessageLabel = new JLabel(message);//提示信息
		MessageLabel.setFont(new Font("黑体", Font.BOLD, 40));
		MessageLabel.setForeground(Color.white);
		MessageLabel.setHorizontalAlignment(JTextField.CENTER);//字体居中显示
		DefaultBackGroudPanel.add(MessageLabel,BorderLayout.CENTER);//将Label加到Panel中并居中显示
		
	}
	/**
	 * 默认的关闭方式：淡出
	 */
	private void close() {//自定义窗口关闭的方式(慢慢淡出)
		new Thread(new Runnable() {
			public void run() {
				try {
					int alp = PanelAlpha;
						for(;alp>0;alp -= 5) {
							Thread.sleep(15);
							DefaultBackGroudPanel.setBackground(new Color(Color_R,Color_G,Color_B,alp));
							DefaultBackGroudPanel.setVisible(false);
							DefaultBackGroudPanel.setVisible(true);
					}
				}catch(Exception e) {}
				StarOptionFrame.this.dispose();//关闭窗口
			}
		}).start();
	}
	

	@Override
	public void setVisible(boolean visable) {
		super.setVisible(visable);
		if(visable)ColseByTime();
	}
	public void setVisible(boolean visable,int showTime) {
		this.setVisible(visable);
		if(visable)ColseByTime(showTime);
		else this.dispose();//如果不显示则直接释放资源
	}
	

	private void ColseByTime() {
		ColseByTime(DefaultCloseTime);
	}
	private void ColseByTime(int closeTime) {
		new Thread(new Runnable() {//默认�?4秒钟后关�?
			public void run() {
				try {
					Thread.sleep(closeTime);
				} catch (InterruptedException e) {
				}
				close();
			}
		}).start();
	}
}
