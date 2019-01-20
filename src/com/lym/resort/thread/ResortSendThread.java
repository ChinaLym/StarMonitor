package com.lym.resort.thread;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import javax.imageio.ImageIO;
/**
 * 该线程为 持续向客户机发送本机屏幕图片的线程
 * @author Administrator
 *
 */
public class ResortSendThread extends Thread {
	//可否控制
	private volatile static int PermissionLevel;

	private  Socket server;
	DataOutputStream dataOut;
	//初始化函数

	@SuppressWarnings("unused")
	public ResortSendThread(Socket sever,int PermissionLevel) {
		this.server = sever;
		ResortSendThread.PermissionLevel = PermissionLevel;
		OutputStream os = null;
		try {
			// 获得socket的输入输出流 进行包装
			os = server.getOutputStream();
			dataOut = new DataOutputStream(os);
		} catch (IOException e) {//获取失败则关闭两个流
			e.printStackTrace();
			System.out.println("获取输出流失败");//关闭两个流释放资源
			if(os!=null)
				try {
					os.close();
				} catch (IOException e1) {
				}
			if(dataOut!=null) {
				try {
					dataOut.close();
				} catch (Exception e2) {
				}
			}
				
		}
	}

	@Override
	public void run() {
		showImage();
	}

	
	public void showImage() {
		try {
			// 进行捕屏幕,获得一个工具
			Toolkit tookit = Toolkit.getDefaultToolkit();
			// 获得屏幕的大小
			Dimension dimension = tookit.getScreenSize();

			// 传送屏幕的宽度、高度
			dataOut.writeInt(dimension.width);
			dataOut.writeInt(dimension.height);
			dataOut.writeInt(PermissionLevel);
			// 新建一个坐标空间 ,起始点的坐标为（0,0）
			Rectangle rec = new Rectangle(dimension);

			// 需要不断的截图，发给服务端
			while (true) {
				// 获得屏幕字节数组
				byte[] imgByte = getImageBuffer(rec);
				dataOut.writeInt(imgByte.length);// 将图片数组的长度发送给客户端
				dataOut.write(imgByte);// 将图片字节数组发送给客户端
				dataOut.flush();// 刷新输出流的缓冲区
				Thread.sleep(50);
			}
		}catch (IOException e) {
			//输出流异常，结束本次控制
			return;
		}
		catch (Exception e) {
			// 处理日志消息
		}
	}

	// 将图片转换成字节数组
	private static byte[] getImageBuffer(Rectangle rec) throws Exception {
		// 创建一个机器人
		Robot robot = new Robot();
		// 截取屏幕
		BufferedImage bufImg = robot.createScreenCapture(rec);//默认24色
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();//默认32
		ImageIO.write(bufImg, "JPG", byteOut);
		return byteOut.toByteArray();
	}

}
