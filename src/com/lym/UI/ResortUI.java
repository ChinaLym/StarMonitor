package com.lym.UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;

/**
 * 新设计的统一微透明风格，但还未实装
 * @author Administrator
 *
 */
public class ResortUI {
	
	public static void main(String[] args) {
		new ResortUI(9999);
	}
	
	private static Point OldLocation = new Point(650,300);
	private static void setFramePosition(MouseEvent m){
		Point p = frame.getLocation();
		frame.setLocation(
		        p.x + (m.getX() - OldLocation.x), 
		        p.y + (m.getY() - OldLocation.y)); 
	}
	//定义快捷键标识，用于在设置多个热键时，在事件处理中区分用户按下的热键
	public static final int FUNC2_KEY_MARK = 2;
	public static final int FUNC_KEY_MARK = 1;
	public static final int EXIT_KEY_MARK = 0;
	
	//交互窗口为静态变量，只有一个Frame窗口
	static JFrame frame ;
	static JTextArea MessageArea;
	static JRadioButton AllowControlButton = null;
	static JLabel ConnectNum = null;
	//是否允许控制，该变量为静态变量，只能  只能允许本机用户控制或者允许所有已经连本机的用户控制
	volatile static boolean AllowControl = false;
	
	//加载信息框Frame的内容
	static {
		frame = new JFrame("消息框");
		frame.setBounds(100, 100, 290, 340);
		frame.setLayout(null);
		frame.setUndecorated(true);//去掉边框
		frame.setBackground(new Color(180,180,250,220));
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setAlwaysOnTop(true);
		frame.setResizable(false);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int res = JOptionPane.showConfirmDialog(frame, "确定退出？","确认退出",JOptionPane.OK_CANCEL_OPTION);
				
				if (res == JOptionPane.OK_OPTION) {
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				    } 
				else  {
				    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
				    }
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
		
		MessageArea = new JTextArea();
		MessageArea.setEditable(false);
		MessageArea.setLineWrap(true);
		MessageArea.setFont(new Font("宋体", Font.PLAIN, 20));
		JScrollPane jsp = new JScrollPane(MessageArea);
		jsp.setBounds(5, 5, 280, 200);
		((DefaultCaret) MessageArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		frame.add(jsp);
		//设置是否可以其他人控制本机
		AllowControlButton = new JRadioButton("是否允许控制");
		AllowControlButton.setFont(new Font("黑体", Font.BOLD, 20));
		AllowControlButton.setBounds(20, 200, 160, 30);
		AllowControlButton.setBackground(new Color(0,0,0,0));
		AllowControlButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(AllowControlButton.isSelected()) {
					AllowControl = true;
					addMessage("=系统消息√：已允许控制");
					frame.requestFocus();
				}else{
					AllowControl = false;
					addMessage("=系统消息×：已拒绝控制");
					frame.requestFocus();
				}
			}
		});
		frame.add(AllowControlButton);
		
		JRadioButton MoreInformationButton = new JRadioButton("更多");
		MoreInformationButton.setBackground(new Color(0,0,0,0));
		MoreInformationButton.setFont(new Font("黑体", Font.BOLD, 20));
		MoreInformationButton.setSelected(true);
		MoreInformationButton.setBounds(200, 200, 200, 30);
		MoreInformationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(MoreInformationButton.isSelected()) {
					frame.setSize(frame.getWidth(), 330);
				}else
					frame.setSize(frame.getWidth(), 230);
				
			}
		});
		frame.add(MoreInformationButton);
		//提示
		JLabel tip = new JLabel("alt+C：切换允许控制");
		tip.setFont(new Font("黑体", Font.BOLD, 18));
		tip.setBounds(20, 230, 200, 30);
		frame.add(tip);
		JLabel tip2 = new JLabel("alt+G：呼出界面");
		tip2.setFont(new Font("黑体", Font.BOLD, 18));
		tip2.setBounds(20, 260, 200, 30);
		frame.add(tip2);
		JLabel tip3 = new JLabel("alt+Q：退出软件");
		tip3.setFont(new Font("黑体", Font.BOLD, 18));
		tip3.setBounds(20, 290, 200, 30);
		frame.add(tip3);
		//显示连接个数
		JPanel infoPanel = new JPanel();
		//infoPanel.setBorder(BorderFactory.createTitledBorder("连接数"));
		infoPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "连接数", TitledBorder.LEADING, TitledBorder.TOP, new Font("黑体", Font.PLAIN, 15)));
		infoPanel.setBackground(new Color(0,0,0,0));
		infoPanel.setBounds(175, 270, 100, 60);
		frame.add(infoPanel);
		ConnectNum = new JLabel("0");
		ConnectNum.setFont(new Font("黑体", Font.PLAIN, 30));
		ConnectNum.setBounds(0, 0, 100, 50);
		ConnectNum.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				ConnectNum.setText((Thread.activeCount()-3)/2+"");
			}
		});
		ConnectNum.setToolTipText("点击可刷新");
		infoPanel.add(ConnectNum);
		addMessage("面板UI已经设计完毕，还未实装至StarMonitor");
		registerShortcutKey();
		//设置主窗口可见
		frame.setVisible(true);
		
	}
	/**
	 * 初始化函数
	 * @param PermissionLevel
	 */
	public ResortUI(int PermissionLevel) {
		//初始化本地的变量
		AllowControl = PermissionLevel==0?false:true;
		AllowControlButton.setSelected(AllowControl);
	}
	/**
	 * 将本界面置于最前端
	 */
	public static void setFrameFocus() {
		frame.setBounds(100, 100, frame.getWidth(), frame.getHeight());
    	frame.setExtendedState(JFrame.NORMAL);
    	frame.toFront();
    	frame.requestFocus();
	}
	/**
	 * 刷新计数（由于多线程因素，计数很可能出现错误）
	 * 可以把计数方式改成获取    存储于客户端连接的数组的长度
	 */
	public static void reFreshConnectNum() {
		ConnectNum.setText((Thread.activeCount()-3)/2+"");
	}
	//向信息框MessageArea中添加消息
	public static synchronized void addMessage(String s) {
		MessageArea.append(s + "\n");
	}
	
	/**
	 * 注册快捷键：先添加注册表，后添加相对应的监听器以及执行函数
	 * 
	 */
	static void registerShortcutKey(){
		try {
			JIntellitype.getInstance().registerHotKey(FUNC2_KEY_MARK, JIntellitype.MOD_ALT, (int)'G');  
			JIntellitype.getInstance().registerHotKey(FUNC_KEY_MARK, JIntellitype.MOD_ALT, (int)'C');  
	    	JIntellitype.getInstance().registerHotKey(EXIT_KEY_MARK, JIntellitype.MOD_ALT, (int)'Q');  
	        //为已注册的快捷键添加监听
	    	JIntellitype.getInstance().addHotKeyListener(new HotkeyListener() {
				public void onHotKey(int markCode) {
					switch (markCode) {  
			        case FUNC_KEY_MARK:  //切换允许控制
			        	if(AllowControlButton.isSelected()) {
			        		AllowControl = false;
							addMessage("=系统消息×：已拒绝控制");
			        		AllowControlButton.setSelected(false);
			        		frame.requestFocus();
			        	}else {
			        		AllowControl = true;
							addMessage("=系统消息√：已允许控制");
			        		AllowControlButton.setSelected(true);
			        		frame.requestFocus();
			        	}
			            break;  
			        case FUNC2_KEY_MARK:  
			        	setFrameFocus();
			            break; 
			        case EXIT_KEY_MARK:  //退出软件
			        	System.exit(0);
			            break;   
					} 				
				}
			});
		}catch(Exception e) {
			addMessage("×系统消息×：快捷键监听器创建失败");
		}
	}


}
