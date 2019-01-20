package com.lym.control;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import com.lym.UI.StarComponents.StarLoading;
import com.lym.UI.StarComponents.StarOptionFrame;
import com.lym.obj.Message;

/**
 * 协助方：目前布局不是最好，可以进行修改，界面大小固定不能变目前
 * @author slt
 *
 */
public class Control {
	//交互的流
	DataInputStream din = null;
	ObjectOutputStream OOS = null;
	
	//组件
	JFrame frame = null;
	JLabel PicturLable;
	
	//参数
	int PORT = 9999;
	String IP = null;		// IP地址
	
	//构造函数
	public Control(String ipAddress,int port) {
		PORT = port;
		IP = ipAddress;
		startControl();
	}
	
	//初始化以及开启监控
	void startControl(){
		try {
			
			if(!connect())//连接服务器
				return ;
				
			initFrame();//初始化Frame
			
			if(!initComponents())//初始化界面组件
				return;
			
			recievePicture();//界面绘制
			
			}catch(ConnectException ce) {
				new StarOptionFrame("连接失败",StarOptionFrame.ErrorMessage).setVisible(true);
				System.exit(0);
			} 
			catch (SocketException se) {
				//读取数据时候服务器关闭
				// 写日志
				new StarOptionFrame("对方已退出或拒绝您的连接",StarOptionFrame.ErrorMessage).setVisible(true);
				System.exit(0);
			}
			catch (Exception e) {
				// 写日志
				e.printStackTrace();
				new StarOptionFrame("连接中断",StarOptionFrame.WarnMessage).setVisible(true);
				return ;
			}
	}
	//连接
	boolean connect() {
		StarLoading LoadingGif = new StarLoading(new ImageIcon("imge/LoadingGIF3.gif"));//播放动画，掩盖网络不好时的等待
		LoadingGif.setTimeout(-1);
		LoadingGif.show();
	try {
			// 连接到服务器
			@SuppressWarnings("resource")
			Socket client = new Socket(IP, PORT);
			// 获取客户端的输入流并转换成字节输入流
			din = new DataInputStream(client.getInputStream());
			// 获得客户端的输出流并转换成对象输入流，方便向服务器发送消息
			OOS = new ObjectOutputStream(client.getOutputStream());
			//连接服务器成功
			return true;
		}catch(Exception e) {
			new StarOptionFrame("连接失败,请检查后重试",StarOptionFrame.ErrorMessage).setVisible(true);
			return false;
		} finally {
			LoadingGif.stop();//结束gif的播放
		}
		
		
	}
	//初始化Frame，Frame的基本參數設置
	void initFrame(){
		// 创建面板
		frame = new JFrame("星控视频协助窗口");
		frame.setIconImage((new ImageIcon("imge/StarICON.png")).getImage());
			// 窗口关闭则退出程序
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if(JOptionPane.showConfirmDialog(frame, "退出协助？")==JOptionPane.OK_OPTION) {
					System.exit(0);
				};
				
			}
		}); 
		frame.setSize(800, 600);		
	}
	boolean initComponents() {
		int width,height,IsAllowControl;
		try {
			width = din.readInt();
			height = din.readInt();
			IsAllowControl = din.readInt();
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
		// 更新当前窗口大小
		frame.setSize(width + 400,height);//400为聊天输入宽度
		// 创建一个画板
		JPanel panel = new JPanel();
		panel.setLayout(null);
		JScrollPane jscrollPane = new JScrollPane(panel);// 给panel创建滚动条
		jscrollPane.setBounds(0, 0, width, height);
		panel.setLayout(new FlowLayout(0));				// 设置panel的布局
		frame.add(jscrollPane);							// 将scrollPane添加到窗体中
		
		JTextArea MessageArea = new JTextArea() ;				//消息框
		MessageArea.setFont(new Font("宋体", Font.PLAIN, 20));
		MessageArea.setLineWrap(true);							//自动换行
		JScrollPane messageJScp = new JScrollPane(MessageArea);	//为消息框添加滚动条
		messageJScp.setBounds(width + 50 , 20, 300, 250);		
		((DefaultCaret)MessageArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);//自动滚动至最后
		frame.add(messageJScp);
		
		JButton BackButton = new JButton("发送");				//发送按钮
		BackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//发送消息test
				String s = MessageArea.getText();				//只有不是空消息的时候才发送，减少对方压力，发送后清空输入框
				if(s!=null&&!"".equals(s))
					sendMessageObject(new Message(s));
				MessageArea.setText("");
			}
		});
		BackButton.setBounds(width + 100, 300, 80, 60);
		frame.add(BackButton);
		
		// 创建lable ，用来显示图像
		PicturLable = new JLabel();
		PicturLable.setBounds(0, 0, width, height);
		setMyMouseListener(PicturLable);//为panel绑定定制的鼠标监听器
		panel.add(PicturLable);			// 将lable放到画板中
		
		setMyKeyBoardListener(frame);	//添加键盘监听器
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
		//输出提示信息
		if(IsAllowControl==1) {
			new StarOptionFrame("您可以控制对方的电脑啦！",StarOptionFrame.OKMessage).setVisible(true);
		}else {
			new StarOptionFrame("对方目前不允许您操控哦~",StarOptionFrame.WarnMessage).setVisible(true);
		}
		return true;
	}
	//主要功能，不断执行重绘
	void recievePicture() throws IOException {
		while (true) {// 客户端需要不断读取数据
			// 读取图片数据
			int length = din.readInt();
			byte[] buffer = new byte[length];
			// 读取图片数组放到buffer数组中
			din.readFully(buffer);

			ImageIcon imageIcon = new ImageIcon(buffer);
			PicturLable.setIcon(imageIcon);

			// 需要重新绘制
			frame.repaint();
		}
	}
	/**
	 * 向对方发送event对象
	 * @param event
	 */
	private void sendEventObject(InputEvent event){//发送操作
		try{  
		OOS.writeObject(event);
		OOS.flush();
		
		}catch(SocketException se){
			//服务器端退出
			new StarOptionFrame("退出提醒：对方已关闭软件").setVisible(true);
			System.exit(0);
		}
		catch(Exception ef){
			ef.printStackTrace();
		}
	}
	
	/**
	 * 向对方发送消息的函数
	 * @param m
	 */
	private void sendMessageObject(Message m){//发送操作
		try{  
		OOS.writeObject(m);
		OOS.flush();
		
		}catch(Exception ef){
			ef.printStackTrace();
		}
	}
	/**
	 * 将com绑定一个自定义的鼠标监听器
	 * @param com
	 */
	private void setMyMouseListener(Component com) {
		//鼠标监听器
		com.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
				sendEventObject(e);
			}
			public void mousePressed(MouseEvent e) {
				sendEventObject(e);
			}
			public void mouseExited(MouseEvent e) {//鼠标进入进出并影响操作不发送指令以节省网络
				//sendEventObject(e);
			}
			public void mouseEntered(MouseEvent e) {
				//sendEventObject(e);
			}
			public void mouseClicked(MouseEvent e) {
				sendEventObject(e);
				
			}
			});

		//鼠标动作监听器
		com.addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent e) {
				sendEventObject(e);
			}
			public void mouseDragged(MouseEvent e) {
				sendEventObject(e);
			}
			});
		
		//监听鼠标滚轮
		com.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				sendEventObject(e);
			}
			});
		
	}
	/**
	 * 键盘需要绑定到frame上才可以
	 * @param com
	 */
	private void setMyKeyBoardListener(Component com) {
		//监听键盘,特殊处理
		com.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
				sendEventObject(e);
			}
			public void keyReleased(KeyEvent e) {
				sendEventObject(e);
			}
			public void keyPressed(KeyEvent e) {
				sendEventObject(e);
			}
		});		
	}




	



}
