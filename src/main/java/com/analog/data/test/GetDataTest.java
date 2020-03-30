package com.analog.data.test;


import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class GetDataTest {
	private int listenPort = 10009;
	public GetDataTest(int listenPort){
		this.listenPort = listenPort;
		StartReceiver();
	}
	public void StartReceiver(){
		new Thread(){
			public void run(){
				ServerSocket server = null;
				try {
					System.out.println("监听端口"+listenPort);
					server = new ServerSocket(listenPort);			
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				Socket socket = null;
				while(true){
					try {
						socket = server.accept();
					} catch (Exception e) {
						e.printStackTrace();
						try {
							socket.close();
						} catch (Exception e1) {
						}
						socket = null;
						continue;
					}				
					new Thread(new SocketThread(socket)).start();					
				}
				
				
				
			}
		}.start();
	}
	
	
	
	public class SocketThread implements Runnable{
		private Socket socket = null;
		
		public SocketThread(Socket socket){
			this.socket = socket;
		}
		
		
		 
		public void logConnect(){
			DataInputStream dis = null;				
			try {
				dis = new DataInputStream(socket.getInputStream());
				while(true){
					String dataString = dis.readUTF();
					System.out.println("10009端口的数据-receiveData:" + dataString);
				}
			} catch (Exception e) {					
				e.printStackTrace();					
			}finally{
				try {
					dis.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}			
				try {
					socket.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		public void run() {
			logConnect();			
		}
	}
	public static void main(String[] args) {
		GetDataTest ds = new GetDataTest(10009);
	}
	
	
}
