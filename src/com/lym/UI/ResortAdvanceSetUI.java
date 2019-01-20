package com.lym.UI;

import java.awt.Font;
import java.awt.Point;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

import javax.swing.border.TitledBorder;

import com.lym.UI.StarComponents.StarLabel;
import com.lym.UI.StarComponents.StarOptionFrame;
import com.lym.Util.StarToolkit;
import com.lym.obj.ResortServiceSettings;

import javax.swing.UIManager;
/**
 * 为开启服务前 参数设置的模态UI
 * @author Administrator
 *
 */
public class ResortAdvanceSetUI {

	private JFrame frame;
	private ResortSetUI parentFrame;
	private JTextField PortTextField;
	
	private StarLabel NoFilterRadioLabel;
	private StarLabel UseWhiteListRadioButton;
	private StarLabel BlackRadioButton;
	private JButton SetBlackListButton;
	private JButton SetWhiteListButton;
	
	private StarLabel OnlyWatchRadioButton;
	private StarLabel AllowWatchAndControlRadioButton;
	private StarLabel AllowAllRadioButton;
	
	//保存设置的对象
	ResortServiceSettings Settings;
	
	int initWidth = 430;
	int initHeight = 520;
	private Point OldLocation = new Point(650,300);

	
	private void setFramePosition(MouseEvent m){
		Point p = frame.getLocation();
		frame.setLocation(
		        p.x + (m.getX() - OldLocation.x), 
		        p.y + (m.getY() - OldLocation.y)); 
	}
	

/*	public ResortAdvanceSetUI(ResortServiceSettings OldSettings) {
		this.OldSettings = OldSettings;
		initialize();
		this.frame.setVisible(true);
	}*/
	public ResortAdvanceSetUI(ResortSetUI parentframe,ResortServiceSettings Settings) {
		this.parentFrame = parentframe;
		this.Settings = Settings;
		initialize();
		loadSetting();
		this.frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame();
		parentFrame.setEnabled(false);
				//new JDialog(parentFrame,true);
		frame.setResizable(false);
		frame.setUndecorated(true);//去掉边框
		//放置于屏幕中间
		OldLocation = StarToolkit.getCenterPoint(initWidth, initHeight);
		frame.setBounds(OldLocation.x, OldLocation.y, initWidth, initHeight);
		frame.setBackground(new Color(0,0,0,0));
		frame.setType(JFrame.Type.UTILITY);//不将该jframe在任务栏上展示
		frame.setAlwaysOnTop(true);

		
		if(Settings == null)
			Settings = new ResortServiceSettings(9999);
		
		
		JPanel BackgroudPanel = new JPanel();
		BackgroudPanel.setToolTipText("\u8FC7\u6EE4\u6389\u6211\u4E0D\u60F3\u8FDE\u63A5\u7684ip");
		frame.getContentPane().add(BackgroudPanel, BorderLayout.CENTER);
		BackgroudPanel.setBackground(new Color(0,0,0,200));
		BackgroudPanel.setLayout(null);
		BackgroudPanel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent m) {
				OldLocation.x = m.getX();
				OldLocation.y = m.getY();
			}
			public void mouseReleased(MouseEvent m) {
				setFramePosition(m);
			}
			
		});
		//Title
		JLabel TitleLabel = new JLabel("\u9AD8\u7EA7\u8BBE\u7F6E");
		TitleLabel.setFont(new Font("黑体", Font.BOLD, 20));
		TitleLabel.setForeground(Color.white);
		TitleLabel.setBounds(14, 13, 116, 44);
		BackgroudPanel.add(TitleLabel);
		
		JLabel PortLabel = new JLabel("\u7AEF\u53E3\u53F7");
		PortLabel.setForeground(Color.WHITE);
		PortLabel.setFont(new Font("黑体", Font.BOLD, 20));
		PortLabel.setBounds(39, 77, 91, 44);
		BackgroudPanel.add(PortLabel);
		
		PortTextField = new JTextField(Settings.getPORT() + "");
		PortTextField.setBounds(144, 79, 109, 44);
		PortTextField.setHorizontalAlignment(JTextField.CENTER);
		PortTextField.setFont(new Font("黑体", Font.BOLD, 24));
		StarToolkit.addNumLimitToTextJField(PortTextField,5);//为端口文本框添加数字限定器
		BackgroudPanel.add(PortTextField);
		PortTextField.setColumns(10);
		
		//端口测试
		JButton TestPortButton = new JButton("\u5360\u7528\u68C0\u6D4B");
		TestPortButton.setToolTipText("\u68C0\u6D4B\u8BE5\u7AEF\u53E3\u53F7\u662F\u5426\u88AB\u5360\u7528");
		TestPortButton.setFont(new Font("微软雅黑", Font.BOLD, 20));
		TestPortButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(StarToolkit.TestPortIfCanUse(PortTextField.getText())){
					new StarOptionFrame("该端口可用",StarOptionFrame.OKMessage).setVisible(true);
				}else new StarOptionFrame("该端口不可用",StarOptionFrame.ErrorMessage).setVisible(true);
			}
		});
		TestPortButton.setBounds(267, 77, 124, 44);
		BackgroudPanel.add(TestPortButton);
		
		JPanel FilterPanel = new JPanel();
		FilterPanel.setToolTipText("\u9009\u62E9ip\u8FC7\u6EE4\u65B9\u5F0F");
		FilterPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u8FC7 \u6EE4 IP", TitledBorder.LEADING, TitledBorder.TOP, new Font("微软雅黑", Font.PLAIN, 15), new Color(255, 255, 255)));
		FilterPanel.setBounds(14, 134, 404, 164);
		FilterPanel.setBackground(new Color(0,0,0,0));
		FilterPanel.setLayout(null);
		BackgroudPanel.add(FilterPanel);
		
		//设置黑名单
		SetBlackListButton = new JButton("\u8BBE\u7F6E\u9ED1\u540D\u5355");
		SetBlackListButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ListSetUI(frame,Settings);
			}
		});
		SetBlackListButton.setVisible(false);
		SetBlackListButton.setBounds(206, 58, 188, 40);
		FilterPanel.add(SetBlackListButton);
		SetBlackListButton.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		
		//设置白名单
		SetWhiteListButton = new JButton("\u8BBE\u7F6E\u767D\u540D\u5355");
		SetWhiteListButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ListSetUI(frame,Settings);
			}
		});
		SetWhiteListButton.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		SetWhiteListButton.setBounds(206, 111, 188, 40);
		SetWhiteListButton.setVisible(false);
		FilterPanel.add(SetWhiteListButton);
		
		
		NoFilterRadioLabel = new StarLabel("\u8FC7\u6EE4\u9ED1\u540D\u5355");
		NoFilterRadioLabel.setText("\u4E0D\u8FDB\u884C\u8FC7\u6EE4\uFF08\u9ED8\u8BA4\uFF09");
		NoFilterRadioLabel.setSelect(true);
		NoFilterRadioLabel.setBounds(14, 23, 216, 31);
		NoFilterRadioLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(!NoFilterRadioLabel.isSelect()) {
					NoFilterRadioLabel.setSelect(true);
					BlackRadioButton.setSelect(false);
					SetBlackListButton.setVisible(BlackRadioButton.isSelect());
					UseWhiteListRadioButton.setSelect(false);
					SetWhiteListButton.setVisible(UseWhiteListRadioButton.isSelect());
					frame.repaint();
				}
			}
		});
		FilterPanel.add(NoFilterRadioLabel);
		
		BlackRadioButton = new StarLabel("\u8FC7\u6EE4\u9ED1\u540D\u5355");
		BlackRadioButton.setSelect(false);
		BlackRadioButton.setBounds(14, 67, 170, 31);
		BlackRadioButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(!BlackRadioButton.isSelect()) {
					BlackRadioButton.setSelect(true);
					SetBlackListButton.setVisible(BlackRadioButton.isSelect());
					UseWhiteListRadioButton.setSelect(false);
					SetWhiteListButton.setVisible(UseWhiteListRadioButton.isSelect());
					NoFilterRadioLabel.setSelect(false);
					frame.repaint();
				}
			}
		});
		FilterPanel.add(BlackRadioButton);
		
		UseWhiteListRadioButton = new StarLabel("\u53EA\u5141\u8BB8\u767D\u540D\u5355");
		UseWhiteListRadioButton.setSelect(false);
		UseWhiteListRadioButton.setBounds(14, 115, 170, 31);
		UseWhiteListRadioButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(!UseWhiteListRadioButton.isSelect()) {
					UseWhiteListRadioButton.setSelect(true);
					SetWhiteListButton.setVisible(UseWhiteListRadioButton.isSelect());
					BlackRadioButton.setSelect(false);
					SetBlackListButton.setVisible(BlackRadioButton.isSelect());
					NoFilterRadioLabel.setSelect(false);
					frame.repaint();
				}
				
			}
		});
		FilterPanel.add(UseWhiteListRadioButton);
		
		JPanel SecurityPanel = new JPanel();
		SecurityPanel.setToolTipText("\u8BBE\u7F6E\u5EFA\u7ACB\u8FDE\u63A5\u540E\u5BF9\u65B9\u6700\u521D\u7684\u6743\u9650\uFF0C\u4E3A\u4E86\u60A8\u7684\u5B89\u5168\uFF0C\u63A8\u8350\u4F7F\u7528\u9ED8\u8BA4\u8BBE\u7F6E\uFF0C\u82E5\u60A8\u6709\u7279\u6B8A\u9700\u8981\uFF0C\u5EFA\u8BAE\u60A8\u5728\u8FDE\u63A5\u4E4B\u540E\u518D\u4FEE\u6539\u5BF9\u65B9\u6743\u9650\u4EE5\u4FDD\u969C\u60A8\u7684\u5B89\u5168");
		SecurityPanel.setBounds(14, 300, 404, 156);
		SecurityPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u5B89 \u5168", TitledBorder.LEADING, TitledBorder.TOP, new Font("微软雅黑", Font.PLAIN, 15), new Color(255, 255, 255)));
		SecurityPanel.setBackground(new Color(0,0,0,0));
		BackgroudPanel.add(SecurityPanel);
		SecurityPanel.setLayout(null);
		
		OnlyWatchRadioButton = new StarLabel("\u53EA\u5141\u8BB8\u67E5\u770B\u5C4F\u5E55");
		OnlyWatchRadioButton.setSelect(true);
		OnlyWatchRadioButton.setText("\u53EA\u5141\u8BB8\u67E5\u770B\u5C4F\u5E55\uFF08\u9ED8\u8BA4\uFF09");
		OnlyWatchRadioButton.setBounds(10, 26, 310, 27);
		OnlyWatchRadioButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(!OnlyWatchRadioButton.isSelect()) {
					OnlyWatchRadioButton.setSelect(true);
					AllowWatchAndControlRadioButton.setSelect(false);
					AllowAllRadioButton.setSelect(false);
					frame.repaint();
					}
			}
		});
		SecurityPanel.add(OnlyWatchRadioButton);
		
		AllowWatchAndControlRadioButton = new StarLabel("\u5141\u8BB8\u67E5\u770B\u5C4F\u5E55\u4E0E\u63A7\u5236\uFF08\u4E0D\u63A8\u8350\uFF09");
		AllowWatchAndControlRadioButton.setSelect(false);
		AllowWatchAndControlRadioButton.setBounds(10, 68, 354, 27);
		AllowWatchAndControlRadioButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(!AllowWatchAndControlRadioButton.isSelect()) {
					OnlyWatchRadioButton.setSelect(false);
					AllowWatchAndControlRadioButton.setSelect(true);
					AllowAllRadioButton.setSelect(false);
					frame.repaint();
					}
			}
		});
		SecurityPanel.add(AllowWatchAndControlRadioButton);
		
		AllowAllRadioButton = new StarLabel("\u5141\u8BB8\u67E5\u770B\u63A7\u5236\u548C\u5F00\u542F\u6444\u50CF\u5934\uFF08\u4E0D\u63A8\u8350\uFF09");
		AllowAllRadioButton.setText("\u5141\u8BB8\u76D1\u63A7\u548C\u5F00\u542F\u6444\u50CF\u5934\uFF08\u5DF2\u7981\u6B62\uFF09");
		AllowAllRadioButton.setSelect(false);
		AllowAllRadioButton.setBounds(10, 108, 368, 27);
		SecurityPanel.add(AllowAllRadioButton);
		
		JButton HelpButton = new JButton("\u5E2E\u52A9");
		HelpButton.setToolTipText("\u70B9\u51FB\u83B7\u53D6\u66F4\u591A\u5E2E\u52A9\u4FE1\u606F");
		HelpButton.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		HelpButton.setBounds(346, 13, 72, 31);
		BackgroudPanel.add(HelpButton);
		//确定按钮
		JButton ConfirmButton = new JButton("\u786E\u5B9A");
		ConfirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(saveSetting()) {
					closeFrame();
				}else {
					new StarOptionFrame("端口不可用,请尝试更换",StarOptionFrame.ErrorMessage).setVisible(true);
				}
			}
		});
		ConfirmButton.setFont(new Font("微软雅黑", Font.PLAIN, 22));
		ConfirmButton.setBounds(144, 469, 80, 38);
		BackgroudPanel.add(ConfirmButton);
		//取消按钮
		JButton CancelButton = new JButton("\u53D6\u6D88");
		CancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeFrame();
			}
		});
		CancelButton.setFont(new Font("微软雅黑", Font.PLAIN, 22));
		CancelButton.setBounds(239, 469, 80, 38);
		BackgroudPanel.add(CancelButton);
		//应用按钮
		JButton ApplyButton = new JButton("\u5E94\u7528");
		ApplyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(saveSetting()) {
					new StarOptionFrame("设置已保存！").setVisible(true);
				}else {
					new StarOptionFrame("端口不可用,请尝试更换",StarOptionFrame.ErrorMessage).setVisible(true);
				}
			}
		});
		ApplyButton.setFont(new Font("微软雅黑", Font.PLAIN, 22));
		ApplyButton.setBounds(333, 469, 80, 38);
		BackgroudPanel.add(ApplyButton);
		
		
	}

	void closeFrame() {
		parentFrame.setEnabled(true);
		frame.dispose();
		parentFrame.RefreshUI(Settings);
	}
	int getUI_PermissionLevel(){
		if(OnlyWatchRadioButton.isSelect()) {
			return ResortServiceSettings.PERMISSIONLEVEL_WATCHONLY;
		}else if(AllowWatchAndControlRadioButton.isSelect()){
			return ResortServiceSettings.PERMISSIONLEVEL_WATCH_AND_OPERATION;
		}else if(AllowAllRadioButton.isSelect()) {
			return ResortServiceSettings.PERMISSIONLEVEL_ALL;
		}
		return ResortServiceSettings.PERMISSIONLEVEL_DEFAULT;
	}
	int getUI_ConnectionType(){
		if(NoFilterRadioLabel.isSelect()) {
			return ResortServiceSettings.CONNECTTYPE_ALLOWALL;
		}else if(BlackRadioButton.isSelect()){
			return ResortServiceSettings.CONNECTTYPE_BANBLACKIP;
		}else if(UseWhiteListRadioButton.isSelect()) {
			return ResortServiceSettings.CONNECTTYPE_ONLYALLOWWHITE;
		}
		return ResortServiceSettings.CONNECTTYPE_DEFAULT;
	}
	boolean saveSetting() {
		String port = PortTextField.getText();
		if(!StarToolkit.TestPortIfCanUse(port)){
			return false;
		}
		Settings.setPORT(Integer.parseInt(port));
		Settings.setConnectType(getUI_ConnectionType());
		Settings.setPermissionLevel(getUI_PermissionLevel());
		return true;
	}
	/**
	 * 将传来的setting展示出来
	 */
	void loadSetting() {
		if(Settings == null)return ;
		PortTextField.setText(Settings.getPORT() + "");
		if(!StarToolkit.TestPortIfCanUse(PortTextField.getText())){//检测端口可用
			new StarOptionFrame("Tip:该端口目前不可用").setVisible(true);
		}
		
		
		{
			NoFilterRadioLabel.setSelect(false);
			
			BlackRadioButton.setSelect(false);
			SetBlackListButton.setVisible(false);
			
			UseWhiteListRadioButton.setSelect(false);
			SetWhiteListButton.setVisible(false);
			
		}
		switch (Settings.getConnectType()) {
		case (ResortServiceSettings.CONNECTTYPE_DEFAULT):
			NoFilterRadioLabel.setSelect(true);
			break;
		case (ResortServiceSettings.CONNECTTYPE_BANBLACKIP):
			BlackRadioButton.setSelect(true);
		SetBlackListButton.setVisible(true);
			break;
		case (ResortServiceSettings.CONNECTTYPE_ONLYALLOWWHITE):
			UseWhiteListRadioButton.setSelect(true);
			SetWhiteListButton.setVisible(true);
			break;
		
		default:
			NoFilterRadioLabel.setVisible(true);
			break;
		}
		//先全部置为未选择
		{
			OnlyWatchRadioButton.setSelect(false);
			AllowWatchAndControlRadioButton.setSelect(false);
			AllowAllRadioButton.setSelect(false);
			
		}
		switch (Settings.getPermissionLevel()) {
		case (ResortServiceSettings.PERMISSIONLEVEL_WATCHONLY):
			OnlyWatchRadioButton.setSelect(true);
			break;
		case (ResortServiceSettings.PERMISSIONLEVEL_WATCH_AND_OPERATION):
			AllowWatchAndControlRadioButton.setSelect(true);
			break;
		case (ResortServiceSettings.PERMISSIONLEVEL_ALL):
			AllowAllRadioButton.setSelect(true);
			break;
		
		default:
			OnlyWatchRadioButton.setSelect(true);
			break;
		}
		frame.repaint();
	}
}
