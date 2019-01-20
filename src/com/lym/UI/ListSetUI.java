package com.lym.UI;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.lym.UI.StarComponents.StarOptionFrame;
import com.lym.Util.StarToolkit;
import com.lym.obj.ResortServiceSettings;

import javax.swing.JList;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.event.ActionEvent;

public class ListSetUI {

	//组件
	private JFrame frame;
	private JTextField ipText1;
	private JTextField ipText2;
	private JTextField ipText3;
	private JTextField ipText4;
	private JButton SaveButton;
	private JButton CancelButton;
	private JButton DeleteButton;
	private JButton ModifyButton;
	private JButton AddButton;
	private JList<String> JList_ip;
	
	JFrame parentFrame;
	ResortServiceSettings Settings;
	/**
	 * 默认为单进程，若更改代码改为多进程时关于数组操作需要加锁
	 */
	String[] ipList;
	int maxLength = 100;//默认最多有100个ip
	int IpCount = 0;

	public ListSetUI(JFrame parentFrame,ResortServiceSettings Settings) {
		this.parentFrame = parentFrame;
		if(this.parentFrame!=null) {
			this.parentFrame.setVisible(false);
		}
		if(Settings==null)Settings = new ResortServiceSettings();
		this.Settings = Settings;
		initialize();
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 420, 290);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		frame.setUndecorated(true);//去掉边框
		frame.setBackground(new Color(0,0,0,200));
		frame.setAlwaysOnTop(true);
		StarToolkit.setJFrameOnCenter(frame);
		frame.setType(JFrame.Type.UTILITY);//不将该jframe在任务栏上展示
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(5, 5, 300, 170);
		frame.getContentPane().add(scrollPane);
		
		ipList = new String[maxLength];
		initIpList();
		JList_ip = new JList<String>(getArray());
		JList_ip.setFont(new Font("微软雅黑", Font.PLAIN, 30));
		JList_ip.setFixedCellHeight(40);
		scrollPane.setViewportView(JList_ip);
		
		
		ipText1 = new JTextField("192");
		ipText1.setBounds(15, 188, 50, 35);
		frame.getContentPane().add(ipText1);
		
		
		ipText2 = new JTextField("168");
		ipText2.setBounds(90, 188, 50, 35);
		frame.getContentPane().add(ipText2);
		
		ipText3 = new JTextField("8");
		ipText3.setBounds(165, 188, 50, 35);
		frame.getContentPane().add(ipText3);
		
		ipText4 = new JTextField("");
		ipText4.setBounds(240, 188, 50, 35);
		frame.getContentPane().add(ipText4);
		
		SetNumberOnly(3,ipText1,ipText2,ipText3,ipText4);
		
		//编辑
		JButton EditButton = new JButton("\u7F16\u8F91");
		EditButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getIpFromList();
			}
		});
		EditButton.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		EditButton.setBounds(320, 10, 90, 40);
		frame.getContentPane().add(EditButton);
		
		//删除
		DeleteButton = new JButton("\u5220\u9664");
		DeleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteIp();
				JList_ip.setListData(getArray());
				JList_ip.repaint();
			}
		});
		DeleteButton.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		DeleteButton.setBounds(320, 70, 90, 40);
		frame.getContentPane().add(DeleteButton);
		
		//更新
		ModifyButton = new JButton("\u66F4\u65B0");
		ModifyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modify();
				JList_ip.setListData(getArray());
				JList_ip.repaint();
			}
		});
		ModifyButton.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		ModifyButton.setBounds(320, 130, 90, 40);
		frame.getContentPane().add(ModifyButton);
		
		//新增
		AddButton = new JButton("\u65B0\u589E");
		AddButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String newIp = getInputIp();
				if(hasValue(newIp)){
					new StarOptionFrame("ip已存在",StarOptionFrame.WarnMessage).setVisible(true);
					return;
				}
				if(newIp!=null) {
					ipList[IpCount ++] = newIp;
					JList_ip.setListData(getArray());
					JList_ip.repaint();
				}else {
					new StarOptionFrame("ip地址不正确",StarOptionFrame.ErrorMessage).setVisible(true);

				}
			}
		});
		AddButton.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		AddButton.setBounds(320, 190, 90, 40);
		frame.getContentPane().add(AddButton);
		

		//保存
		SaveButton = new JButton("\u4FDD\u5B58");
		SaveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(updateList()) {
					parentFrame.setVisible(true);
					frame.dispose();
				}else {
					new StarOptionFrame("保存出错请重试", StarOptionFrame.ErrorMessage).setVisible(true);
				}
				
			}
		});
		SaveButton.setFont(new Font("微软雅黑", Font.BOLD, 22));
		SaveButton.setBounds(95, 237, 90, 40);
		frame.getContentPane().add(SaveButton);
		
		//取消
		CancelButton = new JButton("\u53D6\u6D88");
		CancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parentFrame.setVisible(true);
				frame.dispose();
			}
		});
		CancelButton.setFont(new Font("微软雅黑", Font.BOLD, 22));
		CancelButton.setBounds(215, 237, 90, 40);
		frame.getContentPane().add(CancelButton);
		
		frame.setVisible(true);
		
		
	}
	private boolean hasValue(String ip) {
		for(int i = 0; i < IpCount ;i++) {
			if(ipList[i].equals(ip))
				return true;
		}
		return false;
	}
	private String[] getArray() {
		return getArray(IpCount);
	}
	private String[] getArray(int length) {
		if(length>IpCount) {//避免下标越界
			length=IpCount;
		}
		String[] list = new String[length];
		for(int i = 0;i<length;i++) {
			list[i] = ipList[i];
		}
		return list;
	}
	private boolean modify() {
		String ip = getInputIp();
		if(hasValue(ip)){
			return false;
		}
		if(ip != null) {
			int[] selects = JList_ip.getSelectedIndices();
			if(selects.length!=1) {
				return false;
			}ipList[selects[0]] = ip;
		}
			
		return false;
	}
	private boolean deleteIp() {
		int[] selects = JList_ip.getSelectedIndices();
		if(selects.length<1)return false;
		int correct = 0;
		for (int i : selects) {
			removeIpFromArray(i - correct);
			correct ++;
		}
		return true;
	}
	private boolean removeIpFromArray(int index) {
		if(index>=IpCount) {
			throw new IndexOutOfBoundsException();
		}
		for(int i = index;i<IpCount-1;i++) {
			ipList[i] = ipList[i+1];
		}
		IpCount--;
		return true;
	}
	private boolean getIpFromList() {
		int[] selects = JList_ip.getSelectedIndices();
		if(selects.length!=1) {
			return false;
		}
		String ip = JList_ip.getSelectedValue();
		String[] ips = ip.split("\\.");
		ipText1.setText(ips[0]);
		ipText2.setText(ips[1]);
		ipText3.setText(ips[2]);
		ipText4.setText(ips[3]);
		return true;
		
	}
	private String getInputIp() {
		String newIp =  ipText1.getText() + "." + ipText2.getText() + "." + ipText3.getText() + "." + ipText4.getText();
		if(StarToolkit.isValidIP(newIp)) {
			return newIp;
		}else{
			return null;
		}
	}
	//对4个ip文字框统一设置
	private void SetNumberOnly(int maxLength,JTextField...jTextField) {
		for (JTextField tf : jTextField) {
			StarToolkit.addNumLimitToTextJField(tf, maxLength);
			tf.setFont(new Font("黑体", Font.BOLD, 20));
			tf.setHorizontalAlignment(JTextField.CENTER);
		}
	}	
	private  boolean updateList() {
		try {
			ArrayList<String> list = new ArrayList<String>(Arrays.asList(getArray()));
			if(Settings.getConnectType() == ResortServiceSettings.CONNECTTYPE_BANBLACKIP) {
				Settings.setBlackList(list);
			}else if(Settings.getConnectType() == ResortServiceSettings.CONNECTTYPE_ONLYALLOWWHITE){
				Settings.setWhiteList(list);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	private void initIpList() {
		String[] oldips = null;
		if(Settings == null||ipList ==null)return ;
		if(Settings.getConnectType() == ResortServiceSettings.CONNECTTYPE_BANBLACKIP) {
			frame.setTitle("黑名单");
			if(Settings.getBlackList()==null)return;
			oldips = new String[Settings.getBlackList().size()];
			
		}else if(Settings.getConnectType() == ResortServiceSettings.CONNECTTYPE_ONLYALLOWWHITE){
			frame.setTitle("白名单");
			if(Settings.getWhiteList()==null)return;
			oldips = new String[Settings.getWhiteList().size()];
		}
		copyOldIps(oldips);
	}

	private void copyOldIps(String[] oldips) {
		if(oldips == null)return ;
		oldips = Settings.getBlackList().toArray(oldips);
		if(oldips.length>0) {
			IpCount += oldips.length;
			for(int i = 0; i<IpCount;i++) {
				ipList[i] = oldips[i];
			}
		}
	}
}
