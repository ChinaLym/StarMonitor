package com.lym.obj;

import java.net.InetAddress;
import java.util.ArrayList;

public class ResortServiceSettings {
	//连接过滤ip的方式：不进行过滤(默认)，过滤黑名单，只允许白名单
	public static final int CONNECTTYPE_DEFAULT = 0;
	public static final int CONNECTTYPE_ALLOWALL = 0;
	public static final int CONNECTTYPE_BANBLACKIP = 1;
	public static final int CONNECTTYPE_ONLYALLOWWHITE = 2;
	//刚开始连接时候的控制权限
	public static final int PERMISSIONLEVEL_DEFAULT = 0;
	public static final int PERMISSIONLEVEL_WATCHONLY = 0;
	public static final int PERMISSIONLEVEL_WATCH_AND_OPERATION = 1;
	public static final int PERMISSIONLEVEL_ALL = 2;
	
	
	//开启服务的的设置参数
	int PORT = 9999;//默认使用9999端口
	int ConnectType = CONNECTTYPE_DEFAULT;//是否过滤IP
	ArrayList<String> BlackList = null;//黑名单
	ArrayList<String> WhiteList = null;//白名单
	int PermissionLevel = PERMISSIONLEVEL_WATCHONLY;//权限等级
	
	
	/**
	 * 几个初始化函数
	 * @param PORT 				端口号
	 * @param ConnectType		过滤ip的方式
	 * @param list				黑白名单
	 * @param PermissionLevel	设置对方权限级别
	 */
	public ResortServiceSettings(int PORT,int ConnectType,ArrayList<String> list,int PermissionLevel){
		this(PORT, ConnectType, list);		
		this.PermissionLevel = PermissionLevel;
	}
	public ResortServiceSettings(int PORT,int PermissionLevel){
		this.PORT = PORT;
		this.PermissionLevel = PermissionLevel;
	}
	public ResortServiceSettings() {}
	public ResortServiceSettings(int PORT){
		this.PORT = PORT;
	}
	public ResortServiceSettings(int PORT,int ConnectType,ArrayList<String> list){
		this.PORT = PORT;
		switch (ConnectType) {
		case CONNECTTYPE_BANBLACKIP://使用黑名单
			BlackList = list;
			break;
		case CONNECTTYPE_ONLYALLOWWHITE://使用白名单
			WhiteList = list;
			break;
		default:
			break;
		}
	}

	
	public int getPORT() {
		return PORT;
	}
	public void setPORT(int pORT) {
		PORT = pORT;
	}
	public int getConnectType() {
		return ConnectType;
	}
	public void setConnectType(int connectType) {
		ConnectType = connectType;
	}
	public ArrayList<String> getBlackList() {
		return BlackList;
	}
	public void setBlackList(ArrayList<String> blackList) {
		BlackList = blackList;
	}
	public ArrayList<String> getWhiteList() {
		return WhiteList;
	}
	public void setWhiteList(ArrayList<String> whiteList) {
		WhiteList = whiteList;
	}
	public int getPermissionLevel() {
		return PermissionLevel;
	}
	public void setPermissionLevel(int permissionLevel) {
		PermissionLevel = permissionLevel;
	}

	/**
	 * 接收一个IP地址，根据本设置缓存中的设置，返回是否允许该Ip
	 * @param inetAddress
	 * @return
	 */
	public boolean isAllow(InetAddress inetAddress) {
		String ip = null;					//声明但不加载，使用懒加载
		switch (CONNECTTYPE_DEFAULT) {
		case 0:
			return true;//不设置ip过滤
			
		case CONNECTTYPE_BANBLACKIP:		//如果设置了黑名单
			if(BlackList == null)return true;//如果黑名单列表为空，则全都为允许
			if(inetAddress==null)return false;//非空校验
			ip = inetAddress.toString();
			for (String banip : BlackList) {
				if(banip.equals(ip))return false;//如果在黑名单中，返回false
			}
			return true;//否则返回true
			
		case CONNECTTYPE_ONLYALLOWWHITE:		//设置了白名单
			if(WhiteList == null)return false;//如果白名单列表为空，则全部拒绝
			if(inetAddress==null)return false;//非空校验
			ip = inetAddress.toString();
			for (String banip : WhiteList) {
				if(banip.equals(ip))return true;//如果在白名单中，返回true
			}
			return false;//否则返回false
			
		default:
			return true;//权限设置错误,使用默认过滤方式，全部允许
		}
	}
}
