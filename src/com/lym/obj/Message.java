package com.lym.obj;

import java.io.Serializable;

public class Message implements Serializable{
	private static final long serialVersionUID = 1L;
	String message = null;
	int type = 0;
	public String getMessage() {
		return message;
	}

	public int getType() {
		return type;
	}

	public void setMessage(String message) {
		this.message = message;
		type = 1;
	}
	public Message(){
		
	}
	public Message(Object o){
		this.message = o.toString();
		if(o!=null&&o.getClass().getName().contains("Message")) {
			type =1;
		}
	}
	public Message(String s){
		message=s;
		type =1;
	}

	public String toString() {
		return "Message [" + message + "]";
	}
	
}