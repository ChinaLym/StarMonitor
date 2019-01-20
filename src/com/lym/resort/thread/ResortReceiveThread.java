package com.lym.resort.thread;

import java.awt.AWTException;
import java.awt.Font;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import com.lym.obj.Message;
import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;


public class ResortReceiveThread extends Thread {
	
	//定义热键标识，用于在设置多个热键时，在事件处理中区分用户按下的热键
	public static final int FUNC2_KEY_MARK = 2;
	public static final int FUNC_KEY_MARK = 1;
	public static final int EXIT_KEY_MARK = 0;
	
	//交互窗口为静态变量，只有一个Frame窗口
	static Robot robot = null;
	static JFrame frame ;
	static JTextArea MessageArea;
	static JRadioButton AllowControlButton = null;
	static JLabel ConnectNum = null;
	//是否允许控制，该变量为静态变量，只能  只能允许本机用户控制或者允许所有已经连本机的用户控制
	volatile static boolean AllowControl = false;
	//为每个连接创建的变量
	Socket server;
	DataInputStream din = null;
	ObjectInputStream OIS = null;
	
	//加载信息框Frame的内容
	static {
		frame = new JFrame("消息框");
		frame.setBounds(100, 100, 285, 350);
		frame.setLayout(null);
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
		
		MessageArea = new JTextArea();
		MessageArea.setEditable(false);
		MessageArea.setLineWrap(true);
		MessageArea.setFont(new Font("宋体", Font.PLAIN, 20));
		JScrollPane jsp = new JScrollPane(MessageArea);
		jsp.setBounds(0, 0, 280, 200);
		((DefaultCaret) MessageArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		frame.add(jsp);
		//设置是否可以其他人控制本机
		AllowControlButton = new JRadioButton("是否允许控制");
		AllowControlButton.setBounds(20, 200, 160, 30);
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
		
		JRadioButton MoreInformationButton = new JRadioButton("提示");
		MoreInformationButton.setSelected(true);
		MoreInformationButton.setBounds(200, 200, 200, 30);
		MoreInformationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(MoreInformationButton.isSelected()) {
					frame.setSize(285, 350);
				}else
					frame.setSize(285, 260);
				
			}
		});
		frame.add(MoreInformationButton);
		//提示
		JLabel tip = new JLabel("alt+C：切换是否允许控制");
		tip.setBounds(20, 230, 200, 30);
		frame.add(tip);
		JLabel tip2 = new JLabel("alt+G：呼出界面");
		tip2.setBounds(20, 260, 200, 30);
		frame.add(tip2);
		JLabel tip3 = new JLabel("alt+Q：退出软件");
		tip3.setBounds(20, 290, 200, 30);
		frame.add(tip3);
		//显示连接个数
		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(BorderFactory.createTitledBorder("连接数"));
		infoPanel.setBounds(175, 260, 100, 60);
		frame.add(infoPanel);
		ConnectNum = new JLabel("0");
		ConnectNum.setFont(new Font("黑体", Font.PLAIN, 20));
		ConnectNum.setBounds(0, 0, 100, 50);
		ConnectNum.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				reFreshConnectNum();
			}
		});
		ConnectNum.setToolTipText("点击可刷新");
		infoPanel.add(ConnectNum);
		
		registerShortcutKey();
		//设置主窗口可见
		frame.setVisible(true);
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
			System.out.println("创建指令机器人失败");
		}
		
		new Thread(new Runnable() {
			public void run() {
				try {
					while(true) {
						Thread.sleep(500);
						reFreshConnectNum();
					}
				}catch (Exception e) {
				}
			}
		}).start();
	}
	public static void setFrameFocus() {
		frame.setBounds(100, 100, frame.getWidth(), frame.getHeight());
    	frame.setExtendedState(JFrame.NORMAL);
    	frame.toFront();
    	frame.requestFocus();
	}
	public static synchronized void reFreshConnectNum() {
		ConnectNum.setText((Thread.activeCount()-4)/2+"");
	}
	//为其他线程提供查询可否添加信息方法
	public static boolean ifCanAppend() {return  MessageArea!=null;}
	//向信息框中添加消息
	public static synchronized void addMessage(String s) {
		MessageArea.append(s + "\n");
	}
	/**
	 * 初始化函数
	 * @param sever
	 * @param PermissionLevel
	 */
	public ResortReceiveThread(Socket sever,int PermissionLevel) {
		//初始化本地的变量
		this.server = sever;
		AllowControl = PermissionLevel==0?false:true;
		AllowControlButton.setSelected(AllowControl);
		
		// 获取客户端的输入流
		InputStream is = null;
		try {
			is = sever.getInputStream();
			OIS = new ObjectInputStream(is);
			if(ifCanAppend()) {
				//提醒用户有其他用户加入监控，并提醒当前的控制权限
				addMessage("+系统消息+：有用户加入监控\n(ip:"+server.getInetAddress().toString()+")");
				addMessage("-系统消息-：当前"+ (AllowControl?"允许":"拒绝")  + "控制" );
				frame.requestFocus();
				reFreshConnectNum();
			}
		}catch(SocketException|EOFException eofE) {
			if(this.server!=null)
				try {
					this.server.close();
				} catch (Exception e2) {
				}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("获取对象流失败");//抛出关闭通道
			if(is != null)
				try {
					is.close();
				} catch (IOException e1) {
				}
			if(OIS != null)
				try {
					OIS.close();
				} catch (IOException e1) {
				}
			
		} 
	}

	static void registerShortcutKey(){
		//注册快捷键
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
	
	@Override
	public void run() {
		Receive();
	}

	/*
	 * 思路：
	 * 不论指令和消息长度较小，可以实现实时分析
	 * 1.使用同一个流既要接收消息，又要接收指令，则需要使用两个对象的
	 * 	共同父类接收，这两种对象，然后判断该对象到底是哪种对象即可。
	 * 
	 * 2.首先需要判断流通道是否可以使用，如果不可以使用，则证明在初始化工作中
	 * 	产生异常，则直接结束该进程并释放占用的系统资源
	 * 3.使用Object对象接收对象流传来的信息，尝试使用信息对象接收，如果不能
	 * 	正常接收（判断方式在Message对象中）则该对象很可能是一个指令操作，【因为
	 * 	正常情况下（不出现物理错误：网络传输中字节出现错误导致数据错误）收到
	 * 	的对象只有这两种】则判断本机的设置是否允许控制，若允许控制则执行，否则
	 * 	继续尝试接收下一个对象。
	 * */
	public void Receive() {
		try {//把try-catch放置在循环的外面节省资源
			reFreshConnectNum();
			while (true) {
				if(OIS!=null) {
					Object o =  OIS.readObject();
					Message m = new Message(o);
					if(m!=null&&m.getType()!=0)
						addMessage(m.getMessage());
					else if(AllowControl) {
						 InputEvent e =(InputEvent)o;
			             if(e!=null){
			            	 handleEvents(e);
			             }
		             }
				
				}else return;//IOS不存在结束该线程
			}
		}catch(SocketException se) {
			addMessage("-系统消息-：有用户退出监控\n(ip:"+server.getInetAddress().toString()+")");
			setFrameFocus();
			ConnectNum.setText(Integer.parseInt(ConnectNum.getText())-1+"");
			//客户端退出
			return; //结束该线程
		}
		catch (ClassNotFoundException|IOException e1) {
			e1.printStackTrace();
			System.out.println("获取指令无效");
		} catch(ClassCastException ce) {//强制类型转换出错忽略该错误
			//一条指令或信息丢失不进行处理
			//ce.printStackTrace();
		}
	}
	 
	 
    public void handleEvents(InputEvent event){//将发送来的指令在本机执行
        MouseEvent mevent = null ; //鼠标事件
        MouseWheelEvent mwevent = null ;//鼠标滚动事件
        KeyEvent kevent = null ; //键盘事件
        int mousebuttonmask = -100; //鼠标按键
        /*
         * 思路：
         * 用父类对象接受子对象
         * 调用父类对象getID方法,将事件的id取出并用switch判断该事件为何事件
         * 将该事件转化为实际对象
         * 用定义好的机器人在本机执行该事件
         * 
         * */
        switch (event.getID()){
        //鼠标操作 
        case MouseEvent.MOUSE_MOVED :                       //鼠标移动
            mevent = ( MouseEvent )event ;
            robot.mouseMove( mevent.getX() , mevent.getY() );
            break ;
        case MouseEvent.MOUSE_PRESSED :                      //鼠标键按下
            mevent = ( MouseEvent ) event;
            //为了保证点击事件的正确，先将鼠标移动至点击事件的鼠标位置
            robot.mouseMove( mevent.getX() , mevent.getY() );
            mousebuttonmask = getMouseClick(mevent.getButton() );
            //当且仅当指令正确时才执行指令
            if(mousebuttonmask != -100)
            	robot.mousePress(mousebuttonmask);
            break;
         case MouseEvent.MOUSE_RELEASED :              //鼠标键松开
            mevent = ( MouseEvent ) event;
          //为了保证点击事件的正确，先将鼠标移动至点击事件的鼠标位置
            robot.mouseMove( mevent.getX() , mevent.getY() );
            mousebuttonmask = getMouseClick( mevent.getButton() );//取得鼠标按键
            if(mousebuttonmask != -100)
            	robot.mouseRelease( mousebuttonmask );
            break ;
        case MouseEvent.MOUSE_WHEEL :                  //鼠标滚动
            mwevent = ( MouseWheelEvent ) event ;
            robot.mouseWheel(mwevent.getWheelRotation());
            break ;
         case MouseEvent.MOUSE_DRAGGED :                      //鼠标拖拽
            mevent = ( MouseEvent ) event ;
            robot.mouseMove( mevent.getX(), mevent.getY() );
            break ;
            //键盘操作
         case KeyEvent.KEY_PRESSED :                     //按键
            kevent = ( KeyEvent ) event;
            robot.keyPress( kevent.getKeyCode() );
            break ;
         case KeyEvent.KEY_RELEASED :                    //松键
            kevent= ( KeyEvent ) event ;
            robot.keyRelease( kevent.getKeyCode() );
            break ;
        default: break ;//对不影响使用的事件不进行处理
        }


    }
//返回鼠标事件点击的为哪个键，如果接收到的指令不为本机可以执行的指令，返回-100
    private int getMouseClick(int button) {    //取得鼠标按键
        if (button == MouseEvent.BUTTON1) //左键 ,中间键为BUTTON2
            return InputEvent.BUTTON1_MASK;
        if (button == MouseEvent.BUTTON3) //右键
            return InputEvent.BUTTON3_MASK;
        return -100;
    }   

}
