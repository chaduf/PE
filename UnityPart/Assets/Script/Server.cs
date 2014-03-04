using UnityEngine;
using System.Collections;
using System.Threading;
using System.IO;
using System;
using SimpleJSON;

public class Server : MonoBehaviour {
	public int port;

	UdpServer server;
	Thread serverThread;

	// Use this for initialization
	void Start () {
		server = new UdpServer (port);
		server.Setup ();

		serverThread = new Thread (new ThreadStart(connect));
		serverThread.Start ();
	}

	private void connect() {
		server.ClientConnect();
	}

	// Update is called once per frame
	void Update () {
		string command = null;
		JSONNode jsonCommand;

		if (Input.GetKey (KeyCode.Space)) {
			server.close();		
		}


		while (server.getCommand (ref command)) {
			//Debug.Log (server.commandStack.Count);
			jsonCommand = JSON.Parse(command);
			try{
				this.transform.Rotate(jsonCommand["roll"].AsFloat, jsonCommand["azimuth"].AsFloat, jsonCommand["pitch"].AsFloat);
			} catch (Exception e){
				Debug.Log(e);
			}
		}
	}

	void OnDestroy(){
		server.close ();
		
	}
}
