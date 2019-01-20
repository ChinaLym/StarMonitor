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
	
	//�����ȼ���ʶ�����������ö���ȼ�ʱ�����¼������������û����µ��ȼ�
	public static final int FUNC2_KEY_MARK = 2;
	public static final int FUNC_KEY_MARK = 1;
	public static final int EXIT_KEY_MARK = 0;
	
	//��������Ϊ��̬������ֻ��һ��Frame����
	static Robot robot = null;
	static JFrame frame ;
	static JTextArea MessageArea;
	static JRadioButton AllowControlButton = null;
	static JLabel ConnectNum = null;
	//�Ƿ�������ƣ��ñ���Ϊ��̬������ֻ��  ֻ���������û����ƻ������������Ѿ����������û�����
	volatile static boolean AllowControl = false;
	//Ϊÿ�����Ӵ����ı���
	Socket server;
	DataInputStream din = null;
	ObjectInputStream OIS = null;
	
	//������Ϣ��Frame������
	static {
		frame = new JFrame("��Ϣ��");
		frame.setBounds(100, 100, 285, 350);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setAlwaysOnTop(true);
		frame.setResizable(false);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int res = JOptionPane.showConfirmDialog(frame, "ȷ���˳���","ȷ���˳�",JOptionPane.OK_CANCEL_OPTION);
				
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
		MessageArea.setFont(new Font("����", Font.PLAIN, 20));
		JScrollPane jsp = new JScrollPane(MessageArea);
		jsp.setBounds(0, 0, 280, 200);
		((DefaultCaret) MessageArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		frame.add(jsp);
		//�����Ƿ���������˿��Ʊ���
		AllowControlButton = new JRadioButton("�Ƿ��������");
		AllowControlButton.setBounds(20, 200, 160, 30);
		AllowControlButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(AllowControlButton.isSelected()) {
					AllowControl = true;
					addMessage("=ϵͳ��Ϣ�̣����������");
					frame.requestFocus();
				}else{
					AllowControl = false;
					addMessage("=ϵͳ��Ϣ�����Ѿܾ�����");
					frame.requestFocus();
				}
			}
		});
		frame.add(AllowControlButton);
		
		JRadioButton MoreInformationButton = new JRadioButton("��ʾ");
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
		//��ʾ
		JLabel tip = new JLabel("alt+C���л��Ƿ��������");
		tip.setBounds(20, 230, 200, 30);
		frame.add(tip);
		JLabel tip2 = new JLabel("alt+G����������");
		tip2.setBounds(20, 260, 200, 30);
		frame.add(tip2);
		JLabel tip3 = new JLabel("alt+Q���˳����");
		tip3.setBounds(20, 290, 200, 30);
		frame.add(tip3);
		//��ʾ���Ӹ���
		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(BorderFactory.createTitledBorder("������"));
		infoPanel.setBounds(175, 260, 100, 60);
		frame.add(infoPanel);
		ConnectNum = new JLabel("0");
		ConnectNum.setFont(new Font("����", Font.PLAIN, 20));
		ConnectNum.setBounds(0, 0, 100, 50);
		ConnectNum.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				reFreshConnectNum();
			}
		});
		ConnectNum.setToolTipText("�����ˢ��");
		infoPanel.add(ConnectNum);
		
		registerShortcutKey();
		//���������ڿɼ�
		frame.setVisible(true);
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
			System.out.println("����ָ�������ʧ��");
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
	//Ϊ�����߳��ṩ��ѯ�ɷ������Ϣ����
	public static boolean ifCanAppend() {return  MessageArea!=null;}
	//����Ϣ���������Ϣ
	public static synchronized void addMessage(String s) {
		MessageArea.append(s + "\n");
	}
	/**
	 * ��ʼ������
	 * @param sever
	 * @param PermissionLevel
	 */
	public ResortReceiveThread(Socket sever,int PermissionLevel) {
		//��ʼ�����صı���
		this.server = sever;
		AllowControl = PermissionLevel==0?false:true;
		AllowControlButton.setSelected(AllowControl);
		
		// ��ȡ�ͻ��˵�������
		InputStream is = null;
		try {
			is = sever.getInputStream();
			OIS = new ObjectInputStream(is);
			if(ifCanAppend()) {
				//�����û��������û������أ������ѵ�ǰ�Ŀ���Ȩ��
				addMessage("+ϵͳ��Ϣ+�����û�������\n(ip:"+server.getInetAddress().toString()+")");
				addMessage("-ϵͳ��Ϣ-����ǰ"+ (AllowControl?"����":"�ܾ�")  + "����" );
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
			System.out.println("��ȡ������ʧ��");//�׳��ر�ͨ��
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
		//ע���ݼ�
				try {
					JIntellitype.getInstance().registerHotKey(FUNC2_KEY_MARK, JIntellitype.MOD_ALT, (int)'G');  
					JIntellitype.getInstance().registerHotKey(FUNC_KEY_MARK, JIntellitype.MOD_ALT, (int)'C');  
			    	JIntellitype.getInstance().registerHotKey(EXIT_KEY_MARK, JIntellitype.MOD_ALT, (int)'Q');  
			        //Ϊ��ע��Ŀ�ݼ���Ӽ���
			    	JIntellitype.getInstance().addHotKeyListener(new HotkeyListener() {
						public void onHotKey(int markCode) {
							switch (markCode) {  
					        case FUNC_KEY_MARK:  //�л��������
					        	if(AllowControlButton.isSelected()) {
					        		AllowControl = false;
									addMessage("=ϵͳ��Ϣ�����Ѿܾ�����");
					        		AllowControlButton.setSelected(false);
					        		frame.requestFocus();
					        	}else {
					        		AllowControl = true;
									addMessage("=ϵͳ��Ϣ�̣����������");
					        		AllowControlButton.setSelected(true);
					        		frame.requestFocus();
					        	}
					            break;  
					        case FUNC2_KEY_MARK:  
					        	setFrameFocus();
					            break; 
					        case EXIT_KEY_MARK:  //�˳����
					        	System.exit(0);
					            break;   
							} 				
						}
					});
				}catch(Exception e) {
					addMessage("��ϵͳ��Ϣ������ݼ�����������ʧ��");
				}
	}
	
	@Override
	public void run() {
		Receive();
	}

	/*
	 * ˼·��
	 * ����ָ�����Ϣ���Ƚ�С������ʵ��ʵʱ����
	 * 1.ʹ��ͬһ������Ҫ������Ϣ����Ҫ����ָ�����Ҫʹ�����������
	 * 	��ͬ������գ������ֶ���Ȼ���жϸö��󵽵������ֶ��󼴿ɡ�
	 * 
	 * 2.������Ҫ�ж���ͨ���Ƿ����ʹ�ã����������ʹ�ã���֤���ڳ�ʼ��������
	 * 	�����쳣����ֱ�ӽ����ý��̲��ͷ�ռ�õ�ϵͳ��Դ
	 * 3.ʹ��Object������ն�������������Ϣ������ʹ����Ϣ������գ��������
	 * 	�������գ��жϷ�ʽ��Message�����У���ö���ܿ�����һ��ָ�����������Ϊ
	 * 	��������£�����������������紫�����ֽڳ��ִ��������ݴ����յ�
	 * 	�Ķ���ֻ�������֡����жϱ����������Ƿ�������ƣ������������ִ�У�����
	 * 	�������Խ�����һ������
	 * */
	public void Receive() {
		try {//��try-catch������ѭ���������ʡ��Դ
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
				
				}else return;//IOS�����ڽ������߳�
			}
		}catch(SocketException se) {
			addMessage("-ϵͳ��Ϣ-�����û��˳����\n(ip:"+server.getInetAddress().toString()+")");
			setFrameFocus();
			ConnectNum.setText(Integer.parseInt(ConnectNum.getText())-1+"");
			//�ͻ����˳�
			return; //�������߳�
		}
		catch (ClassNotFoundException|IOException e1) {
			e1.printStackTrace();
			System.out.println("��ȡָ����Ч");
		} catch(ClassCastException ce) {//ǿ������ת��������Ըô���
			//һ��ָ�����Ϣ��ʧ�����д���
			//ce.printStackTrace();
		}
	}
	 
	 
    public void handleEvents(InputEvent event){//����������ָ���ڱ���ִ��
        MouseEvent mevent = null ; //����¼�
        MouseWheelEvent mwevent = null ;//�������¼�
        KeyEvent kevent = null ; //�����¼�
        int mousebuttonmask = -100; //��갴��
        /*
         * ˼·��
         * �ø����������Ӷ���
         * ���ø������getID����,���¼���idȡ������switch�жϸ��¼�Ϊ���¼�
         * �����¼�ת��Ϊʵ�ʶ���
         * �ö���õĻ������ڱ���ִ�и��¼�
         * 
         * */
        switch (event.getID()){
        //������ 
        case MouseEvent.MOUSE_MOVED :                       //����ƶ�
            mevent = ( MouseEvent )event ;
            robot.mouseMove( mevent.getX() , mevent.getY() );
            break ;
        case MouseEvent.MOUSE_PRESSED :                      //��������
            mevent = ( MouseEvent ) event;
            //Ϊ�˱�֤����¼�����ȷ���Ƚ�����ƶ�������¼������λ��
            robot.mouseMove( mevent.getX() , mevent.getY() );
            mousebuttonmask = getMouseClick(mevent.getButton() );
            //���ҽ���ָ����ȷʱ��ִ��ָ��
            if(mousebuttonmask != -100)
            	robot.mousePress(mousebuttonmask);
            break;
         case MouseEvent.MOUSE_RELEASED :              //�����ɿ�
            mevent = ( MouseEvent ) event;
          //Ϊ�˱�֤����¼�����ȷ���Ƚ�����ƶ�������¼������λ��
            robot.mouseMove( mevent.getX() , mevent.getY() );
            mousebuttonmask = getMouseClick( mevent.getButton() );//ȡ����갴��
            if(mousebuttonmask != -100)
            	robot.mouseRelease( mousebuttonmask );
            break ;
        case MouseEvent.MOUSE_WHEEL :                  //������
            mwevent = ( MouseWheelEvent ) event ;
            robot.mouseWheel(mwevent.getWheelRotation());
            break ;
         case MouseEvent.MOUSE_DRAGGED :                      //�����ק
            mevent = ( MouseEvent ) event ;
            robot.mouseMove( mevent.getX(), mevent.getY() );
            break ;
            //���̲���
         case KeyEvent.KEY_PRESSED :                     //����
            kevent = ( KeyEvent ) event;
            robot.keyPress( kevent.getKeyCode() );
            break ;
         case KeyEvent.KEY_RELEASED :                    //�ɼ�
            kevent= ( KeyEvent ) event ;
            robot.keyRelease( kevent.getKeyCode() );
            break ;
        default: break ;//�Բ�Ӱ��ʹ�õ��¼������д���
        }


    }
//��������¼������Ϊ�ĸ�����������յ���ָ�Ϊ��������ִ�е�ָ�����-100
    private int getMouseClick(int button) {    //ȡ����갴��
        if (button == MouseEvent.BUTTON1) //��� ,�м��ΪBUTTON2
            return InputEvent.BUTTON1_MASK;
        if (button == MouseEvent.BUTTON3) //�Ҽ�
            return InputEvent.BUTTON3_MASK;
        return -100;
    }   

}
