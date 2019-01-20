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
	final static String TIPWORDS = "我在星控远程协助中开启了一个星控求助请求，快来帮我一下吧！复制这条消息，打开星控点击右边协助他人就可以来控制我的电脑啦~"; 
	final static String  COMMANDDELIMITER = "~$@%";
	final static String  REGCOMMANDDELIMITER = "\\~\\$\\@\\%";
	static char[] opchars= null;
	
	public static IPandPort IpAndPort = new IPandPort();
	
	/**
	 * 传入要创建窗口的大小， 返回该窗口要居中显示时窗口左上角应该在的位置
	 * @param width
	 * @param height
	 * @return
	 */
	public static Point getCenterPoint(int width, int height) {
		//先获取屏幕的宽高
		Dimension   screensize   =   Toolkit.getDefaultToolkit().getScreenSize();
		int ScreenWidth = (int)screensize.getWidth();
		int ScreenHeight = (int)screensize.getHeight();
		
		return new Point((ScreenWidth-width)/2, (ScreenHeight-height)/2);
	}
	/**
	 * 设置jframe在屏幕中居中显示
	 * @param jframe
	 */
	public static void setJFrameOnCenter(JFrame jframe) {
		jframe.setLocation(getCenterPoint(jframe.getWidth(),jframe.getHeight()));
	}
	/**
	 * 设置jframe在屏幕中居中显示,并指定其宽高
	 * @param jframe
	 */
	public static void setJFrameOnCenter(JFrame jframe,int width,int height) {
		jframe.setBounds(new Rectangle(getCenterPoint(width,height),new Dimension(width, height)));
	}


	
	/**
	 * 复制字符串s到剪贴板中
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
	 * 传入一个ip和port，根据此创建星口令并返回
	 * @param ip
	 * @param port
	 * @return
	 */
	public static String createStarCommand(String ip,int port) {
		return TIPWORDS + COMMANDDELIMITER + CodeIPandPort(ip+":"+port);
	}
	
	/**
	 * 传入一个ip和port，根据此创建星口令，并将该星口令复制到剪贴板中
	 * @param ip
	 * @param port
	 * @return
	 */
	public static boolean createStarCommandToClipboard(String ip,int port) {
		return CopyStringToClipboard(createStarCommand(ip,port));
	}
	
	/**
	 * 解析贴板中的内容，是否是一个星口令，如果是返回true
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
	 * 传入一个星口令，解析该星口令，如果解析成功返回true,并将其ip和port放入IpAndPort容器中
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
			if(isValidIP(ip)&&isValidPort(port)) {//检测是不是正确ip
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
	 * 获得解析后的ip
	 * @return
	 */
	public static String getIP() {
		return IpAndPort.getIp();
	}
	
	/**
	 * 获得解析后的port
	 * @return
	 */
	public static int getPORT() {
		return IpAndPort.getPort();
	}
	
	/**
	 * 获取本机ip，预测该函数在复杂网络环境中（如多个网卡，连着wifi还连着网线），可能会出现问题
	 * @return
	 */
	public static String getMyIp() {//
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {}
		return "127.0.0.1";
	}
	/**
	 * 判断字符串ip是否是一个有效的ip
	 * @param ip
	 * @return
	 */
	public static boolean isValidIP(String ip) {
		Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher matcher = pattern.matcher(ip); 
		return matcher.matches();
	}
	
	/**
	 * 判断字符串port是否是一个有效的port
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
	 * 测试端口是否可用
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
	 * 测试String类型的port是否是一个端口号且可以使用（未被占用）
	 * @param port
	 * @return
	 */
	public static boolean TestPortIfCanUse(String port) {
		return StarToolkit.isValidPort(port)&&TestPort(Integer.parseInt(port));
	}
	/**
	 * 星口令的生成和解析规则，传入一个ip:port类型的字符串，或者星口令，对其进行简单的加密或者解密
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
	 * 为CodeIPandPort函数提供使用的函数，功能为交换两个字符的位置
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
	 * 为textField添加一个监听器，限定该文本框内容为：最大长度为MaxLength的纯数字
	 * @param textField
	 * @param MaxLength
	 */
	public static void addNumLimitToTextJField(JTextField textField,int MaxLength) {
		textField.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char key = e.getKeyChar();
				if(!(key>=KeyEvent.VK_0&&key<=KeyEvent.VK_9)) {
					//只允许输入数组和删除
					e.setKeyChar('\0');
				}if(textField.getText().length()>(MaxLength-1)&&key!='\b') {
					//如果超过3个就不能输入
					e.setKeyChar('\0');
				}
			}
		});
	}
	
}
