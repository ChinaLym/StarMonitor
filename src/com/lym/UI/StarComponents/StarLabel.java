package com.lym.UI.StarComponents;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;

public class StarLabel extends JLabel {
	private static final long serialVersionUID = 1L;
	
	boolean isSelect = false;
	String Text;
	static final Color trueFontColor = Color.WHITE;
	static final Color falseFontColor = Color.GRAY;
	
	
	public StarLabel(String text){
		Text = text;
		this.setText(text);
		this.setForeground(falseFontColor);
		this.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 24));
	}
	
	public boolean isSelect() {
		return isSelect;
	}
	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
		SelectChange();
	}
	private void SelectChange() {
		if(isSelect) {
			StarLabel.this.setForeground(trueFontColor);
		}
		else {
			StarLabel.this.setForeground(falseFontColor);
		}
	}
}
