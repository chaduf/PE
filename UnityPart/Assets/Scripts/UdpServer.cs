﻿using System.Collections;
using System.Net.Sockets;
using System.Net;
using System.Text;
using System.Collections.Generic;
using System;

public class UdpServer
{
	//private byte[] buffer = new byte[1024];
	private IPEndPoint endPoint = null;
	private Socket serverSocket = null;
	private int port;
	private bool isListening;
	
	//public List<Client> Clients;
	public List<string> commandStack;

	public UdpServer (int serverPort){
		port = serverPort;
		//commandStack = new List<Client> ();
	}
	
	public void Setup ()
	{
		//NotificationMessage ("Setting up server");
		endPoint = new IPEndPoint(IPAddress.Any, port);
		serverSocket = new Socket(AddressFamily.InterNetwork, 
		                          SocketType.Dgram, ProtocolType.Udp);
		commandStack = new List<string> ();
		serverSocket.Bind(endPoint);
	}

	public void ClientConnect()
	{		
		byte[] data;
		int received;

		IPEndPoint clientEndPoint = new IPEndPoint(IPAddress.Any, port);
		EndPoint tmpRemote = (EndPoint)clientEndPoint;

		data = new byte[1024];
		received = serverSocket.ReceiveFrom(data, ref tmpRemote);

		//data = serverSocket.Receive(;

		isListening = true;
		while (true)
		{
			if (serverSocket == null || !isListening){
				break;
			}
			//NotificationMessage("Waiting for request");
			data = new byte[1024];
			received = serverSocket.ReceiveFrom(data, ref tmpRemote);
			//data = serverSocket.Receive (ref clientEndPoint);

			if (received > 0){
				commandStack.Add(System.Text.Encoding.ASCII.GetString(data));
			}
		}
	}


	public void Close()
	{
		//NotificationMessage ("Closing server");
		isListening = false;
		if (serverSocket != null){
			serverSocket.Close();
			serverSocket = null;
		}
		//NotificationMessage ("Server closed");
	}

	public bool getCommand(ref string command){
		if (commandStack.Count > 0){
			command = commandStack[0];
			commandStack.RemoveAt(0);
			return true;
		}
		return false;
	}
//*/
	public static void NotificationMessage(string msg){
		//Debug.Log (msg);
	}
}
