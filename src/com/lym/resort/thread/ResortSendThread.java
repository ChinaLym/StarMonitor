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
 * ���߳�Ϊ ������ͻ������ͱ�����ĻͼƬ���߳�
 * @author Administrator
 *
 */
public class ResortSendThread extends Thread {
	//�ɷ����
	private volatile static int PermissionLevel;

	private  Socket server;
	DataOutputStream dataOut;
	//��ʼ������

	@SuppressWarnings("unused")
	public ResortSendThread(Socket sever,int PermissionLevel) {
		this.server = sever;
		ResortSendThread.PermissionLevel = PermissionLevel;
		OutputStream os = null;
		try {
			// ���socket����������� ���а�װ
			os = server.getOutputStream();
			dataOut = new DataOutputStream(os);
		} catch (IOException e) {//��ȡʧ����ر�������
			e.printStackTrace();
			System.out.println("��ȡ�����ʧ��");//�ر��������ͷ���Դ
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
			// ���в���Ļ,���һ������
			Toolkit tookit = Toolkit.getDefaultToolkit();
			// �����Ļ�Ĵ�С
			Dimension dimension = tookit.getScreenSize();

			// ������Ļ�Ŀ�ȡ��߶�
			dataOut.writeInt(dimension.width);
			dataOut.writeInt(dimension.height);
			dataOut.writeInt(PermissionLevel);
			// �½�һ������ռ� ,��ʼ�������Ϊ��0,0��
			Rectangle rec = new Rectangle(dimension);

			// ��Ҫ���ϵĽ�ͼ�����������
			while (true) {
				// �����Ļ�ֽ�����
				byte[] imgByte = getImageBuffer(rec);
				dataOut.writeInt(imgByte.length);// ��ͼƬ����ĳ��ȷ��͸��ͻ���
				dataOut.write(imgByte);// ��ͼƬ�ֽ����鷢�͸��ͻ���
				dataOut.flush();// ˢ��������Ļ�����
				Thread.sleep(50);
			}
		}catch (IOException e) {
			//������쳣���������ο���
			return;
		}
		catch (Exception e) {
			// ������־��Ϣ
		}
	}

	// ��ͼƬת�����ֽ�����
	private static byte[] getImageBuffer(Rectangle rec) throws Exception {
		// ����һ��������
		Robot robot = new Robot();
		// ��ȡ��Ļ
		BufferedImage bufImg = robot.createScreenCapture(rec);//Ĭ��24ɫ
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();//Ĭ��32
		ImageIO.write(bufImg, "JPG", byteOut);
		return byteOut.toByteArray();
	}

}
