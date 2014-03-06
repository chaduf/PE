using UnityEngine;
using System.Collections;
using System.Threading;

public class CommandHandler : MonoBehaviour {
	public int port;

	protected UdpServer server;
	protected Thread serverThread;

	// Use this for initialization
	void Start () {
		HandlerSetup ();
	}
	
	// Update is called once per frame
	void FixedUpdate () {
		CommandHandle ();
	}

	void connect(){
		server.ClientConnect ();
	}

	protected virtual void CommandHandle () {}

	protected void HandlerSetup () {
		server = new UdpServer (port);
		server.Setup ();

		serverThread = new Thread (new ThreadStart(connect));
		serverThread.Start ();
	}

	void OnDestroy(){
		server.Close ();		
	}
}
