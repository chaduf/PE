using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;

public class Client {
	public Socket Socket;
	public List<string> CommandStack;

	public Client (Socket clientSocket){
		CommandStack = new List<string> ();
		Socket = clientSocket;
	}
}
