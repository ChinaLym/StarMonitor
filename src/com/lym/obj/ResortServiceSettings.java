package com.lym.obj;

import java.net.InetAddress;
import java.util.ArrayList;

public class ResortServiceSettings {
	//���ӹ���ip�ķ�ʽ�������й���(Ĭ��)�����˺�������ֻ���������
	public static final int CONNECTTYPE_DEFAULT = 0;
	public static final int CONNECTTYPE_ALLOWALL = 0;
	public static final int CONNECTTYPE_BANBLACKIP = 1;
	public static final int CONNECTTYPE_ONLYALLOWWHITE = 2;
	//�տ�ʼ����ʱ��Ŀ���Ȩ��
	public static final int PERMISSIONLEVEL_DEFAULT = 0;
	public static final int PERMISSIONLEVEL_WATCHONLY = 0;
	public static final int PERMISSIONLEVEL_WATCH_AND_OPERATION = 1;
	public static final int PERMISSIONLEVEL_ALL = 2;
	
	
	//��������ĵ����ò���
	int PORT = 9999;//Ĭ��ʹ��9999�˿�
	int ConnectType = CONNECTTYPE_DEFAULT;//�Ƿ����IP
	ArrayList<String> BlackList = null;//������
	ArrayList<String> WhiteList = null;//������
	int PermissionLevel = PERMISSIONLEVEL_WATCHONLY;//Ȩ�޵ȼ�
	
	
	/**
	 * ������ʼ������
	 * @param PORT 				�˿ں�
	 * @param ConnectType		����ip�ķ�ʽ
	 * @param list				�ڰ�����
	 * @param PermissionLevel	���öԷ�Ȩ�޼���
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
		case CONNECTTYPE_BANBLACKIP://ʹ�ú�����
			BlackList = list;
			break;
		case CONNECTTYPE_ONLYALLOWWHITE://ʹ�ð�����
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
	 * ����һ��IP��ַ�����ݱ����û����е����ã������Ƿ������Ip
	 * @param inetAddress
	 * @return
	 */
	public boolean isAllow(InetAddress inetAddress) {
		String ip = null;					//�����������أ�ʹ��������
		switch (CONNECTTYPE_DEFAULT) {
		case 0:
			return true;//������ip����
			
		case CONNECTTYPE_BANBLACKIP:		//��������˺�����
			if(BlackList == null)return true;//����������б�Ϊ�գ���ȫ��Ϊ����
			if(inetAddress==null)return false;//�ǿ�У��
			ip = inetAddress.toString();
			for (String banip : BlackList) {
				if(banip.equals(ip))return false;//����ں������У�����false
			}
			return true;//���򷵻�true
			
		case CONNECTTYPE_ONLYALLOWWHITE:		//�����˰�����
			if(WhiteList == null)return false;//����������б�Ϊ�գ���ȫ���ܾ�
			if(inetAddress==null)return false;//�ǿ�У��
			ip = inetAddress.toString();
			for (String banip : WhiteList) {
				if(banip.equals(ip))return true;//����ڰ������У�����true
			}
			return false;//���򷵻�false
			
		default:
			return true;//Ȩ�����ô���,ʹ��Ĭ�Ϲ��˷�ʽ��ȫ������
		}
	}
}
