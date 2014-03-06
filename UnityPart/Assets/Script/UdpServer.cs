using System.Collections;
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
		//Clients = new List<Client> ();
		//NotificationMessage ("Starting server");

		serverSocket.Bind(endPoint);
		//serverSocket.Listen (5);
		//serverSocket.BeginAccept (new AsyncCallback(AcceptCallback), null);
	}
/*
	private void AcceptCallback(IAsyncResult AR) {
		Socket clientSocket = serverSocket.EndAccept (AR);
		Client client = new Client (clientSocket);
		Clients.Add (client);
		clientSocket.BeginReceive (buffer, 0, buffer.Length, SocketFlags.None, new AsyncCallback (ReceiveCallback), client);
		serverSocket.BeginAccept(new AsyncCallback(AcceptCallback), null);
	}

	private void ReceiveCallback (IAsyncResult AR){
		Client client = (Client)AR.AsyncState;
		int received = client.Socket.EndReceive(AR);
		byte[] dataBuffer = new byte[received];

		string data = Encoding.ASCII.GetString (dataBuffer);
		client.CommandStack.Add (data);
	}


	public void close(){
		isListening = false;
		foreach (Client client in Clients){
			client.CommandStack.Clear();
			client.Socket.Close();
		}
		Clients.Clear ();
	}
*/
	public void ClientConnect()
	{		
		byte[] data;
		int received;

		IPEndPoint clientEndPoint = new IPEndPoint(IPAddress.Any, port);
		EndPoint tmpRemote = (EndPoint)clientEndPoint;

		data = new byte[1024];
		received = serverSocket.ReceiveFrom(data, ref tmpRemote);
		
		isListening = true;
		while (true)
		{
			if (serverSocket == null || !isListening){
				break;
			}
			//NotificationMessage("Waiting for request");
			data = new byte[1024];
			received = serverSocket.ReceiveFrom(data, ref tmpRemote);


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

			if (commandStack.Count > 0){
				return true;
			}
		}
		return false;
	}
//*/
	public static void NotificationMessage(string msg){
		//Debug.Log (msg);
	}
}
