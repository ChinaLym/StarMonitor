package com.lym.obj;
/**
 * ���ڴ洢IP��port��һ����
 * @author Administrator
 *
 */
public class IPandPort{
	String ip = null;
	int port = 0;
	public IPandPort(String IP,int PORT) {
		ip = IP;
		port = PORT;
	}
	public IPandPort() {}
	public String toString() {
		return ip+":"+port;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
} 