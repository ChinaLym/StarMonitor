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
 * Э������Ŀǰ���ֲ�����ã����Խ����޸ģ������С�̶����ܱ�Ŀǰ
 * @author slt
 *
 */
public class Control {
	//��������
	DataInputStream din = null;
	ObjectOutputStream OOS = null;
	
	//���
	JFrame frame = null;
	JLabel PicturLable;
	
	//����
	int PORT = 9999;
	String IP = null;		// IP��ַ
	
	//���캯��
	public Control(String ipAddress,int port) {
		PORT = port;
		IP = ipAddress;
		startControl();
	}
	
	//��ʼ���Լ��������
	void startControl(){
		try {
			
			if(!connect())//���ӷ�����
				return ;
				
			initFrame();//��ʼ��Frame
			
			if(!initComponents())//��ʼ���������
				return;
			
			recievePicture();//�������
			
			}catch(ConnectException ce) {
				new StarOptionFrame("����ʧ��",StarOptionFrame.ErrorMessage).setVisible(true);
				System.exit(0);
			} 
			catch (SocketException se) {
				//��ȡ����ʱ��������ر�
				// д��־
				new StarOptionFrame("�Է����˳���ܾ���������",StarOptionFrame.ErrorMessage).setVisible(true);
				System.exit(0);
			}
			catch (Exception e) {
				// д��־
				e.printStackTrace();
				new StarOptionFrame("�����ж�",StarOptionFrame.WarnMessage).setVisible(true);
				return ;
			}
	}
	//����
	boolean connect() {
		StarLoading LoadingGif = new StarLoading(new ImageIcon("imge/LoadingGIF3.gif"));//���Ŷ������ڸ����粻��ʱ�ĵȴ�
		LoadingGif.setTimeout(-1);
		LoadingGif.show();
	try {
			// ���ӵ�������
			@SuppressWarnings("resource")
			Socket client = new Socket(IP, PORT);
			// ��ȡ�ͻ��˵���������ת�����ֽ�������
			din = new DataInputStream(client.getInputStream());
			// ��ÿͻ��˵��������ת���ɶ����������������������������Ϣ
			OOS = new ObjectOutputStream(client.getOutputStream());
			//���ӷ������ɹ�
			return true;
		}catch(Exception e) {
			new StarOptionFrame("����ʧ��,���������",StarOptionFrame.ErrorMessage).setVisible(true);
			return false;
		} finally {
			LoadingGif.stop();//����gif�Ĳ���
		}
		
		
	}
	//��ʼ��Frame��Frame�Ļ��������O��
	void initFrame(){
		// �������
		frame = new JFrame("�ǿ���ƵЭ������");
		frame.setIconImage((new ImageIcon("imge/StarICON.png")).getImage());
			// ���ڹر����˳�����
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if(JOptionPane.showConfirmDialog(frame, "�˳�Э����")==JOptionPane.OK_OPTION) {
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
		// ���µ�ǰ���ڴ�С
		frame.setSize(width + 400,height);//400Ϊ����������
		// ����һ������
		JPanel panel = new JPanel();
		panel.setLayout(null);
		JScrollPane jscrollPane = new JScrollPane(panel);// ��panel����������
		jscrollPane.setBounds(0, 0, width, height);
		panel.setLayout(new FlowLayout(0));				// ����panel�Ĳ���
		frame.add(jscrollPane);							// ��scrollPane��ӵ�������
		
		JTextArea MessageArea = new JTextArea() ;				//��Ϣ��
		MessageArea.setFont(new Font("����", Font.PLAIN, 20));
		MessageArea.setLineWrap(true);							//�Զ�����
		JScrollPane messageJScp = new JScrollPane(MessageArea);	//Ϊ��Ϣ����ӹ�����
		messageJScp.setBounds(width + 50 , 20, 300, 250);		
		((DefaultCaret)MessageArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);//�Զ����������
		frame.add(messageJScp);
		
		JButton BackButton = new JButton("����");				//���Ͱ�ť
		BackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//������Ϣtest
				String s = MessageArea.getText();				//ֻ�в��ǿ���Ϣ��ʱ��ŷ��ͣ����ٶԷ�ѹ�������ͺ���������
				if(s!=null&&!"".equals(s))
					sendMessageObject(new Message(s));
				MessageArea.setText("");
			}
		});
		BackButton.setBounds(width + 100, 300, 80, 60);
		frame.add(BackButton);
		
		// ����lable ��������ʾͼ��
		PicturLable = new JLabel();
		PicturLable.setBounds(0, 0, width, height);
		setMyMouseListener(PicturLable);//Ϊpanel�󶨶��Ƶ���������
		panel.add(PicturLable);			// ��lable�ŵ�������
		
		setMyKeyBoardListener(frame);	//��Ӽ��̼�����
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
		//�����ʾ��Ϣ
		if(IsAllowControl==1) {
			new StarOptionFrame("�����Կ��ƶԷ��ĵ�������",StarOptionFrame.OKMessage).setVisible(true);
		}else {
			new StarOptionFrame("�Է�Ŀǰ���������ٿ�Ŷ~",StarOptionFrame.WarnMessage).setVisible(true);
		}
		return true;
	}
	//��Ҫ���ܣ�����ִ���ػ�
	void recievePicture() throws IOException {
		while (true) {// �ͻ�����Ҫ���϶�ȡ����
			// ��ȡͼƬ����
			int length = din.readInt();
			byte[] buffer = new byte[length];
			// ��ȡͼƬ����ŵ�buffer������
			din.readFully(buffer);

			ImageIcon imageIcon = new ImageIcon(buffer);
			PicturLable.setIcon(imageIcon);

			// ��Ҫ���»���
			frame.repaint();
		}
	}
	/**
	 * ��Է�����event����
	 * @param event
	 */
	private void sendEventObject(InputEvent event){//���Ͳ���
		try{  
		OOS.writeObject(event);
		OOS.flush();
		
		}catch(SocketException se){
			//���������˳�
			new StarOptionFrame("�˳����ѣ��Է��ѹر����").setVisible(true);
			System.exit(0);
		}
		catch(Exception ef){
			ef.printStackTrace();
		}
	}
	
	/**
	 * ��Է�������Ϣ�ĺ���
	 * @param m
	 */
	private void sendMessageObject(Message m){//���Ͳ���
		try{  
		OOS.writeObject(m);
		OOS.flush();
		
		}catch(Exception ef){
			ef.printStackTrace();
		}
	}
	/**
	 * ��com��һ���Զ������������
	 * @param com
	 */
	private void setMyMouseListener(Component com) {
		//��������
		com.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
				sendEventObject(e);
			}
			public void mousePressed(MouseEvent e) {
				sendEventObject(e);
			}
			public void mouseExited(MouseEvent e) {//�����������Ӱ�����������ָ���Խ�ʡ����
				//sendEventObject(e);
			}
			public void mouseEntered(MouseEvent e) {
				//sendEventObject(e);
			}
			public void mouseClicked(MouseEvent e) {
				sendEventObject(e);
				
			}
			});

		//��궯��������
		com.addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent e) {
				sendEventObject(e);
			}
			public void mouseDragged(MouseEvent e) {
				sendEventObject(e);
			}
			});
		
		//����������
		com.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				sendEventObject(e);
			}
			});
		
	}
	/**
	 * ������Ҫ�󶨵�frame�ϲſ���
	 * @param com
	 */
	private void setMyKeyBoardListener(Component com) {
		//��������,���⴦��
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
