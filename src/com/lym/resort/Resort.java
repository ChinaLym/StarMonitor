package com.lym.resort;
import java.net.ServerSocket;
import java.net.Socket;

import com.lym.UI.StarComponents.StarLoading;
import com.lym.UI.StarComponents.StarOptionFrame;
import com.lym.obj.ResortServiceSettings;
import com.lym.resort.thread.ResortReceiveThread;
import com.lym.resort.thread.ResortSendThread;

/**
 * Զ�̼�ط�����,�������˴�����Ҫ������Ƽܹ���UI���߼���ͨ�Ų� ��Ҫ���룬�������
 * @author Administrator
 *
 */
public class Resort {
	
	ServerSocket socket;
	ResortServiceSettings ConnectRule;
	/**
	 * ��ʼ������
	 */
	public Resort(ResortServiceSettings connParaInfo) {
		this.ConnectRule = connParaInfo;
	}

	
	public void StartServer() {
		new Thread(new Runnable() {
			public void run() {
				Start();
			}
		}).start();
	}
	public void Start(){
		try {
			socket = new ServerSocket(ConnectRule.getPORT());
		}catch (Exception e) {
			new StarOptionFrame("�˿ڱ�ռ��������Ҫ��������",StarOptionFrame.WarnMessage).setVisible(true);
			return;//�����ý���
		}
		//���ؽ��棬��Ҫ�������********************************��Ҫ����
		StarLoading LoadingGif = new StarLoading();//�ȴ�ʱ����GIF
		LoadingGif.setTimeout(-1);
		LoadingGif.show();
		try {
			//Class.forName("com.lym.resort.thread.ResortReceiveThread");
		
			if(socket == null )System.out.println("socket Null Error");
			//���ϵȴ��ͻ��˵�����
			while(true){
				Socket server = socket.accept();//�ȴ��Է�����
				if(LoadingGif!=null) {
					LoadingGif.stop();//����һ��������֮����ֹͣ���Ŷ���
				}
				//printThreadsInfo();//����̵߳ĸ��� test
				if(ConnectRule.isAllow(server.getInetAddress())){//�����ip��ַͨ�������õ�ip�����������л���
					new ResortSendThread(server,ConnectRule.getPermissionLevel()).start();
					new ResortReceiveThread(server,ConnectRule.getPermissionLevel()).start();
				}else {
					try{//Ϊ�˱�֤���������ȶ����У���tryCatchֻ����ѭ���ڲ����������䲻�ǻ����̣߳���Ӱ��ʹ��
						server.close();//����ܾ�������
					}catch(Exception e) {}
				}
			}
		} catch (Exception e1) {
		}
	}

		
	/**
	 * �ú������������ܲ���
	 * ����߳��ͷ��Ƿ���ȷ  ���رռ�ʱ�ͷ���Դ
	 */
	public static void printThreadsInfo(){
		 ThreadGroup currentGroup = 
			      Thread.currentThread().getThreadGroup();
			      int noThreads = currentGroup.activeCount();
			      Thread[] lstThreads = new Thread[noThreads];
			      currentGroup.enumerate(lstThreads);
			      for (int i = 0; i < noThreads; i++)
			      System.out.println("�̺߳ţ�" + i + " = " + lstThreads[i].getName());
	}
	{}
}
