package duduf.udpclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UdpClient extends Thread {

	private InetAddress mServerIp;
	private int mPort;
	private DatagramSocket mSocket = null;
	
	public UdpClient (String ipAddress, int port){
		try {
			mServerIp = InetAddress.getByName(ipAddress);
			mPort = port;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		//TODO Authentication System
		
		try {
			mSocket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void sendPacket (String packetContent) {
		byte[] buffer = packetContent.getBytes();
		int packetLength = packetContent.length();
		try {
			DatagramPacket packet = new DatagramPacket(
					buffer,
					0,
					packetLength,
					mServerIp, 
					mPort);
			mSocket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void closeConnection(){
		if (mSocket != null){
			mSocket.close();
		}
	}
}
