package com.lym.resort;
import java.net.ServerSocket;
import java.net.Socket;

import com.lym.UI.StarComponents.StarLoading;
import com.lym.UI.StarComponents.StarOptionFrame;
import com.lym.obj.ResortServiceSettings;
import com.lym.resort.thread.ResortReceiveThread;
import com.lym.resort.thread.ResortSendThread;

/**
 * 远程监控服务器,服务器端代码需要重新设计架构，UI、逻辑、通信层 需要分离，降低耦合
 * @author Administrator
 *
 */
public class Resort {
	
	ServerSocket socket;
	ResortServiceSettings ConnectRule;
	/**
	 * 初始化函数
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
			new StarOptionFrame("端口被占用啦，需要重新设置",StarOptionFrame.WarnMessage).setVisible(true);
			return;//结束该进程
		}
		//加载界面，需要降低耦合********************************需要完善
		StarLoading LoadingGif = new StarLoading();//等待时播放GIF
		LoadingGif.setTimeout(-1);
		LoadingGif.show();
		try {
			//Class.forName("com.lym.resort.thread.ResortReceiveThread");
		
			if(socket == null )System.out.println("socket Null Error");
			//不断等待客户端的链接
			while(true){
				Socket server = socket.accept();//等待对方加入
				if(LoadingGif!=null) {
					LoadingGif.stop();//当地一个连接上之后则停止播放动画
				}
				//printThreadsInfo();//检测线程的个数 test
				if(ConnectRule.isAllow(server.getInetAddress())){//如果该ip地址通过了设置的ip过滤则对其进行互动
					new ResortSendThread(server,ConnectRule.getPermissionLevel()).start();
					new ResortReceiveThread(server,ConnectRule.getPermissionLevel()).start();
				}else {
					try{//为了保证服务器的稳定运行，该tryCatch只能在循环内部，但由于其不是互动线程，不影响使用
						server.close();//否则拒绝该连接
					}catch(Exception e) {}
				}
			}
		} catch (Exception e1) {
		}
	}

		
	/**
	 * 该函数仅用于性能测试
	 * 检测线程释放是否正确  检测关闭及时释放资源
	 */
	public static void printThreadsInfo(){
		 ThreadGroup currentGroup = 
			      Thread.currentThread().getThreadGroup();
			      int noThreads = currentGroup.activeCount();
			      Thread[] lstThreads = new Thread[noThreads];
			      currentGroup.enumerate(lstThreads);
			      for (int i = 0; i < noThreads; i++)
			      System.out.println("线程号：" + i + " = " + lstThreads[i].getName());
	}
	{}
}
