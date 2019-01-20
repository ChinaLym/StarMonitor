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
 * ����Ƶ�ͳһ΢͸����񣬵���δʵװ
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
	//�����ݼ���ʶ�����������ö���ȼ�ʱ�����¼������������û����µ��ȼ�
	public static final int FUNC2_KEY_MARK = 2;
	public static final int FUNC_KEY_MARK = 1;
	public static final int EXIT_KEY_MARK = 0;
	
	//��������Ϊ��̬������ֻ��һ��Frame����
	static JFrame frame ;
	static JTextArea MessageArea;
	static JRadioButton AllowControlButton = null;
	static JLabel ConnectNum = null;
	//�Ƿ�������ƣ��ñ���Ϊ��̬������ֻ��  ֻ���������û����ƻ������������Ѿ����������û�����
	volatile static boolean AllowControl = false;
	
	//������Ϣ��Frame������
	static {
		frame = new JFrame("��Ϣ��");
		frame.setBounds(100, 100, 290, 340);
		frame.setLayout(null);
		frame.setUndecorated(true);//ȥ���߿�
		frame.setBackground(new Color(180,180,250,220));
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
		MessageArea.setFont(new Font("����", Font.PLAIN, 20));
		JScrollPane jsp = new JScrollPane(MessageArea);
		jsp.setBounds(5, 5, 280, 200);
		((DefaultCaret) MessageArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		frame.add(jsp);
		//�����Ƿ���������˿��Ʊ���
		AllowControlButton = new JRadioButton("�Ƿ��������");
		AllowControlButton.setFont(new Font("����", Font.BOLD, 20));
		AllowControlButton.setBounds(20, 200, 160, 30);
		AllowControlButton.setBackground(new Color(0,0,0,0));
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
		
		JRadioButton MoreInformationButton = new JRadioButton("����");
		MoreInformationButton.setBackground(new Color(0,0,0,0));
		MoreInformationButton.setFont(new Font("����", Font.BOLD, 20));
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
		//��ʾ
		JLabel tip = new JLabel("alt+C���л��������");
		tip.setFont(new Font("����", Font.BOLD, 18));
		tip.setBounds(20, 230, 200, 30);
		frame.add(tip);
		JLabel tip2 = new JLabel("alt+G����������");
		tip2.setFont(new Font("����", Font.BOLD, 18));
		tip2.setBounds(20, 260, 200, 30);
		frame.add(tip2);
		JLabel tip3 = new JLabel("alt+Q���˳����");
		tip3.setFont(new Font("����", Font.BOLD, 18));
		tip3.setBounds(20, 290, 200, 30);
		frame.add(tip3);
		//��ʾ���Ӹ���
		JPanel infoPanel = new JPanel();
		//infoPanel.setBorder(BorderFactory.createTitledBorder("������"));
		infoPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "������", TitledBorder.LEADING, TitledBorder.TOP, new Font("����", Font.PLAIN, 15)));
		infoPanel.setBackground(new Color(0,0,0,0));
		infoPanel.setBounds(175, 270, 100, 60);
		frame.add(infoPanel);
		ConnectNum = new JLabel("0");
		ConnectNum.setFont(new Font("����", Font.PLAIN, 30));
		ConnectNum.setBounds(0, 0, 100, 50);
		ConnectNum.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				ConnectNum.setText((Thread.activeCount()-3)/2+"");
			}
		});
		ConnectNum.setToolTipText("�����ˢ��");
		infoPanel.add(ConnectNum);
		addMessage("���UI�Ѿ������ϣ���δʵװ��StarMonitor");
		registerShortcutKey();
		//���������ڿɼ�
		frame.setVisible(true);
		
	}
	/**
	 * ��ʼ������
	 * @param PermissionLevel
	 */
	public ResortUI(int PermissionLevel) {
		//��ʼ�����صı���
		AllowControl = PermissionLevel==0?false:true;
		AllowControlButton.setSelected(AllowControl);
	}
	/**
	 * ��������������ǰ��
	 */
	public static void setFrameFocus() {
		frame.setBounds(100, 100, frame.getWidth(), frame.getHeight());
    	frame.setExtendedState(JFrame.NORMAL);
    	frame.toFront();
    	frame.requestFocus();
	}
	/**
	 * ˢ�¼��������ڶ��߳����أ������ܿ��ܳ��ִ���
	 * ���԰Ѽ�����ʽ�ĳɻ�ȡ    �洢�ڿͻ������ӵ�����ĳ���
	 */
	public static void reFreshConnectNum() {
		ConnectNum.setText((Thread.activeCount()-3)/2+"");
	}
	//����Ϣ��MessageArea�������Ϣ
	public static synchronized void addMessage(String s) {
		MessageArea.append(s + "\n");
	}
	
	/**
	 * ע���ݼ��������ע�����������Ӧ�ļ������Լ�ִ�к���
	 * 
	 */
	static void registerShortcutKey(){
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


}
