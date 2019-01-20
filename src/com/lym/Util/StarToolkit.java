package com.lym.Util;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JTextField;

import com.lym.obj.IPandPort;


public class StarToolkit {
	final static String TIPWORDS = "�����ǿ�Զ��Э���п�����һ���ǿ��������󣬿�������һ�°ɣ�����������Ϣ�����ǿص���ұ�Э�����˾Ϳ����������ҵĵ�����~"; 
	final static String  COMMANDDELIMITER = "~$@%";
	final static String  REGCOMMANDDELIMITER = "\\~\\$\\@\\%";
	static char[] opchars= null;
	
	public static IPandPort IpAndPort = new IPandPort();
	
	/**
	 * ����Ҫ�������ڵĴ�С�� ���ظô���Ҫ������ʾʱ�������Ͻ�Ӧ���ڵ�λ��
	 * @param width
	 * @param height
	 * @return
	 */
	public static Point getCenterPoint(int width, int height) {
		//�Ȼ�ȡ��Ļ�Ŀ��
		Dimension   screensize   =   Toolkit.getDefaultToolkit().getScreenSize();
		int ScreenWidth = (int)screensize.getWidth();
		int ScreenHeight = (int)screensize.getHeight();
		
		return new Point((ScreenWidth-width)/2, (ScreenHeight-height)/2);
	}
	/**
	 * ����jframe����Ļ�о�����ʾ
	 * @param jframe
	 */
	public static void setJFrameOnCenter(JFrame jframe) {
		jframe.setLocation(getCenterPoint(jframe.getWidth(),jframe.getHeight()));
	}
	/**
	 * ����jframe����Ļ�о�����ʾ,��ָ������
	 * @param jframe
	 */
	public static void setJFrameOnCenter(JFrame jframe,int width,int height) {
		jframe.setBounds(new Rectangle(getCenterPoint(width,height),new Dimension(width, height)));
	}


	
	/**
	 * �����ַ���s����������
	 * @param s
	 * @return
	 */
	public static boolean CopyStringToClipboard(String s) {
		try {
			Clipboard c = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
			Transferable tarnsf = new StringSelection(s);
			c.setContents(tarnsf, null);
			return true;
		}catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * ����һ��ip��port�����ݴ˴����ǿ������
	 * @param ip
	 * @param port
	 * @return
	 */
	public static String createStarCommand(String ip,int port) {
		return TIPWORDS + COMMANDDELIMITER + CodeIPandPort(ip+":"+port);
	}
	
	/**
	 * ����һ��ip��port�����ݴ˴����ǿ���������ǿ���Ƶ���������
	 * @param ip
	 * @param port
	 * @return
	 */
	public static boolean createStarCommandToClipboard(String ip,int port) {
		return CopyStringToClipboard(createStarCommand(ip,port));
	}
	
	/**
	 * ���������е����ݣ��Ƿ���һ���ǿ������Ƿ���true
	 * @return
	 */
	public static boolean analysisStar() {
		Clipboard c = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable tarnsf = c.getContents(null);
		if(tarnsf!=null) {
			if(tarnsf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				try {
					String text = (String)tarnsf.getTransferData(DataFlavor.stringFlavor);
					return analysisStar(text);
					}
				catch (Exception e) {
					return false;
				}
			}
		}
		return false;
	}
	
	/**
	 * ����һ���ǿ���������ǿ����������ɹ�����true,������ip��port����IpAndPort������
	 * @param s
	 * @return
	 */
	public static boolean analysisStar(String s) {
		try {
			String[] strs = (s.trim()).split(REGCOMMANDDELIMITER);
			if(strs.length!=2)return false;
			strs[1] = CodeIPandPort(strs[1]);
			String[] IPandPort = strs[1].split(":");
			if(IPandPort.length!=2)return false;
			String ip = IPandPort[0];
			String port = IPandPort[1];
			if(isValidIP(ip)&&isValidPort(port)) {//����ǲ�����ȷip
				IpAndPort.setIp(ip);
				IpAndPort.setPort(Integer.parseInt(port));
				return true;
			}return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	} 
	
	/**
	 * ��ý������ip
	 * @return
	 */
	public static String getIP() {
		return IpAndPort.getIp();
	}
	
	/**
	 * ��ý������port
	 * @return
	 */
	public static int getPORT() {
		return IpAndPort.getPort();
	}
	
	/**
	 * ��ȡ����ip��Ԥ��ú����ڸ������绷���У���������������wifi���������ߣ������ܻ��������
	 * @return
	 */
	public static String getMyIp() {//
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {}
		return "127.0.0.1";
	}
	/**
	 * �ж��ַ���ip�Ƿ���һ����Ч��ip
	 * @param ip
	 * @return
	 */
	public static boolean isValidIP(String ip) {
		Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher matcher = pattern.matcher(ip); 
		return matcher.matches();
	}
	
	/**
	 * �ж��ַ���port�Ƿ���һ����Ч��port
	 * @param port
	 * @return
	 */
	public static boolean isValidPort(String port) {
		try {
			int IntPort = Integer.parseInt(port);
			return (IntPort>1&&IntPort<65535);
		}
		catch(NumberFormatException ne) {
			return false;
		}
	}
	/**
	 * ���Զ˿��Ƿ����
	 * @param port
	 * @return
	 */
	public static boolean TestPort(int port) {
		try {
			ServerSocket socket = new ServerSocket(port);
			socket.close();
			return true;
		}catch (Exception e) {
			return false;
		}
	}
	/**
	 * ����String���͵�port�Ƿ���һ���˿ں��ҿ���ʹ�ã�δ��ռ�ã�
	 * @param port
	 * @return
	 */
	public static boolean TestPortIfCanUse(String port) {
		return StarToolkit.isValidPort(port)&&TestPort(Integer.parseInt(port));
	}
	/**
	 * �ǿ�������ɺͽ������򣬴���һ��ip:port���͵��ַ����������ǿ��������м򵥵ļ��ܻ��߽���
	 * @param ipANDport
	 * @return
	 */
	public static String CodeIPandPort(String ipANDport) {
		opchars = (new StringBuffer(ipANDport)).reverse().toString().toCharArray();
		ExchangeChar('0','!');
		ExchangeChar('1','#');
		ExchangeChar('2','@');
		ExchangeChar('3','s');
		ExchangeChar('4','%');
		ExchangeChar('5','w');
		ExchangeChar('6','x');
		ExchangeChar('7','?');
		ExchangeChar('8','d');
		ExchangeChar('9','p');
		ExchangeChar(':','c');
		ExchangeChar('.','J');
		return new String(opchars);
	}
	
	/**
	 * ΪCodeIPandPort�����ṩʹ�õĺ���������Ϊ���������ַ���λ��
	 * @param char1
	 * @param char2
	 */
	static void ExchangeChar(char char1,char char2) {
		if(opchars==null)return;
		for(int i = 0;i<opchars.length;i++) {
			if(opchars[i]==char1) {
				opchars[i] = char2;
			}else if(opchars[i]==char2) {
				opchars[i] = char1;
			}
		}
	}
	/**
	 * ΪtextField���һ�����������޶����ı�������Ϊ����󳤶�ΪMaxLength�Ĵ�����
	 * @param textField
	 * @param MaxLength
	 */
	public static void addNumLimitToTextJField(JTextField textField,int MaxLength) {
		textField.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char key = e.getKeyChar();
				if(!(key>=KeyEvent.VK_0&&key<=KeyEvent.VK_9)) {
					//ֻ�������������ɾ��
					e.setKeyChar('\0');
				}if(textField.getText().length()>(MaxLength-1)&&key!='\b') {
					//�������3���Ͳ�������
					e.setKeyChar('\0');
				}
			}
		});
	}
	
}
